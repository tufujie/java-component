package com.jef.util;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jef
 * @date 2020/3/8
 */
public class ProductSupportUtil {

    /**
     * 获取系统的所有版本
     * @author Jef
     * @date 2020/3/8
     * @return java.lang.String
     */
    public static String getAllProduct() {
        return "1,2,4,8,16,32";
    }

    /**
     * 获取设置product的数值。只输出一个
     * @author Jef
     * @date 2020/3/8
     * @param supportProduct 需要支持的版本
     */
    public static Integer getSetProductValue(String supportProduct) {
        return getSetProductValue(getAllProduct(), supportProduct);
    }

    /**
     * 获取设置product的数值
     * @author Jef
     * @date 2020/3/8
     * @param allProduct 所有版本
     * @param supportProduct 需要支持的版本
     */
    public static Integer getSetProductValue(String allProduct, String supportProduct) {
        List<Integer> supportProductList = getProductListByProduct(supportProduct);
        List<Integer> allProductList = getProductListByProduct(allProduct);
        List<Integer> notMatchList = allProductList.stream().filter(vo -> !supportProductList.contains(vo)).collect(Collectors.toList());
        Integer product = null;
        for (int i = 1; i < 100; i++) {
            boolean match = true;
            for (Integer type : supportProductList) {
                if ((type & i) <= 0) {
                    match = false;
                    break;
                }
            }
            if (match) {
                for (Integer type : notMatchList) {
                    if ((type & i) > 0) {
                        match = false;
                        break;
                    }
                }
            }
            if (match) {
                product = i;
                break;
            }
        }
        return product;
    }

    /**
     * 获取所有类型
     * @author Jef
     * @date 2020/3/8
     * @param product 以英文逗号分割的版本
     */
    private static List<Integer> getProductListByProduct(String product) {
        List<String> typeList = StringUtils.getListFromString(product);
        return typeList.stream().map(Integer::valueOf).collect(Collectors.toList());
    }

}