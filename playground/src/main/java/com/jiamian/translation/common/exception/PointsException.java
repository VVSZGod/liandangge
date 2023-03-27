package com.jiamian.translation.common.exception;

public class PointsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PointsException(String message) {
		super(message);
	}

	public PointsException(String message, Throwable cause) {
		super(message, cause);
	}
}
