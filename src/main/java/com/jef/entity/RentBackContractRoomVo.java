package com.jef.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 返租资源
 * @author Jef
 * @date 2018/8/20
 */
public class RentBackContractRoomVo implements Serializable {
    private static final long serialVersionUID = 2144829789067526791L;
    /**
     * Redis缓存key
     */
    private static final String OBJECT_KEY = "t_rt_rentbackcontractroom";

    private String id;
    /**
     * 合同ID
     */
    private String contractID;
    /**
     * 房间ID
     */
    private String roomID;


    // 拓展字段
    /**
     * 房间名称
     */
    private String roomName;
    /**
     * 楼层
     */
    private String floor;
    /**
     * 朝向
     */
    private String orientation;
    /**
     * 户型
     */
    private String roomModel;
    /**
     * 产品类型
     */
    private String productType;
    /**
     * 建筑面积
     */
    private BigDecimal buildingArea;
    /**
     * 套内面积
     */
    private BigDecimal roomArea;
    /**
     * 年租金
     */
    private BigDecimal yearAmount;
    /**
     * 月租金
     */
    private BigDecimal monthAmount;
    /**
     * 建筑面积日单价
     */
    private BigDecimal buildingDayPrice;
    /**
     * 建筑面积月单价
     */
    private BigDecimal buildingMonthPrice;
    /**
     * 套内面积日单价
     */
    private BigDecimal roomDayPrice;
    /**
     * 套内面积月单价
     */
    private BigDecimal roomMonthPrice;
    /**
     * 月底价
     */
    private BigDecimal monthFloorPrice;
    /**
     * 建筑面积月单价底价
     */
    private BigDecimal buildingMonthFloorPrice;
    /**
     * 套内面积月单价底价
     */
    private BigDecimal roomMonthFloorPrice;
    /**
     * 押金
     */
    private BigDecimal deposit;



}
