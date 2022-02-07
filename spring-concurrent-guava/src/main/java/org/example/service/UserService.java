package org.example.service;

import org.example.domain.entity.User;
import org.example.domain.vo.BusinessResponse;
import org.example.domain.vo.UserUpdateRequest;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    User getById(Integer id);

    boolean updateById(Integer id, UserUpdateRequest updateRequest);
}
