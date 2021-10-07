package org.example.repository;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.entities.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
@DS("users")
public interface UserRepository extends BaseMapper<UserEntity> {

}
