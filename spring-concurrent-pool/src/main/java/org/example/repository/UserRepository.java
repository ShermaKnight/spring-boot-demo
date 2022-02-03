package org.example.repository;

import org.example.domain.dto.UserEntity;

import java.util.List;

public interface UserRepository {

    UserEntity getById(Long id);

    List<UserEntity> getByCondition(Boolean active);
}
