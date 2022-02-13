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

    public static final String updateSpark ="updateSpark";//跳过爬虫部分直接获取最新爬取结果进行Spark数据分析步骤
    //public static final String sparklda ="sparklda";//Spark lda步骤处理 提交
    public static final String sparkPairAnalyze="sparkPairAnalyze";//Spark 分析原创文章和非原创文章配对任务
    public static final String sparkPairAnalyzeResult="sparkPairAnalyzeResult";//Spark 分析原创文章和非原创文章配对 处理结果

    @Bean
    public NewTopic updateSpark() {
        return TopicBuilder.name(KafkaTopic.updateSpark)
                .partitions(100)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic sparkPairAnalyze() {
        return TopicBuilder.name(KafkaTopic.sparkPairAnalyze)
                .partitions(100)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic sparkPairAnalyzeResult() {
        return TopicBuilder.name(KafkaTopic.sparkPairAnalyzeResult)
                .partitions(100)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic sparktask() {
        return TopicBuilder.name(KafkaTopic.sparktask)
                .partitions(100)
                .replicas(1)
                .build();
    }

    //spider
    public static final String spidertask ="spidertask";//spidertask
    public static final String spiderresult="spiderresult";//spiderresult
}
