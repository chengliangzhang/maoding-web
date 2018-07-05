-- 建立更新表结构存储过程
DROP PROCEDURE IF EXISTS `updatePermissionTables`;
CREATE PROCEDURE `updatePermissionTables`()
BEGIN
  -- maoding_web_role -- 权限组表
  CREATE TABLE IF NOT EXISTS `maoding_web_role` (
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

  -- maoding_web_role_permission -- 权限树表
  CREATE TABLE IF NOT EXISTS `maoding_web_role_permission` (
    `id` varchar(32) NOT NULL COMMENT 'ID',
    `role_id` varchar(32) DEFAULT NULL COMMENT '角色ID',
    `permission_id` varchar(32) DEFAULT NULL COMMENT '权限ID',
    `company_id` varchar(32) DEFAULT NULL COMMENT '公司ID',
    `forbid_relation_type_id` varchar(40) DEFAULT NULL COMMENT '需要屏蔽此权限的组织关系类型',
    `create_date` datetime DEFAULT NULL COMMENT '创建时间',
    `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
    `update_date` datetime DEFAULT NULL COMMENT '更新时间',
    `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`),
    KEY `role_id` (`role_id`),
    KEY `permission_id` (`permission_id`),
    KEY `company_id` (`company_id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色视图表';

  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_web_role_permission' and column_name='forbid_relation_type_id') then
    alter table maoding_web_role_permission add column `forbid_relation_type_id` varchar(40) DEFAULT NULL COMMENT '需要屏蔽此权限的组织关系类型';
  end if;

  -- maoding_web_permission -- 权限表
  CREATE TABLE IF NOT EXISTS `maoding_web_permission` (
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

  -- maoding_web_user_permission -- 组织角色表
  CREATE TABLE IF NOT EXISTS `maoding_web_user_permission` (
    `id` varchar(32) NOT NULL COMMENT 'ID',
    `company_id` varchar(32) DEFAULT NULL COMMENT '公司ID',
    `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
    `permission_id` varchar(32) DEFAULT NULL COMMENT '权限ID',
    `create_date` datetime DEFAULT NULL COMMENT '创建时间',
    `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
    `update_date` datetime DEFAULT NULL COMMENT '更新时间',
    `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
    `seq` int(4) DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `company_id` (`company_id`),
    KEY `user_id` (`user_id`),
    KEY `permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='前台角色权限表';

  -- maoding_web_company_relation -- 组织关系表
  CREATE TABLE IF NOT EXISTS `maoding_web_company_relation` (
    `id` varchar(32) NOT NULL COMMENT '主键id，uuid',
    `org_id` varchar(32) DEFAULT NULL COMMENT '企业id',
    `org_pid` varchar(32) DEFAULT NULL COMMENT '父id',
    `type_id` varchar(40) DEFAULT NULL COMMENT '组织之间的关系类型',
    `create_date` datetime DEFAULT NULL COMMENT '创建时间',
    `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
    `update_date` datetime DEFAULT NULL COMMENT '更新时间',
    `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`),
    KEY `org_id` (`org_id`),
    KEY `org_pid` (`org_pid`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织加盟申请表';

  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_web_company_relation' and column_name='type_id') then
    alter table maoding_web_company_relation add column `type_id` varchar(40) DEFAULT NULL COMMENT '组织之间的关系类型';
  end if;

END;

-- 建立初始化权限数据过程
DROP PROCEDURE IF EXISTS `initPermission`;
CREATE PROCEDURE `initPermission`()
BEGIN
    -- 初始化角色
		REPLACE INTO `maoding_web_role` (`id`, `company_id`, `code`, `name`, `status`, `order_index`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('0726c6aba7fa40918bb6e795bbe51059', NULL, 'AdminManager', '行政管理', '1', '4', '2015-12-10 15:59:24', NULL, '2015-12-18 13:29:55', NULL);
		REPLACE INTO `maoding_web_role` (`id`, `company_id`, `code`, `name`, `status`, `order_index`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('0726c6aba7fa40918bb6e795bbe51060', NULL, 'ApprovalReport', '审批报表', '0', '4', '2015-12-10 15:59:24', NULL, '2015-12-18 13:29:55', NULL);
		REPLACE INTO `maoding_web_role` (`id`, `company_id`, `code`, `name`, `status`, `order_index`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('0fb8c188097a4d01a0ff6bb9cacb308e', NULL, 'FinancialManager', '财务管理', '0', '6', '2015-12-01 16:15:46', NULL, '2015-12-18 13:29:43', NULL);
		REPLACE INTO `maoding_web_role` (`id`, `company_id`, `code`, `name`, `status`, `order_index`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('157fb631852e41beaed4809ea4cd8d97', NULL, 'ProjectManager', '任务管理', '1', '7', '2015-12-01 16:14:42', NULL, '2015-12-18 13:32:39', NULL);
		REPLACE INTO `maoding_web_role` (`id`, `company_id`, `code`, `name`, `status`, `order_index`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('23297de920f34785b7ad7f9f6f5f1112', NULL, 'OperateManager', '项目管理', '0', '5', '2016-07-25 19:18:26', NULL, '2016-07-25 19:18:31', NULL);
		REPLACE INTO `maoding_web_role` (`id`, `company_id`, `code`, `name`, `status`, `order_index`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('23297de920f34785b7ad7f9f6f5fe9d0', NULL, 'GeneralManager', '组织管理', '0', '3', '2015-12-01 16:14:04', NULL, '2015-12-18 13:29:45', NULL);
		REPLACE INTO `maoding_web_role` (`id`, `company_id`, `code`, `name`, `status`, `order_index`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('23297de920f34785b7ad7f9f6f5fe9d1', NULL, 'OrgManager', '企业负责人', '1', '1', NULL, NULL, NULL, NULL);
		REPLACE INTO `maoding_web_role` (`id`, `company_id`, `code`, `name`, `status`, `order_index`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('2f84f20610314637a8d5113440c69bde', NULL, 'SystemManager', '后台管理', '0', '2', '2015-12-01 16:14:03', NULL, NULL, NULL);

    -- 初始化权限
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('1','sys','系统管理','1100','1','');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('2','com','组织管理','1200','1','');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('3','org','企业负责人','1300','1','');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('4','admin','行政管理','1400','1','');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('5','project','项目管理','1500','1','');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('6','finance','财务管理','1600','1','');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('7','report','统计及报表','1700','1','');
    REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('8','sys_role_permission','权限配置','1020','0','企业中，分配每个人所拥有的权限');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('9','sys_role_user','角色成员管理','1900','1','');
    REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('10','sys_finance_type','财务设置','3030','0','设置企业报销/费用可申请或报销的类型进行设置');
    REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('11','sys_enterprise_logout','组织解散','1000','0','描述');
    REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('12','com_enterprise_edit','组织信息管理','2010','0','企业信息编辑 修改 如企业名称 企业简介等');
    REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('14','hr_org_set,hr_employee','组织架构设置','2020','0','企业中相关人员，人员的添加 修改 删除');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('16','hr_employee','员工管理','2400','1','');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('17','hr_user_role','角色权限分配','2000','1','');
    REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('19','admin_notice','通知公告发布','2030','0','企业中相关公告信息 通知等');
    REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('20','project_edit','项目信息/项目总览/查看项目文档','5050','0','企业中所有项目中基本信息的编辑录入');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('21','project_point_edit','合同回款节点基本信息管理','2800','1','');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('22','project_point_condition','合同回款节点回款条件确认','2900','1','');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('23','project_point_invoice','合同回款节点回款开票登记','3000','1','');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('24','project_point_pay','合同回款节点回款到款确认','3100','1','');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('26','project_task_progress','任务计划进度及变更','3300','1','');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('27','project_task_confirm','项目任务节点的完成确认','3400','1','');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('28','project_design_fee_set','合作设计费金额设定','3500','1','');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('29','project_design_fee_audit','合作设计费审核确认','3600','1','');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('30','project_design_fee_pay','合作设计费付款确认','3700','1','');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('31','project_design_fee_paid','合作设计费到款确认','3800','1','');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('32','project_manage_fee_set','技术审查费金额设置','3900','1','');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('33','project_manage_fee_audit','技术审查费审查确认','4000','1','');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('34','project_manage_fee_pay','技术审查费付款确认','4100','1','');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('35','project_manage_fee_paid','技术审查费到款确认','4200','1','');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('36','project_task_manage','任务人员及进度安排','4300','1','');
    REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('37','project_view_amount','合同信息','5040','0','');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('38','project_design_fee_view','查看合作设计费信息','4600','1','');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('39','project_manage_fee_view','查看技术审查费信息','4700','1','');
    REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('40','finance_fixed_edit','费用录入','3020','0','');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('41','report_new_contract','查看新增合同统计报表','4900','1','');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('42','report_contract_pay','查看合同回款统计报表','5000','1','');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('43','report_design_fee','查看合作设计费统计报表','5100','1','');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('44','report_manage_fee','查看技术审查费统计报表','5200','1','');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('45','report_static_fee','查看项目总览报表','5300','1','');
    REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('46','report_exp_static','查看报销、费用统计报表','4010','0','查看企业所有人员的报销/费用汇总情况');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('47','report_finance_static','查看财务总览报表','5500','1','');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('48','project_view_point','查看合同回款信息','4500','1','');
    REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('49','project_charge_manage','确认付款日期','3040','0','项目费用（合同回款/技术审查费/合作设计费/其他收支)的到账/付款确认，报销/费用的拨款处理');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('50','org_manager','权限分配、删除项目、查看企业所有信息','5800','1','');
    REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('51','project_manager','任务签发','5020','0','企业中从事经营活动的相关人员进行任务的经营签发活动<br>注:系统默认新项目的经营负责人为排在任务签发第一位的人员');
    REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('52','design_manager','生产安排','5030','0','企业中的设计负责人可对经营负责人发布过来的任务进行具体安排<br>注:系统默认新项目的设计负责人为排在生产安排第一位的人员');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('53','super_project_edit','给分公司、事业合伙人编辑项目基本信息','2750','1','');
    REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('54','project_delete','删除项目','5010','0','描述');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('56','project_overview','项目总览','2670','1','企业中所有项目信息的查看权限');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('57','project_archive','查看项目文档','2680','1','企业中所有项目文档（设计依据/归档文件/交付文件)的查看下载');
    REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('58','sys_role_auth','企业认证','1030','0','企业认证的权限');
    REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('100','org_partner','创建分支机构/事业合伙人','1010','0','描述');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('101','org_auth,sys_role_auth','企业认证（作废）','5000','1','描述');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('102','org_permission,sys_role_permission','权限分配','2000','1','描述');
    REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('103','data_import','历史数据导入','1040','0','企业中历史数据打入权限');
  -- REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('104','background_management','后台管理','1300','1','');
    REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('110','summary_leave','查看请假、出差、工时统计等报表','4020','0','企业中相关人员，请假/出差审批完成后的汇总记录');
    REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('401','finance_report','查看财务报表','3010','0','查看收支明细、分类统计、利润报表等');
    REPLACE INTO `maoding_web_permission` (`id`, `code`, `name`, `seq`, `status`, `description`) VALUES ('402','finance_back_fee','确认到账日期','3050','0','');

   --  删除所有
    	delete from maoding_web_role_permission ;

  -- 后台管理权限
		delete from maoding_web_role_permission where role_id = '2f84f20610314637a8d5113440c69bde';
		insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date,forbid_relation_type_id)
      select replace(uuid(),'-',''),'2f84f20610314637a8d5113440c69bde',p.id,null,now(),'2,3' from maoding_web_permission p
      where p.id in (100);
		insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date,forbid_relation_type_id)
      select replace(uuid(),'-',''),'2f84f20610314637a8d5113440c69bde',p.id,null,now(),'' from maoding_web_permission p
      where p.id in (8);
		insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date,forbid_relation_type_id)
      select replace(uuid(),'-',''),'2f84f20610314637a8d5113440c69bde',p.id,null,now(),'1,2,3' from maoding_web_permission p
      where p.id in (58,103);

  -- 组织管理权限
   delete from maoding_web_role_permission where role_id = '23297de920f34785b7ad7f9f6f5fe9d0';
	 insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date,forbid_relation_type_id)
      select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5fe9d0',p.id,null,now(),'1,2,3' from maoding_web_permission p
      where p.id in (12);
	 insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date,forbid_relation_type_id)
      select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5fe9d0',p.id,null,now(),'' from maoding_web_permission p
      where p.id in (14,19);

  -- 财务管理权限
	 delete from maoding_web_role_permission where role_id = '0fb8c188097a4d01a0ff6bb9cacb308e';
   insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date,forbid_relation_type_id)
      select replace(uuid(),'-',''),'0fb8c188097a4d01a0ff6bb9cacb308e',p.id,null,now(),'' from maoding_web_permission p
      where p.id in (401);
   insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date,forbid_relation_type_id)
      select replace(uuid(),'-',''),'0fb8c188097a4d01a0ff6bb9cacb308e',p.id,null,now(),'3' from maoding_web_permission p
      where p.id in (402,49);
   insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date,forbid_relation_type_id)
      select replace(uuid(),'-',''),'0fb8c188097a4d01a0ff6bb9cacb308e',p.id,null,now(),'2,3' from maoding_web_permission p
      where p.id in (40);
   insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date,forbid_relation_type_id)
      select replace(uuid(),'-',''),'0fb8c188097a4d01a0ff6bb9cacb308e',p.id,null,now(),'1,2,3' from maoding_web_permission p
      where p.id in (10);

   -- 审批管理权限
	 delete from maoding_web_role_permission where role_id = '0726c6aba7fa40918bb6e795bbe51060';
   insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date,forbid_relation_type_id)
      select replace(uuid(),'-',''),'0726c6aba7fa40918bb6e795bbe51060',p.id,null,now(),'' from maoding_web_permission p
      where p.id in (46,110);

   -- 项目管理权限
	 delete from maoding_web_role_permission where role_id = '23297de920f34785b7ad7f9f6f5f1112';
   insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date,forbid_relation_type_id)
      select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5f1112',p.id,null,now(),'' from maoding_web_permission p
      where p.id in (54,51,52,37,20);

   -- 根据模板创建各组织的权限关系表
   insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date,forbid_relation_type_id)
        select replace(uuid(),'-',''),p.role_id,p.permission_id,c.id,now(),p.forbid_relation_type_id
        from maoding_web_role_permission p
        inner join maoding_web_company c on (p.company_id is null);

   -- 把 项目总览，查看项目文档 上的人 归并到  项目信息编辑、项目总览、查看项目文档
   update maoding_web_user_permission set permission_id = 20 where permission_id = 56 or permission_id = 57;

   -- 把付款日期确认的人员默认到到账日期确认、费用录入、查看财务报表的权限上
   insert into maoding_web_user_permission (id,company_id,user_id,permission_id,create_date,seq)
      select replace(uuid(),'-',''),company.id,role.user_id,role_def.permission_id,now(),1
      from maoding_web_company company
        inner join maoding_web_role_permission role_def on (role_def.permission_id in (402,40,401) and role_def.company_id is null)
        inner join maoding_web_user_permission role on (company.id = role.company_id and role.permission_id = 49)
        left join maoding_web_user_permission role_have on (role_have.permission_id = role_def.permission_id and company.id = role_have.company_id)
      where role_have.permission_id is null;

   -- 设定缺失的权限负责人为系统管理员
   insert into maoding_web_user_permission (id,company_id,user_id,permission_id,create_date,seq)
        select replace(uuid(),'-',''),company.id,sys_role.user_id,role_def.permission_id,now(),1
				from maoding_web_company company
            inner join maoding_web_role_permission role_def on (role_def.company_id is null)
						inner join maoding_web_team_operater sys_role on (company.id = sys_role.company_id)
  					left join maoding_web_user_permission role_have on (role_have.permission_id = role_def.permission_id and company.id = role_have.company_id)
				where role_have.permission_id is null;

   insert into maoding_web_user_permission (id,company_id,user_id,permission_id,create_date,seq)
        select replace(uuid(),'-',''),company.id,sys_role.user_id,11,now(),1
				from maoding_web_company company
						inner join maoding_web_team_operater sys_role on (company.id = sys_role.company_id)
  					left join maoding_web_user_permission role_have on (role_have.permission_id = 11 and company.id = role_have.company_id)
				where role_have.permission_id is null;

END;

call updatePermissionTables();
call initPermission();