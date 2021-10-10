package org.example.controller;

import org.example.domain.dto.CommonResult;
import org.example.service.BusinessService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/business")
public class BusinessController {

    @Resource
    private BusinessService businessService;

    @GetMapping("/")
    public CommonResult getInformation(HttpServletRequest servletRequest) {
        return new CommonResult(200, "success", businessService.getInformation(servletRequest));
    }

}
