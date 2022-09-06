package org.example.config;

import cn.hutool.core.util.RandomUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

@RefreshScope
@Configuration
public class ConditionConfigure {

    @Bean
    @ConditionalOnProperty(prefix = "condition", name = "instance", havingValue = "true")
    public ConcurrentHashMap<Integer, String> conditionBean() {
        System.out.println("Instance conditionBean.");
        ConcurrentHashMap<Integer, String> cache = new ConcurrentHashMap<>();
        IntStream.range(1, 101).boxed().parallel().forEach(key -> cache.put(key, RandomUtil.randomStringUpper(10)));
        return cache;
    }
}
