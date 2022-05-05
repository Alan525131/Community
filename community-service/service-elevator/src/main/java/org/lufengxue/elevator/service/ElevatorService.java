package org.lufengxue.elevator.service;

import org.lufengxue.pojo.elevator.elevatorDto.Elevator;
import org.lufengxue.pojo.elevator.elevatorDto.Floor;

import java.util.List;
import java.util.Set;

/**
 * 作 者: 陆奉学
 * 工 程 名:  elevator
 * 包    名:  org.lufengxue
 * 描述    ： Elevator业务接口
 */
public interface ElevatorService {

    List<Floor> findFloor(String buildingName);

    Elevator callElevator(String buildingName, String buttons, Integer floorNumber);

    List<Elevator> runElevator(Set<Integer> floorButtons,Integer id);

}
