package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.example.model.AutoCommitProperties;
import org.example.model.PropertiesFactory;
import org.example.model.PropertiesInterface;
import org.example.util.CharacterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Properties;

@Slf4j
@Component
public class AutoCommitConsumer {

    private final static String bootstrap = "192.168.71.128:9092,192.168.71.129:9092,192.168.71.130:9092";
    private final static String topic = "business";
    private final static String groupId = "business-autocommit";

    @Resource
    private PropertiesFactory propertiesFactory;

    @Async
    public void consumer() {
        String type = CharacterUtil.toLowerFirstLetter(AutoCommitProperties.class.getSimpleName());
        Properties properties = propertiesFactory.getProperties(type).getProperties(bootstrap, groupId);
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList(topic));
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
                    String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    log.info("{} receive kafka message, partition: {}, offset: {}, value: {}", now, record.partition(), record.offset(), record.value());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
