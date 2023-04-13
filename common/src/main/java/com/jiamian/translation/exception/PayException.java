package com.jiamian.translation.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author DingGuangHui
 * @date 2022/11/10
 */
public class PayException extends RuntimeException {
	private static final Logger logger = LoggerFactory
			.getLogger(PayException.class);
	private static final long serialVersionUID = 1L;

	public PayException(String message) {
		super(message);
	}

	public PayException(String message, Throwable cause) {
		super(message, cause);
	}

}
