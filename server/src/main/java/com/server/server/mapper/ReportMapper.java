package com.server.server.mapper;

import com.server.server.entity.dto.SalesTrendDTO;
import com.server.server.entity.dto.ProductRankingDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface ReportMapper {

    /**
     * 最近X天的销售额趋势
     */
    List<SalesTrendDTO> getSalesTrend(@Param("days") Integer days);

    /**
     * 商品销售数量排行榜 (Top 10)
     */
    List<ProductRankingDTO> getProductSalesRanking();

    /**
     * 各商品分类的销售额占比
     */
    List<Map<String, Object>> getCategorySalesDistribution();
}
