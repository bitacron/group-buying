/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : localhost:3306
 Source Schema         : groupbuying-product

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 19/09/2024 00:50:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for attr
-- ----------------------------
DROP TABLE IF EXISTS `attr`;
CREATE TABLE `attr`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '属性id',
  `name` char(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '属性名',
  `input_type` int(11) NULL DEFAULT NULL COMMENT '属性录入方式：0->手工录入；1->从列表中选取',
  `select_list` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '可选值列表[用逗号分隔]',
  `attr_group_id` bigint(20) NULL DEFAULT NULL COMMENT '属性分组id',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品属性' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of attr
-- ----------------------------
INSERT INTO `attr` VALUES (1, '品牌', 0, '', 1, '2021-06-06 17:43:46', '2023-04-04 09:50:55', 1);
INSERT INTO `attr` VALUES (2, '规格', 0, '', 1, '2021-06-06 17:43:58', '2023-04-04 09:50:53', 1);
INSERT INTO `attr` VALUES (3, '产地', 1, '中国,美国', 1, '2021-06-06 17:44:08', '2021-06-06 17:44:08', 0);
INSERT INTO `attr` VALUES (4, '保质期', 1, '一年,两年', 1, '2021-06-06 17:44:20', '2021-06-06 17:44:20', 0);
INSERT INTO `attr` VALUES (5, '储存条件', 1, '常温,冷场', 1, '2021-06-06 17:46:08', '2021-06-06 17:46:08', 0);
INSERT INTO `attr` VALUES (6, '储存条件', 1, '常温,冷场', 2, '2023-03-13 17:02:47', '2023-03-13 17:02:47', 0);
INSERT INTO `attr` VALUES (7, '规格', 1, '10*10,12*20,14*16', 4, '2023-03-14 09:34:22', '2023-03-14 09:34:22', 0);
INSERT INTO `attr` VALUES (8, '11', 0, '1', 6, '2023-04-04 13:41:26', '2023-04-04 13:42:44', 1);
INSERT INTO `attr` VALUES (9, '品牌名称', 0, '', 6, '2023-04-04 13:43:25', '2023-04-04 13:43:25', 0);
INSERT INTO `attr` VALUES (10, '规格', 1, '10*10,12*20', 6, '2023-04-04 13:44:04', '2023-04-04 13:44:04', 0);
INSERT INTO `attr` VALUES (11, '品牌名称', 0, '', 4, '2024-04-19 17:59:58', '2024-04-19 17:59:58', 0);

-- ----------------------------
-- Table structure for attr_group
-- ----------------------------
DROP TABLE IF EXISTS `attr_group`;
CREATE TABLE `attr_group`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分组id',
  `name` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组名',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '属性分组' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of attr_group
-- ----------------------------
INSERT INTO `attr_group` VALUES (1, '蔬菜水果', 1, '蔬菜水果', '2021-06-06 17:43:21', '2021-06-06 17:43:21', 0);
INSERT INTO `attr_group` VALUES (2, '海鲜水品', 2, '海鲜水品', '2021-11-17 03:31:25', '2021-11-17 03:31:25', 0);
INSERT INTO `attr_group` VALUES (3, '速冻食品', 3, '速冻食品', '2021-11-17 03:31:53', '2021-11-17 03:31:53', 0);
INSERT INTO `attr_group` VALUES (4, '母婴玩具', 4, '母婴玩具', '2023-04-04 13:40:55', '2024-04-19 17:57:02', 0);
INSERT INTO `attr_group` VALUES (5, '电子产品', 5, '电子产品', '2024-04-19 17:56:07', '2024-04-19 17:57:08', 0);
INSERT INTO `attr_group` VALUES (6, '测试', 11, '1', '2023-04-04 12:28:52', '2024-04-19 17:58:45', 0);

-- ----------------------------
-- Table structure for base_category_trademark
-- ----------------------------
DROP TABLE IF EXISTS `base_category_trademark`;
CREATE TABLE `base_category_trademark`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `category3_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '三级级分类id',
  `trademark_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '品牌id',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '三级分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_category_trademark
-- ----------------------------
INSERT INTO `base_category_trademark` VALUES (1, 61, 1, '2021-08-20 15:22:50', '2021-08-20 15:23:14', 1);
INSERT INTO `base_category_trademark` VALUES (2, 61, 2, '2021-08-20 15:22:50', '2021-08-25 19:11:47', 1);
INSERT INTO `base_category_trademark` VALUES (4, 61, 1, '2021-08-20 15:23:27', '2021-08-20 15:34:36', 1);
INSERT INTO `base_category_trademark` VALUES (5, 61, 1, '2021-08-23 17:47:49', '2021-08-23 17:47:49', 0);
INSERT INTO `base_category_trademark` VALUES (6, 62, 1, '2021-08-23 17:48:00', '2021-08-23 17:48:00', 0);
INSERT INTO `base_category_trademark` VALUES (7, 62, 2, '2021-08-23 17:48:00', '2021-08-25 19:11:58', 1);
INSERT INTO `base_category_trademark` VALUES (8, 61, 2, '2021-08-25 19:11:52', '2021-08-25 19:12:25', 1);
INSERT INTO `base_category_trademark` VALUES (10, 63, 2, '2021-10-09 08:44:57', '2021-10-09 08:44:57', 0);
INSERT INTO `base_category_trademark` VALUES (11, 63, 3, '2021-10-09 08:45:05', '2021-10-09 08:45:05', 0);

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类id',
  `name` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  `img_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父分类id',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '是否显示[0-不显示，1显示]',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品三级分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, '新鲜蔬菜', '\r\nhttps://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/18/xinxianshucai.jpg', NULL, NULL, 1, '2021-06-06 17:36:18', '2024-04-20 23:02:17', 0);
INSERT INTO `category` VALUES (2, '时令水果', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/18/shilingshuiguo.jpg', NULL, NULL, 2, '2021-06-06 17:36:45', '2024-04-20 23:02:21', 0);
INSERT INTO `category` VALUES (3, '鱼肉蛋奶', '\r\nhttps://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/18/yuroudannai.jpg', NULL, NULL, 3, '2021-06-06 17:37:22', '2024-04-20 23:02:24', 0);
INSERT INTO `category` VALUES (4, '海鲜水产', '\r\nhttps://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/18/haixianshuichan.jpg', NULL, NULL, 4, '2021-06-06 17:37:42', '2024-04-20 23:02:26', 0);
INSERT INTO `category` VALUES (5, '速食冻品', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/18/sudongshuiping.jpg', NULL, NULL, 5, '2021-06-06 17:38:07', '2024-04-20 23:02:30', 0);
INSERT INTO `category` VALUES (6, '乳品烘焙', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/18/rupinhongpei.jpg', NULL, NULL, 6, '2021-06-06 17:39:14', '2024-04-20 23:02:40', 0);
INSERT INTO `category` VALUES (7, '电子产品', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/18/dianzichanpin.jpg', NULL, NULL, 7, '2021-06-06 17:39:37', '2024-04-20 23:01:46', 0);
INSERT INTO `category` VALUES (8, '酒饮冲调', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/18/jiushuichongtiao.jpg', NULL, NULL, 8, '2021-06-06 17:40:05', '2024-04-20 23:03:58', 0);
INSERT INTO `category` VALUES (9, '休闲零食', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/18/xiuxianlingshi.jpg', NULL, NULL, 10, '2021-06-06 17:40:33', '2024-04-20 23:04:06', 0);
INSERT INTO `category` VALUES (10, '粮油调味', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/18/liangyoutiaowei.jpg', NULL, NULL, 10, '2021-06-06 17:40:51', '2024-04-20 23:04:13', 0);
INSERT INTO `category` VALUES (11, '日用百货', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/18/riyongbaihuo.jpg', NULL, NULL, 11, '2021-06-06 17:41:06', '2024-04-20 23:04:21', 0);
INSERT INTO `category` VALUES (12, '鲜花宠物', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/18/xianhuachongwu.jpg', NULL, NULL, 12, '2021-06-06 17:41:25', '2024-04-20 23:04:28', 0);
INSERT INTO `category` VALUES (13, '母婴玩具', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/18/muyingwanju.jpg', NULL, NULL, 13, '2021-06-06 17:41:43', '2024-04-20 23:04:41', 0);
INSERT INTO `category` VALUES (14, '数码家电', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/18/shumajiadian.jpg', NULL, NULL, 14, '2021-06-06 17:42:08', '2024-04-20 23:04:50', 0);
INSERT INTO `category` VALUES (15, 'test', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/helloWorld.jpg', NULL, NULL, 100, '2023-04-04 11:53:58', '2024-04-20 23:05:20', 0);

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT 'sku_id',
  `sku_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名字',
  `nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '会员昵称',
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `star` tinyint(1) NULL DEFAULT NULL COMMENT '星级',
  `ip` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '会员ip',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '显示状态[0-不显示，1-显示]',
  `follow_count` int(11) NULL DEFAULT NULL COMMENT '点赞数',
  `reply_count` int(11) NULL DEFAULT NULL COMMENT '回复数',
  `resources` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评论图片/视频[json数据；[{type:文件类型,url:资源路径}]]',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容',
  `type` tinyint(4) NULL DEFAULT NULL COMMENT '评论类型[0 - 对商品的直接评论，1 - 对评论的回复]',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品评价' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for comment_replay
-- ----------------------------
DROP TABLE IF EXISTS `comment_replay`;
CREATE TABLE `comment_replay`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `comment_id` bigint(20) NULL DEFAULT NULL,
  `nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `content` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 1 COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '产品评价回复表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mq_repeat_record
-- ----------------------------
DROP TABLE IF EXISTS `mq_repeat_record`;
CREATE TABLE `mq_repeat_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `business_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务编号',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'mq去重表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for region_ware
-- ----------------------------
DROP TABLE IF EXISTS `region_ware`;
CREATE TABLE `region_ware`  (
  `id` bigint(20) NOT NULL DEFAULT 0 COMMENT 'id',
  `region` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '开通区域',
  `region_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '区域名称',
  `ware_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '仓库',
  `ware_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '仓库名称',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 1 COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '城市仓库关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sku_attr_value
-- ----------------------------
DROP TABLE IF EXISTS `sku_attr_value`;
CREATE TABLE `sku_attr_value`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT '商品id',
  `attr_id` bigint(20) NULL DEFAULT NULL COMMENT '属性id',
  `attr_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '属性名',
  `attr_value` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '属性值',
  `sort` int(11) NULL DEFAULT NULL COMMENT '顺序',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 226 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'spu属性值' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sku_attr_value
-- ----------------------------
INSERT INTO `sku_attr_value` VALUES (1, 1, 1, '品牌', '自产', 1, '2021-06-06 17:53:03', '2021-08-17 11:25:47', 1);
INSERT INTO `sku_attr_value` VALUES (2, 1, 2, '规格', '300g±30g', 2, '2021-06-06 17:53:03', '2021-08-17 11:25:47', 1);
INSERT INTO `sku_attr_value` VALUES (3, 1, 3, '产地', '成都龙泉', 3, '2021-06-06 17:53:03', '2021-08-17 11:25:47', 1);
INSERT INTO `sku_attr_value` VALUES (4, 1, 4, '保质期', '3天', 4, '2021-06-06 17:53:03', '2021-08-17 11:25:47', 1);
INSERT INTO `sku_attr_value` VALUES (5, 1, 5, '储存条件', '常温', 5, '2021-06-06 17:53:03', '2021-08-17 11:25:47', 1);
INSERT INTO `sku_attr_value` VALUES (6, 2, 1, '品牌', '自产', 1, '2021-06-06 18:04:27', '2021-08-17 11:26:23', 1);
INSERT INTO `sku_attr_value` VALUES (7, 2, 2, '规格', '250', 2, '2021-06-06 18:04:27', '2021-08-17 11:26:23', 1);
INSERT INTO `sku_attr_value` VALUES (8, 2, 3, '产地', '成都郫县', 3, '2021-06-06 18:04:27', '2021-08-17 11:26:23', 1);
INSERT INTO `sku_attr_value` VALUES (9, 2, 4, '保质期', '180天', 4, '2021-06-06 18:04:27', '2021-08-17 11:26:23', 1);
INSERT INTO `sku_attr_value` VALUES (10, 2, 5, '储存条件', '常温', 5, '2021-06-06 18:04:27', '2021-08-17 11:26:23', 1);
INSERT INTO `sku_attr_value` VALUES (11, 3, 1, '品牌', '宏达', 1, '2021-06-06 18:05:57', '2021-08-17 11:26:43', 1);
INSERT INTO `sku_attr_value` VALUES (12, 3, 2, '规格', '300g±30g', 2, '2021-06-06 18:05:57', '2021-08-17 11:26:43', 1);
INSERT INTO `sku_attr_value` VALUES (13, 3, 3, '产地', '成都龙泉', 3, '2021-06-06 18:05:57', '2021-08-17 11:26:43', 1);
INSERT INTO `sku_attr_value` VALUES (14, 3, 4, '保质期', '3天', 4, '2021-06-06 18:05:57', '2021-08-17 11:26:43', 1);
INSERT INTO `sku_attr_value` VALUES (15, 3, 5, '储存条件', '冷场', 5, '2021-06-06 18:05:57', '2021-08-17 11:26:43', 1);
INSERT INTO `sku_attr_value` VALUES (16, 4, 1, '品牌', '三菱', 1, '2021-06-06 18:09:42', '2021-08-17 11:27:12', 1);
INSERT INTO `sku_attr_value` VALUES (17, 4, 2, '规格', '500g±30g', 2, '2021-06-06 18:09:42', '2021-08-17 11:27:12', 1);
INSERT INTO `sku_attr_value` VALUES (18, 4, 3, '产地', '成都达州', 3, '2021-06-06 18:09:42', '2021-08-17 11:27:12', 1);
INSERT INTO `sku_attr_value` VALUES (19, 4, 4, '保质期', '180天', 4, '2021-06-06 18:09:42', '2021-08-17 11:27:12', 1);
INSERT INTO `sku_attr_value` VALUES (20, 4, 5, '储存条件', '常温', 5, '2021-06-06 18:09:42', '2021-08-17 11:27:12', 1);
INSERT INTO `sku_attr_value` VALUES (21, 5, 1, '品牌', '自产', 1, '2021-06-06 18:10:56', '2021-08-17 11:27:29', 1);
INSERT INTO `sku_attr_value` VALUES (22, 5, 2, '规格', '500±30g', 2, '2021-06-06 18:10:56', '2021-08-17 11:27:29', 1);
INSERT INTO `sku_attr_value` VALUES (23, 5, 3, '产地', '四川巴中', 3, '2021-06-06 18:10:56', '2021-08-17 11:27:29', 1);
INSERT INTO `sku_attr_value` VALUES (24, 5, 4, '保质期', '200天', 4, '2021-06-06 18:10:56', '2021-08-17 11:27:29', 1);
INSERT INTO `sku_attr_value` VALUES (25, 5, 5, '储存条件', '常温', 5, '2021-06-06 18:10:56', '2021-08-17 11:27:29', 1);
INSERT INTO `sku_attr_value` VALUES (26, 6, 1, '品牌', '自产', 1, '2021-06-06 18:13:46', '2021-08-17 11:27:49', 1);
INSERT INTO `sku_attr_value` VALUES (27, 6, 2, '规格', '500g±30g', 2, '2021-06-06 18:13:46', '2021-08-17 11:27:49', 1);
INSERT INTO `sku_attr_value` VALUES (28, 6, 3, '产地', '四川达州', 3, '2021-06-06 18:13:46', '2021-08-17 11:27:49', 1);
INSERT INTO `sku_attr_value` VALUES (29, 6, 4, '保质期', '3', 4, '2021-06-06 18:13:46', '2021-08-17 11:27:49', 1);
INSERT INTO `sku_attr_value` VALUES (30, 6, 5, '储存条件', '冷场', 5, '2021-06-06 18:13:46', '2021-08-17 11:27:49', 1);
INSERT INTO `sku_attr_value` VALUES (31, 7, 1, '品牌', '自产', 1, '2021-06-06 18:15:18', '2021-08-17 11:28:10', 1);
INSERT INTO `sku_attr_value` VALUES (32, 7, 2, '规格', '300g±30g', 2, '2021-06-06 18:15:18', '2021-08-17 11:28:10', 1);
INSERT INTO `sku_attr_value` VALUES (33, 7, 3, '产地', '四川达州', 3, '2021-06-06 18:15:18', '2021-08-17 11:28:10', 1);
INSERT INTO `sku_attr_value` VALUES (34, 7, 4, '保质期', '4天', 4, '2021-06-06 18:15:18', '2021-08-17 11:28:10', 1);
INSERT INTO `sku_attr_value` VALUES (35, 7, 5, '储存条件', '常温', 5, '2021-06-06 18:15:18', '2021-08-17 11:28:10', 1);
INSERT INTO `sku_attr_value` VALUES (36, 8, 1, '品牌', '自产', 1, '2021-06-06 18:16:26', '2021-06-06 18:25:29', 1);
INSERT INTO `sku_attr_value` VALUES (37, 8, 2, '规格', '500g±30g', 2, '2021-06-06 18:16:26', '2021-06-06 18:25:29', 1);
INSERT INTO `sku_attr_value` VALUES (38, 8, 3, '产地', '四川内江', 3, '2021-06-06 18:16:26', '2021-06-06 18:25:29', 1);
INSERT INTO `sku_attr_value` VALUES (39, 8, 4, '保质期', '5天', 4, '2021-06-06 18:16:26', '2021-06-06 18:25:29', 1);
INSERT INTO `sku_attr_value` VALUES (40, 8, 5, '储存条件', '常温', 5, '2021-06-06 18:16:26', '2021-06-06 18:25:29', 1);
INSERT INTO `sku_attr_value` VALUES (41, 9, 1, '品牌', '自产', 1, '2021-06-06 18:17:47', '2021-08-17 11:28:42', 1);
INSERT INTO `sku_attr_value` VALUES (42, 9, 2, '规格', '500g±30g', 2, '2021-06-06 18:17:47', '2021-08-17 11:28:42', 1);
INSERT INTO `sku_attr_value` VALUES (43, 9, 3, '产地', '四川巴中', 3, '2021-06-06 18:17:47', '2021-08-17 11:28:42', 1);
INSERT INTO `sku_attr_value` VALUES (44, 9, 4, '保质期', '5天', 4, '2021-06-06 18:17:47', '2021-08-17 11:28:42', 1);
INSERT INTO `sku_attr_value` VALUES (45, 9, 5, '储存条件', '冷场', 5, '2021-06-06 18:17:47', '2021-08-17 11:28:42', 1);
INSERT INTO `sku_attr_value` VALUES (46, 8, 1, '品牌', '自产', 1, '2021-06-06 18:16:26', '2021-08-17 11:28:25', 1);
INSERT INTO `sku_attr_value` VALUES (47, 8, 2, '规格', '500g±30g', 2, '2021-06-06 18:16:26', '2021-08-17 11:28:25', 1);
INSERT INTO `sku_attr_value` VALUES (48, 8, 3, '产地', '四川内江', 3, '2021-06-06 18:16:26', '2021-08-17 11:28:25', 1);
INSERT INTO `sku_attr_value` VALUES (49, 8, 4, '保质期', '5天', 4, '2021-06-06 18:16:26', '2021-08-17 11:28:25', 1);
INSERT INTO `sku_attr_value` VALUES (50, 8, 5, '储存条件', '常温', 5, '2021-06-06 18:16:26', '2021-08-17 11:28:25', 1);
INSERT INTO `sku_attr_value` VALUES (51, 10, 1, '品牌', '农家', 1, '2021-08-14 21:02:56', '2021-09-18 09:28:28', 1);
INSERT INTO `sku_attr_value` VALUES (52, 10, 2, '规格', '南瓜+2kg', 2, '2021-08-14 21:02:56', '2021-09-18 09:28:28', 1);
INSERT INTO `sku_attr_value` VALUES (53, 10, 3, '产地', '成都', 3, '2021-08-14 21:02:56', '2021-09-18 09:28:28', 1);
INSERT INTO `sku_attr_value` VALUES (54, 10, 4, '保质期', '180天 ', 4, '2021-08-14 21:02:56', '2021-09-18 09:28:28', 1);
INSERT INTO `sku_attr_value` VALUES (55, 10, 5, '储存条件', '常温', 5, '2021-08-14 21:02:56', '2021-09-18 09:28:28', 1);
INSERT INTO `sku_attr_value` VALUES (56, 11, 1, '品牌', '农家', 1, '2021-08-14 21:26:30', '2021-08-17 11:03:54', 1);
INSERT INTO `sku_attr_value` VALUES (57, 11, 2, '规格', '300g±30g', 2, '2021-08-14 21:26:30', '2021-08-17 11:03:54', 1);
INSERT INTO `sku_attr_value` VALUES (58, 11, 3, '产地', '成都', 3, '2021-08-14 21:26:30', '2021-08-17 11:03:54', 1);
INSERT INTO `sku_attr_value` VALUES (59, 11, 4, '保质期', '7', 4, '2021-08-14 21:26:30', '2021-08-17 11:03:54', 1);
INSERT INTO `sku_attr_value` VALUES (60, 11, 5, '储存条件', '常温', 5, '2021-08-14 21:26:30', '2021-08-17 11:03:54', 1);
INSERT INTO `sku_attr_value` VALUES (61, 12, 1, '品牌', '农家', 1, '2021-08-14 21:28:16', '2021-08-17 11:03:36', 1);
INSERT INTO `sku_attr_value` VALUES (62, 12, 2, '规格', '300g±30g', 2, '2021-08-14 21:28:16', '2021-08-17 11:03:36', 1);
INSERT INTO `sku_attr_value` VALUES (63, 12, 3, '产地', '成都', 3, '2021-08-14 21:28:16', '2021-08-17 11:03:36', 1);
INSERT INTO `sku_attr_value` VALUES (64, 12, 4, '保质期', '7', 4, '2021-08-14 21:28:16', '2021-08-17 11:03:36', 1);
INSERT INTO `sku_attr_value` VALUES (65, 12, 5, '储存条件', '常温', 5, '2021-08-14 21:28:16', '2021-08-17 11:03:36', 1);
INSERT INTO `sku_attr_value` VALUES (66, 13, 1, '品牌', '农商', 1, '2021-08-14 21:51:50', '2021-08-17 10:44:20', 1);
INSERT INTO `sku_attr_value` VALUES (67, 13, 2, '规格', '500g±30g', 2, '2021-08-14 21:51:50', '2021-08-17 10:44:20', 1);
INSERT INTO `sku_attr_value` VALUES (68, 13, 3, '产地', '成都', 3, '2021-08-14 21:51:50', '2021-08-17 10:44:20', 1);
INSERT INTO `sku_attr_value` VALUES (69, 13, 4, '保质期', '7', 4, '2021-08-14 21:51:50', '2021-08-17 10:44:20', 1);
INSERT INTO `sku_attr_value` VALUES (70, 13, 5, '储存条件', '冷场', 5, '2021-08-14 21:51:50', '2021-08-17 10:44:20', 1);
INSERT INTO `sku_attr_value` VALUES (71, 13, 1, '品牌', '农商', 1, '2021-08-14 21:51:50', '2021-08-17 10:46:50', 1);
INSERT INTO `sku_attr_value` VALUES (72, 13, 2, '规格', '500g±30g', 2, '2021-08-14 21:51:50', '2021-08-17 10:46:50', 1);
INSERT INTO `sku_attr_value` VALUES (73, 13, 3, '产地', '成都', 3, '2021-08-14 21:51:50', '2021-08-17 10:46:50', 1);
INSERT INTO `sku_attr_value` VALUES (74, 13, 4, '保质期', '7', 4, '2021-08-14 21:51:50', '2021-08-17 10:46:50', 1);
INSERT INTO `sku_attr_value` VALUES (75, 13, 5, '储存条件', '冷场', 5, '2021-08-14 21:51:50', '2021-08-17 10:46:50', 1);
INSERT INTO `sku_attr_value` VALUES (76, 13, 1, '品牌', '农商', 1, '2021-08-14 21:51:50', '2021-08-17 10:54:16', 1);
INSERT INTO `sku_attr_value` VALUES (77, 13, 2, '规格', '500g±30g', 2, '2021-08-14 21:51:50', '2021-08-17 10:54:16', 1);
INSERT INTO `sku_attr_value` VALUES (78, 13, 3, '产地', '成都', 3, '2021-08-14 21:51:50', '2021-08-17 10:54:16', 1);
INSERT INTO `sku_attr_value` VALUES (79, 13, 4, '保质期', '7', 4, '2021-08-14 21:51:50', '2021-08-17 10:54:16', 1);
INSERT INTO `sku_attr_value` VALUES (80, 13, 5, '储存条件', '冷场', 5, '2021-08-14 21:51:50', '2021-08-17 10:54:16', 1);
INSERT INTO `sku_attr_value` VALUES (81, 13, 1, '品牌', '农商', 1, '2021-08-14 21:51:50', '2021-08-17 11:02:50', 1);
INSERT INTO `sku_attr_value` VALUES (82, 13, 2, '规格', '500g±30g', 2, '2021-08-14 21:51:50', '2021-08-17 11:02:50', 1);
INSERT INTO `sku_attr_value` VALUES (83, 13, 3, '产地', '成都', 3, '2021-08-14 21:51:50', '2021-08-17 11:02:50', 1);
INSERT INTO `sku_attr_value` VALUES (84, 13, 4, '保质期', '7', 4, '2021-08-14 21:51:50', '2021-08-17 11:02:50', 1);
INSERT INTO `sku_attr_value` VALUES (85, 13, 5, '储存条件', '冷场', 5, '2021-08-14 21:51:50', '2021-08-17 11:02:50', 1);
INSERT INTO `sku_attr_value` VALUES (86, 13, 1, '品牌', '农商', 1, '2021-08-17 11:02:50', '2021-08-17 11:02:50', 0);
INSERT INTO `sku_attr_value` VALUES (87, 13, 2, '规格', '500g±30g', 2, '2021-08-17 11:02:50', '2021-08-17 11:02:50', 0);
INSERT INTO `sku_attr_value` VALUES (88, 13, 3, '产地', '成都', 3, '2021-08-17 11:02:50', '2021-08-17 11:02:50', 0);
INSERT INTO `sku_attr_value` VALUES (89, 13, 4, '保质期', '7', 4, '2021-08-17 11:02:50', '2021-08-17 11:02:50', 0);
INSERT INTO `sku_attr_value` VALUES (90, 13, 5, '储存条件', '冷场', 5, '2021-08-17 11:02:50', '2021-08-17 11:02:50', 0);
INSERT INTO `sku_attr_value` VALUES (91, 12, 1, '品牌', '农家', 1, '2021-08-17 11:03:36', '2021-08-17 11:28:59', 1);
INSERT INTO `sku_attr_value` VALUES (92, 12, 2, '规格', '300g±30g', 2, '2021-08-17 11:03:36', '2021-08-17 11:28:59', 1);
INSERT INTO `sku_attr_value` VALUES (93, 12, 3, '产地', '成都', 3, '2021-08-17 11:03:36', '2021-08-17 11:28:59', 1);
INSERT INTO `sku_attr_value` VALUES (94, 12, 4, '保质期', '7', 4, '2021-08-17 11:03:36', '2021-08-17 11:28:59', 1);
INSERT INTO `sku_attr_value` VALUES (95, 12, 5, '储存条件', '常温', 5, '2021-08-17 11:03:36', '2021-08-17 11:28:59', 1);
INSERT INTO `sku_attr_value` VALUES (96, 11, 1, '品牌', '农家', 1, '2021-08-17 11:03:54', '2021-08-17 11:03:54', 0);
INSERT INTO `sku_attr_value` VALUES (97, 11, 2, '规格', '300g±30g', 2, '2021-08-17 11:03:54', '2021-08-17 11:03:54', 0);
INSERT INTO `sku_attr_value` VALUES (98, 11, 3, '产地', '成都', 3, '2021-08-17 11:03:54', '2021-08-17 11:03:54', 0);
INSERT INTO `sku_attr_value` VALUES (99, 11, 4, '保质期', '7', 4, '2021-08-17 11:03:54', '2021-08-17 11:03:54', 0);
INSERT INTO `sku_attr_value` VALUES (100, 11, 5, '储存条件', '常温', 5, '2021-08-17 11:03:54', '2021-08-17 11:03:54', 0);
INSERT INTO `sku_attr_value` VALUES (101, 1, 1, '品牌', '自产', 1, '2021-08-17 11:25:47', '2021-09-18 09:40:23', 1);
INSERT INTO `sku_attr_value` VALUES (102, 1, 2, '规格', '300g±30g', 2, '2021-08-17 11:25:47', '2021-09-18 09:40:23', 1);
INSERT INTO `sku_attr_value` VALUES (103, 1, 3, '产地', '成都龙泉', 3, '2021-08-17 11:25:47', '2021-09-18 09:40:23', 1);
INSERT INTO `sku_attr_value` VALUES (104, 1, 4, '保质期', '3天', 4, '2021-08-17 11:25:47', '2021-09-18 09:40:23', 1);
INSERT INTO `sku_attr_value` VALUES (105, 1, 5, '储存条件', '常温', 5, '2021-08-17 11:25:48', '2021-09-18 09:40:23', 1);
INSERT INTO `sku_attr_value` VALUES (106, 2, 1, '品牌', '自产', 1, '2021-08-17 11:26:23', '2021-08-17 11:26:23', 0);
INSERT INTO `sku_attr_value` VALUES (107, 2, 2, '规格', '250', 2, '2021-08-17 11:26:23', '2021-08-17 11:26:23', 0);
INSERT INTO `sku_attr_value` VALUES (108, 2, 3, '产地', '成都郫县', 3, '2021-08-17 11:26:23', '2021-08-17 11:26:23', 0);
INSERT INTO `sku_attr_value` VALUES (109, 2, 4, '保质期', '180天', 4, '2021-08-17 11:26:23', '2021-08-17 11:26:23', 0);
INSERT INTO `sku_attr_value` VALUES (110, 2, 5, '储存条件', '常温', 5, '2021-08-17 11:26:23', '2021-08-17 11:26:23', 0);
INSERT INTO `sku_attr_value` VALUES (111, 3, 1, '品牌', '宏达', 1, '2021-08-17 11:26:44', '2021-08-17 11:26:44', 0);
INSERT INTO `sku_attr_value` VALUES (112, 3, 2, '规格', '300g±30g', 2, '2021-08-17 11:26:44', '2021-08-17 11:26:44', 0);
INSERT INTO `sku_attr_value` VALUES (113, 3, 3, '产地', '成都龙泉', 3, '2021-08-17 11:26:44', '2021-08-17 11:26:44', 0);
INSERT INTO `sku_attr_value` VALUES (114, 3, 4, '保质期', '3天', 4, '2021-08-17 11:26:44', '2021-08-17 11:26:44', 0);
INSERT INTO `sku_attr_value` VALUES (115, 3, 5, '储存条件', '冷场', 5, '2021-08-17 11:26:44', '2021-08-17 11:26:44', 0);
INSERT INTO `sku_attr_value` VALUES (116, 4, 1, '品牌', '三菱', 1, '2021-08-17 11:27:12', '2021-09-18 09:35:51', 1);
INSERT INTO `sku_attr_value` VALUES (117, 4, 2, '规格', '500g±30g', 2, '2021-08-17 11:27:12', '2021-09-18 09:35:51', 1);
INSERT INTO `sku_attr_value` VALUES (118, 4, 3, '产地', '成都达州', 3, '2021-08-17 11:27:12', '2021-09-18 09:35:51', 1);
INSERT INTO `sku_attr_value` VALUES (119, 4, 4, '保质期', '180天', 4, '2021-08-17 11:27:12', '2021-09-18 09:35:51', 1);
INSERT INTO `sku_attr_value` VALUES (120, 4, 5, '储存条件', '常温', 5, '2021-08-17 11:27:12', '2021-09-18 09:35:51', 1);
INSERT INTO `sku_attr_value` VALUES (121, 5, 1, '品牌', '自产', 1, '2021-08-17 11:27:29', '2021-09-28 06:08:58', 1);
INSERT INTO `sku_attr_value` VALUES (122, 5, 2, '规格', '500±30g', 2, '2021-08-17 11:27:29', '2021-09-28 06:08:58', 1);
INSERT INTO `sku_attr_value` VALUES (123, 5, 3, '产地', '四川巴中', 3, '2021-08-17 11:27:29', '2021-09-28 06:08:58', 1);
INSERT INTO `sku_attr_value` VALUES (124, 5, 4, '保质期', '200天', 4, '2021-08-17 11:27:29', '2021-09-28 06:08:58', 1);
INSERT INTO `sku_attr_value` VALUES (125, 5, 5, '储存条件', '常温', 5, '2021-08-17 11:27:29', '2021-09-28 06:08:58', 1);
INSERT INTO `sku_attr_value` VALUES (126, 6, 1, '品牌', '自产', 1, '2021-08-17 11:27:49', '2021-08-17 11:27:49', 0);
INSERT INTO `sku_attr_value` VALUES (127, 6, 2, '规格', '500g±30g', 2, '2021-08-17 11:27:49', '2021-08-17 11:27:49', 0);
INSERT INTO `sku_attr_value` VALUES (128, 6, 3, '产地', '四川达州', 3, '2021-08-17 11:27:49', '2021-08-17 11:27:49', 0);
INSERT INTO `sku_attr_value` VALUES (129, 6, 4, '保质期', '3', 4, '2021-08-17 11:27:49', '2021-08-17 11:27:49', 0);
INSERT INTO `sku_attr_value` VALUES (130, 6, 5, '储存条件', '冷场', 5, '2021-08-17 11:27:49', '2021-08-17 11:27:49', 0);
INSERT INTO `sku_attr_value` VALUES (131, 7, 1, '品牌', '自产', 1, '2021-08-17 11:28:10', '2021-09-27 10:38:32', 1);
INSERT INTO `sku_attr_value` VALUES (132, 7, 2, '规格', '300g±30g', 2, '2021-08-17 11:28:10', '2021-09-27 10:38:32', 1);
INSERT INTO `sku_attr_value` VALUES (133, 7, 3, '产地', '四川达州', 3, '2021-08-17 11:28:10', '2021-09-27 10:38:32', 1);
INSERT INTO `sku_attr_value` VALUES (134, 7, 4, '保质期', '4天', 4, '2021-08-17 11:28:10', '2021-09-27 10:38:32', 1);
INSERT INTO `sku_attr_value` VALUES (135, 7, 5, '储存条件', '常温', 5, '2021-08-17 11:28:10', '2021-09-27 10:38:32', 1);
INSERT INTO `sku_attr_value` VALUES (136, 8, 1, '品牌', '自产', 1, '2021-08-17 11:28:25', '2021-08-17 11:28:25', 0);
INSERT INTO `sku_attr_value` VALUES (137, 8, 2, '规格', '500g±30g', 2, '2021-08-17 11:28:25', '2021-08-17 11:28:25', 0);
INSERT INTO `sku_attr_value` VALUES (138, 8, 3, '产地', '四川内江', 3, '2021-08-17 11:28:25', '2021-08-17 11:28:25', 0);
INSERT INTO `sku_attr_value` VALUES (139, 8, 4, '保质期', '5天', 4, '2021-08-17 11:28:26', '2021-08-17 11:28:26', 0);
INSERT INTO `sku_attr_value` VALUES (140, 8, 5, '储存条件', '常温', 5, '2021-08-17 11:28:26', '2021-08-17 11:28:26', 0);
INSERT INTO `sku_attr_value` VALUES (141, 9, 1, '品牌', '自产', 1, '2021-08-17 11:28:42', '2021-08-17 11:28:42', 0);
INSERT INTO `sku_attr_value` VALUES (142, 9, 2, '规格', '500g±30g', 2, '2021-08-17 11:28:42', '2021-08-17 11:28:42', 0);
INSERT INTO `sku_attr_value` VALUES (143, 9, 3, '产地', '四川巴中', 3, '2021-08-17 11:28:42', '2021-08-17 11:28:42', 0);
INSERT INTO `sku_attr_value` VALUES (144, 9, 4, '保质期', '5天', 4, '2021-08-17 11:28:42', '2021-08-17 11:28:42', 0);
INSERT INTO `sku_attr_value` VALUES (145, 9, 5, '储存条件', '冷场', 5, '2021-08-17 11:28:42', '2021-08-17 11:28:42', 0);
INSERT INTO `sku_attr_value` VALUES (146, 12, 1, '品牌', '农家', 1, '2021-08-17 11:28:59', '2021-08-17 11:28:59', 0);
INSERT INTO `sku_attr_value` VALUES (147, 12, 2, '规格', '300g±30g', 2, '2021-08-17 11:28:59', '2021-08-17 11:28:59', 0);
INSERT INTO `sku_attr_value` VALUES (148, 12, 3, '产地', '成都', 3, '2021-08-17 11:28:59', '2021-08-17 11:28:59', 0);
INSERT INTO `sku_attr_value` VALUES (149, 12, 4, '保质期', '7', 4, '2021-08-17 11:28:59', '2021-08-17 11:28:59', 0);
INSERT INTO `sku_attr_value` VALUES (150, 12, 5, '储存条件', '常温', 5, '2021-08-17 11:28:59', '2021-08-17 11:28:59', 0);
INSERT INTO `sku_attr_value` VALUES (151, 10, 1, '品牌', '农家', 1, '2021-09-18 09:28:28', '2021-09-18 09:28:28', 0);
INSERT INTO `sku_attr_value` VALUES (152, 10, 2, '规格', '南瓜+2kg', 2, '2021-09-18 09:28:28', '2021-09-18 09:28:28', 0);
INSERT INTO `sku_attr_value` VALUES (153, 10, 3, '产地', '成都', 3, '2021-09-18 09:28:28', '2021-09-18 09:28:28', 0);
INSERT INTO `sku_attr_value` VALUES (154, 10, 4, '保质期', '180天 ', 4, '2021-09-18 09:28:28', '2021-09-18 09:28:28', 0);
INSERT INTO `sku_attr_value` VALUES (155, 10, 5, '储存条件', '常温', 5, '2021-09-18 09:28:28', '2021-09-18 09:28:28', 0);
INSERT INTO `sku_attr_value` VALUES (156, 4, 1, '品牌', '三菱', 1, '2021-09-18 09:35:51', '2021-09-18 09:35:51', 0);
INSERT INTO `sku_attr_value` VALUES (157, 4, 2, '规格', '500g±30g', 2, '2021-09-18 09:35:51', '2021-09-18 09:35:51', 0);
INSERT INTO `sku_attr_value` VALUES (158, 4, 3, '产地', '成都达州', 3, '2021-09-18 09:35:51', '2021-09-18 09:35:51', 0);
INSERT INTO `sku_attr_value` VALUES (159, 4, 4, '保质期', '180天', 4, '2021-09-18 09:35:51', '2021-09-18 09:35:51', 0);
INSERT INTO `sku_attr_value` VALUES (160, 4, 5, '储存条件', '常温', 5, '2021-09-18 09:35:51', '2021-09-18 09:35:51', 0);
INSERT INTO `sku_attr_value` VALUES (161, 1, 1, '品牌', '自产', 1, '2021-09-18 09:40:23', '2021-09-18 09:41:28', 1);
INSERT INTO `sku_attr_value` VALUES (162, 1, 2, '规格', '300g±30g', 2, '2021-09-18 09:40:23', '2021-09-18 09:41:28', 1);
INSERT INTO `sku_attr_value` VALUES (163, 1, 3, '产地', '成都龙泉', 3, '2021-09-18 09:40:23', '2021-09-18 09:41:28', 1);
INSERT INTO `sku_attr_value` VALUES (164, 1, 4, '保质期', '3天', 4, '2021-09-18 09:40:23', '2021-09-18 09:41:28', 1);
INSERT INTO `sku_attr_value` VALUES (165, 1, 5, '储存条件', '常温', 5, '2021-09-18 09:40:23', '2021-09-18 09:41:28', 1);
INSERT INTO `sku_attr_value` VALUES (166, 1, 1, '品牌', '自产', 1, '2021-09-18 09:41:29', '2021-09-18 09:55:42', 1);
INSERT INTO `sku_attr_value` VALUES (167, 1, 2, '规格', '300g±30g', 2, '2021-09-18 09:41:29', '2021-09-18 09:55:42', 1);
INSERT INTO `sku_attr_value` VALUES (168, 1, 3, '产地', '成都龙泉', 3, '2021-09-18 09:41:29', '2021-09-18 09:55:42', 1);
INSERT INTO `sku_attr_value` VALUES (169, 1, 4, '保质期', '3天', 4, '2021-09-18 09:41:29', '2021-09-18 09:55:42', 1);
INSERT INTO `sku_attr_value` VALUES (170, 1, 5, '储存条件', '常温', 5, '2021-09-18 09:41:29', '2021-09-18 09:55:42', 1);
INSERT INTO `sku_attr_value` VALUES (171, 1, 1, '品牌', '自产', 1, '2021-09-18 09:55:42', '2021-09-18 09:56:00', 1);
INSERT INTO `sku_attr_value` VALUES (172, 1, 2, '规格', '300g±30g', 2, '2021-09-18 09:55:42', '2021-09-18 09:56:00', 1);
INSERT INTO `sku_attr_value` VALUES (173, 1, 3, '产地', '成都龙泉', 3, '2021-09-18 09:55:42', '2021-09-18 09:56:00', 1);
INSERT INTO `sku_attr_value` VALUES (174, 1, 4, '保质期', '3天', 4, '2021-09-18 09:55:42', '2021-09-18 09:56:00', 1);
INSERT INTO `sku_attr_value` VALUES (175, 1, 5, '储存条件', '常温', 5, '2021-09-18 09:55:42', '2021-09-18 09:56:00', 1);
INSERT INTO `sku_attr_value` VALUES (176, 1, 1, '品牌', '自产', 1, '2021-09-18 09:56:00', '2021-09-18 09:56:11', 1);
INSERT INTO `sku_attr_value` VALUES (177, 1, 2, '规格', '300g±30g', 2, '2021-09-18 09:56:01', '2021-09-18 09:56:11', 1);
INSERT INTO `sku_attr_value` VALUES (178, 1, 3, '产地', '成都龙泉', 3, '2021-09-18 09:56:01', '2021-09-18 09:56:11', 1);
INSERT INTO `sku_attr_value` VALUES (179, 1, 4, '保质期', '3天', 4, '2021-09-18 09:56:01', '2021-09-18 09:56:11', 1);
INSERT INTO `sku_attr_value` VALUES (180, 1, 5, '储存条件', '常温', 5, '2021-09-18 09:56:01', '2021-09-18 09:56:11', 1);
INSERT INTO `sku_attr_value` VALUES (181, 1, 1, '品牌', '自产', 1, '2021-09-18 09:56:11', '2021-09-18 09:56:11', 0);
INSERT INTO `sku_attr_value` VALUES (182, 1, 2, '规格', '300g±30g', 2, '2021-09-18 09:56:11', '2021-09-18 09:56:11', 0);
INSERT INTO `sku_attr_value` VALUES (183, 1, 3, '产地', '成都龙泉', 3, '2021-09-18 09:56:11', '2021-09-18 09:56:11', 0);
INSERT INTO `sku_attr_value` VALUES (184, 1, 4, '保质期', '3天', 4, '2021-09-18 09:56:11', '2021-09-18 09:56:11', 0);
INSERT INTO `sku_attr_value` VALUES (185, 1, 5, '储存条件', '常温', 5, '2021-09-18 09:56:11', '2021-09-18 09:56:11', 0);
INSERT INTO `sku_attr_value` VALUES (186, 7, 1, '品牌', '自产', 1, '2021-09-27 10:38:32', '2021-09-27 10:42:35', 1);
INSERT INTO `sku_attr_value` VALUES (187, 7, 2, '规格', '300g±30g', 2, '2021-09-27 10:38:32', '2021-09-27 10:42:35', 1);
INSERT INTO `sku_attr_value` VALUES (188, 7, 3, '产地', '四川达州', 3, '2021-09-27 10:38:32', '2021-09-27 10:42:35', 1);
INSERT INTO `sku_attr_value` VALUES (189, 7, 4, '保质期', '4天', 4, '2021-09-27 10:38:32', '2021-09-27 10:42:35', 1);
INSERT INTO `sku_attr_value` VALUES (190, 7, 5, '储存条件', '常温', 5, '2021-09-27 10:38:32', '2021-09-27 10:42:35', 1);
INSERT INTO `sku_attr_value` VALUES (191, 7, 1, '品牌', '自产', 1, '2021-09-27 10:42:35', '2021-09-27 10:42:35', 0);
INSERT INTO `sku_attr_value` VALUES (192, 7, 2, '规格', '300g±30g', 2, '2021-09-27 10:42:35', '2021-09-27 10:42:35', 0);
INSERT INTO `sku_attr_value` VALUES (193, 7, 3, '产地', '四川达州', 3, '2021-09-27 10:42:35', '2021-09-27 10:42:35', 0);
INSERT INTO `sku_attr_value` VALUES (194, 7, 4, '保质期', '4天', 4, '2021-09-27 10:42:35', '2021-09-27 10:42:35', 0);
INSERT INTO `sku_attr_value` VALUES (195, 7, 5, '储存条件', '常温', 5, '2021-09-27 10:42:35', '2021-09-27 10:42:35', 0);
INSERT INTO `sku_attr_value` VALUES (196, 5, 1, '品牌', '自产', 1, '2021-09-28 06:08:58', '2021-09-28 06:08:58', 0);
INSERT INTO `sku_attr_value` VALUES (197, 5, 2, '规格', '500±30g', 2, '2021-09-28 06:08:58', '2021-09-28 06:08:58', 0);
INSERT INTO `sku_attr_value` VALUES (198, 5, 3, '产地', '四川巴中', 3, '2021-09-28 06:08:58', '2021-09-28 06:08:58', 0);
INSERT INTO `sku_attr_value` VALUES (199, 5, 4, '保质期', '200天', 4, '2021-09-28 06:08:58', '2021-09-28 06:08:58', 0);
INSERT INTO `sku_attr_value` VALUES (200, 5, 5, '储存条件', '常温', 5, '2021-09-28 06:08:58', '2021-09-28 06:08:58', 0);
INSERT INTO `sku_attr_value` VALUES (201, 15, 1, '品牌', '寿光苹果', 1, '2023-03-13 17:00:01', '2023-03-13 17:00:01', 0);
INSERT INTO `sku_attr_value` VALUES (202, 15, 2, '规格', '10kg', 2, '2023-03-13 17:00:01', '2023-03-13 17:00:01', 0);
INSERT INTO `sku_attr_value` VALUES (203, 15, 3, '产地', '山东', 3, '2023-03-13 17:00:01', '2023-03-13 17:00:01', 0);
INSERT INTO `sku_attr_value` VALUES (204, 15, 4, '保质期', '180天', 4, '2023-03-13 17:00:01', '2023-03-13 17:00:01', 0);
INSERT INTO `sku_attr_value` VALUES (205, 15, 5, '储存条件', '常温', 5, '2023-03-13 17:00:01', '2023-03-13 17:00:01', 0);
INSERT INTO `sku_attr_value` VALUES (206, 16, 6, '储存条件', '冷场', 1, '2023-03-14 11:39:22', '2023-03-14 11:39:22', 0);
INSERT INTO `sku_attr_value` VALUES (207, 17, 9, '品牌名称', '未知', NULL, '2023-04-04 21:33:20', '2023-04-04 22:39:56', 1);
INSERT INTO `sku_attr_value` VALUES (208, 17, 10, '规格', '10*10', NULL, '2023-04-04 21:33:20', '2023-04-04 22:39:56', 1);
INSERT INTO `sku_attr_value` VALUES (209, 18, 9, '品牌名称', '托马斯', NULL, '2023-04-04 21:39:46', '2023-04-04 22:41:07', 1);
INSERT INTO `sku_attr_value` VALUES (210, 18, 10, '规格', '12*20', NULL, '2023-04-04 21:39:46', '2023-04-04 22:41:07', 1);
INSERT INTO `sku_attr_value` VALUES (211, 17, 9, '品牌名称', '未知', NULL, '2023-04-04 22:39:56', '2023-04-04 22:39:56', 0);
INSERT INTO `sku_attr_value` VALUES (212, 17, 10, '规格', '10*10', NULL, '2023-04-04 22:39:56', '2023-04-04 22:39:56', 0);
INSERT INTO `sku_attr_value` VALUES (213, 18, 9, '品牌名称', '托马斯', NULL, '2023-04-04 22:41:07', '2023-04-04 22:41:07', 0);
INSERT INTO `sku_attr_value` VALUES (214, 18, 10, '规格', '12*20', NULL, '2023-04-04 22:41:07', '2023-04-04 22:41:07', 0);
INSERT INTO `sku_attr_value` VALUES (215, 19, 9, '品牌名称', '托马斯', NULL, '2023-04-06 21:33:10', '2023-04-06 21:33:10', 0);
INSERT INTO `sku_attr_value` VALUES (216, 19, 10, '规格', '10*10', NULL, '2023-04-06 21:33:10', '2023-04-06 21:33:10', 0);
INSERT INTO `sku_attr_value` VALUES (217, 20, 9, '品牌名称', '东方红', NULL, '2023-04-06 21:41:27', '2023-04-06 21:41:27', 0);
INSERT INTO `sku_attr_value` VALUES (218, 20, 10, '规格', '12*20', NULL, '2023-04-06 21:41:27', '2023-04-06 21:41:27', 0);
INSERT INTO `sku_attr_value` VALUES (219, 14, 3, '产地', '中国', 1, '2024-04-19 21:04:38', '2024-04-20 18:02:37', 1);
INSERT INTO `sku_attr_value` VALUES (220, 14, 4, '保质期', '一年', 2, '2024-04-19 21:04:38', '2024-04-20 18:02:37', 1);
INSERT INTO `sku_attr_value` VALUES (221, 14, 5, '储存条件', '常温', 3, '2024-04-19 21:04:38', '2024-04-20 18:02:37', 1);
INSERT INTO `sku_attr_value` VALUES (222, 14, 6, '储存条件', '常温', 1, '2024-04-20 18:02:37', '2024-04-20 18:05:29', 1);
INSERT INTO `sku_attr_value` VALUES (223, 14, 3, '产地', '中国', 1, '2024-04-20 18:05:29', '2024-04-20 18:05:29', 0);
INSERT INTO `sku_attr_value` VALUES (224, 14, 4, '保质期', '一年', 2, '2024-04-20 18:05:29', '2024-04-20 18:05:29', 0);
INSERT INTO `sku_attr_value` VALUES (225, 14, 5, '储存条件', '常温', 3, '2024-04-20 18:05:29', '2024-04-20 18:05:29', 0);

-- ----------------------------
-- Table structure for sku_detail
-- ----------------------------
DROP TABLE IF EXISTS `sku_detail`;
CREATE TABLE `sku_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT '商品id',
  `detail_html` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '详情内容',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'spu属性值' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sku_detail
-- ----------------------------
INSERT INTO `sku_detail` VALUES (1, 1, '西红柿', '2021-06-06 17:53:03', '2021-06-06 18:00:18', 0);
INSERT INTO `sku_detail` VALUES (2, 2, '红薯', '2021-06-06 18:04:27', '2021-06-06 18:04:27', 0);
INSERT INTO `sku_detail` VALUES (3, 3, '红薯', '2021-06-06 18:05:57', '2021-06-06 18:05:57', 0);
INSERT INTO `sku_detail` VALUES (4, 4, '大蒜', '2021-06-06 18:09:42', '2021-06-06 18:09:42', 0);
INSERT INTO `sku_detail` VALUES (5, 5, '土豆', '2021-06-06 18:10:56', '2021-06-06 18:10:56', 0);
INSERT INTO `sku_detail` VALUES (6, 6, '丝瓜', '2021-06-06 18:13:46', '2021-06-06 18:13:46', 0);
INSERT INTO `sku_detail` VALUES (7, 7, '辣椒', '2021-06-06 18:15:18', '2021-06-06 18:15:18', 0);
INSERT INTO `sku_detail` VALUES (8, 8, '茄子', '2021-06-06 18:16:26', '2021-06-06 18:16:26', 0);
INSERT INTO `sku_detail` VALUES (9, 9, '玉米', '2021-06-06 18:17:47', '2021-06-06 18:17:47', 0);
INSERT INTO `sku_detail` VALUES (10, 10, '南瓜', '2021-08-14 21:02:56', '2021-08-14 21:02:56', 0);
INSERT INTO `sku_detail` VALUES (11, 11, '苹果300g±30g', '2021-08-14 21:26:30', '2021-08-14 21:26:30', 0);
INSERT INTO `sku_detail` VALUES (12, 12, '橘子300g±30g', '2021-08-14 21:28:16', '2021-08-14 21:28:16', 0);
INSERT INTO `sku_detail` VALUES (13, 13, NULL, '2021-08-14 21:51:50', '2021-08-27 21:17:30', 0);

-- ----------------------------
-- Table structure for sku_image
-- ----------------------------
DROP TABLE IF EXISTS `sku_image`;
CREATE TABLE `sku_image`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT 'sku_id',
  `img_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片名称',
  `img_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 106 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品图片' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sku_image
-- ----------------------------
INSERT INTO `sku_image` VALUES (53, 11, 'pingguo.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/pingguo.jpg', 1, '2021-08-17 11:03:54', '2024-04-20 21:06:28', 0);
INSERT INTO `sku_image` VALUES (56, 2, 'hongshu.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/hongshu.jpg', 1, '2021-08-17 11:26:23', '2024-04-20 21:04:00', 0);
INSERT INTO `sku_image` VALUES (57, 2, 'hongshu.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/hongshu.jpg', 2, '2021-08-17 11:26:23', '2024-04-20 21:04:00', 0);
INSERT INTO `sku_image` VALUES (58, 3, 'sijidou.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/sijidou.jpg', 1, '2021-08-17 11:26:43', '2024-04-20 21:04:00', 0);
INSERT INTO `sku_image` VALUES (59, 3, 'sijidou.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/sijidou.jpg', 2, '2021-08-17 11:26:43', '2024-04-20 21:04:00', 0);
INSERT INTO `sku_image` VALUES (60, 3, 'sijidou.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/sijidou.jpg', 3, '2021-08-17 11:26:43', '2024-04-20 21:04:00', 0);
INSERT INTO `sku_image` VALUES (66, 6, 'sigua.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/sigua.jpg', 1, '2021-08-17 11:27:49', '2024-04-20 21:04:00', 0);
INSERT INTO `sku_image` VALUES (67, 6, 'sigua.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/sigua.jpg', 2, '2021-08-17 11:27:49', '2024-04-20 21:04:00', 0);
INSERT INTO `sku_image` VALUES (71, 8, 'qiezi.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/qiezi.jpg', 1, '2021-08-17 11:28:25', '2024-04-20 21:04:00', 0);
INSERT INTO `sku_image` VALUES (72, 8, 'qiezi.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/qiezi.jpg', 2, '2021-08-17 11:28:25', '2024-04-20 21:04:00', 0);
INSERT INTO `sku_image` VALUES (73, 9, 'yumi.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/yumi.jpg', 1, '2021-08-17 11:28:42', '2024-04-20 21:04:00', 0);
INSERT INTO `sku_image` VALUES (74, 9, 'yumi.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/yumi.jpg', 2, '2021-08-17 11:28:42', '2024-04-20 21:04:00', 0);
INSERT INTO `sku_image` VALUES (75, 9, 'yumi.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/yumi.jpg', 3, '2021-08-17 11:28:42', '2024-04-20 21:04:00', 0);
INSERT INTO `sku_image` VALUES (76, 12, 'juzi.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/juzi.jpg', 1, '2021-08-17 11:28:59', '2024-04-20 21:04:00', 0);
INSERT INTO `sku_image` VALUES (77, 10, 'nangua.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/nangua.jpg', 1, '2021-09-18 09:28:28', '2024-04-20 21:04:00', 0);
INSERT INTO `sku_image` VALUES (78, 10, 'nangua.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/nangua.jpg', 2, '2021-09-18 09:28:28', '2024-04-20 21:04:00', 0);
INSERT INTO `sku_image` VALUES (79, 4, 'dasuan.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/dasuan.jpg', 1, '2021-08-17 11:27:12', '2024-04-20 21:04:00', 0);
INSERT INTO `sku_image` VALUES (80, 4, 'dasuan.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/dasuan.jpg', 2, '2021-08-17 11:27:12', '2024-04-20 21:04:00', 0);
INSERT INTO `sku_image` VALUES (81, 4, 'dasuan.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/dasuan.jpg', 3, '2021-08-17 11:27:12', '2024-04-20 21:04:00', 0);
INSERT INTO `sku_image` VALUES (92, 1, 'xihongshi.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/xihongshi.jpg', 1, '2021-08-17 11:25:47', '2024-04-20 21:04:00', 0);
INSERT INTO `sku_image` VALUES (93, 1, 'xihongshi.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/xihongshi.jpg', 2, '2021-08-17 11:25:47', '2024-04-20 21:04:00', 0);
INSERT INTO `sku_image` VALUES (100, 5, 'tudou.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/tudou.jpg', 1, '2021-08-17 11:27:29', '2024-04-20 21:04:00', 0);
INSERT INTO `sku_image` VALUES (101, 5, 'tudou.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/tudou.jpg', 2, '2021-08-17 11:27:29', '2024-04-20 21:04:00', 0);
INSERT INTO `sku_image` VALUES (102, 14, 'banana.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/19/644264e7f5504ebd9da399c6d2b39890banana.jpg', 1, '2024-04-19 21:04:38', '2024-04-20 18:02:37', 1);
INSERT INTO `sku_image` VALUES (103, 14, 'banana.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/19/644264e7f5504ebd9da399c6d2b39890banana.jpg', 1, '2024-04-19 21:04:38', '2024-04-20 18:05:29', 1);
INSERT INTO `sku_image` VALUES (104, 14, 'banana.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/19/644264e7f5504ebd9da399c6d2b39890banana.jpg', 1, '2024-04-19 21:04:38', '2024-04-19 21:04:38', 0);
INSERT INTO `sku_image` VALUES (105, 15, 'jitui.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/23/b803dd6dd26643a486f6e76e3c8203aejitui.jpg', 1, '2024-04-23 21:09:50', '2024-04-23 21:09:50', 0);

-- ----------------------------
-- Table structure for sku_info
-- ----------------------------
DROP TABLE IF EXISTS `sku_info`;
CREATE TABLE `sku_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '分类id',
  `attr_group_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '平台属性分组id',
  `sku_type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '商品类型：0->普通商品 1->秒杀商品',
  `sku_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'sku名称',
  `img_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '展示图片',
  `per_limit` int(11) NOT NULL DEFAULT 1 COMMENT '限购个数/每天（0：不限购）',
  `publish_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '上架状态：0->下架；1->上架',
  `check_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '审核状态：0->未审核；1->审核通过',
  `is_new_person` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否新人专享：0->否；1->是',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `sku_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'sku编码',
  `price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '价格',
  `market_price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '市场价',
  `stock` int(11) NOT NULL DEFAULT 0 COMMENT '库存',
  `lock_stock` int(11) NOT NULL DEFAULT 0 COMMENT '锁定库存',
  `low_stock` int(11) NOT NULL DEFAULT 0 COMMENT '预警库存',
  `sale` int(11) NOT NULL DEFAULT 0 COMMENT '销量',
  `ware_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '仓库',
  `version` bigint(20) NOT NULL DEFAULT 0,
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'sku信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sku_info
-- ----------------------------
INSERT INTO `sku_info` VALUES (1, 1, 1, 0, '西红柿', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/xihongshi.jpg', 5, 1, 1, 0, 1, '0001', 2.20, 2.90, 99, 20, 10, 1, 1, 2, '2021-06-06 17:53:03', '2024-04-20 21:04:01', 0);
INSERT INTO `sku_info` VALUES (2, 1, 1, 0, '红薯', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/hongshu.jpg', 5, 1, 1, 0, 2, '0002', 1.79, 2.50, 100, 25, 10, 0, 1, 2, '2021-06-06 18:04:27', '2024-04-20 21:03:27', 0);
INSERT INTO `sku_info` VALUES (3, 1, 1, 0, '四季豆', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/sijidou.jpg', 5, 1, 1, 0, 3, '0003', 3.50, 4.10, 100, 97, 10, 0, 1, 1, '2021-06-06 18:05:57', '2024-04-20 21:04:01', 0);
INSERT INTO `sku_info` VALUES (4, 1, 1, 0, '大蒜', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/dasuan.jpg', 5, 1, 1, 0, 4, '0004', 5.50, 7.80, 100, 47, 10, 0, 1, 2, '2021-06-06 18:09:42', '2024-04-20 21:04:01', 0);
INSERT INTO `sku_info` VALUES (5, 1, 1, 0, '土豆', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/tudou.jpg', 5, 1, 1, 1, 5, '0005', 5.30, 5.90, 100, 62, 10, 0, 1, 1, '2021-06-06 18:10:56', '2024-04-20 21:04:01', 0);
INSERT INTO `sku_info` VALUES (6, 1, 1, 0, '丝瓜', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/sigua.jpg', 5, 1, 1, 1, 6, '0006', 3.60, 4.50, 100, 26, 10, 0, 1, 1, '2021-06-06 18:13:46', '2024-04-20 21:04:01', 0);
INSERT INTO `sku_info` VALUES (8, 1, 1, 1, '茄子', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/qiezi.jpg', 5, 0, 1, 0, 8, '0008', 3.50, 4.40, 100, 0, 10, 0, 1, 1, '2021-06-06 18:16:26', '2024-04-20 21:04:01', 0);
INSERT INTO `sku_info` VALUES (9, 1, 1, 1, '玉米', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/yumi.jpg', 5, 0, 1, 0, 9, '0009', 1.90, 3.00, 100, 0, 10, 0, 1, 1, '2021-06-06 18:17:47', '2024-04-20 21:04:01', 0);
INSERT INTO `sku_info` VALUES (10, 1, 1, 0, '南瓜', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/nangua.jpg', 5, 1, 1, 1, 10, '555667', 5.00, 4.00, 100, 43, 10, 0, 1, 0, '2021-08-14 21:02:56', '2024-04-20 21:04:01', 0);
INSERT INTO `sku_info` VALUES (11, 2, 1, 0, '苹果', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/pingguo.jpg', 5, 1, 0, 0, 11, '0011', 5.00, 7.00, 100, 3, 10, 0, 1, 0, '2021-08-14 21:26:30', '2024-04-20 21:06:40', 0);
INSERT INTO `sku_info` VALUES (12, 2, 1, 0, '橘子', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/juzi.jpg', 5, 1, 1, 0, 12, '0012', 7.00, 8.00, 100, 0, 10, 0, 1, 0, '2021-08-14 21:28:16', '2024-04-20 21:04:01', 0);
INSERT INTO `sku_info` VALUES (13, 1, 1, 0, '蔬菜拼盘', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/shucaipinpan.jpg', 5, 1, 1, 0, 13, '0013', 50.00, 70.00, 100, 59, 10, 0, 1, 0, '2021-08-14 21:51:50', '2024-04-20 21:08:09', 0);
INSERT INTO `sku_info` VALUES (14, 2, 1, 0, '香蕉', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/19/644264e7f5504ebd9da399c6d2b39890banana.jpg', 12, 0, 0, 0, 15, '2009', 3.00, 4.00, 100, 0, 5, 0, 0, 0, '2024-04-19 21:04:38', '2024-04-20 18:04:28', 0);
INSERT INTO `sku_info` VALUES (15, 3, 3, 0, '鸡腿', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/23/b803dd6dd26643a486f6e76e3c8203aejitui.jpg', 5, 1, 1, 0, 15, '2221', 10.00, 12.00, 50, 0, 10, 0, 0, 0, '2024-04-23 21:09:50', '2024-04-24 13:13:19', 0);

-- ----------------------------
-- Table structure for sku_poster
-- ----------------------------
DROP TABLE IF EXISTS `sku_poster`;
CREATE TABLE `sku_poster`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT '商品id',
  `img_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名称',
  `img_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件路径',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 152 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品海报表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sku_poster
-- ----------------------------
INSERT INTO `sku_poster` VALUES (36, 11, 'pngguo.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/pingguo.jpg', '2021-09-18 09:40:01', '2024-04-20 21:06:56', 0);
INSERT INTO `sku_poster` VALUES (39, 2, 'hongshu.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/hongshu.jpg', '2021-09-18 09:40:01', '2024-04-20 21:03:58', 0);
INSERT INTO `sku_poster` VALUES (40, 2, 'hongshu.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/hongshu.jpg', '2021-09-18 09:40:01', '2024-04-20 21:03:58', 0);
INSERT INTO `sku_poster` VALUES (41, 3, 'sijidou.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/sijidou.jpg', '2021-09-18 09:40:01', '2024-04-20 21:03:58', 0);
INSERT INTO `sku_poster` VALUES (49, 6, 'sigua.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/sigua.jpg', '2021-09-18 09:40:01', '2024-04-20 21:03:58', 0);
INSERT INTO `sku_poster` VALUES (50, 6, 'sigua.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/sigua.jpg', '2021-09-18 09:40:01', '2024-04-20 21:03:58', 0);
INSERT INTO `sku_poster` VALUES (54, 8, 'qiezi.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/qiezi.jpg', '2021-09-18 09:40:01', '2024-04-20 21:03:58', 0);
INSERT INTO `sku_poster` VALUES (55, 8, 'qiezi.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/qiezi.jpg', '2021-09-18 09:40:01', '2024-04-20 21:03:58', 0);
INSERT INTO `sku_poster` VALUES (56, 9, 'yumi.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/yumi.jpg', '2021-09-18 09:40:01', '2024-04-20 21:03:58', 0);
INSERT INTO `sku_poster` VALUES (57, 9, 'yumi.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/yumi.jpg', '2021-09-18 09:40:01', '2024-04-20 21:03:58', 0);
INSERT INTO `sku_poster` VALUES (59, 12, 'juzi.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/juzi.jpg', '2021-09-18 09:40:01', '2024-04-20 21:03:58', 0);
INSERT INTO `sku_poster` VALUES (60, 10, 'nangua.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/nangua.jpg', '2021-09-18 09:40:01', '2024-04-20 21:03:58', 0);
INSERT INTO `sku_poster` VALUES (61, 10, 'nangua.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/nangua.jpg', '2021-09-18 09:40:01', '2024-04-20 21:03:58', 0);
INSERT INTO `sku_poster` VALUES (62, 4, 'dasuan.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/dasuan.jpg', '2021-09-18 09:40:01', '2024-04-20 21:03:58', 0);
INSERT INTO `sku_poster` VALUES (63, 4, 'dasuan.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/dasuan.jpg', '2021-09-18 09:40:01', '2024-04-20 21:03:58', 0);
INSERT INTO `sku_poster` VALUES (138, 1, 'xihongshi.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/xihongshi.jpg', '2021-09-18 09:41:28', '2024-04-20 21:03:58', 0);
INSERT INTO `sku_poster` VALUES (139, 1, 'xihongshi.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/xihongshi.jpg', '2021-09-18 09:41:28', '2024-04-20 21:03:58', 0);
INSERT INTO `sku_poster` VALUES (146, 5, 'tudou.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/tudou.jpg', '2021-09-18 09:40:01', '2024-04-20 21:03:58', 0);
INSERT INTO `sku_poster` VALUES (147, 5, 'tudou.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/20/tudou.jpg', '2021-09-18 09:40:01', '2024-04-20 21:03:58', 0);
INSERT INTO `sku_poster` VALUES (148, 14, 'banana.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/19/855f3fa679904315b7224495e3002ab4banana.jpg', '2024-04-19 21:04:38', '2024-04-20 18:02:37', 1);
INSERT INTO `sku_poster` VALUES (149, 14, 'banana.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/19/855f3fa679904315b7224495e3002ab4banana.jpg', '2024-04-19 21:04:38', '2024-04-20 18:05:29', 1);
INSERT INTO `sku_poster` VALUES (150, 14, 'banana.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/19/855f3fa679904315b7224495e3002ab4banana.jpg', '2024-04-19 21:04:38', '2024-04-19 21:04:38', 0);
INSERT INTO `sku_poster` VALUES (151, 15, 'jitui.jpg', 'https://group-buying-chn.oss-cn-beijing.aliyuncs.com/2024/04/23/952b49dab35b43e38842938609b4621djitui.jpg', '2024-04-23 21:09:50', '2024-04-23 21:09:50', 0);

-- ----------------------------
-- Table structure for sku_stock_history
-- ----------------------------
DROP TABLE IF EXISTS `sku_stock_history`;
CREATE TABLE `sku_stock_history`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sku_id` bigint(20) NOT NULL DEFAULT 0,
  `price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '销售价格',
  `stock` int(11) NOT NULL DEFAULT 0 COMMENT '库存',
  `sale` int(11) NOT NULL DEFAULT 0 COMMENT '销量',
  `sale_date` date NULL DEFAULT NULL COMMENT '销售日期',
  `ware_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '仓库',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 1 COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'sku的库存历史记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ware
-- ----------------------------
DROP TABLE IF EXISTS `ware`;
CREATE TABLE `ware`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '名称',
  `province` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省code',
  `city` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '城市code',
  `district` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区域code',
  `detail_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `longitude` decimal(10, 7) NULL DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10, 7) NULL DEFAULT NULL COMMENT '纬度',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '仓库表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ware
-- ----------------------------
INSERT INTO `ware` VALUES (1, '成都仓库', NULL, NULL, NULL, NULL, NULL, NULL, '2021-06-15 08:07:17', '2021-06-15 08:07:17', 0);

SET FOREIGN_KEY_CHECKS = 1;
