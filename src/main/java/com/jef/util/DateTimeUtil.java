package com.jef.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 时间转化工具全，对原始工具类进行拓展
 * 原始时间工具类中已存在功能
 * 比较两个时间是否同一天
 * 将String格式的日期转化为日期
 * 添加年、月、星期、日、小时、分钟、秒、毫秒
 * 修改...
 * 等等
 * 同步到865行
 *
 * @author Jef
 * @create 2018/6/1 19:13
 */
public class DateTimeUtil extends DateUtils {
    public static final String COMPACT_DATE_FORMAT = "yyyyMMdd";
    public static final String YM = "yyyyMM";
    public static final String YM_SECOND = "yyyy-MM";
    public static final String NORMAL_DATE_FORMAT = "yyyy-MM-dd";
    public static final String NORMAL_DATE_FORMATV2 = "yyyy/MM/dd";
    public static final String NORMAL_DATE_FORMAT_NEW = "yyyy-mm-dd hh24:mi:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_ALL = "yyyyMMddHHmmssS";
    public static final String DATETIME_Z_FORMAT = "yyyy-MM-dd HH:mm:ss 'GMT'Z";
    public static final String GMT_Z_FORMAT = "d MMM yyyy HH:mm:ss 'GMT'";
    public static final String GMT_T_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_SECONND = "yyyyMMddHHmmss";
    public static final String DATE_CHINESE_FORMAT = "yyyy年MM月dd日HH时mm分ss秒"; // 中国时间
    public static final String YEAR_FORMAT = "yyyy";
    public static final String MONTH_DAY_FORMAT = "MM-dd";
    public static final String MD = "MMdd";
    public static final String MONTH_FORMAT = "MM";
    public static final String DAY_FORMAT = "dd";
    public static final String DATE_FORMAT_SSS = "yyyy-MM-dd HH:mm:ss.SSS"; // 精确到毫秒
    public static final String DATETIME_FORMAT3 = "yyyyMMddHHmmss";

    // 偏移方向
    public static final int TO_BEFORE = 0; // 偏移方向，向前
    public static final int TO_AFTER = 1; // 偏移方向，向后
    // 时间偏移类型，按年、按月、按天、按小时、按分钟偏移
    public static final int DIRECT_TYPE_OF_YEAR = 1;
    public static final int DIRECT_TYPE_OF_MONTH = 2;
    public static final int DIRECT_TYPE_OF_DAY = 7;
    public static final int DIRECT_TYPE_OF_HOUR = 10;
    public static final int DIRECT_TYPE_OF_MINUTE = 12;

    public static final int CPMPARE_DAY = 0;
    public static final int CPMPARE_MONTH = 1;
    public static final int CPMPARE_YEAR = 2;

    public static final String SMT_TOMEZONE = "GMT-8:00";

    public static String dayNamesStartSunday[] = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    public static String dayNamesStartMonday[] = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
    public static final SimpleDateFormat SHORT_DATE_FORMAT = new SimpleDateFormat(NORMAL_DATE_FORMAT);

    public static final int[] DAY_ARRAY = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    public static final int[] DAY_ARRAY_ONE = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private static final ThreadLocal<SimpleDateFormat> DATE_FORMAT_THREAD_LOCAL = new ThreadLocal<>();
    public static final long SECOND = 1000L;
    public static final long MINUTE = 60000L;
    public static final long HOUR = 3600000L;
    public static final long DAY = 86400000L;
    public static final long WEEK = 604800000L;
    public static final long YEAR = 31536000000L;

    // 天数
    private static Integer DAY_NUM = 365;
    // 月份数
    private static Integer MONTH_NUM = 12;

    /**
     * 设置日期格式：SimpleDateFormat 非线程安全安全处理
     *
     * @param format
     */
    private static SimpleDateFormat setAndGetDateFormat(String format) {
        if (format == null) {
            format = NORMAL_DATE_FORMAT;
        }
        DATE_FORMAT_THREAD_LOCAL.remove();
        if (!(NORMAL_DATE_FORMAT.equals(format)
                || YEAR_FORMAT.equals(format)
                || MONTH_FORMAT.equals(format)
                || DAY_FORMAT.equals(format)
                || MONTH_DAY_FORMAT.equals(format)
                || DEFAULT_DATE_FORMAT.equals(format)
                || YM.equals(format)
                || COMPACT_DATE_FORMAT.equals(format)
                || YM_SECOND.equals(format))) {
            format = NORMAL_DATE_FORMAT;
        }
        DATE_FORMAT_THREAD_LOCAL.set(new SimpleDateFormat(format));
        return DATE_FORMAT_THREAD_LOCAL.get();
    }

    /**
     * 返回两个日期内的月份，开始日期大于结束日期，加一年
     *
     * @param startDate "01-27"
     * @param endDate   "12-01"
     * @return
     * @throws Exception
     */
    public static List<String> getMonthBetween(String startDate, String endDate) throws Exception {
        ArrayList<String> result = new ArrayList<>();
        // 格式化为月日
        SimpleDateFormat sdfMd = setAndGetDateFormat(MONTH_DAY_FORMAT);
        // 格式化为月
        SimpleDateFormat sdfM = setAndGetDateFormat(MONTH_FORMAT);
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(sdfMd.parse(startDate));
        start.set(start.get(Calendar.YEAR), start.get(Calendar.MONTH), 1);
        end.setTime(sdfMd.parse(endDate));
        end.set(start.get(Calendar.YEAR), end.get(Calendar.MONTH), 2);
        // 开始日期大于结束日期， 加一年
        if (start.after(end)) {
            end.add(Calendar.YEAR, 1);
        }
        while (start.before(end)) {
            result.add(sdfM.format(start.getTime()));
            start.add(Calendar.MONTH, 1);
        }
        return result;
    }

