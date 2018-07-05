/*
Navicat MySQL Data Transfer

Source Server         : LocalMySQL
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : maoding_test

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2017-12-08 17:00:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for maoding_corp_sync_company
-- ----------------------------
DROP TABLE IF EXISTS `maoding_corp_sync_company`;
CREATE TABLE `maoding_corp_sync_company` (
  `id` varchar(32) NOT NULL COMMENT '主键id，uuid',
  `corp_endpoint` varchar(32) NOT NULL COMMENT '协同端id，唯一性标记',
  `company_id` varchar(32) NOT NULL COMMENT '团队Id',
  `remarks` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  KEY `company_id` (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AVG_ROW_LENGTH=8192 COMMENT='协同团队同步清单';

-- ----------------------------
-- Records of maoding_corp_sync_company
-- ----------------------------
INSERT INTO `maoding_corp_sync_company` VALUES ('97a6e439340f4405959664a2cff396b9', 'EP1', '64486f128da14f35932e63c5be0fc32c', '', '2017-06-29 10:34:32', null, '2017-06-29 10:34:32', null);
INSERT INTO `maoding_corp_sync_company` VALUES ('c463e19d08c2420b9e6332263fd9b3f1', 'EP1', '02077b2b62d143d7b912c6c234cc5d47', '', '2017-06-29 18:10:32', null, '2017-06-29 18:10:32', null);
INSERT INTO `maoding_corp_sync_company` VALUES ('e14cefeac3c147e08ef43f19678c1537', 'EP1', '5e0e1532f5e746ab86db8f04c656a8c7', '', '2017-06-29 15:28:58', null, '2017-06-29 15:28:58', null);
