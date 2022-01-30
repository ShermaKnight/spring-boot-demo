package org.example.domain.dto;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class RedisEvent extends ApplicationEvent {

    private String serviceName;

    public RedisEvent(Object source, String serviceName) {
        super(source);
        this.serviceName = serviceName;
    }
}
