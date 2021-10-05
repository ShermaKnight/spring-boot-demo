package org.example.service;

import org.example.entities.AddressEntity;
import org.example.entities.UserEntity;
import org.example.model.UserCreateRequest;

import java.util.List;

public interface ShardingService {

    void create(UserCreateRequest createRequest);

    UserEntity getUser(Long id);

    AddressEntity getAddress(Long id);
}
