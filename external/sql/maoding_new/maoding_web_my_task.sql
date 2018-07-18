/*
Navicat MySQL Data Transfer

Source Server         : RemotMySQL
Source Server Version : 50625
Source Host           : 192.168.1.253:3306
Source Database       : maoding_new

Target Server Type    : MYSQL
Target Server Version : 50625
File Encoding         : 65001

Date: 2018-07-18 16:53:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for maoding_web_my_task
-- ----------------------------
DROP TABLE IF EXISTS `maoding_web_my_task`;
CREATE TABLE `maoding_web_my_task` (
  `id` varchar(32) NOT NULL COMMENT '主键id，uuid',
  `task_content` longtext COMMENT '任务内容',
  `task_title` varchar(100) DEFAULT NULL COMMENT '任务标题',
  `task_type` int(2) DEFAULT NULL COMMENT '任务类型(1.签发：经营负责人,2.生产安排（项目设计负责人）.13.生产安排（任务负责人。）',
  `handler_id` varchar(32) DEFAULT NULL COMMENT '任务处理人（company_user_id）',
  `company_id` varchar(32) DEFAULT NULL COMMENT '组织id（company_id）',
  `project_id` varchar(255) DEFAULT NULL COMMENT '项目id',
  `status` varchar(1) DEFAULT NULL COMMENT '任务状态 是否已处理(0.未处理，1.已处，3：开始中',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `target_id` varchar(32) DEFAULT NULL COMMENT '项目id,报销单id,设计节点id',
  `param1` varchar(255) DEFAULT NULL COMMENT '保存任务targetId中关联的数据的id(taskType = 3 ，保存的是任务的id,taskType = 财务的，保存的是所在收款节点，便于后面查询）',
  `param2` varchar(255) DEFAULT NULL COMMENT '审核：param2=2：退回，param2=1：同意',
  `param3` varchar(255) DEFAULT NULL COMMENT '任务分组：1：财务类型（项目财务任务，可一批人处理，所以没有handlerId，用param3=1标识为财务型）',
  `param4` varchar(255) DEFAULT NULL COMMENT '删除标识：0：有效，1：无效',
  `send_company_id` varchar(32) DEFAULT NULL COMMENT '发送任务的组织id',
  `deadline` date DEFAULT NULL COMMENT '截止日期',
  `complete_date` date DEFAULT NULL COMMENT '完成日期',
  `start_date` date DEFAULT NULL COMMENT '开始时间',
  PRIMARY KEY (`id`),
  KEY `handler_id` (`handler_id`),
  KEY `company_id` (`company_id`),
  KEY `project_id` (`project_id`),
  KEY `target_id` (`target_id`),
  KEY `send_company_id` (`send_company_id`),
  KEY `task_type` (`task_type`) USING BTREE,
  KEY `param1` (`param1`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='我的任务表';
