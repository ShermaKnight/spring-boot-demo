package org.example.lock;

import cn.hutool.core.util.RandomUtil;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.SneakyThrows;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@DisplayName("ReentrantLock测试用例")
public class ReentrantLockTest {

    private ReentrantLock lock = new ReentrantLock();
    private static final List<String> FILE_PATHS = new ArrayList<String>() {
        {
            add("D:/Document/1.txt");
            add("D:/Document/2.txt");
            add("D:/Document/3.txt");
            add("D:/Document/4.txt");
        }
    };

    private void merge(String filePath, List<String> deltaLines) {
        lock.lock();
        try {
            Path path = Paths.get(filePath);
            List<String> fileLines = Files.exists(path) ? Files.readAllLines(path) : new ArrayList<>();
            fileLines.addAll(deltaLines);
            fileLines.sort(Comparator.naturalOrder());
            Files.write(path, fileLines);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Test
    @SneakyThrows
    @DisplayName("可重入锁")
    public void reentrantLock() {
        ExecutorService delegate = Executors.newCachedThreadPool();
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(delegate);
        List<? extends ListenableFuture<?>> futures = IntStream.range(0, 1000).boxed().map(i -> executorService.submit(() -> {
            List<String> deltaLines = Stream.of(RandomUtil.randomStringUpper(10)).collect(Collectors.toList());
            merge(RandomUtil.randomEle(FILE_PATHS), deltaLines);
        })).collect(Collectors.toList());
        Futures.allAsList(futures).get();
    }
}
