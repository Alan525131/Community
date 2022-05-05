package org.lufengxue.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.lufengxue.enums.ResponseEnum;


/**
 * @author : Allen
 * @date : 2022/04/27 22:56
 * @desc : 自定义异常类
 */
@Slf4j
@Getter
public abstract class BaseException extends RuntimeException{
    /**
     *  错误编码
     */
    protected String code;

    /**
     *  错误消息
     */
    protected String msg;

    /**
     * http 响应状态
     */
    protected int status;


    public  BaseException(ResponseEnum responseEnum) {
        super(responseEnum.message);
        this.code = responseEnum.code;
        this.msg = responseEnum.message;
        this.status = responseEnum.status;
    }

    public BaseException(ResponseEnum responseEnum, Throwable e) {
        super(String.format("[%s]%s: %s", e.getClass().getSimpleName(), e.getMessage(), responseEnum.message),e);
        this.code = responseEnum.code;
        this.msg = responseEnum.message;
        this.status = responseEnum.status;
    }


}
