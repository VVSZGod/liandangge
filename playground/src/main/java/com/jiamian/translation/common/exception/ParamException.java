package com.jiamian.translation.common.exception;

public class ParamException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ParamException() {
    }

    public ParamException(String message) {
        super(message);
    }

    public ParamException(String message, Throwable cause) {
        super(message, cause);
    }

}
