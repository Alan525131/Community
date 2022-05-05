package org.lufengxue.pojo.user.userBo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 作 者: 陆奉学
 * 工 程 名:  elevator
 * 包    名:  org.lufengxue.pojo.bo
 * 日    期:  2022-03-2022/3/29
 * 时    间:  20:25
 * 描    述: 用户令牌封装
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthToken implements Serializable {

    /**
     * 令牌信息
     */
    private   String accessToken;

    /**
     *  刷新token(refresh_token)
     */
    private  String refreshToken;

    /**
     *  jwt短令牌
     */
    private  String jti;
}
