package com.jiamian.translation.component;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author DingGuangHui
 * @date 2022/11/7
 */
@Component("httpDao")
public class HttpDao {
	public static final String TOKEN = "token";
	private final static String IP_PATTERN = "((?:(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d)\\.){3}(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d))";

	public HttpDao() {
	}

	/**
	 * 获取当前HTTP请求对象
	 *
	 * @return HttpServletRequest对象
	 */
	public HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
	}

	

	/**
	 * 获取用户ip
	 *
	 * @return
	 */
	public String getIpAddr() {
		HttpServletRequest httpServletRequest = getRequest();
		String ip = null;
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			// 优先使用游戏盾给出的ip
			ip = httpServletRequest.getHeader("yc-real-ip");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			String forwardedIp = httpServletRequest
					.getHeader("X-Forwarded-For");
			if (!StringUtils.isEmpty(forwardedIp)) {
				Pattern r = Pattern.compile(IP_PATTERN);
				String[] ipArray = forwardedIp.split(",");
				for (String i : ipArray) {
					Matcher m = r.matcher(i.trim());
					if (m.matches()) {
						ip = i;
						break;
					}
				}
			}
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = httpServletRequest.getHeader("WL-Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = httpServletRequest.getHeader("cdn-src-ip");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = getRequest().getHeader("X-Real-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = getRequest().getRemoteAddr();
		}
		return ip == null ? "" : ip;
	}

}
