/*
Navicat MySQL Data Transfer

Source Server         : RemotMySQL
Source Server Version : 50625
Source Host           : 192.168.1.253:3306
Source Database       : maoding_new

Target Server Type    : MYSQL
Target Server Version : 50625
File Encoding         : 65001

Date: 2018-09-14 11:42:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for maoding_dynamic_form
-- ----------------------------
DROP TABLE IF EXISTS `maoding_dynamic_form`;
CREATE TABLE `maoding_dynamic_form` (
  `id` varchar(32) NOT NULL COMMENT '动态表单的id',
  `form_name` varchar(32) DEFAULT NULL COMMENT '名称',
  `company_id` varchar(32) DEFAULT NULL COMMENT '组织id（如果为null，则为公共的可以供每个组织选择复制的）',
  `form_type` varchar(32) DEFAULT NULL COMMENT '表单类型',
  `seq` int(4) DEFAULT NULL COMMENT '排序',
  `status` int(2) DEFAULT NULL COMMENT '1：被启用，0：未被启用',
  `deleted` int(1) DEFAULT NULL COMMENT '删除标识',
  `create_date` datetime DEFAULT NULL,
  `create_by` varchar(50) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `update_by` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='动态表单主表';
