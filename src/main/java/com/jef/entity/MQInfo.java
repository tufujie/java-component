package com.jef.entity;

import java.util.Date;

/**
 * @author Jef
 * @date 2021/12/7
 */
public class MQInfo {

    // 用户ID
    private String userId;
    // 业务类型
    private String mqAdaptor;
    // 业务ID
    private String bizId;
    // 业务时间
    private Date bizTime;
    // 业务描述
    private String desc;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMqAdaptor() {
        return mqAdaptor;
    }

    public void setMqAdaptor(String mqAdaptor) {
        this.mqAdaptor = mqAdaptor;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public Date getBizTime() {
        return bizTime;
    }

    public void setBizTime(Date bizTime) {
        this.bizTime = bizTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}