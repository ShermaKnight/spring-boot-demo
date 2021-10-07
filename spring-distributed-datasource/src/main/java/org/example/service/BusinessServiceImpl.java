package org.example.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.example.entities.OrderEntity;
import org.example.entities.UserEntity;
import org.example.handler.ServiceException;
import org.example.model.BusinessResponse;
import org.example.repository.OrderRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class BusinessServiceImpl implements BusinessService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private OrderRepository orderRepository;

    @Override
    public BusinessResponse getBusinessById(Long id) {
        UserEntity userEntity = getById(id);
        if (Optional.ofNullable(userEntity).isPresent()) {
            BusinessResponse response = new BusinessResponse();
            BeanUtils.copyProperties(userEntity, response);
            List<OrderEntity> orders = getByCondition(userEntity.getId());
            if (CollectionUtils.isNotEmpty(orders)) {
                response.getOrders().addAll(orders);
            }
            return response;
        }
        throw new ServiceException(HttpStatus.BAD_REQUEST.value(), "参数不合法");
    }

    private UserEntity getById(Long id) {
        return userRepository.selectById(id);
    }

    private List<OrderEntity> getByCondition(Long userId) {
        LambdaQueryWrapper<OrderEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderEntity::getUserId, userId);
        return orderRepository.selectList(queryWrapper);
    }
}
