package logger.aop;

import logger.annotations.SaveLog;
import logger.model.LogEntity;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author Alvin
 */
@Slf4j
@Aspect
@Order(-1)
@Component
public class LogAnnotationAop {

    @Around("@annotation(saveLog)")
    public Object saveLog(ProceedingJoinPoint point, SaveLog saveLog) throws Throwable {
        LogEntity logEntity = new LogEntity();
        logEntity.setBeginTime(new Date());
        logEntity.setDescribe(saveLog.logInfo());

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null){
            HttpServletRequest request = servletRequestAttributes.getRequest();
            logEntity.setRequestMethod(request.getMethod());
            logEntity.setRequestUrl(request.getRequestURL().toString());
            logEntity.setSrcClient(request.getHeader("User-Agent"));

            // TODO
            logEntity.setSrcIp(request.getHeader("Ip"));
            logEntity.setSrcAddr(request.getHeader("addr"));
            logEntity.setUserName(request.getHeader("token"));
        }
        try {
            Object proceed = point.proceed();
            logEntity.setSucceed(true);
            return proceed;
        } catch (Exception e) {
            logEntity.setSucceed(false);
            throw e;
        } finally {
            logEntity.setUseTime(System.currentTimeMillis() - logEntity.getBeginTime().getTime());
            // TODO
            Runnable runnable = () -> {
                System.out.println("保存日志: " + logEntity.toString());
            };
            // FIXME
            new Thread(runnable).start();
        }

    }

}
