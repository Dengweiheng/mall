-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: xingqi_erp
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `erp_category`
--

DROP TABLE IF EXISTS `erp_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `erp_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品分类表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `erp_category`
--

LOCK TABLES `erp_category` WRITE;
/*!40000 ALTER TABLE `erp_category` DISABLE KEYS */;
INSERT INTO `erp_category` (`id`, `name`, `create_time`) VALUES (1,'手机','2026-03-03 16:29:02'),(2,'办公','2026-03-03 16:29:02'),(3,'食品','2026-03-03 16:29:02'),(4,'数码','2026-03-03 16:29:02'),(5,'配件','2026-03-03 16:29:02'),(6,'test1','2026-03-03 16:50:33');
/*!40000 ALTER TABLE `erp_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `erp_finance`
--

DROP TABLE IF EXISTS `erp_finance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `erp_finance` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '财务记录ID',
  `type` tinyint NOT NULL COMMENT '类型: 1收入, 2支出',
  `amount` decimal(10,2) NOT NULL COMMENT '金额',
  `related_order_id` bigint DEFAULT NULL COMMENT '关联的订单ID(可为空,比如日常开销)',
  `remark` varchar(255) DEFAULT NULL COMMENT '财务备注说明',
  `operator_id` bigint NOT NULL COMMENT '操作人ID (录入该记录的人)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='财务记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `erp_finance`
--

LOCK TABLES `erp_finance` WRITE;
/*!40000 ALTER TABLE `erp_finance` DISABLE KEYS */;
INSERT INTO `erp_finance` (`id`, `type`, `amount`, `related_order_id`, `remark`, `operator_id`, `create_time`) VALUES (1,1,7999.00,1,'销售收入-手机',1,'2026-03-01 11:54:09'),(2,1,334.00,2,'销售收入-百货',1,'2026-03-01 11:54:09'),(3,2,1500.00,NULL,'缴纳办公室房租',2,'2026-03-01 11:54:09'),(4,1,6999.00,4,'销售收入-手机',1,'2026-03-01 11:54:09'),(5,2,200.00,NULL,'电费支出',2,'2026-03-01 11:54:09'),(6,1,128.00,5,'销售收入-食品',1,'2026-03-01 11:54:09'),(7,1,3999.00,7,'核心客户订单',1,'2026-03-01 11:54:09'),(8,2,50.00,NULL,'饮用水开支',2,'2026-03-01 11:54:09'),(9,1,40.00,8,'办公耗材销售',2,'2026-03-01 11:54:09'),(10,1,30.00,10,'小笔文具收入',2,'2026-03-01 11:54:09'),(11,1,7999.00,11,'订单支付收入: ORD1772338519926F381',1,'2026-03-01 12:17:03'),(12,1,50000.00,12,'订单支付收入: ORD1772527996242869C',1,'2026-03-03 16:53:22');
/*!40000 ALTER TABLE `erp_finance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `erp_order`
--

DROP TABLE IF EXISTS `erp_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `erp_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(64) NOT NULL COMMENT '订单流水号',
  `user_id` bigint NOT NULL COMMENT '下单用户ID (sys_user的id)',
  `total_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '订单总金额',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '订单状态: 0待支付, 1已支付待发货, 2已发货, 3已完成, 4已取消',
  `remark` varchar(255) DEFAULT NULL COMMENT '订单备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `erp_order`
--

LOCK TABLES `erp_order` WRITE;
/*!40000 ALTER TABLE `erp_order` DISABLE KEYS */;
INSERT INTO `erp_order` (`id`, `order_no`, `user_id`, `total_amount`, `status`, `remark`, `create_time`, `update_time`) VALUES (1,'ORD202403010001',3,7999.00,2,'客户急用，请尽快发货','2026-03-01 11:54:09','2026-03-01 11:54:09'),(2,'ORD202403010002',4,334.00,3,'已收到货，质量很好','2026-03-01 11:54:09','2026-03-01 11:54:09'),(3,'ORD202403010003',5,299.00,0,'稍后支付','2026-03-01 11:54:09','2026-03-01 11:54:09'),(4,'ORD202403010004',6,6999.00,3,'送货上门','2026-03-01 11:54:09','2026-03-01 11:54:09'),(5,'ORD202403010005',7,128.00,1,'帮朋友买的','2026-03-01 11:54:09','2026-03-01 11:54:09'),(6,'ORD202403010006',8,35.00,4,'买错了，取消订单','2026-03-01 11:54:09','2026-03-01 11:54:09'),(7,'ORD202403010007',3,3999.00,3,'二次回购','2026-03-01 11:54:09','2026-03-01 11:54:09'),(8,'ORD202403010008',4,40.00,1,'办公用品采购','2026-03-01 11:54:09','2026-03-01 11:54:09'),(9,'ORD202403010009',5,25.00,2,'晚上吃','2026-03-01 11:54:09','2026-03-01 11:54:09'),(10,'ORD202403010010',6,30.00,3,'性价比很高','2026-03-01 11:54:09','2026-03-01 11:54:09'),(11,'ORD1772338519926F381',6,7999.00,3,'购物车批量下单','2026-03-01 12:15:20','2026-03-01 12:15:20'),(12,'ORD1772527996242869C',14,50000.00,3,'购物车批量下单','2026-03-03 16:53:16','2026-03-03 16:53:16');
/*!40000 ALTER TABLE `erp_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `erp_order_item`
--

DROP TABLE IF EXISTS `erp_order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `erp_order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `order_id` bigint NOT NULL COMMENT '归属订单ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(100) NOT NULL COMMENT '商品名称快照',
  `product_price` decimal(10,2) NOT NULL COMMENT '商品单价快照',
  `quantity` int NOT NULL DEFAULT '1' COMMENT '购买数量',
  `total_price` decimal(10,2) NOT NULL COMMENT '明细总金额',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单商品明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `erp_order_item`
--

LOCK TABLES `erp_order_item` WRITE;
/*!40000 ALTER TABLE `erp_order_item` DISABLE KEYS */;
INSERT INTO `erp_order_item` (`id`, `order_id`, `product_id`, `product_name`, `product_price`, `quantity`, `total_price`, `create_time`) VALUES (1,1,1,'iPhone 15 Pro',7999.00,1,7999.00,'2026-03-01 11:54:09'),(2,2,3,'罗技越野鼠标',299.00,1,299.00,'2026-03-01 11:54:09'),(3,2,6,'良品铺子肉脯',35.00,1,35.00,'2026-03-01 11:54:09'),(4,3,3,'罗技越野鼠标',299.00,1,299.00,'2026-03-01 11:54:09'),(5,4,2,'Huawei Mate 60',6999.00,1,6999.00,'2026-03-01 11:54:09'),(6,5,5,'三只松鼠坚果大礼包',128.00,1,128.00,'2026-03-01 11:54:09'),(7,7,7,'Xiaomi 14',3999.00,1,3999.00,'2026-03-01 11:54:09'),(8,8,8,'A4打印纸',20.00,2,40.00,'2026-03-01 11:54:09'),(9,9,9,'自嗨锅火锅',25.00,1,25.00,'2026-03-01 11:54:09'),(10,10,10,'得力中性笔',1.50,20,30.00,'2026-03-01 11:54:09'),(11,11,1,'iPhone 17 Pro',7999.00,1,7999.00,'2026-03-01 12:15:19'),(12,12,12,'test2',10000.00,5,50000.00,'2026-03-03 16:53:16');
/*!40000 ALTER TABLE `erp_order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `erp_product`
--

DROP TABLE IF EXISTS `erp_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `erp_product` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `sku` varchar(100) NOT NULL COMMENT '商品SKU编码',
  `name` varchar(100) NOT NULL COMMENT '商品名称',
  `category_id` bigint DEFAULT NULL COMMENT '所属分类ID',
  `category` varchar(50) DEFAULT NULL COMMENT '分类名称快照',
  `price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '单价',
  `stock` int NOT NULL DEFAULT '0' COMMENT '库存数量',
  `image` varchar(255) DEFAULT NULL COMMENT '商品图片',
  `description` text COMMENT '商品描述',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态: 1上架, 0下架',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sku` (`sku`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品库存表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `erp_product`
--

LOCK TABLES `erp_product` WRITE;
/*!40000 ALTER TABLE `erp_product` DISABLE KEYS */;
INSERT INTO `erp_product` (`id`, `sku`, `name`, `category_id`, `category`, `price`, `stock`, `image`, `description`, `status`, `create_time`, `update_time`) VALUES (1,'PHONE-001','iPhone 17 Pro',1,'手机',8000.00,59,'/api/file/view/31e71386-6cd6-4315-b8c5-ec327d61a7e4.webp','新款苹果手机，钛金属设计',1,'2026-03-01 11:54:09','2026-03-03 16:29:15'),(2,'PHONE-002','Huawei Mate 60',1,'手机',6999.00,30,'/api/file/view/f1c0b28f-f5b9-4db0-890f-7c4734363333.jpg','华为卫星通话手机',1,'2026-03-01 11:54:09','2026-03-03 16:29:15'),(3,'OFFICE-001','罗技鼠标',2,'办公',299.00,100,'/api/file/view/06de3ef6-8668-4834-aee1-51926a2f00f2.webp','符合人体工学设计的无线鼠标',1,'2026-03-01 11:54:09','2026-03-03 16:29:15'),(4,'OFFICE-002','机械键盘K8',2,'办公',499.00,80,'/api/file/view/494acb2a-313d-403e-8e4a-1f5f954c3cb0.webp','青轴背光机械键盘',1,'2026-03-01 11:54:09','2026-03-03 16:29:15'),(5,'FOOD-001','三只松鼠坚果大礼包',3,'食品',128.00,200,'/api/file/view/9275d542-750f-42d2-bf9b-c0cb06650827.webp','精选各类坚果',1,'2026-03-01 11:54:09','2026-03-03 16:29:15'),(6,'FOOD-002','良品铺子肉脯',3,'食品',35.00,500,'/api/file/view/7bc1add7-8507-49f5-a81c-a36568c15c69.webp','经典猪肉脯',1,'2026-03-01 11:54:09','2026-03-03 16:29:15'),(7,'PHONE-003','Xiaomi 14',1,'手机',3999.00,40,'/api/file/view/6db1468f-5481-4cef-b1a5-f7c5d1403d5d.webp','徕卡光学镜头',1,'2026-03-01 11:54:09','2026-03-03 16:29:15'),(8,'OFFICE-003','A4打印纸',2,'办公',20.00,1000,'/api/file/view/df6fd955-2734-42da-b140-64342a5715f5.webp','70g 复印纸 500张/包',1,'2026-03-01 11:54:09','2026-03-03 16:29:15'),(9,'FOOD-003','自嗨锅火锅',3,'食品',25.00,150,'/api/file/view/c769e9fd-5820-4394-9ca1-93888577a753.webp','懒人速食火锅',1,'2026-03-01 11:54:09','2026-03-03 16:29:15'),(10,'OFFICE-004','得力中性笔',2,'办公',1.50,2000,NULL,'0.5mm 黑色签字笔',1,'2026-03-01 11:54:09','2026-03-03 16:29:15'),(11,'123456','test',1,'手机',5000.00,10,'/api/file/view/a840c784-3c51-4a9b-a681-be89192dd9b2.jpg',NULL,1,'2026-03-03 16:19:46','2026-03-03 16:29:15'),(12,'test2123456','test2',6,'test',10000.00,15,'/api/file/view/c5eef3e2-16df-47aa-9d7d-9154eb4c5e0c.jpg',NULL,1,'2026-03-03 16:51:17','2026-03-03 16:51:17');
/*!40000 ALTER TABLE `erp_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `erp_stock_record`
--

DROP TABLE IF EXISTS `erp_stock_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `erp_stock_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `type` tinyint NOT NULL COMMENT '变动类型: 1入库, 2出库, 3盘点修正',
  `change_amount` int NOT NULL COMMENT '变动数量(可正可负)',
  `before_stock` int NOT NULL COMMENT '变动前库存',
  `after_stock` int NOT NULL COMMENT '变动后库存',
  `operator_id` bigint DEFAULT NULL COMMENT '操作人ID',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='库存变动记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `erp_stock_record`
--

LOCK TABLES `erp_stock_record` WRITE;
/*!40000 ALTER TABLE `erp_stock_record` DISABLE KEYS */;
INSERT INTO `erp_stock_record` (`id`, `product_id`, `type`, `change_amount`, `before_stock`, `after_stock`, `operator_id`, `remark`, `create_time`) VALUES (1,1,1,50,0,50,1,'首批入库','2026-03-01 11:54:09'),(2,2,1,30,0,30,1,'首批入库','2026-03-01 11:54:09'),(3,3,1,100,0,100,2,'日常补货','2026-03-01 11:54:09'),(4,1,2,-1,50,49,1,'订单出库 (ORD202403010001)','2026-03-01 11:54:09'),(5,2,2,-1,30,29,1,'订单出库 (ORD202403010004)','2026-03-01 11:54:09'),(6,5,1,200,0,200,2,'食品入库','2026-03-01 11:54:09'),(7,3,3,-2,100,98,2,'盘点损耗','2026-03-01 11:54:09'),(8,7,1,40,0,40,1,'新品上市入库','2026-03-01 11:54:09'),(9,10,1,2000,0,2000,2,'大批量到货','2026-03-01 11:54:09'),(10,8,2,-2,1000,998,2,'领用出库','2026-03-01 11:54:09'),(11,1,1,5,50,55,1,'手动操作','2026-03-01 12:04:37'),(12,1,2,5,55,50,1,'手动操作','2026-03-01 12:04:44'),(13,1,2,1,50,49,NULL,'订单或系统自动调整','2026-03-01 12:15:20'),(14,1,1,10,49,59,2,'手动操作','2026-03-01 12:44:20'),(15,11,1,10,0,10,NULL,'初始库存导入','2026-03-03 16:19:47'),(16,12,1,10,0,10,NULL,'初始库存导入','2026-03-03 16:51:18'),(17,12,1,10,10,20,3,'test','2026-03-03 16:52:21'),(18,12,2,5,20,15,NULL,'订单或系统自动调整','2026-03-03 16:53:16');
/*!40000 ALTER TABLE `erp_stock_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `role` varchar(20) NOT NULL DEFAULT 'USER' COMMENT '角色: SUPER_ADMIN, ADMIN, USER',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态: 1正常, 0禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户系统表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `phone`, `email`, `avatar`, `role`, `status`, `create_time`, `update_time`) VALUES (1,'admin','$2a$10$m8BSrbjEumGu3VQct3JBQ.2mG3d8vMNuVhCHOBSrd1Zahix23DaZG','超级管理员','18111111111','123@qq.com',NULL,'SUPER_ADMIN',1,'2026-02-27 13:11:54','2026-03-01 12:39:56'),(2,'puto_admin','$2a$10$IMDnehMMWzL1YjO.ZwLwTu7ONAjxtZNbdIIxd1i2Q3BWB3040tApS','普通管理员',NULL,NULL,NULL,'ADMIN',1,'2026-03-01 11:49:32','2026-03-01 12:44:36'),(3,'manager1','$2a$10$m8BSrbjEumGu3VQct3JBQ.2mG3d8vMNuVhCHOBSrd1Zahix23DaZG','张经理','13800138001','m1@example.com',NULL,'ADMIN',1,'2026-03-01 11:54:09','2026-03-01 12:39:56'),(4,'manager2','$2a$10$m8BSrbjEumGu3VQct3JBQ.2mG3d8vMNuVhCHOBSrd1Zahix23DaZG','李主管','13800138002','m2@example.com',NULL,'ADMIN',1,'2026-03-01 11:54:09','2026-03-01 12:39:56'),(5,'user001','$2a$10$m8BSrbjEumGu3VQct3JBQ.2mG3d8vMNuVhCHOBSrd1Zahix23DaZG','王小二','13911112222','w1@test.com',NULL,'USER',0,'2026-03-01 11:54:09','2026-03-01 12:39:56'),(6,'user002','$2a$10$m8BSrbjEumGu3VQct3JBQ.2mG3d8vMNuVhCHOBSrd1Zahix23DaZG','赵铁柱','13933334444','z2@test.com',NULL,'USER',1,'2026-03-01 11:54:09','2026-03-01 12:39:56'),(7,'user003','$2a$10$m8BSrbjEumGu3VQct3JBQ.2mG3d8vMNuVhCHOBSrd1Zahix23DaZG','孙美美','13955556666','s3@test.com',NULL,'USER',1,'2026-03-01 11:54:09','2026-03-01 12:39:56'),(8,'user004','$2a$10$m8BSrbjEumGu3VQct3JBQ.2mG3d8vMNuVhCHOBSrd1Zahix23DaZG','钱多多','13977778888','q4@test.com',NULL,'USER',1,'2026-03-01 11:54:09','2026-03-01 12:39:56'),(9,'user005','$2a$10$m8BSrbjEumGu3VQct3JBQ.2mG3d8vMNuVhCHOBSrd1Zahix23DaZG','周星星','13812345678','z5@test.com',NULL,'USER',1,'2026-03-01 11:54:09','2026-03-01 12:39:56'),(10,'user006','$2a$10$m8BSrbjEumGu3VQct3JBQ.2mG3d8vMNuVhCHOBSrd1Zahix23DaZG','吴所谓','13887654321','w6@test.com',NULL,'USER',1,'2026-03-01 11:54:09','2026-03-01 12:39:56'),(11,'user007','$2a$10$m8BSrbjEumGu3VQct3JBQ.2mG3d8vMNuVhCHOBSrd1Zahix23DaZG','郑能量','13700001111','z7@test.com',NULL,'USER',1,'2026-03-01 11:54:09','2026-03-01 12:39:56'),(12,'user008','$2a$10$m8BSrbjEumGu3VQct3JBQ.2mG3d8vMNuVhCHOBSrd1Zahix23DaZG','刘大宽','13722223333','f8@test.com',NULL,'USER',0,'2026-03-01 11:54:09','2026-03-03 16:15:58'),(13,'test','$2a$10$m8BSrbjEumGu3VQct3JBQ.2mG3d8vMNuVhCHOBSrd1Zahix23DaZG','张三',NULL,NULL,NULL,'USER',1,'2026-03-01 12:39:34','2026-03-01 12:39:34'),(14,'test2','$2a$10$0AAIsEtkhKzNguEQcNnum.10aLlhD0Zpv2TywQoHplO1AILo/pSnm','test2',NULL,NULL,NULL,'USER',1,'2026-03-03 16:52:56','2026-03-03 16:52:56');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'xingqi_erp'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-08 13:25:18
