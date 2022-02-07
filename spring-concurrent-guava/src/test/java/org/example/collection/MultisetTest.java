package org.example.collection;

import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
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

    @Test
    @DisplayName("不可变Map")
    public void useImmutableMap() {
        ImmutableMap<Integer, String> words = new ImmutableMap.Builder<Integer, String>()
                .put(1, RandomUtil.randomStringUpper(10))
                .put(2, RandomUtil.randomStringUpper(10))
                .build();
        words.put(3, RandomUtil.randomStringUpper(10));
        words.put(1, RandomUtil.randomStringUpper(10));
    }

    @Test
    @DisplayName("不可变List")
    public void useImmutableList() {
        ImmutableList<String> words = ImmutableList.of("google", "youtube");
        words.add("chrome");
    }
}
