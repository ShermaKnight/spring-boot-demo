package org.example.service;

import org.example.domain.vo.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse getById(Long id);

    List<UserResponse> getByCondition();
}
