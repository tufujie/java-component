package com.jef.entity.hardware;

import java.math.BigDecimal;

/**
 * 物料
 *
 * @author Jef
 * @date 2021/12/7
 */
public interface Matter {

    /**
     * 硬件类型
     */
    String getType();

    /**
     * 品牌
     */
    String getBrand();

    /**
     * 型号
     */
    String getModel();

    /**
     * 价格
     */
    BigDecimal getPrice();

}