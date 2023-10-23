package com.jef.entity.hardware.memory;

import com.jef.entity.hardware.Matter;

import java.math.BigDecimal;

/**
 * 三星内存条
 *
 * @author Jef
 * @date 2021/12/7
 */
public class SansungMemory implements Matter {
    @Override
    public String getType() {
        return "内存";
    }

    @Override
    public String getBrand() {
        return "三星";
    }

    @Override
    public String getModel() {
        return "S001";
    }

    @Override
    public BigDecimal getPrice() {
        return new BigDecimal(70);
    }
}