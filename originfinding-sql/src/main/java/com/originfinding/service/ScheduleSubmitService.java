package com.originfinding.service;

import com.google.gson.Gson;
import com.originfinding.config.KafkaTopic;
import com.originfinding.config.RedisKey;

import com.originfinding.request.SubmitRequest;
import com.originfinding.response.SubmitResponse;
import com.originfinding.util.UrlFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ScheduleSubmitService {

    @Autowired
    KafkaTemplate kafkaTemplate;

    @Autowired
    Gson gson;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //向线程池提交请求 尽最大努力完成
    public void scheduleSubmitFunction(SubmitRequest request){
        HashSet<String> set=new HashSet<>();
        set.addAll(request.getList());
        List<String> list = set.stream().distinct().collect(Collectors.toList());//url过滤重复url
        log.info("/schedule/submit begin filter"+gson.toJson(list));
        List<String> ans = null;//筛选出符合条件的URl
        try {
            ans = UrlFilter.filter(list);
        } catch (NoSuchMethodException e) {
            log.error("NoSuchMethodException "+e.getMessage());
            return;
        }
        log.info("/schedule/submit end filter"+gson.toJson(ans));

        for (String url : ans) {
            SubmitResponse.SubmitResponseEntity entity=new SubmitResponse.SubmitResponseEntity();

            String res=stringRedisTemplate.opsForValue().get(RedisKey.spiderKey(url));

            if (res!=null&&!res.equals("")) {
                //除非找到新的链接否则不进行Spark分析
                continue;
            }
            else {
                log.info("/schedule/submit redis record doesn't exist "+url);
                stringRedisTemplate.opsForValue().set(RedisKey.spiderKey(url),gson.toJson(entity), Duration.ofHours(7*24));
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
                entity.setUrl(url);
                entity.setUpdateTime(null);//标记为未处理状态
                stringRedisTemplate.opsForValue().set(RedisKey.responseKey(url),gson.toJson(entity), Duration.ofHours(7*24));//首次处理时过期时间设置短一些
            }
        }
        kafkaTemplate.flush();
    }
}
