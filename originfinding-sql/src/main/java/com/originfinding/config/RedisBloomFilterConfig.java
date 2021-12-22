package com.originfinding.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//ref https://www.cnblogs.com/ysocean/p/12594982.html
@Configuration
public class RedisBloomFilterConfig {
    @Autowired
    RedissonClient redissonClient;

    @Bean
    public RBloomFilter<String> getBlomFilter(){
        RBloomFilter<String> filter=redissonClient.getBloomFilter("urllist");
        //初始化布隆过滤器：预计元素为100000000L,误差率为3%
        filter.tryInit(100000000L,0.03);
        return filter;
    }

    @Autowired
    RBloomFilter<String> bloomFilter;

    public void test(String url){

        boolean exist=bloomFilter.contains(url);
        if(exist){
            return;
        }
    }
}
