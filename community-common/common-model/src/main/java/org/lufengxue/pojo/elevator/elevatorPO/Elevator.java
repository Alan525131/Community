package org.lufengxue.pojo.elevator.elevatorPO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * 作 者: 陆奉学
 * 工 程 名:  community-management
 * 包    名:  org.lufengxue.elevator.pojo.elevatorPO
 * 日    期:  2022-04-2022/4/16
 * 时    间:  21:01
 * 描    述:
 */
@Data
@Table(name = "eleyator")
@ApiModel(value = "电梯")
public class Elevator implements Serializable {

    @ApiModelProperty("电梯id")
    @Column(name = "id")
    private Integer id;

    @ApiModelProperty("大楼id")
    @Column(name = "building_id")
    private Integer buildingId;

    @ApiModelProperty(value = "电梯使用状态: 1 可用，2不可用")
    @Column(name = "status")
    private Integer status;

    @ApiModelProperty(value = "电梯速度：秒/米")
    private Double speed;

    @ApiModelProperty(value = "电梯所在楼层")
    @Column(name = "inFloor")
    private Integer inFloor;

    @ApiModelProperty("电梯门开关 true 开，false 关， ")
    private Boolean isOpen;

    @ApiModelProperty("求救铃声")
    private Integer call;

    @ApiModelProperty("楼层按钮")
    private Set<Integer> floorButtons;

    @ApiModelProperty("电梯运行状态：1 往上，2往下，3静止")
    @Column(name = "sports")
    private Integer sports;
}
