package com.originfinding;

import com.originfinding.config.SystemConfig;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.TimeZone;

@MapperScan("com.originfinding.mapper")
@SpringBootApplication
@Slf4j
public class TestMain {

    public static void main(String[] args) {
        log.info("spider args:"+ Arrays.toString(args));

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

        SpringApplication.run(TestMain.class, args);
    }

    @PostConstruct
    void started() {
        // 设置用户时区为 UTC
        //TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai")));
    }

}


