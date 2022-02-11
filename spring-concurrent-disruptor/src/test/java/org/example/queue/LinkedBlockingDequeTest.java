package org.example.queue;

import cn.hutool.core.util.RandomUtil;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@DisplayName("LinkedBlockingDeque测试用例")
public class LinkedBlockingDequeTest {

    private static final AtomicInteger product = new AtomicInteger(1);

    private class Producer implements Runnable {

        private List<LinkedBlockingDeque<String>> workers;

        public Producer(List<LinkedBlockingDeque<String>> workers) {
            this.workers = workers;
        }

        @SneakyThrows
        @Override
        public void run() {
            if (CollectionUtils.isNotEmpty(workers)) {
                while (true) {
                    IntStream.range(0, 20).boxed().forEach(i -> {
                        try {
                            int index = RandomUtil.randomInt(0, workers.size());
                            LinkedBlockingDeque<String> deque = workers.get(index);
                            String data = String.valueOf(product.getAndIncrement());
                            deque.put(data);
                            System.out.printf("producer index: %d, data: %s\n", index, data);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                    TimeUnit.SECONDS.sleep(1);
                }
            }
        }
    }

    private class Consumer<T> implements Runnable {

        private CountDownLatch latch;
        private List<LinkedBlockingDeque<T>> workers;
        private int index;

        public Consumer(CountDownLatch latch, List<LinkedBlockingDeque<T>> workers, int index) {
            this.latch = latch;
            this.workers = workers;
            this.index = index;
        }

        @SneakyThrows
        @Override
        public void run() {
            try {
                if (CollectionUtils.isNotEmpty(workers) && index < workers.size() && index >= 0) {
                    while (true) {
                        LinkedBlockingDeque<T> deque = workers.get(index);
                        while (!deque.isEmpty()) {
                            String threadName = Thread.currentThread().getName();
                            System.out.printf("worker name: %s, state: ok, index: %d, data: %s\n", threadName, index, deque.takeFirst());
                        }
                        execute(index);
                    }
                }
            } finally {
                latch.countDown();
            }
        }

        @SneakyThrows
        private void execute(int index) {
            if (workers.size() > 1) {
                int x = RandomUtil.randomInt(0, workers.size());
                while (x == index) {
                    x = RandomUtil.randomInt(0, workers.size());
                }
                LinkedBlockingDeque<T> deque = workers.get(x);
                while (workers.get(index).isEmpty() && !deque.isEmpty()) {
                    String threadName = Thread.currentThread().getName();
                    System.out.printf("worker name: %s, state: steal, index: %d, data: %s\n", threadName, x, deque.takeLast());
                }
            }
        }
    }

    @Test
    @SneakyThrows
    @DisplayName("工作窃取算法")
    public void useDeque() {
        List<LinkedBlockingDeque<String>> workers = Stream.of(
                new LinkedBlockingDeque<String>(),
                new LinkedBlockingDeque<String>()
        ).collect(Collectors.toList());
        new Thread(new Producer(workers)).start();
        CountDownLatch latch = new CountDownLatch(workers.size());
        IntStream.range(0, 2).boxed().forEach(i -> {
            new Thread(new Consumer<String>(latch, workers, i)).start();
        });
        latch.await();
    }
}
