package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.vo.BusinessResponse;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@Service
public class BusinessServiceImpl implements BusinessService {

    private final static String topic = "business";

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public BusinessResponse getInformation(HttpServletRequest servletRequest) {
        kafkaTemplate.send(topic, String.valueOf(System.currentTimeMillis())).addCallback(result -> {
            log.info("send message success, message offset: {}", result.getRecordMetadata().offset());
        }, ex -> {
            log.error("send message failed, {}", ex.getMessage());
        });
        return build(servletRequest);
    }

    private BusinessResponse build(HttpServletRequest servletRequest) {
        BusinessResponse response = new BusinessResponse(new Date());
        response.setPath(servletRequest.getServletPath());
        return response;
    }
}
