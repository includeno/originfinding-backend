package com.originfinding.listener;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.originfinding.config.KafkaTopic;
import com.originfinding.entity.SimRecord;
import com.originfinding.entity.SpiderRecord;
import com.originfinding.entity.UrlRecord;
import com.originfinding.listener.message.SparkResultMessage;
import com.originfinding.listener.message.SparkTaskMessage;
import com.originfinding.service.SimRecordService;
import com.originfinding.service.SpiderRecordService;
import com.originfinding.service.feign.SpiderService;
import com.originfinding.util.SimHash;
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
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class MyKafkaListener {
    @Autowired
    SpiderService api;

    @Autowired
    SimRecordService simRecordService;

    @Autowired
    SpiderRecordService spiderRecordService;

    @Autowired
    KafkaTemplate kafkaTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    RBloomFilter<String> bloomFilter;

    @Autowired
    Gson gson;

    @Value("${spring.cloud.consul.discovery.instance-id}")
    String instance_id;

    @Transactional(rollbackFor = Exception.class)
    @KafkaListener(id = "CommonpageConsumer", topics = KafkaTopic.commonpage)
    public void listenCommonpage(String message) throws Exception {
        log.info("CommonpageConsumer receive:" + message);
        //监听controller发送的消息 message=url
        String url = message;

        //远程调用spider爬虫服务
        UrlRecord res = api.crawl(url);
        if (res == null) {
            log.error("error: 爬虫服务出错");
            return;
        } else if (res.getContent() == null) {
            throw new Exception("爬虫服务处于暂时休息中");
        } else if (res.getContent().length() == 0 || res.getTitle().length() == 0) {
            log.error("error: 爬虫服务无法爬取此网页，请稍后重试" + gson.toJson(res));
            return;
        }
        log.info("spider crawl:"+gson.toJson(res));
        Date date = new Date();
        //保存当前url的爬虫记录
        QueryWrapper<SpiderRecord> spiderQueryWrapper = new QueryWrapper();
        spiderQueryWrapper.eq("url", url);
        SpiderRecord spiderRecord = spiderRecordService.getOne(spiderQueryWrapper);
        log.info("spiderRecordService.getOne: "+gson.toJson(spiderRecord));
        if (spiderRecord == null) {
            //无记录
            spiderRecord = new SpiderRecord();
            spiderRecord.setUrl(url);

            spiderRecord.setTag(res.getTag());
            spiderRecord.setTitle(res.getTitle());
            spiderRecord.setContent(res.getContent());
            spiderRecord.setTime(res.getTime());

            spiderRecord.setCreateTime(date);
            spiderRecord.setUpdateTime(date);
            boolean op=spiderRecordService.save(spiderRecord);
            log.info("spiderRecordService.save: "+op);
        } else {
            spiderRecord.setTag(res.getTag());
            spiderRecord.setContent(res.getContent());
            spiderRecord.setTime(res.getTime());

            spiderRecord.setUpdateTime(date);
            boolean op=spiderRecordService.updateById(spiderRecord);
            log.info("spiderRecordService.updateById: "+op);
        }

        //计算simhash 64位长度
        SimRecord temp = null;
        SimHash simHash = new SimHash(res.getContent(), 64);

        //TODO redis 判断记录存在=>url转化为相同长度的hash值
        //判断url存在于布隆过滤器中 很可能MySQL中
        boolean exist = bloomFilter.contains(url);
        boolean operation = false;

        if (exist) {
            log.info("bloomFilter exists ");
            //已存在记录
            QueryWrapper<SimRecord> queryWrapper = new QueryWrapper();
            queryWrapper.eq("url", url);
            temp = new SimRecord();
            temp.setUrl(url);
            temp.setParentId(-1);//默认原创 未找到关联的原创文章

            temp.setUrl(res.getUrl());
            temp.setTitle(res.getTitle());
            temp.setTag(res.getTag());
            temp.setTime(res.getTime());

            temp.setSimhash(simHash.getStrSimHash());
            temp.setUpdateTime(date);

            operation = simRecordService.update(temp, queryWrapper);//按照url更新
        } else {
            log.info("bloomFilter doesn't exist ");
            QueryWrapper<SimRecord> queryWrapper = new QueryWrapper();
            queryWrapper.eq("url", url);
            temp = simRecordService.getOne(queryWrapper);
            log.info("simRecordService.getOne(queryWrapper) " + gson.toJson(temp));
            if (temp == null) {
                log.warn("未查询到 已知url的SimRecord记录");
                //不存在记录
                temp = new SimRecord();

                temp.setUrl(res.getUrl());
                temp.setTitle(res.getTitle());
                temp.setTag(res.getTag());
                temp.setTime(res.getTime());

                temp.setSimhash(simHash.getStrSimHash());
                temp.setCreateTime(date);
                temp.setUpdateTime(date);

                temp.setParentId(-1);//默认原创 未找到关联的原创文章
                operation = simRecordService.save(temp);
            } else {
                log.warn("查询到 已知url的SimRecord记录");
                //测试环境下 布隆过滤器中未出现
                temp.setParentId(-1);//默认原创 未找到关联的原创文章

                temp.setUrl(res.getUrl());
                temp.setTitle(res.getTitle());
                temp.setTag(res.getTag());
                temp.setTime(res.getTime());

                temp.setSimhash(simHash.getStrSimHash());
                temp.setUpdateTime(date);

                operation = simRecordService.updateById(temp);
            }
        }
        //数据库操作成功
        if (operation == true) {
            //数据库保存记录
            log.info("CommonpageConsumer db:" + gson.toJson(temp));
            //数据库查id
            QueryWrapper<SimRecord> queryWrapper = new QueryWrapper();
            queryWrapper.eq("url", url);
            SimRecord record = simRecordService.getOne(queryWrapper);
            log.info("CommonpageConsumer getOne:" + gson.toJson(record));

            SparkTaskMessage sparkTaskMessage = SparkTaskMessage.fromSimRecord(record, res.getContent());

            //任务添加至队列
            kafkaTemplate.send(KafkaTopic.sparktask, gson.toJson(sparkTaskMessage)).addCallback(new SuccessCallback() {
                @Override
                public void onSuccess(Object o) {
                    log.info("sparktask send success "+url);
                }
            }, new FailureCallback() {
                @Override
                public void onFailure(Throwable throwable) {
                    log.error("sparktask send error "+url+" "+throwable.getMessage());
                }
            });
            kafkaTemplate.flush();
            //其余操作成功后添加至布隆过滤器
            if (!bloomFilter.contains(url)) {
                bloomFilter.add(url);
            }
        } else {
            log.error("CommonpageConsumer 数据库操作失败");
            throw new Exception("数据库操作失败");
        }

    }

    @KafkaListener(id = "QueueConsumer", topics = KafkaTopic.queue, containerFactory = "batchFactory")
    public void listenQueue(List<ConsumerRecord<String, String>> list) {
        log.info("QueueConsumer receive :" + list.size());
        //分词
        for (ConsumerRecord<String, String> temp : list) {
            log.info(temp.value());
            SparkTaskMessage message = gson.fromJson(temp.value(), SparkTaskMessage.class);


        }

    }


    //@KafkaListener(id = "SparktaskConsumer", topics = KafkaTopic.sparktask)
    public void listenTask(String message) {
        log.info("SparktaskConsumer receive :" + message);
        SparkTaskMessage sparkTaskMessage = gson.fromJson(message, SparkTaskMessage.class);


    }

    @KafkaListener(id = "SparkResultConsumer", topics = KafkaTopic.sparkresult)
    public void listenTaskResult(String message) {
        log.info("SparkResultConsumer receive :" + message);
        SparkResultMessage sparkResultMessage = gson.fromJson(message, SparkResultMessage.class);

    }
}
