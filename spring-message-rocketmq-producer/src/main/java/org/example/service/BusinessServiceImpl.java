package org.example.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.example.domain.vo.BusinessResponse;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@Service
public class BusinessServiceImpl implements BusinessService {

    private final static String topic = "business";

    @Resource
    private RocketMQTemplate rocketTemplate;

    @Override
    public BusinessResponse getInformation(HttpServletRequest servletRequest) {
        syncSend();
        return build(servletRequest);
    }

    // 发送同步消息
    @SneakyThrows
    private void syncSend() {
        Message<String> message = MessageBuilder.withPayload(String.valueOf(System.currentTimeMillis())).build();

        // 同步消息
        // SendResult sendResult = rocketTemplate.syncSend(topic, message);

        // 延时消息 1到18分别对应1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
        SendResult sendResult = rocketTemplate.syncSend(topic, message, 10 * 1000, 4);
        log.info("{}", sendResult);
    }

    // 发送异步消息
    @SneakyThrows
    private void asyncSend() {
        Message<String> message = MessageBuilder.withPayload(String.valueOf(System.currentTimeMillis())).build();

        // 异步消息
        // rocketTemplate.asyncSend(topic, message, new CommonSendCallback());

        // 延迟消息
        rocketTemplate.asyncSend(topic, message, new CommonSendCallback(), 10 * 1000, 4);
        log.info("other business.");
    }

    private class CommonSendCallback implements SendCallback {
        @Override
        public void onSuccess(SendResult sendResult) {
            log.info("send success. {}", sendResult);
        }

        @Override
        public void onException(Throwable throwable) {
            log.error("send failed.", throwable);
        }
    }

    private BusinessResponse build(HttpServletRequest servletRequest) {
        BusinessResponse response = new BusinessResponse(new Date());
        response.setPath(servletRequest.getServletPath());
        return response;
    }
}
