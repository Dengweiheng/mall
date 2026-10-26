package com.server.server.controller;

import com.server.server.common.Result;
import com.server.server.entity.dto.ProductRankingDTO;
import com.server.server.entity.dto.SalesTrendDTO;
import com.server.server.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/report")
@CrossOrigin
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 获取销售金额趋势 (图表用)
     */
    @GetMapping("/sales-trend")
    public Result<List<SalesTrendDTO>> getSalesTrend(@RequestParam(defaultValue = "7") Integer days) {
        return Result.success(reportService.getSalesTrend(days));
    }

    /**
     * 获取热销商品排行 (柱状图用)
     */
    @GetMapping("/product-ranking")
    public Result<List<ProductRankingDTO>> getProductRanking() {
        return Result.success(reportService.getProductRanking());
    }

    /**
     * 获取商品分类销售占比 (饼图用)
     */
    @GetMapping("/category-distribution")
    public Result<List<Map<String, Object>>> getCategoryDistribution() {
        return Result.success(reportService.getCategoryDistribution());
    }
}
