package org.example.service;

import org.example.repository.Address;

import java.util.List;

public interface UserService {

    List<Address> getAddressByUserId(Integer userId);

    boolean reload();
}
