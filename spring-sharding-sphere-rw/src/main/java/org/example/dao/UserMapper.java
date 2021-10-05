package org.example.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.entities.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<UserEntity> {


}
