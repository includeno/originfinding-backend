package com.originfinding.util;

import com.originfinding.service.match.MatchService;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class UrlFilter {

    //筛选出符合条件的URl
    public static List<String> filter(List<String> list) throws NoSuchMethodException {
        List<String> ans = new ArrayList<>();

        for (String url : list) {
            for (Class c : MatchHelper.impls) {
                Method match = c.getMethod("match", new Class[]{String.class});
                //spring获取实例
                MatchService service = (MatchService) SpringContextUtil.getContext().getBean(c);
                try {
                    boolean result = (boolean) match.invoke(service, url);
                    if (result == true) {
                        ans.add(url);
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return ans;
    }
}