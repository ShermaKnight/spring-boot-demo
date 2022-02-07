package org.example.service;

import org.example.domain.entity.User;
import org.example.domain.vo.BusinessResponse;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    User getById(Integer id);

}
