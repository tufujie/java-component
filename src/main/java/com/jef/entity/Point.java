package com.jef.entity;

/**
 * 坐标
 * @author Jef
 * @date 2020/6/20
 */
public class Point {

    /**
     * 经度
     */
    private String latitude;
    /**
     * 纬度
     */
    private String longitude;

    public Point(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}