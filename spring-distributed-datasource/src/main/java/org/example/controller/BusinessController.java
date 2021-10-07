package org.example.controller;

import org.example.model.CommonResult;
import org.example.service.BusinessService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/business")
public class BusinessController {

    @Resource
    private BusinessService businessService;

    @GetMapping("/{id}")
    public CommonResult getById(@PathVariable("id") Long id) {
        return new CommonResult(200, "success", businessService.getBusinessById(id));
    }

}
