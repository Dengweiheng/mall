package com.server.server.entity.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 首页统计数据 DTO
 */
@Data
public class HomeDataDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 今日订单数
     */
    private Long todayOrders;

    /**
     * 今日销售额
     */
    private BigDecimal todaySales;

    /**
     * 我的待处理订单 (待支付或待发货)
     */
    private Long pendingOrders;

    /**
     * 销售额趋势 (最近7天)
     */
    private java.util.List<SalesTrendDTO> salesTrend;

    /**
     * 最新动态 (最近10条订单记录等)
     */
    private java.util.List<String> activities;
}
