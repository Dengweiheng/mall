package com.server.server.common;

import lombok.Data;
import java.io.Serializable;

/**
 * 通用返回结果类
 */
@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 状态码 (200: 成功, 其他: 失败)
     */
    private int code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 数据载荷
     */
    private T data;

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = 200;
        result.msg = "操作成功";
        result.data = data;
        return result;
    }

    public static <T> Result<T> error(String msg) {
        return error(500, msg);
    }

    public static <T> Result<T> error(int code, String msg) {
        Result<T> result = new Result<>();
        result.code = code;
        result.msg = msg;
        return result;
    }
}
