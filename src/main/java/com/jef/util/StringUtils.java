package com.jef.util;

import com.google.common.collect.Lists;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jef
 */
public class StringUtils extends org.apache.commons.lang.StringUtils {
    private static final String DEFAULT_CHARTSET = "UTF-8";
    // 乱码正则
    private static final Pattern LUAN_PATTERN = Pattern.compile("\"\\\\s*|\\t*|\\r*|\\n*\"");
    // BigDecimal正则
    private static Pattern BIGDECIMAAL_PATTERN = Pattern.compile("([1-9]+[0-9]*|0)(\\.[\\d]+)?");
    // 正负浮点数或0即所有浮点数正则
    private static Pattern BIGDECIMAAL_PATTERN_ALL = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");
    // 正负整数或0即所有整数正则
    private static Pattern POSITIVE_NEGATIVE_PATTERN = Pattern.compile("^[-\\+]?[\\d]*$");
    /**
     * 正整数正则
     */
    private static Pattern MORETHAN_ZERO_PATTERN = Pattern.compile("^[1-9]\\d*$");
    /**
     *判断字符串是否仅含有数字和字母正则
     */
    private static String LETTERORDIGIT_PATTERN = "^[a-z0-9A-Z]+$";

    /**
     * 将驼峰式命名的字符串转换为下划线大写方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。</br>
     * 例如：HelloWorld->HELLO_WORLD
     *
     * @param name
     *            转换前的驼峰式命名的字符串
     * @return 转换后下划线大写方式命名的字符串
     */
    public static String underscoreName(String name) {
        StringBuilder result = new StringBuilder();
        if ((name != null) && (name.length() > 0)) {
            // 将第一个字符处理成大写
            result.append(name.substring(0, 1).toUpperCase());
            // 循环处理其余字符
            for (int i = 1; i < name.length(); i++) {
                String s = name.substring(i, i + 1);
                // 在大写字母前添加下划线
                if (s.equals(s.toUpperCase())
                        && !Character.isDigit(s.charAt(0))&&Character.isLetter(s.charAt(0))) {
                    result.append("_");
                }
                // 其他字符直接转成大写
                result.append(s.toUpperCase());
            }
        }
        return result.toString();
    }

    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。</br>
     * 例如：HELLO_WORLD->HelloWorld
     *
     * @param name
     *            转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String camelName(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if ((name == null) || name.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母小写
            return name.substring(0, 1).toLowerCase() + name.substring(1);
        }
        // 用下划线将原始字符串分割
        String camels[] = name.split("_");
        for (String camel : camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 处理真正的驼峰片段
            if (result.length() == 0) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel.toLowerCase());
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(camel.substring(0, 1).toUpperCase());
                result.append(camel.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /**
     * 是否不为空
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || "null".equals(str) || "".equals(str.trim()) || str.trim().length() == 0;
    }

    /**
     * 将List<Stirng>转化为String，以某种符号分割
     * @param strList String集合
     * @param delimiter 分隔符
     */
    public static String getStringFromList(List<String> strList, String delimiter) {
        if (null == strList || 0 == strList.size()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String str : strList) {
            if (isNotEmpty(str)) {
                if (isNotEmpty(sb.toString())) {
                    sb.append(delimiter);
                }
                sb.append(str);
            }
        }
        return sb.toString();
    }

    /**
     * 将Set<Stirng>转化为String，以某种符号分割
     * @param strSet String集合
     */
    public static String getStringFromSetV2(Set<String> strSet) {
        return getStringFromSetV2(strSet, ",");
    }

    /**
     * 将Set<Stirng>转化为String，以某种符号分割
     * @param strSet String集合
     * @param delimiter 分隔符
     */
    public static String getStringFromSetV2(Set<String> strSet, String delimiter) {
        List<String> list = new ArrayList<>(strSet);
        return getStringFromList(list, delimiter);
    }

    /**
     * 将Set<Stirng>转化为String，以某种符号分隔，常用于页面展示
     * 以英文逗号分隔
     * @param strSet String集合
     */
    public static String getStringFromSet(Set<String> strSet) {
        return getStringFromSet(strSet, ",");
    }

    /**
     * 将Set<Stirng>转化为String，以某种符号分隔，常用于页面展示
     * @param strSet String集合
     * @param delimiter 分隔符，常用,-等
     */
    public static String getStringFromSet(Set<String> strSet, String delimiter) {
        if (null == strSet || 0 == strSet.size()) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (String str : strSet) {
            if (isNotEmpty(str)) {
                if (isNotEmpty(sb.toString())) {
                    sb.append(delimiter);
                }
                sb.append(str);
            }
        }
        return sb.toString();
    }

    /**
     * List<String>转化为String，需要jdk1.8，格式：test1, test2
     * @param list
     * @return
     */
    public static String getStringFromListOne(List<String> list, String delimiter) {
        return String.join(delimiter, list);
    }

    /**
     * List<String>转化为String，使用jar，格式：test1, test2
     * @param list
     * @param delimiter
     * @return
     */
    public static String getStringFromListTwo(List<String> list, String delimiter) {
        return StringUtils.join(list, delimiter);
    }

    /**
     * List<String>转化为String，格式：test1, test2
     * @param list
     * @return
     */
    public static String getStringFromListThree(List<String> list) {
        return getStringFromArray(getArrayFromList(list));
    }

    /**
     * List<String>转化为String，格式：test1, test2
     * @param list
     * @return
     */
    public static String getStringFromListFour(List<String> list, String delimiter) {
        return String.join(",", list);
    }

    /**
     * 数组转化为String，格式：test1, test2
     * @param strArray
     * @return
     */
    public static String getStringFromArray(String[] strArray) {
        String s1 = Arrays.toString(strArray);
        return s1.substring(1, s1.length() - 1).replace(" ", "");
    }

    /**
     * List<String>转数组
     * @param list
     */
    public static String[] getArrayFromList(List<String> list) {
        return list.toArray(new String[list.size()]);
    }

    /**
     * 字符串转List<String>，以英文逗号分隔
     * @author Jef
     * @date 2019/9/8
     * @param str 字符串
     * @return java.util.List<java.lang.String>
     */
    public static List<String> getListFromString(String str) {
        if (isEmpty(str)) {
            return new ArrayList<>();
        }
        return getListFromString(str, ",");
    }

    /**
     * 以特定分隔符分隔的字符串转List<String>
     * @author Jef
     * @date 2019/9/8
     * @param str 字符串
     * @param delimiter 分隔符
     * @return java.util.List<java.lang.String>
     */
    public static List<String> getListFromString(String str, String delimiter) {
        return new ArrayList<>(Arrays.asList(getArrayFromString(str, delimiter)));
    }

    /**
     * 字符串转数组
     * @author Jef
     * @date 2019/9/8
     * @param str 字符串
     * @param delimiter 分隔符
     * @return java.lang.String[]
     */
    public static String[] getArrayFromString(String str, String delimiter) {
        return str.split(delimiter);
    }

    /**
     * 数组转List<String>
     * @param strArray 字符串数组
     */
    public static List<String> getListFromArray(String[] strArray) {
        return new ArrayList<>(Arrays.asList(strArray));
    }

    /**
     * 针对前台界面传递的参数进行解码，防止乱码产生
     * @param srcData %E4%B8%AD%E6%96%87   对应的就是中文 由页面js encodeURIComponent("中文")产生
     * @return
     */
    public static String decodeURLCharset(String srcData){
        String value = decodeURLCharset(srcData,DEFAULT_CHARTSET);
        return cleanXSS( value) ;
    }

    /**
     * XSS安全问题， 通过html编码转义
     */
    public static String cleanXSS(String value) {
        if (value == null) {
            return null;
        }
        // 转义
        value = value.replaceAll("<", "<").replaceAll(">", ">");
        //房间可能带括号
        //	value = value.replaceAll("\\(", "(").replaceAll("\\)", ")");
        value = value.replaceAll("'", "'");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']",
                "\"\"");
        return value.trim();
    }

    /**
     * 针对前台界面传递的参数进行解码，防止乱码产生
     * @param srcData 需要进行解码的数据
     * @param chartType 解码类型
     * @return
     */
    public static String decodeURLCharset(String srcData, String chartType) {
        if (srcData == null || srcData.trim().length() <= 0) {
            return srcData;
        }
        if (chartType == null) {
            chartType = DEFAULT_CHARTSET;
        }
        try {
            srcData = URLDecoder.decode(srcData, chartType);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return srcData;
    }

    /**
     * 将乱码转义
     * @date:Sep 25, 2013 3:04:39 PM
     * @param data
     * @return
     */
    public static String getUTF8String(String data) {
        if (data == null || data.trim().length() <= 0) {
            return "";
        }
        String result;
        try {
            result = new String(data.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            result = data;
        }
        return result;
    }

    /**
     * 判断字符串值是否相等
     * @param str1 字符串1
     * @param str2 字符串2
     * @return boolean
     */
    public static boolean isValueEquals(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return false;
        } else {
            return str1.equals(str2);
        }
    }

    /**
     * 判断字符串不为空且长度符合
     * @author Tony
     * @date:Mar 9, 2016 1:47:43 PM
     * @param str
     * @param len
     * @return
     */
    public static boolean isNotEmptyLength(String str, int len){
        return isNotEmpty(str) && str.length() <= len;
    }

    /**
     * 如果为空设置默认值
     * @param str 字符串
     * @param ds 默认字符串
     * @return
     */
    public static String getStringByDefault(String str) {
        return getStringByDefault(str, "");
    }

    /**
     * 如果为空设置默认值
     * @param str 字符串
     * @param ds 默认字符串
     * @return
     */
    public static String getStringByDefault(String str, String ds) {
        if (isEmpty(str)) {
            return ds;
        } else {
            return str;
        }
    }

    /**
     * 获取随机字符串
     * @param length 生成字符串的长度
     * @return
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 数组转String
     * @param array 数组
     * @param separator 分隔符
     */
    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }

    /**
     * 数组转String
     * @param array 数组
     * @param separator 分隔符
     * @param startIndex 开始下标
     * @param endIndex 结束下标
     */
    public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = "";
        }
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return "";
        }
        StringBuilder buf = new StringBuilder(noOfItems * 16);
        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    /**
     * 获取字符出现次数
     * @param str 字符串
     * @param key 字符
     * @return 次数
     */
    public static int getKeyCount(String str, String key) {
        int count = 0;
        int index = 0;
        while((index = str.indexOf(key, index)) != -1) {
            index = index + key.length();
            count ++;
        }
        return count;
    }

    /**
     * 判断是否含有特殊字符
     * @param str 字符串
     * @return
     */
    public static boolean stringValidate(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？し️️️️]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();

    }

    /**
     * 判断是否为中文
     * @param c 字符
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否含有中文乱码
     * @param str
     * @return
     */
    public static boolean isMessyCode(String str) {
        Matcher m = LUAN_PATTERN.matcher(str);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = 0;
        float count = 0;
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!Character.isLetterOrDigit(c)) {
                if (!isChinese(c)) {
                    count = count + 1;
                }
                chLength++;
            }
        }
        float result = count / chLength;
        return result > 0.4;
    }

    /**
     * 判断一个字符串是否都是中文
     * @param str 字符串
     * @return
     */
    public static boolean isChinese(String str) {
        char[] chars = str.toCharArray();
        for (char c : chars) {
            if (!isChinese(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将字母转换成数字
     * @param input
     * @return
     */
    public static String letterToNumOne(String input) {
        String reg = "[a-zA-Z]";
        StringBuffer strBuf = new StringBuffer();
        input = input.toLowerCase();
        if (null != input && !"".equals(input)) {
            for (char c : input.toCharArray()) {
                if (String.valueOf(c).matches(reg)) {
                    strBuf.append(c - 96);
                } else {
                    strBuf.append(c);
                }
            }
            return strBuf.toString();
        } else {
            return input;
        }
    }

    /**
     * 将字母转换成数字
     * @param input
     */
    public static Integer letterToNum(String input) {
        StringBuilder numStr = new StringBuilder();
        for (byte b : input.getBytes()) {
            numStr.append(b - 96);
        }
        return Integer.valueOf(numStr.toString());
    }

    /**
     * 将数字转换成字母
     * @param input
     */
    public static String numToLetter(String input) {
        StringBuilder str = new StringBuilder();
        for (byte b : input.getBytes()) {
            str.append((char) (b + 48));
        }
        return str.toString();
    }

    /**
     * 获得小数位数
     * @param str 字符串
     * @return
     */
    public static int getDigit(String str){
        int index = str.indexOf(".");
        int digitNum = 0 ;
        if(index >= 0){
            digitNum = str.length() - ( index + 1 );
        }
        return digitNum;
    }

    /**
     * 是否是数值型
     * @param str 字符串
     * @return
     */
    public static boolean isBigDecimal(String str){
        return BIGDECIMAAL_PATTERN.matcher(str).matches();
    }

    /**
     * 判断字符串是否为正整数或0
     * @return boolean
     * @throws
     */
    public static boolean isNumeric(String str) {
        if (str == null || "".equals(str)) {
            return false;
        }
        for (int i = str.length(); --i >= 0;) {
            if (!Character.isDigit(str.charAt(i))) {
                // 判断了一个字符串的每个字符是否都是数字
                return false;
            }
        }
        return true;
    }

    public static boolean isNumericV3(String str){
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    public static boolean isNumericV4(String str){
        for(int i=str.length();--i>=0;){
            int chr=str.charAt(i);
            if(chr<48 || chr>57)
                return false;
        }
        return true;
    }

    /**
     * 是否是正整数
     * @author Jef
     * @date 2020/1/3
     * @param string
     * @return boolean
     */
    public static boolean isNumericMorethanZero(String string) {
        if (isEmpty(string)) {
            return false;
        }
        return MORETHAN_ZERO_PATTERN.matcher(string).matches();
    }

    /**
     * 是否是正负浮点数或0
     * @param str 需要验证的字符串
     * @return
     */
    public static boolean isBigDecimalAll(String str){
        // 003,004这样的视为字符串
        if (str.startsWith("0") && !"0".equals(str) && !str.contains(".")) {
            return false;
        }
        return BIGDECIMAAL_PATTERN_ALL.matcher(str).matches();
    }

    /**
     * 判断是否为正负整数或0
     * @param str 需要验证的字符串
     * @return
     */
    public static boolean isNumericAll(String str){
        // 003,004这样的视为字符串
        if (str.startsWith("0") && !"0".equals(str) && !str.contains(".")) {
            return false;
        }
        return POSITIVE_NEGATIVE_PATTERN.matcher(str).matches();
    }

    /**
     * Description:除去空字符串，并消除两边空格
     */
    public static String dealNull(String str) {
        return str == null ? "" : str.trim();
    }

    /**
     * Description:除去空字符串，并消除两边空格
     */
    public static Object dealNull(Object str) {
        return str == null ? "" : str;
    }

    /**
     * Description:将对象转化为字符串，用于特殊场景
     */
    public static String getStr(Object object) {
        if (object != null && object.toString().length() > 0) {
            return object.toString();
        }
        return "";
    }

    /**
     * Description:截取指定长度的字符串
     * 与字符串 substring 方法相比，规避空字符串，长度不够截取等问题
     */
    public static String getSubString(String sOurce, int len) {
        if (isEmpty(sOurce)) {
            return "";
        }
        if (sOurce.length() <= len) {
            return sOurce;
        }
        return sOurce.substring(0, len);
    }

    /**
     * Description:字符串转换为 boolean
     */
    public static boolean booleanValue(String tfString) {
        String trimmed = tfString.trim().toUpperCase();
        return (trimmed.equals("Y")) || (trimmed.equals("true"));
    }


    /**
     * Description:去除字符串所有空格
     */
    public static String trimAllWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str);
        int index = 0;
        while (sb.length() > index) {
            if (Character.isWhitespace(sb.charAt(index))) {
                sb.deleteCharAt(index);
            } else {
                index++;
            }
        }
        return sb.toString();
    }

    /**
     * Description:字符串是否有长度
     */
    public static boolean hasLength(CharSequence str) {
        return (str != null && str.length() > 0);
    }

    /**
     * Description:获取UUID
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    /**
     * 字符串首字母小写
     *
     * @param name 待处理字符串
     */
    public static String firstLetterLowercase(String name) {
        if (isEmpty(name)) {
            return null;
        }
        return lowerCase(name.substring(0, 1)) + name.substring(1);
    }

    /**
     * 首字母大写
     *
     * @param name 待处理字符串
     */
    public static String firstLetterUppercase(String name) {
        if (isEmpty(name)) {
            return null;
        }
        return upperCase(name.substring(0, 1)) + name.substring(1);
    }

    public static void main(String[] args) {
        String out = " hello word! String ";
        Object out1 = " hello word!  Object ";

        System.out.println("1. 去除空格和空字符串：" + dealNull(out));
        System.out.println("2. 去除空格和空字符串：" + dealNull(out1));
        System.out.println("3. 对象转字符串：" + getStr(out1));
        System.out.println("4. 截取字符串：" + getSubString(out, 36));
        System.out.println("5. 字符串转换为 boolean：" + booleanValue("Y"));
        System.out.println("6. 字符串是否有长度:" + hasLength(out));
        System.out.println("7. 去除字符串所有空格:" + trimAllWhitespace(getStr(out1)));
        System.out.println("8. 获取UUID：" + getUUID());
        System.out.println("9. 首字母小写：" + firstLetterLowercase("VFFFFF"));
        System.out.println("10. 首字母大写：" + firstLetterUppercase("vFFFFF"));

    }

    /**
     * 替换特殊字符，以保证JSON解析无误
     * @param str
     * @return
     */
    public static String strReplaceAll(String str){
        if(isNotEmpty(str)){
            str = str.replaceAll("[\\t\\n\\r\"\'\\\\]", "");
        }
        return str;
    }

    /**
     * 用某个字符串替换掉字符串中子字符串，并支持替换特殊字符
     * @author Jef
     * @date 2019/3/26
     * @param str 替换之前的字符串
     * @param regex 需要被替换的字符串
     * @param replacement 用于替换的字符串
     * @param replaceSpecialCharacters 是否替换特殊字符
     * @return java.lang.String
     */
    public static String replaceAll(String str, String regex, String replacement, boolean replaceSpecialCharacters) {
        str = replace(str, regex, replacement);
        if (replaceSpecialCharacters) {
            str = strReplaceAll(str);
        }
        return str;
    }

    /**
     * 用某个字符串替换掉字符串中子字符串
     * @author Jef
     * @date 2019/3/26
     * @param str 替换之前的字符串
     * @param regex 需要被替换的字符串
     * @param replacement 用于替换的字符串
     */
    public static String replace(String str, String regex, String replacement) {
        if (str == null) {
            return null;
        }
        return str.replaceAll(regex, replacement);
    }

    /**
     * 删除以 split 字符分割的字符串中的子串，最终还是能形成正确的格式
     * @author Jef
     * @date 2019/5/10
     * @param beforeStr 未删除子串前
     * @param deleteStr 删除的子串
     * @param split 分割的字符
     * @return java.lang.String
     */
    public static String deleteStr(String beforeStr, String deleteStr, String split)  {
        if (StringUtils.isNotEmpty(beforeStr) && StringUtils.isNotEmpty(deleteStr)) {
            // "abc,def,ghi", 三种情况，移除abc,移除def,移除ghi
            String deleteStrOne = deleteStr + split, deleteStrTwo = split + deleteStr, deleteStrThree = split + deleteStr + split;
            if (beforeStr.contains(deleteStrOne) && !beforeStr.contains(deleteStrTwo)) {
                beforeStr = beforeStr.replace(deleteStrOne, "");
            } else if (beforeStr.contains(deleteStrThree)) {
                beforeStr = beforeStr.replace(deleteStrTwo, "");
            } else if (beforeStr.contains(deleteStrTwo) && !beforeStr.contains(deleteStrOne)) {
                beforeStr = beforeStr.replace(deleteStrTwo, "");
            } else {
                beforeStr = beforeStr.replace(deleteStr, "");
            }
        }
        return beforeStr;
    }

    /**
     * 删除以,分割的字符串中的子串，最终还是能形成正确的格式
     * @author Jef
     * @date 2019/5/10
     * @param beforeStr 未删除子串前
     * @param deleteStr 删除的子串
     * @return java.lang.String
     */
    public static String deleteStr(String beforeStr, String deleteStr)  {
        return deleteStr(beforeStr, deleteStr, ",");
    }

    /**
     * 是否为空
     * @param obj 对象
     * @return
     */
    @SuppressWarnings( "unchecked" )
    public static boolean isEmpty(Object obj) {
        if (obj instanceof String) {
            return isEmpty((String) obj);
        } else if (obj instanceof Number) {
            Number num = (Number) obj;
            BigDecimal tempNum = new BigDecimal(num.toString());

            // 数字0值作empty处理
            return (0 == BigDecimal.ZERO.compareTo(tempNum));
        } else if (obj instanceof Map) {
            Map map = (Map) obj;
            return map.size() <= 0;
        } else if (obj instanceof Collection) {
            Collection c = (Collection) obj;
            return c.size() <= 0;
        } else {
            return obj == null ? true : isEmpty(obj.toString());
        }
    }

    /**
     * 判断字符串中的字符是否都是中文
     * @param str 字符串
     */
    public static boolean isChineseV2(String str) {
        return str.matches("[\u4E00-\u9FA5]+");
    }

    /**
     * 是否是英文
     * @param str 字符串
     * @return 是否是英文
     */
    public static boolean isEnglish(String str){
        return str.matches("^[a-zA-Z]*");

    }

    /**
     * 判断字符串是否仅含有数字和字母
     * @author Jef
     * @date 2019/5/23
     * @param str 字符串
     * @return boolean
     */
    public static boolean isLetterOrDigit(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return str.matches(LETTERORDIGIT_PATTERN);
    }

    /**
     * 拼接变更内容
     */
    public static void getAddChangeInfo(StringBuilder sb, String columName, Object beforeValueObj, Object afterValueObj) {
        String beforeValue, afterValue = "";
        if (beforeValueObj instanceof BigDecimal) {
            BigDecimal beforeValueDecimal = NumberUtils.toBigDecimal(beforeValueObj, 2), afterValueDecimal = NumberUtils.toBigDecimal(afterValueObj, 2);
            beforeValue = String.valueOf(beforeValueDecimal);
            afterValue = String.valueOf(afterValueDecimal);
        } else if (beforeValueObj instanceof Date) {
            Date beforeValueDate = (Date) beforeValueObj;
            beforeValue = DateTimeUtil.formatDate(beforeValueDate);
        } else {
            beforeValue = StringUtils.getStringByDefault(String.valueOf(beforeValueObj));
            afterValue = StringUtils.getStringByDefault(String.valueOf(afterValueObj));
        }
        if (afterValueObj instanceof Date) {
            Date afterValueDate = (Date) afterValueObj;
            afterValue = DateTimeUtil.formatDate(afterValueDate);
        }
        if (!beforeValue.equals(afterValue)) {
            sb.append(columName + ":" + beforeValue + "->" + afterValue + ";");
        }
    }

    /**
     * 隐藏手机号码
     * @author Jef
     * @date 2020/3/25
     * @param phone
     * @return java.lang.String
     */
    public static String hidePhone(String phone) {
        if (isNotEmpty(phone)) {
            phone = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            if (!phone.contains("*")) {
                if (phone.length() >= 7) {
                    return phone.substring(0, 3) + "****" + phone.substring(7);
                } else if (phone.length() > 4) {
                    return phone.substring(0, phone.length() - 4) + "****";
                }
            }
        }
        return phone;
    }

    /**
     * 获取测试使用集合，用于通用功能测试
     * @author Jef
     * @date 2020/3/25
     * @param
     * @return java.util.List<java.lang.String>
     */
    public static List<String> getTestStringList() {
        return getListFromString("alligator,crocodile,test3,test4,test5,ZEBRA");
    }


    /**
     * 获取测试使用集合，用于通用功能测试
     * @author Jef
     * @date 2020/3/25
     * @param
     * @return java.util.List<java.lang.String>
     */
    public static List<String> getTestStringListV2() {
        return getListFromString("test3,test4,test5,test6");
    }

    /**
     * 获取两个集合的交集
     * @author Jef
     * @date 2020/4/7
     * @param strList 集合1
     * @param strListOther 集合2
     * @return java.util.List<java.lang.String>
     */
    public static List<String> getAfterRetainAllList(List<String> strList, List<String> strListOther) {
        List<String> retainList = Lists.newArrayList();
        retainList.addAll(strList);
        retainList.retainAll(strListOther);
        return retainList;
    }

    /**
     * 获取两个集合的差集
     * @author Jef
     * @date 2020/4/7
     * @param strList 集合1
     * @param strListOther 集合2
     * @return java.util.List<java.lang.String>
     */
    public static List<String> getAfterRemoveAllList(List<String> strList, List<String> strListOther) {
        List<String> removeList = Lists.newArrayList();
        removeList.addAll(strList);
        removeList.removeAll(strListOther);
        return removeList;
    }

    /**
     * 获取两个集合的差集
     * @author Jef
     * @date 2020/4/7
     * @param strList 集合1
     * @param strListOther 集合2
     * @return java.util.List<java.lang.String>
     */
    public static List<String> getAfterRemoveAllListV2(List<String> strList, List<String> strListOther) {
        List<String> retainList = getAfterRetainAllList(strList, strListOther);
        List<String> removeList = Lists.newArrayList();
        removeList.addAll(strList);
        removeList.removeAll(retainList);
        return removeList;
    }

    /**
     * 从字符串集合中抽离出实际需要的数据
     * @author Jef
     * @date 2020/5/2
     * @param strList
     * @return void
     */
    public static void soutValueAndIDBySplit(List<String> strList) {
        for (String name : strList) {
            String allMessage = "";
            List<String> idNameList = StringUtils.getListFromString(name);
            for (int i = 0; i < idNameList.size(); i++) {
                String idName = idNameList.get(i);
                if (i == idNameList.size() - 1) {
                    if (StringUtils.isNotEmpty(allMessage)) {
                        allMessage += ",";
                    }
                    allMessage += idName.substring(0, 32);
                    allMessage += " " + idName.substring(idName.length() - 32, idName.length());
                } else {
                    List<String> idList = StringUtils.getListFromString(idName, "_");
                    if (StringUtils.isNotEmpty(allMessage)) {
                        allMessage += ",";
                    }
                    allMessage += idList.get(0);
                }
            }
            System.out.println(allMessage);
        }
    }

    /**
     * 打印字符串
     */
    public static void printString(String s) {
        System.out.println("字符串=" + s);
    }

}
