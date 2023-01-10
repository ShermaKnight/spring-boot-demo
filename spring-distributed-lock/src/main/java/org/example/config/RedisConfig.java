package org.example.config;

import org.example.service.RedisLock;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

@Configuration
public class RedisConfig {

    @Resource
    private RedisTemplate redisTemplate;

    @Bean
    public RedisLock redisLock() {
        return new RedisLock("shopping", redisTemplate);
    }

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://10.211.55.3:6379");
        return Redisson.create(config);
    }
}
