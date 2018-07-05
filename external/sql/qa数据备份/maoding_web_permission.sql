/*
Navicat MySQL Data Transfer

Source Server         : RemotMySQL
Source Server Version : 50625
Source Host           : 172.16.6.71:3306
Source Database       : maoding_new

Target Server Type    : MYSQL
Target Server Version : 50625
File Encoding         : 65001

Date: 2018-03-02 10:44:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for maoding_web_permission
-- ----------------------------
DROP TABLE IF EXISTS `maoding_web_permission`;
CREATE TABLE `maoding_web_permission` (
  `id` varchar(32) NOT NULL COMMENT '视图ID',
  `code` varchar(96) DEFAULT NULL COMMENT 'code值',
  `name` varchar(32) DEFAULT NULL COMMENT '权限名称',
  `pid` varchar(32) DEFAULT NULL COMMENT '父权限ID',
  `root_id` varchar(32) DEFAULT NULL COMMENT '根权限ID',
  `seq` int(11) DEFAULT NULL COMMENT '排序',
  `status` char(1) DEFAULT NULL COMMENT '0=生效，1＝不生效',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `description` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  KEY `root_id` (`root_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限表';

-- ----------------------------
-- Records of maoding_web_permission
-- ----------------------------
INSERT INTO `maoding_web_permission` VALUES ('1', 'sys', '系统管理', null, '1', '1100', '0', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('10', 'sys_finance_type', '财务费用类别设置', '6', '6', '5600', '0', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('11', 'sys_enterprise_logout', '组织解散', '1', '1', '2100', '0', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('12', 'com_enterprise_edit', '组织信息管理', '2', '2', '2200', '0', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('14', 'hr_org_set,hr_employee', '组织架构设置', '2', '2', '2300', '0', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('16', 'hr_employee', '员工管理', '4', '4', '2400', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('17', 'hr_user_role', '角色权限分配', '1', '1', '2000', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('19', 'admin_notice', '通知公告发布', '4', '4', '2600', '0', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('2', 'com', '组织管理', null, '2', '1200', '0', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('20', 'project_eidt', '项目基本信息编辑', '5', '5', '2700', '0', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('21', 'project_point_edit', '合同回款节点基本信息管理', '5', '5', '2800', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('22', 'project_point_condition', '合同回款节点回款条件确认', '5', '5', '2900', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('23', 'project_point_invoice', '合同回款节点回款开票登记', '5', '5', '3000', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('24', 'project_point_pay', '合同回款节点回款到款确认', '5', '5', '3100', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('25', 'project_task_issue', '任务签发', '5', '5', '3200', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('26', 'project_task_progress', '任务计划进度及变更', '5', '5', '3300', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('27', 'project_task_confirm', '项目任务节点的完成确认', '5', '5', '3400', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('28', 'project_design_fee_set', '合作设计费金额设定', '5', '5', '3500', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('29', 'project_design_fee_audit', '合作设计费审核确认', '5', '5', '3600', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('3', 'org', '企业负责人', null, '3', '1300', '0', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('30', 'project_design_fee_pay', '合作设计费付款确认', '5', '5', '3700', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('31', 'project_design_fee_paid', '合作设计费到款确认', '5', '5', '3800', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('32', 'project_manage_fee_set', '技术审查费金额设置', '5', '5', '3900', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('33', 'project_manage_fee_audit', '技术审查费审查确认', '5', '5', '4000', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('34', 'project_manage_fee_pay', '技术审查费付款确认', '5', '5', '4100', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('35', 'project_manage_fee_paid', '技术审查费到款确认', '5', '5', '4200', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('36', 'project_task_manage', '任务人员及进度安排', '5', '5', '4300', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('37', 'project_view_amount', '查看合同扫描件', '2', '2', '4400', '0', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('38', 'project_design_fee_view', '查看合作设计费信息', '5', '5', '4600', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('39', 'project_manage_fee_view', '查看技术审查费信息', '5', '5', '4700', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('4', 'admin', '行政管理', null, '4', '1400', '0', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('40', 'finance_fixed_edit', '财务月账录入', '6', '6', '4800', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('41', 'report_new_contract', '查看新增合同统计报表', '7', '7', '4900', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('42', 'report_contract_pay', '查看合同回款统计报表', '7', '7', '5000', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('43', 'report_design_fee', '查看合作设计费统计报表', '7', '7', '5100', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('44', 'report_manage_fee', '查看技术审查费统计报表', '7', '7', '5200', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('45', 'report_static_fee', '查看项目总览报表', '7', '7', '5300', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('46', 'report_exp_static', '查看报销汇总', '2', '2', '5800', '0', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('47', 'report_finance_static', '查看财务总览报表', '7', '7', '5500', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('48', 'project_view_point', '查看合同回款信息', '5', '5', '4500', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('49', 'project_charge_manage', '项目收支管理', '6', '6', '5700', '0', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('5', 'project', '项目管理', null, '5', '1500', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('50', 'org_manager', '权限分配、删除项目、查看企业所有信息', '3', '3', '5800', '0', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('51', 'project_manager', '任务签发', '5', '5', '2650', '0', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('52', 'design_manager', '生产安排', '5', '5', '2660', '0', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('53', 'super_project_edit', '给分公司、事业合伙人编辑项目基本信息', '5', '5', '2750', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('6', 'finance', '财务管理', null, '6', '1600', '0', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('7', 'report', '统计及报表', null, '7', '1700', '1', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('8', 'sys_role_permission', '权限分配', '1', '1', '1800', '0', null, null, null, null, null);
INSERT INTO `maoding_web_permission` VALUES ('9', 'sys_role_user', '角色成员管理', '1', '1', '1900', '1', null, null, null, null, null);
