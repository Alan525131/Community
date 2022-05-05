package org.lufengxue.pojo.user.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 作 者: 陆奉学
 * 工 程 名:  elevator
 * 包    名:  org.lufengxue
 * 描述    ： Role构建
 */
@ApiModel
@Table(name = "ele_role")
public class Role implements Serializable {

    @Id
    @ApiModelProperty("角色id")
    @Column(name = "role_id")
    private String roleId;

    @ApiModelProperty("角色姓名")
    @Column(name = "role_name")
    private String roleName;

    @ApiModelProperty("角色编码")
    @Column(name = "role_code")
    private String roleCode;

}
