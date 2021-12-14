package com.originfinding.service;

import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public interface ContentService {

    //等待页面加载完毕
    void wait(WebDriver chrome, String url);

    //抓取主要内容的方法
    String getMainContent(WebDriver chrome, String url);

    //抓取标题的方法
    String getTitle(WebDriver chrome, String url);

    //抓取时间的方法
    Date getTime(WebDriver chrome, String url);

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

        }
        return host;
    }

    boolean match(String url);
}
