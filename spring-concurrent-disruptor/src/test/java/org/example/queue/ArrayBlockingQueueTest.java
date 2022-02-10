package org.example.queue;

import cn.hutool.core.util.RandomUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@DisplayName("ArrayBlockingQueue测试用例")
public class ArrayBlockingQueueTest {

    @Test
    @SneakyThrows
    @DisplayName("生产者消费者")
    public void producerAndConsumer() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(1);
        new Thread(() -> {
            IntStream.range(0, 10).boxed().forEach(i -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    queue.put(RandomUtil.randomStringUpper(10));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }).start();
        new Thread(() -> {
            IntStream.range(0, 10).boxed().forEach(i -> {
                try {
                    System.out.println(queue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            countDownLatch.countDown();
        }).start();
        countDownLatch.await();
    }
}
