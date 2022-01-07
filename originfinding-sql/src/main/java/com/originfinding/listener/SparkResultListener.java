package com.originfinding.listener;

import com.google.gson.Gson;
import com.originfinding.config.KafkaTopic;
import com.originfinding.listener.message.SparkResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@Configuration
public class SparkResultListener {
    @Autowired
    Gson gson;

    @KafkaListener(id = "SparkResultConsumer", topics = KafkaTopic.sparkresult)
    public void listenTaskResult(String message) {
        log.info("SparkResultConsumer receive :" + message);
        SparkResultMessage sparkResultMessage = gson.fromJson(message, SparkResultMessage.class);

    }
}
