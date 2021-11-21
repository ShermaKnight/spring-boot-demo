package org.example.model;

import java.util.HashMap;
import java.util.Properties;

public interface PropertiesInterface {

    Properties getProperties();

    Properties getProperties(HashMap<String, String> extensions);

    Properties getProperties(String bootstrap, String groupId, String clientId, HashMap<String, String> extensions);
}
