package com.originfinding.service.match;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@Slf4j
public class ZhihuzhuanlanService implements MatchService {
    public static final String[] patterns = new String[]{
            "https://zhuanlan.zhihu.com/p/(.*)",//https://zhuanlan.zhihu.com/p/88403925
            "https://www.zhihu.com/column/p/(.*)"//https://www.zhihu.com/column/p/24172120
    };

    @Override
    public boolean match(String url) {
        for (String pattern : patterns) {
            Pattern p = Pattern.compile(pattern);
            if (p.matcher(url).matches()) {
                log.info("zhihuzhuanlan matches @url:"+url);
                return true;
            }
        }
        return false;
    }
}

