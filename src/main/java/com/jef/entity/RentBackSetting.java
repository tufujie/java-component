package com.jef.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 返租比例约定
 * @author Jef
 * @date 2018/8/20
 */
public class RentBackSetting implements Serializable {
    private static final long serialVersionUID = -7422460958474199144L;
    /**
     * Redis缓存key
     */
    private static final String OBJECT_KEY = "t_rt_rentbacksetting";
    private String id;
    /**
     * 合同ID
     */
    private String contractID;
    /**
     * 开始日期
     */
    private Date startDate;
    /**
     * 结束日期
     */
    private Date endDate;
    /**
     * 返租比例类型，1递增，2递减
     */
    private Integer backType;
    /**
     * 返租比例，1.0，5.5
     */
    private BigDecimal backPercent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContractID() {
        return contractID;
    }

    public void setContractID(String contractID) {
        this.contractID = contractID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getBackType() {
        return backType;
    }

    public void setBackType(Integer backType) {
        this.backType = backType;
    }

    public BigDecimal getBackPercent() {
        return backPercent;
    }

    public void setBackPercent(BigDecimal backPercent) {
        this.backPercent = backPercent;
    }
}
