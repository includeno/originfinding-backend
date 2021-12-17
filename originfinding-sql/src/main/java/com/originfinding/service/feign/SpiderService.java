package com.originfinding.service.feign;

import com.originfinding.entity.UrlRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "spider")
public interface SpiderService {

    @PostMapping("/crawl")
    UrlRecord crawl(@RequestParam("url") String url) ;
}
