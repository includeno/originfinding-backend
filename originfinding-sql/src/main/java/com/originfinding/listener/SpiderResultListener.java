package com.originfinding.listener;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.originfinding.algorithm.WordPair;
import com.originfinding.algorithm.ansj.AnsjSimHash;
import com.originfinding.config.KafkaTopic;
import com.originfinding.config.RedisKey;
import com.originfinding.entity.SimRecord;
import com.originfinding.entity.SpiderRecord;
import com.originfinding.entity.UrlRecord;
import com.originfinding.listener.message.PairTaskMessage;
import com.originfinding.message.SpiderResultMessage;
import com.originfinding.service.sql.SimRecordService;
import com.originfinding.service.sql.SpiderRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Configuration
public class SpiderResultListener {

    @Autowired
    private SimRecordService simRecordService;

    @Autowired
    private SpiderRecordService spiderRecordService;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private Gson gson;

    @KafkaListener(
            id = "BatchSpiderResultConsumer",
            topics = KafkaTopic.spiderresult,
            containerFactory = "batchFactory",
            properties={
                    "max.poll.interval.ms:60000",
                    "max.poll.records:300"
            }
    )
    public void batchListenSpiderResult(List<ConsumerRecord<String, String>> list){
        log.info("SpiderResultConsumer receive:" + list.size());
        List<String> messages=list.stream().map(a->a.value()).collect(Collectors.toList());
        log.warn("SpiderResultConsumer RAW:"+gson.toJson(messages));

    }


    //@KafkaListener(id = "SpiderResultConsumer", topics = KafkaTopic.spiderresult,containerFactory = "")
    public void listenSpiderResult(String message) throws Exception {
        log.info("SpiderResultConsumer receive:" + message);
        SpiderResultMessage spiderResultMessage = null;
        try {
            spiderResultMessage = gson.fromJson(message, SpiderResultMessage.class);
            log.warn("spiderRecord:" + gson.toJson(spiderResultMessage));
        } catch (Exception e) {
            spiderResultMessage = null;
        }

        if (spiderResultMessage == null) {
            return;
        }
        String url = spiderResultMessage.getUrl();
        saveSpiderRecord(spiderResultMessage);

        if (spiderResultMessage.getValid().equals(0)) {
            QueryWrapper<SimRecord> queryWrapper = new QueryWrapper();
            queryWrapper.eq("url", url);
            SimRecord temp = simRecordService.getOne(queryWrapper);
            temp.setValid(0);
            simRecordService.updateById(temp);
            log.warn("invalid url:" + url + " " + gson.toJson(spiderResultMessage));
            return;
        }

        //TODO redis 判断记录存在=>url转化为相同长度的hash值
        SimRecord temp = saveSimRecord(spiderResultMessage);
        //步骤5 数据库操作成功
        if (temp != null) {
            //发送pair处理请求
            PairTaskMessage pairTaskMessage = PairTaskMessage.fromSimRecord(temp);
            //步骤6 任务添加至sparktask队列
            kafkaTemplate.send(KafkaTopic.sparkPairAnalyze, gson.toJson(pairTaskMessage)).addCallback(new SuccessCallback() {
                @Override
                public void onSuccess(Object o) {
                    log.info("PairTaskMessage send success " + url + " " + gson.toJson(pairTaskMessage));
                    //爬虫任务成功后再更新Redis内缓存数据
                    stringRedisTemplate.opsForValue().set(RedisKey.spiderKey(url), gson.toJson(new Date()), Duration.ofHours(7 * 24));
                }
            }, new FailureCallback() {
                @Override
                public void onFailure(Throwable throwable) {
                    log.error("PairTaskMessage send error " + url + " " + throwable.getMessage());
                }
            });

            kafkaTemplate.flush();
        } else {
            log.error("CommonpageConsumer 数据库操作失败");
            throw new Exception("数据库操作失败");
        }

    }


    //批量写入

    private SimRecord saveSimRecord(SpiderResultMessage res) {
        Date date = new Date();
        boolean operation = false;
        SimRecord temp = null;
        QueryWrapper<SimRecord> queryWrapper = new QueryWrapper();
        queryWrapper.eq("url", res.getUrl());
        temp = simRecordService.getOne(queryWrapper);
        log.info("getOne " + gson.toJson(temp));
        if (temp != null) {
            temp.setUrl(res.getUrl());
            temp.setTime(res.getTime());
            temp.setView(res.getView());

            temp.setSimhash(res.getSimhash());
            temp.setUpdateTime(date);
            operation = simRecordService.update(temp, queryWrapper);//按照url更新
            log.info("simRecordService.update: " + operation);
        } else {
            temp = new SimRecord();

            temp.setUrl(res.getUrl());
            temp.setTime(res.getTime());
            temp.setView(res.getView());

            temp.setSimhash(res.getSimhash());
            temp.setCreateTime(date);
            temp.setUpdateTime(date);
            //默认原创 未找到关联的原创文章
            temp.setSimparentId(-1);
            temp.setEarlyparentId(-1);
            operation = simRecordService.save(temp);
            log.info("simRecordService.save: " + operation);
        }

        String simRecordId = stringRedisTemplate.opsForValue().get(RedisKey.simRecordKey(res.getUrl()));
        if (simRecordId != null) {
            Integer id = Integer.parseInt(simRecordId);
            temp = simRecordService.getById(id);
        } else {
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("url", res.getUrl());
            temp = simRecordService.getOne(queryWrapper);
            stringRedisTemplate.opsForValue().set(RedisKey.simRecordKey(res.getUrl()), temp.getId().toString());
        }
        return temp;
    }

    public void saveSpiderRecord(SpiderResultMessage res) {
        Date date = new Date();
        //插入当前url的爬虫记录
        SpiderRecord spiderRecord = new SpiderRecord();
        spiderRecord.setUrl(res.getUrl());

        spiderRecord.setTag(res.getTag());
        spiderRecord.setTitle(res.getTitle());
        spiderRecord.setContent(res.getContent());
        spiderRecord.setView(res.getView());
        spiderRecord.setTime(res.getTime());

        spiderRecord.setCreateTime(date);
        spiderRecord.setUpdateTime(date);
        spiderRecord.setValid(1);
        if (res.getContent() == null || res.getTitle() == null || res.getContent().length() == 0 || res.getTitle().length() == 0) {
            spiderRecord.setValid(0);//无效文本
        } else if (res.getContent().length() < 150) {
            spiderRecord.setValid(2);//短文本
        }
        boolean op = spiderRecordService.save(spiderRecord);
        log.info("spiderRecordService.save: " + op + " valid:" + spiderRecord.getValid());
    }
}
