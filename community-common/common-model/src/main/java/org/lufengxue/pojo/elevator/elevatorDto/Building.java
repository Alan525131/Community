package org.lufengxue.pojo.elevator.elevatorDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;


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


    @ApiModelProperty("大楼名")
    private String buildingName;

}
