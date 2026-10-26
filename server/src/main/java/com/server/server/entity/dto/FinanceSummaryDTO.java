package com.server.server.entity.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 财务概览数据传输对象
 */
@Data
public class FinanceSummaryDTO {
    /**
     * 总收入
     */
    private BigDecimal totalIncome = BigDecimal.ZERO;
    
    /**
     * 总支出
     */
    private BigDecimal totalExpense = BigDecimal.ZERO;
    
    /**
     * 净利润 (收入 - 支出)
     */
    private BigDecimal balance = BigDecimal.ZERO;
}
