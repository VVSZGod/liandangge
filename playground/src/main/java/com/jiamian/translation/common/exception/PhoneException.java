package com.jiamian.translation.common.exception;

/**
 * 业务异常
 */
public class PhoneException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PhoneException() {
    }

    public PhoneException(String message) {
        super(message);
    }

    public PhoneException(String message, Throwable cause) {
        super(message, cause);
    }

}
