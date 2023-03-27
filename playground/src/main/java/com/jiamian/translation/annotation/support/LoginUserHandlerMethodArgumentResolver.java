package com.jiamian.translation.annotation.support;

import com.jiamian.translation.common.constant.CommonConstant;
import com.jiamian.translation.util.UserTokenUtil;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.jiamian.translation.annotation.LoginUser;

public class LoginUserHandlerMethodArgumentResolver
		implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().isAssignableFrom(Long.class)
				&& parameter.hasParameterAnnotation(LoginUser.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer container, NativeWebRequest request,
			WebDataBinderFactory factory) {
		return UserTokenUtil
				.getUserId(request.getHeader(CommonConstant.REQUEST_TOKEN_KEY));
	}
}
