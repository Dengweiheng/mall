package com.server.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.server.entity.Category;
import com.server.server.mapper.CategoryMapper;
import com.server.server.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品分类服务实现类
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Override
    public List<Category> findAll() {
        return this.list();
    }
}
