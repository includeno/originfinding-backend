package com.originfinding.config;

public class KafkaTopic {

    public static final String searchrequest="searchrequest";//搜索请求

    //Spark监听
    public static final String task="sparktask";//Spark task
    public static final String result="sparkresult";//Spark 结果
    public static final String queue="queue";//批量读取n条kafka消息 请求聚合

    //普通网页爬虫监听
    public static final String commonpage="commonpage";
}
