package com.server.server.controller;

import com.server.server.common.PageResult;
import com.server.server.common.Result;
import com.server.server.entity.FinanceRecord;
import com.server.server.entity.dto.FinanceSummaryDTO;
import com.server.server.service.FinanceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/finance")
@CrossOrigin
public class FinanceRecordController {

    @Autowired
    private FinanceRecordService financeRecordService;

    /**
     * 获取财务报表明细 (支持日期筛选)
     */
    @GetMapping("/page")
    public Result<PageResult<FinanceRecord>> findPage(
            @RequestParam(required = false) Integer type, // 1收入, 2支出
            @RequestParam(required = false) String startTime, // 格式: yyyy-MM-dd HH:mm:ss
            @RequestParam(required = false) String endTime,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        PageResult<FinanceRecord> pageInfo = financeRecordService.findPage(type, startTime, endTime, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    /**
     * 获取财务统计概览 (总利润, 总收入, 总支出)
     */
    @GetMapping("/summary")
    public Result<FinanceSummaryDTO> getSummary(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        return Result.success(financeRecordService.getSummary(startTime, endTime));
    }

    /**
     * 手工录入一笔财务（比如办公支出、水电费等）
     */
    @PostMapping
    public Result<Void> record(@RequestBody FinanceRecord financeRecord) {
        financeRecordService.addRecord(
                financeRecord.getType(),
                financeRecord.getAmount(),
                financeRecord.getRelatedOrderId(),
                financeRecord.getRemark(),
                financeRecord.getOperatorId()
        );
        return Result.success();
    }
}
