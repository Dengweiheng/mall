package com.server.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.server.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
