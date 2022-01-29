package org.example.concurrent;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.stream.IntStream;

@DisplayName("ThreadFactoryBuilder测试用例")
public class ThreadFactoryBuilderTest {

    @Test
    @DisplayName("创建线程池")
    @SneakyThrows
    public void createThreadPool() {
        int count = 10;
        CountDownLatch countDownLatch = new CountDownLatch(count);

        LinkedBlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>(1024);
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("file-transport-%d").build();
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2,
                24, 60, TimeUnit.SECONDS, blockingQueue, threadFactory, new ThreadPoolExecutor.AbortPolicy());

        IntStream.range(0, count).forEach(i -> {
            poolExecutor.submit(new Task(countDownLatch, i));
        });
        countDownLatch.await();
        System.out.println("所有线程结束");
    }
}
