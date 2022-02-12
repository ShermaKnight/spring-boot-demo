package org.example.actor;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

public class MyActor extends AbstractActor {

    private final LoggingAdapter logging = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public void postStop() throws Exception {
        logging.info("Stopping actor {}", this);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().matchEquals("printIt", p -> {
            logging.info("The address of this actor is: {}", getSelf());
            getSender().tell("Got Message", getSelf());
        }).build();
    }
}
