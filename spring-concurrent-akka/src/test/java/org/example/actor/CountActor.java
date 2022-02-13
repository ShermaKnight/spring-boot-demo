package org.example.actor;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CountActor extends AbstractActor {

    private final LoggingAdapter logging = Logging.getLogger(getContext().getSystem(), this);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CountData {

        private Integer count;
    }

    @Data
    public static class PrintData {

    }

    private int count = 0;

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(CountData.class, r -> {
            count += r.getCount();
        }).match(PrintData.class, r -> {
            logging.info("current count: {}", String.valueOf(count));
        }).build();
    }
}
