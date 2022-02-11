package org.example.domain.dto;

import cn.hutool.core.util.RandomUtil;
import lombok.SneakyThrows;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Sports implements Runnable {

    private Athlete athlete;
    private CountDownLatch latch;

    public Sports(Athlete athlete, CountDownLatch latch) {
        this.athlete = athlete;
        this.latch = latch;
    }

    @SneakyThrows
    @Override
    public void run() {
        try {
            execute(athlete);
        } finally {
            latch.countDown();
        }
    }

    @SneakyThrows
    private void execute(Athlete athlete) {
        long time = RandomUtil.randomLong(5, 20);
        TimeUnit.SECONDS.sleep(time);
        athlete.setTime(time);
        System.out.printf("运动员%s跑完全程, 耗时%d秒\n", athlete.getName(), time);
    }
}
