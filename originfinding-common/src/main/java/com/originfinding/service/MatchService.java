package com.originfinding.service;

import java.net.MalformedURLException;
import java.net.URL;

public interface MatchService {

    //验证URL和爬虫匹配
    boolean match(String url);
}
