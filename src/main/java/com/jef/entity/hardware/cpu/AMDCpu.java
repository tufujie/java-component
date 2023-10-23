package com.jef.entity.hardware.cpu;

import com.jef.entity.hardware.Matter;

import java.math.BigDecimal;

/**
 * AMD CPU
 *
 * @author Jef
 * @date 2021/12/7
 */
public class AMDCpu implements Matter {
    @Override
    public String getType() {
        return "CPU";
    }

    @Override
    public String getBrand() {
        return "AMD";
    }

    @Override
    public String getModel() {
        return "A001";
    }

    @Override
    public BigDecimal getPrice() {
        return new BigDecimal(90);
    }
}