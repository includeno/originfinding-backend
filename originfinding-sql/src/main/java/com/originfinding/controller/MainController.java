package com.originfinding.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.originfinding.config.KafkaTopic;
import com.originfinding.config.RedisKey;
import com.originfinding.entity.SimRecord;
import com.originfinding.request.QueryRequest;
import com.originfinding.request.SubmitRequest;
import com.originfinding.response.QueryResponse;
import com.originfinding.response.SubmitResponse;
import com.originfinding.service.sql.SimRecordService;
import com.originfinding.util.UrlFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/origin")
public class MainController {
    @Autowired
    KafkaTemplate kafkaTemplate;

    @Autowired
    SimRecordService simRecordService;

    @Autowired
    Gson gson;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //TODO 查询URL原创度情况 查询redis内存在的记录 不存在则 查询数据库内存在的记录 不发送kafka消息
    @GetMapping("query")
    public QueryResponse queryOne(QueryRequest request){
        QueryResponse queryResponse=new QueryResponse();

        return queryResponse;
    }

    //submit 提交批处理请求
    @PostMapping("/submit")
    public SubmitResponse submit(SubmitRequest request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<SubmitResponse.SubmitResponseEntity> answer=submitFunction(request,Boolean.FALSE);
        SubmitResponse response=new SubmitResponse();
        response.setList(answer);
        log.info("/submit response:"+gson.toJson(response));
        return response;
    }

    //schedule submit 提交批处理请求，省略返回值
    @PostMapping("/schedule/submit")
    public String submitSchedule(SubmitRequest request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        updateFunction(request,Boolean.FALSE);
        return "ok";
    }

    //schedule submit 提交批处理请求，省略返回值
    @PostMapping("/test/schedule/submit")
    public String testSubmitSchedule(SubmitRequest request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        submitFunction(request,Boolean.TRUE);
        return "ok";
    }

    //submit 提交批处理请求
    @PostMapping("/test/submit")
    public SubmitResponse submitForTest(SubmitRequest request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<SubmitResponse.SubmitResponseEntity> answer=submitFunction(request,Boolean.TRUE);
        SubmitResponse response=new SubmitResponse();
        response.setList(answer);
        log.info("/submit response:"+gson.toJson(response));
        return response;
    }


    public List<SubmitResponse.SubmitResponseEntity> submitFunction(SubmitRequest request,Boolean skipRedis){
        List<String> list = request.getList().stream().distinct().collect(Collectors.toList());//url过滤重复url
        log.info("/submit begin filter"+gson.toJson(list));
        List<String> ans = null;//筛选出符合条件的URl
        try {
            ans = UrlFilter.filter(list);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        log.info("/submit end filter"+gson.toJson(ans));

        List<SubmitResponse.SubmitResponseEntity> answer = new ArrayList<>();
        Date now=new Date();
        for (String url : ans) {
            SubmitResponse.SubmitResponseEntity entity=new SubmitResponse.SubmitResponseEntity();
            //redis判断url爬虫记录存在
            String spiderValue =stringRedisTemplate.opsForValue().get(RedisKey.spiderKey(url));
            if (spiderValue!=null&&!spiderValue.equals("")) {
                log.info("/submit redis spider record exists "+url);
                entity=gson.fromJson(spiderValue,SubmitResponse.SubmitResponseEntity.class);
                answer.add(entity);
                //发送更新请求
                kafkaTemplate.send(KafkaTopic.updateSpark, url).addCallback(new SuccessCallback() {
                    @Override
                    public void onSuccess(Object o) {
                        log.info("spidertask send success "+url);
                    }
                }, new FailureCallback() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        log.error("updateRedis send error "+url+" "+throwable.getMessage());
                    }
                });
            }
            else {
                log.info("/submit redis spider record doesn't exist "+url);
                stringRedisTemplate.opsForValue().set(RedisKey.responseKey(url),"", Duration.ofHours(7*24));
                stringRedisTemplate.opsForValue().set(RedisKey.spiderKey(url),gson.toJson(new Date()), Duration.ofHours(7*24));

                //提交spark处理
                kafkaTemplate.send(KafkaTopic.spidertask, url).addCallback(new SuccessCallback() {
                    @Override
                    public void onSuccess(Object o) {
                        log.info("spidertask send success "+url);
                    }
                }, new FailureCallback() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        log.error("spidertask send error "+url+" "+throwable.getMessage());
                    }
                });

