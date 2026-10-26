-- 兴启电商品牌 ERP 系统 - 测试数据脚本
USE `xingqi_erp`;

-- 1. 用户表 (sys_user) - 已有 admin (1)，补充 10 条
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `phone`, `email`, `role`, `status`) VALUES
('manager1', '$2a$10$NHuZq685lIRp4Z2UTjTkY.wHaYJSvmn8P2G3TmjjOIu0UlWnN4dBla', '张经理', '13800138001', 'm1@example.com', 'ADMIN', 1),
('manager2', '$2a$10$NHuZq685lIRp4Z2UTjTkY.wHaYJSvmn8P2G3TmjjOIu0UlWnN4dBla', '李主管', '13800138002', 'm2@example.com', 'ADMIN', 1),
('user001', '$2a$10$NHuZq685lIRp4Z2UTjTkY.wHaYJSvmn8P2G3TmjjOIu0UlWnN4dBla', '王小二', '13911112222', 'w1@test.com', 'USER', 1),
('user002', '$2a$10$NHuZq685lIRp4Z2UTjTkY.wHaYJSvmn8P2G3TmjjOIu0UlWnN4dBla', '赵铁柱', '13933334444', 'z2@test.com', 'USER', 1),
('user003', '$2a$10$NHuZq685lIRp4Z2UTjTkY.wHaYJSvmn8P2G3TmjjOIu0UlWnN4dBla', '孙美美', '13955556666', 's3@test.com', 'USER', 1),
('user004', '$2a$10$NHuZq685lIRp4Z2UTjTkY.wHaYJSvmn8P2G3TmjjOIu0UlWnN4dBla', '钱多多', '13977778888', 'q4@test.com', 'USER', 1),
('user005', '$2a$10$NHuZq685lIRp4Z2UTjTkY.wHaYJSvmn8P2G3TmjjOIu0UlWnN4dBla', '周星星', '13812345678', 'z5@test.com', 'USER', 1),
('user006', '$2a$10$NHuZq685lIRp4Z2UTjTkY.wHaYJSvmn8P2G3TmjjOIu0UlWnN4dBla', '吴所谓', '13887654321', 'w6@test.com', 'USER', 1),
('user007', '$2a$10$NHuZq685lIRp4Z2UTjTkY.wHaYJSvmn8P2G3TmjjOIu0UlWnN4dBla', '郑能量', '13700001111', 'z7@test.com', 'USER', 1),
('user008', '$2a$10$NHuZq685lIRp4Z2UTjTkY.wHaYJSvmn8P2G3TmjjOIu0UlWnN4dBla', '冯大宽', '13722223333', 'f8@test.com', 'USER', 0);

-- 2. 商品库存表 (erp_product)
INSERT INTO `erp_product` (`sku`, `name`, `category_id`, `category`, `price`, `stock`, `image`, `description`) VALUES
('PHONE-001', 'iPhone 15 Pro', 1, '手机', 7999.00, 50, 'https://images.unsplash.com/photo-1696446701796-da61225697cc?w=400', '新款苹果手机，钛金属设计'),
('PHONE-002', 'Huawei Mate 60', 1, '手机', 6999.00, 30, 'https://images.unsplash.com/photo-1695653422718-283ce9b1f7e0?w=400', '华为卫星通话手机'),
('OFFICE-001', '罗技越野鼠标', 2, '办公', 299.00, 100, 'https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=400', '符合人体工学设计的无线鼠标'),
('OFFICE-002', '机械键盘K8', 2, '办公', 499.00, 80, 'https://images.unsplash.com/photo-1511467687858-23d96c32e4ae?w=400', '青轴背光机械键盘'),
('FOOD-001', '三只松鼠坚果大礼包', 3, '食品', 128.00, 200, 'https://images.unsplash.com/photo-1536627217140-6925509e45e0?w=400', '精选各类坚果'),
('FOOD-002', '良品铺子肉脯', 3, '食品', 35.00, 500, 'https://images.unsplash.com/photo-1599490659223-e1539f759fbc?w=400', '经典猪肉脯'),
('PHONE-003', 'Xiaomi 14', 1, '手机', 3999.00, 40, 'https://images.unsplash.com/photo-1592899677977-9c10ca588bbd?w=400', '徕卡光学镜头'),
('OFFICE-003', 'A4打印纸', 2, '办公', 20.00, 1000, 'https://images.unsplash.com/photo-1589182373726-e4f658ab50f0?w=400', '70g 复印纸 500张/包'),
('FOOD-003', '自嗨锅火锅', 3, '食品', 25.00, 150, 'https://images.unsplash.com/photo-1582878826629-29b7ad1cdc43?w=400', '懒人速食火锅'),
('OFFICE-004', '得力中性笔', 2, '办公', 1.50, 2000, 'https://images.unsplash.com/photo-1585336139118-bfa9fc990b79?w=400', '0.5mm 黑色签字笔');

