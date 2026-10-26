package com.server.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.server.common.PageResult;
import com.server.server.entity.FinanceRecord;
import com.server.server.entity.dto.FinanceSummaryDTO;

import java.time.LocalDateTime;

public interface FinanceRecordService extends IService<FinanceRecord> {
    
    /**
     * 增加财务记录 (主要是收入/支出)
     */
    void addRecord(Integer type, java.math.BigDecimal amount, Long relatedOrderId, String remark, Long operatorId);

    /**
     * 分页查询财务流水
     */
    PageResult<FinanceRecord> findPage(Integer type, String startTime, String endTime, int pageNum, int pageSize);

    /**
     * 获取财务统计概览
     */
    FinanceSummaryDTO getSummary(String startTime, String endTime);
}
