package com.jef.util;

import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 金额、数字类工具类
 * @author Jef
 * @create 2018/6/8 12:03
 */
public class NumberUtils {
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");
    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private static final int DEF_DIV_SCALE = 4;
    /**
     * 手机号的正则表达式
     */
    private static Pattern PHONE_PATTERN = Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]{9}$");
    // double正则
    private static Pattern DOUBLE_PATTERN = Pattern.compile("^[-\\+]?[.\\d]*$");


    /**
     * BigDecimal的加法求和封装
     * @param bs BigDecimal集合
     * @param newScale 精确度，保留到几位小数
     * @param roundingMode 应用的舍入模式
     * @return returnBigdecimal 和
     */
    public static BigDecimal safeAdd(List<BigDecimal> bs, int newScale, int roundingMode) {
        BigDecimal returnBigdecimal = BigDecimal.ZERO;
        if (null != bs && !bs.isEmpty()) {
            for (BigDecimal b : bs) {
                returnBigdecimal = returnBigdecimal.add(null == b ? BigDecimal.ZERO : b);
            }
        }
        if (returnBigdecimal.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return returnBigdecimal.setScale(newScale, roundingMode);
    }

    /**
     * Integer加法求和封装
     * @param is Integer集合
     * @return
     */
    public static Integer safeAdd(List<Integer> is) {
        Integer returnInteger = 0;
        if (null != is && !is.isEmpty()) {
            for (Integer i : is) {
                returnInteger += null == i ? 0 : i;
            }
        }
        return returnInteger;
    }

    /**
     * 将BigDecimal转化为百分比
     * @param bigDecimal BigDecimal的数值
     * @param maxFractionDigit 最多小数位数
     */
    public static String bigDecimalToPercent(BigDecimal bigDecimal, Integer maxFractionDigit) {
        if (bigDecimal == null) {
            return "";
        }
        NumberFormat percent = NumberFormat.getPercentInstance();
        if (maxFractionDigit == null) {
            maxFractionDigit = 2;
        }
        percent.setMaximumFractionDigits(maxFractionDigit);
        return percent.format(bigDecimal.doubleValue());
    }

    /**
     * 获取数值数据
     * @param str
     * @param defVal 默认值，单字符串不是数值的时候使用，保证至少有一个默认值返回
     * @return
     */
    public static BigDecimal getNumber(String str, int defVal){
        if (isNumber(str)) {
            return toBigDecimal(str);
        } else {
            return new BigDecimal(defVal);
        }
    }

    /**
     * 获取数值数据，默认值为0
     * @param str
     * @return
     */
    public static BigDecimal getNumber(String str){
        return getNumber(str, 0);
    }

    /**
     * 判断字符串是否为数字
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        char[] chars = str.toCharArray();
        int sz = chars.length;
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false;
        boolean foundDigit = false;
        int start = chars[0] == '-' ? 1 : 0;
        if ((sz > start + 1) && (chars[start] == '0') && (chars[(start + 1)] == 'x')) {
            int i = start + 2;
            if (i == sz) {
                return false;
            }
            for (; i < chars.length; i++) {
                if (((chars[i] < '0') || (chars[i] > '9')) && ((chars[i] < 'a') || (chars[i] > 'f'))
                        && ((chars[i] < 'A') || (chars[i] > 'F'))) {
                    return false;
                }
            }
            return true;
        }
        sz--;
        int i = start;
        while ((i < sz) || ((i < sz + 1) && (allowSigns) && (!foundDigit))) {
            if ((chars[i] >= '0') && (chars[i] <= '9')) {
                foundDigit = true;
                allowSigns = false;
            } else if (chars[i] == '.') {
                if ((hasDecPoint) || (hasExp)) {
                    return false;
                }
                hasDecPoint = true;
            } else if ((chars[i] == 'e') || (chars[i] == 'E')) {
                if (hasExp) {
                    return false;
                }
                if (!foundDigit) {
                    return false;
                }
                hasExp = true;
                allowSigns = true;
            } else if ((chars[i] == '+') || (chars[i] == '-')) {
                if (!allowSigns) {
                    return false;
                }
                allowSigns = false;
                foundDigit = false;
            } else {
                return false;
            }
            i++;
        }
        if (i < chars.length) {
            if ((chars[i] >= '0') && (chars[i] <= '9')) {
                return true;
            }
            if ((chars[i] == 'e') || (chars[i] == 'E')) {
                return false;
            }
            if ((!allowSigns) && ((chars[i] == 'd') || (chars[i] == 'D') || (chars[i] == 'f') || (chars[i] == 'F'))) {
                return foundDigit;
            }
            if ((chars[i] == 'l') || (chars[i] == 'L')) {
                return (foundDigit) && (!hasExp);
            }
            return false;
        }
        return (!allowSigns) && (foundDigit);
    }

    public static boolean isDoubleNumeric(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        return DOUBLE_PATTERN.matcher(str).matches();
    }

    /**
     * 判断是否为数字，且位数是否相符
     * @param str
     * @param numLen 要求整数位数
     * @param scaLen 要求小数位数
     * @return
     */
    public static boolean isNumberScale(String str, int numLen, int scaLen) {
        if (!isNumber(str)) {
            return false;
        }
        int strIndex = str.indexOf(".");
        int num;
        int scale = 0;
        if (strIndex>0) {
            num = str.substring(0, strIndex).length(); // 实际整数位数
            scale = str.substring(strIndex + 1).length(); // 实际小数位数
        } else {
            num = str.length();
        }
        if (num > numLen || scale > scaLen) {
            return false;
        }
        return true;
    }

    /**
     * Object对象转换为BigDecimal对象
     */
    public static BigDecimal toBigDecimal(Object obj) {
        BigDecimal bigDecimal = ZERO;
        if (null != obj) {
            if (obj instanceof BigDecimal) {
                bigDecimal = (BigDecimal) obj;
            } else {
                NumberFormat numberFormat = NumberFormat.getInstance();
                String str = obj.toString().trim();
                if ("".equals(str)) {
                    return bigDecimal;
                }
                Number number = null;
                try {
                    number = numberFormat.parse(str);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (null != number) {
                    try {
                        str = number.toString();
                        bigDecimal = new BigDecimal(str);
                    } catch (Exception e) {
                    }
                }
            }
        }
        return bigDecimal;
    }

    /**
     * Object对象转换为BigDecimal对象，保留指定小数位数
     * @param obj
     * @param scale
     * @return
     */
    public static BigDecimal toBigDecimal(Object obj, int scale) {
        return toBigDecimal(obj).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Object对象转换为BigDecimal对象，保留指定小数位数，并指定舍入模式
     * @param obj
     * @param scale
     * @param roundingMode
     * @return
     */
    public static BigDecimal toBigDecimal(Object obj, int scale, int roundingMode) {
        return toBigDecimal(obj).setScale(scale, roundingMode);
    }

    /**
     * 两个BigDecimal类型相加
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        BigDecimal bigDecimal = ZERO;
        if (a == null) {
            a = bigDecimal;
        }
        if (b == null) {
            b= bigDecimal;
        }
        return a.add(b);
    }

    /**
     * 两个Object类型相加
     * @param dec1
     * @param dec2
     * @return
     */
    public static BigDecimal add(Object dec1, Object dec2) {
        if (dec1 == null && dec2 == null) {
            return null;
        } else {
            return toBigDecimal(dec1).add(toBigDecimal(dec2));
        }
    }

    /**
     * 两个BigDecimal类型相减
     * @param dec1
     * @param dec2
     * @return
     */
    public static BigDecimal subtract(Object dec1, Object dec2) {
        if (dec1 == null && dec2 == null) {
            return null;
        } else {
            return toBigDecimal(dec1).subtract(toBigDecimal(dec2));
        }
    }

    /**
     * @deprecated 两个Object相乘，推荐使用divide(Object dec1,Object dec2,int scale)， 需要传入精度
     * @param dec1
     * @param dec2
     * @return
     */
    public static BigDecimal multiply(Object dec1, Object dec2) {
        if (dec1 == null && dec2 == null) {
            return null;
        } else {
            return toBigDecimal(dec1).multiply(toBigDecimal(dec2));
        }
    }

    /**
     * 两个Object相除，并指定保留小数位数
     * @param dec1
     * @param dec2
     * @param scale
     * @return
     */
    public static BigDecimal multiply(Object dec1, Object dec2, int scale) {
        if (dec1 == null || dec2 == null) {
            return null;
        } else {
            BigDecimal obj = toBigDecimal(dec1).multiply(toBigDecimal(dec2));
            return obj.setScale(scale, BigDecimal.ROUND_HALF_UP);
        }
    }

    /**
     * @deprecated 两个Object相除，推荐使用divide(Object dec1,Object dec2,int scale)， 需要传入精度
     * @param dec1
     * @param dec2
     * @return
     */
    public static BigDecimal divide(Object dec1, Object dec2) {
        return divide(dec1, dec2, 10, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 两个Object相除，并指定保留小数位数
     * @param dec1
     * @param dec2
     * @param scale
     * @return
     */
    public static BigDecimal divide(Object dec1, Object dec2, int scale) {
        return divide(dec1, dec2, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 两个Object相除，并指定保留小数位数，指定舍入模式
     * @param dec1
     * @param dec2
     * @param scale
     * @param roundingMode
     * @return
     */
    public static BigDecimal divide(Object dec1, Object dec2, int scale, int roundingMode) {
        if (dec1 == null && dec2 == null) {
            return null;
        }
        if (toBigDecimal(dec2).signum() == 0) {
            return null;
        }
        return toBigDecimal(dec1).divide(toBigDecimal(dec2), scale, roundingMode);
    }

    /**
     * 判断一个Ojbect是否是正数
     * @param obj
     * @return
     */
    public static boolean isPositiveBigDecimal(Object obj) {
        return toBigDecimal(obj).compareTo(ZERO) > 0;
    }

    /**
     * 是否为空或零
     * @param bd
     * @return
     */
    public static boolean isNullZero(BigDecimal bd) {
        return bd == null || bd.compareTo(ZERO) == 0;
    }

    /**
     * 比较两个金额字段的大小
     * @param obj1
     * @param obj2
     * @return 1 obj1 > obj2 0 obj1 = obj2 -1 obj1 < obj2
     */
    public static int compareValue(Object obj1, Object obj2) {
        return toBigDecimal(obj1).compareTo(toBigDecimal(obj2));
    }

    /**
     * 判断左边是否大于右边
     * @param obj1
     * @param obj2
     */
    public static boolean isGreaterThan(Object obj1, Object obj2) {
        return 1 == compareValue(obj1, obj2);
    }

    /**
     * 判断左边是否小于右边
     * @param obj1
     * @param obj2
     */
    public static boolean isLessThan(Object obj1, Object obj2) {
        return -1 == compareValue(obj1, obj2);
    }

    /**
     * 是否等于
     * @param obj1
     * @param obj2
     */
    public static boolean isEqual(Object obj1, Object obj2) {
        return 0 == compareValue(obj1, obj2);
    }

    /**
     * @param d
     * @return
     */
    public static String formatHardNumberTwo(double d) {
        // 1234.56123以1234.56格式输出
        DecimalFormat df = new DecimalFormat("#0.00");
        df.setGroupingUsed(false);
        return df.format(d);
    }

    /**
     * @param d
     * @return
     */
    public static String formatLocalNumberTwo(double d) {
        return formatLocalByPattern(d, "#.00");
    }

    /**
     * 采用千分位显示
     * @param d
     * @return
     */
    public static String formatHardNumberThoudTwo(double d) {
        // 保留两位小数，以千分位显示，等效于#,###.00
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(d);
    }

    public static String formatHardNumberTwo(String d) {
        // 保留两位小数
        if (d == null || "".equals(d.trim())) {
            return "0.00";
        }
        DecimalFormat df = new DecimalFormat("#.00");
        df.setGroupingUsed(false);
        return df.format(parseNumber(d).doubleValue());
    }

    /**
     * @param d
     * @return
     */
    public static String formatHardNumberFour(double d) {
        DecimalFormat df = new DecimalFormat("#.0000");
        df.setGroupingUsed(false);
        return df.format(d);
    }

    /**
     *
     * @param d
     * @return
     */
    public static String formatLocalNumberFour(double d) {
        return formatLocalByPattern(d, "#.0000");
    }

    /**
     *
     * @param d
     * @return
     */
    public static String formatNumberTwo(double d) {
        return formatByPattern(d, "#.00");
    }

    public static String formatNumberTwo2(double d) {
        return formatByPattern(d, "0.00");
    }

    /**
     *
     * @param d
     * @return
     */
    public static String formatNumberFour(double d) {
        return formatByPattern(d, "#.0000");
    }

    /**
     * 根据本地区域要求格式化金额
     * @param d
     * @return
     */
    public static String formatCurrency(double d) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        return currencyFormat.format(d);
    }

    /**
     * 自定义格式化金额
     * @param d
     * @return
     */
    public static String formatDecimal(double d) {
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setDecimalSeparatorAlwaysShown(true);
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);
        return decimalFormat.format(d);
    }

    /**
     * 根据本地区域要求格式化数字
     * @param d
     * @return
     */
    public static String formatNumber(double d) {
        NumberFormat currencyFormat = NumberFormat.getNumberInstance();
        return currencyFormat.format(d);
    }

    /**
     * 根据本地区域要求格式化百分数
     * @param d
     * @return
     */
    public static String formatPercent(double d) {
        NumberFormat currencyFormat = NumberFormat.getPercentInstance();
        return currencyFormat.format(d);
    }

    /**
     * 控制指数形式的格式
     * @return
     */
    public static String formatENumberByPattern(double d, String pattern) {
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(d);
    }

    /**
     * 以某种格式转化
     * @param d
     * @param pattern
     * @return
     */
    public static String formatByPattern(double d, String pattern) {
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(d);
    }

    /**
     * 以某种格式转化
     * @return
     */
    public static String formatLocalByPattern(double d, String pattern) {
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        df.applyPattern(pattern);
        return df.format(d);
    }

    /**
     * 读取并解析包含格式化的数字的字符串
     * @param number
     * @return
     */
    public static Number parseNumber(String number) {
        try {
            NumberFormat nf = NumberFormat.getInstance();
            return nf.parse(number);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return 0;
    }

    /**
     * double保留n位小数
     * @param numerical
     * @param length
     * @return
     */
    private static double getRoundDouble(double numerical, int length) {
        DecimalFormat decimalFormat = null;
        if (length == 15) {
            decimalFormat = new DecimalFormat("###.###############");
        } else if (length == 5) {
            decimalFormat = new DecimalFormat("###.#####");
        } else if (length == 4) {
            decimalFormat = new DecimalFormat("###.####");
        } else if (length == 3) {
            decimalFormat = new DecimalFormat("########.###");
        } else if (length == 2) {
            decimalFormat = new DecimalFormat("########.##");

        } else if (length == 1) {
            decimalFormat = new DecimalFormat("########.#");
        } else if (length == 0) {
            decimalFormat = new DecimalFormat("########");
        }
        return Double.parseDouble(decimalFormat.format(numerical));
    }

    /**
     * 保留两位小数
     * @param numerical
     * @return
     */
    public static double getRoundDoubleTwo(double numerical) {
        return getRoundDouble(numerical, 2);
    }

    /**
     * 保留两位小数
     * @param numerical
     * @return
     */
    public static String getRoundStringTwo(double numerical) {
        return getRoundString(numerical, 2);
    }

    /**
     * 保留三位小数
     * @param numerical
     * @return
     */
    public static double getRoundDoubleThree(double numerical) {
        return getRoundDouble(numerical, 3);
    }

    /**
     * 保留四位小数
     * @param numerical
     * @return
     */
    public static double getRoundDoubleFour(double numerical) {
        return getRoundDouble(numerical, 4);
    }

    /**
     * 保留全部
     * @param numerical
     * @return
     */
    public static double getRoundDoubleMore(double numerical) {
        return getRoundDouble(numerical, 15);
    }

    /**
     * double保留指定位小数
     * @param numerical
     * @param length
     * @return
     */
    public static String getRoundString(double numerical, int length) {
        DecimalFormat decimalFormat = null;
        if (length == 4) {
            decimalFormat = new DecimalFormat("###.####");
        } else if (length == 3) {
            decimalFormat = new DecimalFormat("##,###,###.###");
        } else if (length == 2) {
            decimalFormat = new DecimalFormat("##,###,##0.00");
        } else if (length == 1) {
            decimalFormat = new DecimalFormat("##,###,###.#");
        } else if (length == 0) {
            decimalFormat = new DecimalFormat("##,###,###");
        }
        return decimalFormat.format(numerical);
    }

    /**
     * 面积字段 强制保留位数,如: length=3,numerical=7.7777,则返回值为7.778,小数位超过length长度默认四舍五入
     * 小数位之前每隔三位用逗号分隔
     * @param numerical
     * @param length
     * @return
     */
    private static String getRoundStringValue(double numerical, int length) {
        DecimalFormat decimalFormat;
        double decimalFlag;
        double resultValue;
        if (length == 3) {
            decimalFlag = 1000;
            decimalFormat = new DecimalFormat("##,###,##0.000");
        } else if (length == 2) {
            decimalFlag = 100;
            decimalFormat = new DecimalFormat("##,###,##0.00");
        } else if (length == 1) {
            decimalFlag = 10;
            decimalFormat = new DecimalFormat("##,###,##0.0");
        } else if (length == 0) {
            decimalFlag = 0;
            decimalFormat = new DecimalFormat("##,###,###");
        } else {
            decimalFlag = 10000;
            decimalFormat = new DecimalFormat("##,###,##0.0000");
        }
        if (decimalFlag != 0) {
            resultValue = Math.round(numerical * decimalFlag) / decimalFlag;
        } else {
            resultValue = Math.round(numerical);
        }
        if (decimalFormat != null) {
            return decimalFormat.format(resultValue);
        }
        return "0";
    }

    /**
     *
     * 面积字段 强制保留位数,如: length=3,numerical=7.7777,则返回值为7.778,小数位超过length长度默认四舍五入
     * @param numerical
     * @param length 小数位数
     * @return double类型
     */
    private static double getRoundDoubleValue(double numerical, int length) {
        // 默认保留整数
        StringBuilder formatStr = new StringBuilder("#");
        double decimalFlag = 1;
        if (length != 0) {
            formatStr.append(".");
            for (int i = 0; i < length; i++) {
                decimalFlag *= 10;
                formatStr.append("#");
            }
        }
        DecimalFormat decimalFormat = new DecimalFormat(formatStr.toString());
        double resultValue = Math.round(numerical * decimalFlag) / decimalFlag;
        return Double.parseDouble(decimalFormat.format(resultValue));
    }

    /**
     *
     * 面积字段 强制保留位数,如: length=3,numerical=7.7777,则返回值为7.778,小数位超过length长度默认四舍五入
     * @param numerical
     * @param length 小数位数
     * @return double类型
     */
    private static double getRoundDoubleValueV2(double numerical, int length) {
        NumberFormat format = NumberFormat.getInstance();
        format.setMinimumFractionDigits(length);
        format.setMaximumFractionDigits(length);
        return 1L;
    }

    /**
     * 动态设定保留位数
     * @param numerical
     * @param places
     * 保留位数
     * @return
     */
    public static String getRoundStringPlaces(double numerical, int places) {
        return getRoundStringValue(numerical, places);
    }

    /**
     * double保留两位小数，转化为String
     * @param numerical
     * @return
     */
    public static String getRoundStringTwoNew(double numerical) {
        DecimalFormat decimalFormat;
        decimalFormat = new DecimalFormat("##,###,##0.00");
        return decimalFormat.format(numerical);
    }

    /**
     * 精确到元 ,举例：住宅本期成交均价为“15207.71” 则应为： “15208”
     * @param data
     * 需要转换的数据
     * @return String
     */
    public static String toOneWithHalfUp(double data) {
        return getRoundString(new BigDecimal(data).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue(), 0);
    }

    /**
     * 精确到万元 ,举例：住宅本期成交均价为“15207.71” 则应为： “1.52万”
     * @param data
     * 需要转换的数据
     * @return String peter 2009-06-05
     */
    public static String toTenThousand(double data) {
        return getRoundString(new BigDecimal(String.valueOf((data / 10000))).setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue(), 2);// +"万";
    }

    /**
     * 精确到万元 ,举例：住宅本期成交均价为“15207.71” 则应为： “1万”
     * @param data
     * 需要转换的数据
     * @return String peter 2011-07-19
     */
    public static String toTenThousand1(double data) {
        return getRoundString(new BigDecimal(String.valueOf((data / 10000))).setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue(), 0);// +"万";
    }

    public static String changeForMillion(double data) {
        return new BigDecimal(String.valueOf((data / 1000000))).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    public static double changeForMillionD(double data) {
        String tempStr = new BigDecimal(String.valueOf((data / 1000000))).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        return Double.parseDouble(tempStr);
    }

    /**
     * 提供精确的加法运算
     * @param v1
     * 被加数
     * @param v2
     * 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算
     * @param v1
     * 被减数
     * @param v2
     * 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算
     * @param v1
     * 被乘数
     * @param v2
     * 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入
     * @param v1
     * 被除数
     * @param v2
     * 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入
     * @param v1
     * 被除数
     * @param v2
     * 除数
     * @param scale
     * 表示表示需要精确到小数点以后几位
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理
     * @param v
     * 需要四舍五入的数字
     * @param scale
     * 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 传入原来的数据和保留位数，得到最新的数据
     * 会去除末尾多余的0
     * @param beforeData 数值
     * @param paraValue 保留位数
     * @return double
     */
    public static double getParaDouble(Object beforeData, int paraValue) {
        double resultData = 0;
        StringBuilder builder = new StringBuilder();
        if (null != beforeData) {
            String[] strings = beforeData.toString().split(",");
            if (strings.length > 0) {
                for (int i = 0; i < strings.length; i++) {
                    builder.append(strings[i]);
                }
            }
            resultData = getRoundDoubleValue(Double.parseDouble(builder.toString()), paraValue);
        }
        return resultData;
    }

    /**
     * 项目参数设置影响,根据保留位和进位方式 对 金额字段精度处理. 单位:元,角,分
     * @author 周海明
     * @date 2012-4-27
     * @param numerical
     * 原数据
     * @param places
     * 保留位数
     * @param carry
     * 进位方式
     * @param isComma
     * 是否以逗号分隔显示(true:是,false:否)
     * @return
     */
    public static String getReturnResultPlaces(double numerical, int places, int carry, boolean isComma) {
        return getReturnResultValue(numerical, places, carry, isComma);
    }

    /**
     * 项目参数设置影响,根据保留位和进位方式 对 金额字段精度处理. 单位：元,角,分
     * @param numerical
     * 原数据
     * @param places
     * 保留位数
     * @param carry
     * 进位方式
     * @param isComma
     * 是否以逗号分隔显示(true:是,false:否)
     * @return
     */
    private static String getReturnResultValue(double numerical, int places, int carry, boolean isComma) {
        double decimalFlag;
        double resultValue;
        if (places == 5) {
            decimalFlag = 10000;
        } else if (places == 4) {
            decimalFlag = 1000;
        }
        // 分:保留2位小数
        else if (places == 3) {
            decimalFlag = 100;
        }
        // 角:保留1位小数
        else if (places == 2) {
            decimalFlag = 10;
        }
        // 默认：元
        else {
            decimalFlag = 0;
        }

        if (decimalFlag != 0) {
            // 进位处理
            // 四舍五入
            if (carry == 1) {
                resultValue = Math.round(numerical * decimalFlag) / decimalFlag;
            }// 舍零
            else if (carry == 2) {
                resultValue = Math.floor(numerical * decimalFlag) / decimalFlag;
            }// 进位
            else if (carry == 3) {
                resultValue = Math.ceil(numerical * decimalFlag) / decimalFlag;
            }// 其他默认为四舍五入
            else {
                resultValue = Math.round(numerical * decimalFlag) / decimalFlag;
            }
        } else {

            // 进位处理
            // 四舍五入
            if (carry == 1) {
                resultValue = Math.round(numerical);
            }// 舍零
            else if (carry == 2) {
                resultValue = Math.floor(numerical);
            }// 进位
            else if (carry == 3) {
                resultValue = Math.ceil(numerical);
            }// 其他默认为四舍五入
            else {
                resultValue = Math.round(numerical);
            }
        }
        // 强制保留小数位处理
        DecimalFormat decimalFormat = null;
        if (isComma) {
            if (decimalFlag == 10000) {
                decimalFormat = new DecimalFormat("##,###,##0.0000");
            } else if (decimalFlag == 1000) {
                decimalFormat = new DecimalFormat("##,###,##0.000");
            } else if (decimalFlag == 100) {
                decimalFormat = new DecimalFormat("##,###,##0.00");
            } else if (decimalFlag == 10) {
                decimalFormat = new DecimalFormat("##,###,##0.0");
            } else if (decimalFlag == 0) {
                decimalFormat = new DecimalFormat("##,###,###");
            }
        } else {
            if (decimalFlag == 10000) {
                decimalFormat = new DecimalFormat("#0.0000");
            } else if (decimalFlag == 1000) {
                decimalFormat = new DecimalFormat("#0.000");
            } else if (decimalFlag == 100) {
                decimalFormat = new DecimalFormat("#0.00");
            } else if (decimalFlag == 10) {
                decimalFormat = new DecimalFormat("#0.0");
            } else if (decimalFlag == 0) {
                decimalFormat = new DecimalFormat("#");
            }
        }
        return decimalFormat.format(resultValue);
    }

    /**
     * 项目参数设置影响,根据保留位和进位方式 对 金额字段精度处理. 单位:万,千,百,十,元
     * @param numerical
     * 原数据
     * @param places
     * 保留位数
     * @param carry
     * 进位方式
     * @param isComma
     * 是否以逗号分隔显示(true:是,false:否)
     * @return
     */
    public static String getBigAmountPlaces(double numerical, int places, int carry, boolean isComma) {
        return getBigAmountValue(numerical, places, carry, isComma);
    }

    /**
     * 项目参数设置影响,根据保留位和进位方式 对 金额字段精度处理. 单位：万,千,百,十,元
     * @param numerical
     * 原数据
     * @param places
     * 保留位数
     * @param carry
     * 进位方式
     * @param isComma
     * 是否以逗号分隔显示(true:是,false:否)
     * @return
     */
    private static String getBigAmountValue(double numerical, int places, int carry, boolean isComma) {
        double decimalFlag;
        double resultValue;

        if (places == 5) {
            decimalFlag = 0;
        } else if (places == 4) {
            decimalFlag = 10;
        } else if (places == 3) {
            decimalFlag = 100;
        } else if (places == 2) {
            decimalFlag = 1000;
        } else if (places == 1) {
            decimalFlag = 10000;
        }
        // 默认:千
        else {
            decimalFlag = 1000;
        }
        if (decimalFlag != 0) {
            // 进位处理
            // 四舍五入
            if (carry == 1) {
                resultValue = Math.round(numerical / decimalFlag) * decimalFlag;
            }// 舍零
            else if (carry == 2) {
                resultValue = Math.floor(numerical / decimalFlag) * decimalFlag;
            }// 进位
            else if (carry == 3) {
                resultValue = Math.ceil(numerical / decimalFlag) * decimalFlag;
            }// 其他默认为四舍五入
            else {
                resultValue = Math.round(numerical / decimalFlag) * decimalFlag;
            }
        } else {
            // 进位处理
            // 四舍五入
            if (carry == 1) {
                resultValue = Math.round(numerical);
            }// 舍零
            else if (carry == 2) {
                resultValue = Math.floor(numerical);
            }// 进位
            else if (carry == 3) {
                resultValue = Math.ceil(numerical);
            }// 其他默认为四舍五入
            else {
                resultValue = Math.round(numerical);
            }
        }
        // 是否有逗号
        DecimalFormat decimalFormat = null;
        if (isComma) {
            decimalFormat = new DecimalFormat("##,###,###");
        } else {
            decimalFormat = new DecimalFormat("#");
        }
        return decimalFormat.format(resultValue).toString();
    }

    /**
     * 获得特定长度的一串随机数字
     * @param length
     * @return
     * @throws
     */
    public static String getRandomNumberStr(int length){
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for(int i = 0 ; i < length; i++){
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }

    /**
     * 手机号验证 (国际)
     * @param phone
     * @return 验证通过返回true
     */
    public static boolean isMobile(String phone) {
        if (phone == null) {
            return false;
        }
        if (phone.indexOf("-") > 0) {
            String[] phoneStr = phone.split("-");
            // 发国际短信 , 账号 66-898678405
            return phoneStr.length == 2 && phoneStr[0].length() < 6 && phoneStr[1].length() > 6;
        } else {
            // "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$" //当前手机号全适配 考虑号段新增就要改
            Matcher m = PHONE_PATTERN.matcher(phone);
            return m.matches();
        }
    }

    /**
     * 将BigDecimal转化为不含小数点后末尾0的字符串
     * @param bigDecimal 已设置精度的BigDecimal的数值
     */
    public static String bigDecimalToStringWithoutZero(BigDecimal bigDecimal) {
        return bigDecimal.stripTrailingZeros().toPlainString();
    }

    /**
     * 将BigDecimal转化为字符串
     * @param bigDecimal 已设置精度的BigDecimal的数值
     */
    public static String bigDecimalToStringWithZero(BigDecimal bigDecimal) {
        return bigDecimal.toString();
    }

    /**
     * @Description 泛型求和
     * @author Jef
     * @date 2020/4/15
     * @param num1
     * @param num2
     * @return T
     */
    public static <T extends Number> T sum(T num1, T num2) {
        T result = null;
        if (num1 == null || num2 == null) {
            result = num1 != null ? num1 : (num2 != null ? num2 : null);
        } else if (num1 instanceof Integer) {
            result = (T) Integer.valueOf(num1.intValue() + num2.intValue());
        } else if (num1 instanceof Float) {
            result = (T) Float.valueOf(num1.floatValue() + num2.floatValue());
        } else if (num1 instanceof Double) {
            result = (T) Double.valueOf(num1.doubleValue() + num2.doubleValue());
        } else if (num1 instanceof Long) {
            result = (T) Long.valueOf(num1.longValue() + num2.longValue());
        } else if (num1 instanceof Short) {
            result = (T) Short.valueOf((short) (num1.shortValue() + num2.shortValue()));
        } else if (num1 instanceof Byte) {
            result = (T) Byte.valueOf((byte) (num1.byteValue() + num2.byteValue()));
        } else if (num1 instanceof BigDecimal) {
            result = (T) ((BigDecimal) num1).add((BigDecimal) num2);
        }
        return result;
    }

    /**
     * BigDecimal格式化为字符串
     * 向下取整后不保留小数，即去小数点及之后的数
     */
    public static String formatToStringDown(BigDecimal bd) {
        return formatToString(bd, BigDecimal.ROUND_DOWN);
    }

    /**
     * BigDecimal格式化为字符串
     * 向上取整后不保留小数，即去小数点及之后的数
     */
    public static String formatToStringUp(BigDecimal bd) {
        return formatToString(bd, BigDecimal.ROUND_UP);
    }

    /**
     * BigDecimal格式化为字符串
     */
    public static String formatToString(BigDecimal bd, int roundMode) {
        if (bd == null) {
            return "0";
        }
        DecimalFormat myformat= new DecimalFormat("0");
        bd = toBigDecimal(bd, 0, roundMode);
        return myformat.format(bd);
    }

   public void mutifyAndAdd() {
       BigDecimal a = NumberUtils.multiply(68790.0000000000,	1);
       BigDecimal a3 = NumberUtils.multiply(308245.0000000000,	1);
       BigDecimal a7 = NumberUtils.multiply(399688.0000000000,	1);
       BigDecimal a2 = NumberUtils.multiply(10561.0000000000,	20);
       BigDecimal a4 = NumberUtils.multiply(6120.0000000000,	20);
       BigDecimal a6 = NumberUtils.multiply(8267.0000000000,	20);
       BigDecimal a5 = NumberUtils.multiply(553.0000000000,	40);
       List<BigDecimal> bigDecimalList = Lists.newArrayList();
       bigDecimalList.add(a);
       bigDecimalList.add(a2);
       bigDecimalList.add(a3);
       bigDecimalList.add(a4);
       bigDecimalList.add(a5);
       bigDecimalList.add(a6);
       bigDecimalList.add(a7);
       BigDecimal all = NumberUtils.safeAdd(bigDecimalList, 2, BigDecimal.ROUND_HALF_UP);
       System.out.println(all);
   }

   /**
    * 去除末尾多余的0
    * @author Jef
    * @date 2020/5/27
    * @param bd
    * @return java.lang.String
    */
   public static String stripTrailingZeros(BigDecimal bd) {
       return NumberUtils.toBigDecimal(bd).stripTrailingZeros().toPlainString();
   }

    /**
     * 除法向上取整
     *
     * @param num    除数
     * @param numTwo 被除数
     * @return
     */
    public static int divideUp(int num, int numTwo) {
        return num / numTwo + (num % numTwo == 0 ? 0 : 1);
    }

    public static BigDecimal getPercent(Object obj, int scale) {
        return divide(obj, ONE_HUNDRED, scale);
    }

    public static BigDecimal getRateValue(Object obj, Object rate, int scale) {
        return divide(multiply(obj, rate, scale), ONE_HUNDRED, scale);
    }
}
