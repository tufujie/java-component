package com.jef.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单信息
 *
 * @author Jef
 * @create 2018/5/15 19:18
 */
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = -7738420644752056965L;
    /**
     * ID
     */
    private Long id;

    private String extraOrderId;

    private Long shopId;

    private BigDecimal totalPrice;

    private BigDecimal totalInPrice;

    /**
     * 订单商品
     * @return
     */
    private List<OrderProduct> orderProductList;

    /**
     * 折扣方式
     */
    private int discountType;

    /**
     * 购买的用户信息
     */
    private User user;

    // 订单ID
    private String orderId;
    // 用户ID
    private String uid;
    // 商品
    private String sku;
    // 创建时间
    private Date createOrderTime;
    // 创建人ID
    private Long creatorID;
    // 创建人名称
    private String creatorName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExtraOrderId() {
        return extraOrderId;
    }

    public void setExtraOrderId(String extraOrderId) {
        this.extraOrderId = extraOrderId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalInPrice() {
        return totalInPrice;
    }

    public void setTotalInPrice(BigDecimal totalInPrice) {
        this.totalInPrice = totalInPrice;
    }

    public List<OrderProduct> getOrderProductList() {
        return orderProductList;
    }

    public void setOrderProductList(List<OrderProduct> orderProductList) {
        this.orderProductList = orderProductList;
    }

    public int getDiscountType() {
        return discountType;
    }

    public void setDiscountType(int discountType) {
        this.discountType = discountType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Date getCreateOrderTime() {
        return createOrderTime;
    }

    public void setCreateOrderTime(Date createOrderTime) {
        this.createOrderTime = createOrderTime;
    }

    public Long getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(Long creatorID) {
        this.creatorID = creatorID;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
}
