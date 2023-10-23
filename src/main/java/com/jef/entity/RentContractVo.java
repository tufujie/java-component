package com.jef.entity;

import java.math.BigDecimal;

/**
 * 合同表
 *
 * @author Jef
 * @date 2018/8/13 11:03
 */
public class RentContractVo {

    private int isBreach; // 违约金设置 是否计算违约金(0否,1是)

    private int billingCycle; // 计算周期（0天，1月）

    private int breachDay; // 计算违约金天数（应收日之后）

    private BigDecimal calStandard; // 计算标准

    private int freeType; // 豁免类型（0天，1月）

    private int immunity; // 豁免天数

    public int getIsBreach() {
        return isBreach;
    }

    public void setIsBreach(int isBreach) {
        this.isBreach = isBreach;
    }

    public int getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(int billingCycle) {
        this.billingCycle = billingCycle;
    }

    public BigDecimal getCalStandard() {
        return calStandard;
    }

    public void setCalStandard(BigDecimal calStandard) {
        this.calStandard = calStandard;
    }

    public int getFreeType() {
        return freeType;
    }

    public void setFreeType(int freeType) {
        this.freeType = freeType;
    }

    public int getImmunity() {
        return immunity;
    }

    public void setImmunity(int immunity) {
        this.immunity = immunity;
    }

    public int getBreachDay() {
        return breachDay;
    }

    public void setBreachDay(int breachDay) {
        this.breachDay = breachDay;
    }

}
