package org.example.listener;

import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentChangeListener {

    @EventListener
    public void listener(EnvironmentChangeEvent environmentChangeEvent) {
        System.out.println("Environment change.");
    }
}
