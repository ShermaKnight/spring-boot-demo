package org.example.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.google.common.util.concurrent.*;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.example.domain.dto.*;
import org.example.domain.vo.BusinessResponse;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@Service
public class BusinessServiceImpl implements BusinessService {

    @Override
    public BusinessResponse getInformation(HttpServletRequest servletRequest) {
        return build(servletRequest);
    }

    private BusinessResponse build(HttpServletRequest servletRequest) {
        BusinessResponse response = new BusinessResponse(new Date());
        response.setPath(servletRequest.getServletPath());
        return response;
    }

    @SneakyThrows
    @Override
    public void running(List<Athlete> athletes, Referee referee) {
        if (CollectionUtils.isEmpty(athletes)) {
            return;
        }
        // runningWithGuava(athletes, referee);
        runningWithDisruptor(athletes, referee);
    }

    @SneakyThrows
    private void runningWithDisruptor(List<Athlete> athletes, Referee referee) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("disruptor-%d").build();
        Disruptor<Athlete> disruptor = new Disruptor<>(() -> new Athlete(), 1024 * 8, threadFactory, ProducerType.MULTI, new YieldingWaitStrategy());
        List<AthleteConsumer> consumers = Stream.of(referee).map(r -> {
            AthleteConsumer consumer = new AthleteConsumer(r);
            return consumer;
        }).collect(Collectors.toList());
        disruptor.handleEventsWith(consumers.toArray(new AthleteConsumer[0]));
        disruptor.start();

        ExecutorService producerExecutor = Executors.newFixedThreadPool(5);
        athletes.stream().forEach(athlete -> {
            AthleteProducer producer = new AthleteProducer(athlete, disruptor.getRingBuffer());
            producerExecutor.submit(producer);
        });

        disruptor.shutdown();
        athletes.stream().sorted(Comparator.comparingDouble(Athlete::getTime)).forEach(System.out::println);
    }

    @SneakyThrows
    private void runningWithGuava(List<Athlete> athletes, Referee referee) {
        CountDownLatch latch = new CountDownLatch(athletes.size());
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(5));
        List<? extends ListenableFuture<?>> futures = athletes.stream().map(athlete -> executorService.submit(new Sports(athlete, latch))).collect(Collectors.toList());
        Futures.allAsList(futures).get();
        athletes.stream().sorted(Comparator.comparingLong(Athlete::getTime)).forEach(System.out::println);
    }

}
