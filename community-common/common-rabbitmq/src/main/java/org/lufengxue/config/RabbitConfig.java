package org.lufengxue.config;

import lombok.extern.slf4j.Slf4j;
import org.lufengxue.comstants.RabbitMQComstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/*
配置大概如下
spring:
  rabbitmq:
    port: 5672
    host: 127.0.0.1
    username: alvin
    password: 123456
    virtual-host: /dev
    # 开启发送确认
    publisherConfirmType: SIMPLE
    # 开启发送失败回退
    publisherReturns: true
    listener:
      direct:
        # 开启手动Ack
        acknowledge-mode: manual
      simple:
        # 开启手动Ack
        acknowledge-mode: manual
        retry:
          enabled: true
 */

/**
 * RabbitMQ 生产端确认, 消费端确认
 */
@Slf4j
@Configuration
public class RabbitConfig implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitConfig() {
        // 设置这两个, 开启发送确认
        rabbitTemplate.setReturnCallback(this);
        rabbitTemplate.setConfirmCallback(this);
        log.info("[初始化] {} 初始化完成.", "RabbitMQ 确认设置");
    }


    /**
     * 对列
     */
    @Bean
    public Queue queueA() {
        return new Queue(RabbitMQComstants.QUEUE_NAME_A, true);
    }

    /**
     * 对列
     */
    @Bean
    public Queue queueB() {
        return new Queue(RabbitMQComstants.QUEUE_NAME_B, true);
    }

    /**
     * 主题交换机
     */
    @Bean
    TopicExchange exchange() {
        return ExchangeBuilder.topicExchange(RabbitMQComstants.EXCHANGE_NAME).durable(true).build();
    }

    /**
     * 绑定 A 队列
     */
    @Bean
    Binding bindingExchangeMessageA() {
        return BindingBuilder.bind(queueA()).to(exchange()).with("chata");
    }

    /**
     * 1绑定 B 队列, 一个队列两次绑定, 同时被命中, 本队列只传一次消息
     */
    @Bean
    Binding bindingExchangeMessageB1() {
        return BindingBuilder.bind(queueA()).to(exchange()).with("chatb");
    }

    /**
     * 2绑定 B 队列, 一个队列两次绑定, 同时被命中, 本队列只传一次消息
     */
    @Bean
    Binding bindingExchangeMessageB2() {
        return BindingBuilder.bind(queueA()).to(exchange()).with("chatc");
    }


    /**
     * 确认消息是否到达 Exchange ack为 false 则发送失败
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (!ack) {
            log.error("消息未到达Exchange, 原因: {}", cause);
        }
    }

    /**
     * 确认消息是否到达 Queue，消息发送失败触发(比如 routing_key 匹配不到 queue)
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.error("消息未到达Queue, 消息 {}, 交换机 {}, 路由key {}, 应答码 {}, 原因 {}", msg, exchange, routingKey, replyCode, replyText);
    }

}
