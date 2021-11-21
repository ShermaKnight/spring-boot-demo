package org.example.model;

import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class AsyncCommitProperties extends PropertiesAbstract implements PropertiesInterface {

    @Override
    protected Properties bootstrap() {
        Properties properties = new Properties();
        properties.put("enable.auto.commit", false);
        properties.put("auto.offset.reset", "earliest");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return properties;
    }

}
