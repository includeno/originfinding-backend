package com.originfinding.util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class GlobalDateUtil {

    //转换格式 2018-08-15 18:18
    //适用网站 博客园 csdn
    public static Date convert(String input) {
        log.info("convert from:"+input);
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd hh:mm");
        Date ans= null;
        try {
            ans = format.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        finally {

        }
        return ans;
    }

    //转换格式 2021.11.19 17:08:03
    //适用网站 简书
    public static Date convertFull(String input) {
        log.info("convertFull from:"+input);
        SimpleDateFormat format = new SimpleDateFormat("yy.MM.dd hh:mm:ss");
        Date ans= null;
        try {
            ans = format.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        finally {

        }
        return ans;
    }
}
