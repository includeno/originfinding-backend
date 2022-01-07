package com.originfinding.service.match;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@Slf4j
public class CsdnService implements MatchService {
    public static final String[] patterns = new String[]{
            "https://blog.csdn.net/(.+)/article/details/(.+)",//https://blog.csdn.net/qq_16214677/article/details/84863046
            "https://(.+).blog.csdn.net/article/details/(.+)",//https://gxyyds.blog.csdn.net/article/details/96458591
    };

    @Override
    public boolean match(String url) {
        for (String pattern : patterns) {
            Pattern p = Pattern.compile(pattern);
            if (p.matcher(url).matches()) {
                log.info("csdn matches @url:"+url);
                return true;
            }
        }
        return false;
    }
}
