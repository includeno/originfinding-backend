package com.originfinding.service.match;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.regex.Pattern;

@Slf4j
@Service
public class JianshuService implements MatchService {
    public static final String[] patterns = new String[]{
            "https://www.jianshu.com/p/(.*)",//https://www.jianshu.com/p/f0ad0f80fd2c
    };

    @Override
    public boolean match(String url) {
        for (String pattern : patterns) {
            Pattern p = Pattern.compile(pattern);
            if (p.matcher(url).matches()) {
                log.info("jianshu matches");
                return true;
            }
        }
        return false;
    }

}
