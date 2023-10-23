package com.jef.exception;

import com.jef.util.StringUtils;

/**
 * java.lang.IllegalArgumentException: Illegal group reference
 * @author Jef
 * @date 2019/4/16
 */
public class ExceptionDeal {
    public static void main(String[] args) {
        exceptionAndDealOne();
    }

    private static void exceptionAndDealOne() {
        String testStr = "te st";
        try {
            System.out.println(StringUtils.replaceAll(testStr, " ", "$$", true));
        } catch (Exception e) {
            System.out.println("出现异常=" + e.getMessage());
        }
        System.out.println("使用解决方案");
        System.out.println(testStr.replaceAll(" ", java.util.regex.Matcher.quoteReplacement("$$")));
    }
}