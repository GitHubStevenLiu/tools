package com.ziasset.webmessage.config;

import com.ziasset.webmessage.config.MqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * @author zhouhahong
 */
@Component
@Slf4j
public class CommonMqListener {

    /**
     * 监听消费用户日志
     *
     * @param message
     */
    @RabbitListener(queues = MqConfig.TEST_QUEUE_NAME)
    public void consumeUserLogQueue(@Payload byte[] message) throws UnsupportedEncodingException {
        log.info("mesasge,{}", (new String(message, "UTf-8")));
    }

}
