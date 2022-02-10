package org.example.queue;

import cn.hutool.core.util.RandomUtil;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@DisplayName("PriorityBlockingQueue测试用例")
public class PriorityBlockingQueueTest {

    @Test
    @SneakyThrows
    @DisplayName("优先级队列")
    public void priority() {
        PriorityBlockingQueue<PriorityTask> queue = new PriorityBlockingQueue<>();
        queue.addAll(IntStream.range(0, 10).boxed().map(i -> {
            PriorityTask task = new PriorityTask(i);
            task.setTaskName(RandomUtil.randomStringUpper(10));
            task.setPriority(RandomUtil.randomInt(0, 10));
            return task;
        }).collect(Collectors.toList()));
        while (!queue.isEmpty()) {
            System.out.println(queue.take());
        }
    }

    @Data
    private class PriorityTask implements Comparable<PriorityTask> {

        private Integer id;
        private String taskName;
        private int priority;

        public PriorityTask(Integer id) {
            this.id = id;
        }

        @Override
        public int compareTo(PriorityTask o) {
            return this.priority - o.getPriority();
        }
    }
}
