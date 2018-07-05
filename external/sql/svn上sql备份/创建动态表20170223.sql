/*
Navicat MySQL Data Transfer

Source Server         : idccapp
Source Server Version : 50625
Source Host           : 172.16.6.71:3306
Source Database       : maoding_new

Target Server Type    : MYSQL
Target Server Version : 50625
File Encoding         : 65001

Date: 2017-02-23 15:04:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for maoding_web_org_dynamic
-- ----------------------------
DROP TABLE IF EXISTS `maoding_web_org_dynamic`;
CREATE TABLE `maoding_web_org_dynamic` (
  `id` varchar(32) NOT NULL COMMENT '动态id',
  `dynamic_title` varchar(500) DEFAULT NULL COMMENT '动态标题',
  `dynamic_content` longtext COMMENT '动态内容',
  `company_id` varchar(32) DEFAULT NULL COMMENT '公司id',
  `dynamic_type` varchar(1) DEFAULT NULL COMMENT '动态类型（3确定合作方，2确定乙方,1项目立项，4通知公告 （暂时产品给出四种））',
  `target_id` varchar(32) DEFAULT NULL COMMENT '目标id(项目id或者通知公告Id)',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_date` datetime DEFAULT NULL COMMENT '修改时间',
  `field1` varchar(255) DEFAULT NULL COMMENT '预留字段',
  `field2` varchar(255) DEFAULT NULL COMMENT '预留字段',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
