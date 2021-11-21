package org.example.service;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Properties;

@Component
public class AutoCommitConsumer {

    private final static String topic = "business-normal";
    private final static String groupId = "business-autocommit";

    @Async
    public void consumer() {
//        Properties props = new Properties();
//        props.put("bootstrap.servers", "192.168.71.128:9092,192.168.71.129:9092,192.168.71.130:9092");
//        props.put("group.id", groupId);
//        props.put("client.id", groupId + "-01");
//        props.put("enable.auto.commit", true);
//        props.put("auto.commit.interval.ms", 1000);
//        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
//        consumer.subscribe(Arrays.asList(topic));
    }
}
