package com.server.server.service;

import com.server.server.entity.dto.HomeDataDTO;

public interface HomeService {

    /**
     * 获取首页统计指标
     */
    HomeDataDTO getHomeStatistics();
}
