/*
Navicat MySQL Data Transfer

Source Server         : RemotMySQL
Source Server Version : 50625
Source Host           : 192.168.1.253:3306
Source Database       : maoding_new

Target Server Type    : MYSQL
Target Server Version : 50625
File Encoding         : 65001

Date: 2018-09-14 23:04:28
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for maoding_process_type
-- ----------------------------
DROP TABLE IF EXISTS `maoding_process_type`;
CREATE TABLE `maoding_process_type` (
  `id` varchar(32) NOT NULL COMMENT '流程ID',
  `company_id` varchar(64) DEFAULT NULL COMMENT '流程实例id',
  `target_type` varchar(32) DEFAULT NULL COMMENT '业务类型',
  `type` int(1) DEFAULT '0' COMMENT '流程类型（1：自由流程，2：固定流程，3：分条件流程）',
  `status` int(1) DEFAULT '0' COMMENT '0:未启用，1：启用',
  `deleted` int(1) DEFAULT '0' COMMENT '删除标识',
  `create_date` datetime DEFAULT NULL,
  `create_by` varchar(50) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `update_by` varchar(50) DEFAULT NULL,
  `condition_field_id` varchar(32) DEFAULT NULL COMMENT '动态表单中，用于作为条件流程的字段id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='流程与业务表的关联表';
