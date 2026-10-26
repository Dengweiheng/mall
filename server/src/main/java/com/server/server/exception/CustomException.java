package com.server.server.exception;

/**
 * 自定义业务异常
 */
public class CustomException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private Integer code;

    public CustomException(String message) {
        super(message);
        this.code = 500;
    }

    public CustomException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
