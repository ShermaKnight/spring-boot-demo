package org.example.service;

import org.example.model.JobRequest;
import org.example.model.JobResponse;

import java.util.List;

public interface JobService {

    List<JobResponse> listAllJob();
    List<JobResponse> listRunningJob();

    boolean addJob(JobRequest jobRequest);
    boolean startJobs();
    boolean triggerJob(String jobName, String jobGroupName);

    boolean deleteJob(String jobName, String jobGroupName);
    boolean pauseJob(String jobName, String jobGroupName);
    boolean resumeJob(String jobName, String jobGroupName);
}
