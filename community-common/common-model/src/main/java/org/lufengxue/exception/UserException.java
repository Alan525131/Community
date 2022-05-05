package org.lufengxue.exception;


import org.lufengxue.enums.ResponseEnum;

/**
 * 错误来源于用户:
 * 比如参数错误，
 * 用户安装版本过低，
 * 用户操作超时等问题；
 */
public class UserException extends BaseException {


    public UserException(ResponseEnum responseEnum) {
        super(responseEnum);
    }

    public UserException(ResponseEnum responseEnum, Throwable e) {
        super(responseEnum, e);
    }
}
