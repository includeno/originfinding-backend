package com.originfinding;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
import java.util.TimeZone;

@SpringBootApplication
@MapperScan("com.originfinding.mapper")
public class SQLServerMain {

    public static void main(String[] args) {
        SpringApplication.run(SQLServerMain.class, args);
    }

    @PostConstruct
    void started() {
        // 设置用户时区为 UTC
        //TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai")));
    }
}