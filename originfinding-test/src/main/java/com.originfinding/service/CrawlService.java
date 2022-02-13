package com.originfinding.service;

import com.google.gson.Gson;
import com.originfinding.config.SpiderLimit;
import com.originfinding.entity.UrlRecord;
import com.originfinding.enums.SpiderCode;
import com.originfinding.response.SpiderResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CrawlService {

    @Autowired
    private CommonPageService commonPageService;

    @Autowired
    private Gson gson;

    public SpiderResponse crawl(String url) {
        long start=System.currentTimeMillis();
        log.info("crawl begin:"+url+" "+start);
        SpiderResponse response=new SpiderResponse();
        UrlRecord record = new UrlRecord();
        record.setUrl(url);
        if (SpiderLimit.spiders.size()<SpiderLimit.countOfSpider&&!SpiderLimit.spiders.contains(url)) {
            SpiderLimit.spiders.add(url);
            log.info("commonPageService crawl begin:"+gson.toJson(record));
            record=commonPageService.crawl(record);
            log.info("commonPageService crawl end:"+gson.toJson(record));
            SpiderLimit.spiders.remove(url);
            if(record!=null&&record.getTitle()!=null&&record.getTitle().length()>0&&record.getContent()!=null&&record.getContent().length()>0){
                response.setCode(SpiderCode.SUCCESS.getCode());
            }
            else {
                response.setCode(SpiderCode.SPIDER_UNREACHABLE.getCode());
            }
        }
        else{
            response.setCode(SpiderCode.SPIDER_COUNT_LIMIT.getCode());//因为爬虫服务数量已满
        }
        response.setRecord(record);
        log.info("crawl end:"+url+" "+(System.currentTimeMillis()-start));
        return response;
    }
}
