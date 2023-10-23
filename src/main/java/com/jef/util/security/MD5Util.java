package com.jef.util.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * 转换字节数组为16进制字串
	 * 
	 * @param b
	 *            字节数组
	 * @return 16进制字串
	 */
	public static String byteArrayToString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));// 若使用本函数转换则可得到加密结果的16进制表示，即数字字母混合的形式
			// resultSb.append(byteToNumString(b[i]));//使用本函数则返回加密结果的10进制数字字串，即全数字形式
		}
		return resultSb.toString();
	}

	private static String byteToNumString(byte b) {
		int _b = b;
		if (_b < 0) {
			_b = 256 + _b;
		}
		return String.valueOf(_b);
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String encode(String origin) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToString(md.digest(resultString.getBytes()));
			// resultString = new String(md.digest(resultString.getBytes()),"UTF-8");
		} catch (Exception ex) {
		}
		return resultString;
	}

	/**
	 * 将content做MD5加密，返回加密后的数组
	 *
	 * @param content
	 * @param encode
	 * @return
	 */
	public static byte[] encode(String content, String encode) {

		encode = encode == null ? "UTF-8" : encode;

		byte[] md = null;

		try {

			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(content.getBytes(encode));
			md = md5.digest();
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}

		return md;
	}


	public static void main(String[] args) {
		// 注册时密码加密
		System.out.println(MD5Util.encode("123456"));
		// 登录验证密码
		System.out.println(MD5Util.encode("admin").equals(
				"ac83f4dd7a2b30d56d5a9b2abd3e1a32"));
	}
}
