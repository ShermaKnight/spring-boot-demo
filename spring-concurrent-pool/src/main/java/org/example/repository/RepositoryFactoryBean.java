package org.example.repository;

import cn.hutool.core.util.RandomUtil;
import org.apache.commons.lang3.StringUtils;
import org.example.domain.dto.UserEntity;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class RepositoryFactoryBean implements FactoryBean<UserRepository> {

    private static final HashMap<Long, UserEntity> userEntities = new HashMap<Long, UserEntity>() {
        {
            IntStream.range(0, 100).forEach(i -> {
                put(new Long(i), new UserEntity(new Long(i), RandomUtil.randomStringUpper(10), RandomUtil.randomBoolean()));
            });
        }
    };

    @Override
    public UserRepository getObject() throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Class[] classes = {UserRepository.class};
        return (UserRepository) Proxy.newProxyInstance(classLoader, classes, (proxy, method, args) -> {
            String methodName = method.getName();
            if (StringUtils.startsWithIgnoreCase(methodName, "getBy")) {
                if (StringUtils.contains(methodName, "getById") && args[0] instanceof Long) {
                    return userEntities.get((Long) args[0]);
                } else if (StringUtils.contains(methodName, "getByCondition") && args[0] instanceof Boolean) {
                    Boolean active = (Boolean) args[0];
                    return userEntities.keySet().stream().filter(key -> active ? userEntities.get(key).isActive() : !userEntities.get(key).isActive())
                            .map(key -> userEntities.get(key)).collect(Collectors.toList());
                }
            }
            return null;
        });
    }

    @Override
    public Class<?> getObjectType() {
        return UserRepository.class;
    }
}
