package com.server.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.server.common.PageResult;
import com.server.server.entity.FinanceRecord;
import com.server.server.entity.dto.FinanceSummaryDTO;
import com.server.server.mapper.FinanceRecordMapper;
import com.server.server.service.FinanceRecordService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FinanceRecordServiceImpl extends ServiceImpl<FinanceRecordMapper, FinanceRecord> implements FinanceRecordService {

    @Override
    public void addRecord(Integer type, BigDecimal amount, Long relatedOrderId, String remark, Long operatorId) {
        FinanceRecord record = new FinanceRecord();
        record.setType(type);
        record.setAmount(amount);
        record.setRelatedOrderId(relatedOrderId);
        record.setRemark(remark);
        record.setOperatorId(operatorId);
        record.setCreateTime(LocalDateTime.now());
        this.save(record);
    }

    @Override
    public PageResult<FinanceRecord> findPage(Integer type, String startTime, String endTime, int pageNum, int pageSize) {
        Page<FinanceRecord> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<FinanceRecord> wrapper = new LambdaQueryWrapper<>();
        
        if (type != null) {
            wrapper.eq(FinanceRecord::getType, type);
        }
        if (StringUtils.hasText(startTime)) {
            wrapper.ge(FinanceRecord::getCreateTime, startTime);
        }
        if (StringUtils.hasText(endTime)) {
            wrapper.le(FinanceRecord::getCreateTime, endTime);
        }
        
        wrapper.orderByDesc(FinanceRecord::getCreateTime);
        Page<FinanceRecord> result = this.page(page, wrapper);
        return new PageResult<>(result.getTotal(), result.getRecords());
    }

    @Override
    public FinanceSummaryDTO getSummary(String startTime, String endTime) {
        LambdaQueryWrapper<FinanceRecord> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(startTime)) {
            wrapper.ge(FinanceRecord::getCreateTime, startTime);
        }
        if (StringUtils.hasText(endTime)) {
            wrapper.le(FinanceRecord::getCreateTime, endTime);
        }

        List<FinanceRecord> list = this.list(wrapper);
        
        BigDecimal income = BigDecimal.ZERO;
        BigDecimal expense = BigDecimal.ZERO;
        
        for (FinanceRecord record : list) {
            if (record.getType() == 1) { // 收入
                income = income.add(record.getAmount());
            } else if (record.getType() == 2) { // 支出
                expense = expense.add(record.getAmount());
            }
        }
        
        FinanceSummaryDTO summary = new FinanceSummaryDTO();
        summary.setTotalIncome(income);
        summary.setTotalExpense(expense);
        summary.setBalance(income.subtract(expense));
        
        return summary;
    }
}
