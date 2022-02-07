package org.example.service;

import com.google.common.cache.LoadingCache;
import lombok.SneakyThrows;
import org.example.domain.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private LoadingCache<Integer, User> userLoadingCache;

    public UserServiceImpl(LoadingCache<Integer, User> userLoadingCache) {
        this.userLoadingCache = userLoadingCache;
    }

    @Override
    @SneakyThrows
    public User getById(Integer id) {
        return userLoadingCache.get(id);
    }
}
