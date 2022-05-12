package org.lufengxue;


import lombok.extern.slf4j.Slf4j;
import org.lufengxue.constant.LogConstant;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 作 者: 陆奉学
 * 工 程 名:  elevator
 * 包    名:  org.lufengxue
 * 日    期:  2022-03-2022/3/30
 * 时    间:  0:35
 * 描    述:
 */
@Slf4j
@EnableEurekaClient
@SpringBootApplication
@MapperScan(basePackages = "org.lufengxue.user.mapper")
//@EnableFeignClients(basePackages = "org.lufengxue.pojo.user.userFeign.UserFeign")
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class,args);
        log.info(LogConstant.PUN_FINISH,"用户模块");
    }
        @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
