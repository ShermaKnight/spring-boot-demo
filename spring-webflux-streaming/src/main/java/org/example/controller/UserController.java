package org.example.controller;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import org.example.model.UserRequest;
import org.example.model.UserResponse;
import org.example.service.UserService;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/hello")
    public Mono<String> hello() {
        return Mono.just("hello, reactive");
    }

    @PostMapping("/")
    public Mono<Boolean> saveUser(@Validated @RequestBody UserRequest userRequest) {
        return userService.save(userRequest);
    }

    @GetMapping("/{id}")
    public Mono<UserResponse> get(@PathVariable Long id) {
        return userService.get(id);
    }

    @GetMapping("/")
    public Flux<UserResponse> list() {
        return userService.list();
    }
}
