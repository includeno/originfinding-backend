package com.originfinding.listener;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.originfinding.config.KafkaTopic;
import com.originfinding.config.RedisKey;
import com.originfinding.entity.SimRecord;
import com.originfinding.listener.message.PairTaskResultMessage;
import com.originfinding.response.SubmitResponse;
import com.originfinding.service.sql.SimRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Date;

@Slf4j
@Configuration
public class SparkPairAnalyzeResultListener {
    @Autowired
    SimRecordService simRecordService;

    @Autowired
    Gson gson;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Transactional(rollbackFor = Exception.class)
    @KafkaListener(id = "PairAnalyzeResultConsumer", topics = KafkaTopic.sparkPairAnalyzeResult)
    public void listen(String message) throws Exception {
        log.info("PairAnalyzeResultConsumer receive:" + message);
        PairTaskResultMessage pairTaskResultMessage = gson.fromJson(message, PairTaskResultMessage.class);
        SimRecord simRecord = simRecordService.getById(pairTaskResultMessage.getId());

        simRecord.setSimlevelfirst(pairTaskResultMessage.getSimlevelfirst());
        simRecord.setSimlevelsecond(pairTaskResultMessage.getSimlevelsecond());
        simRecord.setSimparentId(pairTaskResultMessage.getSimparentId());
        simRecord.setEarlyparentId(pairTaskResultMessage.getEarlyparentId());
        simRecord.setVersion(simRecord.getVersion() + 1);
        simRecord.setUpdateTime(new Date());
        boolean operation = simRecordService.updateById(simRecord);
        log.info("PairAnalyzeResult update:" + operation);

        SubmitResponse.SubmitResponseEntity entity = new SubmitResponse.SubmitResponseEntity();
        entity.setUrl(simRecord.getUrl());
        entity.setSim3(simRecord.getSimlevelfirst().toString());
        entity.setSim4(simRecord.getSimlevelsecond().toString());
        //simparentId
        if (!simRecord.getSimparentId().equals(-1)) {
            //查询关联数据
            QueryWrapper<SimRecord> parentQuery = new QueryWrapper();
            parentQuery.eq("id", simRecord.getSimparentId());
            SimRecord parent = simRecordService.getOne(parentQuery);
            if (parent == null) {
                entity.setSimparentUrl("");
            } else {
                entity.setSimparentUrl(parent.getUrl());
            }
        }
        //earlyparentId
        if (!simRecord.getEarlyparentId().equals(-1)) {
            //查询关联数据
            QueryWrapper<SimRecord> parentQuery = new QueryWrapper();
            parentQuery.eq("id", simRecord.getEarlyparentId());
            SimRecord parent = simRecordService.getOne(parentQuery);
            if (parent == null) {
                entity.setEarlyparentUrl("");
            } else {
                entity.setEarlyparentUrl(parent.getUrl());
            }
        }
        stringRedisTemplate.opsForValue().set(RedisKey.responseKey(simRecord.getUrl()), gson.toJson(entity), Duration.ofHours(7 * 24));//更新时过期时间设置长一些

    }
}
