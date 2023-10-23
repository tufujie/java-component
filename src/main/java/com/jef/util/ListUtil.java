package com.jef.util;

import com.jef.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * List集合工具
 *
 * @author Jef
 * @date 2018/9/14 11:09
 */
public class ListUtil {
    /**
     * 将list按照指定元素个数(n)分割
     * @param <T> 类型
     * @param source 集合
     * @param n 按n个分割
     * @return 返回分割后的几个集合
     */
    public static <T> List<List<T>> partList(List<T> source, int n) {
        if (source == null) {
            return null;
        }

        if (n == 0) {
            return null;
        }
        List<List<T>> result = new ArrayList<List<T>>();
        // 集合长度
        int size = source.size();
        // 余数
        int remaider = size % n;
        // 商
        int number = size / n;
        for (int i = 0; i < number; i++) {
            List<T> value = source.subList(i * n, (i + 1) * n);
            result.add(value);
        }

        if (remaider > 0) {
            List<T> subList = source.subList(size - remaider, size);
            result.add(subList);
        }
        return result;
    }

    /**
     * 输出IDList
     * @author Jef
     * @date 2020/1/17
     * @param idList id集合
     */
    public static void systemPrintStringList(List<String> idList) {
        systemPrintStringList(idList, "");
    }

    /**
     * 输出IDList
     * @author Jef
     * @date 2020/1/17
     * @param idList id集合
     */
    public static void systemPrintStringList(List<String> idList, String message) {
        System.out.println(message + ",stringList集合=" + idList);
    }

    /**
     * 输出IDList
     * @author Jef
     * @date 2020/1/17
     * @param idList id集合
     */
    public static void systemPrintIDList(List<Long> idList) {
        systemPrintIDList(idList, "");
    }

    /**
     * 输出IDList
     * @author Jef
     * @date 2020/1/17
     * @param idList id集合
     */
    public static void systemPrintIDList(List<Long> idList, String message) {
        System.out.println(message + ",idList集合=" + idList);
    }

    /**
     * 输出userList
     * @author Jef
     * @date 2020/1/17
     * @param userList id集合
     */
    public static void systemPrintUserList(List<User> userList) {
        systemPrintUserList(userList, "");
    }
    /**
     * 输出userList
     * @author Jef
     * @date 2020/1/17
     * @param userList id集合
     */
    public static void systemPrintUserList(List<User> userList, String message) {
        System.out.println(message + userList);
    }

}
