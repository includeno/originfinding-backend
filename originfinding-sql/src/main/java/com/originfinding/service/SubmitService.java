package com.originfinding.service;

import com.google.gson.Gson;
import com.originfinding.config.KafkaTopic;
import com.originfinding.config.RedisKey;
import com.originfinding.request.SubmitRequest;
import com.originfinding.response.SubmitResponse;
import com.originfinding.service.sql.SimRecordService;
import com.originfinding.util.UrlFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SubmitService {

    @Autowired
    KafkaTemplate kafkaTemplate;

    @Autowired
    SimRecordService simRecordService;

    @Autowired
    Gson gson;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public List<SubmitResponse.SubmitResponseEntity> submitFunction(SubmitRequest request){
        HashSet<String> set=new HashSet<>();
        set.addAll(request.getList());
        List<String> list = set.stream().distinct().collect(Collectors.toList());//url过滤重复url
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
            //响应缓存
            String responseValue =stringRedisTemplate.opsForValue().get(RedisKey.responseKey(url));
            //响应缓存不存在
            if(responseValue==null){
                //响应缓存设置临时值
                entity.setUrl(url);
                entity.setUpdateTime(null);//标记为未处理状态
                stringRedisTemplate.opsForValue().set(RedisKey.responseKey(url),gson.toJson(entity), Duration.ofHours(7*24));
                //爬虫缓存
                String spiderValue =stringRedisTemplate.opsForValue().get(RedisKey.spiderKey(url));
                //爬虫缓存不存在
                if(spiderValue==null){
                    stringRedisTemplate.opsForValue().set(RedisKey.spiderKey(url),"", Duration.ofHours(7*24));
                    //发送爬虫请求
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
                }
                else{
                    String updateValue =stringRedisTemplate.opsForValue().get(RedisKey.updateKey(url));
                    if(updateValue!=null){
                        continue;
                    }
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
                    stringRedisTemplate.opsForValue().set(RedisKey.spiderKey(url),"", Duration.ofHours(4));
                }
            }
            else{
                entity=gson.fromJson(responseValue,SubmitResponse.SubmitResponseEntity.class);
                answer.add(entity);
            }
        }
        return answer;
    }
}
