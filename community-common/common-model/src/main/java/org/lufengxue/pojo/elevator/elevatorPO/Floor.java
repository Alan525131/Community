package org.lufengxue.pojo.elevator.elevatorPO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 作 者: 陆奉学
 * 工 程 名:  community-management
 * 包    名:  org.lufengxue.elevator.pojo.elevatorPO
 * 日    期:  2022-04-2022/4/16
 * 时    间:  21:30
 * 描    述:
 */
@Data
@Table(name = "floor")
@ApiModel(value = "floor", description = "楼层")
public class Floor {

    @ApiModelProperty("当前楼层号")
    @Column(name = "floor_number")
    private Integer floorNumber;

    @ApiModelProperty("电梯上下按钮： 上，下")
    @Column(name = "buttons")
    private String buttons;

    @ApiModelProperty("楼层状态：1，最低楼，2中间楼，3最高楼")
    @Column(name = "floor_status")
    private Integer floorStatus;

    @ApiModelProperty("楼层每层高度")
    private Double floorHeight;

}
