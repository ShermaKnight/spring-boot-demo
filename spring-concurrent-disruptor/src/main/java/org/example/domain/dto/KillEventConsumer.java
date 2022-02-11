package org.example.domain.dto;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

public class KillEventConsumer implements EventHandler<KillEvent>, WorkHandler<KillEvent> {

    @Override
    public void onEvent(KillEvent event) throws Exception {
        String threadName = Thread.currentThread().getName();
        System.out.printf("线程: %s, 请求traceId: %s, 用户Id: %d, 实现秒杀\n", threadName, event.getSequenceId(), event.getUserId());
    }

    @Override
    public void onEvent(KillEvent event, long sequence, boolean endOfBatch) throws Exception {
        String threadName = Thread.currentThread().getName();
        System.out.printf("线程: %s, 请求traceId: %s, 用户Id: %d, 实现秒杀\n", threadName, event.getSequenceId(), event.getUserId());
    }
}
