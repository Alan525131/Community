package lufengxue.gateway.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.lufengxue.enums.ResponseEnum;
import org.lufengxue.exception.BaseException;
import org.lufengxue.response.Result;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

public class ResponseUtils {

    private static final ObjectMapper JACKSON = new ObjectMapper();

    /**
     * 返回校验失败
     */
    @SneakyThrows
    public static Mono<Void> writeErrorInfo(ServerHttpResponse response, ResponseEnum responseEnum) {
        //noinspection deprecation
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatusCode(HttpStatus.valueOf(responseEnum.status));
        //noinspection BlockingMethodInNonBlockingContext
        String resultData = JACKSON.writeValueAsString(Result.fail(responseEnum));
        DataBuffer buffer = response.bufferFactory().wrap(resultData.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }


    /**
     * 返回校验失败
     */
    @SneakyThrows
    public static Mono<Void> writeErrorInfo(ServerHttpResponse response, BaseException baseException) {
        //noinspection deprecation
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatusCode(HttpStatus.valueOf(baseException.getStatus()));
        //noinspection BlockingMethodInNonBlockingContext
        String resultData = JACKSON.writeValueAsString(Result.fail(baseException));
        DataBuffer buffer = response.bufferFactory().wrap(resultData.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

}
