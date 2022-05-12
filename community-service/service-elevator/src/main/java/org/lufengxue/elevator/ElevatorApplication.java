package org.lufengxue.elevator;

import lombok.extern.slf4j.Slf4j;
import org.lufengxue.constant.LogConstant;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 作 者: 陆奉学
 * 工 程 名:  community
 * 包    名:  org.lufengxue.elevator
 * 日    期:  2022-04-2022/4/11
 * 时    间:  23:12
 * 描    述:
 */
@Slf4j
@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = "org.lufengxue.elevator.mapper")
public class ElevatorApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElevatorApplication.class,args);
        log.info(LogConstant.PUN_FINISH,"电梯模块");
    }
}
