package org.example.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.model.JobRequest;
import org.example.model.JobResponse;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JobServiceImpl implements JobService {

    @Resource
    private Scheduler scheduler;

    public static final String TRIGGER_IDENTITY_PREFIX = "trigger_";

    @Override
    @SneakyThrows
    public List<JobResponse> listAllJob() {
        List<JobResponse> jobResponses = new ArrayList<>();
        Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyJobGroup());
        if (CollectionUtils.isNotEmpty(jobKeys)) {
            jobKeys.stream().forEach(key -> {
                try {
                    List<? extends Trigger> triggers = scheduler.getTriggersOfJob(key);
                    if (CollectionUtils.isNotEmpty(triggers)) {
                        List<JobResponse> list = triggers.stream().map(trigger -> parse(key, trigger))
                                .filter(response -> Optional.ofNullable(response).isPresent())
                                .collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(list)) {
                            jobResponses.addAll(list);
                        }
                    }
                } catch (Exception e) {
                    // do nothing
                }
            });
        }
        return jobResponses;
    }

    @Override
    public List<JobResponse> listRunningJob() {
        try {
            List<JobResponse> jobResponses = new ArrayList<>();
            List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
            if (CollectionUtils.isNotEmpty(executingJobs)) {
                List<JobResponse> list = executingJobs.stream().map(context -> parse(context))
                        .filter(response -> Optional.ofNullable(response).isPresent())
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(list)) {
                    jobResponses.addAll(list);
                }
            }
            return jobResponses;
        } catch (SchedulerException e) {
            // do nothing
        }
        return Collections.EMPTY_LIST;
    }

    private JobResponse parse(JobKey key, Trigger trigger) {
        try {
            JobResponse response = new JobResponse();
            response.setJobName(key.getName());
            response.setJobGroup(key.getGroup());
            response.setJobDescription(trigger.getDescription());
            response.setTriggerStatus(scheduler.getTriggerState(trigger.getKey()).ordinal());
            response.setTriggerStatusName(scheduler.getTriggerState(trigger.getKey()).name());
            return response;
        } catch (SchedulerException e) {
            return null;
        }
    }

    private JobResponse parse(JobExecutionContext context) {
        return parse(context.getJobDetail().getKey(), context.getTrigger());
    }

    @Override
    public boolean addJob(JobRequest jobRequest) {
        try {
            Class<? extends Job> clazz = (Class<? extends Job>) Class.forName(jobRequest.getJobClass());
            clazz.newInstance();
            JobDetail job = JobBuilder.newJob(clazz)
                    .withIdentity(jobRequest.getJobName(), jobRequest.getJobGroup())
                    .withDescription(jobRequest.getJobDescription())
                    .build();

            JobDataMap jobDataMap = job.getJobDataMap();
            List<Map<String, Object>> data = jobRequest.getJobData();
            if (CollectionUtils.isNotEmpty(data)) {
                data.forEach(jobDataItem -> jobDataItem.keySet().forEach((key) -> {
                    jobDataMap.put(key, jobDataItem.get(key));
                }));
            }

            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(jobRequest.getCronExpression());
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(TRIGGER_IDENTITY_PREFIX + jobRequest.getJobName(), jobRequest.getJobGroup())
                    .startNow()
                    .withSchedule(cronScheduleBuilder)
                    .build();
            scheduler.scheduleJob(job, trigger);
            scheduler.start();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SchedulerException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean startJobs() {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean triggerJob(String jobName, String jobGroupName) {
        try {
            JobKey key = new JobKey(jobName, jobGroupName);
            scheduler.triggerJob(key);
        } catch (SchedulerException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteJob(String jobName, String jobGroupName) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));
        } catch (SchedulerException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean pauseJob(String jobName, String jobGroupName) {
        try {
            JobKey key = JobKey.jobKey(jobName, jobGroupName);
            scheduler.pauseJob(key);
        } catch (SchedulerException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean resumeJob(String jobName, String jobGroupName) {
        try {
            JobKey key = JobKey.jobKey(jobName, jobGroupName);
            scheduler.resumeJob(key);
        } catch (SchedulerException e) {
            return false;
        }
        return true;
    }
}
