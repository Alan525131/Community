package org.lufengxue.exception.global;

import org.lufengxue.exception.proper.ExceptionPrintProperties;
import org.lufengxue.exception.util.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.lufengxue.exception.BaseException;
import org.lufengxue.exception.SysException;
import org.lufengxue.exception.UserException;
import org.lufengxue.response.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

import static org.lufengxue.enums.ResponseEnum.B_SYSTEM_ERR;


/**
 * 目前所有异常的处理方式都一样, 为了以后的扩展性, 这里还是分开处理.
 */
@Slf4j
@RestControllerAdvice
public class GlobalException {


    public GlobalException() {
        log.info("[初始化] {} 初始化完成.", "GlobalException");
    }


    @Autowired
    private ExceptionPrintProperties properties;


    /**
     * 来源于用户的异常
     */
    @ExceptionHandler({UserException.class})
    public Result<String> userException(BaseException e) {
        printError(e);
        return Result.fail(e);
    }


    /**
     * 来源于系统的异常
     */
    @ExceptionHandler({SysException.class})
    public Result<String> sysException(BaseException e) {
        printError(e);
        return Result.fail(e);
    }

    /**
     * 其它异常
     */
    @ExceptionHandler({Throwable.class})
    public Result<String> runtimeException(Throwable e) {
        printError(e);
        return Result.fail(e.getMessage());
    }


    /**
     * 打印请求属性
     */
    public void printError(Throwable ex) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        StringBuilder sb = new StringBuilder();
        sb.append("\n全局异常: ");
        if (ex instanceof BaseException) {
            sb.append("code: ").append(((BaseException) ex).getCode()).append(" ");
        }
        if (ex != null && StringUtils.isNotBlank(ex.getMessage())) {
            sb.append(ex.getMessage());
        }
        // 拼接请求IP
        sb.append("\n\t请求 IP: ").append(IPUtil.getIpAddr(request));
        // 拼接请求路径
        sb.append("\n\t请求路径: ").append(request.getRequestURL().toString());
        // 拼接请求头
        sb.append("\n\t请求头: ");
        String[] headers = properties.getHeaders();
        for (String headerName : headers) {
            String header = request.getHeader(headerName);
            if (StringUtils.isNotEmpty(header)) {
                sb.append("\n\t\t").append(headerName).append(": ").append(header);
            }
        }

        // 拼接参数
        Map<String, String[]> params = request.getParameterMap();
        if (!params.isEmpty()) {
            sb.append("\n\t请求参数: ");
            for (String key : params.keySet()) {
                sb.append("\n\t\t").append(key).append(" : ");
                String[] val = params.get(key);
                if (val == null) {
                    sb.append("null");
                } else {
                    sb.append(String.join(",", val));
                }
            }
        }
        // 打印错误级别日志
        log.error(sb.toString(), ex);
    }


}
