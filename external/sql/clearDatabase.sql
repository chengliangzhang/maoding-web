-- 清理无效存储过程
DROP PROCEDURE IF EXISTS `updateFieldForQA`;
DROP PROCEDURE IF EXISTS `clearDatabase`;
DROP PROCEDURE IF EXISTS `clearTables`;
DROP PROCEDURE IF EXISTS `createTables`;
DROP PROCEDURE IF EXISTS `fillStorageInfo`;
DROP PROCEDURE IF EXISTS `restoreData`;
DROP PROCEDURE IF EXISTS `backupData`;
DROP PROCEDURE IF EXISTS `clearStorageData`;
DROP PROCEDURE IF EXISTS `copyFileRealPath`;
DROP PROCEDURE IF EXISTS `copyStorageData`;
DROP PROCEDURE IF EXISTS `updatePermissionTables`;
DROP PROCEDURE IF EXISTS `clearTables`;
DROP FUNCTION IF EXISTS `getIssueId`;
DROP FUNCTION IF EXISTS `getPath`;
DROP FUNCTION IF EXISTS `getTaskPath`;
DROP FUNCTION IF EXISTS `updateTables`;

--  清理无效表
DROP TABLE IF EXISTS `maoding_const`;
DROP TABLE IF EXISTS `md_list_const`;
DROP TABLE IF EXISTS `maoding_custom_const`;
DROP TABLE IF EXISTS `maoding_storage`;
DROP TABLE IF EXISTS `maoding_storage_dir`;
DROP TABLE IF EXISTS `maoding_storage_file`;
DROP TABLE IF EXISTS `maoding_storage_file_his`;
DROP TABLE IF EXISTS `maoding_storage_file_ref`;
DROP TABLE IF EXISTS `sys_maoding_web_duty`;
DROP TABLE IF EXISTS `sys_maoding_web_org`;
DROP TABLE IF EXISTS `sys_maoding_web_org_relation`;
DROP TABLE IF EXISTS `sys_maoding_web_user`;
DROP TABLE IF EXISTS `sys_maoding_web_user_admin`;

-- 清理无效的im表
DROP TABLE IF EXISTS `maoding_web_im_error_user`;
DROP TABLE IF EXISTS `maoding_web_im_chat_history`;
DROP TABLE IF EXISTS `maoding_web_im_group`;
DROP TABLE IF EXISTS `maoding_web_im_group_error`;
DROP TABLE IF EXISTS `maoding_web_im_group_error_user`;
DROP TABLE IF EXISTS `maoding_web_im_group_user`;
DROP TABLE IF EXISTS `maoding_web_im_user`;


-- 清理无效视图
DROP VIEW IF EXISTS `maoding_storage_file_tmp`;
DROP VIEW IF EXISTS `maoding_storage_file_info`;
DROP VIEW IF EXISTS `maoding_storage_node_root_copy`;
DROP VIEW IF EXISTS `maoding_storage_node_copy`;
DROP VIEW IF EXISTS `maoding_storage_root_copy`;
DROP VIEW IF EXISTS `maoding_storage_copy`;
DROP VIEW IF EXISTS `maoding_storage_old_node_copy`;
DROP VIEW IF EXISTS `maoding_range`;
DROP VIEW IF EXISTS `maoding_permission`;
DROP VIEW IF EXISTS `maoding_role_permission`;
DROP VIEW IF EXISTS `maoding_role_permission_all`;
DROP VIEW IF EXISTS `maoding_task`;
DROP VIEW IF EXISTS `md_list_role_and_sub_role`;
DROP VIEW IF EXISTS `md_list_role_permission`;
DROP VIEW IF EXISTS `maoding_task`;
DROP VIEW IF EXISTS `maoding_issue`;
DROP VIEW IF EXISTS `maoding_role`;
DROP VIEW IF EXISTS `maoding_storage_all`;
DROP VIEW IF EXISTS `maoding_storage_node`;
DROP VIEW IF EXISTS `maoding_storage_node_commit`;
DROP VIEW IF EXISTS `maoding_storage_node_commit_task`;
DROP VIEW IF EXISTS `maoding_storage_node_design`;
DROP VIEW IF EXISTS `maoding_storage_node_design_task`;
DROP VIEW IF EXISTS `maoding_storage_old_node`;
DROP VIEW IF EXISTS `maoding_storage_root`;
DROP VIEW IF EXISTS `maoding_task`;
DROP VIEW IF EXISTS `maoding_task_member`;
DROP VIEW IF EXISTS `maoding_file`;
DROP VIEW IF EXISTS `maoding_role_manage_role`;
DROP VIEW IF EXISTS `maoding_storage`;
DROP VIEW IF EXISTS `maoding_storage_project`;
DROP VIEW IF EXISTS `maoding_storage_range`;
DROP VIEW IF EXISTS `maoding_storage_storage`;
DROP VIEW IF EXISTS `maoding_storage_task`;
DROP VIEW IF EXISTS `maoding_task_node`;
DROP VIEW IF EXISTS `md_web_role`;

-- -- 清理无用字段
-- md_tree_storage
DROP PROCEDURE IF EXISTS `clearFields`;
CREATE PROCEDURE `clearFields`()
BEGIN
  -- md_tree_storage
  if exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage' and column_name='node_length') then
    alter table md_tree_storage drop column `node_length`;
  end if;

END;
call clearFields();
DROP PROCEDURE IF EXISTS `clearFields`;

-- -- 清理无效数据
-- 协同文件
DROP PROCEDURE IF EXISTS `clearData`;
CREATE PROCEDURE `clearData`()
BEGIN
  delete from md_list_storage_file_his;
  delete from md_list_storage_file;
  delete from md_tree_storage;
END;
call clearData();
DROP PROCEDURE IF EXISTS `clearData`;

-- 已删除的SkyDrive文件
DROP PROCEDURE IF EXISTS `clearData`;
CREATE PROCEDURE `clearData`()
  BEGIN
    delete from maoding_web_project_sky_drive where pid is null and status != '0';
  END;
call clearData();
DROP PROCEDURE IF EXISTS `clearData`;


