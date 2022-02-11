package org.example.service;

import cn.hutool.core.lang.UUID;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.example.disruptor.Consumer;
import org.example.domain.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("OrderService测试用例")
public class OrderServiceTest {

    @Resource
    private OrderService orderService;

    @Test
    @DisplayName("运行测试")
    public void runningTest() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("disruptor-%d").build();
        Disruptor<KillEvent> disruptor = new Disruptor<>(new KillEventFactory(), 1024 * 1024, threadFactory, ProducerType.MULTI, new YieldingWaitStrategy());
        List<KillEventConsumer> consumers = IntStream.range(0, 16).boxed().map(i -> new KillEventConsumer()).collect(Collectors.toList());
        disruptor.handleEventsWithWorkerPool(consumers.toArray(new KillEventConsumer[0]));
        disruptor.start();

        KillEventProducer producer = new KillEventProducer(disruptor.getRingBuffer());
        LongStream.range(0, 10000).parallel().boxed().forEach(i -> producer.kill(UUID.fastUUID().toString(true), i));
        disruptor.shutdown();
    }

}
