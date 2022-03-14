package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.vo.BusinessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@Service
public class BusinessServiceImpl implements BusinessService {

    @Resource
    private RedisLock redisLock;

    private Integer count = 98;

    @Override
    public BusinessResponse getInformation(HttpServletRequest servletRequest) {
        return build(servletRequest);
    }

    private BusinessResponse build(HttpServletRequest servletRequest) {
        BusinessResponse response = new BusinessResponse(new Date());
        response.setPath(servletRequest.getServletPath());
        return response;
    }

    @Override
    public void decrease() {
        try {
            redisLock.lock();
            if (count < 1) {
                log.info("库存不足");
            } else {
                count--;
                log.info("当前线程: {}, 减库存成功, 剩余库存: {}", Thread.currentThread().getId(), count);
            }
        } finally {
            redisLock.unlock();
        }
    }
}
