package com.jef.entity.hardware.memory;

import com.jef.entity.hardware.Matter;

import java.math.BigDecimal;

/**
 * 金士顿内存条
 *
 * @author Jef
 * @date 2021/12/7
 */
public class KingstonMemory implements Matter {
    @Override
    public String getType() {
        return "内存";
    }

    @Override
    public String getBrand() {
        return "金士顿";
    }

    @Override
    public String getModel() {
        return "K001";
    }

    @Override
    public BigDecimal getPrice() {
        return new BigDecimal(80);
    }
}