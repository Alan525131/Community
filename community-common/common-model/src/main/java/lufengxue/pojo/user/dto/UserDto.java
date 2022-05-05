package lufengxue.pojo.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 作 者: 陆奉学
 * 工 程 名:  elevator
 * 包    名:  org.lufengxue.pojo.po
 * 日    期:  2022-03-2022/3/29
 * 时    间:  16:47
 * 描    述:
 */
@Data
public class UserDto implements Serializable {

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户密码")
    private String password;

    @ApiModelProperty("性别，1男，0女")
    private String sex;

    @ApiModelProperty("用户电话号码")
    private String phone;

    @ApiModelProperty("使用状态（1正常 0非正常）")
    private String status;

    @ApiModelProperty("创建时间")
    private Date createdDate;

    @ApiModelProperty("更新时间")
    private Date updatedDate;

    @ApiModelProperty("用户昵称名")
    private String nickName;

}
