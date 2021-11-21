package org.example.model;

import java.util.HashMap;
import java.util.Optional;
import java.util.Properties;

public abstract class PropertiesAbstract implements PropertiesInterface {

    protected abstract Properties bootstrap();

    @Override
    public Properties getProperties() {
        return bootstrap();
    }

    @Override
    public Properties getProperties(String bootstrap, String groupId) {
        HashMap<String, String> extensions = new HashMap<>();
        extensions.putIfAbsent("bootstrap.servers", bootstrap);
        extensions.putIfAbsent("group.id", groupId);
        return getProperties(extensions);
    }

    @Override
    public Properties getProperties(HashMap<String, String> extensions) {
        Properties properties = bootstrap();
        if (Optional.ofNullable(extensions).isPresent() && extensions.size() > 0) {
            extensions.keySet().stream().forEach(key -> {
                properties.put(key, extensions.get(key));
            });
        }
        return properties;
    }

    @Override
    public Properties getProperties(String bootstrap, String groupId, String clientId, HashMap<String, String> extensions) {
        extensions.putIfAbsent("bootstrap.servers", bootstrap);
        extensions.putIfAbsent("group.id", groupId);
        extensions.putIfAbsent("client.id", clientId);
        return getProperties(extensions);
    }
}
