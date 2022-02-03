package org.example.service;

import cn.hutool.core.bean.BeanUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.example.domain.dto.UserEntity;
import org.example.domain.vo.UserResponse;
import org.example.handler.ServiceException;
import org.example.repository.RepositoryBeanRegister;
import org.example.repository.UserRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, ApplicationContextAware {

//    @Resource
//    private UserRepository userRepository;

    private UserRepository userRepository;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void initialize() {
        if (!Optional.ofNullable(userRepository).isPresent()) {
            userRepository = applicationContext.getBean("customUserRepository", UserRepository.class);
        }
    }

    @Override
    public UserResponse getById(Long id) {
        UserEntity userEntity = userRepository.getById(id);
        if (Optional.ofNullable(userEntity).isPresent()) {
            UserResponse userResponse = new UserResponse();
            BeanUtil.copyProperties(userEntity, userResponse);
            return userResponse;
        }
        throw new ServiceException(0, "用户不存在");
    }

    @Override
    public List<UserResponse> getByCondition() {
        List<UserEntity> userEntities = userRepository.getByCondition(Boolean.TRUE);
        if (CollectionUtils.isNotEmpty(userEntities)) {
            return userEntities.stream().map(userEntity -> {
                UserResponse userResponse = new UserResponse();
                BeanUtil.copyProperties(userEntity, userResponse);
                return userResponse;
            }).collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }
}
