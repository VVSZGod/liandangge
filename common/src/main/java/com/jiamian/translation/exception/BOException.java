package com.jiamian.translation.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 业务异常
 */
public class BOException extends RuntimeException {
	private static final Logger logger = LoggerFactory
			.getLogger(BOException.class);
	private static final long serialVersionUID = 1L;

	public BOException(String message) {
		super(message);
	}

	public BOException(String message, Throwable cause) {
		super(message, cause);
	}

}
