/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : localhost:3306
 Source Schema         : groupbuying-order

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 19/09/2024 00:50:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cart_info
-- ----------------------------
DROP TABLE IF EXISTS `cart_info`;
CREATE TABLE `cart_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `category_id` bigint(20) NULL DEFAULT NULL COMMENT '分类id',
  `sku_type` tinyint(4) NULL DEFAULT NULL COMMENT 'sku类型',
  `sku_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'sku名称 (冗余)',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT 'skuid',
  `cart_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '放入购物车时价格',
  `sku_num` int(11) NULL DEFAULT NULL COMMENT '数量',
  `per_limit` int(11) NULL DEFAULT NULL COMMENT '限购个数',
  `img_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片文件',
  `is_checked` tinyint(1) NOT NULL DEFAULT 1,
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态（1：正常 0：无效）',
  `ware_id` bigint(20) NULL DEFAULT NULL COMMENT '仓库id',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '购物车表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_deliver
-- ----------------------------
DROP TABLE IF EXISTS `order_deliver`;
CREATE TABLE `order_deliver`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `deliver_date` datetime(0) NULL DEFAULT NULL COMMENT '配送日期',
  `leader_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '团长id',
  `driver_id` bigint(20) NULL DEFAULT NULL COMMENT '司机id',
  `driver_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '司机名称',
  `driver_phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '司机电话',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '状态（0：默认，1：已发货，2：团长收货）',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单配送表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '会员_id',
  `nick_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `order_no` char(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '订单号',
  `coupon_id` bigint(20) NULL DEFAULT NULL COMMENT '使用的优惠券',
  `total_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '订单总额',
  `activity_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '促销金额',
  `coupon_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '优惠券',
  `original_total_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '原价金额',
  `feight_fee` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '运费',
  `feight_fee_reduce` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '减免运费',
  `refundable_time` datetime(0) NULL DEFAULT NULL COMMENT '可退款日期（签收后1天）',
  `pay_type` tinyint(4) NULL DEFAULT NULL COMMENT '支付方式【1->微信】',
  `source_type` tinyint(4) NULL DEFAULT NULL COMMENT '订单来源[0->小程序；1->H5]',
  `order_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '订单状态【0->待付款；1->待发货；2->已发货；3->待用户收货，已完成；-1->已取消】',
  `process_status` tinyint(4) NOT NULL DEFAULT 0,
  `leader_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '团长id',
  `leader_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '团长名称',
  `leader_phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '团长电话',
  `take_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '提货点名称',
  `receiver_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人电话',
  `receiver_post_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人邮编',
  `receiver_province` bigint(20) NULL DEFAULT NULL COMMENT '省份/直辖市',
  `receiver_city` bigint(20) NULL DEFAULT NULL COMMENT '城市',
  `receiver_district` bigint(20) NULL DEFAULT NULL COMMENT '区',
  `receiver_address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `payment_time` datetime(0) NULL DEFAULT NULL COMMENT '支付时间',
  `delivery_time` datetime(0) NULL DEFAULT NULL COMMENT '发货时间',
  `take_time` datetime(0) NULL DEFAULT NULL COMMENT '提货时间',
  `receive_time` datetime(0) NULL DEFAULT NULL COMMENT '确认收货时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单备注',
  `cancel_time` datetime(0) NULL DEFAULT NULL COMMENT '取消订单时间',
  `cancel_reason` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '取消订单原因',
  `ware_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '仓库id',
  `commission_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '团长佣金',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 199 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_info
-- ----------------------------
INSERT INTO `order_info` VALUES (169, 31, NULL, '1682240682997', NULL, 10.80, 0.00, 0.00, 10.80, 0.00, 0.00, NULL, NULL, NULL, 0, 1, 3, '懂华', '15012222256', '北京魔方公寓店', '杨大力', '13978909876', NULL, NULL, NULL, NULL, '北京魔方公寓店', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0.00, '2023-04-23 17:04:58', '2023-04-23 17:04:58', 0);
INSERT INTO `order_info` VALUES (170, 31, NULL, '1682392293341', NULL, 3.50, 0.00, 0.00, 3.50, 0.00, 0.00, NULL, NULL, NULL, 0, 1, 3, '懂华', '15012222256', '北京魔方公寓店', '杨大力', '13980798765', NULL, NULL, NULL, NULL, '北京魔方公寓店', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0.00, '2023-04-25 11:12:16', '2023-04-25 11:12:16', 0);
INSERT INTO `order_info` VALUES (171, 31, NULL, '1682392518915', NULL, 10.80, 0.00, 0.00, 10.80, 0.00, 0.00, NULL, NULL, NULL, 0, 1, 3, '懂华', '15012222256', '北京魔方公寓店', '111', '13567890987', NULL, NULL, NULL, NULL, '北京魔方公寓店', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0.00, '2023-04-25 11:15:47', '2023-04-25 11:15:47', 0);
INSERT INTO `order_info` VALUES (172, 31, NULL, '1682393446100', NULL, 8.80, 0.00, 0.00, 8.80, 0.00, 0.00, NULL, NULL, NULL, 0, 1, 3, '懂华', '15012222256', '北京魔方公寓店', '333', '13567897890', NULL, NULL, NULL, NULL, '北京魔方公寓店', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0.00, '2023-04-25 11:30:56', '2023-04-25 11:30:56', 0);
INSERT INTO `order_info` VALUES (173, 31, NULL, '1682393662435', NULL, 5.30, 0.00, 0.00, 5.30, 0.00, 0.00, NULL, NULL, NULL, 0, 1, 3, '懂华', '15012222256', '北京魔方公寓店', '4', '13456789099', NULL, NULL, NULL, NULL, '北京魔方公寓店', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0.00, '2023-04-25 11:34:32', '2023-04-25 11:34:32', 0);
INSERT INTO `order_info` VALUES (174, 31, NULL, '1682393852353', NULL, 7.50, 0.00, 0.00, 7.50, 0.00, 0.00, NULL, NULL, NULL, 0, 1, 3, '懂华', '15012222256', '北京魔方公寓店', '444', '13456787654', NULL, NULL, NULL, NULL, '北京魔方公寓店', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0.00, '2023-04-25 11:37:58', '2023-04-25 11:37:58', 0);
INSERT INTO `order_info` VALUES (175, 31, NULL, '1682393996770', NULL, 5.30, 0.00, 0.00, 5.30, 0.00, 0.00, NULL, NULL, NULL, 0, 1, 3, '懂华', '15012222256', '北京魔方公寓店', '55', '13456543333', NULL, NULL, NULL, NULL, '北京魔方公寓店', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0.00, '2023-04-25 11:40:11', '2023-04-25 11:40:11', 0);
INSERT INTO `order_info` VALUES (176, 31, NULL, '1682394075104', NULL, 5.30, 0.00, 0.00, 5.30, 0.00, 0.00, NULL, NULL, NULL, 0, 1, 3, '懂华', '15012222256', '北京魔方公寓店', '666', '13667765544', NULL, NULL, NULL, NULL, '北京魔方公寓店', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0.00, '2023-04-25 11:41:23', '2023-04-25 11:41:23', 0);
INSERT INTO `order_info` VALUES (177, 31, NULL, '1682394273827', NULL, 5.30, 0.00, 0.00, 5.30, 0.00, 0.00, NULL, NULL, NULL, 0, 1, 3, '懂华', '15012222256', '北京魔方公寓店', '777', '13556654789', NULL, NULL, NULL, NULL, '北京魔方公寓店', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0.00, '2023-04-25 11:44:42', '2023-04-25 11:44:42', 0);
INSERT INTO `order_info` VALUES (178, 31, NULL, '1682394354592', NULL, 14.30, 0.00, 0.00, 14.30, 0.00, 0.00, NULL, NULL, NULL, 0, 1, 3, '懂华', '15012222256', '北京魔方公寓店', '9999', '13987654321', NULL, NULL, NULL, NULL, '北京魔方公寓店', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0.00, '2023-04-25 11:46:14', '2023-04-25 11:46:14', 0);
INSERT INTO `order_info` VALUES (179, 31, NULL, '1682415120296', NULL, 10.80, 0.00, 0.00, 10.80, 0.00, 0.00, NULL, NULL, NULL, 0, 1, 3, '懂华', '15012222256', '北京魔方公寓店', '4532', '13425431234', NULL, NULL, NULL, NULL, '北京魔方公寓店', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0.00, '2023-04-25 17:32:17', '2023-04-25 17:32:17', 0);
INSERT INTO `order_info` VALUES (180, 27, NULL, '1682416124200', NULL, 5.30, 0.00, 0.00, 5.30, 0.00, 0.00, NULL, NULL, NULL, 0, 1, 3, '懂华', '15012222256', '北京魔方公寓店', '56', '13421231234', NULL, NULL, NULL, NULL, '北京魔方公寓店', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0.00, '2023-04-25 17:48:53', '2023-04-25 17:48:53', 0);
INSERT INTO `order_info` VALUES (181, 31, NULL, '1682416819890', NULL, 5.30, 0.00, 0.00, 5.30, 0.00, 0.00, NULL, NULL, NULL, 0, 1, 3, '懂华', '15012222256', '北京魔方公寓店', '1', '13456789000', NULL, NULL, NULL, NULL, '北京魔方公寓店', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0.00, '2023-04-25 18:00:28', '2023-04-25 18:00:28', 0);
INSERT INTO `order_info` VALUES (182, 36, NULL, '1682417748665', NULL, 5.30, 0.00, 0.00, 5.30, 0.00, 0.00, NULL, NULL, NULL, 0, 1, 3, '懂华', '15012222256', '北京魔方公寓店', '5', '13456789090', NULL, NULL, NULL, NULL, '北京魔方公寓店', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0.00, '2023-04-25 18:15:57', '2023-04-25 18:15:57', 0);
INSERT INTO `order_info` VALUES (183, 36, NULL, '1682422583069', NULL, 5.00, 0.00, 0.00, 5.00, 0.00, 0.00, NULL, NULL, NULL, 0, 1, 3, '懂华', '15012222256', '北京魔方公寓店', '33', '13456789099', NULL, NULL, NULL, NULL, '北京魔方公寓店', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0.00, '2023-04-25 19:36:34', '2023-04-25 19:36:34', 0);
INSERT INTO `order_info` VALUES (184, 36, NULL, '1682422852768', NULL, 5.00, 0.00, 0.00, 5.00, 0.00, 0.00, NULL, NULL, NULL, 0, 1, 3, '懂华', '15012222256', '北京魔方公寓店', '3', '13454321234', NULL, NULL, NULL, NULL, '北京魔方公寓店', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0.00, '2023-04-25 19:40:59', '2023-04-25 19:40:59', 0);
INSERT INTO `order_info` VALUES (185, 36, NULL, '1682423016938', NULL, 5.50, 0.00, 0.00, 5.50, 0.00, 0.00, NULL, NULL, NULL, 0, 1, 3, '懂华', '15012222256', '北京魔方公寓店', '5', '13555555555', NULL, NULL, NULL, NULL, '北京魔方公寓店', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0.00, '2023-04-25 19:43:45', '2023-04-25 19:43:45', 0);
INSERT INTO `order_info` VALUES (186, 36, NULL, '1682423276196', NULL, 5.00, 0.00, 0.00, 5.00, 0.00, 0.00, NULL, NULL, NULL, 0, 1, 3, '懂华', '15012222256', '北京魔方公寓店', '5', '13456789000', NULL, NULL, NULL, NULL, '北京魔方公寓店', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0.00, '2023-04-25 19:48:03', '2023-04-25 19:48:03', 0);
INSERT INTO `order_info` VALUES (187, 36, NULL, '1682423709092', NULL, 5.30, 0.00, 0.00, 5.30, 0.00, 0.00, NULL, NULL, NULL, 0, 1, 3, '懂华', '15012222256', '北京魔方公寓店', '6', '13456789000', NULL, NULL, NULL, NULL, '北京魔方公寓店', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0.00, '2023-04-25 19:55:17', '2023-04-25 19:55:17', 0);
INSERT INTO `order_info` VALUES (188, 36, NULL, '1682423947261', NULL, 5.30, 0.00, 0.00, 5.30, 0.00, 0.00, NULL, NULL, NULL, 0, 1, 3, '懂华', '15012222256', '北京魔方公寓店', '4', '13423456789', NULL, NULL, NULL, NULL, '北京魔方公寓店', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0.00, '2023-04-25 19:59:14', '2023-04-25 19:59:14', 0);
INSERT INTO `order_info` VALUES (189, 36, NULL, '1682424457246', NULL, 2.20, 0.00, 0.00, 2.20, 0.00, 0.00, NULL, NULL, NULL, 0, 1, 3, '懂华', '15012222256', '北京魔方公寓店', '6', '13455431234', NULL, NULL, NULL, NULL, '北京魔方公寓店', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0.00, '2023-04-25 20:07:45', '2023-04-25 20:07:45', 0);
INSERT INTO `order_info` VALUES (190, 36, NULL, '1682425039734', NULL, 3.60, 0.00, 0.00, 3.60, 0.00, 0.00, NULL, NULL, NULL, 0, 1, 3, '懂华', '15012222256', '北京魔方公寓店', '6', '13667789900', NULL, NULL, NULL, NULL, '北京魔方公寓店', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0.00, '2023-04-25 20:17:27', '2023-04-25 20:17:27', 0);
INSERT INTO `order_info` VALUES (191, 36, NULL, '1682425829654', NULL, 3.60, 0.00, 0.00, 3.60, 0.00, 0.00, NULL, NULL, NULL, 0, 1, 3, '懂华', '15012222256', '北京魔方公寓店', '5', '13555555555', NULL, NULL, NULL, NULL, '北京魔方公寓店', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0.00, '2023-04-25 20:30:36', '2023-04-25 20:30:36', 0);
INSERT INTO `order_info` VALUES (192, 36, NULL, '1682426019007', NULL, 2.20, 0.00, 0.00, 2.20, 0.00, 0.00, NULL, NULL, NULL, 0, 1, 3, '懂华', '15012222256', '北京魔方公寓店', '6', '13666666666', NULL, NULL, NULL, NULL, '北京魔方公寓店', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0.00, '2023-04-25 20:33:47', '2023-04-25 20:33:47', 0);
INSERT INTO `order_info` VALUES (193, 36, NULL, '1682426174095', NULL, 5.00, 0.00, 0.00, 5.00, 0.00, 0.00, NULL, NULL, NULL, 0, 1, 3, '懂华', '15012222256', '北京魔方公寓店', '5', '13555567788', NULL, NULL, NULL, NULL, '北京魔方公寓店', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0.00, '2023-04-25 20:36:23', '2023-04-25 20:36:23', 0);
INSERT INTO `order_info` VALUES (194, 36, NULL, '1682426422674', NULL, 5.30, 0.00, 0.00, 5.30, 0.00, 0.00, NULL, NULL, NULL, 0, 1, 3, '懂华', '15012222256', '北京魔方公寓店', '6', '13445567788', NULL, NULL, NULL, NULL, '北京魔方公寓店', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0.00, '2023-04-25 20:40:29', '2023-04-25 20:40:29', 0);
INSERT INTO `order_info` VALUES (195, 36, NULL, '1682426743216', NULL, 2.20, 0.00, 0.00, 2.20, 0.00, 0.00, NULL, NULL, NULL, 0, 1, 3, '懂华', '15012222256', '北京魔方公寓店', '张大虎', '18978009876', NULL, NULL, NULL, NULL, '北京魔方公寓店', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0.00, '2023-04-25 20:46:02', '2023-04-25 20:46:02', 0);
INSERT INTO `order_info` VALUES (196, 36, NULL, '1682429647121', NULL, 7.09, 0.00, 0.00, 7.09, 0.00, 0.00, NULL, NULL, NULL, 0, 1, 3, '懂华', '15012222256', '北京魔方公寓店', '李二牛', '15678900000', NULL, NULL, NULL, NULL, '北京魔方公寓店', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0.00, '2023-04-25 21:34:27', '2023-04-25 21:34:27', 0);
INSERT INTO `order_info` VALUES (197, 36, NULL, '1682435683147', NULL, 2.20, 0.00, 0.00, 2.20, 0.00, 0.00, NULL, NULL, NULL, 1, 2, 3, '懂华', '15012222256', '北京魔方公寓店', '张三户', '13567899000', NULL, NULL, NULL, NULL, '北京魔方公寓店', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0.00, '2023-04-25 23:15:04', '2023-04-25 23:15:04', 0);
INSERT INTO `order_info` VALUES (198, 36, NULL, '1682573952476', NULL, 8.20, 2.00, 0.00, 10.20, 0.00, 0.00, NULL, NULL, NULL, 0, 1, 3, '懂华', '15012222256', '北京魔方公寓店', '杨大力', '13567899000', NULL, NULL, NULL, NULL, '北京魔方公寓店', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0.00, '2023-04-27 13:39:34', '2023-04-27 13:39:34', 0);

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT 'order_id',
  `category_id` bigint(20) NULL DEFAULT NULL COMMENT '商品分类id',
  `sku_type` tinyint(4) NULL DEFAULT NULL COMMENT 'sku类型',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT '商品sku编号',
  `sku_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品sku名字',
  `img_url` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品sku图片',
  `sku_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品sku价格',
  `sku_num` int(11) NULL DEFAULT NULL COMMENT '商品购买的数量',
  `split_activity_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品促销分解金额',
  `split_coupon_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠券优惠分解金额',
  `split_total_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '该商品经过优惠后的分解金额',
  `leader_id` bigint(20) NULL DEFAULT NULL COMMENT '团长id（冗余）',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 433 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单项信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_item
-- ----------------------------
INSERT INTO `order_item` VALUES (394, 169, 1, 0, 4, '大蒜', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/dasuan.jpg', 5.50, 1, 0.00, 0.00, 5.50, 3, '2023-04-23 17:04:58', '2023-04-23 17:04:58', 0);
INSERT INTO `order_item` VALUES (395, 169, 1, 0, 5, '土豆', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/tudou.jpg', 5.30, 1, 0.00, 0.00, 5.30, 3, '2023-04-23 17:04:58', '2023-04-23 17:04:58', 0);
INSERT INTO `order_item` VALUES (396, 170, 1, 0, 3, '四季豆', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/sijidou.jpg', 3.50, 1, 0.00, 0.00, 3.50, 3, '2023-04-25 11:12:16', '2023-04-25 11:12:16', 0);
INSERT INTO `order_item` VALUES (397, 171, 1, 0, 4, '大蒜', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/dasuan.jpg', 5.50, 1, 0.00, 0.00, 5.50, 3, '2023-04-25 11:15:47', '2023-04-25 11:15:47', 0);
INSERT INTO `order_item` VALUES (398, 171, 1, 0, 5, '土豆', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/tudou.jpg', 5.30, 1, 0.00, 0.00, 5.30, 3, '2023-04-25 11:15:47', '2023-04-25 11:15:47', 0);
INSERT INTO `order_item` VALUES (399, 172, 1, 0, 3, '四季豆', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/sijidou.jpg', 3.50, 1, 0.00, 0.00, 3.50, 3, '2023-04-25 11:30:56', '2023-04-25 11:30:56', 0);
INSERT INTO `order_item` VALUES (400, 172, 1, 0, 5, '土豆', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/tudou.jpg', 5.30, 1, 0.00, 0.00, 5.30, 3, '2023-04-25 11:30:56', '2023-04-25 11:30:56', 0);
INSERT INTO `order_item` VALUES (401, 173, 1, 0, 5, '土豆', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/tudou.jpg', 5.30, 1, 0.00, 0.00, 5.30, 3, '2023-04-25 11:34:32', '2023-04-25 11:34:32', 0);
INSERT INTO `order_item` VALUES (402, 174, 1, 0, 1, '西红柿', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/xihongshi.jpg', 2.20, 1, 0.00, 0.00, 2.20, 3, '2023-04-25 11:37:58', '2023-04-25 11:37:58', 0);
INSERT INTO `order_item` VALUES (403, 174, 1, 0, 5, '土豆', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/tudou.jpg', 5.30, 1, 0.00, 0.00, 5.30, 3, '2023-04-25 11:37:58', '2023-04-25 11:37:58', 0);
INSERT INTO `order_item` VALUES (404, 175, 1, 0, 5, '土豆', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/tudou.jpg', 5.30, 1, 0.00, 0.00, 5.30, 3, '2023-04-25 11:40:11', '2023-04-25 11:40:11', 0);
INSERT INTO `order_item` VALUES (405, 176, 1, 0, 5, '土豆', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/tudou.jpg', 5.30, 1, 0.00, 0.00, 5.30, 3, '2023-04-25 11:41:23', '2023-04-25 11:41:23', 0);
INSERT INTO `order_item` VALUES (406, 177, 1, 0, 5, '土豆', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/tudou.jpg', 5.30, 1, 0.00, 0.00, 5.30, 3, '2023-04-25 11:44:42', '2023-04-25 11:44:42', 0);
INSERT INTO `order_item` VALUES (407, 178, 1, 0, 3, '四季豆', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/sijidou.jpg', 3.50, 1, 0.00, 0.00, 3.50, 3, '2023-04-25 11:46:14', '2023-04-25 11:46:14', 0);
INSERT INTO `order_item` VALUES (408, 178, 1, 0, 4, '大蒜', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/dasuan.jpg', 5.50, 1, 0.00, 0.00, 5.50, 3, '2023-04-25 11:46:14', '2023-04-25 11:46:14', 0);
INSERT INTO `order_item` VALUES (409, 178, 1, 0, 5, '土豆', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/tudou.jpg', 5.30, 1, 0.00, 0.00, 5.30, 3, '2023-04-25 11:46:14', '2023-04-25 11:46:14', 0);
INSERT INTO `order_item` VALUES (410, 179, 1, 0, 4, '大蒜', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/dasuan.jpg', 5.50, 1, 0.00, 0.00, 5.50, 3, '2023-04-25 17:32:17', '2023-04-25 17:32:17', 0);
INSERT INTO `order_item` VALUES (411, 179, 1, 0, 5, '土豆', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/tudou.jpg', 5.30, 1, 0.00, 0.00, 5.30, 3, '2023-04-25 17:32:17', '2023-04-25 17:32:17', 0);
INSERT INTO `order_item` VALUES (412, 180, 1, 0, 5, '土豆', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/tudou.jpg', 5.30, 1, 0.00, 0.00, 5.30, 3, '2023-04-25 17:48:53', '2023-04-25 17:48:53', 0);
INSERT INTO `order_item` VALUES (413, 181, 1, 0, 5, '土豆', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/tudou.jpg', 5.30, 1, 0.00, 0.00, 5.30, 3, '2023-04-25 18:00:28', '2023-04-25 18:00:28', 0);
INSERT INTO `order_item` VALUES (414, 182, 1, 0, 5, '土豆', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/tudou.jpg', 5.30, 1, 0.00, 0.00, 5.30, 3, '2023-04-25 18:15:57', '2023-04-25 18:15:57', 0);
INSERT INTO `order_item` VALUES (415, 183, 2, 0, 11, '苹果', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/pngguo.jpg', 5.00, 1, 0.00, 0.00, 5.00, 3, '2023-04-25 19:36:34', '2023-04-25 19:36:34', 0);
INSERT INTO `order_item` VALUES (416, 184, 1, 0, 10, '南瓜', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/nangua.jpg', 5.00, 1, 0.00, 0.00, 5.00, 3, '2023-04-25 19:40:59', '2023-04-25 19:40:59', 0);
INSERT INTO `order_item` VALUES (417, 185, 1, 0, 4, '大蒜', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/dasuan.jpg', 5.50, 1, 0.00, 0.00, 5.50, 3, '2023-04-25 19:43:45', '2023-04-25 19:43:45', 0);
INSERT INTO `order_item` VALUES (418, 186, 1, 0, 10, '南瓜', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/nangua.jpg', 5.00, 1, 0.00, 0.00, 5.00, 3, '2023-04-25 19:48:03', '2023-04-25 19:48:03', 0);
INSERT INTO `order_item` VALUES (419, 187, 1, 0, 5, '土豆', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/tudou.jpg', 5.30, 1, 0.00, 0.00, 5.30, 3, '2023-04-25 19:55:17', '2023-04-25 19:55:17', 0);
INSERT INTO `order_item` VALUES (420, 188, 1, 0, 5, '土豆', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/tudou.jpg', 5.30, 1, 0.00, 0.00, 5.30, 3, '2023-04-25 19:59:14', '2023-04-25 19:59:14', 0);
INSERT INTO `order_item` VALUES (421, 189, 1, 0, 1, '西红柿', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/xihongshi.jpg', 2.20, 1, 0.00, 0.00, 2.20, 3, '2023-04-25 20:07:45', '2023-04-25 20:07:45', 0);
INSERT INTO `order_item` VALUES (422, 190, 1, 0, 6, '丝瓜', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/sigua.jpg', 3.60, 1, 0.00, 0.00, 3.60, 3, '2023-04-25 20:17:27', '2023-04-25 20:17:27', 0);
INSERT INTO `order_item` VALUES (423, 191, 1, 0, 6, '丝瓜', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/sigua.jpg', 3.60, 1, 0.00, 0.00, 3.60, 3, '2023-04-25 20:30:36', '2023-04-25 20:30:36', 0);
INSERT INTO `order_item` VALUES (424, 192, 1, 0, 1, '西红柿', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/xihongshi.jpg', 2.20, 1, 0.00, 0.00, 2.20, 3, '2023-04-25 20:33:47', '2023-04-25 20:33:47', 0);
INSERT INTO `order_item` VALUES (425, 193, 1, 0, 10, '南瓜', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/nangua.jpg', 5.00, 1, 0.00, 0.00, 5.00, 3, '2023-04-25 20:36:23', '2023-04-25 20:36:23', 0);
INSERT INTO `order_item` VALUES (426, 194, 1, 0, 5, '土豆', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/tudou.jpg', 5.30, 1, 0.00, 0.00, 5.30, 3, '2023-04-25 20:40:29', '2023-04-25 20:40:29', 0);
INSERT INTO `order_item` VALUES (427, 195, 1, 0, 1, '西红柿', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/xihongshi.jpg', 2.20, 1, 0.00, 0.00, 2.20, 3, '2023-04-25 20:46:02', '2023-04-25 20:46:02', 0);
INSERT INTO `order_item` VALUES (428, 196, 1, 0, 2, '红薯', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/hongshu.jpg', 1.79, 1, 0.00, 0.00, 1.79, 3, '2023-04-25 21:34:27', '2023-04-25 21:34:27', 0);
INSERT INTO `order_item` VALUES (429, 196, 1, 0, 5, '土豆', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/tudou.jpg', 5.30, 1, 0.00, 0.00, 5.30, 3, '2023-04-25 21:34:27', '2023-04-25 21:34:27', 0);
INSERT INTO `order_item` VALUES (430, 197, 1, 0, 1, '西红柿', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/xihongshi.jpg', 2.20, 1, 0.00, 0.00, 2.20, 3, '2023-04-25 23:15:04', '2023-04-25 23:15:04', 0);
INSERT INTO `order_item` VALUES (431, 198, 1, 0, 1, '西红柿', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/xihongshi.jpg', 2.20, 3, 2.00, 0.00, 4.60, 3, '2023-04-27 13:39:34', '2023-04-27 13:39:34', 0);
INSERT INTO `order_item` VALUES (432, 198, 1, 0, 6, '丝瓜', 'https://ssyx-guigu.oss-cn-beijing.aliyuncs.com/img/sigua.jpg', 3.60, 1, 0.00, 0.00, 3.60, 3, '2023-04-27 13:39:34', '2023-04-27 13:39:34', 0);

-- ----------------------------
-- Table structure for order_log
-- ----------------------------
DROP TABLE IF EXISTS `order_log`;
CREATE TABLE `order_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单id',
  `operate_user` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人：用户；系统；后台管理员',
  `process_status` int(11) NULL DEFAULT NULL COMMENT '订单状态',
  `note` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 177 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单操作日志记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_log
-- ----------------------------
INSERT INTO `order_log` VALUES (1, 1, NULL, 1, NULL, '2021-08-18 14:14:01', '2021-08-18 14:14:01', 0);
INSERT INTO `order_log` VALUES (2, 2, NULL, 1, NULL, '2021-09-14 16:17:35', '2021-09-14 16:17:35', 0);
INSERT INTO `order_log` VALUES (3, 3, NULL, 1, NULL, '2021-09-14 19:46:10', '2021-09-14 19:46:10', 0);
INSERT INTO `order_log` VALUES (4, 4, NULL, 1, NULL, '2021-09-14 20:34:03', '2021-09-14 20:34:03', 0);
INSERT INTO `order_log` VALUES (5, 5, NULL, 1, NULL, '2021-09-14 21:09:14', '2021-09-14 21:09:14', 0);
INSERT INTO `order_log` VALUES (6, 6, NULL, 1, NULL, '2021-09-14 21:29:21', '2021-09-14 21:29:21', 0);
INSERT INTO `order_log` VALUES (7, 7, NULL, 1, NULL, '2021-09-14 21:30:22', '2021-09-14 21:30:22', 0);
INSERT INTO `order_log` VALUES (8, 8, NULL, 1, NULL, '2021-09-14 21:35:19', '2021-09-14 21:35:19', 0);
INSERT INTO `order_log` VALUES (9, 9, NULL, 1, NULL, '2021-09-14 21:50:04', '2021-09-14 21:50:04', 0);
INSERT INTO `order_log` VALUES (10, 10, NULL, 1, NULL, '2021-09-15 17:07:21', '2021-09-15 17:07:21', 0);
INSERT INTO `order_log` VALUES (11, 11, NULL, 1, NULL, '2021-09-15 17:09:17', '2021-09-15 17:09:17', 0);
INSERT INTO `order_log` VALUES (12, 12, NULL, 1, NULL, '2021-09-15 17:10:32', '2021-09-15 17:10:32', 0);
INSERT INTO `order_log` VALUES (13, 13, NULL, 1, NULL, '2021-09-15 18:23:41', '2021-09-15 18:23:41', 0);
INSERT INTO `order_log` VALUES (14, 14, NULL, 1, NULL, '2021-09-15 18:24:18', '2021-09-15 18:24:18', 0);
INSERT INTO `order_log` VALUES (15, 15, NULL, 1, NULL, '2021-09-15 18:25:14', '2021-09-15 18:25:14', 0);
INSERT INTO `order_log` VALUES (16, 16, NULL, 1, NULL, '2021-09-15 18:27:52', '2021-09-15 18:27:52', 0);
INSERT INTO `order_log` VALUES (17, 17, NULL, 1, NULL, '2021-09-15 18:30:22', '2021-09-15 18:30:22', 0);
INSERT INTO `order_log` VALUES (18, 18, NULL, 1, NULL, '2021-09-15 18:31:49', '2021-09-15 18:31:49', 0);
INSERT INTO `order_log` VALUES (19, 19, NULL, 1, NULL, '2021-09-15 18:36:05', '2021-09-15 18:36:05', 0);
INSERT INTO `order_log` VALUES (20, 20, NULL, 1, NULL, '2021-09-15 18:38:09', '2021-09-15 18:38:09', 0);
INSERT INTO `order_log` VALUES (21, 21, NULL, 1, NULL, '2021-09-15 18:39:56', '2021-09-15 18:39:56', 0);
INSERT INTO `order_log` VALUES (22, 22, NULL, 1, NULL, '2021-09-15 18:41:35', '2021-09-15 18:41:35', 0);
INSERT INTO `order_log` VALUES (23, 23, NULL, 1, NULL, '2021-09-15 18:44:45', '2021-09-15 18:44:45', 0);
INSERT INTO `order_log` VALUES (24, 24, NULL, 1, NULL, '2021-09-15 18:45:42', '2021-09-15 18:45:42', 0);
INSERT INTO `order_log` VALUES (25, 25, NULL, 1, NULL, '2021-09-15 18:51:35', '2021-09-15 18:51:35', 0);
INSERT INTO `order_log` VALUES (26, 26, NULL, 1, NULL, '2021-09-15 18:55:58', '2021-09-15 18:55:58', 0);
INSERT INTO `order_log` VALUES (27, 27, NULL, 1, NULL, '2021-09-15 18:57:56', '2021-09-15 18:57:56', 0);
INSERT INTO `order_log` VALUES (28, 28, NULL, 1, NULL, '2021-09-15 19:42:42', '2021-09-15 19:42:42', 0);
INSERT INTO `order_log` VALUES (29, 29, NULL, 1, NULL, '2021-09-15 19:44:42', '2021-09-15 19:44:42', 0);
INSERT INTO `order_log` VALUES (30, 30, NULL, 1, NULL, '2021-09-15 20:11:28', '2021-09-15 20:11:28', 0);
INSERT INTO `order_log` VALUES (31, 31, NULL, 1, NULL, '2021-09-15 20:15:06', '2021-09-15 20:15:06', 0);
INSERT INTO `order_log` VALUES (32, 32, NULL, 1, NULL, '2021-09-15 20:16:52', '2021-09-15 20:16:52', 0);
INSERT INTO `order_log` VALUES (33, 33, NULL, 1, NULL, '2021-09-15 20:18:37', '2021-09-15 20:18:37', 0);
INSERT INTO `order_log` VALUES (34, 31, NULL, 2, NULL, '2021-09-15 21:16:27', '2021-09-15 21:16:27', 0);
INSERT INTO `order_log` VALUES (35, 31, NULL, 2, NULL, '2021-09-15 21:16:29', '2021-09-15 21:16:29', 0);
INSERT INTO `order_log` VALUES (36, 32, NULL, 2, NULL, '2021-09-15 21:17:05', '2021-09-15 21:17:05', 0);
INSERT INTO `order_log` VALUES (37, 33, NULL, 2, NULL, '2021-09-15 21:17:19', '2021-09-15 21:17:19', 0);
INSERT INTO `order_log` VALUES (38, 34, NULL, 1, NULL, '2021-09-16 07:30:24', '2021-09-16 07:30:24', 0);
INSERT INTO `order_log` VALUES (39, 34, NULL, 2, NULL, '2021-09-16 07:38:20', '2021-09-16 07:38:20', 0);
INSERT INTO `order_log` VALUES (40, 20, NULL, 2, NULL, '2021-09-16 08:53:52', '2021-09-16 08:53:52', 0);
INSERT INTO `order_log` VALUES (41, 35, NULL, 1, NULL, '2021-09-16 20:06:47', '2021-09-16 20:06:47', 0);
INSERT INTO `order_log` VALUES (42, 36, NULL, 1, NULL, '2021-09-16 20:08:00', '2021-09-16 20:08:00', 0);
INSERT INTO `order_log` VALUES (43, 36, NULL, 2, NULL, '2021-09-16 20:08:13', '2021-09-16 20:08:13', 0);
INSERT INTO `order_log` VALUES (44, 37, NULL, 1, NULL, '2021-09-18 15:18:50', '2021-09-18 15:18:50', 0);
INSERT INTO `order_log` VALUES (45, 37, NULL, 2, NULL, '2021-09-18 15:19:19', '2021-09-18 15:19:19', 0);
INSERT INTO `order_log` VALUES (46, 38, NULL, 1, NULL, '2021-09-19 16:46:03', '2021-09-19 16:46:03', 0);
INSERT INTO `order_log` VALUES (47, 38, NULL, 2, NULL, '2021-09-19 16:46:18', '2021-09-19 16:46:18', 0);
INSERT INTO `order_log` VALUES (48, 39, NULL, 1, NULL, '2021-09-27 17:17:59', '2021-09-27 17:17:59', 0);
INSERT INTO `order_log` VALUES (49, 39, NULL, 2, NULL, '2021-09-27 17:18:12', '2021-09-27 17:18:12', 0);
INSERT INTO `order_log` VALUES (50, 40, NULL, 1, NULL, '2021-09-28 07:21:33', '2021-09-28 07:21:33', 0);
INSERT INTO `order_log` VALUES (51, 41, NULL, 1, NULL, '2021-09-28 08:17:39', '2021-09-28 08:17:39', 0);
INSERT INTO `order_log` VALUES (52, 42, NULL, 1, NULL, '2021-09-28 08:18:59', '2021-09-28 08:18:59', 0);
INSERT INTO `order_log` VALUES (53, 42, NULL, 2, NULL, '2021-09-28 08:19:13', '2021-09-28 08:19:13', 0);
INSERT INTO `order_log` VALUES (54, 43, NULL, 1, NULL, '2021-09-29 05:33:05', '2021-09-29 05:33:05', 0);
INSERT INTO `order_log` VALUES (55, 44, NULL, 1, NULL, '2021-09-29 09:31:27', '2021-09-29 09:31:27', 0);
INSERT INTO `order_log` VALUES (56, 45, NULL, 1, NULL, '2021-09-29 23:40:59', '2021-09-29 23:40:59', 0);
INSERT INTO `order_log` VALUES (57, 46, NULL, 1, NULL, '2021-09-29 23:41:22', '2021-09-29 23:41:22', 0);
INSERT INTO `order_log` VALUES (58, 47, NULL, 1, NULL, '2021-09-29 23:43:19', '2021-09-29 23:43:19', 0);
INSERT INTO `order_log` VALUES (59, 48, NULL, 1, NULL, '2021-10-12 07:39:20', '2021-10-12 07:39:20', 0);
INSERT INTO `order_log` VALUES (60, 49, NULL, 1, NULL, '2021-10-13 01:51:16', '2021-10-13 01:51:16', 0);
INSERT INTO `order_log` VALUES (61, 50, NULL, 1, NULL, '2021-10-19 04:56:43', '2021-10-19 04:56:43', 0);
INSERT INTO `order_log` VALUES (62, 51, NULL, 1, NULL, '2021-10-19 05:42:26', '2021-10-19 05:42:26', 0);
INSERT INTO `order_log` VALUES (63, 52, NULL, 1, NULL, '2021-10-20 07:37:17', '2021-10-20 07:37:17', 0);
INSERT INTO `order_log` VALUES (64, 53, NULL, 1, NULL, '2021-10-20 07:42:36', '2021-10-20 07:42:36', 0);
INSERT INTO `order_log` VALUES (65, 54, NULL, 1, NULL, '2021-10-21 03:01:48', '2021-10-21 03:01:48', 0);
INSERT INTO `order_log` VALUES (66, 55, NULL, 1, NULL, '2021-10-21 03:38:41', '2021-10-21 03:38:41', 0);
INSERT INTO `order_log` VALUES (67, 56, NULL, 1, NULL, '2021-11-12 08:28:20', '2021-11-12 08:28:20', 0);
INSERT INTO `order_log` VALUES (68, 57, NULL, 1, NULL, '2021-11-12 09:20:35', '2021-11-12 09:20:35', 0);
INSERT INTO `order_log` VALUES (69, 58, NULL, 1, NULL, '2021-11-12 09:22:05', '2021-11-12 09:22:05', 0);
INSERT INTO `order_log` VALUES (70, 59, NULL, 1, NULL, '2021-11-12 12:11:24', '2021-11-12 12:11:24', 0);
INSERT INTO `order_log` VALUES (71, 60, NULL, 1, NULL, '2021-11-15 09:15:42', '2021-11-15 09:15:42', 0);
INSERT INTO `order_log` VALUES (72, 61, NULL, 1, NULL, '2021-11-19 07:26:29', '2021-11-19 07:26:29', 0);
INSERT INTO `order_log` VALUES (73, 62, NULL, 1, NULL, '2021-11-19 07:28:54', '2021-11-19 07:28:54', 0);
INSERT INTO `order_log` VALUES (74, 63, NULL, 1, NULL, '2021-11-19 07:39:28', '2021-11-19 07:39:28', 0);
INSERT INTO `order_log` VALUES (75, 64, NULL, 1, NULL, '2021-11-19 07:41:05', '2021-11-19 07:41:05', 0);
INSERT INTO `order_log` VALUES (76, 65, NULL, 1, NULL, '2021-11-19 08:26:38', '2021-11-19 08:26:38', 0);
INSERT INTO `order_log` VALUES (77, 66, NULL, 1, NULL, '2021-11-19 08:30:49', '2021-11-19 08:30:49', 0);
INSERT INTO `order_log` VALUES (78, 67, NULL, 1, NULL, '2021-11-19 10:21:30', '2021-11-19 10:21:30', 0);
INSERT INTO `order_log` VALUES (79, 68, NULL, 1, NULL, '2021-11-19 14:59:36', '2021-11-19 14:59:36', 0);
INSERT INTO `order_log` VALUES (80, 69, NULL, 1, NULL, '2021-11-19 14:59:37', '2021-11-19 14:59:37', 0);
INSERT INTO `order_log` VALUES (81, 70, NULL, 1, NULL, '2021-11-19 14:59:37', '2021-11-19 14:59:37', 0);
INSERT INTO `order_log` VALUES (82, 71, NULL, 1, NULL, '2021-11-19 16:34:57', '2021-11-19 16:34:57', 0);
INSERT INTO `order_log` VALUES (83, 72, NULL, 1, NULL, '2021-11-20 01:58:25', '2021-11-20 01:58:25', 0);
INSERT INTO `order_log` VALUES (84, 73, NULL, 1, NULL, '2021-11-20 02:24:59', '2021-11-20 02:24:59', 0);
INSERT INTO `order_log` VALUES (85, 74, NULL, 1, NULL, '2021-11-20 02:35:15', '2021-11-20 02:35:15', 0);
INSERT INTO `order_log` VALUES (86, 75, NULL, 1, NULL, '2021-11-20 04:58:09', '2021-11-20 04:58:09', 0);
INSERT INTO `order_log` VALUES (87, 76, NULL, 1, NULL, '2021-11-20 05:02:46', '2021-11-20 05:02:46', 0);
INSERT INTO `order_log` VALUES (88, 77, NULL, 1, NULL, '2021-11-20 06:46:45', '2021-11-20 06:46:45', 0);
INSERT INTO `order_log` VALUES (89, 78, NULL, 1, NULL, '2021-11-20 07:55:31', '2021-11-20 07:55:31', 0);
INSERT INTO `order_log` VALUES (90, 79, NULL, 1, NULL, '2021-11-20 08:54:05', '2021-11-20 08:54:05', 0);
INSERT INTO `order_log` VALUES (91, 80, NULL, 1, NULL, '2021-11-20 09:32:21', '2021-11-20 09:32:21', 0);
INSERT INTO `order_log` VALUES (92, 81, NULL, 1, NULL, '2021-11-20 09:33:28', '2021-11-20 09:33:28', 0);
INSERT INTO `order_log` VALUES (93, 82, NULL, 1, NULL, '2021-11-20 13:45:43', '2021-11-20 13:45:43', 0);
INSERT INTO `order_log` VALUES (94, 83, NULL, 1, NULL, '2021-11-20 13:59:58', '2021-11-20 13:59:58', 0);
INSERT INTO `order_log` VALUES (95, 84, NULL, 1, NULL, '2021-11-22 00:53:36', '2021-11-22 00:53:36', 0);
INSERT INTO `order_log` VALUES (96, 85, NULL, 1, NULL, '2021-11-22 03:56:22', '2021-11-22 03:56:22', 0);
INSERT INTO `order_log` VALUES (97, 86, NULL, 1, NULL, '2021-11-22 06:21:16', '2021-11-22 06:21:16', 0);
INSERT INTO `order_log` VALUES (98, 87, NULL, 1, NULL, '2021-11-22 06:37:03', '2021-11-22 06:37:03', 0);
INSERT INTO `order_log` VALUES (99, 88, NULL, 1, NULL, '2021-11-22 11:07:35', '2021-11-22 11:07:35', 0);
INSERT INTO `order_log` VALUES (100, 89, NULL, 1, NULL, '2021-11-22 11:08:22', '2021-11-22 11:08:22', 0);
INSERT INTO `order_log` VALUES (101, 90, NULL, 1, NULL, '2021-11-22 11:21:46', '2021-11-22 11:21:46', 0);
INSERT INTO `order_log` VALUES (102, 91, NULL, 1, NULL, '2021-11-23 01:11:47', '2021-11-23 01:11:47', 0);
INSERT INTO `order_log` VALUES (103, 92, NULL, 1, NULL, '2021-11-23 01:15:47', '2021-11-23 01:15:47', 0);
INSERT INTO `order_log` VALUES (104, 93, NULL, 1, NULL, '2021-11-23 01:17:31', '2021-11-23 01:17:31', 0);
INSERT INTO `order_log` VALUES (105, 94, NULL, 1, NULL, '2021-11-23 01:18:21', '2021-11-23 01:18:21', 0);
INSERT INTO `order_log` VALUES (106, 95, NULL, 1, NULL, '2021-11-23 01:19:39', '2021-11-23 01:19:39', 0);
INSERT INTO `order_log` VALUES (107, 96, NULL, 1, NULL, '2021-11-23 01:23:14', '2021-11-23 01:23:14', 0);
INSERT INTO `order_log` VALUES (108, 97, NULL, 1, NULL, '2021-11-23 01:33:25', '2021-11-23 01:33:25', 0);
INSERT INTO `order_log` VALUES (109, 98, NULL, 1, NULL, '2021-11-23 01:49:42', '2021-11-23 01:49:42', 0);
INSERT INTO `order_log` VALUES (110, 99, NULL, 1, NULL, '2021-11-23 01:55:31', '2021-11-23 01:55:31', 0);
INSERT INTO `order_log` VALUES (111, 100, NULL, 1, NULL, '2021-11-23 01:57:31', '2021-11-23 01:57:31', 0);
INSERT INTO `order_log` VALUES (112, 101, NULL, 1, NULL, '2021-11-23 01:59:38', '2021-11-23 01:59:38', 0);
INSERT INTO `order_log` VALUES (113, 102, NULL, 1, NULL, '2021-11-23 02:00:09', '2021-11-23 02:00:09', 0);
INSERT INTO `order_log` VALUES (114, 103, NULL, 1, NULL, '2021-11-23 02:00:10', '2021-11-23 02:00:10', 0);
INSERT INTO `order_log` VALUES (115, 104, NULL, 1, NULL, '2021-11-23 02:05:51', '2021-11-23 02:05:51', 0);
INSERT INTO `order_log` VALUES (116, 105, NULL, 1, NULL, '2021-11-23 02:13:26', '2021-11-23 02:13:26', 0);
INSERT INTO `order_log` VALUES (117, 106, NULL, 1, NULL, '2021-11-23 02:13:51', '2021-11-23 02:13:51', 0);
INSERT INTO `order_log` VALUES (118, 107, NULL, 1, NULL, '2021-11-23 02:14:59', '2021-11-23 02:14:59', 0);
INSERT INTO `order_log` VALUES (119, 108, NULL, 1, NULL, '2021-11-23 02:15:47', '2021-11-23 02:15:47', 0);
INSERT INTO `order_log` VALUES (120, 109, NULL, 1, NULL, '2021-11-23 02:26:16', '2021-11-23 02:26:16', 0);
INSERT INTO `order_log` VALUES (121, 110, NULL, 1, NULL, '2021-11-23 02:30:24', '2021-11-23 02:30:24', 0);
INSERT INTO `order_log` VALUES (122, 111, NULL, 1, NULL, '2021-11-23 02:32:51', '2021-11-23 02:32:51', 0);
INSERT INTO `order_log` VALUES (123, 112, NULL, 1, NULL, '2021-11-23 07:01:12', '2021-11-23 07:01:12', 0);
INSERT INTO `order_log` VALUES (124, 113, NULL, 1, NULL, '2021-11-23 07:01:46', '2021-11-23 07:01:46', 0);
INSERT INTO `order_log` VALUES (125, 114, NULL, 1, NULL, '2021-11-23 07:02:44', '2021-11-23 07:02:44', 0);
INSERT INTO `order_log` VALUES (126, 115, NULL, 1, NULL, '2021-11-23 07:15:25', '2021-11-23 07:15:25', 0);
INSERT INTO `order_log` VALUES (127, 116, NULL, 1, NULL, '2021-11-23 07:18:28', '2021-11-23 07:18:28', 0);
INSERT INTO `order_log` VALUES (128, 117, NULL, 1, NULL, '2021-11-23 07:18:45', '2021-11-23 07:18:45', 0);
INSERT INTO `order_log` VALUES (129, 118, NULL, 1, NULL, '2021-11-23 07:20:25', '2021-11-23 07:20:25', 0);
INSERT INTO `order_log` VALUES (130, 119, NULL, 1, NULL, '2021-11-23 07:38:28', '2021-11-23 07:38:28', 0);
INSERT INTO `order_log` VALUES (131, 120, NULL, 1, NULL, '2021-11-23 07:48:15', '2021-11-23 07:48:15', 0);
INSERT INTO `order_log` VALUES (132, 121, NULL, 1, NULL, '2021-11-23 07:48:31', '2021-11-23 07:48:31', 0);
INSERT INTO `order_log` VALUES (133, 122, NULL, 1, NULL, '2021-11-23 07:50:48', '2021-11-23 07:50:48', 0);
INSERT INTO `order_log` VALUES (134, 123, NULL, 1, NULL, '2021-11-23 08:02:51', '2021-11-23 08:02:51', 0);
INSERT INTO `order_log` VALUES (135, 124, NULL, 1, NULL, '2021-11-23 08:45:21', '2021-11-23 08:45:21', 0);
INSERT INTO `order_log` VALUES (136, 125, NULL, 1, NULL, '2021-11-23 08:47:22', '2021-11-23 08:47:22', 0);
INSERT INTO `order_log` VALUES (137, 126, NULL, 1, NULL, '2021-11-23 12:12:22', '2021-11-23 12:12:22', 0);
INSERT INTO `order_log` VALUES (138, 127, NULL, 1, NULL, '2021-11-24 00:43:29', '2021-11-24 00:43:29', 0);
INSERT INTO `order_log` VALUES (139, 128, NULL, 1, NULL, '2021-11-24 00:47:21', '2021-11-24 00:47:21', 0);
INSERT INTO `order_log` VALUES (140, 129, NULL, 1, NULL, '2021-11-24 00:48:26', '2021-11-24 00:48:26', 0);
INSERT INTO `order_log` VALUES (141, 130, NULL, 1, NULL, '2021-11-24 00:48:50', '2021-11-24 00:48:50', 0);
INSERT INTO `order_log` VALUES (142, 131, NULL, 1, NULL, '2021-11-24 00:57:07', '2021-11-24 00:57:07', 0);
INSERT INTO `order_log` VALUES (143, 132, NULL, 1, NULL, '2021-11-24 01:01:43', '2021-11-24 01:01:43', 0);
INSERT INTO `order_log` VALUES (144, 133, NULL, 1, NULL, '2021-11-24 01:03:45', '2021-11-24 01:03:45', 0);
INSERT INTO `order_log` VALUES (145, 134, NULL, 1, NULL, '2021-11-24 01:18:33', '2021-11-24 01:18:33', 0);
INSERT INTO `order_log` VALUES (146, 135, NULL, 1, NULL, '2021-11-24 01:28:08', '2021-11-24 01:28:08', 0);
INSERT INTO `order_log` VALUES (147, 136, NULL, 1, NULL, '2021-11-24 01:30:36', '2021-11-24 01:30:36', 0);
INSERT INTO `order_log` VALUES (148, 137, NULL, 1, NULL, '2021-11-24 07:53:39', '2021-11-24 07:53:39', 0);
INSERT INTO `order_log` VALUES (149, 138, NULL, 1, NULL, '2021-11-24 08:13:03', '2021-11-24 08:13:03', 0);
INSERT INTO `order_log` VALUES (150, 139, NULL, 1, NULL, '2021-11-24 08:28:35', '2021-11-24 08:28:35', 0);
INSERT INTO `order_log` VALUES (151, 140, NULL, 1, NULL, '2021-11-24 08:31:13', '2021-11-24 08:31:13', 0);
INSERT INTO `order_log` VALUES (152, 141, NULL, 1, NULL, '2021-11-24 08:42:12', '2021-11-24 08:42:12', 0);
INSERT INTO `order_log` VALUES (153, 142, NULL, 1, NULL, '2021-11-24 08:53:00', '2021-11-24 08:53:00', 0);
INSERT INTO `order_log` VALUES (154, 143, NULL, 1, NULL, '2021-11-24 08:54:37', '2021-11-24 08:54:37', 0);
INSERT INTO `order_log` VALUES (155, 144, NULL, 1, NULL, '2021-11-24 08:55:26', '2021-11-24 08:55:26', 0);
INSERT INTO `order_log` VALUES (156, 145, NULL, 1, NULL, '2021-11-24 09:44:52', '2021-11-24 09:44:52', 0);
INSERT INTO `order_log` VALUES (157, 146, NULL, 1, NULL, '2021-11-24 10:00:16', '2021-11-24 10:00:16', 0);
INSERT INTO `order_log` VALUES (158, 147, NULL, 1, NULL, '2021-11-25 01:23:08', '2021-11-25 01:23:08', 0);
INSERT INTO `order_log` VALUES (159, 148, NULL, 1, NULL, '2021-11-25 02:23:50', '2021-11-25 02:23:50', 0);
INSERT INTO `order_log` VALUES (160, 149, NULL, 1, NULL, '2021-11-25 02:33:49', '2021-11-25 02:33:49', 0);
INSERT INTO `order_log` VALUES (161, 150, NULL, 1, NULL, '2021-11-25 02:34:44', '2021-11-25 02:34:44', 0);
INSERT INTO `order_log` VALUES (162, 151, NULL, 1, NULL, '2021-11-25 02:36:41', '2021-11-25 02:36:41', 0);
INSERT INTO `order_log` VALUES (163, 152, NULL, 1, NULL, '2021-11-25 02:37:21', '2021-11-25 02:37:21', 0);
INSERT INTO `order_log` VALUES (164, 153, NULL, 1, NULL, '2021-11-25 02:39:19', '2021-11-25 02:39:19', 0);
INSERT INTO `order_log` VALUES (165, 154, NULL, 1, NULL, '2021-11-25 02:48:13', '2021-11-25 02:48:13', 0);
INSERT INTO `order_log` VALUES (166, 155, NULL, 1, NULL, '2021-11-25 02:48:56', '2021-11-25 02:48:56', 0);
INSERT INTO `order_log` VALUES (167, 156, NULL, 1, NULL, '2021-11-25 02:51:05', '2021-11-25 02:51:05', 0);
INSERT INTO `order_log` VALUES (168, 157, NULL, 1, NULL, '2021-11-25 02:51:23', '2021-11-25 02:51:23', 0);
INSERT INTO `order_log` VALUES (169, 158, NULL, 1, NULL, '2021-11-25 02:55:53', '2021-11-25 02:55:53', 0);
INSERT INTO `order_log` VALUES (170, 159, NULL, 1, NULL, '2021-11-25 02:56:26', '2021-11-25 02:56:26', 0);
INSERT INTO `order_log` VALUES (171, 160, NULL, 1, NULL, '2021-11-25 03:00:53', '2021-11-25 03:00:53', 0);
INSERT INTO `order_log` VALUES (172, 161, NULL, 1, NULL, '2021-11-26 01:25:31', '2021-11-26 01:25:31', 0);
INSERT INTO `order_log` VALUES (173, 162, NULL, 1, NULL, '2021-11-26 03:28:41', '2021-11-26 03:28:41', 0);
INSERT INTO `order_log` VALUES (174, 163, NULL, 1, NULL, '2021-11-26 03:49:56', '2021-11-26 03:49:56', 0);
INSERT INTO `order_log` VALUES (175, 164, NULL, 1, NULL, '2023-03-18 11:05:06', '2023-03-18 11:05:06', 0);
INSERT INTO `order_log` VALUES (176, 165, NULL, 1, NULL, '2023-03-18 11:35:19', '2023-03-18 11:35:19', 0);

-- ----------------------------
-- Table structure for order_return_apply
-- ----------------------------
DROP TABLE IF EXISTS `order_return_apply`;
CREATE TABLE `order_return_apply`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单id',
  `merchant_id` bigint(20) NULL DEFAULT NULL COMMENT '团长门店id',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT '退货商品id',
  `order_sn` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单编号',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '会员用户名',
  `return_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '退款金额',
  `return_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退货人姓名',
  `return_phone` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退货人电话',
  `status` int(11) NULL DEFAULT NULL COMMENT '申请状态：0->待处理；1->退货中；2->已完成；3->已拒绝',
  `handle_time` datetime(0) NULL DEFAULT NULL COMMENT '处理时间',
  `sku_pic` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品图片',
  `sku_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `sku_num` int(11) NULL DEFAULT NULL COMMENT '退货数量',
  `sku_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品单价',
  `sku_real_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品实际支付单价',
  `reason` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '原因',
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `proof_pics` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '凭证图片，以逗号隔开',
  `handle_note` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '处理备注',
  `handle_man` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '处理人员',
  `receive_man` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人',
  `receive_time` datetime(0) NULL DEFAULT NULL COMMENT '收货时间',
  `receive_note` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货备注',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单退货申请' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_return_reason
-- ----------------------------
DROP TABLE IF EXISTS `order_return_reason`;
CREATE TABLE `order_return_reason`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退货类型',
  `sort` int(11) NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT NULL COMMENT '状态：0->不启用；1->启用',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '退货原因表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_set
-- ----------------------------
DROP TABLE IF EXISTS `order_set`;
CREATE TABLE `order_set`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `seckill_order_overtime` int(11) NULL DEFAULT NULL COMMENT '秒杀订单超时关闭时间(分)',
  `normal_order_overtime` int(11) NULL DEFAULT NULL COMMENT '正常订单超时时间(分)',
  `confirm_overtime` int(11) NULL DEFAULT NULL COMMENT '发货后自动确认收货时间（天）',
  `finish_overtime` int(11) NULL DEFAULT NULL COMMENT '自动完成交易时间，不能申请售后（天）',
  `profit_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '佣金分成比例',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单设置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_set
-- ----------------------------
INSERT INTO `order_set` VALUES (1, 1, 1, 10, 10, 0.10, '2021-06-09 11:55:54', '2021-07-01 15:14:55', 0);

-- ----------------------------
-- Table structure for payment_info
-- ----------------------------
DROP TABLE IF EXISTS `payment_info`;
CREATE TABLE `payment_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `order_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单编号',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `payment_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付类型（微信 支付宝）',
  `trade_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易编号',
  `total_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '支付金额',
  `subject` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易内容',
  `payment_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付状态',
  `callback_time` datetime(0) NULL DEFAULT NULL COMMENT '回调时间',
  `callback_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '回调信息',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 108 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '支付信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of payment_info
-- ----------------------------
INSERT INTO `payment_info` VALUES (89, '1682416124200', '180', 27, '2', NULL, 0.01, 'userID:27下订单', '1', NULL, NULL, '2023-04-25 17:49:09', '2023-04-25 17:49:09', 0);
INSERT INTO `payment_info` VALUES (90, '1682416819890', '181', 31, '2', NULL, 0.01, 'userID:31下订单', '1', NULL, NULL, '2023-04-25 18:00:36', '2023-04-25 18:00:36', 0);
INSERT INTO `payment_info` VALUES (91, '1682417748665', '182', 36, '2', NULL, 0.01, 'userID:36下订单', '1', NULL, NULL, '2023-04-25 18:16:02', '2023-04-25 18:16:01', 0);
INSERT INTO `payment_info` VALUES (92, '1682422583069', '183', 36, '2', NULL, 0.01, 'userID:36下订单', '1', NULL, NULL, '2023-04-25 19:36:46', '2023-04-25 19:36:46', 0);
INSERT INTO `payment_info` VALUES (93, '1682422852768', '184', 36, '2', NULL, 0.01, 'userID:36下订单', '1', NULL, NULL, '2023-04-25 19:41:06', '2023-04-25 19:41:05', 0);
INSERT INTO `payment_info` VALUES (94, '1682423016938', '185', 36, '2', NULL, 0.01, 'userID:36下订单', '2', '2023-04-25 19:44:22', '{transaction_id=4200001881202304257097036595, nonce_str=nD0qPvGj1lsO4b2W, trade_state=SUCCESS, bank_type=OTHERS, openid=odo3j4i6KdS4jVu5667WGokoSrAQ, sign=0230A6A6877A76A52EB5E6139602B649, return_msg=OK, fee_type=CNY, mch_id=1481962542, cash_fee=1, out_trade_no=1682423016938, cash_fee_type=CNY, appid=wxcc651fcbab275e33, total_fee=1, trade_state_desc=支付成功, trade_type=JSAPI, result_code=SUCCESS, attach=, time_end=20230425194413, is_subscribe=N, return_code=SUCCESS}', '2023-04-25 19:43:49', '2023-04-25 19:44:21', 0);
INSERT INTO `payment_info` VALUES (95, '1682423276196', '186', 36, '2', NULL, 0.01, 'userID:36下订单', '2', '2023-04-25 19:49:22', '{transaction_id=4200001851202304257186767204, nonce_str=NS8Qtep3ju1qfmEV, trade_state=SUCCESS, bank_type=OTHERS, openid=odo3j4i6KdS4jVu5667WGokoSrAQ, sign=8A0BF7772AE1055ACA5E17711420C9CA, return_msg=OK, fee_type=CNY, mch_id=1481962542, cash_fee=1, out_trade_no=1682423276196, cash_fee_type=CNY, appid=wxcc651fcbab275e33, total_fee=1, trade_state_desc=支付成功, trade_type=JSAPI, result_code=SUCCESS, attach=, time_end=20230425194914, is_subscribe=N, return_code=SUCCESS}', '2023-04-25 19:48:55', '2023-04-25 19:49:22', 0);
INSERT INTO `payment_info` VALUES (96, '1682423709092', '187', 36, '2', NULL, 0.01, 'userID:36下订单', '2', '2023-04-25 19:55:55', '{transaction_id=4200001796202304250736065730, nonce_str=qbd1Y8ofURycjH36, trade_state=SUCCESS, bank_type=OTHERS, openid=odo3j4i6KdS4jVu5667WGokoSrAQ, sign=1DE6C7F26EDD158CCA6EA68FEA53712E, return_msg=OK, fee_type=CNY, mch_id=1481962542, cash_fee=1, out_trade_no=1682423709092, cash_fee_type=CNY, appid=wxcc651fcbab275e33, total_fee=1, trade_state_desc=支付成功, trade_type=JSAPI, result_code=SUCCESS, attach=, time_end=20230425195547, is_subscribe=N, return_code=SUCCESS}', '2023-04-25 19:55:25', '2023-04-25 19:55:55', 0);
INSERT INTO `payment_info` VALUES (97, '1682423947261', '188', 36, '2', NULL, 0.01, 'userID:36下订单', '2', '2023-04-25 19:59:48', '{transaction_id=4200001796202304258886602284, nonce_str=OXZ04i6i2XWQhNgG, trade_state=SUCCESS, bank_type=OTHERS, openid=odo3j4i6KdS4jVu5667WGokoSrAQ, sign=D1EAF1D92AB5620BD600875B1712B66F, return_msg=OK, fee_type=CNY, mch_id=1481962542, cash_fee=1, out_trade_no=1682423947261, cash_fee_type=CNY, appid=wxcc651fcbab275e33, total_fee=1, trade_state_desc=支付成功, trade_type=JSAPI, result_code=SUCCESS, attach=, time_end=20230425195940, is_subscribe=N, return_code=SUCCESS}', '2023-04-25 19:59:24', '2023-04-25 19:59:47', 0);
INSERT INTO `payment_info` VALUES (98, '1682424457246', '189', 36, '2', NULL, 0.01, 'userID:36下订单', '2', '2023-04-25 20:08:18', '{transaction_id=4200001858202304257530011174, nonce_str=UecgohgDoUGXErHG, trade_state=SUCCESS, bank_type=OTHERS, openid=odo3j4i6KdS4jVu5667WGokoSrAQ, sign=AD53D9412AC0252A883E72DB15035DC3, return_msg=OK, fee_type=CNY, mch_id=1481962542, cash_fee=1, out_trade_no=1682424457246, cash_fee_type=CNY, appid=wxcc651fcbab275e33, total_fee=1, trade_state_desc=支付成功, trade_type=JSAPI, result_code=SUCCESS, attach=, time_end=20230425200811, is_subscribe=N, return_code=SUCCESS}', '2023-04-25 20:07:58', '2023-04-25 20:08:18', 0);
INSERT INTO `payment_info` VALUES (99, '1682425039734', '190', 36, '2', NULL, 0.01, 'userID:36下订单', '2', '2023-04-25 20:17:57', '{transaction_id=4200001865202304253668575975, nonce_str=ueCqWTiwSkxHCHPK, trade_state=SUCCESS, bank_type=OTHERS, openid=odo3j4i6KdS4jVu5667WGokoSrAQ, sign=5CB3BE4E69934DDC59C91A0E66BE2F53, return_msg=OK, fee_type=CNY, mch_id=1481962542, cash_fee=1, out_trade_no=1682425039734, cash_fee_type=CNY, appid=wxcc651fcbab275e33, total_fee=1, trade_state_desc=支付成功, trade_type=JSAPI, result_code=SUCCESS, attach=, time_end=20230425201749, is_subscribe=N, return_code=SUCCESS}', '2023-04-25 20:17:37', '2023-04-25 20:17:57', 0);
INSERT INTO `payment_info` VALUES (100, '1682425829654', '191', 36, '2', NULL, 0.01, 'userID:36下订单', '2', '2023-04-25 20:31:04', '{transaction_id=4200001881202304250553840897, nonce_str=PTkeJgkm7mlHFKtH, trade_state=SUCCESS, bank_type=OTHERS, openid=odo3j4i6KdS4jVu5667WGokoSrAQ, sign=DE574D06B0EEBF5971DA1D21A8789915, return_msg=OK, fee_type=CNY, mch_id=1481962542, cash_fee=1, out_trade_no=1682425829654, cash_fee_type=CNY, appid=wxcc651fcbab275e33, total_fee=1, trade_state_desc=支付成功, trade_type=JSAPI, result_code=SUCCESS, attach=, time_end=20230425203058, is_subscribe=N, return_code=SUCCESS}', '2023-04-25 20:30:44', '2023-04-25 20:31:04', 0);
INSERT INTO `payment_info` VALUES (101, '1682426019007', '192', 36, '2', NULL, 0.01, 'userID:36下订单', '1', NULL, NULL, '2023-04-25 20:33:52', '2023-04-25 20:33:51', 0);
INSERT INTO `payment_info` VALUES (102, '1682426174095', '193', 36, '2', NULL, 0.01, 'userID:36下订单', '1', NULL, NULL, '2023-04-25 20:36:27', '2023-04-25 20:36:27', 0);
INSERT INTO `payment_info` VALUES (103, '1682426422674', '194', 36, '2', NULL, 0.01, 'userID:36下订单', '2', '2023-04-25 20:40:55', '{transaction_id=4200001855202304254583711999, nonce_str=eU2h27FllgVfoR8i, trade_state=SUCCESS, bank_type=OTHERS, openid=odo3j4i6KdS4jVu5667WGokoSrAQ, sign=A68F42553279D663B56F2373F8C963E4, return_msg=OK, fee_type=CNY, mch_id=1481962542, cash_fee=1, out_trade_no=1682426422674, cash_fee_type=CNY, appid=wxcc651fcbab275e33, total_fee=1, trade_state_desc=支付成功, trade_type=JSAPI, result_code=SUCCESS, attach=, time_end=20230425204044, is_subscribe=N, return_code=SUCCESS}', '2023-04-25 20:40:31', '2023-04-25 20:40:54', 0);
INSERT INTO `payment_info` VALUES (104, '1682426743216', '195', 36, '2', NULL, 0.01, 'userID:36下订单', '2', '2023-04-25 20:46:22', '{transaction_id=4200001801202304251699476411, nonce_str=kULXCsc7NEh7mCza, trade_state=SUCCESS, bank_type=OTHERS, openid=odo3j4i6KdS4jVu5667WGokoSrAQ, sign=6A581EF57067EB838353215A009F51C0, return_msg=OK, fee_type=CNY, mch_id=1481962542, cash_fee=1, out_trade_no=1682426743216, cash_fee_type=CNY, appid=wxcc651fcbab275e33, total_fee=1, trade_state_desc=支付成功, trade_type=JSAPI, result_code=SUCCESS, attach=, time_end=20230425204618, is_subscribe=N, return_code=SUCCESS}', '2023-04-25 20:46:05', '2023-04-25 20:46:22', 0);
INSERT INTO `payment_info` VALUES (105, '1682429647121', '196', 36, '2', '4200001791202304252109512929', 0.01, 'userID:36下订单', '2', NULL, '{transaction_id=4200001791202304252109512929, nonce_str=OFJ8jGo86NFspFc8, trade_state=SUCCESS, bank_type=OTHERS, openid=odo3j4i6KdS4jVu5667WGokoSrAQ, sign=A887252E42D90C8412BB2932B7DC26CD, return_msg=OK, fee_type=CNY, mch_id=1481962542, cash_fee=1, out_trade_no=1682429647121, cash_fee_type=CNY, appid=wxcc651fcbab275e33, total_fee=1, trade_state_desc=支付成功, trade_type=JSAPI, result_code=SUCCESS, attach=, time_end=20230425213525, is_subscribe=N, return_code=SUCCESS}', '2023-04-25 21:35:03', '2023-04-25 21:35:03', 0);
INSERT INTO `payment_info` VALUES (106, '1682435683147', '197', 36, '2', '4200001804202304256082851609', 0.01, 'userID:36下订单', '2', NULL, '{transaction_id=4200001804202304256082851609, nonce_str=e8G4kTG9AAGQQvUC, trade_state=SUCCESS, bank_type=OTHERS, openid=odo3j4i6KdS4jVu5667WGokoSrAQ, sign=F31980B22664DD18E6B6B570632938D0, return_msg=OK, fee_type=CNY, mch_id=1481962542, cash_fee=1, out_trade_no=1682435683147, cash_fee_type=CNY, appid=wxcc651fcbab275e33, total_fee=1, trade_state_desc=支付成功, trade_type=JSAPI, result_code=SUCCESS, attach=, time_end=20230425231655, is_subscribe=N, return_code=SUCCESS}', '2023-04-25 23:16:13', '2023-04-25 23:16:13', 0);
INSERT INTO `payment_info` VALUES (107, '1682573952476', '198', 36, '2', NULL, 0.01, 'userID:36下订单', '1', NULL, NULL, '2023-04-27 13:39:47', '2023-04-27 13:39:47', 0);

-- ----------------------------
-- Table structure for refund_info
-- ----------------------------
DROP TABLE IF EXISTS `refund_info`;
CREATE TABLE `refund_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `out_trade_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对外业务编号',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单编号',
  `sku_id` bigint(20) NULL DEFAULT NULL,
  `payment_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付类型（微信 支付宝）',
  `trade_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易编号',
  `total_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '退款金额',
  `subject` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易内容',
  `refund_status` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退款状态',
  `callback_time` datetime(0) NULL DEFAULT NULL COMMENT '回调时间',
  `callback_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '回调信息',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_out_trade_no`(`out_trade_no`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '退款信息表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
