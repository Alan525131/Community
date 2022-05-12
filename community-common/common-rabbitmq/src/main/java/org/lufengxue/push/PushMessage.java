package org.lufengxue.push;

import org.apache.commons.lang3.time.FastDateFormat;
import org.lufengxue.comstants.RabbitMQComstants;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;


@Component
@EnableScheduling
public class PushMessage {

    public static final FastDateFormat DATE_FORMAT_HH_MM_SS = FastDateFormat.getInstance("HH:mm:ss");

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 测试定时推送消息
     */
    @Scheduled(cron = "*/2 * * * * *")
    public void callback() {
        String time = DATE_FORMAT_HH_MM_SS.format(new Date());
        rabbitTemplate.convertAndSend(RabbitMQComstants.EXCHANGE_NAME, "chat*", "定时消息: " + time);
    }


}
