package com.originfinding.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GlobalDateUtil {

    //转换格式 2018-08-15 18:18
    //适用网站 博客园
    public static Date convert(String input) {
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
}
