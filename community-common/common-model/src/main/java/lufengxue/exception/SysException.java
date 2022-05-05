package lufengxue.exception;

import lufengxue.enums.ResponseEnum;

/**
 * 错误来源于当前系统:
 * 往往是业务逻辑出错
 * 或程序健壮性差等问题
 */
public class SysException extends BaseException {

    public SysException(ResponseEnum responseEnum) {
        super(responseEnum);
    }

    public SysException(ResponseEnum responseEnum, Throwable e) {
        super(responseEnum, e);
    }

}
