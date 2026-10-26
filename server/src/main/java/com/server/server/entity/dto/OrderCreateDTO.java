package com.server.server.entity.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 创建订单 DTO
 */
@Data
public class OrderCreateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 当前下单用户的ID (如果使用Token其实可以从上下文中获取，这里直接传)
     */
    private Long userId;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 购买的商品明细列表
     */
    private List<OrderItemDTO> items;

    @Data
    public static class OrderItemDTO {
        /**
         * 商品ID
         */
        private Long productId;

        /**
         * 购买数量
         */
        private Integer quantity;
    }
}
