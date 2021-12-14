package com.originfinding.config;

import com.originfinding.SpiderMain;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SeleniumConfig {

    @Bean
    public WebDriver getWebDriver(){
        ChromeOptions chromeOptions = new ChromeOptions();
        if(SpiderMain.isLinux()){
            chromeOptions.addArguments("--headless");
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("--disable-gpu");
            chromeOptions.addArguments("--disable-dev-shm-usage");
        }
        else if(SpiderMain.isWindows()){
            chromeOptions.addArguments("--headless");
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("--disable-gpu");
            chromeOptions.addArguments("--disable-dev-shm-usage");
            chromeOptions.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
        }
        else if(SpiderMain.isMac()){
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("--disable-gpu");
            chromeOptions.addArguments("--disable-dev-shm-usage");
        }


        //设置为 headless 模式 （必须）
        //chromeOptions.addArguments("--headless");
        //设置浏览器窗口打开大小  （非必须）
        //chromeOptions.addArguments("--window-size=1920,1080");
        return new ChromeDriver(chromeOptions);
    }
}
