package org.lufengxue.elevator.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lufengxue.contanents.CacheName;
import org.lufengxue.elevator.mapper.FloorMapper;
import org.lufengxue.elevator.service.FloorService;
import org.lufengxue.elevator.service.config.ElevatorConfig;
import org.lufengxue.pojo.elevator.elevatorDto.Floor;
import org.lufengxue.pojo.elevator.elevatorPO.Building;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author : Allen
 * @date : 2022/05/12 13:54
 * @desc :
 */
@Slf4j
@Service
public class FloorServiceImpl implements FloorService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ElevatorConfig elevatorConfig;

    /**
     * @param buildingName 大楼名字
     *                     根据大楼名字 查询所有本大楼的楼层号
     */
    @Override
//    @Cacheable(value = CacheName.BUILDING_FLOOR_NUMBER, key = "'building:' + #buildingName")
    public List<Floor> findFloor(String buildingName) {
        if (StringUtils.isEmpty(buildingName)) {
            throw new RuntimeException("大楼名称不能为空");
        }
        // 如果缓存当中没有当前大楼的 楼层数据就从缓存当中查找
        List<Floor> floors = getFindFloor(buildingName);
        floors.sort((f1, f2) -> f1.getId() - f2.getId());
        return floors;
    }

    /**
     * 获取大楼对应的所有楼层属性 存储到redis
     *
     * @param buildingName
     * @return
     */
    public List<Floor> getFindFloor(String buildingName) {
        // 创建楼层对象 //把po 转成do
        Floor floorRedis = new Floor();
        List<Floor> floorList = new ArrayList<>();

        // 第一次从数据库中查出来
        Building building = elevatorConfig.getBuildingInstance(buildingName);
        //获取到楼层对象集合
        Set<org.lufengxue.pojo.elevator.elevatorPO.Floor> floors = building.getFloors();
        for (org.lufengxue.pojo.elevator.elevatorPO.Floor floor : floors) {
                floorRedis.setId(floor.getId());
                floorRedis.setFloorNumber(floor.getFloorNumber());
                floorRedis.setFloorStatus(floor.getFloorStatus());
                floorList.add(floorRedis);
            }
        return floorList;
    }
}
