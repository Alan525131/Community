package org.lufengxue.enums;


import org.lufengxue.constant.HttpStatus;
import org.lufengxue.exception.BaseException;
import org.lufengxue.response.Result;

/**
 * 通用异常枚举
 * <br/>
 * 可用于可抛异常
 * {@link BaseException#BaseException(ResponseEnum)}
 * {@link BaseException#BaseException(ResponseEnum, Throwable)}
 * <br/>
 * 也可用于请求失败返回 {@link Result#fail(BaseException)}
 */
public enum ResponseEnum {

    /* 错误消息原因*/

    MISSION_OK      ("2000",HttpStatus.OK, "任务成功"),
    ACCESSERROR     ("20003", HttpStatus.UNAUTHORIZED,"权限不足"),
    REMOTEERROR     ("20004", HttpStatus.CLIENT_TIMEOUT, "远程调用失败"),
    NOTFOUNDERROR   ("20006", HttpStatus.BAD_REQUEST,  "没有对应的数据"),
    A_NOT_FOUND     ("404", HttpStatus.NOT_FOUND,"资源未找到"),
    A_ACCESS_DENIED ("403",HttpStatus.FORBIDDEN,"拒绝访问"),
    A_NAME_PWD_ERR  ("401",  HttpStatus.UNAUTHORIZED,"用户名或密码错误"),
    A_BAD_REQUEST   ("400",  HttpStatus.BAD_REQUEST,"错误的请求"),
    B_SYSTEM_ERR    ("500", HttpStatus.INTERNAL_ERROR,"系统内部错误"),
   BUTN_PARAMETE_ERROR   ("405", HttpStatus.BAD_REQUEST,"按钮参数错误"),
   PARAMETE_TEYP_ERROR   ("406", HttpStatus.BAD_REQUEST,"参数类型错误"),
    TARGET_FLOOR_IS_EMPTY ("407", HttpStatus.BAD_REQUEST,"筛选出来的目标楼层为空"),
    ;


    /**
     * 错误码,唯一标识
     */
    public final String code;

    /**
     * 错误消息,对错误的描述
     */
    public final String message;



    /**
     * 请求响应状态码
     * {@link HttpStatus}
     */
    public final int status;



    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    ResponseEnum(String code,int status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

}
