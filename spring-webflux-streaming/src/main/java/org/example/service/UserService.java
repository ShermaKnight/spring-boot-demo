package org.example.service;

import org.example.model.UserRequest;
import org.example.model.UserResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface UserService {

    Mono<Boolean> save(UserRequest userRequest);

    Mono<UserResponse> get(Long id);

    Flux<UserResponse> list();
}
