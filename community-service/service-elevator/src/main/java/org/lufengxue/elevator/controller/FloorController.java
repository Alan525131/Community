package org.lufengxue.elevator.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.lufengxue.elevator.service.FloorService;
import org.lufengxue.enums.StatusCode;
import org.lufengxue.pojo.elevator.elevatorDto.Floor;
import org.lufengxue.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * @author : Allen
 * @date : 2022/05/12 13:47
 * @desc : 获取所有大楼楼层
 */
@Slf4j
@Api(tags = "电梯管理楼层类")
@RestController("/floor")
public class FloorController {

    @Autowired
    private FloorService floorService;

    @GetMapping("/findFloor")
    @ApiOperation("根据大楼名字查询所有楼层")
    @ApiImplicitParam(name = "buildingName", value = "大楼名称", dataType = "String", paramType = "query", required = true)
    public Result<List<Floor>> findFloor(String buildingName) {
        List<Floor> floorList = floorService.findFloor(buildingName);
        return new Result(StatusCode.OK, "查询大楼楼层成功", floorList);
    }
}
