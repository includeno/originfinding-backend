package com.originfinding.controller;

import com.originfinding.entity.UrlRecord;
import com.originfinding.service.feign.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpiderController {
    @Autowired
    SpiderService api;

    @PostMapping("/spider")
    public UrlRecord spider(String url){
        return api.crawl(url);
    }
}
