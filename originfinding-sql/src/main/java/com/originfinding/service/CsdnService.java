package com.originfinding.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CsdnService implements URLValidService{
    public static final String path="https://blog.csdn.net/*/article/details/*";
    public static final String host="blog.csdn.net";

    @Override
    public boolean match(String url) {
        String host=getHost(url);
        if(host.equals(CsdnService.host)){
            return false;
        }
        else {
            return false;
        }
    }
}
