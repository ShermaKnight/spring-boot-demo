package org.example.controller;

import org.example.domain.dto.CommonResult;
import org.example.domain.vo.UserUpdateRequest;
import org.example.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/{id}")
    public CommonResult getById(@PathVariable Integer id) {
        return new CommonResult(200, "success", userService.getById(id));
    }

    @PostMapping("/{id}")
    public CommonResult updateById(@PathVariable Integer id, @RequestBody UserUpdateRequest updateRequest) {
        return new CommonResult(200, "success", userService.updateById(id, updateRequest));
    }

}
