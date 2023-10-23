package com.jef.util.security;

import com.jef.util.HttpClientUtil;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;

/**
 * Base64工具类
 *
 * @author Jef
 * @date 2018/8/11 12:59
 */
public class Base64Util {
    /**
     * 加密
     * @param str
     * @return
     */
    public static String getBase64(String str) {
        byte[] b = null;
        String s = null;
        try {
            b = str.getBytes(HttpClientUtil.CHARSET_UTF);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (b != null) {
            s = new BASE64Encoder().encode(b);
        }
        return s;
    }

    /**
     * 解密
     * @param s
     */
    public static String getFromBase64(String s) {
        String result = null;
        if (s != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                byte[] b = decoder.decodeBuffer(s);
                result = new String(b, HttpClientUtil.CHARSET_UTF);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
