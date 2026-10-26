package com.server.server.service;

import com.server.server.entity.dto.SalesTrendDTO;
import com.server.server.entity.dto.ProductRankingDTO;
import java.util.List;
import java.util.Map;

public interface ReportService {
    
    /**
     * 获取销售趋势 (默认最近7天)
     */
    List<SalesTrendDTO> getSalesTrend(Integer days);

    /**
     * 获取热销排行 (Top 10)
     */
    List<ProductRankingDTO> getProductRanking();

    /**
     * 获取品类分布
     */
    List<Map<String, Object>> getCategoryDistribution();
}
