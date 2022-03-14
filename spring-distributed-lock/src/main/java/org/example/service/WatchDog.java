package org.example.service;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public class WatchDog extends Thread {

    private String lock;
    private boolean businessDone;
    private RedisTemplate redisTemplate;

    public WatchDog(String lock, boolean businessDone, RedisTemplate redisTemplate) {
        this.lock = lock;
        this.businessDone = businessDone;
        this.redisTemplate = redisTemplate;
    }

    public boolean isBusinessDone() {
        return businessDone;
    }

    public void setBusinessDone(boolean businessDone) {
        this.businessDone = businessDone;
    }

    @Override
    public void run() {
        while (businessDone) {
            if (redisTemplate.getExpire(lock) < 10l) {
                redisTemplate.expire(lock, 30l, TimeUnit.SECONDS);
            }
        }
    }
}
