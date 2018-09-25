/*
Navicat MySQL Data Transfer

Source Server         : RemotMySQL
Source Server Version : 50625
Source Host           : 192.168.1.253:3306
Source Database       : maoding_new

Target Server Type    : MYSQL
Target Server Version : 50625
File Encoding         : 65001

Date: 2018-09-25 11:42:48
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for maoding_process_instance_relation
-- ----------------------------
DROP TABLE IF EXISTS `maoding_process_instance_relation`;
CREATE TABLE `maoding_process_instance_relation` (
  `id` varchar(32) NOT NULL COMMENT '流程ID',
  `target_id` varchar(32) DEFAULT NULL COMMENT '关联的对象的id',
  `process_instance_id` varchar(64) DEFAULT NULL COMMENT '流程实例id',
  `target_type` varchar(32) DEFAULT NULL COMMENT '业务类型',
  `deleted` int(1) DEFAULT '0' COMMENT '删除标识',
  `create_date` datetime DEFAULT NULL,
  `create_by` varchar(50) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `update_by` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='流程与业务表的关联表';
