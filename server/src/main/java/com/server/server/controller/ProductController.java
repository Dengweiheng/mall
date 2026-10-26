package com.server.server.controller;

import com.server.server.common.PageResult;
import com.server.server.common.Result;
import com.server.server.entity.Product;
import com.server.server.service.ProductService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.server.server.entity.StockRecord;
import com.server.server.mapper.StockRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private StockRecordMapper stockRecordMapper;

    /**
     * 分页查询商品库存在线数据
     */
    @GetMapping("/page")
    public Result<PageResult<Product>> findPage(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String sku,
            @RequestParam(defaultValue = "") String category,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer lowStock,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageResult<Product> pageInfo = productService.findPage(name, sku, status, category, lowStock, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    /**
     * 获取单个商品详情
     */
    @GetMapping("/{id}")
    public Result<Product> getById(@PathVariable Long id) {
        return Result.success(productService.getById(id));
    }

    /**
     * 新增商品
     */
    @PostMapping
    public Result<Void> add(@RequestBody Product product) {
        productService.addProduct(product);
        return Result.success();
    }

    /**
     * 修改商品
     */
    @PutMapping
    public Result<Void> update(@RequestBody Product product) {
        productService.updateProduct(product);
        return Result.success();
    }

    /**
     * 修改商品状态 (上架/下架)
     */
    @PutMapping("/{id}/status/{status}")
    public Result<Void> updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        productService.updateStatus(id, status);
        return Result.success();
    }

    /**
     * 删除商品
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        productService.removeById(id);
        return Result.success();
    }

    /**
     * 库存直接调整 (加减库存), 比如盘点或特殊处理时用
     */
    @PutMapping("/{id}/stock")
    public Result<Void> adjustStock(@PathVariable Long id, @RequestParam Integer stockChange) {
        productService.adjustStock(id, stockChange);
        return Result.success();
    }

    /**
     * 商品入库 (手动入库记录)
     */
    @PostMapping("/{id}/stock-in")
    public Result<Void> stockIn(@PathVariable Long id, @RequestParam Integer amount, @RequestParam Long operatorId, @RequestParam(required = false) String remark) {
        productService.stockIn(id, amount, operatorId, remark);
        return Result.success();
    }

    @PostMapping("/{id}/stock-out")
    public Result<Void> stockOut(@PathVariable Long id, @RequestParam Integer amount, @RequestParam Long operatorId, @RequestParam(required = false) String remark) {
        productService.stockOut(id, amount, operatorId, remark);
        return Result.success();
    }

    /**
     * 查询库存变动记录 (流水账)
     */
    @GetMapping("/stock-records")
    public Result<PageResult<StockRecord>> getStockRecords(
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Integer type,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        Page<StockRecord> page = new Page<>(pageNum, pageSize);


        Page<StockRecord> result = stockRecordMapper.selectPageWithProductName(page, productId, type);
        return Result.success(new PageResult<>(result.getTotal(), result.getRecords()));
    }
}
