package com.jiamian.translation.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiamian.translation.entity.dto.IpAddressDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

public class IpUtil {
	private static final Logger logger = LoggerFactory.getLogger(IpUtil.class);

	public static String getIp(HttpServletRequest request) {
		return request.getHeader("X-Real-IP");
	}

	/**
	 * @param ip
	 * @return
	 */
	public static IpAddressDTO getAddressByIp(String ip) {
		try {
			String result = null;
			try {
				result = getJsonContent(
						"http://ip.taobao.com/service/getIpInfo.php?ip=" + ip);
			} catch (Exception e) {
				try {
					result = getJsonContent(
							"http://ip.taobao.com/service/getIpInfo.php?ip="
									+ ip);
				} catch (Exception e1) {
					LoggerUtil.error("getAddressByIp-getJsonContent fail", e1);
				}
			}
			JSONObject jsonObject = JSON.parseObject(result);
			if (jsonObject == null) {
				return null;
			}
			int code = jsonObject.getInteger("code");
			if (code == 0) {
				return jsonObject.getObject("data", IpAddressDTO.class);
			}
		} catch (Exception e) {
			LoggerUtil.error("getAddressByIp fail", e);
		}
		return null;
	}

	public static String getJsonContent(String urlStr) throws Exception {
		// 获取HttpURLConnection连接对象
		URL url = new URL(urlStr);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		// 设置连接属性
		httpConn.setConnectTimeout(3 * 1000);
		httpConn.setReadTimeout(3 * 1000);
		httpConn.setDoInput(true);
		httpConn.setRequestMethod("GET");
		// 获取相应码
		int respCode = httpConn.getResponseCode();
		if (respCode == 200) {
			StringBuilder builder = new StringBuilder();
			BufferedReader br = new BufferedReader(
					new InputStreamReader(httpConn.getInputStream(), "utf-8"));
			for (String s = br.readLine(); s != null; s = br.readLine()) {
				builder.append(s);
			}
			br.close();
			return builder.toString();
		}
		return null;
	}

	public static String getIpAddr(HttpServletRequest request) {
		String ipAddress;
		try {
			ipAddress = request.getHeader("x-forwarded-for");
			if (ipAddress == null || ipAddress.length() == 0
					|| "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getHeader("Proxy-Client-IP");
			}
			if (ipAddress == null || ipAddress.length() == 0
					|| "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ipAddress == null || ipAddress.length() == 0
					|| "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getRemoteAddr();
				if (ipAddress.equals("127.0.0.1")) {
					// 根据网卡取本机配置的IP
					InetAddress inet = null;
					try {
						inet = InetAddress.getLocalHost();
					} catch (UnknownHostException e) {
						logger.error(e.getMessage(), e);
					}
					ipAddress = inet.getHostAddress();
				}
			}
			// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
			if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
				// = 15
				if (ipAddress.indexOf(",") > 0) {
					ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
				}
			}
		} catch (Exception e) {
			ipAddress = "";
		}

		return ipAddress;
	}

}
