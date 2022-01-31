package org.example.concurrent;

import cn.hutool.core.util.RandomUtil;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DisplayName("CompletableFuture测试用例")
public class CompletableFutureTest {

    @Test
    @SneakyThrows
    @DisplayName("CompletableFuture静态方法runAsync")
    public void runAsyncTest() {
        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                System.out.println(RandomUtil.randomStringUpper(20));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).get();
    }

    @Test
    @SneakyThrows
    @DisplayName("CompletableFuture静态方法supplyAsync")
    public void supplyAsyncTest() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        CompletableFuture<String> future = CompletableFuture.supplyAsync(new InnerTask(countDownLatch));
        countDownLatch.await();
        System.out.println(future.get());
    }

    @Test
    @SneakyThrows
    @DisplayName("CompletableFuture静态方法allOf")
    public void allOfTest() {
        CompletableFuture<String> stringFuture = CompletableFuture.supplyAsync(() -> RandomUtil.randomStringUpper(10));
        CompletableFuture<Double> doubleFuture = CompletableFuture.supplyAsync(() -> RandomUtil.randomDouble(10));
        List<? extends Serializable> list = Stream.of(stringFuture, doubleFuture).map(CompletableFuture::join).collect(Collectors.toList());
        list.stream().forEach(l -> System.out.println(l));
    }

    @Test
    @DisplayName("thenApply进行转换")
    public void thenApplyTest() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> RandomUtil.randomStringUpper(10))
                .thenApplyAsync(r -> r + System.currentTimeMillis());
        System.out.println(future.join());
    }

    @Test
    @DisplayName("thenAccept进行转换")
    public void thenAcceptTest() {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return RandomUtil.randomStringUpper(10);
        }).thenAcceptAsync(r -> System.out.println(r + System.currentTimeMillis()));
        future.join();
        System.out.println("主线程结束");
    }

    @Test
    @DisplayName("thenRun进行转换")
    public void thenRunTest() {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return RandomUtil.randomStringUpper(10);
        }).thenRunAsync(() -> System.out.println(System.currentTimeMillis()));
        future.join();
        System.out.println("主线程结束");
    }

    @Test
    @DisplayName("thenCombine进行合并")
    public void thenCombineTest() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return RandomUtil.randomStringUpper(10);
        }).thenCombineAsync(CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return RandomUtil.randomStringUpper(10);
        }), (r1, r2) -> r1 + r2 + System.currentTimeMillis());
        System.out.println(future.join());
        System.out.println("主线程结束");
    }

    @Test
    @DisplayName("thenAcceptBoth进行合并")
    public void thenAcceptBothTest() {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return RandomUtil.randomStringUpper(10);
        }).thenAcceptBothAsync(CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return RandomUtil.randomStringUpper(10);
        }), (r1, r2) -> System.out.println(r1 + r2 + System.currentTimeMillis()));
        future.join();
        System.out.println("主线程结束");
    }

    @Test
    @DisplayName("runAfterBoth两个CompletionStage运行完后执行")
    public void runAfterBothTest() {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return RandomUtil.randomStringUpper(10);
        }).runAfterBothAsync(CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return RandomUtil.randomStringUpper(10);
        }), () -> System.out.println(System.currentTimeMillis()));
        future.join();
        System.out.println("主线程结束");
    }

    @Test
    @DisplayName("applyToEither返回最快的结果进行处理")
    public void applyToEitherTest() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "first stage";
        }).applyToEitherAsync(CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "second stage";
        }), r -> r);
        System.out.println(future.join());
        System.out.println("主线程结束");
    }

    @Test
    @DisplayName("acceptEither返回最快的结果进行处理")
    public void acceptEitherTest() {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "first stage";
        }).acceptEitherAsync(CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "second stage";
        }), r -> System.out.println(r));
        future.join();
        System.out.println("主线程结束");
    }

    @Data
    private class InnerTask implements Supplier<String> {

        private CountDownLatch countDownLatch;

        public InnerTask(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public String get() {
            try {
                TimeUnit.SECONDS.sleep(RandomUtil.randomInt(3));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }
            return RandomUtil.randomStringUpper(10);
        }
    }
}
