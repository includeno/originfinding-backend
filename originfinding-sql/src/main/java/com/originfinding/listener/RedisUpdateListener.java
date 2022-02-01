package com.originfinding.listener;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.originfinding.config.KafkaTopic;
import com.originfinding.entity.SimRecord;
import com.originfinding.entity.SpiderRecord;
import com.originfinding.entity.UrlRecord;
import com.originfinding.listener.message.LdaMessage;
import com.originfinding.service.sql.SimRecordService;
import com.originfinding.service.sql.SpiderRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

@Slf4j
@Configuration
public class RedisUpdateListener {
    @Autowired
    SimRecordService simRecordService;

    @Autowired
    SpiderRecordService spiderRecordService;

    @Autowired
    Gson gson;

    @Autowired
    KafkaTemplate kafkaTemplate;

    @KafkaListener(id = "UpdateRedisConsumer", topics = KafkaTopic.updateRedis)
    public void updateRedis(String message) throws Exception {
        String url=message;
        //查询上一次的爬虫记录id
        Integer id=spiderRecordService.getLastId(url);
        if(id==null){
            log.error("updateRedis 无上一次爬取结果");
            return;
        }
        //获取爬虫记录content
        SpiderRecord spiderRecord = spiderRecordService.getById(id);

        UrlRecord record=SpiderRecord.toUrlRecord(spiderRecord);
        //获取simrecord记录
        QueryWrapper<SimRecord> queryWrapper = new QueryWrapper();
        queryWrapper.eq("url", url);
        SimRecord temp = simRecordService.getOne(queryWrapper);
        if(temp!=null &&temp.getValid()==1){
            //发送lda处理请求
            LdaMessage ldaMessage = LdaMessage.fromSimRecord(temp,record.getContent());
            //步骤6 任务添加至sparktask队列
            kafkaTemplate.send(KafkaTopic.sparklda,url, gson.toJson(ldaMessage)).addCallback(new SuccessCallback() {
                @Override
                public void onSuccess(Object o) {
                    log.info("LdaMessage send success " + record.getUrl());
                }
            }, new FailureCallback() {
                @Override
                public void onFailure(Throwable throwable) {
                    log.error("LdaMessage send error " + record.getUrl() + " " + throwable.getMessage());
                }
            });
            kafkaTemplate.flush();
        }
        else {
            log.warn("无效的simrecord记录");
        }

    }
}
