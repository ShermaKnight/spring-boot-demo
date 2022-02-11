package org.example.domain.dto;

import cn.hutool.core.bean.BeanUtil;
import com.lmax.disruptor.RingBuffer;

public class KillEventProducer {

    private RingBuffer<KillEvent> ringBuffer;

    public KillEventProducer(RingBuffer<KillEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void kill(KillEvent event) {
        long sequence = ringBuffer.next();
        try {
            KillEvent killEvent = ringBuffer.get(sequence);
            BeanUtil.copyProperties(event, killEvent);
        } finally {
            ringBuffer.publish(sequence);
        }
    }

    public void kill(String sequenceId, Long userId) {
        ringBuffer.publishEvent((event, sequence, args) -> {
            event.setSequenceId((String) args[0]);
            event.setUserId((Long) args[1]);
        }, sequenceId, userId);
    }
}
