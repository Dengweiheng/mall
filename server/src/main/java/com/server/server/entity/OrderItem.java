package com.server.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单明细实体类 (erp_order_item)
 */
@Data
@TableName("erp_order_item")
public class OrderItem implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 明细ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 归属订单ID
     */
    private Long orderId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品名称快照
     */
    private String productName;

    /**
     * 商品单价快照
     */
    private BigDecimal productPrice;

    /**
     * 购买数量
     */
    private Integer quantity;

    /**
     * 明细总金额
     */
    private BigDecimal totalPrice;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
