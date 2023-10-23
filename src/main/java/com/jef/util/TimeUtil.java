package com.jef.util;

import java.util.Date;

/**
 * @author Jef
 * @date 2020/4/6
 */
public class TimeUtil {

    private static double getUseSecondTime(long startTime, long endTime) {
        return ((endTime - startTime)) / (double) 1000;
    }

    public static void printTotalTime(long startTime, long endTime) {
        System.out.println(String.format("运行总花费时间=%10.6f秒", getUseSecondTime(startTime, endTime)));
    }

    /**
     * 打印所有使用的时间
     * @author Jef
     * @date 2019/8/20
     * @param dateStart
     * @return void
     */
    public static void printAllUseTime(Date dateStart) {
        printAllUseTime(dateStart, new Date());
    }


    /**
     * 打印所有使用的时间
     * @author Jef
     * @date 2019/8/20
     * @param dateStart
     * @param dateEnd
     * @return void
     */
    public static void printAllUseTime(Date dateStart, Date dateEnd) {
        System.out.println("任务运行结束，任务总运行时间【" + getUseSecondTime(dateStart.getTime(), dateEnd.getTime()) + "秒】");
    }
}