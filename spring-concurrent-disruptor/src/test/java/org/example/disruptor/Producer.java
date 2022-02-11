package org.example.disruptor;

import cn.hutool.core.util.RandomUtil;
import com.lmax.disruptor.RingBuffer;

import java.math.RoundingMode;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Producer implements Runnable {

    public static final int NUMBER = 10;

    private static AtomicInteger ID_INCREMENT = new AtomicInteger(0);

    private CountDownLatch latch;
    private RingBuffer<Product> ringBuffer;

    public Producer(CountDownLatch latch, RingBuffer<Product> ringBuffer) {
        this.latch = latch;
        this.ringBuffer = ringBuffer;
    }

    public Producer(RingBuffer<Product> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    @Override
    public void run() {
        try {
            IntStream.range(0, NUMBER).boxed().forEach(i -> {
                long sequence = ringBuffer.next();
                try {
                    Product product = ringBuffer.get(sequence);
                    product.setId(ID_INCREMENT.getAndIncrement());
                    product.setName(RandomUtil.randomStringUpper(10));
                    product.setWeight(RandomUtil.randomDouble(2, RoundingMode.UP));
                } finally {
                    ringBuffer.publish(sequence);
                }
            });
        } finally {
            if (Optional.ofNullable(latch).isPresent()) {
                latch.countDown();
            }
        }
    }
}
