package org.example.actor;

import akka.actor.*;
import akka.pattern.PatternsCS;
import akka.testkit.TestKit;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import scala.concurrent.Future;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@DisplayName("Akka Actor测试用例")
public class AkkaActorTest {

    @Test
    @DisplayName("使用tell方法发送消息")
    public void sendMessageUsingTell() {
        ActorSystem actorSystem = ActorSystem.create("test-system");
        TestKit testKit = new TestKit(actorSystem);
        ActorRef actorRef = actorSystem.actorOf(Props.create(MyActor.class));
        actorRef.tell("printIt", testKit.testActor());
        testKit.expectMsg("Got Message");
    }

    @Test
    @SneakyThrows
    @DisplayName("使用ask方法发送消息")
    public void sendMessageUsingAsk() {
        ActorSystem actorSystem = ActorSystem.create("test-system");
        ActorRef actorRef = actorSystem.actorOf(Props.create(WordCounterActor.class));
        CompletableFuture<Object> future = PatternsCS.ask(actorRef, new WordCounterActor.CountWords("this is a text"), 1000).toCompletableFuture();
        Integer numberOfWords = (Integer) future.get();
        Assert.assertTrue("The actor should count 4 words", 4 == numberOfWords);
    }

    @Test
    @DisplayName("异常处理")
    public void responseWithException() {
        ActorSystem actorSystem = ActorSystem.create("test-system");
        ActorRef actorRef = actorSystem.actorOf(Props.create(WordCounterActor.class));
        CompletableFuture<Object> future = PatternsCS.ask(actorRef, new WordCounterActor.CountWords(""), 1000).toCompletableFuture();
        try {
            future.get(1000, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            System.out.println(e.getMessage());
            Assert.assertTrue("Invalid error message", e.getMessage().contains("The text to process can't be null!"));
        } catch (InterruptedException | TimeoutException e) {
            Assert.fail("Actor should respond with an exception instead of timing out !");
        }
    }

    @Test
    @SneakyThrows
    @DisplayName("统计单词数量")
    public void countTheWordsInText() {
        ActorSystem actorSystem = ActorSystem.create("test-system");
        ActorRef actorRef = actorSystem.actorOf(ReadingActor.props(), "readingActor");
        actorRef.tell(new ReadingActor.ReadLines(TEXT), ActorRef.noSender());
        Future<Terminated> terminateResponse = actorSystem.terminate();
    }

    private static String TEXT = "Lorem Ipsum is simply dummy text\n" +
            "of the printing and typesetting industry.\n" +
            "Lorem Ipsum has been the industry's standard dummy text\n" +
            "ever since the 1500s, when an unknown printer took a galley\n" +
            "of type and scrambled it to make a type specimen book.\n" +
            " It has survived not only five centuries, but also the leap\n" +
            "into electronic typesetting, remaining essentially unchanged.\n" +
            " It was popularised in the 1960s with the release of Letraset\n" +
            " sheets containing Lorem Ipsum passages, and more recently with\n" +
            " desktop publishing software like Aldus PageMaker including\n" +
            "versions of Lorem Ipsum.";

    @Test
    @SneakyThrows
    @DisplayName("并发计数")
    public void count() {
        ActorSystem actorSystem = ActorSystem.create("test-system");
        ActorRef actorRef = actorSystem.actorOf(Props.create(CountActor.class), "countActor");

        ExecutorService delegate = Executors.newFixedThreadPool(8);
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(delegate);
        List<? extends ListenableFuture<?>> futures = IntStream.range(0, 8).boxed().map(i -> executorService.submit(() -> {
            IntStream.range(0, 100000).boxed().forEach(j -> actorRef.tell(new CountActor.CountData(1), ActorRef.noSender()));
        })).collect(Collectors.toList());
        Futures.allAsList(futures).get();

        TimeUnit.SECONDS.sleep(10);
        actorRef.tell(new CountActor.PrintData(), ActorRef.noSender());
        Future<Terminated> terminateResponse = actorSystem.terminate();
    }
}
