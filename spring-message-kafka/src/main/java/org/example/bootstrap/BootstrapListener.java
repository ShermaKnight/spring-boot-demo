package org.example.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.example.service.Consumer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Component
public class BootstrapListener implements ApplicationContextAware {

    @Resource
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Map<String, Consumer> beans = applicationContext.getBeansOfType(Consumer.class);
        beans.keySet().parallelStream().forEach(key -> {
            beans.get(key).consumer();
        });
        log.info("Bootstrap done.");
    }
}
