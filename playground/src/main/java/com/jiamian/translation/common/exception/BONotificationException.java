package com.jiamian.translation.common.exception;

public class BONotificationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BONotificationException(String message) {
		super(message);
	}

	public BONotificationException(String message, Throwable cause) {
		super(message, cause);
	}

}
