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
import com.originfinding.service.ScheduleSubmitService;
import com.originfinding.service.SubmitService;
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
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/origin")
public class MainController {
    @Autowired
    SubmitService submitService;

    @Autowired
    ScheduleSubmitService scheduleSubmitService;

    @Autowired
    Gson gson;

    //TODO 查询URL原创度情况 查询redis内存在的记录 不存在则 查询数据库内存在的记录 不发送kafka消息
    @GetMapping("/query")
    public QueryResponse queryOne(QueryRequest request){
        QueryResponse queryResponse=new QueryResponse();

        return queryResponse;
    }

    //submit 提交接口
    @PostMapping("/submit")
    public SubmitResponse submit(SubmitRequest request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<SubmitResponse.SubmitResponseEntity> answer=submitService.submitFunction(request);
        SubmitResponse response=new SubmitResponse();
        response.setList(answer);
        log.info("/submit response:"+gson.toJson(response));
        return response;
    }

    //schedule submit 定时提交接口 无返回值
    @PostMapping("/schedule/submit")
    public String submitSchedule(SubmitRequest request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        scheduleSubmitService.scheduleSubmitFunction(request);
        return "ok";
    }

}
