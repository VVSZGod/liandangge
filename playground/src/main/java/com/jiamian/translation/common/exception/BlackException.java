package com.jiamian.translation.common.exception;

/**
 * @author ding
 * @date 2020/8/28
 */
public class BlackException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BlackException(String message) {
        super(message);
    }

    public BlackException(String message, Throwable cause) {
        super(message, cause);
    }
}
