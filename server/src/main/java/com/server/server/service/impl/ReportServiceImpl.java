package com.server.server.service.impl;

import com.server.server.entity.dto.SalesTrendDTO;
import com.server.server.entity.dto.ProductRankingDTO;
import com.server.server.mapper.ReportMapper;
import com.server.server.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Override
    public List<SalesTrendDTO> getSalesTrend(Integer days) {
        if (days == null) days = 7;
        return reportMapper.getSalesTrend(days);
    }

    @Override
    public List<ProductRankingDTO> getProductRanking() {
        return reportMapper.getProductSalesRanking();
    }

    @Override
    public List<Map<String, Object>> getCategoryDistribution() {
        return reportMapper.getCategorySalesDistribution();
    }
}
