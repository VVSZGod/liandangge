package com.jiamian.translation.exception;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.jiamian.translation.entity.JsonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.jiamian.translation.util.LoggerUtil;

@ControllerAdvice(basePackages = { "com.jiamian.translation.controller" })
public class GlobalExceptionHandler {

	@ExceptionHandler
	@ResponseBody
	public JsonResult handleException(HttpServletRequest request, Exception e) {
		e.printStackTrace();

		if (e instanceof MethodArgumentTypeMismatchException) {
			return JsonResult.errorResult("异常错误，待会在重新试试吧~");
		}

		if (e instanceof MethodArgumentNotValidException) {
			BindingResult result = ((MethodArgumentNotValidException) e)
					.getBindingResult();
			StringBuilder errorMsg = new StringBuilder();

			if (result.hasErrors()) {
				List<FieldError> fieldErrors = result.getFieldErrors();
				fieldErrors.forEach(error -> {
					errorMsg.append(error.getDefaultMessage()).append("!");
				});
			}
			return JsonResult.errorResult(errorMsg.toString());
		}
		if (LoggerUtil.isUnExpectError(e)) {
			LoggerUtil.error("Global API Exception Handle", e);
		}

		ErrorCodeEnum errorCodeEnum = ErrorCodeEnum.getByClass(e.getClass());
		if (errorCodeEnum.isShowError()) {
			return JsonResult.errorResult(
					StringUtils.isNotBlank(e.getMessage()) ? e.getMessage()
							: errorCodeEnum.getMsg(),
					errorCodeEnum);
		} else {
			return JsonResult.errorResult(errorCodeEnum.getMsg(),
					errorCodeEnum);
		}
	}
}