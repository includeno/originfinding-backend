package com.originfinding;

import com.originfinding.config.SystemConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
import java.util.TimeZone;

@EnableDiscoveryClient
@SpringBootApplication
public class SpiderMain {

    public static void main(String[] args) {

        //chromedriver http://npm.taobao.org/mirrors/chromedriver/
        if(SystemConfig.isLinux()){
            System.setProperty("webdriver.chrome.driver", "/tools/chromedriver");//linux
        }
        if(SystemConfig.isWindows()){
            System.setProperty("webdriver.chrome.driver", "C:\\EnvironmentSoftwares\\chromedriver_win32\\chromedriver.exe");//windows
        }
        if(SystemConfig.isMac()){
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


