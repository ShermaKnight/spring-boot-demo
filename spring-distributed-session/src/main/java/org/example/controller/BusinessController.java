package org.example.controller;

import org.example.model.CommonResult;
import org.example.service.BusinessService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/business")
public class BusinessController {

    @Resource
    private BusinessService businessService;

    @GetMapping("/")
    public CommonResult getInformation() {
        return new CommonResult(200, "success", businessService.getInformation());
    }

    @GetMapping("/permission")
    public CommonResult getPermission() {
        return new CommonResult(200, "success", businessService.getPermission());
    }
}
