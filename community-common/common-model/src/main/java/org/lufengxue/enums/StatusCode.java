package org.lufengxue.enums;

/**
 * 返回码
 */
public class StatusCode {
    /**
     * 成功
     */
    public static final String OK = "20000";
    /**
     * 失败
     */
    public static final String ERROR = "20001";
    /**
     * 用户名或密码错误
     */
    public static final String LOGINERROR = "20002";
    /**
     * 权限不足
     */
    public static final String ACCESSERROR = "20003";
    /**
     * 远程调用失败
     */
    public static final String REMOTEERROR = "20004";
    /**
     * 重复操作
     */
    public static final String REPERROR = "20005";
    /**
     * 没有对应的抢购数据
     */
    public static final String NOTFOUNDERROR = "20006";
}
