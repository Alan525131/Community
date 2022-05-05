package lufengxue.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Primary
public class SwaggerResources implements SwaggerResourcesProvider {

    public static final String API_URI = "v3/api-docs";

    @Autowired
    private GatewayProperties gatewayProperties;

    @Autowired
    private DiscoveryClient discoveryClient;


    @Override
    public List<SwaggerResource> get() {
        // 在线服务实例
        List<String> services = discoveryClient.getServices();
        // 路由配置信息
        List<RouteDefinition> routes = gatewayProperties.getRoutes();
        // 文档资源
        List<SwaggerResource> resources = new ArrayList<>();
        routes.stream()
                .filter(routeDefinition -> services.contains(routeDefinition.getId()))
                .forEach(route -> {
                    route.getPredicates().stream()
                            .filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
                            .forEach(predicateDefinition -> {
                                String path = predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0");
                                // 截取 /xxx/** 中的 /xxx/ + v3/api-docs
                                path = path.substring(0, path.indexOf("/", 1) + 1).concat(API_URI);
                                resources.add(swaggerResource(route.getId(), path));
                            });
                });
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("3.0.3");
        return swaggerResource;
    }

}
