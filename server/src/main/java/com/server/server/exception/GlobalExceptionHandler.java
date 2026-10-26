package com.server.server.exception;

import com.server.server.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 基础异常
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return Result.error(500, "服务器异常，请联系管理员");
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(CustomException.class)
    public Result<?> handleCustomException(CustomException e) {
        log.error(e.getMessage(), e);
        return Result.error(e.getCode(), e.getMessage());
    }
}
