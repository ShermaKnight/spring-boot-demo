package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.model.AutoCommitProperties;
import org.example.model.AsyncCommitProperties;
import org.example.model.PropertiesFactory;
import org.example.model.PropertiesInterface;
import org.example.util.CharacterUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class PropertiesFactoryTest {

    @Autowired
    private PropertiesFactory propertiesFactory;

    @Test
    public void build() {
        log.info("properties implement: {}", propertiesFactory.list());
        PropertiesInterface properties = propertiesFactory.getProperties(CharacterUtil.toLowerFirstLetter(AutoCommitProperties.class.getSimpleName()));
        log.info("get properties: {}", properties.getProperties());
        properties = propertiesFactory.getProperties(CharacterUtil.toLowerFirstLetter(AsyncCommitProperties.class.getSimpleName()));
        log.info("get properties: {}", properties.getProperties());
    }

}
