package org.example.model;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class PropertiesFactory {

    @Resource
    private Map<String, PropertiesInterface> cache;

    @Resource
    private List<PropertiesInterface> list;

    public PropertiesInterface getProperties(String type) {
        if (Optional.ofNullable(cache).isPresent()) {
            return cache.get(type);
        }
        return null;
    }

    public List<PropertiesInterface> list() {
        return list;
    }
}
