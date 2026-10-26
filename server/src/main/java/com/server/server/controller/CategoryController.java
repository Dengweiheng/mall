package com.server.server.controller;

import com.server.server.common.Result;
import com.server.server.entity.Category;
import com.server.server.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品分类控制器
 */
@RestController
@RequestMapping("/api/category")
@CrossOrigin
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 获取所有分类
     */
    @GetMapping("/all")
    public Result<List<Category>> findAll() {
        return Result.success(categoryService.findAll());
    }

    /**
     * 新增分类
     */
    @PostMapping
    public Result<Void> add(@RequestBody Category category) {
        categoryService.save(category);
        return Result.success();
    }

    /**
     * 修改分类
     */
    @PutMapping
    public Result<Void> update(@RequestBody Category category) {
        categoryService.updateById(category);
        return Result.success();
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        categoryService.removeById(id);
        return Result.success();
    }
}
