package org.example.lock;

import lombok.SneakyThrows;
import org.example.service.BusinessService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.util.concurrent.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("Redis分布式锁测试用例")
public class RedisLockTest {

    @Resource
    private BusinessService businessService;

    @Test
    @SneakyThrows
    @DisplayName("并发调用")
    public void invoke() {
        CountDownLatch countDownLatch = new CountDownLatch(100);
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 100; i++) {
            executorService.submit(new Task(countDownLatch, businessService));
        }
        countDownLatch.await();
        System.out.println("执行结束");
        executorService.shutdown();
    }

    private class Task implements Runnable {

        private CountDownLatch countDownLatch;
        private BusinessService businessService;

        public Task(CountDownLatch countDownLatch, BusinessService businessService) {
            this.countDownLatch = countDownLatch;
            this.businessService = businessService;
        }

        @Override
        public void run() {
            try {
                businessService.decrease();
            } finally {
                countDownLatch.countDown();
            }
        }
    }
}
