package org.lufengxue;

import lombok.extern.slf4j.Slf4j;
import org.lufengxue.constant.LogConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 作 者: 陆奉学
 * 工 程 名:  elevator
 * 包    名:  org.lufengxue
 * 日    期:  2022-03-2022/3/28
 * 时    间:  19:40
 * 描    述:
 */
@Slf4j
@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class,args);
        log.info(LogConstant.PUN_FINISH,"eureka");
    }
}
