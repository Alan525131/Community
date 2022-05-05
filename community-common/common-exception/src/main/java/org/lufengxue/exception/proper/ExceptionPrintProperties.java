package org.lufengxue.exception.proper;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static org.lufengxue.constant.HeaderNameConstant.*;


/**
 * 配置异常时需要打印的请求头
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "global.exception.print")
public class ExceptionPrintProperties {

    private String[] headers = {LANGUAGE, AGENT, ORIGIN, CONTENT_TYPE, USERNAME, TOKEN, CLIENT};

}
