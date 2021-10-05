package org.example.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.example.dao.AddressMapper;
import org.example.dao.UserMapper;
import org.example.entities.AddressEntity;
import org.example.entities.UserEntity;
import org.example.handler.ServiceException;
import org.example.model.UserCreateRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ShardingServiceImpl implements ShardingService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private AddressMapper addressMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(UserCreateRequest createRequest) {
        if (Optional.ofNullable(createRequest).isPresent()) {
            UserEntity userEntity = new UserEntity();
            BeanUtils.copyProperties(createRequest, userEntity);
            Date date = new Date();
            userEntity.setCreateTime(date);
            userEntity.setUpdateTime(date);
            userMapper.insert(userEntity);
            if (Optional.ofNullable(userEntity.getId()).isPresent() && userEntity.getId() > 0) {
                if (CollectionUtils.isNotEmpty(createRequest.getAddresses())) {
                    createRequest.getAddresses().stream()
                            .filter(request -> StringUtils.isNotEmpty(request.getReceiver()) && StringUtils.isNotEmpty(request.getAddress()))
                            .forEach(request -> {
                                AddressEntity addressEntity = new AddressEntity();
                                BeanUtils.copyProperties(request, addressEntity);
                                addressEntity.setUserId(userEntity.getId());
                                addressEntity.setCreateTime(date);
                                addressEntity.setUpdateTime(date);
                                addressMapper.insert(addressEntity);
                            });
                }
                return;
            }
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "插入数据失败");
        }
        throw new ServiceException(HttpStatus.BAD_REQUEST.value(), "参数不合法");
    }

    @Override
    public UserEntity getUser(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public AddressEntity getAddress(Long id) {
        return addressMapper.selectById(id);
    }
}
