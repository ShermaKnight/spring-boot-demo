package org.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
@RunWith(SpringRunner.class)
public class WebFluxTest {

    @Test
    public void fluxTest() {
        List<String> filter = Stream.of(",", ".", ";").collect(Collectors.toList());
        String content = "When grace is lost from life, come with a burst of song.";
        Flux.fromArray(content.split(" "))
                .map(f -> f.trim().toLowerCase(Locale.ROOT))
                .flatMap(f -> Flux.fromArray(f.split("")))
                .distinct()
                .filter(f -> !filter.contains(f))
                .sort()
                .subscribe(f -> System.out.print(f + " "));
    }
}
