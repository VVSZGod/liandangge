package com.jiamian.translation.common.exception;

/**
 * 该业务异常不用打印
 */
public class BizUnshowException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BizUnshowException(String message) {
        super(message);
    }

    public BizUnshowException(String message, Throwable cause) {
        super(message, cause);
    }

}
