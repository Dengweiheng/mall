package com.server.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体类 (erp_product)
 */
@Data
@TableName("erp_product")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商品SKU编码
     */
    private String sku;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 所属分类ID
     */
    private Long categoryId;

    /**
     * 分类名称快照
     */
    private String category;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 库存数量
     */
    private Integer stock;

    /**
     * 商品图片
     */
    private String image;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 状态: 1上架, 0下架
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
