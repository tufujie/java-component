package com.jef.util;

/**
 * @author Jef
 * @date 2019/4/4
 */
public class DBUtil {

    /**
     * 根据用户ID分表
     * @param userID 用户ID
     * @return
     */
    public static Integer getTableNameByUserID(Long userID) {
        return userID % 2 == 1 ? 1 : 2;
    }
}