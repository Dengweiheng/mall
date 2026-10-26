package com.server.server.service.impl;

import com.server.server.entity.dto.HomeDataDTO;
import com.server.server.mapper.HomeMapper;
import com.server.server.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private HomeMapper homeMapper;

    @Autowired
    private com.server.server.mapper.ReportMapper reportMapper;

    @Override
    public HomeDataDTO getHomeStatistics() {
        HomeDataDTO dto = new HomeDataDTO();
        dto.setTodayOrders(homeMapper.countTodayOrders());
        dto.setTodaySales(homeMapper.sumTodaySales());
        dto.setPendingOrders(homeMapper.countPendingOrders());
        dto.setActivities(homeMapper.getRecentActivities());
        
        // 获取最近7天的趋势图数据
        dto.setSalesTrend(reportMapper.getSalesTrend(7));
        
        return dto;
    }
}
