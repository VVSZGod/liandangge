package com.jiamian.translation.exception;

public enum ErrorCodeEnum {
	// 普通 业务异常
	ERROR_BIZ(42000, BOException.class, "Business Exception", true),

	// 该异常不打印
	ERROR_BIZ_UNSHOW(43000, BizUnshowException.class,
			"Business Exception unshow", true),
	// 该异常会通知
	ERROR_BIZ_EMAIL_ADMIN(44000, BONotificationException.class,
			"Business Exception, need email notification", true),
	// 授权异常
	ERROR_AUTHORIZATION(46000, AuthorizationException.class, "请登录!", true),
	// 位置系统异常
	ERROR_UNKNOW(49000, Exception.class, "未知错误!", false), ERROR_BLACK(50000,
			BlackException.class, "违规被封禁", true),
	// 参数异常
	ERROR_PARAM(51000, ParamException.class, "参数异常!", true),
	// 积分异常
	ERROR_POINTS(52000, PointsException.class, "积分不足!", true), ERROR_PAY(53000,
			PayException.class, "充值异常", true);

	/**
	 * 错误码
	 */
	private int code;

	/**
	 * 异常类.
	 */
	private Class<?> clazz;

	/**
	 * 简要错误信息.
	 */
	private String msg;

	/**
	 * 是否显示详细的错误信息.
	 */
	private boolean isShowError;

	ErrorCodeEnum(int code, Class<?> clazz, String msg, boolean isShowError) {
		this.code = code;
		this.clazz = clazz;
		this.msg = msg;
		this.isShowError = isShowError;
	}

	public static ErrorCodeEnum getByClass(Class<?> clazz) {
		for (ErrorCodeEnum errorCodeEnum : ErrorCodeEnum.values()) {
			if (errorCodeEnum.getClazz().equals(clazz)) {
				return errorCodeEnum;
			}
		}
		return ErrorCodeEnum.ERROR_UNKNOW;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isShowError() {
		return isShowError;
	}

	public void setShowError(boolean isShowError) {
		this.isShowError = isShowError;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}