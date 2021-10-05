package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.model.CommonResult;
import org.example.model.UserCreateRequest;
import org.example.service.ShardingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/sharding")
public class ShardingController {

    @Autowired
    private ShardingService shardingService;

    @PostMapping("/user")
    public CommonResult create(@Validated @RequestBody UserCreateRequest createRequest) {
        shardingService.create(createRequest);
        return new CommonResult(200, "success");
    }

    @GetMapping("/user/{id}")
    public CommonResult getUser(@PathVariable("id") Long id) {
        return new CommonResult(200, "success", shardingService.getUser(id));
    }

    @GetMapping("/address/{id}")
    public CommonResult getAddress(@PathVariable("id") Long id) {
        return new CommonResult(200, "success", shardingService.getAddress(id));
    }
}
