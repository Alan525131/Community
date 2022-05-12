package org.lufengxue.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.lufengxue.enums.ResponseEnum;
import org.lufengxue.enums.StatusCode;
import org.lufengxue.exception.BaseException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;


@Getter
@ApiModel(description = "返回响应数据")
public class Result<T> implements Serializable {



    @ApiModelProperty(required = true, value = "返回成功状态码 200为成功,其它为失败")
    protected String code;

    @ApiModelProperty(required = true, value = "返回成功提示信息 ")
    protected String message;

    @ApiModelProperty(required = false, value = "返回逻辑数据")
    protected T data;


    /**
     * @param <T>
     * @return 返回成功消息
     */
    public static <T> Result<T> success() {
        return new Result<T>( StatusCode.OK,"操作成功");

    }

    public static <T> Result<T> success(T data) {
        return new Result<T>(StatusCode.OK, "操作成功", data);
    }

//    public static <T> Result<T> success(ResponseEnum seEnum,T data) {
//        return new Result<T>(seEnum, "操作成功", data);
//    }


    /**
     * @param seEnum
     * @param <T>
     * @return 返回失败消息
     */
    public static <T> Result<T> fail(ResponseEnum seEnum) {
        setHttpStatus(seEnum.status);
        return new Result<T>(seEnum.getCode(), seEnum.getMessage());
    }

    public static <T> Result<T> fail(BaseException e) {
        setHttpStatus(e.getStatus());
        return new Result<T>(e.getCode(), e.getMsg());
    }

    public static <T> Result<T> fail(String msg) {
        setHttpStatus(500);
        return new Result<T>("500", msg);
    }

    /**
     * 构造函数
     *
     * @param code    状态码
     * @param message 错误消息
     */
    public Result(String code, String message,T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 构造函数
     *
     * @param code    状态码
     * @param message 错误消息
     */
    public Result(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 设置响应状态码
     */
    protected static void setHttpStatus(int status) {
        try {
            HttpServletResponse response = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getResponse();
            if (response != null) {
                response.setStatus(status);
            }
        } catch (IllegalStateException e) {
            return;
        }
    }
}
