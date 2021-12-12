package org.example.repository;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

public class UserRepository {

    private static ConcurrentHashMap<Integer, User> cache = new ConcurrentHashMap<>();

    static {
        IntStream.range(0, 10).forEach(i -> cache.put(i, build(i)));
    }

    private static User build(Integer id) {
        User user = new User();
        user.setId(id);
        user.setUserName();
    }
}
