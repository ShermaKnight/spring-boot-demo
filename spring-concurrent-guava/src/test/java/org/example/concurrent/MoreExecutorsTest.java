package org.example.concurrent;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.RandomUtil;
import com.google.common.util.concurrent.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@DisplayName("MoreExecutors测试用例")
public class MoreExecutorsTest {

    @Test
    @SneakyThrows
    @DisplayName("异步执行任务完毕之后回调")
    public void asynchronousListener() {
        ExecutorService delegate = Executors.newFixedThreadPool(5);
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(delegate);
        ListenableFuture<Integer> submit = executorService.submit(() -> {
            System.out.println(Thread.currentThread().getName() + ": " + System.currentTimeMillis());
            TimeUnit.SECONDS.sleep(2);
            int data = RandomUtil.randomInt();
            System.out.println(Thread.currentThread().getName() + ": " + data);
            return data;
        });
        submit.addListener(() -> {
            System.out.println("任务完成");
        }, MoreExecutors.directExecutor());
        System.out.println("获取返回结果: " + submit.get());
    }

    @Test
    @SneakyThrows
    @DisplayName("异步执行任务完毕之后回调")
    public void asynchronousCallback() {
        ExecutorService delegate = Executors.newFixedThreadPool(5);
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(delegate);
        ListenableFuture<Integer> submit = executorService.submit(() -> {
            System.out.println(Thread.currentThread().getName() + ": " + System.currentTimeMillis());
            TimeUnit.SECONDS.sleep(2);
            int data = RandomUtil.randomInt();
            System.out.println(Thread.currentThread().getName() + ": " + data);
            return data;
        });
        Futures.addCallback(submit, new FutureCallback<Integer>() {
            @Override
            public void onSuccess(Integer result) {
                System.out.println("任务完成: " + result);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        }, MoreExecutors.directExecutor());
        System.out.println("获取返回结果: " + submit.get());
    }

    @Test
    @SneakyThrows
    @DisplayName("批量获取异步结果")
    public void getAsynchronousResultInBatch() {
        ExecutorService delegate = Executors.newFixedThreadPool(5);
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(delegate);
        List<ListenableFuture<Integer>> futures = new ArrayList<>();
        IntStream.range(0, 10).forEach(i -> {
            int data = i;
            futures.add(executorService.submit(() -> {
                System.out.println(Thread.currentThread().getName() + ": " + System.currentTimeMillis());
                TimeUnit.SECONDS.sleep(2);
                System.out.println(Thread.currentThread().getName() + ": " + data);
                return data;
            }));
        });
        List<Integer> integers = Futures.allAsList(futures).get();
        integers.stream().forEach(System.out::println);
    }

    @Test
    @SneakyThrows
    @DisplayName("批量异步执行任务完毕之后回调")
    public void asynchronousCallbackInBatch() {
        ExecutorService delegate = Executors.newFixedThreadPool(5);
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(delegate);
        List<ListenableFuture<Integer>> futures = new ArrayList<>();
        IntStream.range(0, 10).forEach(i -> {
            int data = i;
            futures.add(executorService.submit(() -> {
                System.out.println(Thread.currentThread().getName() + ": " + System.currentTimeMillis());
                TimeUnit.SECONDS.sleep(2);
                System.out.println(Thread.currentThread().getName() + ": " + data);
                return data;
            }));
        });
        ListenableFuture<List<Integer>> listenableFuture = Futures.allAsList(futures);
        Futures.addCallback(listenableFuture, new FutureCallback<List<Integer>>() {
            @Override
            public void onSuccess(List<Integer> result) {
                Integer sum = result.stream().reduce(Integer::sum).get();
                System.out.println("求和: " + sum);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        }, MoreExecutors.directExecutor());
        TimeUnit.SECONDS.sleep(20);
    }
}
