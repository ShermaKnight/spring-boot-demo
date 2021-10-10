package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class BusinessConsumer {

    private final static String topic = "business";
    private final static String groupId = "business-dev";

    @KafkaListener(topics = topic, groupId = groupId)
    public void consumer(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        try {
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            log.info("{} receive kafka message, partition: {}, offset: {}, value: {}", now, record.partition(), record.offset(), record.value());
            TimeUnit.SECONDS.sleep(1);
            acknowledgment.acknowledge();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
