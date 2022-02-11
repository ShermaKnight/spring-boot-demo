package org.example.domain.dto;

import com.lmax.disruptor.EventFactory;

public class KillEventFactory implements EventFactory<KillEvent> {

    @Override
    public KillEvent newInstance() {
        return new KillEvent();
    }
}
