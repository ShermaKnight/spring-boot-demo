package org.example.controller;

import org.example.domain.dto.CommonResult;
import org.example.domain.dto.RedisEvent;
import org.example.service.BusinessService;
import org.example.service.ConfigureService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/business")
public class BusinessController {

    @Resource
    private BusinessService businessService;

    @Resource
    private ConfigureService configureService;

    @Resource
    private ApplicationEventPublisher eventPublisher;

    @ResponseBody
    @GetMapping("/")
    public CommonResult getInformation(HttpServletRequest servletRequest) {
        return new CommonResult(200, "success", businessService.getInformation(servletRequest));
    }

    @ResponseBody
    @GetMapping("/event/{serviceName}")
    public CommonResult publishEvent(@PathVariable String serviceName) {
        RedisEvent redisEvent = new RedisEvent(this, serviceName);
        eventPublisher.publishEvent(redisEvent);
        return new CommonResult(200, "success");
    }

    @ResponseBody
    @GetMapping("/configure")
    public void getConfigure(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        configureService.getConfigure(servletRequest, servletResponse);
    }
}
