package org.example.service;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.cache.LoadingCache;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.example.domain.entity.User;
import org.example.domain.vo.UserUpdateRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Override
    public boolean updateById(Integer id, UserUpdateRequest updateRequest) {
        User user = userLoadingCache.getIfPresent(id);
        if (Optional.of(user).isPresent()) {
            if (StringUtils.isNoneEmpty(updateRequest.getUserName())) {
                user.setUserName(updateRequest.getUserName());
            }
            if (Optional.ofNullable(updateRequest.getSalary()).isPresent()) {
                user.setSalary(user.getSalary());
            }
            userLoadingCache.invalidate(id);
            userLoadingCache.put(id, user);
        }
        return true;
    }
}
