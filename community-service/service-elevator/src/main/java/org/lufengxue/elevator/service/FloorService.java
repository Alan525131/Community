package org.lufengxue.elevator.service;

import org.lufengxue.pojo.elevator.elevatorDto.Floor;

import java.util.List;
import java.util.Set;

/**
 * @author : Allen
 * @date : 2022/05/12 13:53
 * @desc :
 */
public interface FloorService {

    List<Floor> findFloor(String buildingName);
}
