/*
Navicat MySQL Data Transfer

Source Server         : RemotMySQL
Source Server Version : 50625
Source Host           : 192.168.1.253:3306
Source Database       : maoding_new

Target Server Type    : MYSQL
Target Server Version : 50625
File Encoding         : 65001

Date: 2018-06-26 18:38:58
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for maoding_web_project_task
-- ----------------------------
DROP TABLE IF EXISTS `maoding_web_project_task`;
CREATE TABLE `maoding_web_project_task` (
  `id` varchar(32) NOT NULL COMMENT '主键id，uuid',
  `from_company_id` varchar(32) DEFAULT NULL,
  `company_id` varchar(32) DEFAULT NULL COMMENT '组织id',
  `project_id` varchar(32) NOT NULL COMMENT '项目id',
  `org_id` varchar(32) DEFAULT NULL COMMENT '部门id',
  `task_name` varchar(200) NOT NULL COMMENT '任务名称',
  `task_pid` varchar(32) DEFAULT NULL COMMENT '父id',
  `task_path` text COMMENT '任务完整路径id-id',
  `task_type` int(1) DEFAULT NULL COMMENT '类型（签发设计阶段或服务内容\r\n1=设计阶段 \r\n2=签发\r\n0=生产\r\n3=签发（未发布）\r\n4=生产（未发布），5:=设计任务',
  `task_level` int(11) DEFAULT NULL COMMENT '签发次数级别',
  `task_status` varchar(1) DEFAULT '0' COMMENT '0生效，1删除,2:未发布，3：未发布（修改）',
  `task_remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  `seq` int(4) DEFAULT NULL COMMENT '排序',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `is_operater_task` int(1) DEFAULT NULL COMMENT '是否是经营任务（1：经营任务，0：是经营任务，但是可以进行生产，或许直接是生产任务）',
  `end_status` int(1) DEFAULT '0' COMMENT '结束状态：0=未开始，1=已完成，2=已终止',
  `complete_date` date DEFAULT NULL COMMENT '完成时间',
  `be_modify_id` varchar(32) DEFAULT NULL COMMENT '被修改记录的id，用于修改任务，新增一条未被发布的数据，该字段记录被修改记录的id',
  `start_time` date DEFAULT NULL,
  `end_time` date DEFAULT NULL,
  `completion` varchar(200) DEFAULT NULL COMMENT '完成情况',
  PRIMARY KEY (`id`),
  KEY `from_company_id` (`from_company_id`),
  KEY `company_id` (`company_id`),
  KEY `project_id` (`project_id`),
  KEY `org_id` (`org_id`),
  KEY `task_pid` (`task_pid`),
  KEY `be_modify_id` (`be_modify_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务表';
