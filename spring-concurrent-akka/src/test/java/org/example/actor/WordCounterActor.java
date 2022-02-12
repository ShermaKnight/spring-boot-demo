package org.example.actor;

import akka.actor.AbstractActor;
import akka.actor.Status;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

public class WordCounterActor extends AbstractActor {

    private final LoggingAdapter logging = Logging.getLogger(getContext().getSystem(), this);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class CountWords {

        private String line;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(WordCounterActor.CountWords.class, r -> {
            try {
                logging.info("Received CountWords message from " + getSender());
                getSender().tell(countWordsFromLine(r.line), getSelf());
            } catch (Exception e) {
                getSender().tell(new Status.Failure(e), getSelf());
            }
        }).build();
    }

    private int countWordsFromLine(String line) {
        if (StringUtils.isEmpty(line)) {
            throw new IllegalArgumentException("The text to process can't be null!");
        }
        int numberOfWords = 0;
        String[] words = line.split(" ");
        for (String possibleWord : words) {
            if (possibleWord.trim().length() > 0) {
                numberOfWords++;
            }
        }
        return numberOfWords;
    }
}