-- 3. 订单表 (erp_order)
INSERT INTO `erp_order` (`order_no`, `user_id`, `total_amount`, `status`, `remark`) VALUES
('ORD202403010001', 3, 7999.00, 1, '客户急用，请尽快发货'),
('ORD202403010002', 4, 334.00, 3, '已收到货，质量很好'),
('ORD202403010003', 5, 299.00, 0, '稍后支付'),
('ORD202403010004', 6, 6999.00, 2, '送货上门'),
('ORD202403010005', 7, 128.00, 1, '帮朋友买的'),
('ORD202403010006', 8, 35.00, 4, '买错了，取消订单'),
('ORD202403010007', 3, 3999.00, 3, '二次回购'),
('ORD202403010008', 4, 40.00, 1, '办公用品采购'),
('ORD202403010009', 5, 25.00, 2, '晚上吃'),
('ORD202403010010', 6, 30.00, 3, '性价比很高');

-- 4. 订单明细表 (erp_order_item)
INSERT INTO `erp_order_item` (`order_id`, `product_id`, `product_name`, `product_price`, `quantity`, `total_price`) VALUES
(1, 1, 'iPhone 15 Pro', 7999.00, 1, 7999.00),
(2, 3, '罗技越野鼠标', 299.00, 1, 299.00),
(2, 6, '良品铺子肉脯', 35.00, 1, 35.00),
(3, 3, '罗技越野鼠标', 299.00, 1, 299.00),
(4, 2, 'Huawei Mate 60', 6999.00, 1, 6999.00),
(5, 5, '三只松鼠坚果大礼包', 128.00, 1, 128.00),
(7, 7, 'Xiaomi 14', 3999.00, 1, 3999.00),
(8, 8, 'A4打印纸', 20.00, 2, 40.00),
(9, 9, '自嗨锅火锅', 25.00, 1, 25.00),
(10, 10, '得力中性笔', 1.50, 20, 30.00);

-- 5. 财务记录表 (erp_finance)
INSERT INTO `erp_finance` (`type`, `amount`, `related_order_id`, `remark`, `operator_id`) VALUES
(1, 7999.00, 1, '销售收入-手机', 1),
(1, 334.00, 2, '销售收入-百货', 1),
(2, 1500.00, NULL, '缴纳办公室房租', 2),
(1, 6999.00, 4, '销售收入-手机', 1),
(2, 200.00, NULL, '电费支出', 2),
(1, 128.00, 5, '销售收入-食品', 1),
(1, 3999.00, 7, '核心客户订单', 1),
(2, 50.00, NULL, '饮用水开支', 2),
(1, 40.00, 8, '办公耗材销售', 2),
(1, 30.00, 10, '小笔文具收入', 2);

-- 6. 库存变动记录表 (erp_stock_record)
INSERT INTO `erp_stock_record` (`product_id`, `type`, `change_amount`, `before_stock`, `after_stock`, `operator_id`, `remark`) VALUES
(1, 1, 50, 0, 50, 1, '首批入库'),
(2, 1, 30, 0, 30, 1, '首批入库'),
(3, 1, 100, 0, 100, 2, '日常补货'),
(1, 2, -1, 50, 49, 1, '订单出库 (ORD202403010001)'),
(2, 2, -1, 30, 29, 1, '订单出库 (ORD202403010004)'),
(5, 1, 200, 0, 200, 2, '食品入库'),
(3, 3, -2, 100, 98, 2, '盘点损耗'),
(7, 1, 40, 0, 40, 1, '新品上市入库'),
(10, 1, 2000, 0, 2000, 2, '大批量到货'),
(8, 2, -2, 1000, 998, 2, '领用出库');
