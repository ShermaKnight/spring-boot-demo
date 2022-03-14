package org.example.service;

import cn.hutool.core.lang.UUID;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class RedisLock implements Lock {

    private String lock;
    private long expire = 30l;

    private WatchDog watchDog;
    private RedisTemplate redisTemplate;

    public RedisLock(String lock, RedisTemplate redisTemplate) {
        this.lock = lock;
        this.redisTemplate = redisTemplate;
    }

    @SneakyThrows
    @Override
    public void lock() {
        while (!tryLock()) {

        }
        watchDog = new WatchDog(lock, true, redisTemplate);
        watchDog.start();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return redisTemplate.opsForValue().setIfAbsent(lock, UUID.fastUUID().toString(), expire, TimeUnit.SECONDS);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        redisTemplate.delete(lock);
        watchDog.setBusinessDone(false);
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
