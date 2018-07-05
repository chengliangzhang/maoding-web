/*
Navicat MySQL Data Transfer

Source Server         : RemotMySQL
Source Server Version : 50625
Source Host           : 172.16.6.71:3306
Source Database       : maoding_qa

Target Server Type    : MYSQL
Target Server Version : 50625
File Encoding         : 65001

Date: 2018-03-02 11:07:35
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for maoding_web_role
-- ----------------------------
DROP TABLE IF EXISTS `maoding_web_role`;
CREATE TABLE `maoding_web_role` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `company_id` varchar(32) DEFAULT NULL COMMENT '公司ID（此字段为空则表示当前角色是公用角色）',
  `code` varchar(32) DEFAULT NULL COMMENT '角色编码',
  `name` varchar(32) DEFAULT NULL COMMENT '角色名称',
  `status` char(1) DEFAULT NULL COMMENT '0=生效，1＝不生效',
  `order_index` int(11) DEFAULT NULL COMMENT '角色排序',
  `create_date` datetime DEFAULT NULL,
  `create_by` varchar(50) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `update_by` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `company_id` (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='前台角色表';

-- ----------------------------
-- Records of maoding_web_role
-- ----------------------------
INSERT INTO `maoding_web_role` VALUES ('0726c6aba7fa40918bb6e795bbe51059', null, 'AdminManager', '行政管理', '1', '6', '2015-12-10 15:59:24', null, '2015-12-18 13:29:55', null);
INSERT INTO `maoding_web_role` VALUES ('0fb8c188097a4d01a0ff6bb9cacb308e', null, 'FinancialManager', '财务管理', '0', '5', '2015-12-01 16:15:46', null, '2015-12-18 13:29:43', null);
INSERT INTO `maoding_web_role` VALUES ('157fb631852e41beaed4809ea4cd8d97', null, 'ProjectManager', '任务管理', '1', '7', '2015-12-01 16:14:42', null, '2015-12-18 13:32:39', null);
INSERT INTO `maoding_web_role` VALUES ('23297de920f34785b7ad7f9f6f5f1112', null, 'OperateManager', '项目管理', '0', '4', '2016-07-25 19:18:26', null, '2016-07-25 19:18:31', null);
INSERT INTO `maoding_web_role` VALUES ('23297de920f34785b7ad7f9f6f5fe9d0', null, 'GeneralManager', '组织管理', '0', '3', '2015-12-01 16:14:04', null, '2015-12-18 13:29:45', null);
INSERT INTO `maoding_web_role` VALUES ('23297de920f34785b7ad7f9f6f5fe9d1', null, 'OrgManager', '企业负责人', '0', '1', null, null, null, null);
INSERT INTO `maoding_web_role` VALUES ('2f84f20610314637a8d5113440c69bde', null, 'SystemManager', '系统管理员', '0', '2', '2015-12-01 16:14:03', null, null, null);
