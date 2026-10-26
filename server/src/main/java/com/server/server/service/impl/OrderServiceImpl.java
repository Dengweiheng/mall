package com.server.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.server.common.PageResult;
import com.server.server.entity.Order;
import com.server.server.entity.OrderItem;
import com.server.server.entity.Product;
import com.server.server.entity.User;
import com.server.server.entity.dto.OrderCreateDTO;
import com.server.server.entity.vo.OrderVO;
import com.server.server.exception.CustomException;
import com.server.server.mapper.OrderItemMapper;
import com.server.server.mapper.OrderMapper;
import com.server.server.mapper.UserMapper;
import com.server.server.service.FinanceRecordService;
import com.server.server.service.OrderService;
import com.server.server.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private FinanceRecordService financeRecordService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public OrderVO getOrderDetail(Long id) {
        Order order = this.getById(id);
        if (order == null) return null;

        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(order, vo);

        // 1. 查询订单明细
        List<OrderItem> items = orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, id)
        );
        vo.setItems(items);

        // 2. 查询下单人姓名
        User user = userMapper.selectById(order.getUserId());
        if (user != null) {
            vo.setUsername(user.getUsername());
        }

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrder(OrderCreateDTO createDTO) {
        if (createDTO.getItems() == null || createDTO.getItems().isEmpty()) {
            throw new CustomException(400, "订单中没有商品");
        }
        
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        // 1. 生成订单主表基础信息
        Order order = new Order();
        String orderNo = "ORD" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4);
        order.setOrderNo(orderNo.toUpperCase());
        order.setUserId(createDTO.getUserId());
        order.setStatus(0); // 待支付
        order.setRemark(createDTO.getRemark());
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        
        // 校验商品和库存，并扣减库存
        for (OrderCreateDTO.OrderItemDTO dto : createDTO.getItems()) {
            Product product = productService.getById(dto.getProductId());
            if (product == null || product.getStatus() == 0) {
                throw new CustomException(400, "包含不存在或已下架的商品");
            }
            if (product.getStock() < dto.getQuantity()) {
                throw new CustomException(400, "商品[" + product.getName() + "]库存不足");
            }

            // 扣减库存 (Service 内部有日志记录)
            productService.adjustStock(product.getId(), -dto.getQuantity());

            // 累加金额
            BigDecimal itemTotal = product.getPrice().multiply(new BigDecimal(dto.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            // 构造明细项
            OrderItem item = new OrderItem();
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setProductPrice(product.getPrice());
            item.setQuantity(dto.getQuantity());
            item.setTotalPrice(itemTotal);
            orderItems.add(item);
        }

        order.setTotalAmount(totalAmount);
        this.save(order); // 保存订单主表

        // 2. 批量保存订单明细
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
            orderItemMapper.insert(item);
        }
        log.info("Order created successfully: {}, Total amount: {}", order.getOrderNo(), order.getTotalAmount());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payOrder(Long orderId, Long adminId) {
        Order order = this.getById(orderId);
        if (order == null) throw new CustomException("订单不存在");
        if (order.getStatus() != 0) throw new CustomException(400, "订单状态错误，无法支付");
        
        order.setStatus(1); // 已支付待发货
        this.updateById(order);

        // 增加财务收入记录
        financeRecordService.addRecord(1, order.getTotalAmount(), order.getId(), "订单支付收入: " + order.getOrderNo(), adminId);
        log.info("Order paid successfully: {}, AdminId: {}", order.getOrderNo(), adminId);
    }

    @Override
    public void shipOrder(Long orderId) {
        Order order = this.getById(orderId);
        if (order == null || order.getStatus() != 1) throw new CustomException(400, "订单状态错误，无法发货");
        
        order.setStatus(2); // 已发货
        this.updateById(order);
    }

    @Override
    public void completeOrder(Long orderId) {
        Order order = this.getById(orderId);
        if (order == null || order.getStatus() != 2) throw new CustomException(400, "订单状态错误，无法完成");
        
        order.setStatus(3); // 已完成
        this.updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long orderId) {
        Order order = this.getById(orderId);
        if (order == null) throw new CustomException("订单不存在");
        if (order.getStatus() == 4) throw new CustomException(400, "订单已取消");
        if (order.getStatus() == 3) throw new CustomException(400, "订单已完成，无法取消");

        // 1. 把商品库存加回来
        List<OrderItem> items = orderItemMapper.selectList(new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, order.getId()));
        for (OrderItem item : items) {
            productService.adjustStock(item.getProductId(), item.getQuantity());
        }

        // 2. 如果已经支付过，生成退款（财务支出）
        if (order.getStatus() >= 1) {
             financeRecordService.addRecord(2, order.getTotalAmount(), order.getId(), "订单取消退款: " + order.getOrderNo(), order.getUserId());
        }

        order.setStatus(4); // 已取消
        this.updateById(order);
        log.info("Order cancelled successfully: {}", order.getOrderNo());
    }

    @Override
    public PageResult<Order> findPage(String orderNo, Integer status, Long userId, String username, int pageNum, int pageSize) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(orderNo)) wrapper.like(Order::getOrderNo, orderNo);
        if (status != null) wrapper.eq(Order::getStatus, status);
        
        if (userId != null) {
            wrapper.eq(Order::getUserId, userId);
        } else if (StringUtils.hasText(username)) {
            // 通过用户名过滤
            LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
            userWrapper.like(User::getUsername, username);
            List<User> users = userMapper.selectList(userWrapper);
            if (users != null && !users.isEmpty()) {
                List<Long> ids = users.stream().map(User::getId).collect(Collectors.toList());
                wrapper.in(Order::getUserId, ids);
            } else {
                return new PageResult<>(0, new ArrayList<>());
            }
        }
        
        wrapper.orderByDesc(Order::getCreateTime);
        Page<Order> result = this.page(page, wrapper);
        return new PageResult<>(result.getTotal(), result.getRecords());
    }
}
