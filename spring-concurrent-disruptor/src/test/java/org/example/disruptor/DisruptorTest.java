package org.example.disruptor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@DisplayName("Disruptor测试用例")
public class DisruptorTest {

    @Test
    @SneakyThrows
    @DisplayName("单消费者")
    public void singleConsumerTest() {
        CountDownLatch latch = new CountDownLatch(2);
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("disruptor-%d").build();
        Disruptor<Product> disruptor = new Disruptor<>(() -> new Product(), 1024 * 8, threadFactory, ProducerType.SINGLE, new YieldingWaitStrategy());
        Consumer consumer = new Consumer(latch);
        disruptor.handleEventsWith(consumer);
        disruptor.start();

        ExecutorService producerExecutor = Executors.newFixedThreadPool(1);
        Producer producer = new Producer(latch, disruptor.getRingBuffer());
        producerExecutor.submit(producer);

        latch.await();
        disruptor.shutdown();
        producerExecutor.shutdown();
    }

    @Test
    @SneakyThrows
    @DisplayName("多生产者多消费者")
    public void MultipartProducerAndConsumerTest() {
        int processor = Runtime.getRuntime().availableProcessors() * 2;
        RingBuffer<Product> ringBuffer = RingBuffer.create(ProducerType.MULTI, () -> new Product(), 1024 * 8, new YieldingWaitStrategy());
        List<Consumer> consumers = IntStream.range(0, processor).boxed().map(i -> new Consumer()).collect(Collectors.toList());
        WorkerPool<Product> workerPool = new WorkerPool<>(ringBuffer, ringBuffer.newBarrier(), new IgnoreExceptionHandler(), consumers.toArray(new Consumer[0]));
        Sequence[] sequences = workerPool.getWorkerSequences();
        ringBuffer.addGatingSequences(sequences);
        ExecutorService consumerExecutor = Executors.newFixedThreadPool(processor);
        workerPool.start(consumerExecutor);

        ExecutorService producerExecutor = Executors.newFixedThreadPool(5);
        IntStream.range(0, 5).boxed().forEach(i -> {
            Producer producer = new Producer(ringBuffer);
            producerExecutor.submit(producer);
        });
        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    @SneakyThrows
    @DisplayName("多消费者不可重复消费")
    public void MultipartConsumerTest() {
        CountDownLatch latch = new CountDownLatch(3);
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("disruptor-%d").build();
        Disruptor<Product> disruptor = new Disruptor<>(() -> new Product(), 1024 * 8, threadFactory, ProducerType.SINGLE, new YieldingWaitStrategy());
        List<Consumer> consumers = IntStream.range(0, 2).boxed().map(i -> new Consumer(latch)).collect(Collectors.toList());
        disruptor.handleEventsWithWorkerPool(consumers.toArray(new Consumer[0]));
        disruptor.start();

        ExecutorService producerExecutor = Executors.newFixedThreadPool(1);
        Producer producer = new Producer(latch, disruptor.getRingBuffer());
        producerExecutor.submit(producer);

        latch.await();
        disruptor.shutdown();
        producerExecutor.shutdown();
    }

    @Test
    @SneakyThrows
    @DisplayName("多消费者可重复消费")
    public void MultipartConsumerRepeatTest() {
        CountDownLatch latch = new CountDownLatch(3);
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("disruptor-%d").build();
        Disruptor<Product> disruptor = new Disruptor<>(() -> new Product(), 1024 * 8, threadFactory, ProducerType.SINGLE, new YieldingWaitStrategy());
        List<Consumer> consumers = IntStream.range(0, 2).boxed().map(i -> new Consumer(latch)).collect(Collectors.toList());
        disruptor.handleEventsWith(consumers.toArray(new Consumer[0]));
        disruptor.start();

        ExecutorService producerExecutor = Executors.newFixedThreadPool(1);
        Producer producer = new Producer(latch, disruptor.getRingBuffer());
        producerExecutor.submit(producer);

        latch.await();
        disruptor.shutdown();
        producerExecutor.shutdown();
    }
}
