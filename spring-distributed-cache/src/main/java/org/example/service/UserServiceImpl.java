package org.example.service;

import org.example.repository.Address;
import org.example.repository.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Override
    public List<Address> getAddressByUserId(Integer userId) {
        User user = UserRepository.cache.get(userId);
        if (Optional.ofNullable(user).isPresent()) {
            return user.getAddresses();
        }
        return Collections.EMPTY_LIST;
    }
}
