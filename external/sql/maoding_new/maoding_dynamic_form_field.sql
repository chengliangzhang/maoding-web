/*
Navicat MySQL Data Transfer

Source Server         : RemotMySQL
Source Server Version : 50625
Source Host           : 192.168.1.253:3306
Source Database       : maoding_new

Target Server Type    : MYSQL
Target Server Version : 50625
File Encoding         : 65001

Date: 2018-09-14 11:42:41
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for maoding_dynamic_form_field
-- ----------------------------
DROP TABLE IF EXISTS `maoding_dynamic_form_field`;
CREATE TABLE `maoding_dynamic_form_field` (
  `id` varchar(32) NOT NULL COMMENT '动态表单字段的id',
  `form_id` varchar(32) DEFAULT NULL COMMENT '主表id（maoding_dynamic_form的id）',
  `field_pid` varchar(32) DEFAULT NULL COMMENT '父记录id（maoding_dynamic_form_field的id）,如果pid不为null，则代表的是明细中的字段',
  `field_title` varchar(32) DEFAULT NULL COMMENT '标题',
  `field_type` int(2) DEFAULT '0' COMMENT '字段类型',
  `field_unit` varchar(10) DEFAULT '0' COMMENT '字段的单位',
  `is_statistics` int(2) DEFAULT '0' COMMENT '是否参与统计（0：不参与，1：参与）',
  `field_tooltip` varchar(32) DEFAULT NULL COMMENT '提示文本',
  `field_default_value` varchar(100) DEFAULT NULL COMMENT '默认值（暂时定100个长度）',
  `field_select_value_type` int(4) DEFAULT '0' COMMENT '默认值0(由数据字典提供，如果为0，则从maoding_dynamic_form_field_selectable_value 去获取)',
  `seq_x` int(4) DEFAULT NULL COMMENT '横坐标排序（如果x相同，则排成一行）',
  `seq_y` int(4) DEFAULT NULL COMMENT '纵坐标排序',
  `required_type` int(2) DEFAULT '0' COMMENT '必填类型（1：必填，0：非必填）',
  `deleted` int(1) DEFAULT NULL COMMENT '删除标识',
  `create_date` datetime DEFAULT NULL,
  `create_by` varchar(50) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `update_by` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='动态表单字段表';
