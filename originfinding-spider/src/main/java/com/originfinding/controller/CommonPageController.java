package com.originfinding.controller;

import com.originfinding.config.SeleniumConfig;
import com.originfinding.entity.UrlRecord;
import com.originfinding.service.CommonPageService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class CommonPageController {

    @Autowired
    CommonPageService commonPageService;

    //读取网页的主要内容
    @PostMapping("/crawl")
    public UrlRecord crawl(@RequestParam("url") String url) {

        SeleniumConfig config = new SeleniumConfig();
        WebDriver chrome = config.getWebDriver();
        chrome.get(url);
        Integer random = new Random(9).nextInt();
        try {
            TimeUnit.MILLISECONDS.sleep(1072 + random * 123);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        UrlRecord record = new UrlRecord();
        record.setUrl(url);
        try {
            record=commonPageService.crawl(chrome, record);
            log.warn(record.getTitle());
        } catch (Exception e) {
            log.error("Exception in getMainContent");
            e.printStackTrace();
        } finally {
            chrome.close();
        }
        return record;
    }
}
