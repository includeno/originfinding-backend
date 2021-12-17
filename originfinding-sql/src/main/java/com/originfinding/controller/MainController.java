package com.originfinding.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.originfinding.config.KafkaTopic;
import com.originfinding.entity.SimRecord;
import com.originfinding.request.QueryRequest;
import com.originfinding.request.SubmitRequest;
import com.originfinding.response.QueryResponse;
import com.originfinding.response.SubmitResponse;
import com.originfinding.service.SimRecordService;
import com.originfinding.util.UrlFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
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
        List<String> list = request.getList().stream().distinct().collect(Collectors.toList());//url过滤重复url
        log.info("/submit begin filter"+gson.toJson(list));
        List<String> ans = UrlFilter.filter(list);//筛选出符合条件的URl
        log.info("/submit end filter"+gson.toJson(ans));

        SubmitResponse response=new SubmitResponse();
        List<SubmitResponse.SubmitResponseEntity> answer = new ArrayList<>();
        for (String url : ans) {
            SubmitResponse.SubmitResponseEntity entity=new SubmitResponse.SubmitResponseEntity();
            //redis判断此url在周期内是否存在 不存在则发送消息，存在则立即返回
            String res = stringRedisTemplate.opsForValue().get(url);
            if (res!=null&&!res.equals("")) {
                //TODO redis 周期内存在记录 读取redis内数据
                entity=gson.fromJson(res,SubmitResponse.SubmitResponseEntity.class);
                answer.add(entity);
            }
            else {
                //redis 周期内不存在记录
                //提交spark处理
                kafkaTemplate.send(KafkaTopic.commonpage, url);

                //读取数据库
                QueryWrapper<SimRecord> queryWrapper = new QueryWrapper();
                SimRecord simRecord = simRecordService.getOne(queryWrapper);
                //数据库中不存在
                if (simRecord == null) {
                    log.info("/submit simRecord == null "+url);
                    entity.setUrl(url);
                    answer.add(entity);
                } else {
                    log.info("/submit simRecord != null "+url);
                    //补充数据库数据
                    entity.setUrl(url);
                    entity.setSim3(simRecord.getSim3());
                    entity.setSim4(simRecord.getSim4());
                    entity.setSim5(simRecord.getSim5());
                    entity.setUpdateTime(simRecord.getUpdateTime());
                    if(simRecord.getParentId().equals(-1)){
                        entity.setParentUrl("");
                    }
                    else{

                        //查询关联数据
                        QueryWrapper<SimRecord> parentQuery = new QueryWrapper();
                        SimRecord parent = simRecordService.getOne(parentQuery);
                        if(parent==null){
                            log.info("/submit parent doesn't exist "+url);
                            entity.setParentUrl("");
                        }
                        else{
                            log.info("/submit parent exists "+url);
                            entity.setParentUrl(parent.getUrl());
                        }
                    }
                    //TODO redis内添加周期数据
                    stringRedisTemplate.opsForValue().set(url,gson.toJson(entity));
                    log.info("redis set!");
                    answer.add(entity);
                }
            }
        }
        log.info("/submit response:"+gson.toJson(response));
        response.setList(answer);
        return response;
    }
}
