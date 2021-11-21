package org.example.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.example.service.AutoCommitConsumer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Component
public class BootstrapListener implements ApplicationContextAware {

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private AutoCommitConsumer autoCommitConsumer;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        autoCommitConsumer.consumer();
        log.info("Bootstrap done.");
    }
}
