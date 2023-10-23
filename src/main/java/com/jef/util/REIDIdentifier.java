package com.jef.util;

import java.util.UUID;

/**
 * 唯一ID生成器
 * @author Jef
 * @create 2018/6/20 13:16
 */
public class REIDIdentifier {
    /**
     * 生成UUID
     * @return
     */
    public static String randomEOID() {
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        return id;
    }

    /**
     * 生成n个随机UUID
     * @param n 随机数的个数
     */
    public static void generateNUUID(Integer n) {
        for (int i = 0; i < n; i++) {
            System.out.println(randomEOID());
        }
    }
}
