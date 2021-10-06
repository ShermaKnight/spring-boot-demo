package org.example.controller;

import com.alibaba.fastjson.JSON;
import org.example.model.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    @GetMapping("/hello")
    public Mono<String> hello() {
        return Mono.just("hello, reactive");
    }

    @PostMapping("/")
    public Mono<Boolean> saveUser(@RequestBody UserRequest userRequest) {
        ReactiveHashOperations<String, Object, Object> hashOperations = reactiveStringRedisTemplate.opsForHash();
        return hashOperations.put("USER_HS", String.valueOf(userRequest.getId()), JSON.toJSONString(userRequest));
    }

    @GetMapping("/{id}")
    public Mono<UserRequest> get(@PathVariable Long id) {
        ReactiveHashOperations<String, Object, Object> hashOperations = reactiveStringRedisTemplate.opsForHash();
        Mono<Object> mono = hashOperations.get("USER_HS", String.valueOf(id));
        return mono.map(e -> JSON.parseObject((String) e, UserRequest.class));
    }
}
