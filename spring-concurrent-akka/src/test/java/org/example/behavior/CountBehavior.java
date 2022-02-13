package org.example.behavior;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;

public class CountBehavior extends AbstractBehavior {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CountData {

        private Integer count;
    }

    @Data
    public static class PrintData {

    }

    private Logger logger;
    private int count = 0;

    public static Behavior create() {
        return Behaviors.setup(CountBehavior::new);
    }

    public CountBehavior(ActorContext context) {
        super(context);
        logger = getContext().getLog();
    }

    @Override
    public Receive createReceive() {
        return newReceiveBuilder().onMessage(Object.class, r -> {
            if (r instanceof CountData) {
                count += ((CountData) r).getCount();
            } else if (r instanceof PrintData) {
                logger.info("current count: {}", count);
            }
            return this;
        }).build();
    }
}
