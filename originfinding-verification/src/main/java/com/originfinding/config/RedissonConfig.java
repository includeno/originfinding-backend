package com.originfinding.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host:}")
    private String redisHost;

    @Value("${spring.redis.port:}")
    private String redisPort;

    @Value("${spring.redis.password:}")
    private String redisPassword;

    @Value("${spring.redis.database:}")
    private Integer database;

    @Value("${spring.redis.pool.minidle}")
    private int connectionMinimumIdleSize = 4;//从节点最小空闲连接数）
    @Value("${spring.redis.pool.subminidle}")
    private int subscriptionConnectionMinimumIdleSize = 1; //从节点发布和订阅连接的最小空闲连接数）
    @Value("${spring.redis.pool.maxidle}")
    private int subscriptionConnectionPoolSize = 24; //（从节点发布和订阅连接池大小）
    @Value("${spring.redis.pool.maxactive}")
    private int connectionPoolSize = 48;//连接池大小）

    /**
     * RedissonClient bean
     *
     * @return RedissonClient
     * @throws IOException IOException
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() throws IOException {
        // 1、创建配置
        Config config = new Config();
        config.setCodec(new JsonJacksonCodec())
                .useSingleServer()
                .setAddress("redis://" + redisHost + ":" + redisPort)
                .setPassword(redisPassword).setDatabase(database)
                .setConnectionMinimumIdleSize(connectionMinimumIdleSize)
                .setSubscriptionConnectionMinimumIdleSize(subscriptionConnectionMinimumIdleSize)
                .setSubscriptionConnectionPoolSize(subscriptionConnectionPoolSize)
                .setConnectionPoolSize(connectionPoolSize)
                .setConnectTimeout(300000);
        return Redisson.create(config);
    }
}
