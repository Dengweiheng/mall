package com.server.server.mapper;

import org.apache.ibatis.annotations.Mapper;
import java.math.BigDecimal;

@Mapper
public interface HomeMapper {
    /**
     * 今日订单总数
     */
    long countTodayOrders();

    /**
     * 今日销售总额
     */
    BigDecimal sumTodaySales();

    /**
     * 代处理订单数 (待支付、待发货)
     */
    long countPendingOrders();

    /**
     * 获取最近动态 (订单、库存记录等)
     */
    java.util.List<String> getRecentActivities();
}
