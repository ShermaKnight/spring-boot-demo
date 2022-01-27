package org.example.collection;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

@DisplayName("Multiset测试用例")
public class MultisetTest {

    @Test
    @DisplayName("Multiset使用")
    public void useMultiset() {
        Multiset<String> words = HashMultiset.create();
        words.addAll(Arrays.asList("google", "google", "youtube"));
        words.stream().forEach(word -> {
            System.out.println(word + ", " + words.count(word));
        });
    }
}
