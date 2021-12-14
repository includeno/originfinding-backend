package com.originfinding.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ZhihuzhuanlanService implements URLValidService{
    public static final String path="https://zhuanlan.zhihu.com";
    public static final String host="zhuanlan.zhihu.com";

    @Override
    public boolean match(String url) {
        String host=getHost(url);
        if(host.equals(ZhihuzhuanlanService.host)&&url.startsWith(ZhihuzhuanlanService.path)){
            return false;
        }
        else {
            return false;
        }
    }
}

