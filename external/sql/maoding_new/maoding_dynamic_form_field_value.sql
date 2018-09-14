/*
Navicat MySQL Data Transfer

Source Server         : RemotMySQL
Source Server Version : 50625
Source Host           : 192.168.1.253:3306
Source Database       : maoding_new

Target Server Type    : MYSQL
Target Server Version : 50625
File Encoding         : 65001

Date: 2018-09-14 11:42:57
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for maoding_dynamic_form_field_value
-- ----------------------------
DROP TABLE IF EXISTS `maoding_dynamic_form_field_value`;
CREATE TABLE `maoding_dynamic_form_field_value` (
  `id` varchar(32) NOT NULL COMMENT '动态表单字段的id',
  `main_id` varchar(32) DEFAULT NULL COMMENT '审批主表id（maoding_web_exp_main 的id）',
  `field_id` varchar(32) DEFAULT NULL COMMENT '动态字段表id（maoding_dynamic_form_field的id）',
  `field_value` varchar(1000) DEFAULT NULL COMMENT '值（全部用字符串类型接收）',
  `field_value_pid` varchar(32) DEFAULT NULL COMMENT '父记录id（maoding_dynamic_form_field_value）,如果pid不为null，则代表的是明细中的字段',
  `deleted` int(1) DEFAULT NULL COMMENT '删除标识',
  `create_date` datetime DEFAULT NULL,
  `create_by` varchar(50) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `update_by` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='动态表实例值';
