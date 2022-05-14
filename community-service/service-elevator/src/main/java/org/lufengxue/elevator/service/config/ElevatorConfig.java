package org.lufengxue.elevator.service.config;

import org.lufengxue.pojo.elevator.elevatorPO.Building;
import org.lufengxue.pojo.elevator.elevatorPO.Elevator;
import org.lufengxue.pojo.elevator.elevatorPO.Floor;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author : Allen
 * @date : 2022/05/13 13:42
 * @desc : 电梯 实例配置类
 */
@Configuration
public class ElevatorConfig {


    public static Building getBuildingInstance(String buildingName) {
        // 大楼 集合
        Set<Building> buildings = new HashSet<>();
        //  A栋 大楼对应的电梯属性
        HashSet<Elevator> elevatorsA = new HashSet<>();
        //  B栋 大楼对应的电梯属性
        HashSet<Elevator> elevatorsB = new HashSet<>();

        // A楼栋 对应的楼层属性
        HashSet<Floor> floorsA = new HashSet<>();
        // B楼栋 对应的楼层属性
        HashSet<Floor> floorsB = new HashSet<>();

        Building b1 = new Building(1, "A栋", 1, floorsA, elevatorsA);
        Building b2 = new Building(2, "B栋", 2, floorsB, elevatorsB);
        buildings.add(b1);
        buildings.add(b2);

        Floor f1 = new Floor(1, 1, 1, 1, 3.0);
        Floor f2 = new Floor(2, 2, 2, 1, 3.0);
        Floor f3 = new Floor(3, 3, 2, 1, 3.0);
        Floor f4 = new Floor(4, 5, 2, 1, 3.0);
        Floor f5 = new Floor(5, 6, 2, 1, 3.0);
        Floor f6 = new Floor(6, 7, 2, 1, 3.0);
        Floor f7 = new Floor(7, 8, 2, 1, 3.0);
        Floor f8 = new Floor(8, 9, 2, 1, 3.0);
        Floor f9 = new Floor(9, 10, 2, 1, 3.0);
        Floor f10 = new Floor(10, 11, 2, 1, 3.0);
        Floor f11 = new Floor(11, 12, 2, 1, 3.0);
        Floor f12 = new Floor(12, 13, 2, 1, 3.0);
        Floor f13 = new Floor(13, 15, 2, 1, 3.0);
        Floor f14 = new Floor(14, 16, 2, 1, 3.0);
        Floor f15 = new Floor(15, 17, 2, 1, 3.0);
        Floor f16 = new Floor(16, 19, 2, 1, 3.0);
        Floor f17 = new Floor(17, 20, 3, 1, 3.0);

        floorsA.add(f1);
        floorsA.add(f2);
        floorsA.add(f3);
        floorsA.add(f4);
        floorsA.add(f5);
        floorsA.add(f6);
        floorsA.add(f7);
        floorsA.add(f8);
        floorsA.add(f9);
        floorsA.add(f10);
        floorsA.add(f11);
        floorsA.add(f12);
        floorsA.add(f13);
        floorsA.add(f14);
        floorsA.add(f15);
        floorsA.add(f16);
        floorsA.add(f17);

        floorsB.add(f1);
        floorsB.add(f2);
        floorsB.add(f3);
        floorsB.add(f4);
        floorsB.add(f5);
        floorsB.add(f6);
        floorsB.add(f7);
        floorsB.add(f8);
        floorsB.add(f9);
        floorsB.add(f10);
        floorsB.add(f11);
        floorsB.add(f12);
        floorsB.add(f13);
        floorsB.add(f14);
        floorsB.add(f15);
        floorsB.add(f16);
        floorsB.add(f17);

        Elevator e1 = new Elevator(1, 3, 1, 1, 1, 0.5);
        Elevator e2 = new Elevator(2, 3, 1, 1, 1, 0.5);
        Elevator e3 = new Elevator(3, 3, 1, 1, 1, 0.5);
        Elevator e4 = new Elevator(4, 3, 1, 1, 1, 0.5);
        Elevator e5 = new Elevator(5, 3, 1, 2, 2, 0.5);
        Elevator e6 = new Elevator(6, 3, 1, 2, 2, 0.5);
        Elevator e7 = new Elevator(7, 3, 1, 2, 2, 0.5);
        Elevator e8 = new Elevator(8, 3, 1, 2, 2, 0.5);

        elevatorsA.add(e1);
        elevatorsA.add(e2);
        elevatorsA.add(e3);
        elevatorsA.add(e4);

        elevatorsB.add(e5);
        elevatorsB.add(e6);
        elevatorsB.add(e7);
        elevatorsB.add(e8);


        Building building = new Building();
        if (b1.getBuildingName().equals(buildingName)) {
            building = b1;
        }else {
            building = b2;
        }
        return building;
    }

    public static void main(String[] args) {

        org.lufengxue.pojo.elevator.elevatorDto.Floor floorRedis = new org.lufengxue.pojo.elevator.elevatorDto.Floor();
        List<org.lufengxue.pojo.elevator.elevatorDto.Floor> floorList = new ArrayList<>();
        // 第一次从数据库中查出来
        Building building = getBuildingInstance("A栋");
        //获取到楼层对象集合
        Set<org.lufengxue.pojo.elevator.elevatorPO.Floor> floors = building.getFloors();
        for (org.lufengxue.pojo.elevator.elevatorPO.Floor floor : floors) {
            floorRedis.setId(floor.getId());
            floorRedis.setFloorNumber(floor.getFloorNumber());
            floorRedis.setFloorStatus(floor.getFloorStatus());
            floorRedis.setBuildingId(floor.getBuildingId());
            floorList.add(floorRedis);
        }
        System.out.println(floorList);
//        queryElevatorDataById(3);
    }
//    public static Elevator queryElevatorDataById(Integer id){
//        Building building = new Building();
//        Elevator elevator1 = new Elevator();
//        Set<Elevator> elevators = building.getElevators();
//        for (Elevator elevator : elevators) {
//            if (elevator.getId().equals(id)) {
//                elevator1 = elevator;
//                System.out.println(elevator);
//            }
//        }
//       return elevator1;




}
