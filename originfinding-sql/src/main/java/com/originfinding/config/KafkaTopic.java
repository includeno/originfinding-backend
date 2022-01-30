package com.originfinding.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@EnableKafka
@Configuration
public class KafkaTopic {
    //Spark监听
    public static final String sparktask ="sparktask";//Spark task
    public static final String sparkresult="sparkresult";//Spark 结果

    public static final String sparklda ="sparklda";//Spark lda步骤处理
    public static final String sparkPairAnalyze="sparkPairAnalyze";//Spark 分析原创文章和非原创文章配对任务
    public static final String sparkPairAnalyzeResult="sparkPairAnalyzeResult";//Spark 分析原创文章和非原创文章配对 处理结果

    @Bean
    public NewTopic sparklda() {
        return TopicBuilder.name(KafkaTopic.sparklda)
                .partitions(10)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic sparkPairAnalyze() {
        return TopicBuilder.name(KafkaTopic.sparkPairAnalyze)
                .partitions(10)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic tfidf() {
        return TopicBuilder.name(KafkaTopic.sparkPairAnalyzeResult)
                .partitions(10)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic task() {
        return TopicBuilder.name(KafkaTopic.sparktask)
                .partitions(10)
                .replicas(1)
                .build();
    }

    //spider
    public static final String spidertask ="spidertask";//spidertask
    public static final String spiderresult="spiderresult";//spiderresult


    @Bean
    public NewTopic spidertask() {
        return TopicBuilder.name(KafkaTopic.spidertask)
                .partitions(12)
                .replicas(1)
                .build();
    }



    @Bean
    public NewTopic spiderresult() {
        return TopicBuilder.name(KafkaTopic.spiderresult)
                .partitions(12)
                .replicas(1)
                .build();
    }
}
