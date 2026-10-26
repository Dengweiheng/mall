package com.server.server.common;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 分页返回结果类
 */
@Data
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 当前页数据内容
     */
    private List<T> list;

    public PageResult(long total, List<T> list) {
        this.total = total;
        this.list = list;
    }
}
