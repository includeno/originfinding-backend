package com.originfinding.service.match;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
@Slf4j
public class OschinaService implements MatchService {
    public static final String[] patterns = new String[]{
            "https://my.oschina.net/(.+)/blog/(.+)",//https://my.oschina.net/xcafe/blog/5389937
            "https://my.oschina.net/u/(.+)/blog/(.+)",//https://my.oschina.net/u/729507/blog/78144
    };

    @Override
    public boolean match(String url) {
        for (String pattern : patterns) {
            Pattern p = Pattern.compile(pattern);
            if (p.matcher(url).matches()) {
                return true;
            }
        }
        return false;
    }
}
