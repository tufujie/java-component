package com.jef.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 修改变化的id集合工具类
 * @author Jef
 * @date 2019/5/30
 */
public class IdChangeUtil {

    /**
     * 获取新增和删除的id集合
     * 解决思路：
     * 1.向set中放入修改后的权限id集合，通过removeall(修改前的权限集合id)获取新增的权限集合；
     * 2.向set中放入修改前的权限id集合，通过removeall(修改后的权限集合id)获取删除的权限集合，
     * 3.分别进行相应的删除和新增操作。
     * @author Jef
     * @date 2019/5/31
     * @param newSet 修改前的集合
     * @param oldSet 修改后的集合
     * @return java.util.Map<java.lang.String,java.util.Set<java.lang.String>>
     */
    public static Map<String, Set<String>> solveChangeID(Set<String> newSet, Set<String> oldSet) {
        // 新增的id集合
        Set<String> addSet = new HashSet<>();
        // 删除的id集合
        Set<String> removeSet = new HashSet<>();
        removeSet.addAll(oldSet);
        removeSet.removeAll(newSet);
        addSet.addAll(newSet);
        addSet.removeAll(oldSet);
        Map<String, Set<String>> resultMap = new HashMap<>();
        resultMap.put("addSet", addSet);
        resultMap.put("removeSet", removeSet);
        return resultMap;
    }
}