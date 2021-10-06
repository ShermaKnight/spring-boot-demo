package org.example.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.User;
import org.example.model.UserRequest;
import org.example.model.UserResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final static String USER_HASH_KEY = "USER_HS";

    @Resource
    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    @Override
    public Mono<Boolean> save(UserRequest userRequest) {
        User user = new User();
        BeanUtils.copyProperties(userRequest, user);
        ReactiveHashOperations<String, Object, Object> hashOperations = reactiveStringRedisTemplate.opsForHash();
        return hashOperations.put(USER_HASH_KEY, String.valueOf(user.getId()), JSON.toJSONString(user));
    }

    @Override
    public Mono<UserResponse> get(Long id) {
        ReactiveHashOperations<String, Object, Object> hashOperations = reactiveStringRedisTemplate.opsForHash();
        return getUserResponse(hashOperations, String.valueOf(id));
    }

    @Override
    public Flux<UserResponse> list() {
        ReactiveHashOperations<String, Object, Object> hashOperations = reactiveStringRedisTemplate.opsForHash();
        Flux<UserResponse> responseFlux = hashOperations.entries(USER_HASH_KEY).map(entry -> buildUserResponse((String) entry.getValue()));
        return responseFlux;
    }

    private Mono<UserResponse> getUserResponse(ReactiveHashOperations hashOperations, String id) {
        Mono<Object> mono = hashOperations.get(USER_HASH_KEY, String.valueOf(id));
        return mono.map(m -> buildUserResponse((String) m));
    }

    private UserResponse buildUserResponse(String o) {
        User user = JSON.parseObject(o, User.class);
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user, userResponse);
        return userResponse;
    }
}
