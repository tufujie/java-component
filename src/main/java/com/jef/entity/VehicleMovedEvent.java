package com.jef.entity;

/**
 * 动车运行事件
 * @author Jef
 * @date 2020/6/20
 */
public class VehicleMovedEvent {
    /**
     * 动车ID
     */
    private String velicleID;

    public VehicleMovedEvent(String velicleID) {
        this.velicleID = velicleID;
    }

    public String getVelicleID() {
        return velicleID;
    }

    public void setVelicleID(String velicleID) {
        this.velicleID = velicleID;
    }

    Point getNewLocation() {
        // 从卫星定位上获取定位
        String latitude = "3", longitude = "4";
        return new Point(latitude, longitude);
    }
}

