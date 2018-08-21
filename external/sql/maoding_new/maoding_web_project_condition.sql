/*
Navicat MySQL Data Transfer

Source Server         : RemotMySQL
Source Server Version : 50625
Source Host           : 192.168.1.253:3306
Source Database       : maoding_new

Target Server Type    : MYSQL
Target Server Version : 50625
File Encoding         : 65001

Date: 2018-08-21 18:48:13
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for maoding_web_project_condition
-- ----------------------------
DROP TABLE IF EXISTS `maoding_web_project_condition`;
CREATE TABLE `maoding_web_project_condition` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `company_id` varchar(32) DEFAULT NULL COMMENT '企业id',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
  `code` varchar(256) DEFAULT NULL COMMENT '查询code值',
  `type` int(1) DEFAULT NULL COMMENT '类型：0：我的项目；1：项目总览',
  `status` int(1) DEFAULT NULL COMMENT '是否有效：0：有效：1：无效',
  `create_date` datetime DEFAULT NULL,
  `create_by` varchar(32) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `update_by` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
