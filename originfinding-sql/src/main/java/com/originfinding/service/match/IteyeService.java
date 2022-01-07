package com.originfinding.service.match;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@Slf4j
public class IteyeService implements MatchService {
    public static final String[] patterns = new String[]{
            "https://www.iteye.com/blog/(.+)",//https://www.iteye.com/blog/m17165851127-2524064
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
