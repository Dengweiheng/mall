package com.server.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 库存变动记录实体类 (erp_stock_record)
 */
@Data
@TableName("erp_stock_record")
public class StockRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品名称 (联表展示使用)
     */
    @TableField(exist = false)
    private String productName;

    /**
     * 变动类型: 1入库, 2出库, 3盘点修正
     */
    private Integer type;

    /**
     * 变动数量
     */
    private Integer changeAmount;

    /**
     * 变动前库存
     */
    private Integer beforeStock;

    /**
     * 变动后库存
     */
    private Integer afterStock;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 记录时间
     */
    private LocalDateTime createTime;
}
