package com.jiamian.translation.util;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.jiamian.translation.exception.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;

public class LoggerUtil {

	private static Logger logger = LoggerFactory.getLogger(LoggerUtil.class);

	private static final List<String> UNREMIND_ERROR = Lists.newArrayList(
			"断开的管道", "ClientAbortException: java.io.IOException",
			"The last packet successfully received",
			"{\"errcode\":40029,\"errmsg\":\"invalid code",
			" {\"errcode\":40163,\"errmsg\":\"code been used",
			"NoHttpResponseException: api.weixin.qq.com:443",
			"{\"errcode\":40001,\"errmsg\":\"invalid credential, access_token is invalid or not latest",
			"bind.MissingServletRequestParameterException");

	/**
	 * 是否是生产环境
	 */
	private static boolean isPro = false;
	private static String projectName = "miplus";

	public static void error(String msg) {
		initLineNumber();
		LoggerFactory.getLogger(getClassName()).error(msg);
		removeLineNumber();
	}

	public static void error(String msg, Exception e) {
		if (isShow(e)) {
			initLineNumber();
			LoggerFactory.getLogger(getClassName()).error(msg, e);
			removeLineNumber();
		}
	}

	/**
	 * 二次封装 目的：1.输入邮件的报错通知内容更直观，2.自动对非基本类型的Object序列化
	 */
	public static void error(String msg, Object... obj) {
		Exception e = getException(obj);
		String errerMsg = arrayFormat(msg, obj, e);
		error(errerMsg, e);
	}

	public static void warn(String msg) {
		initLineNumber();
		LoggerFactory.getLogger(getClassName()).warn(msg);
		removeLineNumber();
	}

	public static void warn(String msg, Object... obj) {
		Exception e = getException(obj);
		warn(arrayFormat(msg, obj, e));
	}

	public static void info(String msg) {
		info(LoggerFactory.getLogger(getClassName()), msg);
	}

	public static void info(Logger logger, String msg) {
		initLineNumber();
		logger.info(msg);
		removeLineNumber();
	}

	public static void info(String msg, Object... obj) {
		info(arrayFormat(msg, obj));
	}

	public static void debug(String msg) {
		initLineNumber();
		LoggerFactory.getLogger(getClassName()).debug(msg);
		removeLineNumber();
	}

	public static void debug(String msg, Object... obj) {
		debug(arrayFormat(msg, obj));
	}

	public static boolean isUnExpectError(Exception e) {
		if (isPro) {
			return !(e instanceof BOException)
					&& !(e instanceof AuthorizationException)
					&& !(e instanceof BizUnshowException)
					&& !(e instanceof PasswordException)
					&& !(e instanceof PhoneException);
		}
		return true;
	}

