package org.example.disruptor;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;

@Data
@Slf4j
public class Consumer implements EventHandler<Product>, WorkHandler<Product> {

    private int count = 0;
    private CountDownLatch latch;

    public Consumer(CountDownLatch latch) {
        this.latch = latch;
    }

    public Consumer() {
    }

    @Override
    public void onEvent(Product product, long l, boolean b) throws Exception {
        execute(product);
    }

    @Override
    public void onEvent(Product event) throws Exception {
        execute(event);
    }

    private void execute(Product product) {
        count++;
        log.info("{}", product);
        if (Optional.ofNullable(latch).isPresent() && count == Producer.NUMBER) {
            latch.countDown();
        }
    }
}
