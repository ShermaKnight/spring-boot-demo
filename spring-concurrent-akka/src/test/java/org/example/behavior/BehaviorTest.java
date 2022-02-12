package org.example.behavior;

import akka.actor.typed.ActorSystem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@DisplayName("Behavior测试用例")
public class BehaviorTest {

    @Test
    @DisplayName("hello-world")
    public void helloWorld() {
        final ActorSystem<GreeterMain.SayHello> greeterMain = ActorSystem.create(GreeterMain.create(), "hello-world");
        greeterMain.tell(new GreeterMain.SayHello("Charles"));
        try {
            System.out.println(">>> Press ENTER to exit <<<");
            System.in.read();
        } catch (IOException ignored) {
            //
        } finally {
            greeterMain.terminate();
        }
    }
}
