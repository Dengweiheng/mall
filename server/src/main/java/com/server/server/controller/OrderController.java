package com.server.server.controller;

import com.server.server.common.PageResult;
import com.server.server.common.Result;
import com.server.server.entity.Order;
import com.server.server.entity.dto.OrderCreateDTO;
import com.server.server.entity.vo.OrderVO;
import com.server.server.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 分页查询订单
     */
    @GetMapping("/page")
    public Result<PageResult<Order>> findPage(
            @RequestParam(defaultValue = "") String orderNo,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "") String username,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageResult<Order> pageInfo = orderService.findPage(orderNo, status, userId, username, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    /**
     * 创建订单
     */
    @PostMapping
    public Result<Void> create(@RequestBody OrderCreateDTO createDTO) {
        orderService.createOrder(createDTO);
        return Result.success();
    }

    /**
     * 支付订单
     * @param id 订单id
     * @param adminId 当前审批付款的管理员id (为了简便直接让前端传)
     */
    @PutMapping("/{id}/pay")
    public Result<Void> pay(@PathVariable Long id, @RequestParam Long adminId) {
        orderService.payOrder(id, adminId);
        return Result.success();
    }

    /**
     * 订单发货
     */
    @PutMapping("/{id}/ship")
    public Result<Void> ship(@PathVariable Long id) {
        orderService.shipOrder(id);
        return Result.success();
    }

    /**
     * 订单完成
     */
    @PutMapping("/{id}/complete")
    public Result<Void> complete(@PathVariable Long id) {
        orderService.completeOrder(id);
        return Result.success();
    }

    /**
     * 取消订单
     */
    @PutMapping("/{id}/cancel")
    public Result<Void> cancel(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return Result.success();
    }

    /**
     * 获取订单详情 (含商品明细)
     */
    @GetMapping("/{id}")
    public Result<OrderVO> getById(@PathVariable Long id) {
        return Result.success(orderService.getOrderDetail(id));
    }
}
