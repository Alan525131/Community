package lufengxue.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Swagger 路径加谓词前缀
 */
@Slf4j
@Component
public class SwaggerGlobalFilter implements GlobalFilter, Ordered {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String END_URI = "/v3/api-docs";
    private static final String PATHS = "paths";

    @SuppressWarnings(value = "unchecked")
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        if (!path.endsWith(END_URI)) {
            return chain.filter(exchange);
        }
        String basePath = path.substring(0, path.lastIndexOf(END_URI));
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                        DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                        DataBuffer dataBf = dataBufferFactory.join(dataBuffers);
                        byte[] content = new byte[dataBf.readableByteCount()];
                        dataBf.read(content);
                        //释放掉内存
                        DataBufferUtils.release(dataBf);
                        //原始响应
                        String originalbody = new String(content, StandardCharsets.UTF_8);
                        try {
                            ObjectNode objectNode = (ObjectNode) OBJECT_MAPPER.readTree(originalbody);
                            assert objectNode != null;
                            ObjectNode paths = (ObjectNode) objectNode.get(PATHS);
                            if (paths != null) {
                                Iterator<String> fieldNames = paths.fieldNames();
                                HashSet<String> sets = new HashSet<>();
                                while (fieldNames.hasNext()) {
                                    sets.add(fieldNames.next());
                                }
                                for (String oldKey : sets) {
                                    String newKey = basePath.concat(oldKey);
                                    JsonNode jsonNode = paths.remove(oldKey);
                                    paths.set(newKey, jsonNode);
                                }
                            }
                            //修改后的响应体
                            originalbody = OBJECT_MAPPER.writeValueAsString(objectNode);
                            getHeaders().setContentLength(originalbody.getBytes(StandardCharsets.UTF_8).length);
                        } catch (JsonProcessingException e) {
                            log.error(e.getMessage(), e);
                        }
                        return bufferFactory.wrap(originalbody.getBytes());
                    }));
                }
                return super.writeWith(body);
            }
        };
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    @Override
    public int getOrder() {
        return -1;
    }


}
