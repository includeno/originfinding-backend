package com.originfinding.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.Gson;
import com.originfinding.entity.UrlRecord;
import com.originfinding.service.CommonPageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class CommonPageController {

    @Autowired
    CommonPageService commonPageService;

    @Autowired
    Gson gson;

    //创建令牌桶
    private RateLimiter rateLimiter= RateLimiter.create(0.2);//每5秒放行1个请求

    //读取网页的主要内容
    @PostMapping("/crawl")
    public UrlRecord crawl(@RequestParam("url") String url) {
        UrlRecord record = new UrlRecord();
        record.setUrl(url);
        if (rateLimiter.tryAcquire(3, TimeUnit.SECONDS)) {
            log.info("crawl begin:"+gson.toJson(record));
            record=commonPageService.crawl(record);
            log.info("crawl end:"+gson.toJson(record));
            return record;
        }
        else{
            record.setContent(null);//因为爬虫服务 休息间隔而暂停爬取
        }
        return record;
    }
}
