package org.lufengxue.elevator.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lufengxue.contanents.CacheName;
import org.lufengxue.elevator.mapper.FloorMapper;
import org.lufengxue.elevator.service.FloorService;
import org.lufengxue.pojo.elevator.elevatorDto.Floor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private FloorMapper floorMapper;

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
        List<Floor> floors;
        // 如果缓存当中没有当前大楼的 楼层数据就从缓存当中查找
        if (!redisTemplate.hasKey(CacheName.BUILDING_FLOOR_NUMBER + buildingName)) {

            floors = getFindFloor(buildingName);

            //否则就根据 大楼名从redis 获取所有的对应楼层
        } else {
            floors = redisTemplate.boundHashOps(CacheName.BUILDING_FLOOR_NUMBER + buildingName).values();
            floors.sort((f1,f2)->f1.getId() - f2.getId());
        }
        return floors;
    }

    /**
     * 获取大楼对应的所有楼层属性 存储到redis
     *
     * @param buildingName
     * @return
     */
    private List<Floor> getFindFloor(String buildingName) {
        // 创建楼层对象
        Floor floorRedis = new Floor();
        // 第一次从数据库中查出来
        List<Floor> floors = floorMapper.findFloor(buildingName);
        for (Floor floor : floors) {
            floorRedis.setId(floor.getId());
            floorRedis.setFloorNumber(floor.getFloorNumber());
            floorRedis.setFloorStatus(floor.getFloorStatus());
            Integer id = floorRedis.getId();
            redisTemplate.boundHashOps(CacheName.BUILDING_FLOOR_NUMBER + buildingName).put(id, floorRedis);
        }
        return floors;
    }
}
