package com.originfinding.config;

public class RedisKey {

    public static String spiderKey(String url){
        return "spider:"+url;
    }

    public static String responseKey(String url){
        return "response:"+url;
    }

}
