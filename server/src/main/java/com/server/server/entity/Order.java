package com.server.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类 (erp_order)
 */
@Data
@TableName("erp_order")
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订单流水号
     */
    private String orderNo;

    /**
     * 下单用户ID (sys_user的id)
     */
    private Long userId;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 订单状态: 0待支付, 1已支付待发货, 2已发货, 3已完成, 4已取消
     */
    private Integer status;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
