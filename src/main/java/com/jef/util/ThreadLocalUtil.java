package com.jef.util;

import com.jef.constant.BasicConstant;
import com.jef.entity.User;

/**
 * @author tufujie
 * @date 2023/8/17
 */
public class ThreadLocalUtil {
    static ThreadLocal<String> threadLocalName = new ThreadLocal<String>();

    static ThreadLocal<User> threadLocalUser = new ThreadLocal<User>();

    public static void setThreadLocalName(String name) {
        threadLocalName.set(name);
    }

    public static String getThreadLocalName() {
        return threadLocalName.get();
    }

    public static void removeThreadLocalName() {
        threadLocalName.remove();
    }

    public static void setThreadLocalUser(User user) {
        threadLocalUser.set(user);
    }

    public static User getThreadLocalUser() {
        return threadLocalUser.get();
    }

    public static void removeThreadLocalUser() {
        threadLocalUser.remove();
    }


    public static void main(String[] args) {
        for (int i = 1; i < 3; i++) {
            int finalI = i;
            Thread thread1 = new Thread(() -> {
                ThreadLocalUtil.setThreadLocalName("设置时的线程名称=" + Thread.currentThread().getName() + "，值为=" + BasicConstant.USER_NAME + finalI);
                System.out.println(ThreadLocalUtil.getThreadLocalName() + "；获取时的线程名称=" + Thread.currentThread().getName());
                ThreadLocalUtil.removeThreadLocalName();
            });
            thread1.start();
        }
    }
}