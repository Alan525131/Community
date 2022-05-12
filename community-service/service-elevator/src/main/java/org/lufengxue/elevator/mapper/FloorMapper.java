package org.lufengxue.elevator.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.lufengxue.pojo.elevator.elevatorDto.Floor;

import java.util.List;

/**
 * @author : Allen
 * @date : 2022/05/12 14:04
 * @desc :
 */
@Mapper
public interface FloorMapper {

    /**
     * 根据大楼名查询出对应的所有楼层
     *
     * @param buildingName
     * @return
     */
    List<Floor> findFloor(String buildingName);
}
