package com.jef.util;

import com.jef.entity.User;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 用户测试类工具
 * @author Jef
 * @date 2019/11/1
 */
public class UserUtil {


    /**
     * 打印用户姓名和电话
     * @author Jef
     * @date 2019/11/1
     * @param user
     * @return void
     */
    public static void printUserNameAndPhone(User user) {
        List<User> users = Arrays.asList(user);
        printUserNameAndPhone(users);
    }

    /**
     * 打印用户姓名和电话
     * @author Jef
     * @date 2019/11/1
     * @param userList
     * @return void
     */
    public static void printUserNameAndPhone(List<User> userList) {
        for (User user : userList) {
            System.out.println("姓名=" + user.getName() + "；电话=" + user.getPhone());
        }
        System.out.println("------------");
    }

    /**
     * 获取用户名大写
     * @param user
     * @return
     */
    public static String getUpperUserName(User user) {
        if (user != null) {
            String userName = user.getName();
            if (userName != null) {
                return userName.toUpperCase();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 获取用户名大写jdk8
     * @param user
     * @return
     */
    public static String getUpperUserNameV2(User user) {
        Optional<User> userOpt = Optional.ofNullable(user);
        return userOpt.map(User::getName)
                .map(String::toUpperCase)
                .orElse(null);
    }
}