                //读取数据库
                QueryWrapper<SimRecord> queryWrapper = new QueryWrapper();
                queryWrapper.eq("url",url);
                SimRecord simRecord = simRecordService.getOne(queryWrapper);
                //数据库中不存在
                if (simRecord == null) {
                    log.info("/submit simRecord == null "+url);
                    entity.setUrl(url);
                    entity.setUpdateTime(null);//标记为未处理状态

                    stringRedisTemplate.opsForValue().set(RedisKey.responseKey(url),gson.toJson(entity), Duration.ofHours(7*24));//首次处理时过期时间设置短一些
                    answer.add(entity);
                } else {
                    log.info("/submit simRecord != null "+url);
                    //补充数据库数据
                    entity.setUrl(url);
                    entity.setSim3(simRecord.getSimlevelfirst());
                    entity.setSim4(simRecord.getSimlevelsecond());

                    entity.setUpdateTime(simRecord.getUpdateTime());//此url已处理过并且有记录 已提交新的处理
                    //simparentId
                    if(!simRecord.getSimparentId().equals(-1)){
                        //查询关联数据
                        QueryWrapper<SimRecord> parentQuery = new QueryWrapper();
                        parentQuery.eq("id",simRecord.getSimparentId());
                        SimRecord parent = simRecordService.getOne(parentQuery);
                        if(parent==null){
                            log.info("/submit earlyparentId doesn't exist "+url);
                            entity.setSimparentUrl("");
                        }
                        else{
                            log.info("/submit earlyparentId exists "+url);
                            entity.setSimparentUrl(parent.getUrl());
                        }
                    }
                    //earlyparentId
                    if(!simRecord.getEarlyparentId().equals(-1)){
                        //查询关联数据
                        QueryWrapper<SimRecord> parentQuery = new QueryWrapper();
                        parentQuery.eq("id",simRecord.getEarlyparentId());
                        SimRecord parent = simRecordService.getOne(parentQuery);
                        if(parent==null){
                            log.info("/submit earlyparentId doesn't exist "+url);
                            entity.setEarlyparentUrl("");
                        }
                        else{
                            log.info("/submit earlyparentId exists "+url);
                            entity.setEarlyparentUrl(parent.getUrl());
                        }
                    }

                    stringRedisTemplate.opsForValue().set(RedisKey.responseKey(url),gson.toJson(entity), Duration.ofHours(7*24));//更新时过期时间设置长一些
                    answer.add(entity);
                }
            }
        }

        return answer;
    }

    public void updateFunction(SubmitRequest request,Boolean skipRedis){
        List<String> list = request.getList().stream().distinct().collect(Collectors.toList());//url过滤重复url
        log.info("/submit begin filter"+gson.toJson(list));
        List<String> ans = null;//筛选出符合条件的URl
        try {
            ans = UrlFilter.filter(list);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        log.info("/submit end filter"+gson.toJson(ans));

        for (String url : ans) {
            SubmitResponse.SubmitResponseEntity entity=new SubmitResponse.SubmitResponseEntity();

            String res=stringRedisTemplate.opsForValue().get(RedisKey.spiderKey(url));

            if (res!=null&&!res.equals("")) {
                //除非找到新的链接否则不进行Spark分析
                continue;
            }
            else {
                log.info("/submit redis record doesn't exist "+url);
                //TODO redis内添加周期数据
                stringRedisTemplate.opsForValue().set(url,gson.toJson(entity), Duration.ofHours(7*24));
                //redis 周期内不存在记录
                //提交spark处理
                kafkaTemplate.send(KafkaTopic.spidertask, url).addCallback(new SuccessCallback() {
                    @Override
                    public void onSuccess(Object o) {
                        log.info("spidertask send success "+url);
                    }
                }, new FailureCallback() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        log.error("spidertask send error "+url+" "+throwable.getMessage());
                    }
                });


                //读取数据库
                QueryWrapper<SimRecord> queryWrapper = new QueryWrapper();
                queryWrapper.eq("url",url);
                SimRecord simRecord = simRecordService.getOne(queryWrapper);
                //数据库中不存在
                if (simRecord == null) {
                    log.info("/submit simRecord == null "+url);
                    entity.setUrl(url);
                    entity.setUpdateTime(null);//标记为未处理状态
                    stringRedisTemplate.opsForValue().set(url,gson.toJson(entity), Duration.ofHours(7*24));//首次处理时过期时间设置短一些
                } else {
                    log.info("/submit simRecord != null "+url);
                    //补充数据库数据
                    entity.setUrl(url);
                    entity.setSim3(simRecord.getSimlevelfirst());
                    entity.setSim4(simRecord.getSimlevelsecond());

                    entity.setUpdateTime(simRecord.getUpdateTime());//此url已处理过并且有记录 已提交新的处理
                    //simparentId
                    if(!simRecord.getSimparentId().equals(-1)){
                        //查询关联数据
                        QueryWrapper<SimRecord> parentQuery = new QueryWrapper();
                        parentQuery.eq("id",simRecord.getSimparentId());
                        SimRecord parent = simRecordService.getOne(parentQuery);
                        if(parent==null){
                            log.info("/submit earlyparentId doesn't exist "+url);
                            entity.setSimparentUrl("");
                        }
                        else{
                            log.info("/submit earlyparentId exists "+url);
                            entity.setSimparentUrl(parent.getUrl());
                        }
                    }
                    //earlyparentId
                    if(!simRecord.getEarlyparentId().equals(-1)){
                        //查询关联数据
                        QueryWrapper<SimRecord> parentQuery = new QueryWrapper();
                        parentQuery.eq("id",simRecord.getEarlyparentId());
                        SimRecord parent = simRecordService.getOne(parentQuery);
                        if(parent==null){
                            log.info("/submit earlyparentId doesn't exist "+url);
                            entity.setEarlyparentUrl("");
                        }
                        else{
                            log.info("/submit earlyparentId exists "+url);
                            entity.setEarlyparentUrl(parent.getUrl());
                        }
                    }
                    stringRedisTemplate.opsForValue().set(url,gson.toJson(entity), Duration.ofHours(7*24));//更新时过期时间设置长一些
                }
            }
        }
        kafkaTemplate.flush();
    }
}
