package com.originfinding.service;

import com.originfinding.TestMain;
import com.originfinding.config.SystemConfig;
import com.originfinding.response.SpiderResponse;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(classes = TestMain.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TencentCloudServiceTest {
    @Autowired
    CrawlService crawlService;

    @BeforeAll
    public static void be(){
        //chromedriver http://npm.taobao.org/mirrors/chromedriver/
        if(SystemConfig.isLinux()){
            System.setProperty("webdriver.chrome.driver", "/tools/chromedriver");//linux
        }
        if(SystemConfig.isWindows()){
            System.setProperty("webdriver.chrome.driver", "private/chromedriver.exe");//windows
        }
        if(SystemConfig.isMac()){
            System.setProperty("webdriver.chrome.driver", "private/chromedriver");//mac
        }
    }

    @Test
    public void tencentCommon(){
        SpiderResponse res = crawlService.crawl("https://cloud.tencent.com/developer/article/1927707");

        System.out.println("=========");
        System.out.println(res.record.getView());
        System.out.println(res.record.getValid());
    }

    @Test
    public void tencentK(){
        SpiderResponse res = crawlService.crawl("https://cloud.tencent.com/developer/article/1903972");

        System.out.println("=========");
        System.out.println(res.record.getView());
        System.out.println(res.record.getValid());
    }
}