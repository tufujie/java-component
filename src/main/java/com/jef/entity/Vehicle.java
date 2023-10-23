package com.jef.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动车
 * 地铁原理类似
 * @author Jef
 * @date 2020/6/20
 */
public class Vehicle {
    /**
     * 动车ID
     */
    private String velicleID;
    /**
     * 动车集合
     */
    private List<Vehicle> vehicleList;
    /**
     * 地址集合
     */
    private Map<String, Point> locationMap = new HashMap<>();

    public String getVelicleID() {
        return velicleID;
    }

    public void setVelicleID(String velicleID) {
        this.velicleID = velicleID;
    }

    public Map<String, Point> getLocations() {
        return locationMap;
    }

    /**
     * 获取具体动车的位置
     * @author Jef
     * @date 2020/6/20
     * @param velicleID
     */
    public Point getLocation(String velicleID) {
        return locationMap.get(velicleID);
    }

    /**
     * 具体动车的移动
     * @author Jef
     * @date 2020/6/20
     * @param event
     */
    public void vehicleMoved(VehicleMovedEvent event) {
        Point point = event.getNewLocation();
        this.setLocation(event.getVelicleID(), point.getLatitude(), point.getLongitude());
    }

    /**
     * 设置动车的位置
     * @author Jef
     * @date 2020/6/20
     * @param velicleID
     * @param latitude
     * @param longitude
     */
    public void setLocation(String velicleID, String latitude, String longitude) {
        Point point = new Point(latitude, longitude);
        locationMap.put(velicleID, point);
    }

}