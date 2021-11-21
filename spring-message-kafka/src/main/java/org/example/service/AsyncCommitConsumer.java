package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.example.model.AsyncCommitProperties;
import org.example.model.AutoCommitProperties;
import org.example.model.PropertiesFactory;
import org.example.util.CharacterUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

@Slf4j
@Component
@SuppressWarnings("all")
public class AsyncCommitConsumer implements Consumer {

    private final static String bootstrap = "192.168.71.128:9092,192.168.71.129:9092,192.168.71.130:9092";
    private final static String topic = "business";
    private final static String groupId = "business-asynccommit";

    @Resource
    private PropertiesFactory propertiesFactory;

    @Async
    @Override
    public void consumer() {
        String type = CharacterUtil.toLowerFirstLetter(AsyncCommitProperties.class.getSimpleName());
        Properties properties = propertiesFactory.getProperties(type).getProperties(bootstrap, groupId);
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList(topic));
        try {
            int count = 0;
            int minCommit = 10;
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
                    String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    log.info("{} receive kafka message, partition: {}, offset: {}, value: {}", now, record.partition(), record.offset(), record.value());
                    count++;
                }
                if (count >= minCommit) {
                    consumer.commitAsync(new OffsetCommitCallback() {
                        @Override
                        public void onComplete(Map<TopicPartition, OffsetAndMetadata> map, Exception e) {
                            if (Optional.ofNullable(e).isPresent()) {
                                log.info("commit failed. {}", e.getMessage());
                            } else {
                                log.info("commit successfully.");
                            }
                        }
                    });
                }
                count = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }
}
