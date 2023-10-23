package com.jef.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author Jef
 * @date 2019/3/27
 */
public class REStringUtil {
    public static final String DEFAULT_CHARTSET = "UTF-8";

    /**
     * 针对前台界面传递的参数进行解码，防止乱码产生
     * @author Jef
     * @param srcData
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
        //
        return value.trim();
    }

    /**
     * 针对前台界面传递的参数进行解码，防止乱码产生
     * @author Jef
     * @param srcData 需要进行解码的数据
     * @param chartType 解码类型
     * @return
     */
    public static String decodeURLCharset(String srcData,String chartType){
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
}