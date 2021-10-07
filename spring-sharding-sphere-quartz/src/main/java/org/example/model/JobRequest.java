package org.example.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@Data
public class JobRequest {

    @NotBlank(message = "任务名称不允许为空")
    private String jobName;

    @NotBlank(message = "任务分组不允许为空")
    private String jobGroup;

    @NotBlank(message = "执行类名不允许为空")
    private String jobClass;

    @NotBlank(message = "执行周期表达式不允许为空")
    private String cronExpression;

    private String jobDescription;

    private List<Map<String, Object>> jobData;
}
