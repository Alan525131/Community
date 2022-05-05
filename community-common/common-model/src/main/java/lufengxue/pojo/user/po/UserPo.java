package lufengxue.pojo.user.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Table;
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
@Table(name="ele_user")
public class UserPo {

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户名")
    @Column(name = "username")
    private String username;

    @ApiModelProperty("用户密码")
    @Column(name = "password")
    private String password;

    @ApiModelProperty("用户昵称名")
    @Column(name = "nick_name")
    private String nickName;

    @ApiModelProperty("性别，1男，0女")
    @Column(name = "sex")
    private String sex;

    @ApiModelProperty("用户电话号码")
    @Column(name = "phone")
    private String phone;

    @ApiModelProperty("使用状态（1正常 0非正常）")
    @Column(name = "status")
    private String status;

    @ApiModelProperty("创建时间")
    @Column(name = "created_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createdDate;

    @ApiModelProperty("更新时间")
    @Column(name = "updated_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updatedDate;
}
