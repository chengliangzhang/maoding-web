/*
Navicat MySQL Data Transfer

Source Server         : RemotMySQL
Source Server Version : 50625
Source Host           : 192.168.1.253:3306
Source Database       : maoding_new

Target Server Type    : MYSQL
Target Server Version : 50625
File Encoding         : 65001

Date: 2018-07-05 10:46:13
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for maoding_web_project_property
-- ----------------------------
DROP TABLE IF EXISTS `maoding_web_project_property`;
CREATE TABLE `maoding_web_project_property` (
  `id` char(32) NOT NULL COMMENT '项目自定义属性ID编号',
  `project_id` char(32) NOT NULL DEFAULT '' COMMENT '项目自定义属性所属项目的ID',
  `field_name` varchar(255) NOT NULL DEFAULT '' COMMENT '字段名称',
  `unit_name` varchar(20) NOT NULL DEFAULT '' COMMENT '字段单位编号，单位名称在const表内记录',
  `field_value` varchar(255) DEFAULT NULL COMMENT '自定义属性的值，统一格式化为字符串',
  `sequencing` int(8) unsigned DEFAULT '0' COMMENT '排序次序',
  `deleted` tinyint(1) unsigned DEFAULT '0' COMMENT '已删除',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` char(32) DEFAULT NULL COMMENT '创建人',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` char(32) DEFAULT NULL COMMENT '更新人',
  `unit_id` smallint(2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `project_id` (`project_id`),
  KEY `deleted` (`deleted`),
  KEY `unit_id` (`unit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
