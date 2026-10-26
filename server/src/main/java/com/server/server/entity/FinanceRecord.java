package com.server.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 财务记录实体类 (erp_finance)
 */
@Data
@TableName("erp_finance")
public class FinanceRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 财务记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 类型: 1收入, 2支出
     */
    private Integer type;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 关联的订单ID(可为空)
     */
    private Long relatedOrderId;

    /**
     * 财务备注说明
     */
    private String remark;

    /**
     * 操作人ID (录入该记录的人)
     */
    private Long operatorId;

    /**
     * 记录时间
     */
    private LocalDateTime createTime;
}
