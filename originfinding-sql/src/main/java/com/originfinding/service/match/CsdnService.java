package com.originfinding.service.match;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@Slf4j
public class CsdnService implements MatchService {
    public static final String pattern="https://blog.csdn.net/(.*)/article/details/(.*)";//正则匹配表达式
    public static final Pattern re = Pattern.compile(pattern);
    public static final String host="blog.csdn.net";

    @Override
    public boolean match(String url) {
        String cur=getHost(url);
        if(cur.equals(host)&&re.matcher(url).matches()){
            return true;
        }
        else {
            return false;
        }
    }
}
