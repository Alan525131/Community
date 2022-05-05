package org.lufengxue.pojo.elevator.elevatorDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
@ApiModel(value = "floor",description = "楼层")
public class Floor {

    @ApiModelProperty("当前楼层号")
    private Integer floorNumber;

    @ApiModelProperty("楼层状态：1，最低楼，2中间楼，3最高楼")
    private Integer floorStatus;


}
