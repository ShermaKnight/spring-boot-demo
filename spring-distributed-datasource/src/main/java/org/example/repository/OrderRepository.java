package org.example.repository;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.entities.OrderEntity;
import org.example.entities.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
@DS("orders")
public interface OrderRepository extends BaseMapper<OrderEntity> {

}
