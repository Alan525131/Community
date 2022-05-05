package logger.model;

import lombok.Data;

import java.util.Date;

/**
 * @author Alvin
 */
@Data
public class LogEntity {

    /**
     * 请求用户名
     */
    private String userName;

    /**
     * 请求来源IP
     */
    private String srcIp;

    /**
     * 请求来源地址
     */
    private String srcAddr;

    /**
     * 请求来源客户端
     */
    private String srcClient;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 请求地址
     */
    private String requestUrl;

    /**
     * 请求描述
     */
    private String describe;

    /**
     * 是否执行成功
     */
    private boolean isSucceed;

    /**
     * 请求时间
     */
    private Date beginTime;

    /**
     * 耗时
     */
    private long useTime;

}
