package com.server.server.entity.dto;

import lombok.Data;

/**
 * 商品销售排行数据
 */
@Data
public class ProductRankingDTO {
    private String name; // 商品名称
    private Integer quantity; // 销售数量
}
