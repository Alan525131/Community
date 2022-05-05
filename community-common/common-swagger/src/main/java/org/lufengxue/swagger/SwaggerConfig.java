package org.lufengxue.swagger;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.*;


@Slf4j
@Data
@Configuration
@EnableKnife4j
@ConfigurationProperties(prefix = "knife4j.setting")
public class SwaggerConfig {

    private String swaggerModelName;
    private String version;
    private String description;
    private boolean enable;

    /**
     * 创建RestApi 并包扫描controller
     */
    @Bean
    public Docket createRestApi() {
        log.info("[初始化] {} 初始化完成.", "Swagger.Docket");
        return new Docket(DocumentationType.OAS_30)
                .enable(enable)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("^(?!/error.*).*$"))
                .build()
                .globalRequestParameters(requestParameters())
                .protocols(setOf("https", "http"))
                ;
    }

    /**
     * 全局请求头
     */
    private List<RequestParameter> requestParameters() {
        List<RequestParameter> headerPars = new ArrayList<>();
        headerPars.add(new RequestParameterBuilder()
                .in(ParameterType.HEADER)
                .name("language")
                .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
                .description("语言: zh(中文), en(英语), vi(越南文), in(印度尼西亚语), th(泰语)")
                .build());
        headerPars.add(new RequestParameterBuilder()
                .in(ParameterType.HEADER)
                .name("client")
                .query(q -> q.model(m -> m.scalarModel(ScalarType.INTEGER)))
                .description("客户端类型: 0 PC, 1 H5, 2 Android, 3 IOS")
                .build());
        headerPars.add(new RequestParameterBuilder()
                .in(ParameterType.HEADER)
                .name("token")
                .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
                .description("令牌")
                .build());
        return headerPars;
    }

    /**
     * 创建 Swagger 主页信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerModelName)
                .description(description)
                .contact(new Contact("《JAVA 开发团队》", null, null))
                .version(version)
                .build();
    }


    private static<T> Set<T> setOf(T...ts) {
        return new HashSet<>(Arrays.asList(ts));
    }



}
