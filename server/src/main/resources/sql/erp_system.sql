-- 兴启电商企业 ERP 管理系统数据库设计

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `xingqi_erp` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `xingqi_erp`;

-- ====================
-- 1. 用户表 (sys_user)
-- 满足：登录注册模块、用户管理模块、管理员管理模块、个人中心模块
-- 角色：SUPER_ADMIN(超级管理员), ADMIN(普通管理员), USER(普通用户)
-- ====================
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码',
  `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像',
  `role` VARCHAR(20) NOT NULL DEFAULT 'USER' COMMENT '角色: SUPER_ADMIN, ADMIN, USER',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1正常, 0禁用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户系统表';

-- 插入默认超级管理员账号 admin / 123456 (假设未经加盐加密前的数据，后续由Spring Security处理可先用明文或框架默认加密)
-- 为了简便，目前先明文配置，实际需要在后端注册时使用 PasswordEncoder
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `role`, `status`) 
VALUES ('admin', '$2a$10$NHuZq685lIRp4Z2UTjTkY.wHaYJSvmn8P2G3TmjjOIu0UlWnN4dBla', '超级管理员', 'SUPER_ADMIN', 1);

-- ====================
-- 2. 商品分类表 (erp_category)
-- ====================
DROP TABLE IF EXISTS `erp_category`;
CREATE TABLE `erp_category` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

INSERT INTO `erp_category` (`name`) VALUES ('手机'), ('办公'), ('食品'), ('数码'), ('配件');

-- ====================
-- 3. 商品库存表 (erp_product)
-- 满足：库存管理模块
-- ====================
DROP TABLE IF EXISTS `erp_product`;
CREATE TABLE `erp_product` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `sku` VARCHAR(100) NOT NULL COMMENT '商品SKU编码',
  `name` VARCHAR(100) NOT NULL COMMENT '商品名称',
  `category_id` BIGINT DEFAULT NULL COMMENT '所属分类ID',
  `category` VARCHAR(50) DEFAULT NULL COMMENT '分类名称快照',
  `price` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '单价',
  `stock` INT NOT NULL DEFAULT 0 COMMENT '库存数量',
  `image` VARCHAR(255) DEFAULT NULL COMMENT '商品图片',
  `description` TEXT COMMENT '商品描述',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1上架, 0下架',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sku` (`sku`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品库存表';

-- ====================
-- 3. 订单表 (erp_order)
-- 满足：订单管理模块
-- ====================
DROP TABLE IF EXISTS `erp_order`;
CREATE TABLE `erp_order` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` VARCHAR(64) NOT NULL COMMENT '订单流水号',
  `user_id` BIGINT NOT NULL COMMENT '下单用户ID (sys_user的id)',
  `total_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '订单总金额',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '订单状态: 0待支付, 1已支付待发货, 2已发货, 3已完成, 4已取消',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '订单备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- ====================
-- 4. 订单明细表 (erp_order_item)
-- 满足：订单管理模块
-- ====================
DROP TABLE IF EXISTS `erp_order_item`;
CREATE TABLE `erp_order_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `order_id` BIGINT NOT NULL COMMENT '归属订单ID',
  `product_id` BIGINT NOT NULL COMMENT '商品ID',
  `product_name` VARCHAR(100) NOT NULL COMMENT '商品名称快照',
  `product_price` DECIMAL(10,2) NOT NULL COMMENT '商品单价快照',
  `quantity` INT NOT NULL DEFAULT 1 COMMENT '购买数量',
  `total_price` DECIMAL(10,2) NOT NULL COMMENT '明细总金额',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单商品明细表';

-- ====================
-- 5. 财务记录表 (erp_finance)
-- 满足：财务管理模块
-- ====================
DROP TABLE IF EXISTS `erp_finance`;
CREATE TABLE `erp_finance` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '财务记录ID',
  `type` TINYINT NOT NULL COMMENT '类型: 1收入, 2支出',
  `amount` DECIMAL(10,2) NOT NULL COMMENT '金额',
  `related_order_id` BIGINT DEFAULT NULL COMMENT '关联的订单ID(可为空,比如日常开销)',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '财务备注说明',
  `operator_id` BIGINT NOT NULL COMMENT '操作人ID (录入该记录的人)',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='财务记录表';

-- ====================
-- 6. 库存变动记录表 (erp_stock_record)
-- 满足：库存管理模块（入库、出库、盘点记录）
-- ====================
DROP TABLE IF EXISTS `erp_stock_record`;
CREATE TABLE `erp_stock_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `product_id` BIGINT NOT NULL COMMENT '商品ID',
  `type` TINYINT NOT NULL COMMENT '变动类型: 1入库, 2出库, 3盘点修正',
  `change_amount` INT NOT NULL COMMENT '变动数量(可正可负)',
  `before_stock` INT NOT NULL COMMENT '变动前库存',
  `after_stock` INT NOT NULL COMMENT '变动后库存',
  `operator_id` BIGINT DEFAULT NULL COMMENT '操作人ID',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存变动记录表';

-- ====================
-- 说明：
-- 首页面板展示、报表管理模块，通常基于以上表的数据聚合查询（如：日收入统计、新增用户统计、热销商品排行榜等），
-- 无需独立创建基础表，如果需要性能优化可以后期增加缓存或聚合结果表。
-- ====================
