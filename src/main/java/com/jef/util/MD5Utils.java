package com.jef.util;

import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Jef
 * @create 2018/7/13 10:29
 */
public class MD5Utils {
    /**
     * 获取MD5加密后的秘钥
     *
     * @param encData 需要加密的字符
     * @return
     */
    public static String getMD5Code(String encData) {
        if (StringUtils.isEmpty(encData)) {
            return null;
        }
        try {
            String data = encData;
            // 加密后的字符串
            MessageDigest md5 = null;
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (Exception e) {
                e.printStackTrace();
            }
            byte[] byteArray = data.getBytes("UTF-8");
            byte[] md5Bytes = md5.digest(byteArray);
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 验证是否是MD5加密密文
     *
     * @param encData    需要比对的明文
     * @param encryptStr 需要比对的密文
     * @return
     */
    public static boolean validateMD5Code(String encData, String encryptStr) {
        if (StringUtils.isEmpty(encData) || StringUtils.isEmpty(encryptStr)) {
            return false;
        }
        String entrCode = getMD5Code(encData);
        if (entrCode.equalsIgnoreCase(encryptStr)) {
            return true;
        }
        return false;
    }

    /**
     * MD5加密
     *
     * @param str
     * @return
     */
    public static String crypt(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes("UTF-8"));
            byte[] hash = md.digest();
            for (int i = 0; i < hash.length; i++) {
                if ((0xff & hash[i]) < 0x10) {
                    hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
                } else {
                    hexString.append(Integer.toHexString(0xFF & hash[i]));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return hexString.toString();
    }
}
