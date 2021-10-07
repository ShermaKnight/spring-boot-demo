package org.example.controller;

import org.example.model.CommonResult;
import org.example.model.JobRequest;
import org.example.service.JobService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/job")
public class JobController {

    @Resource
    private JobService jobService;

    @GetMapping("/all")
    public CommonResult listAllJob() {
        return new CommonResult(200, "success", jobService.listAllJob());
    }

    @GetMapping("/running")
    public CommonResult listRunningJob() {
        return new CommonResult(200, "success", jobService.listRunningJob());
    }

    @PostMapping("")
    public CommonResult addJob(@Validated @RequestBody JobRequest jobRequest) {
        return new CommonResult(200, "success", jobService.addJob(jobRequest));
    }

    @PutMapping("/pause")
    public CommonResult pauseJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroup") String jobGroup) {
        return new CommonResult(200, "success", jobService.pauseJob(jobName, jobGroup));
    }

    @PutMapping("/resume")
    public CommonResult resumeJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroup") String jobGroup) {
        return new CommonResult(200, "success", jobService.resumeJob(jobName, jobGroup));
    }

    @DeleteMapping("")
    public CommonResult deleteJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroup") String jobGroup) {
        return new CommonResult(200, "success", jobService.deleteJob(jobName, jobGroup));
    }
}
