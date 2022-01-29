package com.originfinding.controller;

import com.google.gson.Gson;
import com.originfinding.config.SpiderLimit;
import com.originfinding.entity.UrlRecord;
import com.originfinding.enums.SpiderCode;
import com.originfinding.response.SpiderResponse;
import com.originfinding.service.CommonPageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class CommonPageController {

    @Autowired
    CommonPageService commonPageService;

    @Autowired
    Gson gson;

    //读取网页的主要内容
    @PostMapping("/crawl")
    public SpiderResponse crawl(@RequestParam("url") String url) {
        SpiderResponse response=new SpiderResponse();
        UrlRecord record = new UrlRecord();
        record.setUrl(url);
        if (SpiderLimit.spiders.size()<SpiderLimit.countOfSpider&&!SpiderLimit.spiders.contains(url)) {
            SpiderLimit.spiders.add(url);
            log.info("crawl begin:"+gson.toJson(record));
            record=commonPageService.crawl(record);
            log.info("crawl end:"+gson.toJson(record));
            SpiderLimit.spiders.remove(url);
        }
        else{
            response.setCode(SpiderCode.SPIDER_COUNT_LIMIT.getCode());//因为爬虫服务数量已满
        }
        return response;
    }
}
