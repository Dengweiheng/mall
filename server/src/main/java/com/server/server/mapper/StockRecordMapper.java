package com.server.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.server.server.entity.StockRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StockRecordMapper extends BaseMapper<StockRecord> {

    Page<StockRecord> selectPageWithProductName(
            @Param("page") Page<StockRecord> page,
            @Param("productId") Long productId,
            @Param("type") Integer type
    );
}
