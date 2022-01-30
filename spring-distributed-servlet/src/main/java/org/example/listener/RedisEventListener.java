package org.example.listener;

import cn.hutool.core.util.RandomUtil;
import org.example.domain.dto.RedisEvent;
import org.example.service.ConfigureService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class RedisEventListener {

    @Resource
    private ConfigureService configureService;

    @EventListener
    public void change(RedisEvent redisEvent) {
        String serviceName = redisEvent.getServiceName();
        List<String> data = Stream.of(
                "application.mysql.host: 192.168.71.128",
                "application.mysql.port: 3306",
                "application.mysql.username: root",
                "application.mysql.password: wproot",
                "application.random: " + RandomUtil.randomStringUpper(10)
        ).collect(Collectors.toList());
        System.out.println(data);
        configureService.configureChanged(serviceName, data);
    }
}
