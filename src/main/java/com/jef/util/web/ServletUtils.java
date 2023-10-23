/**
 * Copyright (c) 2008-2011 yeyaomai.net
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * <p>
 * $Id: ServletUtils.java 1211 2011-11-25 16:20:45Z gao_wei $
 */
package com.jef.util.web;

import com.google.common.net.HttpHeaders;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * Http与Servlet工具类.
 *
 * @author gao_wei
 */
@SuppressWarnings("all")
public class ServletUtils {

	/**
	 * HTTP 默认端口 80
	 */
	public static final int port = 80;

	// -- Content Type 定义 --//
	public static final String TEXT_TYPE = "text/plain";
	public static final String JSON_TYPE = "application/json";
	public static final String XML_TYPE = "text/xml";
	public static final String HTML_TYPE = "text/html";
	public static final String JS_TYPE = "text/javascript";
	public static final String EXCEL_TYPE = "application/vnd.ms-excel";

	// -- Header 定义 --//
	public static final String AUTHENTICATION_HEADER = "Authorization";

	// -- 常用数值定义 --//
	public static final long ONE_YEAR_SECONDS = 60 * 60 * 24 * 365;

	/**
	 * 设置客户端缓存过期时间 的Header.
	 */
	public static void setExpiresHeader(HttpServletResponse response,
										long expiresSeconds) {
		// Http 1.0 header
		response.setDateHeader(HttpHeaders.EXPIRES, System.currentTimeMillis()
				+ expiresSeconds * 1000);
		// Http 1.1 header
		response.setHeader(HttpHeaders.CACHE_CONTROL, "private, max-age="
				+ expiresSeconds);
	}

	/**
	 * 设置禁止客户端缓存的Header.
	 */
	public static void setDisableCacheHeader(HttpServletResponse response) {
		// Http 1.0 header
		response.setDateHeader(HttpHeaders.EXPIRES, 1L);
		response.addHeader(HttpHeaders.PRAGMA, "no-cache");
		// Http 1.1 header
		response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0");
	}

	/**
	 * 设置LastModified Header.
	 */
	public static void setLastModifiedHeader(HttpServletResponse response,
											 long lastModifiedDate) {
		response.setDateHeader(HttpHeaders.LAST_MODIFIED, lastModifiedDate);
	}

	/**
	 * 设置Etag Header.
	 */
	public static void setEtag(HttpServletResponse response, String etag) {
		response.setHeader(HttpHeaders.ETAG, etag);
	}

