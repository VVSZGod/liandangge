package com.jiamian.translation.common.exception;

/**
 * @author zhou
 * @date 2020/7/28
 */
public class PasswordException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PasswordException() {

    }

    public PasswordException(String message) {
        super(message);
    }

    public PasswordException(String message, Throwable cause) {
        super(message, cause);
    }

}
