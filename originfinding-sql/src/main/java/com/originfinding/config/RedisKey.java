package com.originfinding.config;

public class RedisKey {

    public static String spiderKey(String url){
        return "spider:"+url;
    }

    public static String responseKey(String url){
        return "response:"+url;
    }

    public static String updateKey(String url){
        return "update:"+url;
    }

    public static String simRecordKey(String url){
        return "simrecord:"+url;
    }

}
