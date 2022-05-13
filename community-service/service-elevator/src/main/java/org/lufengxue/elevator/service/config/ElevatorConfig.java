package org.lufengxue.elevator.service.config;

import org.lufengxue.pojo.elevator.elevatorPO.Building;
import org.lufengxue.pojo.elevator.elevatorPO.Elevator;
import org.lufengxue.pojo.elevator.elevatorPO.Floor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * @author : Allen
 * @date : 2022/05/13 13:42
 * @desc : 电梯 实例配置类
 */
public class ElevatorConfig {


    public static Floor getTheBuildingObjectInstance(String buildingName){
        HashSet<Building> buildings = new HashSet<>();
        HashSet<Floor> floorsA = new HashSet<>();
        HashSet<Floor> floorsB = new HashSet<>();

        HashSet<Floor> elevatorsA = new HashSet<>();
        HashSet<Floor> elevatorsB = new HashSet<>();
        Building b1 = new Building(1,"A栋",1, floorsA,elevatorsA);
       Building b2 = new Building(2,"B栋",2, floorsB,elevatorsB);
        buildings.add(b2);
        buildings.add(b1);
       Floor f1 = new Floor(1,1,"上",1,1,3.0);
       Floor f2 = new Floor(2,2,"上",2,1,3.0);
       Floor f3= new Floor(3,3,"上",2,1,3.0);
       Floor f4 = new Floor(4,5,"上",2,1,3.0);
       Floor f5 = new Floor(5,6,"上",2,1,3.0);
       Floor f6= new Floor(6,7,"上",2,1,3.0);
       Floor f7 = new Floor(7,8,"上",2,1,3.0);
       Floor f8 = new Floor(8,9,"上",2,1,3.0);
       Floor f9 = new Floor(9,10,"上",2,1,3.0);
       Floor f10 = new Floor(10,11,"上",2,1,3.0);
       Floor f11 = new Floor(11,12,"上",2,1,3.0);
       Floor f12 = new Floor(12,13,"上",2,1,3.0);
       Floor f13 = new Floor(13,15,"上",2,1,3.0);
       Floor f14 = new Floor(14,16,"上",2,1,3.0);
       Floor f15 = new Floor(15,17,"上",2,1,3.0);
       Floor f16 = new Floor(16,19,"上",2,1,3.0);
       Floor f17 = new Floor(17,20,"上",3,1,3.0);

        floorsA.add(f1);
       return floor;
   }
//    public static  getFloorInstance
    public static void main(String[] args) {
        Building b1 = new Building(1,"A栋",1);
        Building b2 = new Building(2,"B栋",2);
        ArrayList<Building> buildings = new ArrayList<>();
        buildings.add(b2);
        buildings.add(b1);
        String src = "A栋";
//        for (Building building : buildings) {
//            if (building.getBuildingName().equals(src)) {
//
//                System.out.println(building.getBuildingName());
//            }
//        }
        if (buildings.contains(src)) {
        }



        Floor floor = new Floor();
        floor.getId();

    }
}
