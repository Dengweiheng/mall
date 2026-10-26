package com.server.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.server.common.PageResult;
import com.server.server.entity.Order;
import com.server.server.entity.dto.OrderCreateDTO;
import com.server.server.entity.vo.OrderVO;

public interface OrderService extends IService<Order> {

    /**
     * 获取订单详情（含商品列表和用户信息）
     */
    OrderVO getOrderDetail(Long id);

    /**
     * 创建订单
     */
    void createOrder(OrderCreateDTO createDTO);

    /**
     * 订单支付
     */
    void payOrder(Long orderId, Long adminId);

    /**
     * 订单发货
     */
    void shipOrder(Long orderId);

    /**
     * 订单完成
     */
    void completeOrder(Long orderId);

    /**
     * 取消订单
     */
    void cancelOrder(Long orderId);

    /**
     * 分页查询订单
     */
    PageResult<Order> findPage(String orderNo, Integer status, Long userId, String username, int pageNum, int pageSize);
}
