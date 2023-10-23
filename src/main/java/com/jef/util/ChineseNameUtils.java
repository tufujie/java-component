package com.jef.util;

import java.util.Random;

/**
 * 起名工具类
 *
 * @author Administrator
 * @date 2022/9/30
 */
public class ChineseNameUtils {

    /**
     * 百家姓
     */
    private final static String LAST_NAME = "涂";

    private static final String NAME = "人之初性本善性相近习相远苟不教性乃迁教之道贵以专昔孟母择邻处子不学断机杼窦燕";

    private static String getChineseCharacterFromBook() {
        int random = new Random().nextInt(NAME.length());
        return String.valueOf(NAME.charAt(random));
    }

    public static String getChineseName() {
        //获得一个随机的姓氏
        Random random = new Random(System.currentTimeMillis());
        String name = LAST_NAME;
        /* 从常用字中选取一个或两个字作为名 */
        name += getChineseCharacter() + getChineseCharacter();
        return name;
    }

    public static String getChineseNameBook() {
        //获得一个随机的姓氏
        Random random = new Random(System.currentTimeMillis());
        String name = LAST_NAME;
        /* 从常用字中选取一个或两个字作为名 */
        name += getChineseCharacterFromBook() +
                getChineseCharacterFromBook();
        return name;
    }

    private static String getChineseCharacter() {
        int random = new Random().nextInt(0x9FBB - 0x4E00 + 1) + 0x4E00;
        return new String(new char[]{(char) (random)});
    }

    public static void main(String[] args) {
        System.out.println(getChineseNameBook());
    }

}