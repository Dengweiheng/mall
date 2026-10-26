package com.server.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.server.common.PageResult;
import com.server.server.entity.Product;

public interface ProductService extends IService<Product> {

    /**
     * 分页查询商品（含库存）
     */
    PageResult<Product> findPage(String name, String sku, Integer status, String category, Integer lowStock, int pageNum, int pageSize);

    /**
     * 新增商品
     */
    void addProduct(Product product);

    /**
     * 修改商品基本信息
     */
    void updateProduct(Product product);

    /**
     * 变更商品状态 (上架/下架)
     */
    void updateStatus(Long id, Integer status);

    /**
     * 调整库存 (如: 入库操作, stockChange可以是正数或负数)
     */
    void adjustStock(Long id, Integer stockChange);

    /**
     * 商品入库
     */
    void stockIn(Long id, Integer amount, Long operatorId, String remark);

    /**
     * 商品出库 (手动)
     */
    void stockOut(Long id, Integer amount, Long operatorId, String remark);
}
