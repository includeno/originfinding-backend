package com.originfinding.service;

import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URL;

public interface URLValidService {

    //抓取主机名的方法
    default String getHost(String url){
        URL resource = null;
        String host="";
        try {
            resource = new URL(url);
            host= resource.getHost();// 获取主机名
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        finally {
            return host;
        }
    }

    //验证URL和爬虫匹配
    boolean match(String url);
}
