package com.originfinding.service.match;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@Slf4j
public class ZhihuzhuanlanService implements MatchService {
    public static final String pattern="https://zhuanlan.zhihu.com/p/(.*)";
    public static final Pattern re = Pattern.compile(pattern);
    public static final String host="zhuanlan.zhihu.com";

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

