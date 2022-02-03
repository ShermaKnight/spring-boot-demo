package org.example.pool;

import cn.hutool.core.util.RandomUtil;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.IntStream;

@DisplayName("池化测试用例")
public class CommonPoolTest {

    @Test
    @SneakyThrows
    @DisplayName("默认实现")
    public void defaultImplement() {
        GenericObjectPool<Mark> pool = new GenericObjectPool<>(new BasePooledObjectFactory<Mark>() {
            @Override
            public Mark create() throws Exception {
                return new Mark(RandomUtil.randomStringUpper(10));
            }

            @Override
            public PooledObject<Mark> wrap(Mark mark) {
                return new DefaultPooledObject<>(mark);
            }
        });
        pool.setMaxTotal(8);
        pool.setMaxIdle(4);
        pool.setMinIdle(2);
        print(pool);

        ExecutorService delegate = Executors.newFixedThreadPool(10);
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(delegate);
        List<ListenableFuture<String>> futures = new ArrayList<>();
        IntStream.range(0, 20).forEach(i -> {
            futures.add(executorService.submit(() -> {
                Mark mark = null;
                try {
                    mark = pool.borrowObject();
                    TimeUnit.SECONDS.sleep(RandomUtil.randomInt(3));
                    print(pool);
                    return Thread.currentThread().getName() + ", " + mark.getName();
                } catch (Exception e) {
                    pool.invalidateObject(mark);
                } finally {
                    if (Optional.ofNullable(mark).isPresent()) {
                        pool.returnObject(mark);
                    }
                }
                return "";
            }));
        });
        List<String> list = Futures.allAsList(futures).get();
        list.stream().forEach(System.out::println);
        print(pool);
    }

    private void print(GenericObjectPool pool) {
        System.out.println(" [active: " + pool.getNumActive() + ", idle: " + pool.getNumIdle() + ", wait: " + pool.getNumWaiters() + "]");
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private class Mark {

        private String name;
    }
}