	/**
	 * 根据浏览器If-Modified-Since Header, 计算文件是否已被修改.
	 * <p>
	 * 如果无修改, checkIfModify返回false ,设置304 not modify status.
	 *
	 * @param lastModified 内容的最后修改时间.
	 */
	public static boolean checkIfModifiedSince(HttpServletRequest request,
											   HttpServletResponse response, long lastModified) {
		long ifModifiedSince = request.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE);
		if ((ifModifiedSince != -1) && (lastModified < ifModifiedSince + 1000)) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return false;
		}
		return true;
	}

	/**
	 * 根据浏览器 If-None-Match Header, 计算Etag是否已无效.
	 * <p>
	 * 如果Etag有效, checkIfNoneMatch返回false, 设置304 not modify status.
	 *
	 * @param etag 内容的ETag.
	 */
	public static boolean checkIfNoneMatchEtag(HttpServletRequest request,
											   HttpServletResponse response, String etag) {
		String headerValue = request.getHeader(HttpHeaders.IF_NONE_MATCH);
		if (headerValue != null) {
			boolean conditionSatisfied = false;
			if (!"*".equals(headerValue)) {
				StringTokenizer commaTokenizer = new StringTokenizer(
						headerValue, ",");

				while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
					String currentToken = commaTokenizer.nextToken();
					if (currentToken.trim().equals(etag)) {
						conditionSatisfied = true;
					}
				}
			} else {
				conditionSatisfied = true;
			}

			if (conditionSatisfied) {
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				response.setHeader(HttpHeaders.ETAG, etag);
				return false;
			}
		}
		return true;
	}

	/**
	 * 设置让浏览器弹出下载对话框的Header.
	 *
	 * @param fileName 下载后的文件名.
	 */
	public static void setFileDownloadHeader(HttpServletResponse response,
											 String fileName) {
		try {
			// 中文文件名支持
			String encodedfileName = new String(fileName.getBytes(),
					"ISO8859-1");
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
					+ encodedfileName + "\"");
		} catch (UnsupportedEncodingException e) {
		}
	}

	/**
	 * 取得带相同前缀的Request Parameters.
	 * <p>
	 * 返回的结果的Parameter名已去除前缀.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getParametersStartingWith(
			ServletRequest request, String prefix) {
		Assert.notNull(request, "Request must not be null");
		Enumeration paramNames = request.getParameterNames();
		Map<String, Object> params = new TreeMap<String, Object>();
		if (prefix == null) {
			prefix = "";
		}
		while (paramNames != null && paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if ("".equals(prefix) || paramName.startsWith(prefix)) {
				String unprefixed = paramName.substring(prefix.length());
				String[] values = request.getParameterValues(paramName);
				if (values == null || values.length == 0) {
					// Do nothing, no values found at all.
				} else if (values.length > 1) {
					params.put(unprefixed, values);
				} else {
					params.put(unprefixed, values[0]);
				}
			}
		}
		return params;
	}

	/**
	 * 组合Parameters生成Query String的Parameter部分, 并在paramter name上加上prefix.
	 *
	 * @see #getParametersStartingWith
	 */
	public static String encodeParameterStringWithPrefix(Map<String, Object> params, String prefix) {
		if ((params == null) || (params.size() == 0)) {
			return "";
		}

		if (prefix == null) {
			prefix = "";
		}

		StringBuilder queryStringBuilder = new StringBuilder();
		Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			if (entry.getKey().equals("offset") || entry.getKey().equals("pageNumber") || entry.getKey().equals("pageSize") || entry.getKey().equals("sortType")) {
				continue;
			}
			queryStringBuilder.append(prefix).append(entry.getKey()).append('=').append(entry.getValue());
			if (it.hasNext()) {
				queryStringBuilder.append('&');
			}
		}
		return queryStringBuilder.toString();
	}

	/**
	 * 添加cookie
	 *
	 * @param response
	 * @param name     cookie的名称
	 * @param value    cookie的值
	 * @param maxAge   cookie存放的时间(以秒为单位,假如存放三天,即3*24*60*60; 如果值为0,cookie将随浏览器关闭而清除)
	 * @throws UnsupportedEncodingException
	 */
	public static void addCookie(HttpServletResponse response, String name,
								 String value, int maxAge) {
		if (value != null) {
			try {
				value = URLEncoder.encode(value, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Cookie cookie = new Cookie(name, value);
		// 设置cookie在根目录下所有的路径都启作用
		cookie.setPath("/");
		// 设置cookie的过期时间
		if (maxAge > 0)
			cookie.setMaxAge(maxAge);
		// 添加cookie
		response.addCookie(cookie);
	}

	/**
	 * 添加cookie 将随浏览器关闭而清除
	 *
	 * @param name  cookie的名称
	 * @param value cookie的值
	 */
	public static void addCookie(HttpServletResponse response, String name,
								 String value) {
		addCookie(response, name, value, 0);
	}

	/**
	 * 获取cookie的值
	 *
	 * @param request
	 * @param name    cookie的名称
	 * @return String
	 */
	public static String getCookieVal(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (StringUtils.equals(cookie.getName(), name)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * 得到客户端真实IP地址,防止穿透.如果是代理的也可以得到
	 *
	 * @return
	 */
	public static String getRemortIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (StringUtils.isEmpty(ip)
				|| StringUtils.equalsIgnoreCase(ip, "unknown")) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(ip)
				|| StringUtils.equalsIgnoreCase(ip, "unknown")) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(ip)
				|| StringUtils.equalsIgnoreCase(ip, "unknown")) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 判断客户端浏览器是否为IE
	 *
	 * @return true:是
	 */
	public static boolean isIE(HttpServletRequest request) {
		String agent = request.getHeader("User-Agent");
		return StringUtils.isNotBlank(agent)
				&& agent.toLowerCase(Locale.getDefault()).indexOf("msie") > 0;
	}

	/**
	 * 得到项目的输入地址 比如: http://www.supconit.com 如果不是80端口则返回 :
	 * http://www.supconit:8089.com
	 *
	 * @return String
	 */
	public static String getContextURL(HttpServletRequest request) {
		if (request.getLocalPort() != port) {
			String path = "http://" + request.getLocalName() + ":"
					+ request.getLocalPort() + request.getContextPath();
			return path;
		}
		return "http://" + request.getLocalName() + request.getContextPath();
	}

	/**
	 * 得到项目URL全路径,如：http://localhost:8080/supcon
	 *
	 * @param request
	 * @return String http://localhost:8080/supcon
	 */
	public static String getAppURL(HttpServletRequest request) {
		if (request == null)
			return "";

		StringBuffer url = new StringBuffer();
		int port = request.getServerPort();
		if (port < 0) {
			port = 80; // Work around java.net.URL bug
		}
		String scheme = request.getScheme();
		url.append(scheme);
		url.append("://");
		url.append(request.getServerName());
		if ((scheme.equals("http") && (port != 80))
				|| (scheme.equals("https") && (port != 443))) {
			url.append(':');
			url.append(port);
		}
		url.append(request.getContextPath());
		return url.toString();
	}

	/**
	 * 得到项目的物理地址
	 *
	 * @return C:\Program Files\Tomcat\webapps\jbpm_web_test\
	 */
	public static String getContextPath(HttpServletRequest request) {
		return request.getSession(true).getServletContext().getRealPath("/");
	}

	/**
	 * 判断一个请求是否为ajax请求
	 *
	 * @param request
	 * @return boolean true:是 false:不是
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		String requestedWith = request.getHeader("X-Requested-With");
		return StringUtils.equals(requestedWith, "XMLHttpRequest");
	}

}
