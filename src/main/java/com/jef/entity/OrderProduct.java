package com.jef.entity;

/**
 * 子订单实体类
 *
 * @author Jef
 * @create 2018/5/16 9:34
 */
public class OrderProduct {
    private Long id;

    private Long orderId;

    private String productName;

    private Integer num;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
