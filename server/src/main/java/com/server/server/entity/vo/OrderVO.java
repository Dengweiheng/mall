package com.server.server.entity.vo;

import com.server.server.entity.Order;
import com.server.server.entity.OrderItem;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

/**
 * 订单详细信息视图对象 (VO)
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderVO extends Order {
    private static final long serialVersionUID = 1L;

    /**
     * 订单中的商品明细列表
     */
    private List<OrderItem> items;

    /**
     * 下单人用户名 (方便前端展示)
     */
    private String username;
}
