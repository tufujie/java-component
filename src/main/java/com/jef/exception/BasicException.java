package com.jef.exception;

import java.util.logging.Logger;

/**
 * 异常处理程序
 * 重要原则：只有在你知道如何处理的情况下才捕获异常
 * 对于异常来说，要么捕获处理、要么抛出让上一级处理，上级处理方式亦是如此
 * @author Jef
 * @create 20180715
 */
public class BasicException extends Exception {
    private static final Logger logger = Logger.getLogger("BasicException");

    public BasicException(String message) {
        super(message);
    }

    /**
     * 基本异常，用于测试
     * @param a
     * @param b
     * @throws Exception
     */
    public static void basicException(int a, int b) throws Exception {
        System.out.println("result=" + a / b);
    }


    /**
     * 打印异常日志
     * @param e
     */
    public static void logException(Exception e) {
        // 打出堆栈信息1
        /*StringWriter trace = new StringWriter();
        e.printStackTrace(new PrintWriter(trace));
        logger.severe(trace.toString());*/
        // 打出堆栈信息2
//        e.printStackTrace();
        // 打出堆栈信息3
        /*for (StackTraceElement ste :  e.getStackTrace()) {
            System.out.println(ste);
        }
*/
       /* // 异常1
        System.out.println(e.toString());
        // 异常2
        System.out.println(e);
        // 异常信息
        System.out.println(e.getMessage());*/
    }
}
