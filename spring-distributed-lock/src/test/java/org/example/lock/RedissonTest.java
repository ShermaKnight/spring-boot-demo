package org.example.lock;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("Redisson实现分布式锁测试用例")
public class RedissonTest {

    @Resource
    private RedissonClient redissonClient;

    @Test
    @SneakyThrows
    @DisplayName("并发调用")
    public void invoke() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        IntStream.range(0, 10).parallel().forEach(index -> {
            long threadId = Thread.currentThread().getId();
            RLock lock = redissonClient.getLock("lock0001");
            try {
                if (lock.tryLock(50, TimeUnit.SECONDS)) {
                    log.info("获取到锁 {} {}", index, threadId);
                    log.info("执行业务逻辑 {} {}", index, threadId);
                    TimeUnit.SECONDS.sleep(15);
                    log.info("业务逻辑执行完成 {} {}", index, threadId);
                } else {
                    log.info("未获取到锁 {} {}", index, threadId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (Optional.ofNullable(lock).isPresent() && lock.isLocked() && lock.isHeldByCurrentThread()) {
                    log.info("自动释放锁 {} {}", index, threadId);
                    lock.unlock();
                }
            }
        });
        countDownLatch.await();
    }

    @Test
    @SneakyThrows
    @DisplayName("常规调用")
    public void normal() {
        RLock lock = redissonClient.getLock("lock0001");
        boolean tryLock = lock.tryLock(50, 10, TimeUnit.SECONDS);
    }
}
