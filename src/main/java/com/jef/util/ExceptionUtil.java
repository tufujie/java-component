package com.jef.util;

/**
 * 异常处理
 *
 * @author Jef
 * @date 2019/5/8
 */
public class ExceptionUtil {
    /**
     * 获取异常信息
     *
     * @param e
     * @return void
     * @author Jef
     * @date 2019/8/20
     */
    public static String getExceptionStackTraceMessage(Exception e) {
        return getExceptionStackTraceMessage("发生异常", e);
    }

    /**
     * 获取异常信息
     *
     * @param msg
     * @param e
     * @return void
     * @author Jef
     * @date 2019/8/20
     */
    public static String getExceptionStackTraceMessage(String msg, Exception e) {
        StringBuffer sb = new StringBuffer();
        if (e == null) {
            return sb.toString();
        }
        if (msg != null) {
            sb.append(msg).append("\n");
        }
        return getExceptionStackTraceMessage(e.getStackTrace(), e, sb);
    }

    /**
     * 获取异常信息
     *
     * @param m
     * @param e
     * @param sb
     * @return java.lang.String
     * @author Jef
     * @date 2021/4/15
     */
    public static String getExceptionStackTraceMessage(StackTraceElement[] m, Exception e, StringBuffer sb) {
        sb.append("堆栈信息为：");
        for (StackTraceElement ste : m) {
            sb.append(ste.toString());
            sb.append("\n");
        }
        sb.append("\n");
        sb.append("异常信息为：");
        sb.append(e.toString());
        System.out.println(sb);
        return sb.toString();
    }

}