    /**
     * @param dateStr   一定格式的字符串
     * @param formatStr 字符串的格式
     * @return 指定格式，从字符串取得日期 默认时区为系统时区
     * @throws ParseException
     */
    public static Date getDateFromStr(String dateStr, String formatStr)
            throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr);
        dateFormat.setLenient(true);
        return dateFormat.parse(dateStr);
    }


    /**
     * @param dateStr yyyy-MM-dd格式的字符串
     * @return 根据默认格式，从字符串取得日期 默认时区为系统时区
     * @throws ParseException
     */
    public static Date getDateFromStr(String dateStr) throws ParseException {
        return getDateFromStr(dateStr, NORMAL_DATE_FORMAT);
    }


    /**
     * 格式化日期成字符串
     *
     * @param date
     * @param formater
     */
    public static String formatDate(Date date, String formater) {
        return new SimpleDateFormat(formater).format(date);
    }

    /**
     * 根据日期取得相应时间字符串，yyyy-MM-dd HH:mm:ss格式
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return formatDate(date, NORMAL_DATE_FORMAT);
    }

    /**
     * 根据数字-年月日时分秒获取日期
     *
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static Date date(int year, int month, int day, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, second);
        return calendar.getTime();
    }

    /**
     * 根据数字-年月日获取日期
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static Date date(int year, int month, int day) {
        return date(year, month - 1, day, 0, 0, 0);
    }

    /**
     * 获取今天的开始时间
     * 0点0分0秒
     */
    public static Date getToday() {
        return getDate0Time(new Date());
    }

    /**
     * 移除时分秒
     *
     * @param date
     */
    public static Date removeTime(Date date) {
        return getDate0Time(date);
    }

    /**
     * 设置时分秒毫秒
     *
     * @param date
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static Date setTime(Date date, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return setTime(calendar, hour, minute, second);
    }

    /**
     * 设置时分秒毫秒
     *
     * @param calendar
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static Date setTime(Calendar calendar, int hour, int minute, int second) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 设置0点
     *
     * @param calendar
     * @return
     */
    public static Date setDateZero(Calendar calendar) {
        return setTime(calendar, 0, 0, 0);
    }

    /**
     * 设置23:59:59
     *
     * @param date
     * @return
     */
    public static Date getDateLast(Date date) {
        return setTime(date, 23, 59, 59);
    }

    /**
     * 设置23:59:59
     *
     * @param calendar
     * @return
     */
    public static Date getDateLast(Calendar calendar) {
        return setTime(calendar, 23, 59, 59);
    }

    /**
     * @param date       待转化的时间
     * @param direction  偏移方向
     * @param offset     偏移量
     * @param directType 偏移类型
     * @return 返回偏移后时间
     * @throws ParseException
     */
    public static Date redirectDate(String date, int direction, int offset, int directType) throws ParseException {
        if (date == null || date.length() < 1) {
            return null;
        }
        return redirectDate(DateTimeUtil.getDateFromStr(date), direction, offset, directType);
    }

    /**
     * @param date       待转化的时间
     * @param direction  偏移方向
     * @param offset     偏移量
     * @param directType 偏移类型
     * @return 返回偏移后时间
     */
    public static Date redirectDate(Date date, int direction, int offset, int directType) {
        if (date == null) {
            return null;
        }
        if (direction == TO_BEFORE) {
            offset = -offset;
        }
        // 对日期进行处理，并返回处理后的日期
        GregorianCalendar worldTour = new GregorianCalendar();
        worldTour.setTime(date);
        worldTour.add(directType, offset);
        return worldTour.getTime();
    }

    /**
     * 获取timezone时区的date时间对应的中国时间
     * eg：date："2017-10-16 00:00:01"  timezone："America/Los_Angeles"。
     * 即美国洛杉矶时区的"2017-10-16 00:00:01"对应的中国时间。返回值为"2017-10-16 15:00:01"
     *
     * @param dateStr
     * @param timeZoneId "America/Los_Angeles"
     * @param formatStr  日期格式 DEFAULT_DATE_FORMAT
     * @return
     */
    @Deprecated
    public static Date getZoneDate(String dateStr, String timeZoneId, String formatStr) throws ParseException {
        final DateFormat format = new SimpleDateFormat(formatStr);
        TimeZone tz = TimeZone.getTimeZone(timeZoneId);
        format.setTimeZone(tz);
        Date date = format.parse(dateStr);

        return date;
    }

    /**
     * @return 昨天的这个时间点
     */
    public static Date getYesterday() {
        Date today = new Date();
        return redirectDate(today, TO_BEFORE, 1, DIRECT_TYPE_OF_DAY);
    }

    /**
     * 获取昨天的这个时间点
     *
     * @param dateTime 某个日期
     * @return java.util.Date
     * @author Jef
     * @date 2019/9/3
     */
    public static Date getYesterday(Date dateTime) {
        return getNextDay(dateTime, -1);
    }

    /**
     * 获取某个时间点对应的昨天的0点
     *
     * @param date
     * @return
     */
    public static Date getYesterday0Time(Date date) {
        date = redirectDate(date, TO_BEFORE, 1, DIRECT_TYPE_OF_DAY);
        return getDate0Time(date);
    }

    /**
     * @param date
     * @param hour 几点
     * @return 获取某个时间点对应的昨天的几点
     */
    public static Date getYesterdayHour(Date date, int hour) {
        date = redirectDate(date, TO_BEFORE, 1, DIRECT_TYPE_OF_DAY);
        return getDateTime(date, hour);
    }

    /**
     * @param date
     * @return 获取某个时间对应的昨天的23:59:59
     */
    public static Date getYesterdayLastSecond(Date date) {
        date = getYesterday0Time(date);
        return getDateLast(date);
    }

    /**
     * 取得某个星期的第1天
     *
     * @param date
     * @param firstDayOfWeek 以某一天作为一周第一天，1，周日，2，周一，以此类推
     */
    public static Date getFirstDayOfWeek(Date date, int firstDayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);
        return setDateZero(calendar);
    }

    /**
     * 获取某个星期的最后一天
     *
     * @param date
     * @param firstDayOfWeek
     * @return
     */
    public static Date getLastDayOfWeek(Date date, int firstDayOfWeek) {
        date = getFirstDayOfWeek(date, firstDayOfWeek);
        return addDays(date, 6);
    }

    /**
     * 获取某个星期的最后时刻
     *
     * @param date
     * @param firstDayOfWeek
     * @return
     */
    public static Date getLastTimeOfWeek(Date date, int firstDayOfWeek) {
        date = getLastDayOfWeek(date, firstDayOfWeek);
        return getDateLast(date);
    }

    /**
     * @return 取得当月第一天的零点
     */
    public static Date getFirstDayInCurrentMonth() {
        return getFirstDateOfMonth(new Date());
    }

    /**
     * 取得某月第一天
     *
     * @return
     */
    public static Date getFirstDateOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return setDateZero(calendar);
    }

    /**
     * 取得某月最后一天最后时刻
     *
     * @return
     */
    public static Date getLastTimeOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return getDateLast(calendar);
    }

    /**
     * 取得某月最后一天
     *
     * @return
     */
    public static Date getLastDateOfMonth(Date date) {
        date = getLastTimeOfMonth(date);
        return getDate0Time(date);
    }

    /**
     * 取得某年第一天
     *
     * @return
     */
    public static Date getFirstDateOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 1月1日
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return setDateZero(calendar);
    }

    /**
     * 取得某年最后一天最后时刻
     *
     * @return
     */
    public static Date getLastTimeOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 12月31日
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        return getDateLast(calendar.getTime());
    }

    /**
     * 取得某年最后一天
     *
     * @return
     */
    public static Date getLastDateOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 12月31日
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        return setDateZero(calendar);
    }

    /**
     * 获取日期的年
     *
     * @param dateTime
     * @return
     */
    public static int getYearByDate(Date dateTime) {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(dateTime);
        return gc.get(Calendar.YEAR);
    }

    /**
     * 获取日期的月
     *
     * @param dateTime
     * @return
     */
    public static int getMonthByDate(Date dateTime) {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(dateTime);
        return gc.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取日期的日
     *
     * @param dateTime
     * @return
     */
    public static int getDayByDate(Date dateTime) {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(dateTime);
        return gc.get(Calendar.DAY_OF_MONTH);
    }

    public static Map<String, String> zoneCn = new HashMap() {
        {
            put("Asia/Shanghai", "中");
            put("America/Los_Angeles", "美");
            put("Etc/GMT-1", "中欧");
        }
    };

    /**
     * 将日期转换为GMT时间
     *
     * @param date
     * @return
     */
    public static Date getGmtTime(Date date) {
        TimeZone tz = TimeZone.getDefault();
        return getZoneDate(date, tz);
    }

    private static Date getZoneDate(Date date, TimeZone tz) {
        Date ret = new Date(date.getTime() - tz.getRawOffset());
        if (tz.inDaylightTime(ret)) {
            Date dstDate = new Date(ret.getTime() - tz.getDSTSavings());
            if (tz.inDaylightTime(dstDate)) {
                ret = dstDate;
            }
        }
        return ret;
    }

    /**
     * @param sdate gmt date String
     * @return 根据默认格式，从字符串取得日期，时区为gmt时区
     * @throws ParseException
     */
    public static Date getDateByGmt(String sdate) throws ParseException {
        return getDateByGmt(sdate, DEFAULT_DATE_FORMAT);
    }

    /**
     * @param sdate
     * @param formatStr
     * @return 指定格式，从字符串取得日期 时区为gmt时区
     * @throws ParseException
     */
    public static Date getDateByGmt(String sdate, String formatStr) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(formatStr);
        dateFormat.setLenient(true);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = dateFormat.parse(sdate);
        return date;
    }

    /**
     * @return 取得+800时间，返回date模式 格式为 yyyyMMddHHmmssSZ
     * @throws ParseException
     */
    public static Date get8ZoneDate(String zoneTimeStr, String formatStr) throws ParseException {
        final DateFormat format = new SimpleDateFormat(formatStr, Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("+800"));// 默认就为+8
        Date date = format.parse(zoneTimeStr);
        return date;

    }

    public static Date get8ZoneDate(Date date) throws ParseException {
        return new DateTime(date).withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone("+800"))).toDate();

    }

    /**
     * @return 取得+800时间，返回date模式 格式为 yyyyMMddHHmmssSZ
     * @throws ParseException
     */
    public static Date get8ZoneDate(String zoneTimeStr) throws ParseException {
        return get8ZoneDate(zoneTimeStr, "yyyyMMddHHmmssSSSZ");
    }

    /**
     * 取得-700时间，返回字符模式
     *
     * @param date+800 时间
     * @return
     */
    public static String getUSZoneStr(Date date) {
        return getUSZoneStr(date, DEFAULT_DATE_FORMAT);

    }

    /**
     * @param date +800 时间
     * @return 取得太平洋时间，返回字符模式
     */
    public static String getUSZoneStr(Date date, String formatStr) {
        final DateFormat format = new SimpleDateFormat(formatStr);
        TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
        format.setTimeZone(tz);
        return format.format(date);

    }

    /**
     * @return 取得当前GMT时间，返回字符模式
     */
    public static String getGmtTimeStr() {
        final Date currentTime = new Date();
        final DateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        return format.format(currentTime);
    }

    /**
     * 取得当前GMT时间，返回字符模式
     *
     * @return
     * @throws ParseException
     */
    public static String getZoneStrByGmtTime(String gmtTime, TimeZone timeZone) throws ParseException {
        final DateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date gmtDate = format.parse(gmtTime);
        format.setTimeZone(timeZone);
        return format.format(gmtDate);
    }

    /**
     * 取得当前GMT时间，返回date模式
     *
     * @return
     * @throws ParseException
     */
    public static Date getZoneDateByGmtTime(String gmtTime) throws ParseException {
        final DateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date gmtDate = format.parse(gmtTime);
        return gmtDate;
    }

    /**
     * @param date +800 时间
     * @return 取得GMT时间，返回字符模式
     */
    public static String getGMTStr(Date date, String formatStr) {
        final DateFormat format = new SimpleDateFormat(formatStr);
        TimeZone tz = TimeZone.getTimeZone("GMT");
        format.setTimeZone(tz);
        return format.format(date);
    }

    /**
     * @param date +800 时间
     * @return 取得UTC时间，返回字符模式
     */
    public static String getUTCStr(Date date, String formatStr) {
        final DateFormat format = new SimpleDateFormat(formatStr);
        TimeZone tz = TimeZone.getTimeZone("UTC");
        format.setTimeZone(tz);
        return format.format(date);
    }

    /**
     * @param date     原日期，+8 时间
     * @param timezone 目标时区
     * @return 得到某个时区的某天的0点转换成中国+8时间
     * @throws ParseException
     */
    public static Date getDate0Timezone(Date date, String timezone) throws ParseException {
        String _date = formatDate(date, DEFAULT_DATE_FORMAT);
        String _datezone = string2TimezoneDefault(_date, String.valueOf(timezone));
        String _dataezone0 = getDate0Time(_datezone);
        String _datezone08 = string2TimezoneDefault(_dataezone0, timezone);
        return getDateFromStr(_datezone08, DEFAULT_DATE_FORMAT);
    }

    public static Date getDate24Timezone(Date date, String timezone) throws ParseException {
        String _date = formatDate(date, DEFAULT_DATE_FORMAT);
        String _datezone = string2TimezoneDefault(_date, String.valueOf(timezone));
        String _dataezone0 = getDate24Time(_datezone);
        String _datezone08 = string2TimezoneDefault(_dataezone0, timezone);
        return getDateFromStr(_datezone08, DEFAULT_DATE_FORMAT);
    }

    /**
     * 将日期时间字符串根据转换为指定时区的日期时间.
     *
     * @param srcDateTime   待转化的日期时间.
     * @param dstTimeZoneId 目标的时区编号.
     * @return 转化后的日期时间.
     * @see #string2Timezone(String, String, String, String)
     */
    public static String string2TimezoneDefault(String srcDateTime, String dstTimeZoneId) {
        return string2Timezone(DEFAULT_DATE_FORMAT, srcDateTime, DEFAULT_DATE_FORMAT, dstTimeZoneId);
    }

    /**
     * 将日期时间字符串根据转换为指定时区的日期时间.
     *
     * @param srcFormater   待转化的日期时间的格式.
     * @param srcDateTime   待转化的日期时间.
     * @param dstFormater   目标的日期时间的格式.
     * @param dstTimeZoneId 目标的时区编号.
     * @return 转化后的日期时间.
     */
    public static String string2Timezone(String srcFormater, String srcDateTime, String dstFormater, String dstTimeZoneId) {
        if (srcFormater == null || "".equals(srcFormater))
            return null;
        if (srcDateTime == null || "".equals(srcDateTime))
            return null;
        if (dstFormater == null || "".equals(dstFormater))
            return null;
        if (dstTimeZoneId == null || "".equals(dstTimeZoneId))
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat(srcFormater);
        try {
            int diffTime = getDiffTimeZoneRawOffset(dstTimeZoneId);
            Date d = sdf.parse(srcDateTime);
            long nowTime = d.getTime();
            long newNowTime = nowTime - diffTime;
            d = new Date(newNowTime);
            return formatDate(d, dstFormater);
        } catch (ParseException e) {
            return null;
        } finally {
            sdf = null;
        }
    }

    /**
     * 获取系统当前默认时区与指定时区的时间差.(单位:毫秒)
     *
     * @param timeZoneId 时区Id
     * @return 系统当前默认时区与指定时区的时间差.(单位 : 毫秒)
     */
    private static int getDiffTimeZoneRawOffset(String timeZoneId) {
        return TimeZone.getDefault().getRawOffset() - TimeZone.getTimeZone(timeZoneId).getRawOffset();
    }

    /**
     * @param dataLong
     * @return 根据时间戳取得相应时间字符串，yyyy-MM-dd HH:mm:ss格式
     */
    public static String formatDate(long dataLong) {
        return formatDate(dataLong, DEFAULT_DATE_FORMAT);
    }

    /**
     * @param dataLong long型的时间戳，以秒为单位，不是以毫秒为单位
     * @param formater 希望格式化成的样子
     * @return 从指定的日期获得指定格式的date字串 String
     */
    public static String formatDate(long dataLong, String formater) {
        DateFormat dateFormat = new SimpleDateFormat(formater);
        Calendar calendar = Calendar.getInstance();
        long tmp = dataLong * 1000;
        calendar.setTimeInMillis(tmp);
        return dateFormat.format(calendar.getTime());
    }

    /**
     * 得到某天的0点
     *
     * @param date
     * @return
     */
    public static Date getDate0Time(Date date) {
        return setTime(date, 0, 0, 0);
    }

    /**
     * 得到某天的0点
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getDate0Time(String date) throws ParseException {
        return formatDate(getDateTime(getDateFromStr(date), 0));
    }

    /**
     * @param date
     * @return 得到某天的24点
     */
    public static Date getDate24Time(Date date) {
        return getDateTime(date, 24);
    }

    /**
     * @param sdate
     * @return 得到某天的24点
     * @throws ParseException
     */
    public static String getDate24Time(String sdate) throws ParseException {
        return formatDate(getDateTime(getDateFromStr(sdate), 24));
    }

    /**
     * @param date
     * @param hh
     * @return 得到某天的某点
     */
    public static Date getDateTime(Date date, int hh) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(year, month, day, hh, 00, 00);
        return cal.getTime();
    }


    /**
     * @param date1 需要比较的时间 不能为空(null),需要正确的日期格式
     * @param date2 被比较的时间 为空(null)则为当前时间
     * @param stype 返回值类型 0为多少天，1为多少个月，2为多少年
     * @return
     */
    public static int compareDate(String date1, String date2, int stype) {
        int n = 0;
        String formatStyle = stype == 1 ? "yyyy-MM" : "yyyy-MM-dd";
        date2 = date2 == null ? DateTimeUtil.getCurrentDate() : date2;
        DateFormat df = new SimpleDateFormat(formatStyle);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(df.parse(date1));
            c2.setTime(df.parse(date2));
        } catch (Exception e3) {
            System.out.println("wrong occured");
        }
        while (!c1.after(c2)) { // 循环对比，直到相等，n 就是所要的结果
            n++;
            if (stype == 1) {
                c1.add(Calendar.MONTH, 1); // 比较月份，月份+1
            } else {
                c1.add(Calendar.DATE, 1); // 比较天数，日期+1
            }
        }
        n = n - 1;
        if (stype == 2) {
            n = (int) n / 365;
        }
        return n;
    }

    /**
     * @param startDate
     * @param endDate
     * @return 获取两个时间间隔，精确到分
     */
    public static long getTwoDay(Date startDate, Date endDate) {
        return (startDate.getTime() - endDate.getTime()) / (1000 * 60);
    }

    /**
     * @return 得到当前日期
     */
    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        SimpleDateFormat simple = new SimpleDateFormat(NORMAL_DATE_FORMAT);
        return simple.format(date);
    }

    /**
     * 获取系统当前默认时区与UTC的时间差.(单位:毫秒)
     *
     * @return 系统当前默认时区与UTC的时间差.(单位 : 毫秒)
     */
    private static int getDefaultTimeZoneRawOffset() {
        return TimeZone.getDefault().getRawOffset();
    }

    /**
     * 获取指定时区与UTC的时间差.(单位:毫秒)
     *
     * @param timeZoneId 时区Id
     * @return 指定时区与UTC的时间差.(单位 : 毫秒)
     */
    private static int getTimeZoneRawOffset(String timeZoneId) {
        return TimeZone.getTimeZone(timeZoneId).getRawOffset();
    }

    /**
     * 获取各时区对应的时间
     *
     * @param date       +8时间
     * @param formatStr  最终显示的时间格式，如：HH:mm
     * @param timeZoneId 时区ID
     * @return
     */
    public static String getZoneStr(Date date, String formatStr, String timeZoneId) {
        final DateFormat format = new SimpleDateFormat(formatStr);
        TimeZone tz = TimeZone.getTimeZone(timeZoneId);
        format.setTimeZone(tz);
        return format.format(date);
    }

    /**
     * 获取所有的时区编号. <br>
     * 排序规则:按照ASCII字符的正序进行排序. <br>
     * 排序时候忽略字符大小写.
     *
     * @return 所有的时区编号(时区编号已经按照字符[忽略大小写]排序).
     */
    public static String[] fecthAllTimeZoneIds() {
        Vector v = new Vector();
        String[] ids = TimeZone.getAvailableIDs();
        for (int i = 0; i < ids.length; i++) {
            v.add(ids[i]);
        }
        java.util.Collections.sort(v, String.CASE_INSENSITIVE_ORDER);
        v.copyInto(ids);
        return ids;
    }

    /**
     * @return 获取传入日期属于周几："周一"、"周二"等等
     * 原理就是先看当前日在一个星期中属于第几天，周日->1->下标0，1，周一->2->下标1...
     * c.get(Calendar.DAY_OF_WEEK)是以周日为第一天的
     */
    public static String getCurrentDateWeeks(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return dayNamesStartSunday[c.get(Calendar.DAY_OF_WEEK) - 1];
    }

    /**
     * @param dateStr 字符串时间 格式为yyyy-MM-dd HH:mm:ss
     * @return 提供的是字符串格式的时间，获取传入日期属于周几："周一"、"周二"等等
     * @throws ParseException
     */
    public static String getCurrentDateWeeks(String dateStr) throws ParseException {
        Date date = DateTimeUtil.getDateFromStr(dateStr);
        return getCurrentDateWeeks(date);
    }

    /**
     * 传入的字符串时间格式为特定的时间格式
     *
     * @param dateStr
     * @param formater
     * @return 获取传入日期属于周几："周一"、"周二"等等
     * @throws ParseException
     */
    public static String getCurrentDateWeeks(String dateStr, String formater) throws ParseException {
        Date date = DateTimeUtil.getDateFromStr(dateStr, formater);
        return getCurrentDateWeeks(date);
    }

    /**
     * @param date 任意日期
     * @return 获取任意日期所在周的所有日期，从周一开始到周日
     */
    public static List<Date> getAllWeekDay(Date date) {
        List<Date> currentWeekDate = new ArrayList();
        Calendar c = Calendar.getInstance();
        if (date != null) {
            c.setTime(date);
        }
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        if (c.getFirstDayOfWeek() == Calendar.SUNDAY) {
            c.add(Calendar.DAY_OF_MONTH, 1);
        }
        c.add(Calendar.DAY_OF_MONTH, -dayOfWeek);
        for (int i = 0; i < dayNamesStartMonday.length; i++) {
            c.add(Calendar.DAY_OF_MONTH, 1);
            currentWeekDate.add(c.getTime());
        }
        return currentWeekDate;
    }

    /**
     * @param date     任意日期
     * @param weekName 周几
     * @return 根据一周的任意时间和周几获取周几对应的时间
     */
    public static Date getDateByWeek(Date date, String weekName) {
        List<Date> allWeekDate = getAllWeekDay(date);
        Map<String, Integer> map = new HashMap();
        for (int i = 0; i < dayNamesStartMonday.length; i++) {
            map.put(dayNamesStartMonday[i], i);
        }
        Integer num = map.get(weekName);
        return allWeekDate.get(num);
    }

    /**
     * @param date 任意日期
     * @return 获取任意指定日期周日时间
     */
    public static Date getWeekDaySunday(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int weekDay = c.get(Calendar.DAY_OF_WEEK);
        // 如果今天是周六，那么就加1天，即8-7，如果是周五的话就加2天，即8-6
        c.add(Calendar.DATE, 8 - weekDay);
        return c.getTime();
    }

    /**
     * @return 传入日期，返回传入日期所在周的周一到周日的日期，格式2018-06-02(周二)...
     */
    public static List<String> getWeekDate(Date date) {
        List<Date> allWeekDate = getAllWeekDay(date);
        List<String> dateStrList = new ArrayList();
        for (Date dt : allWeekDate) {
            String dateStr = DateTimeUtil.formatDate(dt, DateTimeUtil.NORMAL_DATE_FORMAT);
            dateStr += "(" + dayNamesStartMonday[allWeekDate.indexOf(dt)] + ")";
            dateStrList.add(dateStr);
        }
        return dateStrList;
    }

    /**
     * 获取某个时间的下一个月
     *
     * @param time
     * @return
     */
    public static Date getNextMonth(Date time) {
        return getNextMonth(time, 1);
    }

    /**
     * 获取某个时间的下一个月
     *
     * @param time
     * @return
     */
    public static Date getNextMonth(Date time, int n) {
        if (time == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.add(Calendar.MONTH, n);
        return calendar.getTime();
    }

    /**
     * 获取某个时间的下一个月
     *
     * @param time
     * @return
     */
    public static Date getBeforeMonth(Date time, int n) {
        return getNextMonth(time, -n);
    }


    /**
     * 获取后一天日期
     *
     * @param dateTime 某个日期
     * @return java.util.Date
     * @author Jef
     * @date 2019/9/3
     */
    public static Date getNextDay(Date dateTime) {
        return getNextDay(dateTime, 1);
    }

    public static Date getNextDay(Date dateTime, int n) {
        Calendar gc = Calendar.getInstance();
        gc.setTime(dateTime);
        gc.add(Calendar.DAY_OF_MONTH, n);
        return gc.getTime();
    }


    /**
     * 根据日期取得当前时间字符串
     *
     * @param formatStr
     */
    public static String formatNow(String formatStr) {
        return formatDate(getNowDateByServer(), formatStr);
    }

    /**
     * 格式化当前时间
     *
     * @return
     */
    public static String formatNow() {
        return formatNow(NORMAL_DATE_FORMAT);
    }

    /**
     * 获取服务器时间
     *
     * @return
     */
    public static Date getNowDateByServer() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * 校验字符串是否符合日期格式，常用于导入
     *
     * @param dateStr
     * @return
     */
    public static Date getDateFromStrUnknownFormat(String dateStr) {
        if (dateStr == null || "".equals(dateStr)) {
            return null;
        }
        String[] strs;
        String splitStr = "";
        if (dateStr.contains("-")) {
            splitStr = "-";
        } else if (dateStr.contains("/")) {
            splitStr = "/";
        }
        strs = dateStr.split(splitStr);
        if (strs.length < 3) {
            return null;
        }
        Integer year = Integer.valueOf(strs[0]), month = Integer.valueOf(strs[1]), day = Integer.valueOf(strs[2]);
        if (year > 3000) {
            return null;
        }
        if (month > 12) {
            return null;
        }
        int[] dayArray = DAY_ARRAY;
        if (isLeapYear(month)) {
            dayArray = DAY_ARRAY_ONE;
        }
        if (day > dayArray[month - 1]) {
            return null;
        }
        Date date;
        try {
            date = getDateFromStr(dateStr);
            return date;
        } catch (ParseException e) {

        }
        try {
            date = getDateFromStr(dateStr, NORMAL_DATE_FORMATV2);
            return date;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 校验某种日期格式的字符串是否正确
     *
     * @param dateStr
     * @param formatStr
     * @return
     */
    public static boolean isRightDateStr(String dateStr, String formatStr) {
        if (dateStr == null || "".equals(dateStr) || formatStr == null || "".equals(formatStr)) {
            return false;
        }
        try {
            getDateFromStr(dateStr, formatStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * 年份是否闰年
     *
     * @param year
     * @return
     */
    public static boolean isLeapYear(int year) {
        if (year % 400 == 0) {
            return true;
        }
        if (year % 4 == 0) {
            return year % 100 != 0;
        }
        return false;
    }

    /**
     * 获取time对应指定日期所在周的同一天，例如，time是周五，那么返回值就是designatedDate对应周的周五
     *
     * @param time
     * @param designatedDate
     * @return
     */
    public static Date getDesignatedWeekFromDate(Date time, Date designatedDate) {
        if (time == null || designatedDate == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        Date startDate = getFirstDayOfWeek(designatedDate, 2);
        if (time.compareTo(startDate) > 0) {
            return calendar.getTime();
        }
        while (calendar.getTime().compareTo(startDate) < 0) {
            calendar.add(Calendar.DATE, 7);
        }
        return calendar.getTime();
    }

    /**
     * 日期转换格式
     *
     * @param date
     * @param format
     * @return
     */
    public static Date changeFormatDate(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String dateStr = simpleDateFormat.format(date);
        try {
            return simpleDateFormat.parse(dateStr);
        } catch (Exception e) {

        }
        return date;
    }

    /**
     * 获取endDate与startDate之间的天数
     * "2018-08-08 00:00:01" "2018-08-08 00:00:00"返回1
     * "2018-08-08 00:00:00" "2018-08-08 00:00:00"返回1
     *
     * @param endDate   结束时间
     * @param startDate 开始时间
     * @return endDate与startDate之间的天数，至少为1
     * @author Jef
     * @date 20180815
     */
    public static int differentDays(Date endDate, Date startDate) {
        if (endDate.compareTo(startDate) < 0) {
            return differentDays(startDate, endDate);
        }
        long diff = endDate.getTime() - startDate.getTime();
        double days = diff / DAY;
        int day = (int) Math.ceil(days);
        if (isZeroHMS(endDate) && isZeroHMS(startDate)) {
            day += 1;
        }
        return day;
    }


    /**
     * endDate比startDate多的天数
     *
     * @param endDate
     * @param startDate
     * @return
     */
    public static int differentDays2(Date endDate, Date startDate) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(startDate);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(endDate);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);
        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2) {
            // 不同年
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (isLeapYear(i)) {
                    timeDistance += 366;
                } else {
                    timeDistance += 365;
                }
            }
            return timeDistance + (day2 - day1);
        } else {
            // 同一年
            return day2 - day1;
        }
    }

    /**
     * 判断传入的时间是否是零点零分零秒"****-**-** 00:00:00"
     *
     * @param date 日期
     */
    public static boolean isZeroHMS(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        return hour == 0 && minute == 0 && second == 0;
    }

    /* 获取固定时间后x年时间
     * @param date 日期
     * @param years 后year年，正整数
     * @return 日期
     */
    public static Date getNextYear(Date date, Integer years) {
        return getBeforeOrNextYear(date, years);
    }

    /**
     * 获取某个时间前years年时间
     *
     * @param date  日期
     * @param years 前year年，正整数
     * @return 前years年时间
     */
    public static Date getBeforeYear(Date date, Integer years) {
        return getBeforeOrNextYear(date, -years);
    }

    /* 获取某个时间后years年时间
     * @param date 日期
     * @param years 后year年，为负数时表示前-years年
     * @return 后years年时间
     */
    private static Date getBeforeOrNextYear(Date date, Integer years) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, years);
        date = calendar.getTime();
        return date;
    }

    private static String[] parsePatterns = {"yyyy-MM-dd", DEFAULT_DATE_FORMAT, "yyyy-MM-dd HH:mm", "yyyy-MM-dd", DEFAULT_DATE_FORMAT, "yyyy-MM-dd HH:mm"};

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd）
     */
    public static String getDate() {
        return getDateV2("yyyy-MM-dd");
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String getDateV2(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String formatDate(Date date, Object... pattern) {
        String formatDate = null;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    /**
     * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, DEFAULT_DATE_FORMAT);
    }

    /**
     * 得到当前时间字符串 格式（HH:mm:ss）
     */
    public static String getTime() {
        return formatDate(new Date(), "HH:mm:ss");
    }

    /**
     * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String getDateTime() {
        return formatDate(new Date(), DEFAULT_DATE_FORMAT);
    }

    /**
     * 得到当前年份字符串 格式（yyyy）
     */
    public static String getYearStr() {
        return getYearStr(new Date());
    }

    /**
     * 得到年份字符串 格式（yyyy）
     */
    public static String getYearStr(Date date) {
        return formatDate(date, YEAR_FORMAT);
    }

    /**
     * 得到某天所属月份 格式（MM）
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 得到上年年份字符串 格式（yyyy）
     */
    public static String getLastYear() {
        Date years = addYears(new Date(), -1);
        return formatDate(years, YEAR_FORMAT);
    }

    /**
     * 得到当前月份字符串 格式（MM）
     */
    public static String getMonthStr() {
        return getMonthStr(new Date());
    }

    /**
     * 得到某天所属月份 格式（MM）
     */
    public static String getMonthStr(Date date) {
        return formatDate(date, MONTH_FORMAT);
    }

    /**
     * 得到某天所属月份 格式（MM）
     */
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 得到当前月份字符串 格式（MM）
     */
    public static String getLastMonth() {
        Date months = addMonths(new Date(), -1);
        return formatDate(months, MONTH_FORMAT);
    }

    /**
     * 得到当天字符串 格式（dd）
     */
    public static String getDayStr() {
        return getDayStr(new Date());
    }

    /**
     * 得到某个日期所属天字符串 格式（dd）
     */
    public static String getDayStr(Date date) {
        return formatDate(date, DAY_FORMAT);
    }

    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 得到当前星期字符串 格式（E）星期几
     */
    public static String getWeek() {
        return formatDate(new Date(), "E");
    }

    /**
     * 日期型字符串转化为日期 格式
     * { "yyyy-MM-dd", DEFAULT_DATE_FORMAT, "yyyy-MM-dd HH:mm",
     * "yyyy-MM-dd", DEFAULT_DATE_FORMAT, "yyyy-MM-dd HH:mm" }
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取过去的天数
     * <p>
     * param date
     * return
     */
    public static long pastDays(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (24 * 60 * 60 * 1000);
    }

    /**
     * 获取过去的小时
     * <p>
     * param date
     * return
     */
    public static long pastHour(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (60 * 60 * 1000);
    }

    /**
     * 获取过去的分钟
     * <p>
     * param date
     * return
     */
    public static long pastMinutes(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (60 * 1000);
    }

    /**
     * 转换为时间（天,时:分:秒.毫秒）
     * <p>
     * param timeMillis
     * return
     */
    public static String formatDateTime(long timeMillis) {
        long day = timeMillis / (24 * 60 * 60 * 1000);
        long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
        long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
        return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
    }

    /**
     * 获取两个日期之间的天数
     * <p>
     * param before
     * param after
     * return
     */
    public static double getDistanceOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
    }

    /**
     * 得到某年某周的第一天
     * <p>
     * param year
     * param week
     * return
     */
    public static Date getFirstDayOfWeek(int year, int week) {
        week = week - 1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DATE, 1);

        Calendar cal = (Calendar) calendar.clone();
        cal.add(Calendar.DATE, week * 7);

        return getFirstDayOfWeek(cal.getTime());
    }

    /**
     * 得到某年某周的最后一天
     * <p>
     * param year
     * param week
     * return
     */
    public static Date getLastDayOfWeek(int year, int week) {
        week = week - 1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DATE, 1);
        Calendar cal = (Calendar) calendar.clone();
        cal.add(Calendar.DATE, week * 7);

        return getLastDayOfWeek(cal.getTime());
    }

    /**
     * 取得当前日期所在周的第一天
     * <p>
     * param date
     * return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek()); // Sunday
        return calendar.getTime();
    }

    /**
     * 取得当前日期所在周的最后一天
     * <p>
     * param date
     * return
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6); // Saturday
        return calendar.getTime();
    }

    /**
     * 取得当前日期所在周的前一周最后一天
     * <p>
     * param date
     * return
     */
    public static Date getLastDayOfLastWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getLastDayOfWeek(calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_YEAR) - 1);
    }

    /**
     * 返回指定日期的月的第一天
     * <p>
     * param date
     * return
     */
    public static Date getFirstDayOfMonthV2(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        return calendar.getTime();
    }

    /**
     * Description:获取指定日期年的第一天
     */
    public static Date getFirstDayOfYearV2(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), Calendar.JANUARY, 1);
        return calendar.getTime();
    }

    /**
     * Description:获取指定日期年的最后一天
     */
    public static Date getLastDayOfYearV2(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), Calendar.DECEMBER, 31);
        return calendar.getTime();
    }

    /**
     * Description:获取指定日期年的第一天
     */
    public static Date getFirstDayOfLastYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR) - 1, Calendar.JANUARY, 1);
        return calendar.getTime();
    }

    /**
     * Description:获取指定日期年的最后一天
     */
    public static Date getLastDayOfLastYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR) - 1, Calendar.DECEMBER, 31);
        return calendar.getTime();
    }

    /**
     * 返回指定年月的月的第一天
     * <p>
     * param year
     * param month
     * return
     */
    public static Date getFirstDateOfMonth(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH);
        }
        calendar.set(year, month - 1, 1);
        return getFirstDateOfMonth(calendar.getTime());
    }

    /**
     * 返回指定日期的月的最后一天
     * <p>
     * param date
     * return
     */
    public static Date getLastDateOfMonthV2(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 返回指定年月的月的最后一天
     * <p>
     * param year
     * param month
     * return
     */
    public static Date getLastTimeOfMonth(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH);
        }
        calendar.set(year, month - 1, 1);
        return getLastTimeOfMonth(calendar.getTime());
    }

    /**
     * Description:返回指定日期的上个月的第一天
     */
    public static Date getFirstDayOfLastMonth(Date date) {
        Date addMonths = addMonths(date, -1);
        return setDays(addMonths, 1);
    }

    /**
     * 返回指定日期的上个月的最后一天
     * <p>
     * param date
     * return
     */
    public static Date getLastDayOfLastMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) - 1, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 返回指定日期的季的第一天
     * <p>
     * param date
     * return
     */
    public static Date getFirstDayOfQuarter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getFirstDayOfQuarter(calendar.get(Calendar.YEAR), getQuarterOfYear(date));
    }

    /**
     * 返回指定年季的季的第一天
     * <p>
     * param year
     * param quarter
     * return
     */
    public static Date getFirstDayOfQuarter(Integer year, Integer quarter) {
        Calendar calendar = Calendar.getInstance();
        Integer month;
        if (quarter == 1) {
            month = 1 - 1;
        } else if (quarter == 2) {
            month = 4 - 1;
        } else if (quarter == 3) {
            month = 7 - 1;
        } else if (quarter == 4) {
            month = 10 - 1;
        } else {
            month = calendar.get(Calendar.MONTH);
        }
        return getFirstDateOfMonth(year, month);
    }

    /**
     * 返回当前日期的季的最后一天
     * <p>
     * param date
     * return
     */
    public static Date getLastDayOfQuarter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getLastDayOfQuarter(calendar.get(Calendar.YEAR), getQuarterOfYear(date));
    }

    /**
     * 返回指定年季的季的最后一天
     * <p>
     * param year
     * param quarter
     * return
     */
    public static Date getLastDayOfQuarter(Integer year, Integer quarter) {
        Calendar calendar = Calendar.getInstance();
        Integer month;
        if (quarter == 1) {
            month = 3 - 1;
        } else if (quarter == 2) {
            month = 6 - 1;
        } else if (quarter == 3) {
            month = 9 - 1;
        } else if (quarter == 4) {
            month = 12 - 1;
        } else {
            month = calendar.get(Calendar.MONTH);
        }
        return getLastTimeOfMonth(year, month);
    }

    /**
     * 返回当前日期的季的第一天
     * <p>
     * param date
     * return
     */
    public static Date getFirstDayOfLastQuarter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getFirstDayOfLastQuarter(calendar.get(Calendar.YEAR), getQuarterOfYear(date));
    }

    /**
     * 返回指定日期的上一季的最后一天
     * <p>
     * param date
     * return
     */
    public static Date getLastDayOfLastQuarter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getLastDayOfLastQuarter(calendar.get(Calendar.YEAR), getQuarterOfYear(date));
    }

    /**
     * 返回指定年季的上一季的最后一天
     * <p>
     * param year
     * param quarter
     * return
     */
    public static Date getLastDayOfLastQuarter(Integer year, Integer quarter) {
        Calendar calendar = Calendar.getInstance();
        Integer month = new Integer(0);
        if (quarter == 1) {
            month = 12 - 1;
        } else if (quarter == 2) {
            month = 3 - 1;
        } else if (quarter == 3) {
            month = 6 - 1;
        } else if (quarter == 4) {
            month = 9 - 1;
        } else {
            month = calendar.get(Calendar.MONTH);
        }
        return getLastTimeOfMonth(year, month);
    }

    /**
     * 返回指定年季的上一季的第一天
     * <p>
     * param year
     * param quarter
     * return
     */
    public static Date getFirstDayOfLastQuarter(Integer year, Integer quarter) {
        Calendar calendar = Calendar.getInstance();
        Integer month = new Integer(0);
        if (quarter == 1) {
            month = 12 - 3;
        } else if (quarter == 2) {
            month = 3 - 3;
        } else if (quarter == 3) {
            month = 6 - 3;
        } else if (quarter == 4) {
            month = 9 - 3;
        } else {
            month = calendar.get(Calendar.MONTH);
        }
        return getFirstDateOfMonth(year, month);
    }

    /**
     * 返回指定日期的季度
     * <p>
     * param date
     * return
     */
    public static int getQuarterOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) / 3 + 1;
    }

    /**
     * Description:获取当前上一季度
     */
    public static int getLastQuarterOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) / 3;
    }


    public static Date getDateStart(Date date) throws ParseException {
        if (date == null) {
            return null;
        }
        return getDate0Time(date);
    }

    public static Date getDateEnd(Date date) throws ParseException {
        if (date == null) {
            return null;
        }
        return removeTime(date);
    }

    /**
     * 获取指定日期的星期
     */
    public static String getDateWeek(Date date) {
        return formatDate(date, "E");
    }


    /**
     * 获取某年某周的星期一
     * <p>
     * param y
     * param w
     * return
     */
    public static Date getBeginDate(Integer y, Integer w) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(y, 0, 1, 0, 0, 0);
        int week = calendar.get(calendar.DAY_OF_WEEK) - 1;
        int days = w * 7 - (8 - week);
        calendar.add(Calendar.DATE, days - 1);
        Date beginDate = calendar.getTime();
        return beginDate;
    }

    /**
     * 获取某年某周的星期天
     * <p>
     * param y
     * param w
     * return
     */
    public static Date getEndDate(Integer y, Integer w) {
        Date beginDate = getBeginDate(y, w);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginDate);
        calendar.add(Calendar.DATE, 6);
        Date endDate = calendar.getTime();
        return endDate;
    }

    /**
     * Description:获取进行比较的日期整型数字
     */
    public static int getDateInt(String date) {
        return Integer.parseInt(DateFormatUtils.format(DateTimeUtil.parseDate(date), "yyyyMMdd"));
    }

    /**
     * Description:获取进行比较的日期整型数字
     */
    public static int getDateInt(Date date) {
        return Integer.parseInt(DateFormatUtils.format(date, "yyyyMMdd"));
    }

    /**
     * 日期转换为 webservice UTC日期
     *
     * @param date 要转换的 util 日期
     */
    public static XMLGregorianCalendar dateToWsXML(Date date) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        XMLGregorianCalendar xmlGregorianCalendar = null;
        try {
            xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlGregorianCalendar;
    }

    /**
     * webservice UTC 日期转换为 util 日期
     *
     * @param gc webservice 封装的 XMLGregorianCalendar 日期类型
     */
    public static Date wsXMLToDate(XMLGregorianCalendar gc) {
        GregorianCalendar gregorianCalendar = gc.toGregorianCalendar();
        return gregorianCalendar.getTime();
    }

    /**
     * 将格式为 Thu Jun 15 16:29:24 CST 2017 的字符串格式化为业务中需要的格式日期字符串
     *
     * @param localeEnglishDate 待转换的格式为 Thu Jun 15 16:29:24 CST 2017 的日期字符串
     * @param pattern           业务中需要的日期格式
     */
    public static String localeEnglishDateToFormatDate(String localeEnglishDate, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        Date parse = null;
        try {
            parse = simpleDateFormat.parse(localeEnglishDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return DateFormatUtils.format(parse, pattern);
    }

    public static void main(String[] args) {
        try {
            Date nowDate = new Date();
            Date lastDate = addYears(nowDate, -1);

            System.out.println("01.已 yyyy-MM-dd 格式获取日期字符串：" + getDate());
            System.out.println("02.已特定日期格式获取日期字符串：" + getDateFromStr("2019-01-01"));
            System.out.println("03.已 yyyy-MM-dd HH:mm:ss 日期格式获取特定日期字符串：" + formatDateTime(nowDate));
            System.out.println("04.已特定日期格式获取特定日期字符串：" + formatDate(nowDate, "yyyy-MM-dd-HH:mm:ss"));

            System.out.println("05.获取当前时间小时分秒字符串：" + getTime());
            System.out.println("06.获取当前时间全字符串：" + getDateTime());
            System.out.println("07.获取当前年字符串：" + getYearStr());
            System.out.println("08.获取去年字符串：" + getLastYear());
            System.out.println("09.获取当前月字符串：" + getMonthStr());
            System.out.println("10.获取上一月字符串：" + getLastMonth());
            System.out.println("11.获取当前日字符串：" + getDayStr());
            System.out.println("12.获取当前星期字符串：" + getWeek());
            System.out.println("13.将日期型字符串转化为日期 格式：" + parseDate(DEFAULT_DATE_FORMAT));
            System.out.println("14.入参与当前时间对比获取过去的天数：" + pastDays(lastDate));
            System.out.println("15.入参与当前时间对比获取过去的小时：" + pastHour(lastDate));
            System.out.println("16.入参与当前时间对比获取过去的分钟：" + pastMinutes(lastDate));
            System.out.println("17.长整型转换为时间（天,时:分:秒.毫秒）：" + formatDateTime(222222222));
            System.out.println("18.获取两个日期之间相差的天数：" + getDistanceOfTwoDate(lastDate, new Date()));

            System.out.println("19.获取指定日期年的第一天：" + formatDate(getFirstDateOfYear(nowDate)));
            System.out.println("20.获取指定日期年的最后一天：" + formatDate(getLastTimeOfYear(nowDate)));
            System.out.println("22.获取指定日期上一年的第一天：" + formatDate(getFirstDayOfLastYear(nowDate)));
            System.out.println("22.获取指定日期上一年的最后一天：" + formatDate(getLastDayOfLastYear(nowDate)));

            System.out.println("23.得到某年某周的第一天：" + formatDate(getFirstDayOfWeek(2017, 7)));
            System.out.println("24.得到某年某周的最后一天：" + formatDate(getLastDayOfWeek(2017, 5)));
            System.out.println("25.得到指定日期所在周的第一天：" + formatDate(getFirstDayOfWeek(nowDate)));
            System.out.println("26.获取指定日期所在周的最后一天：" + formatDate(getLastDayOfWeek(nowDate)));
            System.out.println("27.获取指定日期上周最后一天：" + formatDate(getLastDayOfLastWeek(nowDate)));

            System.out.println("28.获取指定日期月的第一天：" + formatDate(getFirstDateOfMonth(nowDate)));
            System.out.println("29.获取指定日期月的最后一天：" + formatDate(getLastTimeOfMonth(nowDate)));
            System.out.println("30.获取指定日期上月第一天：" + formatDate(getFirstDayOfLastMonth(nowDate)));
            System.out.println("31.获取指定日期上月最后一天：" + formatDate(getLastDayOfLastMonth(nowDate)));
            System.out.println("32.获取某年某月的第一天：" + formatDate(getFirstDateOfMonth(2016, 11)));
            System.out.println("33.获取某年某月的最后一天：" + formatDate(getLastTimeOfMonth(2016, 11)));

            System.out.println("34.获取指定日期季度第一天：" + formatDate(getFirstDayOfQuarter(nowDate)));
            System.out.println("35.获取指定日期季度最后一天：" + formatDate(getLastDayOfQuarter(nowDate)));
            System.out.println("36.获取某年某月对应季度第一天：" + formatDate(getFirstDayOfQuarter(2016, 1)));
            System.out.println("37.获取某年某月对应季度最后一天：" + formatDate(getLastDayOfQuarter(2016, 2)));
            System.out.println("38.获取指定季度上一季度第一天：" + formatDate(getFirstDayOfLastQuarter(nowDate)));
            System.out.println("39.获取指定季度上一季度最后一天：" + formatDate(getLastDayOfLastQuarter(nowDate)));
            System.out.println("40.获取某年某月对应上一季度第一天：" + formatDate(getFirstDayOfLastQuarter(2016, 3)));
            System.out.println("41.获取某年某月对应上一季度最后一天：" + formatDate(getLastDayOfLastQuarter(2016, 4)));

            System.out.println("42.获取指定日期季度：" + getQuarterOfYear(nowDate));
            System.out.println("43.获取指定日期上一季度：" + getLastQuarterOfYear(nowDate));
            System.out.println("44.获取指定日期开始：" + formatDate(getDateStart(nowDate), DEFAULT_DATE_FORMAT));
            System.out.println("45.获取指定日期结束：" + formatDate(getDateEnd(nowDate), DEFAULT_DATE_FORMAT));
            System.out.println("46.获取指定日期星期：" + getDateWeek(nowDate));
            System.out.println("47.获取某年某周的第一天：" + formatDate(getBeginDate(2017, 2)));
            System.out.println("48.获取某年某周的星期天：" + formatDate(getEndDate(2016, 1)));
            System.out.println("49.获取指定日期字符串的整数：" + getDateInt("2016-07-14"));
            System.out.println("50.获取指定日期的整数：" + getDateInt(nowDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取两个时间的相差的天数, 同一天算0天
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long getBetweenSeconds(Date startDate, Date endDate) {
        return (startDate.getTime() - endDate.getTime()) / SECOND;
    }

    /**
     * 获取两个时间的相差的天数, 同一天算0天
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long getBetweenMinutes(Date startDate, Date endDate) {
        return (startDate.getTime() - endDate.getTime()) / MINUTE;
    }

    /**
     * 获取两个时间的相差的天数, 同一天算0天
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getBetweenDays(Date startDate, Date endDate) {
        return Math.abs(new Long((getDate0Time(startDate).getTime() - getDate0Time(endDate).getTime()) / DAY).intValue());
    }

    /**
     * 获取两个时间的相差的年数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getBetweenYears(Date startDate, Date endDate) {
        return Math.abs(new Long((getDate0Time(startDate).getTime() - getDate0Time(endDate).getTime()) / YEAR).intValue());
    }

    /**
     * 租赁计算两个日期之间相隔的月份
     * 同一天算1个月，"2018-01-01"到"2018-11-30"为11个月，到"2018-12-01"为12个月
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return java.lang.Integer
     * @author Jef
     * @date 2019/5/16
     */
    public static Integer getMonthBetween(Date startDate, Date endDate) {
        Integer month = 0;
        while (startDate.before(endDate)) {
            month++;
            startDate = addMonths(startDate, 1);
        }
        if (startDate.compareTo(endDate) == 0) {
            month++;
        }
        return month;
    }

    /**
     * 获取endDate与startDate之间的天数
     * "2018-08-08 00:00:01" "2018-08-08 00:00:00"返回1
     * "2018-08-08 00:00:00" "2018-08-08 00:00:00"返回1
     *
     * @param endDate   结束时间
     * @param startDate 开始时间
     * @return endDate与startDate之间的天数，至少为1
     * @author Jef
     * @date 20180815
     */
    public static int getDayBetween(Date endDate, Date startDate) {
        long diff = endDate.getTime() - startDate.getTime();
        double days = diff / 86400000;
        int day = (int) Math.ceil(days);
        if (isZeroHMS(endDate) && isZeroHMS(startDate)) {
            day += 1;
        }
        return day;
    }

    /**
     * 租赁计算两个日期之间相隔的年份
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return int
     * @author Jef
     * @date 2019/5/16
     */
    public static Integer getYearBetween(Date startDate, Date endDate) {
        return Math.abs(new Long((removeTime(startDate).getTime() - removeTime(endDate).getTime()) / 31536000000L).intValue());
    }

    /**
     * 获取当前日期属于循环年中的第几年
     * 同一天算第一年，"2018-01-01"到"2018-12-31"为第1年，到"2019-01-01"为第二年
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return java.lang.Integer
     * @author Jef
     * @date 2019/5/16
     */
    public static Integer getDateYear(Date startDate, Date endDate) {
        Integer month = getMonthBetween(startDate, endDate);
        return (int) Math.ceil(month / 12.0);
    }

    /**
     * 获取某个日期对应月的天数
     *
     * @param date
     * @return java.lang.Integer
     * @author Jef
     * @date 2019/9/3
     */
    public static Integer getDaysOfMonth(Date date) {
        String dateStr = formatDate(date);
        return getDaysOfMonth(dateStr);
    }

    /**
     * 获取某个日期对应月的天数
     *
     * @param date
     * @return java.lang.Integer
     * @author Jef
     * @date 2019/9/3
     */
    public static Integer getDaysOfMonthV2(Date date) {
        Date lastDateOfMonth = getLastDateOfMonth(date);
        return Integer.valueOf(formatDate(lastDateOfMonth, DateTimeUtil.MONTH_FORMAT));
    }

    /**
     * 获取当月的天数
     *
     * @param monthStr
     * @return
     * @author Jef
     * @date 20190414
     */
    public static Integer getDaysOfMonth(String monthStr) {
        String year = monthStr.split("-")[0],
                month = monthStr.split("-")[1];
        if ("02".equals(month)) {
            Integer yearValue = Integer.parseInt(year);
            if (isLeapYear(yearValue)) {
                return 29;
            } else {
                return 28;
            }
        } else if ("01".equals(month) || "03".equals(month) || "05".equals(month) || "07".equals(month) || "08".equals(month)
                || "10".equals(month) || "12".equals(month)) {
            return 31;
        } else {
            return 30;
        }
    }

    /**
     * 根据开始日期和周期月份数获取周期结束日期
     *
     * @param beginDate 开始日期
     * @param month     月份数
     * @return java.util.Date
     * @author Jef
     * @date 2019/9/20
     */
    public static Date getPeriodEndDate(Date beginDate, int month) {
        Calendar periodEndDateCalendar = Calendar.getInstance();
        periodEndDateCalendar.setTime(beginDate);
        int dayOfMonth = periodEndDateCalendar.get(Calendar.DAY_OF_MONTH) - 1;
        Date addMonthDate = addMonths(beginDate, month);
        periodEndDateCalendar.setTime(addMonthDate);
        periodEndDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return periodEndDateCalendar.getTime();
    }
}
