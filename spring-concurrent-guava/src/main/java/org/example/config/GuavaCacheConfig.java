package org.example.config;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import org.example.cache.UserCacheLoader;
import org.example.domain.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class GuavaCacheConfig {

    @Bean
    public UserCacheLoader userCacheLoader() {
        return new UserCacheLoader();
    }

    @Bean
    public LoadingCache<Integer, User> userCache(UserCacheLoader userCacheLoader) {
        return CacheBuilder.newBuilder()
                .concurrencyLevel(8)
                .initialCapacity(10)
                .maximumSize(100)
                .recordStats()
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .removalListener(notification -> {
                    System.out.println(notification.getKey() + " " + notification.getValue() + " removed, cause: " + notification.getCause());
                })
                .build(userCacheLoader);
    }
}
