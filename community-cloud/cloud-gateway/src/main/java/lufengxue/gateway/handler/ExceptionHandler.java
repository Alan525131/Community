package lufengxue.gateway.handler;

import lombok.extern.slf4j.Slf4j;
import lufengxue.enums.ResponseEnum;
import lufengxue.exception.BaseException;
import lufengxue.exception.SysException;
import lufengxue.exception.UserException;
import lufengxue.gateway.util.ResponseUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 第一个切面
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionHandler implements ErrorWebExceptionHandler {

    /**
     * 打印堆栈信息行数: -1 全打印, 0 不打印, 正整数 打印行数
     */
    private static final int LOG_STACK_ROWS = 15;

    private static final String DELIMIT = "{}";


    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
        ServerHttpRequest request = exchange.getRequest();
        String errorMsg = throwable.getMessage();
        String method = request.getMethodValue();
        String url = request.getURI().toString();
        Map<String, String> params = convertParamMap(request.getQueryParams());

        if (LOG_STACK_ROWS == 0) {
            log.error("\n全局异常: {}\n\turl: [{}] {}\n\tparam: {}\n", errorMsg, method, url, params);
        } else if (LOG_STACK_ROWS == -1) {
            log.error("\n全局异常: {}\n\turl: [{}] {}\n\tparam: {}\n", errorMsg, method, url, params, throwable);
        } else if (LOG_STACK_ROWS > 0) {
            String msg = format("\n全局异常: {}\n\turl: [{}] {}\n\tparam: {}\n", errorMsg, method, url, params);
            log.error(buildLog(throwable, msg));
        }

        BaseException baseException = null;
        if (throwable instanceof BaseException) {
            baseException = (BaseException) throwable;
        } else if (throwable instanceof ResponseStatusException) {
            ResponseStatusException rse = (ResponseStatusException) throwable;
            int status = rse.getStatus().value();
            switch (status) {
                case 401:
                    baseException = new UserException(ResponseEnum.A_NOT_FOUND, throwable);
                    break;
                case 403:
                    baseException = new UserException(ResponseEnum.A_ACCESS_DENIED, throwable);
                    break;
                case 404:
                    baseException = new UserException(ResponseEnum.A_NAME_PWD_ERR, throwable);
                    break;
                default:
                    baseException = new UserException(ResponseEnum.A_BAD_REQUEST, throwable);
            }
        } else {
            baseException = new SysException(ResponseEnum.B_SYSTEM_ERR, throwable);
        }

        if (exchange.getResponse().isCommitted()) {
            return Mono.error(baseException);
        }

        return ResponseUtils.writeErrorInfo(exchange.getResponse(), baseException);
    }

    private Map<String, String> convertParamMap(MultiValueMap<String, String> valueMap) {
        HashMap<String, String> map = new HashMap<>();
        valueMap.forEach((k, v) -> {
            String val = null;
            if (v != null) {
                val = String.join(",", v);
            }
            map.put(k, val);
        });
        return map;
    }

    private static String buildLog(Throwable e, String msg) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(msg)) {
            sb.append(msg).append("\n");
        }

        // cache 到的最外层错误本身
        int len = errorSelf(sb, e, LOG_STACK_ROWS);
        // 压制错误
        //len = errorSuppress(sb, e.getSuppressed(), len);
        // 错误的原因错误
        len = errorCause(sb, e.getCause(), len);


        int allLen = e.getStackTrace().length;
        // 压制长度
        Throwable[] suppress = e.getSuppressed();
        for (Throwable sup : suppress) {
            allLen += sup.getStackTrace().length;
        }
        // 原因长度
        Throwable temp = e;
        while ((temp = temp.getCause()) != null) {
            allLen += temp.getStackTrace().length;
        }
        if (allLen > LOG_STACK_ROWS) {
            sb.append("\t").append("... ").append(allLen - LOG_STACK_ROWS).append(" more");
        }
        return sb.toString();
    }

    private static String format(String msg, Object... params) {
        StringBuilder sb = new StringBuilder();
        int a = 0, b = 0;
        for (int n = 0; n < params.length && (b = msg.indexOf(DELIMIT, a)) > -1; n++, a = b + DELIMIT.length()) {
            sb.append(msg, a, b).append(params[n]);
        }
        if (a < msg.length()) {
            sb.append(msg, a, msg.length());
        }
        return sb.toString();
    }

    private static int errorSuppress(StringBuilder sb, Throwable[] suppress, int len) {
        for (int i = 0; i < suppress.length && len > 0; i++) {
            len = errorCause(sb, suppress[i], len);
        }
        return len;
    }


    private static int errorCause(StringBuilder sb, Throwable cause, int len) {
        while (cause != null && len > 0) {
            sb.append("Caused by: ").append(cause.getClass().getName()).append(": ").append(cause.getMessage()).append("\n");
            StackTraceElement[] causeTrace = cause.getStackTrace();
            for (int i = 0; i < causeTrace.length && len > 0; i++, len--) {
                sb.append("\t").append("at ").append(causeTrace[i]).append("\n");
            }
            cause = cause.getCause();
        }
        return len;
    }

    private static int errorSelf(StringBuilder sb, Throwable e, int len) {
        sb.append("Exception in thread \"").append(Thread.currentThread().getName()).append("\" ")
                .append(e.getClass().getName()).append(": ").append(e.getMessage()).append("\n");
        StackTraceElement[] trace = e.getStackTrace();
        for (int i = 0; i < trace.length && len > 0; i++, len--) {
            sb.append("\t").append("at ").append(trace[i]).append("\n");
        }

        Throwable[] suppressed = e.getSuppressed();
        for (int i = 0; i < suppressed.length && len > 0; i++) {
            Throwable err = suppressed[i];
            trace = err.getStackTrace();
            sb.append("\t").append("Suppressed: ").append(err.getClass().getName()).append("\n");
            sb.append(err.getMessage()).append("\n");
            for (int j = 0; j < trace.length && len > 0; j++, len--) {
                sb.append("\t").append("at ").append(trace[j]).append("\n");
            }
        }
        return len;
    }


}
