package org.lufengxue.pojo.elevator.elevatorPO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.SimpleTimeZone;

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
public class Floor implements Serializable {

    @ApiModelProperty("楼层id")
    @Column(name = "id")
    private Integer id;

    @ApiModelProperty("当前楼层号")
    @Column(name = "floor_number")
    private Integer floorNumber;

    @ApiModelProperty("电梯上下按钮： 上，下")
    @Column(name = "buttons")
    private String buttons;

    @ApiModelProperty("楼层状态：1，最低楼，2中间楼，3最高楼")
    @Column(name = "floor_status")
    private Integer floorStatus;

    @ApiModelProperty("楼层与大楼关联关系")
    @Column(name = "building_id")
    private Integer buildingId;

    @ApiModelProperty("楼层每层高度")
    private Double floorHeight;


    public Floor() {
    }

    public Floor(Integer id, Integer floorNumber, Integer floorStatus, Integer buildingId, Double floorHeight) {
        this.id = id;
        this.floorNumber = floorNumber;
        this.floorStatus = floorStatus;
        this.buildingId = buildingId;
        this.floorHeight = floorHeight;
    }
}
