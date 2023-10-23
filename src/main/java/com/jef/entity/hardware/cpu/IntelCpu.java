package com.jef.entity.hardware.cpu;

import com.jef.entity.hardware.Matter;

import java.math.BigDecimal;

/**
 * Intel CPU
 *
 * @author Jef
 * @date 2021/12/7
 */
public class IntelCpu implements Matter {
    @Override
    public String getType() {
        return "CPU";
    }

    @Override
    public String getBrand() {
        return "因特尔";
    }

    @Override
    public String getModel() {
        return "I001";
    }

    @Override
    public BigDecimal getPrice() {
        return new BigDecimal(100);
    }

}