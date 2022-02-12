package org.example.actor;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PrinterActor extends AbstractActor {

    private final LoggingAdapter logging = Logging.getLogger(getContext().getSystem(), this);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class PrintFinalResult {

        private Integer totalNumberOfWords;
    }

    @Override
    public void preStart() {
        logging.info("Starting PrinterActor {}", this);
    }

    @Override
    public void postStop() {
        logging.info("Stopping PrinterActor {}", this);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(PrinterActor.PrintFinalResult.class, r -> {
            logging.info("Received PrintFinalResult message from " + getSender());
            logging.info("The text has a total number of {} words", r.totalNumberOfWords);
        }).build();
    }
}
