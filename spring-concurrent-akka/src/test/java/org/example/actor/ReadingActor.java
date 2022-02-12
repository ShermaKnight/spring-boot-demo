package org.example.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.PatternsCS;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ReadingActor extends AbstractActor {

    private final LoggingAdapter logging = Logging.getLogger(getContext().getSystem(), this);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class ReadLines {

        private String text;
    }

    public static Props props() {
        return Props.create(ReadingActor.class);
    }

    @Override
    public void preStart() {
        logging.info("Starting ReadingActor {}", this);
    }

    @Override
    public void postStop() {
        logging.info("Stopping ReadingActor {}", this);
    }

    @Override
    public Receive createReceive() {
        logging.info("Execution receive method");
        return receiveBuilder().match(ReadingActor.ReadLines.class, r -> {
            logging.info("Received ReadLines message from " + getSender());

            String[] lines = r.getText().split("\n");
            List<CompletableFuture> futures = new ArrayList<>();
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                ActorRef wordCounterActorRef = getContext().actorOf(Props.create(WordCounterActor.class), "word-counter-" + i);
                CompletableFuture<Object> future = PatternsCS.ask(wordCounterActorRef, new WordCounterActor.CountWords(line), 1000).toCompletableFuture();
                futures.add(future);
            }

            Integer totalNumberOfWords = futures.stream().map(CompletableFuture::join)
                    .mapToInt(n -> (Integer) n)
                    .sum();
            ActorRef printerActorRef = getContext().actorOf(Props.create(PrinterActor.class), "Printer-Actor");
            printerActorRef.forward(new PrinterActor.PrintFinalResult(totalNumberOfWords), getContext());
        }).build();
    }
}
