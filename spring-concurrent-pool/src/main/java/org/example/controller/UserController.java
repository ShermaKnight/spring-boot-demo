package org.example.controller;

import org.example.domain.dto.CommonResult;
import org.example.service.BusinessService;
import org.example.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/{id}")
    public CommonResult getById(@PathVariable Long id) {
        return new CommonResult(200, "success", userService.getById(id));
    }

    @GetMapping("/")
    public CommonResult getByCondition() {
        return new CommonResult(200, "success", userService.getByCondition());
    }

}