	private static boolean isNeedEmail(Exception e) {
		if (!isPro) {
			return false;
		}
		if (e == null) {
			return false;
		}
		String stackTrace = ExceptionUtil.getStackTrace(e);
		if (isUnExpectError(e)) {
			for (String errorMsg : UNREMIND_ERROR) {
				if (StringUtils.contains(stackTrace, errorMsg)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	private static boolean isShow(Exception e) {
		if (!isPro) {
			return true;
		}
		if (e == null || e instanceof BizUnshowException) {
			return false;
		}
		return true;
	}

	public static void setIsPro(boolean isPro) {
		LoggerUtil.isPro = isPro;
	}

	public static void setProjectName(String projectName) {
		LoggerUtil.projectName = projectName;
	}

	/**
	 * 获取调用 error,info,debug静态类的类名
	 *
	 * @return
	 */
	private static String getClassName() {
		return new SecurityManager() {
			public String getClassName() {
				return getClassContext()[3].getName();
			}
		}.getClassName();
	}

	/**
	 * 是否为基本数据类型
	 *
	 * @param target
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static boolean isPrimitive(Object target) {
		Class clzz = target.getClass();
		if (Boolean.class.equals(clzz) || Character.class.equals(clzz)
				|| Short.class.equals(clzz) || Integer.class.equals(clzz)
				|| Long.class.equals(clzz) || Float.class.equals(clzz)
				|| Double.class.equals(clzz) || Byte.class.equals(clzz)
				|| String.class.equals(clzz) || clzz.isPrimitive()) {
			return true;
		}
		return false;
	}

	public static final Exception getException(Object[] argArray) {
		if (argArray == null || argArray.length == 0) {
			return null;
		}

		final Object lastEntry = argArray[argArray.length - 1];
		if (lastEntry instanceof Exception) {
			return (Exception) lastEntry;
		}
		return null;
	}


	public static String parseContent(Object object) {
		if (null == object) {
			return null;
		} else if (isPrimitive(object)) {
			return object.toString();
		} else {
			return JSON.toJSONString(object);
		}
	}

	private static final String arrayFormat(String messagePattern,
			Object[] argArray) {
		return arrayFormat(messagePattern, argArray, null);
	}

	private static final String arrayFormat(String messagePattern,
			Object[] argArray, Exception e) {
		try {
			int subIndex = e == null ? 0 : 1;
			if (argArray == null) {
				return messagePattern;
			} else {
				int i = 0;
				StringBuilder sbuf = new StringBuilder(
						messagePattern.length() + 50);

				for (int L = 0; L < argArray.length - subIndex; ++L) {
					int j = messagePattern.indexOf("{}", i);
					if (j == -1) {
						if (i == 0) {
							return messagePattern;
						}

						sbuf.append(messagePattern, i, messagePattern.length());
						return sbuf.toString();
					}

					if (isEscapedDelimeter(messagePattern, j)) {
						if (!isDoubleEscaped(messagePattern, j)) {
							--L;
							sbuf.append(messagePattern, i, j - 1);
							sbuf.append('{');
							i = j + 1;
						} else {
							sbuf.append(messagePattern, i, j - 1);
							sbuf.append(parseContent(argArray[L]));
							i = j + 2;
						}
					} else {
						sbuf.append(messagePattern, i, j);
						sbuf.append(parseContent(argArray[L]));
						i = j + 2;
					}
				}

				sbuf.append(messagePattern, i, messagePattern.length());
				return sbuf.toString();
			}
		} catch (Exception e1) {
			LoggerUtil.error("LoggerUtil arrayFormat fail", e);
		}
		return null;
	}

	static final boolean isEscapedDelimeter(String messagePattern,
			int delimeterStartIndex) {
		if (delimeterStartIndex == 0) {
			return false;
		} else {
			char potentialEscape = messagePattern
					.charAt(delimeterStartIndex - 1);
			return potentialEscape == '\\';
		}
	}

	static final boolean isDoubleEscaped(String messagePattern,
			int delimeterStartIndex) {
		return delimeterStartIndex >= 2
				&& messagePattern.charAt(delimeterStartIndex - 2) == '\\';
	}

	/**
	 * 获取请求信息
	 *
	 * @param request
	 *            请求
	 * @return 请求信息
	 */
	public static String getRequestMsg(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		sb.append("requestUrl：").append(request.getRequestURI()).append("<br>");
		sb.append("requestIp：").append(IpUtil.getIp(request)).append("<br>");
		sb.append("<br>").append("headers：").append("<br>");
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = headerNames.nextElement();
			String value = request.getHeader(key);
			sb.append("--- ").append(key).append("：").append(value)
					.append("<br>");
		}
		sb.append("<br>").append("parameters:").append("<br>");
		Enumeration<String> parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String key = parameterNames.nextElement();
			String value = request.getParameter(key);
			sb.append("--- ").append(key).append("：").append(value)
					.append("<br>");
		}

		return sb.toString();
	}

	public static void AddUnremindError(String err) {
		UNREMIND_ERROR.add(err);
	}

	private static void removeLineNumber() {
		MDC.remove("line");
	}

	private static void initLineNumber() {
		Exception ex = new RuntimeException();
		StackTraceElement[] stackTrace = ex.getStackTrace();
		for (int i = 0; i < stackTrace.length; i++) {
			if (!"me.hxyfj.util.LoggerUtil"
					.equals(stackTrace[i].getClassName())) {
				StackTraceElement element = ex.getStackTrace()[i];
				MDC.put("line", String.format("[%s]", element.getLineNumber()));
				break;
			}
		}
	}

}
