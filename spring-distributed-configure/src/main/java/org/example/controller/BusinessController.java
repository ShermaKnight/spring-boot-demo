package org.example.controller;

import org.example.config.OssProperties;
import org.example.domain.dto.CommonResult;
import org.example.service.BusinessService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RefreshScope
@RestController
@RequestMapping("/business")
public class BusinessController {

    @Resource
    private BusinessService businessService;

    @GetMapping("/")
    public CommonResult getInformation(HttpServletRequest servletRequest) {
        return new CommonResult(200, "success", businessService.getInformation(servletRequest));
    }

    @Value("${notice.uuid}")
    private String uuid;

    @Value("${condition.instance}")
    private Boolean instance;

    @Resource
    private OssProperties ossProperties;

    @GetMapping("/oss")
    public CommonResult getOss() {
        HashMap<String, Object> response = new HashMap<>();
        response.put("uuid", uuid);
        response.put("oss", ossProperties);
        response.put("instance", instance);
        return new CommonResult(200, "success", response);
    }

    @Resource
    private ContextRefresher contextRefresher;

    @Resource
    private ConfigurableEnvironment configurableEnvironment;

    @GetMapping("/refresh")
    public CommonResult refreshOss() {
        HashMap<String, Object> dynamicCache = new HashMap<>();
        dynamicCache.put("condition.instance", false);
        MapPropertySource propertySource = new MapPropertySource("dynamic", dynamicCache);
        configurableEnvironment.getPropertySources().addFirst(propertySource);
        new Thread(() -> contextRefresher.refresh()).start();
        return new CommonResult(200, "success");
    }
}
