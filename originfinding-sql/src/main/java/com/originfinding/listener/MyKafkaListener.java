package com.originfinding.listener;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.originfinding.config.KafkaTopic;
import com.originfinding.entity.SimRecord;
import com.originfinding.entity.UrlRecord;
import com.originfinding.service.SimRecordService;
import com.originfinding.service.feign.SpiderService;
import com.originfinding.util.SimHash;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class MyKafkaListener {
    @Autowired
    SpiderService api;

    @Autowired
    SimRecordService simRecordService;

    @Autowired
    KafkaTemplate kafkaTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    Gson gson;


    @Transactional(rollbackFor = Exception.class)
    @KafkaListener(id = "CommonpageConsumer", topics = KafkaTopic.commonpage)
    public void listenCommonpage(String message) {
        log.info("CommonpageConsumer receive:"+message);
        //监听controller发送的消息 message=url
        String url = message;

        //远程调用spider爬虫服务
        UrlRecord res = api.crawl(url);
        if (res == null) {
            log.error("error: 爬虫服务出错");
            return;
        }
        if (res.getContent().length() == 0 || res.getTitle().length() == 0) {
            log.error("error: 爬虫服务无法爬取此网页" + gson.toJson(res));
            return;
        }

        SimRecord simRecord = new SimRecord();
        SimRecord temp = null;

        //计算simhash 64位长度
        SimHash simHash = new SimHash(res.getContent(), 64);
        //TODO redis 判断记录存在=>url转化为相同长度的hash值
        if (stringRedisTemplate.opsForValue().get("CommonpageConsumer:"+url)==null) {
            //已存在记录
            QueryWrapper<SimRecord> queryWrapper = new QueryWrapper();
            queryWrapper.eq("url",url);
            temp = simRecordService.getOne(queryWrapper);
            temp.setCreateTime(new Date());
            temp.setParentId(-1);//默认原创 未找到关联的原创文章

            temp.setUrl(res.getUrl());
            temp.setTitle(res.getTitle());
            temp.setTime(res.getTime());
            temp.setUpdateTime(new Date());
            temp.setSimhash(simHash.getStrSimHash());
            simRecordService.updateById(temp);
        } else {
            stringRedisTemplate.opsForValue().set("CommonpageConsumer:"+url,"new",1, TimeUnit.MINUTES);
            //不存在记录
            temp = simRecord;
            temp.setCreateTime(new Date());
            temp.setParentId(-1);//默认原创 未找到关联的原创文章

            temp.setUrl(res.getUrl());
            temp.setTitle(res.getTitle());
            temp.setTime(res.getTime());
            temp.setUpdateTime(new Date());
            temp.setSimhash(simHash.getStrSimHash());
            simRecordService.save(temp);
        }

        //数据库保存记录
        log.info("CommonpageConsumer db:"+gson.toJson(temp));

        //数据库查id
        QueryWrapper<SimRecord> queryWrapper = new QueryWrapper();
        queryWrapper.eq("url",url);
        SimRecord record = simRecordService.getOne(queryWrapper);
        //任务添加至队列
        kafkaTemplate.send(KafkaTopic.queue, record.getId().toString());
    }


    //TODO 一次读取n条记录 批量消费
    @KafkaListener(id = "QueueConsumer", topics = KafkaTopic.queue)
    public void listenQueue(String message) {

        log.info("QueueConsumer receive id:"+message);
    }


    //@KafkaListener(id = "TaskConsumer",topics = KafkaTopic.task)
    public void listenTask(String message) {

    }
}
