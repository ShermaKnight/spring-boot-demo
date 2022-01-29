package org.example.concurrent;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.RandomUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Task implements Runnable {

    private CountDownLatch countDownLatch;
    private Object data;

    public Task(CountDownLatch countDownLatch, Object data) {
        this.countDownLatch = countDownLatch;
        this.data = data;
    }

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(RandomUtil.randomInt(3));
            System.out.println(Thread.currentThread().getName() + ": " + data.toString());
        } catch (InterruptedException e) {
            System.out.println(ExceptionUtil.getMessage(e));
        } finally {
            countDownLatch.countDown();
        }
    }
}
