package org.example.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class JobResponse {

    private String jobName;
    private String jobGroup;
    private String jobDescription;
    private Integer triggerStatus;
    private String triggerStatusName;
    private List<Map<String, Object>> jobData;
}
