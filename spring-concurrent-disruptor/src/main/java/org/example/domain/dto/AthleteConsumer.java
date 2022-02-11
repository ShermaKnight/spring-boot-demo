package org.example.domain.dto;

import cn.hutool.core.util.RandomUtil;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

import java.math.RoundingMode;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

public class AthleteConsumer implements WorkHandler<Athlete>, EventHandler<Athlete> {

    private Referee referee;
    private CountDownLatch latch;

    public AthleteConsumer(Referee referee, CountDownLatch latch) {
        this.referee = referee;
        this.latch = latch;
    }

    public AthleteConsumer(Referee referee) {
        this.referee = referee;
    }

    @Override
    public void onEvent(Athlete event) throws Exception {
        try {
            doScore(event);
        } finally {
            if (Optional.ofNullable(latch).isPresent()) {
                latch.countDown();
            }
        }
    }

    @Override
    public void onEvent(Athlete event, long sequence, boolean endOfBatch) throws Exception {
        try {
            doScore(event);
        } finally {
            if (Optional.ofNullable(latch).isPresent()) {
                latch.countDown();
            }
        }
    }

    private void doScore(Athlete event) {
        Double score = RandomUtil.randomDouble(2, RoundingMode.UP);
        event.setScore(score);
        System.out.printf("裁判员%s给运动员%s打分%f\n", referee.getName(), event.getName(), score.doubleValue());
    }
}
