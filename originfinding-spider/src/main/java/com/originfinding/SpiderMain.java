package com.originfinding;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
import java.util.TimeZone;

@EnableDiscoveryClient
@SpringBootApplication
public class SpiderMain {


    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    public static boolean isMac() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }

    public static void main(String[] args) {

        //chromedriver http://npm.taobao.org/mirrors/chromedriver/
        if(isLinux()){
            System.setProperty("webdriver.chrome.driver", "/tools/chromedriver");//linux
        }
        if(isWindows()){
            System.setProperty("webdriver.chrome.driver", "C:\\EnvironmentSoftwares\\chromedriver_win32\\chromedriver.exe");//windows
        }
        if(isMac()){
            System.setProperty("webdriver.chrome.driver", "chromedriver");//mac
        }

        SpringApplication.run(SpiderMain.class, args);
    }

    @PostConstruct
    void started() {
        // 设置用户时区为 UTC
        //TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai")));
    }

}


