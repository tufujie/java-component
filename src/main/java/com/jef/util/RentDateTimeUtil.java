package com.jef.util;

import cn.hutool.core.date.DateTime;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 租赁日期工具类
 *
 * @author Jef
 * @date 2021/3/17
 */
public class RentDateTimeUtil {
    /**
     * 获取特殊周期结束日期，新算法
     *
     * @param beginDate 开始日期
     * @return java.util.Date
     * @author Jef
     * @date 2019/9/20
     */
    public static Date getPeriodEndDateOfMoreThanTwentyNineByCircle(Date beginDate, Date contractStartDate) {
        return getPeriodEndDateOfMoreThanTwentyNineByCircle(beginDate, contractStartDate, 0);
    }

    /**
     * 获取特殊周期结束日期，新算法
     *
     * @param beginDate      开始日期
     * @param circleStartDay 周期开始日
     * @return java.util.Date
     * @author Jef
     * @date 2019/9/20
     */
    public static Date getPeriodEndDateOfMoreThanTwentyNineByCircle(Date beginDate, int circleStartDay) {
        return getPeriodEndDateOfMoreThanTwentyNineByCircle(beginDate, null, circleStartDay);
    }
    /**
     * 获取特殊周期结束日期，新算法
     *
     * @param beginDate         开始日期
     * @param contractStartDate 合同计费开始日
     * @param circleStartDay    周期开始日
     * @return java.util.Date
     * @author Jef
     * @date 2019/9/20
     */
    public static Date getPeriodEndDateOfMoreThanTwentyNineByCircle(Date beginDate, Date contractStartDate, int circleStartDay) {
        Date endDate = null;
        Calendar periodStartDateCalendar = Calendar.getInstance();
        periodStartDateCalendar.setTime(beginDate);
        int month = periodStartDateCalendar.get(Calendar.MONTH), day = periodStartDateCalendar.get(Calendar.DAY_OF_MONTH);
        if (circleStartDay <= 0) {
            if (LogicUtils.isNull(contractStartDate)) {
                contractStartDate = beginDate;
            }
            Calendar contractStartDateCalendar = Calendar.getInstance();
            contractStartDateCalendar.setTime(contractStartDate);
            circleStartDay = contractStartDateCalendar.get(Calendar.DAY_OF_MONTH);
        }
        boolean addTwentyNineFlag = false;
        // 3月1日 5月1日 7月1日 10月1日 12月1日
        List<Integer> monthList = Arrays.asList(2, 4, 6, 9, 11);
        if (month == 0 && (day == 31 || day == 30)) {
            // 1月31日
            int year = periodStartDateCalendar.get(Calendar.YEAR);
            boolean leapYear = DateTimeUtil.isLeapYear(year);
            int addDay = leapYear ? 29 : 28;
            if (day == 30) {
                // 1月30日
                addDay += 1;
            }
            endDate = DateTimeUtil.addDays(beginDate, addDay);
        } else if (month == 2 && day == 1 && (circleStartDay == 29 || circleStartDay == 30)) {
            // 3月1日且合同开始日期是29、30
            int addDays = 27;
            if (circleStartDay == 30) {
                addDays = 28;
            }
            endDate = DateTimeUtil.addDays(beginDate, addDays);
        } else if (circleStartDay == 31 && day == 1 && monthList.contains(month)) {
            // 合同开始日期是31
            addTwentyNineFlag = true;
        }
        if (addTwentyNineFlag) {
            endDate = DateTimeUtil.addDays(beginDate, 29);
        } else if (LogicUtils.isNull(endDate)) {
            endDate = DateTimeUtil.getPeriodEndDate(beginDate, 1);
        }
        return endDate;
    }


    /**
     * 计算日期所在季度的结束日
     *
     * @param date 某个日期
     * @return java.util.Date
     * @author Jef
     * @date 2021/3/15 14:28
     */
    public static Date getQuarterEndDay(Date date) {
        DateTime dateTime = cn.hutool.core.date.DateUtil.endOfQuarter(date);
        return DateTimeUtil.parseDate(DateTimeUtil.formatDate(dateTime));
    }
    /**
     * 计算日期所在半年的结束日
     *
     * @param date 某个日期
     * @return java.util.Date
     * @author Jef
     * @date 2021/3/15 14:28
     */
    public static Date getHalfYearEndDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int currentMonth = calendar.get(Calendar.MONTH);
        if (currentMonth >= 0 && currentMonth <= 5) {
            calendar.set(Calendar.MONTH, 5);
            calendar.set(Calendar.DATE, 30);
        } else if (currentMonth >= 6 && currentMonth <= 11) {
            calendar.set(Calendar.MONTH, 11);
            calendar.set(Calendar.DATE, 31);
        }
        return DateTimeUtil.parseDate(DateTimeUtil.formatDate(calendar.getTime()));
    }
}