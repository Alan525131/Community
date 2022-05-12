package org.lufengxue.listenten;


import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.lufengxue.comstants.RabbitMQComstants;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Slf4j
@Component
public class ListenMsg {


    /**
     * 确认消息时: true 批量确认,一次性拒绝所有小于 deliveryTag 的消息, false 单消息确认
     */
    private final boolean multiple = false;

    /**
     * 被拒绝是否重新入队列: true 将消息重返当前消息队列,还可以重新发送给消费者, false 将消息丢弃,
     */
    private final boolean requeue = true;


    /**
     * 1监听 A 队列, 一个队列被多个消费者监听, 默认消费者轮询消费这个队列的消息
     */
    @RabbitHandler
    @RabbitListener(queues = RabbitMQComstants.QUEUE_NAME_A)
    public void listenerA(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            log.info("1A消费: {}", msg);
            // 确认消息
            channel.basicAck(deliveryTag, multiple);
        } catch (Exception e) {
            // 拒收消息
            channel.basicNack(deliveryTag, multiple, requeue);
        }
    }

    /**
     * 2监听 A 队列, 一个队列被多个消费者监听, 默认消费者轮询消费这个队列的消息
     */
    @RabbitHandler
    @RabbitListener(queues = RabbitMQComstants.QUEUE_NAME_A)
    public void listenerA2(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            log.info("2A消费: {}", msg);
            // 确认消息
            channel.basicAck(deliveryTag, multiple);
        } catch (Exception e) {
            // 拒收消息
            channel.basicNack(deliveryTag, multiple, requeue);
        }
    }


    /**
     * 监听 B 队列
     */
    @RabbitHandler
    @RabbitListener(queues = RabbitMQComstants.QUEUE_NAME_B)
    public void listenerB(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            log.info("B消费: {}", message);
            // 确认消息
            channel.basicAck(deliveryTag, multiple);
        } catch (Exception e) {
            // 拒收消息
            channel.basicNack(deliveryTag, multiple, requeue);
        }
    }


}
