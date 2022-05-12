package org.lufengxue.pojo.elevator.elevatorPO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.util.Set;

/**
 * 作 者: 陆奉学
 * 工 程 名:  community-management
 * 包    名:  org.lufengxue.elevator.pojo.elevatorPO
 * 日    期:  2022-04-2022/4/16
 * 时    间:  22:05
 * 描    述:
 */

@Data
@ApiModel("大楼")
public class Building {

    @ApiModelProperty("大楼id")
    @Column(name = "id")
    private Integer id;

    @ApiModelProperty("楼层：")
    private Set<Floor> floors;


    @ApiModelProperty("大楼名")
    @Column(name = "building_name")
    private String buildingName;


    @ApiModelProperty("电梯")
    private Set<Elevator> elevators;

}
