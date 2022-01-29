package com.originfinding.util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class GlobalDateUtil {

    //转换格式 2018-08-15 18:18
    //适用网站 博客园
    public static Date convert2(String input) {
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

    //转换格式 2018-08-15 18:18:20
    //适用网站 csdn
    public static Date convert3(String input) {
        String patternFormat = "((19|20)[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]) "
                + "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";
        Pattern pattern = Pattern.compile(patternFormat);
        Matcher matcher = pattern.matcher(input);
        String group=input;
        if(matcher.find()){
            group=matcher.group(0);
        }
        log.info("convert from convert3:"+group);
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
        Date ans= null;
        try {
            ans = format.parse(group);
        } catch (ParseException e) {
            ans=null;
            e.printStackTrace();
        }
        finally {

        }
        return ans;
    }

    //转换格式 2021/12/31 15:07
    //适用网站 oschina
    public static Date convert2_1(String input) {
        String patternFormat = "((19|20)[0-9]{2})/(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01]) "
                + "([01]?[0-9]|2[0-3]):[0-5][0-9]";
        Pattern pattern = Pattern.compile(patternFormat);
        Matcher matcher = pattern.matcher(input);
        Date ans = null;
        String group=input;
        if(matcher.find()){
            group=matcher.group(0);
        }
        log.info("convert from convert2_1:" + group);
        SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd hh:mm");
        try {
            ans = format.parse(group);
        } catch (ParseException e) {
            ans=null;
            e.printStackTrace();
        } finally {

        }
        return ans;
    }

    //转换格式 2021.11.19 17:08:03
    //适用网站 简书
    public static Date convertFull(String input) {
        String patternFormat = "((19|20)[0-9]{2}).(0?[1-9]|1[012]).(0?[1-9]|[12][0-9]|3[01]) "
                + "([01]?[0-9]|2[0-3]):[0-5][0-9]";
        Pattern pattern = Pattern.compile(patternFormat);
        Matcher matcher = pattern.matcher(input);
        Date ans = null;
        String group=input;
        if(matcher.find()){
            group=matcher.group(0);
        }
        log.info("convertFull from:"+group);
        SimpleDateFormat format = new SimpleDateFormat("yy.MM.dd hh:mm:ss");
        try {
            ans = format.parse(group);
        } catch (ParseException e) {
            ans=null;
            e.printStackTrace();
        }
        finally {

        }
        return ans;
    }
}
