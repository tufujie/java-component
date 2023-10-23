package com.jef.util;

import java.util.Arrays;
import java.util.List;

/**
 * @author Jef
 * @date 2020/4/5
 */
public class IntegerUtil {
    /**
     * 获取测试使用集合，用于通用功能测试
     * @author Jef
     * @date 2020/3/25
     * @param
     * @return java.util.List<java.lang.String>
     */
    public static Integer[] getTestIntegerArray() {
        return new Integer[]{-2, 11, -4, 14, -5, -2};
    }


    /**
     * 获取测试使用集合，用于通用功能测试
     * @author Jef
     * @date 2020/3/25
     * @param
     * @return java.util.List<java.lang.String>
     */
    public static List<Integer> getTestIntegerList() {
        return Arrays.asList(-2, 11, -4, 13, -5, -2);
    }

    /**
     * 获取测试使用集合，用于通用功能测试
     * @author Jef
     * @date 2020/3/25
     * @param
     * @return java.util.List<java.lang.String>
     */
    public static List<Integer> getTestIntegerListV2() {
        return Arrays.asList(4, -3, 5, -2, -1, 2, 6, -2);
    }

}