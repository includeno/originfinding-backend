package com.originfinding.listener;

import com.google.gson.Gson;
import com.originfinding.config.KafkaTopic;
import com.originfinding.entity.SimRecord;
import com.originfinding.listener.message.PairTaskResultMessage;
import com.originfinding.service.sql.SimRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Configuration
public class SparkPairAnalyzeResultListener {
    @Autowired
    SimRecordService simRecordService;

    @Autowired
    Gson gson;

    @Transactional(rollbackFor = Exception.class)
    @KafkaListener(id = "PairAnalyzeResultConsumer", topics = KafkaTopic.sparkPairAnalyzeResult)
    public void listen(String message) throws Exception {
        log.info("PairAnalyzeResultConsumer receive:" + message);
        PairTaskResultMessage pairTaskResultMessage=gson.fromJson(message, PairTaskResultMessage.class);
        SimRecord simrecord = simRecordService.getById(pairTaskResultMessage.getId());

        simrecord.setTfidftag(pairTaskResultMessage.getTfidftag());
        simrecord.setLdatag(pairTaskResultMessage.getLdatag());

        simrecord.setSimlevelfirst(pairTaskResultMessage.getSimlevelfirst());
        simrecord.setSimlevelsecond(pairTaskResultMessage.getSimlevelsecond());
        simrecord.setSimparentId(pairTaskResultMessage.getSimparentId());
        simrecord.setEarlyparentId(pairTaskResultMessage.getEarlyparentId());
        boolean operation=simRecordService.updateById(simrecord);
        log.info("PairAnalyzeResult update:"+operation);

    }
}