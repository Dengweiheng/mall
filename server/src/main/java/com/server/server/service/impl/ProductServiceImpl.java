package com.server.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.server.common.PageResult;
import com.server.server.entity.Product;
import com.server.server.entity.StockRecord;
import com.server.server.exception.CustomException;
import com.server.server.mapper.ProductMapper;
import com.server.server.mapper.StockRecordMapper;
import com.server.server.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private StockRecordMapper stockRecordMapper;

    @Override
    public PageResult<Product> findPage(String name, String sku, Integer status, String category, Integer lowStock, int pageNum, int pageSize) {
        Page<Product> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(name)) {
            wrapper.like(Product::getName, name);
        }
        if (StringUtils.hasText(sku)) {
            wrapper.eq(Product::getSku, sku);
        }
        if (StringUtils.hasText(category)) {
            wrapper.eq(Product::getCategory, category);
        }
        if (status != null) {
            wrapper.eq(Product::getStatus, status);
        }
        if (lowStock != null) {
            // 如果传了 lowStock，查询库存小于该值的商品
            wrapper.lt(Product::getStock, lowStock);
        }
        wrapper.orderByDesc(Product::getCreateTime);

        Page<Product> result = this.page(page, wrapper);
        return new PageResult<>(result.getTotal(), result.getRecords());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addProduct(Product product) {
        if (!StringUtils.hasText(product.getSku())) {
            throw new CustomException(400, "商品SKU不能为空");
        }
        Product existingProduct = this.getOne(new LambdaQueryWrapper<Product>().eq(Product::getSku, product.getSku()));
        if (existingProduct != null) {
            throw new CustomException(400, "该SKU编码已存在");
        }
        if (product.getStatus() == null) {
            product.setStatus(1);
        }
        if (product.getStock() == null) {
            product.setStock(0);
        }
        this.save(product);
        
        // 初始库存记录
        if (product.getStock() > 0) {
            saveStockRecord(product.getId(), 1, product.getStock(), 0, product.getStock(), null, "初始库存导入");
        }
    }

    @Override
    public void updateProduct(Product product) {
        if (product.getId() == null) {
            throw new CustomException(400, "必须提供商品ID");
        }
        if (StringUtils.hasText(product.getSku())) {
            Product existingProduct = this.getOne(new LambdaQueryWrapper<Product>()
                    .eq(Product::getSku, product.getSku())
                    .ne(Product::getId, product.getId()));
            if (existingProduct != null) {
                throw new CustomException(400, "该SKU编码已存在");
            }
        }
        this.updateById(product);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Product product = new Product();
        product.setId(id);
        product.setStatus(status);
        this.updateById(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adjustStock(Long id, Integer stockChange) {
        Product product = this.getById(id);
        if (product == null) throw new CustomException("商品不存在");
        
        int before = product.getStock();
        int after = before + stockChange;
        if (after < 0) throw new CustomException(400, "库存余额不足");
        
        product.setStock(after);
        this.updateById(product);
        
        // 记录日志 (自动判定类型)
        int type = stockChange > 0 ? 1 : 2;
        saveStockRecord(id, type, Math.abs(stockChange), before, after, null, "订单或系统自动调整");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stockIn(Long id, Integer amount, Long operatorId, String remark) {
        Product product = this.getById(id);
        if (product == null) throw new CustomException("商品不存在");
        
        int before = product.getStock();
        int after = before + amount;
        
        product.setStock(after);
        this.updateById(product);
        
        saveStockRecord(id, 1, amount, before, after, operatorId, remark);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stockOut(Long id, Integer amount, Long operatorId, String remark) {
        Product product = this.getById(id);
        if (product == null) throw new CustomException("商品不存在");
        
        int before = product.getStock();
        int after = before - amount;
        if (after < 0) throw new CustomException(400, "库存不足，无法出库");
        
        product.setStock(after);
        this.updateById(product);
        
        saveStockRecord(id, 2, amount, before, after, operatorId, remark);
    }

    private void saveStockRecord(Long productId, Integer type, Integer amount, Integer before, Integer after, Long operatorId, String remark) {
        StockRecord record = new StockRecord();
        record.setProductId(productId);
        record.setType(type);
        record.setChangeAmount(amount);
        record.setBeforeStock(before);
        record.setAfterStock(after);
        record.setOperatorId(operatorId);
        record.setRemark(remark);
        record.setCreateTime(LocalDateTime.now());
        stockRecordMapper.insert(record);
    }
}
