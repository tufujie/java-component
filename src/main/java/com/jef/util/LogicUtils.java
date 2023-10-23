package com.jef.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * 逻辑对应非空判断工具
 * 除了jdk外不使用其他的jar
 * @author Jef
 * @create 2018/6/14 13:45
 */
public class LogicUtils {
    private static final int ZERO = 0;

    private static final String EMPTY_STRING = "";

    /**
     * 判断集合非空
     * @param collection
     * @return 空 true 非空 false
     */
    public static boolean isNullOrEmpty(Collection<?> collection) {
        return (null == collection || ZERO == collection.size());
    }

    /**
     * 判断集合非空
     * @param collection
     * @return 空 false 非空 true
     */
    public static boolean isNotNullAndEmpty(Collection<?> collection) {
        return !isNullOrEmpty(collection);
    }

    /**
     * 判断Map非空
     * @param map
     * @return 空 true 非空 false
     */
    public static boolean isNullOrEmpty(Map<?, ?> map) {
        return (null == map || ZERO == map.size());
    }

    /**
     * 判断Map非空
     * @param map
     * @return 空 false 非空 true
     */
    public static boolean isNotNullAndEmpty(Map<?, ?> map) {
        return !isNullOrEmpty(map);
    }

    /**
     * 判断数组非空
     * @param objects
     * @return 空 true 非空 false
     */
    public static boolean isNullOrEmpty(Object[] objects) {
        return (null == objects || ZERO == objects.length);
    }

    /**
     * 判断数据非空
     * @param objects
     * @return 空 false 非空 true
     */
    public static boolean isNotNullAndEmpty(Object[] objects) {
        return !isNullOrEmpty(objects);
    }

    /**
     * 判断对象非空
     * @param objects
     * @return 空 true 非空 false
     */
    public static boolean isNull(final Object ... objects) {
        for(Object obj:objects){
            if (null == obj) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断对象非空
     * @param objects
     * @return 空 false 非空 true
     */
    public static boolean isNotNull(final Object ... objects) {
        return !isNull(objects);
    }

    /**
     * 判断字符串非空
     * @param subjects
     * @return 空 true 非空 false
     */
    public static boolean isNullOrEmpty(final String ... subjects) {
        for(String str:subjects){
            if (null == str || EMPTY_STRING.equals(str.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符串非空
     * @param subjects
     * @return 空 false 非空 true
     */
    public static boolean isNotNullAndEmpty(final String ... subjects) {
        return !isNullOrEmpty(subjects);
    }

    /**
     * 根据传入的 日期格式化要求 判断传入的字符串是否符合
     * @param dateStr 例如 2016-06-16
     * @param format 例如 yyyy-MM-dd
     * @return 符合true 不符合false
     */
    public static boolean isValidDate(String dateStr, String format) {
        boolean flag = false;
        if(isNotNullAndEmpty(dateStr,format)){
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            try {
                // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
                sdf.setLenient(false);
                sdf.parse(dateStr);
                flag=true;
            } catch (Exception e) {
                // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            }
        }
        return flag;
    }
    /**
     * 根据传入的金额判断是否为空或为零
     * @param arAmount
     * @return
     */
    public static boolean isNotNullAndLessZero(BigDecimal arAmount) {
        return !isNullOrLessZero(arAmount);
    }

    /**
     * 根据传入的金额判断是否为空或为零
     * @param arAmount
     * @return
     */
    public static boolean isNullOrLessZero(BigDecimal arAmount) {
        return (arAmount==null || arAmount.compareTo(BigDecimal.ZERO) <= 0);
    }

    /**
     * 获取对象内容是否变化
     * 支持基本类型和包装类型
     * @author Jef
     * @date 2019/12/9
     * @param beforeValueObj 变化之前内容
     * @param afterValueObj 变化之后内容
     * @return boolean
     */
    public static boolean isChange(Object beforeValueObj, Object afterValueObj) {
        if (beforeValueObj == null && afterValueObj == null) {
            return false;
        } else if (beforeValueObj != null && afterValueObj == null) {
            return true;
        } else if (beforeValueObj == null) {
            return true;
        }
        // 两个都有值
        String beforeValue, afterValue;
        if (beforeValueObj instanceof BigDecimal) {
            BigDecimal beforeValueDecimal = NumberUtils.toBigDecimal(beforeValueObj, 4), afterValueDecimal = NumberUtils.toBigDecimal(afterValueObj, 4);
            beforeValue = String.valueOf(beforeValueDecimal);
            afterValue = String.valueOf(afterValueDecimal);
        } else if (beforeValueObj instanceof Date) {
            Date beforeValueDate = (Date) beforeValueObj;
            if (afterValueObj instanceof Date) {
                Date afterValueDate = (Date) afterValueObj;
                return beforeValueDate.compareTo(afterValueDate) != 0;
            } else {
                return true;
            }
        } else {
            beforeValue = StringUtils.getStringByDefault(String.valueOf(beforeValueObj));
            afterValue = StringUtils.getStringByDefault(String.valueOf(afterValueObj));
        }
        return !beforeValue.equals(afterValue);
    }

}
