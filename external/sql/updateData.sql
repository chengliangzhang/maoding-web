-- 设定已有收付款节点的项目费用总额为收付款节点的费用之和
DROP PROCEDURE IF EXISTS `updateData`;
CREATE PROCEDURE `updateData`()
  BEGIN
    update maoding_web_project_cost cost_list
    set cost_list.fee = (select sum(ifnull(point_list.fee,0))
                         from maoding_web_project_cost_point point_list
                         where
                           point_list.status = '0'
                           and point_list.cost_id = cost_list.id
    )
    where cost_list.fee is null;
  END;
call updateData();
DROP PROCEDURE IF EXISTS `updateData`;

-- 调整已有网盘目录次序
DROP PROCEDURE IF EXISTS `updateData`;
CREATE PROCEDURE `updateData`()
BEGIN
  update maoding_web_project_sky_drive set param4 = 0 where pid is null and file_name = '设计依据';
  update maoding_web_project_sky_drive set param4 = 1 where pid is null and file_name = '设计文件';
  update maoding_web_project_sky_drive set param4 = 2 where pid is null and file_name = '交付文件';
  update maoding_web_project_sky_drive set param4 = 3 where pid is null and file_name = '设计成果';
  update maoding_web_project_sky_drive set param4 = 4 where pid is null and file_name = '归档文件';
END;
-- call updateData();
DROP PROCEDURE IF EXISTS `updateData`;

-- 创建协同服务和客户端版本
DROP PROCEDURE IF EXISTS `updateData`;
CREATE PROCEDURE `updateData`()
BEGIN
    REPLACE INTO `md_list_version` (id,svn_repo,svn_version,app_name,version_name,update_url,description,min_depend_svn_version,max_depend_svn_version) VALUES ('08c1cb756a3911e8a8e000ffecc6934a','maoding-services','11230','卯丁协同','v1.0','1;127.0.0.1;c:/work/file_server;update/11230.jar','初始创建',null,null);
    REPLACE INTO `md_list_version` (id,svn_repo,svn_version,app_name,version_name,update_url,description,min_depend_svn_version,max_depend_svn_version) VALUES ('491d1dee6a4e11e8a8e000ffecc6934a','maoding-services','11240','卯丁协同','v1.1','1;127.0.0.1;c:/work/file_server;update/11240.jar','第一次升级',null,null);
    REPLACE INTO `md_list_version` (id,svn_repo,svn_version,app_name,version_name,update_url,description,min_depend_svn_version,max_depend_svn_version) VALUES ('bdb105856a4011e8a8e000ffecc6934a','app','12231','卯丁协同客户端','v2.0','http://www.maoding.com/','初始创建',null,null);
    REPLACE INTO `md_list_version` (id,svn_repo,svn_version,app_name,version_name,update_url,description,min_depend_svn_version,max_depend_svn_version) VALUES ('ddb2fea36a4011e8a8e000ffecc6934a','app','12232','卯丁协同客户端','v2.1','1;127.0.0.1;c:/work/file_server;update/11240.jar','第一次升级',null,'11230');
    REPLACE INTO `md_list_version` (id,svn_repo,svn_version,app_name,version_name,update_url,description,min_depend_svn_version,max_depend_svn_version) VALUES ('76bc3c7d6a4e11e8a8e000ffecc6934a','app','12233','卯丁协同客户端','v2.2','http://www.maoding.com/','第二次升级','11240',null);
END;
-- call updateData();
DROP PROCEDURE IF EXISTS `updateData`;

-- 从storage内复制owner_user_id到file
DROP PROCEDURE IF EXISTS `updateData`;
CREATE PROCEDURE `updateData`()
BEGIN
    update md_list_storage_file file_list
        inner join md_tree_storage storage_tree on (storage_tree.id = file_list.id)
        inner join
            (select storage_tree_2.id,task.company_id
            from md_tree_storage storage_tree_2
              left join md_web_task task on (task.id = storage_tree_2.task_id)
            ) storage_task on (storage_task.id = storage_tree.id)

    set file_list.owner_user_id = storage_tree.owner_user_id,
				file_list.company_id = storage_task.company_id;
