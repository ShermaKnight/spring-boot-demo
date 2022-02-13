package org.example.behavior;

import akka.actor.typed.ActorSystem;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@DisplayName("Akka Behavior测试用例")
public class AkkaBehaviorTest {

    @Test
    @DisplayName("hello-world")
    public void helloWorld() {
        final ActorSystem<GreeterMain.SayHello> greeterMain = ActorSystem.create(GreeterMain.create(), "test-system");
        greeterMain.tell(new GreeterMain.SayHello("Charles"));
        greeterMain.terminate();
    }

    @Test
    @SneakyThrows
    @DisplayName("并发计数")
    public void count() {
        ActorSystem actorSystem = ActorSystem.create(CountBehavior.create(), "test-system");

        ExecutorService delegate = Executors.newFixedThreadPool(8);
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(delegate);
        List<? extends ListenableFuture<?>> futures = IntStream.range(0, 8).boxed().map(i -> executorService.submit(() -> {
            IntStream.range(0, 100000).boxed().forEach(j -> actorSystem.tell(new CountBehavior.CountData(1)));
        })).collect(Collectors.toList());
        Futures.allAsList(futures).get();

        TimeUnit.SECONDS.sleep(10);
        actorSystem.tell(new CountBehavior.PrintData());
        actorSystem.terminate();
    }
}
