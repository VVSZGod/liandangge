package com.jiamian.translation.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jiamian.translation.common.utils.ServletUtil;
import com.jiamian.translation.constant.CommonConstant;
import com.jiamian.translation.entity.JsonResult;
import com.jiamian.translation.util.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.fusesource.jansi.Ansi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

@Aspect
@Component
public class LogAop {

	private static final Logger logger = LoggerFactory.getLogger(LogAop.class);

	/**
	 * 切点
	 */
	private static final String POINTCUT = "execution(public * com.jiamian.translation.controller..*.*(..))";

	/**
	 * 默认的请求内容类型,表单提交
	 **/
	private static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
	/**
	 * JSON请求内容类型
	 **/
	private static final String APPLICATION_JSON = "application/json";
	/**
	 * GET请求
	 **/
	private static final String GET = "GET";
	/**
	 * POST请求
	 **/
	private static final String POST = "POST";

	private static final List<String> FILTER_URLS = Lists
			.newArrayList("/dailyup/daily/stock/zhishu");

	@Around(POINTCUT)
	public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
		// 获取请求相关信息
		String url = "";
		boolean needLog = false;
		try {
			// 获取当前的HttpServletRequest对象
			HttpServletRequest request = ServletUtil.getRequest();

			Map<String, Object> map = new LinkedHashMap<>();

			// 获取请求类名和方法名称
			Signature signature = joinPoint.getSignature();

			// 获取真实的方法对象
			MethodSignature methodSignature = (MethodSignature) signature;
			Method method = methodSignature.getMethod();

			// 请求全路径
			url = request.getRequestURI();
			needLog = !FILTER_URLS.contains(url);
			map.put("path", url);
			// IP地址
			String ip = IpUtil.getIp(request);
			map.put("ip", ip);
			map.put("serverName", request.getServerName());

			// 获取请求方式
			String requestMethod = request.getMethod();
			map.put("requestMethod", requestMethod);

			// 获取请求内容类型
			String contentType = request.getContentType();
			map.put("contentType", contentType);

			// 判断控制器方法参数中是否有RequestBody注解
			Annotation[][] annotations = method.getParameterAnnotations();
			boolean isRequestBody = isRequestBody(annotations);
			map.put("isRequestBody", isRequestBody);
			// 设置请求参数
			Object requestParamJson = getRequestParamJsonString(joinPoint,
					request, requestMethod, contentType, isRequestBody);
			map.put("param", requestParamJson);
			map.put("time", DateUtil.getDate());

			// 获取请求头token
			map.put("token",
					request.getHeader(CommonConstant.REQUEST_TOKEN_KEY));

			String requestInfo;
			requestInfo = JSON.toJSONString(map);
			// 过滤的请求
			if (needLog) {
				LoggerUtil.info(logger, AnsiUtil.getAnsi(Ansi.Color.GREEN,
						"requestInfo:" + requestInfo, false));
			}
		} catch (Exception e) {
			LoggerUtil.error("处理请求数据异常", e);
		}

		// 执行目标方法,获得返回值
		Object result = joinPoint.proceed();
		try {
			if (result instanceof JsonResult) {
				JsonResult jsonResult = (JsonResult) result;
				String responseResultInfo = "\n"
						+ JSON.toJSONString(jsonResult, false);
				if (needLog) {
					LoggerUtil.info(logger, AnsiUtil.getAnsi(Ansi.Color.BLUE,
							"responseResult:" + responseResultInfo, false));
				}
			}
		} catch (Exception e) {
			LoggerUtil.error("处理响应结果异常", e);
		}
		return result;
	}

	/**
	 * 获取请求参数JSON字符串
	 *
	 * @param joinPoint
	 * @param request
	 * @param requestMethod
	 * @param contentType
	 * @param isRequestBody
	 */
	private Object getRequestParamJsonString(ProceedingJoinPoint joinPoint,
			HttpServletRequest request, String requestMethod,
			String contentType, boolean isRequestBody) {
		/**
		 * 判断请求内容类型 通常有3中请求内容类型 1.发送get请求时,contentType为null
		 * 2.发送post请求时,contentType为application/x-www-form-urlencoded 3.发送post
		 * json请求,contentType为application/json 4.发送post
		 * json请求并有RequestBody注解,contentType为application/json
		 */
		Object paramObject = null;
		int requestType = 0;
		if (GET.equals(requestMethod)) {
			requestType = 1;
		} else if (POST.equals(requestMethod)) {
			if (contentType == null) {
				requestType = 5;
			} else if (contentType
					.startsWith(APPLICATION_X_WWW_FORM_URLENCODED)) {
				requestType = 2;
			} else if (contentType.startsWith(APPLICATION_JSON)) {
				if (isRequestBody) {
					requestType = 4;
				} else {
					requestType = 3;
				}
			}
		}

		// 1,2,3中类型时,获取getParameterMap中所有的值,处理后序列化成JSON字符串
		if (requestType == 1 || requestType == 2 || requestType == 3
				|| requestType == 5) {
			Map<String, String[]> paramsMap = request.getParameterMap();
			paramObject = getJsonForParamMap(paramsMap);
		} else if (requestType == 4) {
			// POST,application/json,RequestBody的类型,简单判断,然后序列化成JSON字符串
			Object[] args = joinPoint.getArgs();
			paramObject = argsArrayToJsonString(args);
		}

		return paramObject;
	}

	/**
	 * 判断控制器方法参数中是否有RequestBody注解
	 *
	 * @param annotations
	 * @return
	 */
	private boolean isRequestBody(Annotation[][] annotations) {
		boolean isRequestBody = false;
		for (Annotation[] annotationArray : annotations) {
			for (Annotation annotation : annotationArray) {
				if (annotation instanceof RequestBody) {
					isRequestBody = true;
				}
			}
		}
		return isRequestBody;
	}

	/**
	 * 请求参数拼装
	 *
	 * @param args
	 * @return
	 */
	private Object argsArrayToJsonString(Object[] args) {
		if (args == null) {
			return null;
		}
		// 去掉HttpServletRequest和HttpServletResponse
		List<Object> realArgs = new ArrayList<>();
		for (Object arg : args) {
			if (arg instanceof HttpServletRequest) {
				continue;
			}
			if (arg instanceof HttpServletResponse) {
				continue;
			}
			if (arg instanceof MultipartFile) {
				continue;
			}
			if (arg instanceof ModelAndView) {
				continue;
			}
			realArgs.add(arg);
		}
		if (realArgs.size() == 1) {
			return realArgs.get(0);
		} else {
			return realArgs;
		}
	}

	/**
	 * 获取参数Map的JSON字符串
	 *
	 * @param paramsMap
	 * @return
	 */
	public JSONObject getJsonForParamMap(Map<String, String[]> paramsMap) {
		int paramSize = paramsMap.size();
		if (paramsMap == null || paramSize == 0) {
			return null;
		}
		JSONObject jsonObject = new JSONObject();
		for (Map.Entry<String, String[]> kv : paramsMap.entrySet()) {
			String key = kv.getKey();
			String[] values = kv.getValue();
			if (values == null) {
				// 没有值
				jsonObject.put(key, null);
			} else if (values.length == 1) {
				// 一个值
				jsonObject.put(key, values[0]);
			} else { // 多个值
				jsonObject.put(key, values);
			}
		}
		return jsonObject;
	}

}