END;
-- call updateData();
DROP PROCEDURE IF EXISTS `updateData`;

-- 建立初始化权限数据过程
DROP PROCEDURE IF EXISTS `updateData`;
CREATE PROCEDURE `updateData`()
BEGIN
    -- 企业负责人
    REPLACE INTO `maoding_web_role` VALUES ('23297de920f34785b7ad7f9f6f5fe9d1', null, 'OrgManager', '企业负责人', '0', '1', null, null, null, null);
    REPLACE INTO `maoding_web_permission` VALUES ('11', 'sys_enterprise_logout', '组织解散', '1', '1', '1000', '0', null, null, null, null, '描述');
    REPLACE INTO `maoding_web_permission` VALUES ('102', 'org_permission,sys_role_permission', '权限分配', '1', '1', '2000', '0', null, null, null, null, '描述');
    REPLACE INTO `maoding_web_permission` VALUES ('54', 'project_delete', '删除项目', '5', '5', '3000', '0', null, null, null, null, '描述');
    REPLACE INTO `maoding_web_permission` VALUES ('100', 'org_partner', '事业合伙人/分公司(创建/邀请)', '1', '1', '4000', '0', null, null, null, null, '描述');
    REPLACE INTO `maoding_web_permission` VALUES ('101', 'org_auth,sys_role_auth', '企业认证', '1', '1', '5000', '0', null, null, null, null, '描述');
    REPLACE INTO `maoding_web_permission` VALUES ('103', 'org_data_import,data_import', '历史数据导入', '5', '5', '6000', '0', null, null, null, null, '描述');

    delete from maoding_web_role_permission where role_id = '23297de920f34785b7ad7f9f6f5fe9d1';

    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
      select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5fe9d1',id,null,now() from maoding_web_permission
      where id in (11,102,54,100,101,103);

    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5fe9d1',11,id,now() from maoding_web_company;
    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5fe9d1',102,id,now() from maoding_web_company;
    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5fe9d1',54,id,now() from maoding_web_company;
    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5fe9d1',100,id,now() from maoding_web_company;
    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5fe9d1',101,id,now() from maoding_web_company;
    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5fe9d1',103,id,now() from maoding_web_company;

    -- 系统管理员
    REPLACE INTO `maoding_web_role` VALUES ('2f84f20610314637a8d5113440c69bde', null, 'SystemManager', '系统管理员', '0', '2', '2015-12-01 16:14:03', null, null, null);
    REPLACE INTO `maoding_web_permission` VALUES ('8', 'sys_role_permission', '权限分配', '1', '1', '2000', '0', null, null, null, null, '企业中，分配每个人所拥有的权限');
    REPLACE INTO `maoding_web_permission` VALUES ('58', 'sys_role_auth', '企业认证', '1', '1', '5000', '0', null, null, null, null, '企业认证的权限');
    REPLACE INTO `maoding_web_permission` VALUES ('55', 'data_import', '历史数据导入', '5', '5', '6000', '0', null, null, null, null, '企业中历史数据打入权限');

    delete from maoding_web_role_permission where role_id = '2f84f20610314637a8d5113440c69bde';

    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
      select replace(uuid(),'-',''),'2f84f20610314637a8d5113440c69bde',id,null,now() from maoding_web_permission
      where id in (8,58,55);

    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'2f84f20610314637a8d5113440c69bde',8,id,now() from maoding_web_company;
    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'2f84f20610314637a8d5113440c69bde',58,id,now() from maoding_web_company;
    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'2f84f20610314637a8d5113440c69bde',55,id,now() from maoding_web_company;

    -- 组织管理
    REPLACE INTO `maoding_web_role` VALUES ('23297de920f34785b7ad7f9f6f5fe9d0', null, 'GeneralManager', '组织管理', '0', '3', '2015-12-01 16:14:04', null, '2015-12-18 13:29:45', null);
    REPLACE INTO `maoding_web_permission` VALUES ('12', 'com_enterprise_edit', '组织信息管理', '2', '2', '2200', '0', null, null, null, null, '企业信息编辑 修改 如企业名称 企业简介等');
    REPLACE INTO `maoding_web_permission` VALUES ('14', 'hr_org_set,hr_employee', '组织架构设置', '2', '2', '2300', '0', null, null, null, null, '企业中相关人员，人员的添加 修改 删除');

    delete from maoding_web_role_permission where role_id = '23297de920f34785b7ad7f9f6f5fe9d0';

    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
      select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5fe9d0',id,null,now() from maoding_web_permission
      where id in (12,14);

    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5fe9d0',12,id,now() from maoding_web_company;
    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5fe9d0',14,id,now() from maoding_web_company;

    -- 行政管理
    REPLACE INTO `maoding_web_role` VALUES ('0726c6aba7fa40918bb6e795bbe51059', null, 'AdminManager', '行政管理', '0', '4', '2015-12-10 15:59:24', null, '2015-12-18 13:29:55', null);
    REPLACE INTO `maoding_web_permission` VALUES ('110', 'summary_leave', '请假/出差汇总', '2', '2', '2500', '0', null, null, null, null, '企业中相关人员，请假/出差审批完成后的汇总记录');
    REPLACE INTO `maoding_web_permission` VALUES ('19', 'admin_notice', '通知公告发布', '4', '4', '2600', '0', null, null, null, null, '企业中相关公告信息 通知等');

    delete from maoding_web_role_permission where role_id = '0726c6aba7fa40918bb6e795bbe51059';

    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
      select replace(uuid(),'-',''),'0726c6aba7fa40918bb6e795bbe51059',id,null,now() from maoding_web_permission
      where id in (110,19);

    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'0726c6aba7fa40918bb6e795bbe51059',110,id,now() from maoding_web_company;
    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'0726c6aba7fa40918bb6e795bbe51059',19,id,now() from maoding_web_company;


    -- 项目管理
    REPLACE INTO `maoding_web_role` VALUES ('23297de920f34785b7ad7f9f6f5f1112', null, 'OperateManager', '项目管理', '0', '5', '2016-07-25 19:18:26', null, '2016-07-25 19:18:31', null);
    REPLACE INTO `maoding_web_permission` VALUES ('51', 'project_manager', '任务签发', '5', '5', '2650', '0', null, null, null, null, '企业中从事经营活动的相关人员进行任务的经营签发活动<br>注:系统默认新项目的经营负责人为排在任务签发第一位的人员');
    REPLACE INTO `maoding_web_permission` VALUES ('52', 'design_manager', '生产安排', '5', '5', '2660', '0', null, null, null, null, '企业中的设计负责人可对经营负责人发布过来的任务进行具体安排<br>注:系统默认新项目的设计负责人为排在生产安排第一位的人员');
    REPLACE INTO `maoding_web_permission` VALUES ('56', 'project_overview', '项目总览', '5', '5', '2670', '0', null, null, null, null, '企业中所有项目信息的查看权限');
    REPLACE INTO `maoding_web_permission` VALUES ('57', 'project_archive', '查看项目文档', '5', '5', '2680', '0', null, null, null, null, '企业中所有项目文档（设计依据/归档文件/交付文件)的查看下载');
    REPLACE INTO `maoding_web_permission` VALUES ('20', 'project_eidt,project_edit', '项目基本信息编辑', '5', '5', '2700', '0', null, null, null, null, '企业中所有项目中基本信息的编辑录入');

    delete from maoding_web_role_permission where role_id = '23297de920f34785b7ad7f9f6f5f1112';

    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
      select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5f1112',id,null,now() from maoding_web_permission
      where id in (51,52,56,57,20);

    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5f1112',51,id,now() from maoding_web_company;
    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5f1112',52,id,now() from maoding_web_company;
    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5f1112',56,id,now() from maoding_web_company;
    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5f1112',57,id,now() from maoding_web_company;
    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'23297de920f34785b7ad7f9f6f5f1112',20,id,now() from maoding_web_company;

    -- 财务管理
    REPLACE INTO `maoding_web_role` VALUES ('0fb8c188097a4d01a0ff6bb9cacb308e', null, 'FinancialManager', '财务管理', '0', '6', '2015-12-01 16:15:46', null, '2015-12-18 13:29:43', null);
    REPLACE INTO `maoding_web_permission` VALUES ('46', 'report_exp_static', '查看/费用汇总', '2', '2', '5800', '0', null, null, null, null, '查看企业所有人员的报销/费用汇总情况');
    REPLACE INTO `maoding_web_permission` VALUES ('10', 'sys_finance_type', '财务费用类别设置', '6', '6', '5810', '0', null, null, null, null, '设置企业报销/费用可申请或报销的类型进行设置');
    REPLACE INTO `maoding_web_permission` VALUES ('49', 'project_charge_manage', '项目收支管理', '6', '6', '5820', '0', null, null, null, null, '项目费用（合同回款/技术审查费/合作设计费/其他收支)的到账/付款确认，报销/费用的拨款处理');

    delete from maoding_web_role_permission where role_id = '0fb8c188097a4d01a0ff6bb9cacb308e';

    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
      select replace(uuid(),'-',''),'0fb8c188097a4d01a0ff6bb9cacb308e',id,null,now() from maoding_web_permission
      where id in (46,10,49);

    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'0fb8c188097a4d01a0ff6bb9cacb308e',46,id,now() from maoding_web_company;
    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'0fb8c188097a4d01a0ff6bb9cacb308e',10,id,now() from maoding_web_company;
    insert into maoding_web_role_permission (id,role_id,permission_id,company_id,create_date)
        select replace(uuid(),'-',''),'0fb8c188097a4d01a0ff6bb9cacb308e',49,id,now() from maoding_web_company;

    -- 添加默认权限
    -- 企业负责人
    insert into maoding_web_user_permission (id,company_id,user_id,permission_id,create_date,seq)
        select replace(uuid(),'-',''),company.id,role.user_id,102,now(),1
				from maoding_web_company company
						inner join maoding_web_user_permission role on (company.id = role.company_id and role.permission_id = 11)
  					left join maoding_web_user_permission role_have on (role_have.permission_id = 102 and company.id = role_have.company_id)
				where role_have.permission_id is null;
    insert into maoding_web_user_permission (id,company_id,user_id,permission_id,create_date,seq)
        select replace(uuid(),'-',''),company.id,role.user_id,54,now(),1
				from maoding_web_company company
						inner join maoding_web_user_permission role on (company.id = role.company_id and role.permission_id = 11)
  					left join maoding_web_user_permission role_have on (role_have.permission_id = 54 and company.id = role_have.company_id)
				where role_have.permission_id is null;
    insert into maoding_web_user_permission (id,company_id,user_id,permission_id,create_date,seq)
        select replace(uuid(),'-',''),company.id,role.user_id,100,now(),1
				from maoding_web_company company
						inner join maoding_web_user_permission role on (company.id = role.company_id and role.permission_id = 11)
  					left join maoding_web_user_permission role_have on (role_have.permission_id = 100 and company.id = role_have.company_id)
				where role_have.permission_id is null;
    insert into maoding_web_user_permission (id,company_id,user_id,permission_id,create_date,seq)
        select replace(uuid(),'-',''),company.id,role.user_id,101,now(),1
				from maoding_web_company company
						inner join maoding_web_user_permission role on (company.id = role.company_id and role.permission_id = 11)
  					left join maoding_web_user_permission role_have on (role_have.permission_id = 101 and company.id = role_have.company_id)
				where role_have.permission_id is null;
    insert into maoding_web_user_permission (id,company_id,user_id,permission_id,create_date,seq)
        select replace(uuid(),'-',''),company.id,role.user_id,103,now(),1
				from maoding_web_company company
						inner join maoding_web_user_permission role on (company.id = role.company_id and role.permission_id = 11)
  					left join maoding_web_user_permission role_have on (role_have.permission_id = 103 and company.id = role_have.company_id)
				where role_have.permission_id is null;

		-- 系统管理员
    insert into maoding_web_user_permission (id,company_id,user_id,permission_id,create_date,seq)
        select replace(uuid(),'-',''),company.id,role.user_id,8,now(),1
				from maoding_web_company company
						inner join maoding_web_user_permission role on (company.id = role.company_id and role.permission_id = 11)
  					left join maoding_web_user_permission role_have on (role_have.permission_id = 8 and company.id = role_have.company_id)
				where role_have.permission_id is null;
    insert into maoding_web_user_permission (id,company_id,user_id,permission_id,create_date,seq)
        select replace(uuid(),'-',''),company.id,role.user_id,58,now(),1
				from maoding_web_company company
						inner join maoding_web_user_permission role on (company.id = role.company_id and role.permission_id = 8)
  					left join maoding_web_user_permission role_have on (role_have.permission_id = 58 and company.id = role_have.company_id)
				where role_have.permission_id is null;
    insert into maoding_web_user_permission (id,company_id,user_id,permission_id,create_date,seq)
        select replace(uuid(),'-',''),company.id,role.user_id,55,now(),1
				from maoding_web_company company
						inner join maoding_web_user_permission role on (company.id = role.company_id and role.permission_id = 8)
  					left join maoding_web_user_permission role_have on (role_have.permission_id = 55 and company.id = role_have.company_id)
				where role_have.permission_id is null;

		-- 项目管理
    insert into maoding_web_user_permission (id,company_id,user_id,permission_id,create_date,seq)
        select replace(uuid(),'-',''),company.id,role.user_id,56,now(),1
				from maoding_web_company company
						inner join maoding_web_user_permission role on (company.id = role.company_id and role.permission_id = 11)
  					left join maoding_web_user_permission role_have on (role_have.permission_id = 56 and company.id = role_have.company_id)
				where role_have.permission_id is null;
    insert into maoding_web_user_permission (id,company_id,user_id,permission_id,create_date,seq)
        select replace(uuid(),'-',''),company.id,role.user_id,57,now(),1
				from maoding_web_company company
						inner join maoding_web_user_permission role on (company.id = role.company_id and role.permission_id = 11)
  					left join maoding_web_user_permission role_have on (role_have.permission_id = 57 and company.id = role_have.company_id)
				where role_have.permission_id is null;

    -- 行政管理
    insert into maoding_web_user_permission (id,company_id,user_id,permission_id,create_date,seq)
      select replace(uuid(),'-',''),company.id,role.user_id,110,now(),1
      from maoding_web_company company
        inner join maoding_web_user_permission role on (company.id = role.company_id and role.permission_id = 11)
        left join maoding_web_user_permission role_have on (role_have.permission_id = 110 and company.id = role_have.company_id)
      where role_have.permission_id is null;
    insert into maoding_web_user_permission (id,company_id,user_id,permission_id,create_date,seq)
      select replace(uuid(),'-',''),company.id,role.user_id,19,now(),1
      from maoding_web_company company
        inner join maoding_web_user_permission role on (company.id = role.company_id and role.permission_id = 11)
        left join maoding_web_user_permission role_have on (role_have.permission_id = 19 and company.id = role_have.company_id)
      where role_have.permission_id is null;
END;
-- call updateData();
DROP PROCEDURE IF EXISTS `updateData`;
