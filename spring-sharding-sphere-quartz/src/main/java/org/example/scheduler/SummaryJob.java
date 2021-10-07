package org.example.scheduler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.entities.UserEntity;
import org.example.repository.UserRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class SummaryJob implements Job {

    @Resource
    private UserRepository userRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("{} execute summary", format.format(new Date()));
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        Integer count = userRepository.selectCount(queryWrapper);
        log.info("summary: {}", count);
    }
}
