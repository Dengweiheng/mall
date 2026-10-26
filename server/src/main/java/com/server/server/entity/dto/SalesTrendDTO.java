package com.server.server.entity.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 销售额趋势数据
 */
@Data
public class SalesTrendDTO {
    private String date; // 对应日期，如 2024-01-01
    private BigDecimal amount; // 当日销售额
}
