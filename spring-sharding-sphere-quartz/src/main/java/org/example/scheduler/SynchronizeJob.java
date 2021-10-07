package org.example.scheduler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import java.util.List;

@Slf4j
@Component
public class SynchronizeJob implements Job {

    @Resource
    private UserRepository userRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("{} execute synchronize", format.format(new Date()));
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        List<UserEntity> entities = userRepository.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(entities)) {
            entities.stream().forEach(entity -> {
                log.info("synchronize: {}", entity);
            });
        }
    }
}
