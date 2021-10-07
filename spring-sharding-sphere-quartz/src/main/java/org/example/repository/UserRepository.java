package org.example.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.entities.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseMapper<UserEntity> {

}
