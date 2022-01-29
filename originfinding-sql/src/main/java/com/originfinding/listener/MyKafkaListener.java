package com.originfinding.listener;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.originfinding.algorithm.WordPair;
import com.originfinding.algorithm.ansj.AnsjSimHash;
import com.originfinding.config.KafkaTopic;
import com.originfinding.entity.SimRecord;
import com.originfinding.entity.SpiderRecord;
import com.originfinding.entity.UrlRecord;
import com.originfinding.enums.SpiderCode;
import com.originfinding.listener.message.SparkLdaMessage;
import com.originfinding.listener.message.SparkTaskMessage;
import com.originfinding.listener.message.SparkTfidfTaskMessage;
import com.originfinding.response.SpiderResponse;
import com.originfinding.service.sql.SimRecordService;
import com.originfinding.service.sql.SpiderRecordService;
import com.originfinding.service.feign.SpiderService;
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

import java.util.*;
import java.util.stream.Collectors;

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

        //步骤1 远程调用spider爬虫服务
        SpiderResponse response = api.crawl(url);
        if (response == null) {
            log.error("error: 爬虫服务出错");
            return;
        } else if (response.getCode().equals(SpiderCode.SPIDER_COUNT_LIMIT.getCode())) {
            throw new Exception("爬虫服务处于暂时休息中");
        } else if (response.getCode().equals(SpiderCode.SPIDER_UNREACHABLE.getCode())) {
            log.error("error: 爬虫服务无法爬取此网页，请稍后重试" + gson.toJson(response));
            return;
        }
        log.info("spider crawl:" + gson.toJson(response));

        UrlRecord record= response.getRecord();
        //步骤2 保存当前url的爬虫记录
        saveSpiderRecord(record);

        //步骤3 计算simhash 64位长度
        AnsjSimHash titleAnsjSimHash = new AnsjSimHash(record.getTitle());
        AnsjSimHash contentAnsjSimHash = new AnsjSimHash(record.getContent());

        //步骤4 获取标签
        List<WordPair> tagWordPair = countTags(titleAnsjSimHash, contentAnsjSimHash, record);
        List<String> tags = tagWordPair.stream().map(pair -> pair.getWord()).collect(Collectors.toList());
        String tagString = gson.toJson(tags);
        String simhash = contentAnsjSimHash.getStrSimHash().toString();

        //TODO redis 判断记录存在=>url转化为相同长度的hash值
        SimRecord temp = saveSimRecord(record, tagString, simhash);
        //步骤5 数据库操作成功
        if (temp != null) {
            log.info("CommonpageConsumer db:" + gson.toJson(temp));
            //根据url查询数据库 因为不知道是更新还是
            SparkTaskMessage sparkTaskMessage = SparkTaskMessage.fromSimRecord(temp);
            //步骤6 任务添加至sparktask队列
            kafkaTemplate.send(KafkaTopic.sparktfidftask, gson.toJson(sparkTaskMessage)).addCallback(new SuccessCallback() {
                @Override
                public void onSuccess(Object o) {
                    log.info("TfidfTask send success " + record.getUrl());
                }
            }, new FailureCallback() {
                @Override
                public void onFailure(Throwable throwable) {
                    log.error("TfidfTask send error " + record.getUrl() + " " + throwable.getMessage());
                }
            });

            kafkaTemplate.flush();
            //其余操作成功后添加至布隆过滤器
            if (!bloomFilter.contains(record.getUrl())) {
                bloomFilter.add(record.getUrl());
            }
        } else {
            log.error("CommonpageConsumer 数据库操作失败");
            throw new Exception("数据库操作失败");
        }

    }

    //@KafkaListener(id = "QueueConsumer", topics = KafkaTopic.queue, containerFactory = "batchFactory")
    public void listenQueue(List<ConsumerRecord<String, String>> list) {
        log.info("QueueConsumer receive :" + list.size());
        //分词
        for (ConsumerRecord<String, String> temp : list) {
            log.info(temp.value());
            SparkTfidfTaskMessage message = gson.fromJson(temp.value(), SparkTfidfTaskMessage.class);

        }
    }

    public void saveSpiderRecord(UrlRecord res) {
        Date date = new Date();
        //保存当前url的爬虫记录
        QueryWrapper<SpiderRecord> spiderQueryWrapper = new QueryWrapper();
        spiderQueryWrapper.eq("url", res.getUrl());
        SpiderRecord spiderRecord = spiderRecordService.getOne(spiderQueryWrapper);
        log.info("spiderRecordService.getOne: " + gson.toJson(spiderRecord));
        if (spiderRecord == null) {
            //无记录
            spiderRecord = new SpiderRecord();
            spiderRecord.setUrl(res.getUrl());

            spiderRecord.setTag(res.getTag());
            spiderRecord.setTitle(res.getTitle());
            spiderRecord.setContent(res.getContent());
            spiderRecord.setTime(res.getTime());

            spiderRecord.setCreateTime(date);
            spiderRecord.setUpdateTime(date);
            spiderRecord.setValid(1);
            if(res.getContent().length()==0||res.getTitle().length()==0){
                spiderRecord.setValid(0);
            }
            boolean op = spiderRecordService.save(spiderRecord);
            log.info("spiderRecordService.save: " + op);
        } else {
            spiderRecord.setTag(res.getTag());
            spiderRecord.setContent(res.getContent());
            spiderRecord.setTime(res.getTime());

            spiderRecord.setUpdateTime(date);
            spiderRecord.setValid(1);
            if(res.getContent().length()==0||res.getTitle().length()==0){
                spiderRecord.setValid(0);
            }
            boolean op = spiderRecordService.updateById(spiderRecord);
            log.info("spiderRecordService.updateById: " + op);
        }
    }

    public SimRecord saveSimRecord(UrlRecord res, String tagString, String simhash) {
        Date date = new Date();
        //判断url存在于布隆过滤器中 很可能MySQL中
        boolean exist = bloomFilter.contains(res.getUrl());
        boolean operation = false;
        SimRecord temp = null;
        //第一次
        if(exist==false){
            log.info("BloomFilter doesn't exist ");
            temp = new SimRecord();

            temp.setUrl(res.getUrl());
            temp.setTitle(res.getTitle());
            temp.setTag(res.getTag());
            temp.setTfidftag(tagString);
            temp.setTime(res.getTime());

            temp.setSimhash(simhash);
            temp.setCreateTime(date);
            temp.setUpdateTime(date);

            //默认原创 未找到关联的原创文章
            temp.setSimparentId(-1);
            temp.setEarlyparentId(-1);
            operation = simRecordService.save(temp);
            log.info("simRecordService.save: "+operation);
        }
        else{
            QueryWrapper<SimRecord> queryWrapper = new QueryWrapper();
            queryWrapper.eq("url", res.getUrl());
            temp = simRecordService.getOne(queryWrapper);
            log.info("getOne " + gson.toJson(temp));
            if (temp != null) {
                temp.setUrl(res.getUrl());
                temp.setTitle(res.getTitle());
                temp.setTfidftag(tagString);
                temp.setTag(res.getTag());
                temp.setTime(res.getTime());

                temp.setSimhash(simhash);
                temp.setUpdateTime(date);
                operation = simRecordService.update(temp, queryWrapper);//按照url更新
                log.info("simRecordService.update: "+operation);
            }
            else {
                log.warn("simRecordService.getOne: error");
                temp = new SimRecord();

                temp.setUrl(res.getUrl());
                temp.setTitle(res.getTitle());
                temp.setTfidftag(tagString);
                temp.setTag(res.getTag());
                temp.setTime(res.getTime());

                temp.setSimhash(simhash);
                temp.setCreateTime(date);
                temp.setUpdateTime(date);
                //默认原创 未找到关联的原创文章
                temp.setSimparentId(-1);
                temp.setEarlyparentId(-1);
                operation = simRecordService.save(temp);
                log.info("simRecordService.save: "+operation);
            }
        }
        //数据库失败
        if (operation == false) {
            temp = null;
        } else {
            QueryWrapper<SimRecord> queryWrapper = new QueryWrapper();
            queryWrapper.eq("url", res.getUrl());
            temp = simRecordService.getOne(queryWrapper);
        }
        return temp;
    }

    public List<WordPair> countTags(AnsjSimHash title, AnsjSimHash content, UrlRecord res) {
        HashMap<String, WordPair> wordPairMap = new HashMap<>();
        //存储爬取的标签
        if (res.getTag().length() > 0) {
            List<String> tags = Arrays.asList(res.getTag().split(" "));
            for (String tag : tags) {
                if (tag.replace(" ", "").length() > 0) {
                    WordPair pair = new WordPair(tag, 5);
                    wordPairMap.put(tag, pair);
                }
            }
        }

        List<WordPair> titleTags = title.getWordsPair();
        for (WordPair pair : titleTags) {
            String tag = pair.getWord();
            if (wordPairMap.get(tag) != null) {
                WordPair newPair = wordPairMap.get(tag);
                newPair.setCount(newPair.getCount() + pair.getCount());
                wordPairMap.put(tag, newPair);
            } else {
                wordPairMap.put(tag, pair);
            }
        }

        List<WordPair> contentTags = content.getWordsPair();
        for (WordPair pair : contentTags) {
            String tag = pair.getWord();
            if (wordPairMap.get(tag) != null) {
                WordPair newPair = wordPairMap.get(tag);
                newPair.setCount(newPair.getCount() + pair.getCount());
                wordPairMap.put(tag, newPair);
            } else {
                wordPairMap.put(tag, pair);
            }
        }
        List<WordPair> allPair = wordPairMap.entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toList());
        List<WordPair> sortedPair = allPair.stream().sorted((a, b) -> {
            return b.getCount() - a.getCount();
        }).collect(Collectors.toList());

        return sortedPair;
    }
}
