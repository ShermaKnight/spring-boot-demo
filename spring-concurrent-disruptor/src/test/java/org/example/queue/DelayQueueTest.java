package org.example.queue;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@DisplayName("DelayQueue测试用例")
public class DelayQueueTest {

    @Test
    @SneakyThrows
    @DisplayName("延迟队列")
    public void delay() {
        DelayQueue<DelayTask> delayQueue = new DelayQueue<>();
        delayQueue.addAll(IntStream.range(0, 10).boxed().map(i -> {
            DelayTask task = new DelayTask();
            task.setId(i);
            task.setTaskName(RandomUtil.randomStringUpper(10));
            task.setDelayTime(RandomUtil.randomInt(1, 10) * 1000 + System.currentTimeMillis());
            System.out.println(task);
            return task;
        }).collect(Collectors.toList()));
        System.out.println("数据装载完成");

        while (!delayQueue.isEmpty()) {
            System.out.println(delayQueue.take());
        }
    }

    @Data
    private class DelayTask implements Delayed {

        private Integer id;
        private String taskName;
        private long delayTime;

        @Override
        public long getDelay(TimeUnit unit) {
            return delayTime - System.currentTimeMillis();
        }

        @Override
        public int compareTo(Delayed o) {
            DelayTask task = (DelayTask) o;
            return (int) (delayTime - task.getDelayTime());
        }

        @Override
        public String toString() {
            return "DelayTask{" +
                    "id=" + id +
                    ", taskName='" + taskName + '\'' +
                    ", delayTime=" + DateUtil.format(new Date(delayTime), "yyyy-MM-dd HH:mm:ss") +
                    '}';
        }
    }
}
