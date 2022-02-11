package org.example.domain.dto;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.lmax.disruptor.RingBuffer;
import lombok.SneakyThrows;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AthleteProducer implements Runnable {

    private Athlete athlete;
    private CountDownLatch latch;
    private RingBuffer<Athlete> ringBuffer;

    public AthleteProducer(Athlete athlete, CountDownLatch latch, RingBuffer<Athlete> ringBuffer) {
        this.athlete = athlete;
        this.latch = latch;
        this.ringBuffer = ringBuffer;
    }

    public AthleteProducer(Athlete athlete, RingBuffer<Athlete> ringBuffer) {
        this.athlete = athlete;
        this.ringBuffer = ringBuffer;
    }

    @Override
    public void run() {
        try {
            long sequence = ringBuffer.next();
            try {
                Athlete athlete = ringBuffer.get(sequence);
                execute(this.athlete);
                BeanUtil.copyProperties(this.athlete, athlete);
            } finally {
                ringBuffer.publish(sequence);
            }
        } finally {
            if (Optional.ofNullable(latch).isPresent()) {
                latch.countDown();
            }
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
