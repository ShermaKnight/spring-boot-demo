package org.example.service;

import org.example.repository.Address;
import org.example.repository.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("userCollectService")
public class UserCollectServiceImpl implements UserService {

    @Override
    public List<Address> getAddressByUserId(Integer userId) {
        User user = UserRepository.cache.get(userId);
        if (Optional.ofNullable(user).isPresent()) {
            return user.getAddresses().stream().filter(address -> address.isCollect()).collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }
}
