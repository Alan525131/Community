package org.lufengxue.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.lufengxue.enums.ResponseEnum;
import org.lufengxue.exception.BaseException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;



@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description= "返回响应数据")
public class Result<T> implements Serializable {


    /**
     * 默认成功编码: 00000, 成功http状态: 200
     */
    protected static final String DEFAULT_SUCCEED_CODE = "00000";
    /**
     * 成功消息: SUCCEED,
     */
    protected static final String DEFAULT_SUCCEED_MSG = "SUCCEED";


    @ApiModelProperty(required = true, value = "返回状态码 DEFAULT_SUCCEED_CODE 为成功 ")
    protected  String code;

    @ApiModelProperty(required = true, value = "返回成功提示信息 DEFAULT_SUCCEED_MSG")
    protected String message;

    @ApiModelProperty(required = false, value = "返回逻辑数据")
    protected T data;



    /**
     *
     * @param <T>
     * @return 返回成功消息
     */
    public static <T> Result<T> success() {
        return new Result<T>(DEFAULT_SUCCEED_CODE,DEFAULT_SUCCEED_MSG,null);

    }

    public static <T>  Result<T> success( T data) {
        return new Result<T>(DEFAULT_SUCCEED_CODE,DEFAULT_SUCCEED_MSG,data);
    }


    /**
     *
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

    /**
     *  构造函数
     * @param code 状态码
     * @param message   错误消息
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
