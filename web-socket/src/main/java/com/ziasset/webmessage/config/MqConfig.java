package com.ziasset.webmessage.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhouhahong
 */
@Configuration
@EnableRabbit
@Slf4j
@Data
public class MqConfig {

    public static final String TEST_QUEUE_NAME= "testQueueName";
    public  static final  String TEST_EXCHANGE_NAME="testExchangeName";

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {

//        connectionFactory.setPublisherConfirms(true);
//        connectionFactory.setPublisherReturns(true);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> log.info("消息发送成功:correlationData({}),ack({}),cause({})", correlationData, ack, cause));
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> log.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}", exchange, routingKey, replyCode, replyText, message));
        return rabbitTemplate;
    }

    @Bean("testQueue")
    public Queue testQueue() {
        return new Queue(TEST_QUEUE_NAME, true, false, true);
    }

    @Bean("testExchange")
    public FanoutExchange testExchange() {
        return new FanoutExchange(TEST_EXCHANGE_NAME, true, true);
    }

    @Bean
    public Binding testBinding() {
        return BindingBuilder.bind(testQueue()).to(testExchange());
    }



    public DirectExchange userDirectExchange(){
     DirectExchange directExchange=new DirectExchange("");
        BindingBuilder.bind(testQueue()).to(directExchange);
        return null;
    }





}

