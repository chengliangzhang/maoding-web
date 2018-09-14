/*
Navicat MySQL Data Transfer

Source Server         : RemotMySQL
Source Server Version : 50625
Source Host           : 192.168.1.253:3306
Source Database       : maoding_new

Target Server Type    : MYSQL
Target Server Version : 50625
File Encoding         : 65001

Date: 2018-09-14 11:43:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for maoding_dynamic_form_field_selectable_value
-- ----------------------------
DROP TABLE IF EXISTS `maoding_dynamic_form_field_selectable_value`;
CREATE TABLE `maoding_dynamic_form_field_selectable_value` (
  `id` varchar(32) NOT NULL COMMENT '动态表单字段的id',
  `field_id` varchar(32) DEFAULT NULL COMMENT '动态字段表id（maoding_dynamic_form_field的id）',
  `selectable_value` varchar(100) DEFAULT NULL COMMENT '值（例如：select 下拉框对应的value，如果前端并没有设置，后台默认为vlaue = name）',
  `selectable_name` varchar(100) DEFAULT NULL COMMENT '名称（显示在外面的名称）',
  `seq` int(4) DEFAULT NULL COMMENT '排序',
  `deleted` int(1) DEFAULT NULL COMMENT '删除标识',
  `create_date` datetime DEFAULT NULL,
  `create_by` varchar(50) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `update_by` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='动态表单字段可选值表';
