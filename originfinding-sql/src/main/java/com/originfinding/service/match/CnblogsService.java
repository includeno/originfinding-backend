package com.originfinding.service.match;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@Slf4j
public class CnblogsService implements MatchService {
    public static final String[] patterns = new String[]{
            "https://www.cnblogs.com/(.+)/p/(.+)",//https://www.cnblogs.com/frankdeng/p/9310684.html
    };

    @Override
    public boolean match(String url) {
        for (String pattern : patterns) {
            Pattern p = Pattern.compile(pattern);
            if (p.matcher(url).matches()) {
                log.info("cnblog matches @url:"+url);
                return true;
            }
        }
        return false;
    }

}
