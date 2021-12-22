package com.originfinding.listener;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.originfinding.config.KafkaTopic;
import com.originfinding.entity.SimRecord;
import com.originfinding.entity.UrlRecord;
import com.originfinding.listener.message.SparkTaskMessage;
import com.originfinding.service.SimRecordService;
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
        log.info("CommonpageConsumer receive:"+message);
        //监听controller发送的消息 message=url
        String url = message;

        //远程调用spider爬虫服务
        UrlRecord res = api.crawl(url);
        if (res == null) {
            log.error("error: 爬虫服务出错");
            return;
        }
        else if(res.getContent()==null){
            throw new Exception("爬虫服务处于暂时休息中");
        }
        else if (res.getContent().length() == 0 || res.getTitle().length() == 0) {
            log.error("error: 爬虫服务无法爬取此网页，请稍后重试" + gson.toJson(res));
            return;
        }

        //计算simhash 64位长度
        SimRecord temp = null;
        SimHash simHash = new SimHash(res.getContent(), 64);

        //TODO redis 判断记录存在=>url转化为相同长度的hash值
        //判断url存在于布隆过滤器中
        boolean exist=bloomFilter.contains(url);
        boolean operation=false;
        if (exist) {
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
            operation=simRecordService.updateById(temp);
        } else {
            //不存在记录
            temp = new SimRecord();;
            temp.setCreateTime(new Date());
            temp.setParentId(-1);//默认原创 未找到关联的原创文章

            temp.setUrl(res.getUrl());
            temp.setTitle(res.getTitle());
            temp.setTime(res.getTime());
            temp.setUpdateTime(new Date());
            temp.setSimhash(simHash.getStrSimHash());
            operation=simRecordService.save(temp);
        }
        //数据库操作成功
        if(operation==true){
            //数据库保存记录
            log.info("CommonpageConsumer db:"+gson.toJson(temp));
            //数据库查id
            QueryWrapper<SimRecord> queryWrapper = new QueryWrapper();
            queryWrapper.eq("url",url);
            SimRecord record = simRecordService.getOne(queryWrapper);
            log.info("CommonpageConsumer getOne:"+gson.toJson(record));

            SparkTaskMessage sparkTaskMessage=SparkTaskMessage.fromSimRecord(record,res.getContent());

            //任务添加至队列
            kafkaTemplate.send(KafkaTopic.queue, gson.toJson(sparkTaskMessage));
            //其余操作成功后添加至布隆过滤器
            if(!bloomFilter.contains(url)){
                bloomFilter.add(url);
            }
        }
        else{
            log.error("CommonpageConsumer 数据库操作失败");
            throw new Exception("数据库操作失败");
        }

    }

    @KafkaListener(id = "QueueConsumer", topics = KafkaTopic.queue,containerFactory = "batchFactory")
    public void listenQueue(List<ConsumerRecord<String,String>> list) {
        log.info("QueueConsumer receive :"+list.size());
        //分词
        for(ConsumerRecord<String,String> temp:list){
            log.info(temp.value());
        }

    }


    //@KafkaListener(id = "TaskConsumer",topics = KafkaTopic.task)
    public void listenTask(String message) {

    }
}
