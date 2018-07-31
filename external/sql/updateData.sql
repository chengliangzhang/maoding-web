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
call updateData();
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
call updateData();
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

-- fillStorageInfo -- 从老的sky_drive表中复制数据到storage中
-- DROP PROCEDURE IF EXISTS `fillStorageInfo`;
-- CREATE PROCEDURE `fillStorageInfo`()
-- BEGIN
--     -- 清除项目一级目录
-- #     delete from maoding_storage_tree where (type_id = 20 or type_id = 30);
-- # 		update
-- # 				maoding_storage_tree src
-- # 				inner join
-- # 				(select *
-- # 				 from (
-- # 							select node.id, node.path,node.type_id,node.pid
-- # 									,task.issue_id, task.id as task_id, task.project_id, task.company_id
-- # 									,if(position(task.path in node.path) > 0,substring(node.path, position(task.path in node.path) + char_length(task.path) + 1),node.path) as new_path
-- # 							from maoding_storage_tree node
-- # 									left join maoding_storage_tree parent1 on (node.pid=parent1.id)
-- # 									left join maoding_storage_tree parent2 on (parent1.pid=parent2.id)
-- # 									left join maoding_storage_tree parent3 on (parent2.pid=parent3.id)
-- # 									left join maoding_storage_tree parent4 on (parent3.pid=parent4.id)
-- # 									left join maoding_storage_tree parent5 on (parent4.pid=parent5.id)
-- # 									left join maoding_storage_tree parent6 on (parent5.pid=parent6.id)
-- # 									left join maoding_storage_tree parent7 on (parent6.pid=parent7.id)
-- # 									left join maoding_storage_tree parent8 on (parent7.pid=parent8.id)
-- # 									left join maoding_storage_tree parent9 on (parent8.pid=parent9.id)
-- # 									inner join maoding_task task on (concat(ifnull(parent9.pid,''),
-- # 																													ifnull(parent8.pid,''),
-- # 																													ifnull(parent7.pid,''),
-- # 																													ifnull(parent6.pid,''),
-- # 																													ifnull(parent5.pid,''),
-- # 																													ifnull(parent4.pid,''),
-- # 																													ifnull(parent3.pid,''),
-- # 																													ifnull(parent2.pid,''),
-- # 																													ifnull(parent1.pid,''),
-- # 																													ifnull(node.pid,''))
-- # 																									 like concat('%',task.id,'%'))
-- # 							order by length(task.path) desc
-- # 							) tmp
-- # 				 group by id
-- # 				) dst on (dst.id = src.id)
-- # 		set src.task_id=ifnull(dst.task_id,dst.issue_id),src.path=dst.new_path,
-- # 			src.type_id=if(src.type_id > 10 and dst.new_path like '建筑%',33,if(src.type_id=18,23,src.type_id));
-- END;

-- createDefaultUser -- 创建默认用户
-- DROP PROCEDURE IF EXISTS `createDefaultUser`;
-- CREATE PROCEDURE `createDefaultUser`()
-- BEGIN
-- 	if not exists (select 1 from maoding_web_account where cellphone='13680809727') then
-- 		REPLACE INTO maoding_web_account (id,user_name,password,cellphone,status,create_date,active_time)
-- 		VALUES ('5ffee496fa814ea4b6d26a9208b00a0b','sun','E10ADC3949BA59ABBE56E057F20F883E','13680809727','0','2017-07-04 11:59:37','2017-07-03 18:57:36');
-- 		REPLACE INTO maoding_web_account (id,user_name,password,cellphone,status,create_date,active_time)
-- 		VALUES ('07649b3d23094f28bfce78930bf4d4ac','卢沂','E10ADC3949BA59ABBE56E057F20F883E','13926516981','0','2018-02-02 11:41:57','2018-02-09 13:17:08');
-- 	end if;
-- END;

-- 复制文件实际存储路径
-- DROP PROCEDURE IF EXISTS `copyFileRealPath`;
-- CREATE PROCEDURE `copyFileRealPath`()
-- BEGIN
-- 	if not exists (select 1 from maoding_storage_file where read_only_scope is not null) then
-- 		if exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage_file' and column_name='read_file_scope') then
-- 			update maoding_storage_file
-- 			set	read_only_scope = read_file_scope,
-- 				read_only_key = read_file_key,
-- 				writable_scope = write_file_scope,
-- 				writable_key = write_file_key;
-- 		end if;
-- 	end if;
-- 	if not exists (select 1 from maoding_storage_file where last_modify_role_id is not null) then
-- 		if exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage_file' and column_name='last_modify_duty_id') then
-- 			update maoding_storage_file
-- 			set	last_modify_role_id = last_modify_duty_id;
-- 		end if;
-- 	end if;
-- END;

-- 复制storage数据到storage_tree
-- DROP PROCEDURE IF EXISTS `copyStorageData`;
-- CREATE PROCEDURE `copyStorageData`()
-- 	BEGIN
-- 				if not exists (select 1 from maoding_storage_tree where id is not null) then
-- 					if exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_storage') then
-- 						insert into maoding_storage_tree (id,deleted,create_time,last_modify_time,last_modify_user_id,last_modify_role_id,pid,path,type_id,node_name,file_length,task_id)
-- 							select id,deleted,create_time,last_modify_time,last_modify_user_id,last_modify_duty_id,pid,path,type_id,node_name,file_length,task_id from maoding_storage;
-- 				  end if;
-- 				end if;
-- 	END;
--
-- call createDefaultUser();
-- call copyFileRealPath();
-- call copyStorageData();

-- 更改node_type定义
-- DROP PROCEDURE IF EXISTS `changeNodeType`;
-- CREATE PROCEDURE `changeNodeType`()
-- 	BEGIN
-- 				update md_tree_storage set type_id = 121 where type_id = 1;
-- 				update md_tree_storage set type_id = 320 where type_id = 33 and path not like '%历史版本%';
-- 				update md_tree_storage set type_id = 330 where type_id = 33 and path like '%历史版本%';
-- 				update md_tree_storage set type_id = 321 where type_id = 2 and path not like '%历史版本%';
-- 				update md_tree_storage set type_id = 331 where type_id = 2 and path like '%历史版本%';
-- 				update md_tree_storage set type_id = 220 where type_id = 23 and path not like '%历史版本%';
-- 				update md_tree_storage set type_id = 230 where type_id = 23 and path like '%历史版本%';
-- 				update md_tree_storage set type_id = 220 where type_id = 71 and path not like '%历史版本%';
-- 				update md_tree_storage set type_id = 230 where type_id = 71 and path like '%历史版本%';
-- 				update md_tree_storage set type_id = 221 where type_id = 7 and path not like '%历史版本%';
-- 				update md_tree_storage set type_id = 231 where type_id = 7 and path like '%历史版本%';
-- 	END;
-- call changeNodeType();

-- 同步专业id
-- DROP PROCEDURE IF EXISTS `syncMajor`;
-- CREATE PROCEDURE `syncMajor`()
-- BEGIN
--   DROP TABLE IF EXISTS `net_maoding_data_dictionary`;
--
--   CREATE TABLE `net_maoding_data_dictionary` (
--     `id` varchar(32) NOT NULL COMMENT 'ID',
--     `name` varchar(128) DEFAULT NULL COMMENT '名称',
--     `code` varchar(32) DEFAULT NULL COMMENT '编码',
--     `pid` varchar(32) DEFAULT NULL COMMENT '父ID',
--     `root_id` varchar(32) DEFAULT NULL COMMENT '根结点id',
--     `deep_index` int(32) DEFAULT NULL COMMENT '层级深度',
--     `vl` varchar(32) DEFAULT NULL COMMENT '值',
--     `seq` int(11) DEFAULT NULL COMMENT '排序',
--     `create_date` datetime DEFAULT NULL,
--     `create_by` varchar(50) DEFAULT NULL,
--     `update_date` datetime DEFAULT NULL,
--     `update_by` varchar(50) DEFAULT NULL,
--     `deleted` int(1) DEFAULT '0' COMMENT '删除标识：0=有效，1=无效',
--     PRIMARY KEY (`id`),
--     KEY `pid` (`pid`),
--     KEY `root_id` (`root_id`)
--   ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据字典表';
--
--   -- ----------------------------
--   -- Records of net_maoding_data_dictionary
--   -- ----------------------------
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('0062df57d64311e782f8f8db88fcba36', '主营业务税金及附加', 'gdfy_salestax', null, '0062df57d64311e782f8f8db88fcba36', '0', 'gdfy_salestax', '1', null, null, '2017-12-01 11:39:34', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('006f7f61d64311e782f8f8db88fcba36', '直接人工成本', 'gdfy_directcosts', null, '006f7f61d64311e782f8f8db88fcba36', '0', 'gdfy_directcosts', '2', null, null, '2017-12-01 11:39:34', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('00762ca9d64311e782f8f8db88fcba36', '管理人员工资', 'gdfy_executivesalary', null, '00762ca9d64311e782f8f8db88fcba36', '0', 'gdfy_executivesalary', '3', null, null, '2017-12-01 11:39:34', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('007d88c6d64311e782f8f8db88fcba36', '房屋物业费用', 'gdfy_fwsalary', null, '007d88c6d64311e782f8f8db88fcba36', '0', 'gdfy_fwsalary', '4', null, null, '2017-12-01 11:39:35', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('0084d8f3d64311e782f8f8db88fcba36', '资产摊销', 'gdfy_assetsamortization', null, '0084d8f3d64311e782f8f8db88fcba36', '0', 'gdfy_assetsamortization', '5', null, null, '2017-12-01 11:39:35', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('008c2bbdd64311e782f8f8db88fcba36', '资产减值准备', 'gdfy_zcjzzb', null, '008c2bbdd64311e782f8f8db88fcba36', '0', 'gdfy_zcjzzb', '6', null, null, '2017-12-01 11:39:35', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('009269f5d64311e782f8f8db88fcba36', '财务费用', 'gdfy_cwfy', null, '009269f5d64311e782f8f8db88fcba36', '0', 'gdfy_cwfy', '7', null, null, '2017-12-01 11:39:35', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('00988309d64311e782f8f8db88fcba36', '所得税费用', 'gdfy_sdsfy', null, '00988309d64311e782f8f8db88fcba36', '0', 'gdfy_sdsfy', '8', null, null, '2017-12-01 11:39:35', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('00abf99af1a04d16b72172e7f05825d4', '其他费用', 'gdzc_qtfy', null, '00abf99af1a04d16b72172e7f05825d4', '0', null, '22', null, null, '2015-12-10 10:31:27', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('028c0186fe434504b20ce07ac4099284', '学位', 'doctorate', '', '028c0186fe434504b20ce07ac4099284', '0', '', '1', '2016-06-16 12:04:29', '', '2016-06-15 14:27:41', '', '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('054d44031c0b451a936a715b09cdacae', '一级注册建筑师', null, '44fced63a9fe45ff8b884a829ce0f465', '44fced63a9fe45ff8b884a829ce0f465', '1', '1stclassregisteredarchitect', '1', '2015-12-14 09:57:41', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('067f463ed36a4bdc8ba6334604ca773a', '员工状态', 'yg-zt', null, '067f463ed36a4bdc8ba6334604ca773a', '0', null, '1', '2015-12-14 17:46:45', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('0757dde1b64c481d88ebc359ba572ced', '办公设备', 'bgsb', '96b311a6d1b846929c74ad58b256b658', '96b311a6d1b846929c74ad58b256b658', '1', 'bx-zcgz-bgsb', '1', '2015-12-05 13:12:05', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('087582b513c443a49f163630e09fbc88', '物管费用', 'wyfy', '47cc8ad886ba4f2585661fd83ac6325e', '47cc8ad886ba4f2585661fd83ac6325e', '1', 'gdzc-wyfy-wgfy', '3', '2015-12-05 13:22:10', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('0cc3d4c392a040a7b44123b87a226e1e', '高中', '', '26e817a7ebbe48cb9e59f61ccf61649a', '26e817a7ebbe48cb9e59f61ccf61649a', '1', 'dg-gz', '2', '2016-06-16 12:00:03', '', '2016-06-17 14:18:26', '', '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('0dfa51b4d9574746a5c099d09d0dc291', 'XZ_LZ_DMS', null, '40d3664df1b24bdcb7a7c19f2b465947', '40d3664df1b24bdcb7a7c19f2b465947', '1', '?从公司离职。', '1', null, null, '2016-03-03 14:56:19', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('110aae4ca1da486bac547c433459d475', '强电', null, '2685353ae3c942be82325c57c3e9c94a', '2685353ae3c942be82325c57c3e9c94a', '1', 'qd', '6', '2015-12-15 17:59:00', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('1201c7f1cbe843968e768b75f5d757b9', '增值税', 'zzs', '1d9d1549ce51450b8f5a3160353a16e6', '1d9d1549ce51450b8f5a3160353a16e6', '1', 'gdzc-swfy-zzs', '2', null, null, '2015-12-05 13:25:34', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('12a8cfd9202549a08d342c1c6edef8cf', '景观设计', '', 'a172ddf45101492987dda58e2a557dd7', 'a172ddf45101492987dda58e2a557dd7', '1', 'sight', '3', '2016-06-02 15:23:43', '', '2016-06-16 14:18:48', '', '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('1437685e4feb4267aa4a6abb5525cd27', '施工图审查', null, '8f555ab3ea6c4f73b5e38513eaadfad0', '8f555ab3ea6c4f73b5e38513eaadfad0', '1', '4', '403', '2016-06-22 11:07:31', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('1490880ddb9b457785b859422e18732c', '所得税', 'sds', '1d9d1549ce51450b8f5a3160353a16e6', '1d9d1549ce51450b8f5a3160353a16e6', '1', 'gdzc-swfy-sds', '1', '2015-12-05 13:25:02', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('15bb913bd76a425ea41e29650254ef8c', '中介费用', 'zjfy', '300a460121874c06bec1db56ba771425', '300a460121874c06bec1db56ba771425', '1', 'bx-xzfy-zjfy', '3', '2015-12-05 12:35:32', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('16324ebbdaea41a68929b87a28b41204', '人防', null, '2685353ae3c942be82325c57c3e9c94a', '2685353ae3c942be82325c57c3e9c94a', '1', 'rf', '11', '2015-12-15 17:59:40', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('196c0bfd64d945728814d0b2705f3aae', '工程勘查报告', null, '90ff4888133f4513aacdc9f841562924', '90ff4888133f4513aacdc9f841562924', '1', 'gckcbg', '13', '2015-12-15 18:17:51', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('19bb3f3e3299488f969cd91cdbab33ea', '注册公用设备（给水排水）工程师', null, '4ebc8562aa6d498cbfa7033077826d9a', '4ebc8562aa6d498cbfa7033077826d9a', '1', 'Registeredutilitiesengineer', '1', '2015-12-14 10:16:54', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('19d6f2f9c2fc407eba39f4c3eb7eba71', '中级', 'intermediate', null, '19d6f2f9c2fc407eba39f4c3eb7eba71', '0', null, '2', null, null, '2015-12-14 10:24:31', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('1c6f48757e684b3cb059b94021e12baa', '建筑设计', '', 'a172ddf45101492987dda58e2a557dd7', 'a172ddf45101492987dda58e2a557dd7', '1', 'buid', '2', '2016-06-02 15:22:39', '', '2016-06-20 14:27:47', '', '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('1cc94ffa34f34b2184e87aa4847a6173', 'A', null, '53a05167102e466ebdefcd53d09d15e6', '53a05167102e466ebdefcd53d09d15e6', '1', '1', '1', '2016-06-13 15:01:31', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('1d765dba6f094438b224f50fcc938348', 'BIM', null, '2685353ae3c942be82325c57c3e9c94a', '2685353ae3c942be82325c57c3e9c94a', '1', 'bim', '20', null, null, '2015-12-15 18:08:24', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('1d9d1549ce51450b8f5a3160353a16e6', '税务费用', 'gdzc_swfy', null, '1d9d1549ce51450b8f5a3160353a16e6', '0', null, '21', null, null, '2015-12-10 10:00:41', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('1dbda060f92f48c8a01790cf8153e402', '其他固定支出', 'qtgdzc', '00abf99af1a04d16b72172e7f05825d4', '00abf99af1a04d16b72172e7f05825d4', '1', 'gdzc-qtfy-qtzc', '1', '2015-12-05 13:27:30', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('1e230f078b8842ce93a07d09fd5a115d', '全职', null, 'b5d6d4a557674d168439269399b61e40', 'b5d6d4a557674d168439269399b61e40', '1', 'qz', '1', '2015-12-14 17:45:55', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('1e4d51f2a85441bfbbe17aea85a36f36', '电气专业', 'dq-zy', null, '1e4d51f2a85441bfbbe17aea85a36f36', '0', null, '5', null, null, '2015-12-14 11:14:40', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('1f5eeca26e9b4e8687f51c7977fff799', '概算编制', null, '8f555ab3ea6c4f73b5e38513eaadfad0', '8f555ab3ea6c4f73b5e38513eaadfad0', '1', '3', '304', '2016-06-22 11:07:01', null, '2016-06-22 11:05:41', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('1f8af893940d44d5b1d58959046c3bd0', '已认证', null, '2fc94f122e2149ce8e4c193ab57c1073', '2fc94f122e2149ce8e4c193ab57c1073', '1', 'Y', null, null, null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('2345aa6fd83b4e2083b6191bc7293a03', '交通建筑', null, '5895ea885293417190a7560e52756f83', '5895ea885293417190a7560e52756f83', '1', 'zp-jzgn-jtjz', '5', '2015-12-23 15:22:59', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('236ece4a227a4d459c80612f19d6b517', '一级注册结构工程师', null, '7a383fcfe8cf491883ea7e1a170ca347', '7a383fcfe8cf491883ea7e1a170ca347', '1', '1stclassregisteredstructural', '1', '2015-12-14 10:02:35', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('24bfb21ed3184f389dae701dae0a081d', '会务招待', 'hwzd', '5a7953f5aae046b08c63f6bbc99c1c00', '5a7953f5aae046b08c63f6bbc99c1c00', '1', 'bx-jyfy-hwzd', '2', '2015-12-05 13:10:49', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('262863d574764ae1b905fccfc819c8b9', '市政条件(水暖，道路，热力，通讯等)', null, '90ff4888133f4513aacdc9f841562924', '90ff4888133f4513aacdc9f841562924', '1', 'sztj', '14', '2015-12-15 18:19:44', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('2685353ae3c942be82325c57c3e9c94a', '设计范围', 'designRange', null, '2685353ae3c942be82325c57c3e9c94a', '0', null, '1', null, null, '2015-12-05 11:43:37', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('26e817a7ebbe48cb9e59f61ccf61649a', '学历', 'degrees', '', '26e817a7ebbe48cb9e59f61ccf61649a', '0', '', '1', '2016-06-16 11:57:25', '', '2016-06-16 14:27:58', '', '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('284d3216b84d4a95802f85cf7eaa82e2', '办公建筑', null, '5895ea885293417190a7560e52756f83', '5895ea885293417190a7560e52756f83', '1', 'zp-jzgn-bgjz', '2', '2015-12-23 15:20:28', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('29b0dc81accb4320adf0c7d662539e82', '消息类别', 'ms_type', null, '29b0dc81accb4320adf0c7d662539e82', '0', null, '24', null, null, '2015-12-15 11:17:17', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('2c0b7e6fe951449b9b66ceafc96f9a05', '性别', 'sex', null, '2c0b7e6fe951449b9b66ceafc96f9a05', '0', null, '1', '2015-12-14 18:05:03', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('2f83a04d731546e1923c64d8f9dee9fd', '人防设计', null, '8f555ab3ea6c4f73b5e38513eaadfad0', '8f555ab3ea6c4f73b5e38513eaadfad0', '1', '3', '302', '2016-06-22 11:07:01', null, '2016-06-22 11:05:24', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('2fc94f122e2149ce8e4c193ab57c1073', '认证', 'RZ87456', null, '2fc94f122e2149ce8e4c193ab57c1073', '0', null, '5', null, null, '2015-12-05 12:06:33', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('300a460121874c06bec1db56ba771425', '行政费用', 'bx_xzfy', null, '300a460121874c06bec1db56ba771425', '0', null, '12', '2015-12-05 12:29:45', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('305c2b4940d34df8bdd247d247131999', '手续费用', 'sxfy', '71921791759b480aac406bc07358fa31', '71921791759b480aac406bc07358fa31', '1', 'gdzc-cwfy-sxfy', '3', '2015-12-10 10:03:50', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('30c34bb52d594b89b46ecf60460b6efa', '房租费用', 'fzfy', '47cc8ad886ba4f2585661fd83ac6325e', '47cc8ad886ba4f2585661fd83ac6325e', '1', 'gdzc-wyfy-fzfy', '1', '2015-12-05 13:21:26', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('31220e35ba4c4597a91633d2238427d9', '公司', null, '472fbe13221640f39befebd394a7b45d', null, '1', 'C', null, null, null, '2015-11-11 09:33:12', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('317a1ddc36404587bed419f32eee10d1', '预算', null, '2685353ae3c942be82325c57c3e9c94a', '2685353ae3c942be82325c57c3e9c94a', '1', 'ys', '14', null, null, '2015-12-15 18:08:19', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('326085055bbc4dbda8db174cb45b08df', '商用', null, 'e5a76f7e11004f35bb2b97ebbe4df238', 'e5a76f7e11004f35bb2b97ebbe4df238', '1', '2', '2', null, null, '2015-12-05 11:48:58', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('3270306ab66a4cc6b79f191d3b122a73', '公用设备专业', null, '8a46f8d2459545a2b2c482848ebd12d8', '8a46f8d2459545a2b2c482848ebd12d8', '1', 'gg-sb-zy', '4', '2015-12-14 11:11:51', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('34e696d065dc4626b60de8afa45600bf', '为受邀请的消息', null, '29b0dc81accb4320adf0c7d662539e82', '29b0dc81accb4320adf0c7d662539e82', '1', '2', '2', '2015-12-15 11:13:42', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('3514f20d82164bf8a6a77b6bae9d5130', '员工工资', 'yggz', 'c7d09162a04343bba1faf9dc5cb239de', 'c7d09162a04343bba1faf9dc5cb239de', '1', 'gdzc-ygxc-yggz', '1', '2015-12-05 13:19:22', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('39577a5b08064659a3f556f95d26d7c3', '规划乙级', null, '8f5b496538f94c089f5b597cba9f7cc4', '8f5b496538f94c089f5b597cba9f7cc4', '1', '2', '2', null, null, '2016-03-01 12:19:58', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('3aa9c7dc7bb84077b772bddc06b80aff', '差旅费用', 'clfy', '5b2daf74a90b46449edcea08aef03983', '5b2daf74a90b46449edcea08aef03983', '1', 'bx-ywfy-clfy', '4', '2015-12-05 13:08:49', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('3b4057825eff4c3c8676e5451770162d', '博士', '', '028c0186fe434504b20ce07ac4099284', '028c0186fe434504b20ce07ac4099284', '1', 'dt-bs', '2', '2016-06-16 12:05:27', '', '2016-06-16 14:28:04', '', '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('3bb06e3d49b946e1a9a76bc7f80c4d73', '未签订', null, 'db8cb029a945499998f402ee91f4b3ad', null, '1', 'N', null, '2015-11-05 09:21:58', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('3c2dddc460f347db925e45752520fd1a', '商业建筑', null, '5895ea885293417190a7560e52756f83', '5895ea885293417190a7560e52756f83', '1', 'zp-jzgn-syjz', '8', '2015-12-23 15:24:52', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('3cc62bca94f849f898a52934073db92e', '1/500电子版地形图', null, '90ff4888133f4513aacdc9f841562924', '90ff4888133f4513aacdc9f841562924', '1', 'dxt', '6', '2015-12-15 18:12:22', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('3eddacc4b9034692bf51a39270282a97', '参观考察', 'cgkc', '5a7953f5aae046b08c63f6bbc99c1c00', '5a7953f5aae046b08c63f6bbc99c1c00', '1', 'bx-jyfy-cgkc', '3', '2015-12-05 13:11:18', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('3f76a024d64711e782f8f8db88fcba36', '主营业务税金及附加', 'gdfy_salestax_tax', '0062df57d64311e782f8f8db88fcba36', '0062df57d64311e782f8f8db88fcba36', '0', 'gdfy_ salestax_tax', '1', null, null, '2017-12-01 11:39:34', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('3f81c3a5d64711e782f8f8db88fcba36', '投入在项目上的员工工资', 'gdfy_directcosts_salay', '006f7f61d64311e782f8f8db88fcba36', '006f7f61d64311e782f8f8db88fcba36', '0', 'gdfy_directcosts_salay', '1', null, null, '2017-12-01 11:39:34', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('3f8e0544d64711e782f8f8db88fcba36', '投入在项目上的五险一金', 'gdfy_directcosts_fund', '006f7f61d64311e782f8f8db88fcba36', '006f7f61d64311e782f8f8db88fcba36', '0', 'gdfy_directcosts_fund', '2', null, null, '2017-12-01 11:39:34', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('3f96a87ad64711e782f8f8db88fcba36', '投入在项目上的员工奖金', 'gdfy_directcosts_bonus', '006f7f61d64311e782f8f8db88fcba36', '006f7f61d64311e782f8f8db88fcba36', '0', 'gdfy_directcosts_bonus', '3', null, null, '2017-12-01 11:39:34', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('3fa21beed64711e782f8f8db88fcba36', '管理行政人员的工资', 'gdfy_executivesalary_salay', '00762ca9d64311e782f8f8db88fcba36', '00762ca9d64311e782f8f8db88fcba36', '0', 'gdfy_executivesalary_salay', '1', null, null, '2017-12-01 11:39:34', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('3faaac1ed64711e782f8f8db88fcba36', '管理行政人员的五险一金', 'gdfy_executivesalary_fund', '00762ca9d64311e782f8f8db88fcba36', '00762ca9d64311e782f8f8db88fcba36', '0', 'gdfy_executivesalary_fund', '2', null, null, '2017-12-01 11:39:34', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('3fb368dbd64711e782f8f8db88fcba36', '管理行政人员的奖金', 'gdfy_executivesalary_bonus', '00762ca9d64311e782f8f8db88fcba36', '00762ca9d64311e782f8f8db88fcba36', '0', 'gdfy_executivesalary_bonus', '3', null, null, '2017-12-01 11:39:35', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('3fc05596d64711e782f8f8db88fcba36', '办公场地房租', 'gdfy_fwsalary_bgcd', '007d88c6d64311e782f8f8db88fcba36', '007d88c6d64311e782f8f8db88fcba36', '0', 'gdfy_fwsalary_bgcd', '1', null, null, '2017-12-01 11:39:35', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('3fc93492d64711e782f8f8db88fcba36', '物管费用', 'gdfy_fwsalary_wg', '007d88c6d64311e782f8f8db88fcba36', '007d88c6d64311e782f8f8db88fcba36', '0', 'gdfy_fwsalary_wg', '2', null, null, '2017-12-01 11:39:35', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('3fd21a30d64711e782f8f8db88fcba36', '水电费用', 'gdfy_fwsalary_sd', '007d88c6d64311e782f8f8db88fcba36', '007d88c6d64311e782f8f8db88fcba36', '0', 'gdfy_fwsalary_sd', '3', null, null, '2017-12-01 11:39:35', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('3fdab032d64711e782f8f8db88fcba36', '网络、电话', 'gdfy_fwsalary_net', '007d88c6d64311e782f8f8db88fcba36', '007d88c6d64311e782f8f8db88fcba36', '0', 'gdfy_fwsalary_net', '4', null, null, '2017-12-01 11:39:35', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('3fe3c155d64711e782f8f8db88fcba36', '办公场地维护费用', 'gdfy_fwsalary_gbwh', '007d88c6d64311e782f8f8db88fcba36', '007d88c6d64311e782f8f8db88fcba36', '0', 'gdfy_fwsalary_gbwh', '5', null, null, '2017-12-01 11:39:35', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('3ff01890d64711e782f8f8db88fcba36', '办公室装修摊销', 'gdfy_assetsamortization_bgzx', '0084d8f3d64311e782f8f8db88fcba36', '0084d8f3d64311e782f8f8db88fcba36', '0', 'gdfy_assetsamortization_bgzx', '1', null, null, '2017-12-01 11:39:35', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('3ff97b36d64711e782f8f8db88fcba36', '办公室设备摊销', 'gdfy_assetsamortization_bgsb', '0084d8f3d64311e782f8f8db88fcba36', '0084d8f3d64311e782f8f8db88fcba36', '0', 'gdfy_assetsamortization_sb', '2', null, null, '2017-12-01 11:39:35', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('3ffb635fda3c4d8ca7abe94844e568ad', '建筑乙级', null, '8f5b496538f94c089f5b597cba9f7cc4', '8f5b496538f94c089f5b597cba9f7cc4', '1', '4', '4', '2016-03-01 12:20:35', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('400301a8d64711e782f8f8db88fcba36', '软件摊销', 'gdfy_assetsamortization_rjtx', '0084d8f3d64311e782f8f8db88fcba36', '0084d8f3d64311e782f8f8db88fcba36', '0', 'gdfy_assetsamortization_rjtx', '3', null, null, '2017-12-01 11:39:35', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('400c3630d64711e782f8f8db88fcba36', '不动产折旧', 'gdfy_assetsamortization_bdczj', '0084d8f3d64311e782f8f8db88fcba36', '0084d8f3d64311e782f8f8db88fcba36', '0', 'gdfy_assetsamortization_bdczj', '4', null, null, '2017-12-01 11:39:35', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('4018f374d64711e782f8f8db88fcba36', '应收账款坏账准备', 'gdfy_zcjzzb_hzzb', '008c2bbdd64311e782f8f8db88fcba36', '008c2bbdd64311e782f8f8db88fcba36', '0', 'gdfy_zcjzzb_hzzb', '1', null, null, '2017-12-01 11:39:35', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('40255e8cd64711e782f8f8db88fcba36', '银行手续费', 'gdfy_cwfy_sxf', '009269f5d64311e782f8f8db88fcba36', '009269f5d64311e782f8f8db88fcba36', '0', 'gdfy_cwfy_sxf', '1', null, null, '2017-12-01 11:39:35', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('402de895d64711e782f8f8db88fcba36', '银行利息收入／支出', 'gdfy_cwfy_lx', '009269f5d64311e782f8f8db88fcba36', '009269f5d64311e782f8f8db88fcba36', '0', 'gdfy_cwfy_lx', '2', null, null, '2017-12-01 11:39:35', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('403a2405d64711e782f8f8db88fcba36', '减：所得税费用', 'gdfy_sdsfy_sds', '00988309d64311e782f8f8db88fcba36', '00988309d64311e782f8f8db88fcba36', '0', 'gdfy_sdsfy_sds', '1', null, null, '2017-12-01 11:39:35', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('406bd646ea7f435cadc056a11267eab8', '注册规划师', null, 'c5063e43d5e340bc9f809de1d78c84a9', 'c5063e43d5e340bc9f809de1d78c84a9', '1', 'Theregisteredplanner', '1', null, null, '2015-12-14 10:00:56', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('40d3664df1b24bdcb7a7c19f2b465947', '动态模板', 'dms', null, '40d3664df1b24bdcb7a7c19f2b465947', '0', null, '0', '2016-03-03 14:42:56', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('418adfdf93dd4a49a988ed164ce44f9e', '学士', '', '028c0186fe434504b20ce07ac4099284', '028c0186fe434504b20ce07ac4099284', '1', 'dt-xs', '0', '2016-06-16 12:04:52', '', '2016-06-16 14:28:10', '', '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('437057970471433eab8ad9632ac7211e', '工作室', null, '472fbe13221640f39befebd394a7b45d', null, '1', 'S', null, null, null, '2015-11-11 09:33:19', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('44fced63a9fe45ff8b884a829ce0f465', '建筑专业', 'jz-zy', null, '44fced63a9fe45ff8b884a829ce0f465', '0', null, '1', null, null, '2016-01-06 11:24:09', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('462b9e40964c4aa08cc21b48179ab978', '建筑专业', null, '8a46f8d2459545a2b2c482848ebd12d8', '8a46f8d2459545a2b2c482848ebd12d8', '1', 'jz-zy', '2', '2015-12-14 11:10:53', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('470f278ef4e7482aa4e92f39881b1e20', '市政设计', '', 'a172ddf45101492987dda58e2a557dd7', 'a172ddf45101492987dda58e2a557dd7', '1', 'sight', '5', '2016-06-02 15:23:43', '', '2016-06-16 14:18:48', '', '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('472fbe13221640f39befebd394a7b45d', '用户类型', 'US854865', null, null, '0', null, '2', null, null, '2015-12-05 11:45:14', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('473bb8b3577d440899e580d93440b22b', '注册公用设备（暖通空调）工程师', null, '4ebc8562aa6d498cbfa7033077826d9a', '4ebc8562aa6d498cbfa7033077826d9a', '1', 'Registeredequipmentengineer', '2', '2015-12-14 10:18:16', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('47cc8ad886ba4f2585661fd83ac6325e', '物业费用', 'gdzc_wyfy', null, '47cc8ad886ba4f2585661fd83ac6325e', '0', null, '19', '2015-12-05 13:20:46', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('492eb51ba8da47f79d669d668b2ddbd9', '大专', '', '26e817a7ebbe48cb9e59f61ccf61649a', '26e817a7ebbe48cb9e59f61ccf61649a', '1', 'dg-dz', '4', '2016-06-16 12:01:11', '', '2016-06-15 14:28:16', '', '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('498f9f1b520647519d140f990ec08849', '电气专业', null, '8a46f8d2459545a2b2c482848ebd12d8', '8a46f8d2459545a2b2c482848ebd12d8', '1', 'dq-zy', '5', '2015-12-14 11:12:05', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('4a33f0e869cf4eb5af0a7c6232330633', '宽带费用', 'kdfy', '47cc8ad886ba4f2585661fd83ac6325e', '47cc8ad886ba4f2585661fd83ac6325e', '1', 'gdzc-wyfy-kdfy', '4', null, null, '2015-12-05 13:23:21', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('4bbfe5a9eb6d41e783e2d52932ff3978', '规划设计', '', 'a172ddf45101492987dda58e2a557dd7', 'a172ddf45101492987dda58e2a557dd7', '1', 'planning', '1', '2016-06-02 15:25:06', '', '2016-06-14 14:28:20', '', '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('4ebc8562aa6d498cbfa7033077826d9a', '公用设备专业', 'gg-sb-zy', null, '4ebc8562aa6d498cbfa7033077826d9a', '0', null, '4', null, null, '2015-12-14 11:13:48', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('4f4295fd343447bebb320ec0f87e82cb', '资料归档', null, '8f555ab3ea6c4f73b5e38513eaadfad0', '8f555ab3ea6c4f73b5e38513eaadfad0', '1', '5', '505', '2016-06-22 11:09:18', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('4f6accd967334a54a75ffeb9feba9dae', '总体规划', null, '8f555ab3ea6c4f73b5e38513eaadfad0', '8f555ab3ea6c4f73b5e38513eaadfad0', '1', '2', '201', '2016-06-22 11:07:01', null, '2016-06-22 11:06:02', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('4fb565d74771450a9346715cdc534796', '资质', 'ZZ65482', null, '4fb565d74771450a9346715cdc534796', '0', null, '7', null, null, '2015-12-05 12:06:45', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('50d6b749bfae4b60b9cbc397f5a350f5', '用户', null, '472fbe13221640f39befebd394a7b45d', null, '1', 'U', null, null, null, '2015-11-11 09:33:25', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('5217c99644464ab5a5dee3040141615f', '施工配合', null, 'fa3ec391c5294da295822e3ba8a3b61b', 'fa3ec391c5294da295822e3ba8a3b61b', '1', '5', '5', null, null, '2015-12-05 11:48:17', null, '1');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('53185f8281bd45d19e6aecb8fa935c94', '初步设计', null, 'fa3ec391c5294da295822e3ba8a3b61b', 'fa3ec391c5294da295822e3ba8a3b61b', '1', '3', '3', null, null, '2015-12-05 11:48:05', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('5383c87abccf4339a3f25aae8c026e69', '居住建筑', null, '5895ea885293417190a7560e52756f83', '5895ea885293417190a7560e52756f83', '1', 'zp-jzgn-jzjz', '1', '2015-12-23 15:19:51', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('53a05167102e466ebdefcd53d09d15e6', '任务管理', 'taskManager', null, '53a05167102e466ebdefcd53d09d15e6', '0', null, '100', '2016-06-13 15:00:33', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('541e85d5f9454d8ca8f7be50187cb9f3', '立项批文', null, '90ff4888133f4513aacdc9f841562924', '90ff4888133f4513aacdc9f841562924', '1', 'lxpw', '1', null, null, '2015-12-15 18:10:05', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('54943e7100e24ce58ab46a8d71eee6dc', '专项设计', null, '8f555ab3ea6c4f73b5e38513eaadfad0', '8f555ab3ea6c4f73b5e38513eaadfad0', '1', '4', '402', '2016-06-22 11:07:16', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('569df4275f0e11e7af038038bc0f053d', '专业', 'zy', null, '569df4275f0e11e7af038038bc0f053d', '0', null, '101', null, null, '2017-07-02 18:08:04', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('56a6a7aa5f0e11e7af038038bc0f053d', '规划', 'zy-gh', '569df4275f0e11e7af038038bc0f053d', '569df4275f0e11e7af038038bc0f053d', '0', null, '1', null, null, '2017-07-02 18:08:04', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('56b27d765f0e11e7af038038bc0f053d', '建筑', 'zy-jz', '569df4275f0e11e7af038038bc0f053d', '569df4275f0e11e7af038038bc0f053d', '0', null, '2', null, null, '2017-07-02 18:08:04', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('56bc42045f0e11e7af038038bc0f053d', '室内', 'zy-sn', '569df4275f0e11e7af038038bc0f053d', '569df4275f0e11e7af038038bc0f053d', '0', null, '3', null, null, '2017-07-02 18:08:04', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('56c605d85f0e11e7af038038bc0f053d', '景观', 'zy-jg', '569df4275f0e11e7af038038bc0f053d', '569df4275f0e11e7af038038bc0f053d', '0', null, '4', null, null, '2017-07-02 18:08:04', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('56cb9ec0c21744fa9bda8b04ed76747c', 'XZ_RZ_DMS', null, '40d3664df1b24bdcb7a7c19f2b465947', '40d3664df1b24bdcb7a7c19f2b465947', '1', '?申请加入?。', '0', null, null, '2016-03-03 14:56:25', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('56d040d35f0e11e7af038038bc0f053d', '结构', 'zy-gg', '569df4275f0e11e7af038038bc0f053d', '569df4275f0e11e7af038038bc0f053d', '0', null, '5', null, null, '2017-07-02 18:08:04', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('56dca88e5f0e11e7af038038bc0f053d', '给排水', 'zy-gps', '569df4275f0e11e7af038038bc0f053d', '569df4275f0e11e7af038038bc0f053d', '0', null, '6', null, null, '2017-07-02 18:08:05', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('56e9dc595f0e11e7af038038bc0f053d', '暖通', 'zy-nt', '569df4275f0e11e7af038038bc0f053d', '569df4275f0e11e7af038038bc0f053d', '0', null, '7', null, null, '2017-07-02 18:08:05', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('56f688135f0e11e7af038038bc0f053d', '电气', 'zy-dq', '569df4275f0e11e7af038038bc0f053d', '569df4275f0e11e7af038038bc0f053d', '0', null, '8', null, null, '2017-07-02 18:08:05', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('56f6db669013486b8b9be7718bb35e23', '其他费用', 'bx_qtfy', null, '56f6db669013486b8b9be7718bb35e23', '0', null, '16', '2015-12-05 12:32:14', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('570342785f0e11e7af038038bc0f053d', '其他', 'zy-qt', '569df4275f0e11e7af038038bc0f053d', '569df4275f0e11e7af038038bc0f053d', '0', null, '9', null, null, '2017-07-02 18:08:05', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('579b3440b4324bedb9875bb5da761662', '劳务补贴', 'lwbt', '5b2daf74a90b46449edcea08aef03983', '5b2daf74a90b46449edcea08aef03983', '1', 'bx-ywfy-lwbt', '5', '2015-12-05 13:09:26', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('57cce68f7a7c49b5ae3a0f471c344020', '报审及归档', null, '8f555ab3ea6c4f73b5e38513eaadfad0', '8f555ab3ea6c4f73b5e38513eaadfad0', '1', '2', '205', '2016-06-22 11:07:01', null, '2016-06-22 11:06:31', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('5895ea885293417190a7560e52756f83', '建筑功能', 'zp-jzgn', null, '5895ea885293417190a7560e52756f83', '0', null, '33', '2015-12-23 15:18:27', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('59378797dc5c492ab8554ca37580c68a', '水电费用', 'sdfy', '47cc8ad886ba4f2585661fd83ac6325e', '47cc8ad886ba4f2585661fd83ac6325e', '1', 'gdzc-wyfy-sdfy', '2', '2015-12-05 13:21:48', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('5a7953f5aae046b08c63f6bbc99c1c00', '经营费用', 'bx_jyfy', null, '5a7953f5aae046b08c63f6bbc99c1c00', '0', null, '14', '2015-12-05 12:31:14', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('5b1156fba8f2455ea32be9e10dddac3b', '中专', '', '26e817a7ebbe48cb9e59f61ccf61649a', '26e817a7ebbe48cb9e59f61ccf61649a', '1', 'dg-zz', '3', '2016-06-16 12:00:32', '', '2016-06-15 14:28:25', '', '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('5b2daf74a90b46449edcea08aef03983', '业务费用', 'bx_ywfy', null, '5b2daf74a90b46449edcea08aef03983', '0', null, '13', '2015-12-05 12:30:23', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('5ce162ac6f774bfa8fa51ea51f00aaea', '建筑设计', null, 'cd9789bb82ca4af283291e9e34e2b364', 'cd9789bb82ca4af283291e9e34e2b364', '1', 'server-type-build', '1', null, null, '2016-04-26 15:30:29', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('5da4d8fe652e48d3b0e0cff4c59a17f4', '女', null, '2c0b7e6fe951449b9b66ceafc96f9a05', '2c0b7e6fe951449b9b66ceafc96f9a05', '1', 'F', '2', null, null, '2015-12-14 18:41:58', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('5dfd1e21617244afa1dd94959a28cdbb', '泛光照明', null, '2685353ae3c942be82325c57c3e9c94a', '2685353ae3c942be82325c57c3e9c94a', '1', 'fgzm', '17', '2015-12-15 18:04:06', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('5eae15e1703d4387b167c7710b5fa6b9', '其他建筑', null, '5895ea885293417190a7560e52756f83', '5895ea885293417190a7560e52756f83', '1', 'zp-jzgn-qtjz', '12', '2015-12-23 15:27:02', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('5f17f9c4661e42428b5b282c361b2a0b', '项目前期', null, 'fa3ec391c5294da295822e3ba8a3b61b', 'fa3ec391c5294da295822e3ba8a3b61b', '1', '1', '1', null, null, '2015-12-05 11:47:52', null, '1');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('6165c532d6084f439b4c1a19d4246d84', '帮助中心', null, '9c0939e5dca040d2a713ff8e4ba34a3e', '9c0939e5dca040d2a713ff8e4ba34a3e', '1', '2', '2', null, null, '2015-12-05 12:23:22', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('636e99fd9237479e9a4ba78f9fca237d', '模型费用', 'mxfy', '5b2daf74a90b46449edcea08aef03983', '5b2daf74a90b46449edcea08aef03983', '1', 'bx-ywfy-mxfy', '3', '2015-12-05 13:08:10', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('653802654a0445a7aac4e4d6f8c8e815', '景观设计', null, 'ce84085db558488facc391b107ec0ea0', 'ce84085db558488facc391b107ec0ea0', '1', '2', '2', '2015-12-21 16:10:02', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('65782547e048459da931bf8176b84df1', '主体封顶', null, '8f555ab3ea6c4f73b5e38513eaadfad0', '8f555ab3ea6c4f73b5e38513eaadfad0', '1', '5', '503', null, null, '2016-06-22 11:09:06', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('67fab3c682af4ac3b7800e8e67ecbf4f', '地下室完工', null, '8f555ab3ea6c4f73b5e38513eaadfad0', '8f555ab3ea6c4f73b5e38513eaadfad0', '1', '5', '502', '2016-06-22 11:08:26', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('67face1e61574e41a385b8064a80a1c7', '概念方案3', null, '8f555ab3ea6c4f73b5e38513eaadfad0', '8f555ab3ea6c4f73b5e38513eaadfad0', '1', '1', '103', '2016-06-22 11:07:01', null, '2016-06-22 11:12:45', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('68a08b9cb89a46b89c1807e8c420487a', '施工图设计', null, '8f555ab3ea6c4f73b5e38513eaadfad0', '8f555ab3ea6c4f73b5e38513eaadfad0', '1', '4', '401', '2016-06-22 11:07:01', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('69f4028c1e5c49899b61082b55343e8d', '高级', 'senior', null, '69f4028c1e5c49899b61082b55343e8d', '0', null, '3', null, null, '2015-12-14 10:24:37', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('6a1cdd82493a4359bcd334e54b8bb3e9', '初中', '', '26e817a7ebbe48cb9e59f61ccf61649a', '26e817a7ebbe48cb9e59f61ccf61649a', '1', 'dg-cz', '1', '2016-06-16 11:59:30', '', '2016-06-15 14:28:29', '', '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('6c43bafb74774d4daf6e880c661e0a0b', '未认证', null, '2fc94f122e2149ce8e4c193ab57c1073', '2fc94f122e2149ce8e4c193ab57c1073', '1', 'N', null, null, null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('6d6797cbc0be409ab183ee33aabeefc5', '展会建筑', null, '5895ea885293417190a7560e52756f83', '5895ea885293417190a7560e52756f83', '1', 'zp-jzgn-zhjz', '10', '2015-12-23 15:26:13', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('70aaa4647785483b8c16249ea93a2db2', '建筑红线图', null, '90ff4888133f4513aacdc9f841562924', '90ff4888133f4513aacdc9f841562924', '1', 'jzhxt', '5', '2015-12-15 18:11:36', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('71921791759b480aac406bc07358fa31', '财务费用', 'gdzc_cwfy', null, '71921791759b480aac406bc07358fa31', '0', null, '20', '2015-12-10 10:00:33', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('7343eb85b3594bdb8f081115f9a0ebe7', '设计费用', 'sjfy', '5b2daf74a90b46449edcea08aef03983', '5b2daf74a90b46449edcea08aef03983', '1', 'bx-ywfy-sjfy', '1', '2015-12-05 13:05:44', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('7352524943f84f75b0d069a97ba7d9a9', '气体灭火', null, '2685353ae3c942be82325c57c3e9c94a', '2685353ae3c942be82325c57c3e9c94a', '1', 'qtmh', '10', '2015-12-15 18:07:08', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('7405dc3858154427bc91bdcffccbc64c', '日常用品', 'ycyp', '300a460121874c06bec1db56ba771425', '300a460121874c06bec1db56ba771425', '1', 'bx-xzfy-ycyp', '1', '2015-12-05 12:33:55', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('7417bcbcba0347f083a39bdd10e59c8a', '中级', null, 'd383d21526b14d63af1915b2d7240aee', 'd383d21526b14d63af1915b2d7240aee', '1', 'intermediate', '2', '2015-12-14 11:17:22', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('766501c963c74d4b9475cb94899d4449', '景观', null, '2685353ae3c942be82325c57c3e9c94a', '2685353ae3c942be82325c57c3e9c94a', '1', 'jg', '16', '2015-12-15 18:02:04', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('770e9331b91c40118ee0016810fc2981', '报审及归档', null, '8f555ab3ea6c4f73b5e38513eaadfad0', '8f555ab3ea6c4f73b5e38513eaadfad0', '1', '4', '405', '2016-06-22 11:08:02', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('78e8711574274aa79873db41941017e2', '规划专业', null, '8a46f8d2459545a2b2c482848ebd12d8', '8a46f8d2459545a2b2c482848ebd12d8', '1', 'gh-zy', '1', '2015-12-14 11:09:47', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('793210b0cd8d48d3935c6bfb876f0521', '注册电气（供配电）工程师', null, '1e4d51f2a85441bfbbe17aea85a36f36', '1e4d51f2a85441bfbbe17aea85a36f36', '1', 'Registeredelectricalengineer', '2', '2015-12-14 10:19:49', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('7a383fcfe8cf491883ea7e1a170ca347', '结构专业', 'jg-zy', null, '7a383fcfe8cf491883ea7e1a170ca347', '0', null, '3', null, null, '2015-12-14 11:13:28', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('7a8a6887c3d74df2801fbe2e15201204', '消防', null, '2685353ae3c942be82325c57c3e9c94a', '2685353ae3c942be82325c57c3e9c94a', '1', 'xf', '9', '2015-12-15 17:59:57', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('7c97ecac9230459fa8a86a4b36c81bc8', '结构', null, '2685353ae3c942be82325c57c3e9c94a', '2685353ae3c942be82325c57c3e9c94a', '1', 'jg', '3', '2015-12-15 17:57:45', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('7cf6b2d924554f5ab714e83dc5ab6e2c', '学历', 'XL96853', null, null, '0', null, '6', null, null, '2015-12-05 12:06:39', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('7d9f1f9e59d54ff89d0488c3e7fdda8b', '室内设计', null, 'cd9789bb82ca4af283291e9e34e2b364', 'cd9789bb82ca4af283291e9e34e2b364', '1', 'server-type-shinei', '4', null, null, '2016-04-26 15:30:09', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('7e4f657ed3f44ce58dd584d92c55e9c1', '本科', '', '26e817a7ebbe48cb9e59f61ccf61649a', '26e817a7ebbe48cb9e59f61ccf61649a', '1', 'dg-bk', '5', '2016-06-16 12:01:53', '', '2016-06-15 14:28:34', '', '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('7f3f1abb34774e79b1c57b0ac96692ca', '离职', null, '067f463ed36a4bdc8ba6334604ca773a', '067f463ed36a4bdc8ba6334604ca773a', '1', 'lz', '2', '2015-12-14 17:47:19', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('81d661429ca24602a69f407328d8c90f', '博士后', '', '26e817a7ebbe48cb9e59f61ccf61649a', '26e817a7ebbe48cb9e59f61ccf61649a', '1', 'dg-bsh', '8', '2016-06-16 12:03:12', '', '2016-06-14 14:28:38', '', '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('82945c59b8964414bda5551efaf0c0f1', '二级注册建筑师', null, '44fced63a9fe45ff8b884a829ce0f465', '44fced63a9fe45ff8b884a829ce0f465', '1', 'Level2registeredarchitect', '2', '2015-12-14 09:58:19', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('84521068da7749218c2a3db5daf45f01', '办公费用', 'bgfy', '300a460121874c06bec1db56ba771425', '300a460121874c06bec1db56ba771425', '1', 'bx-xzfy-bgfy', '4', '2015-12-05 12:35:56', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('84d344643c064efd9ae21b44916f433d', '住宅', null, 'e5a76f7e11004f35bb2b97ebbe4df238', 'e5a76f7e11004f35bb2b97ebbe4df238', '1', '1', '1', null, null, '2015-12-05 11:49:02', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('8a46f8d2459545a2b2c482848ebd12d8', '技术资格专业', 'js-zg-zy', null, '8a46f8d2459545a2b2c482848ebd12d8', '0', null, '1', '2015-12-14 11:07:08', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('8a952bd66a2143dcb13840832d9c4acc', '方案设计', null, 'fa3ec391c5294da295822e3ba8a3b61b', 'fa3ec391c5294da295822e3ba8a3b61b', '1', '2', '2', null, null, '2015-12-05 11:48:02', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('8b275367c2994c71a27d407b0d8bc873', '设计任务书', null, '90ff4888133f4513aacdc9f841562924', '90ff4888133f4513aacdc9f841562924', '1', 'sjrws', '4', '2015-12-15 18:11:07', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('8c63a154e4c947499e8b61d3de1558cf', '初设确认函', null, '90ff4888133f4513aacdc9f841562924', '90ff4888133f4513aacdc9f841562924', '1', 'csqrh', '9', '2015-12-15 18:15:29', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('8d2cbc068fc94a8a9f2b55c9ee7ef029', '高级', null, 'd383d21526b14d63af1915b2d7240aee', 'd383d21526b14d63af1915b2d7240aee', '1', 'senior', '3', '2015-12-14 11:17:40', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('8d71cc7417594765bde3c351ea62c34d', '中标通知书', null, '90ff4888133f4513aacdc9f841562924', '90ff4888133f4513aacdc9f841562924', '1', 'zbtzs', '3', '2015-12-15 18:10:41', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('8dc671bf1db7457190033408192c87cc', '概念方案1', null, '8f555ab3ea6c4f73b5e38513eaadfad0', '8f555ab3ea6c4f73b5e38513eaadfad0', '1', '1', '101', '2016-06-22 11:07:01', null, '2016-06-22 11:12:27', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('8f555ab3ea6c4f73b5e38513eaadfad0', '服务内容', 'servercontent', null, '8f555ab3ea6c4f73b5e38513eaadfad0', '0', null, '12', '2016-06-22 09:46:40', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('8f5b496538f94c089f5b597cba9f7cc4', '设计资质', 'certificate', null, '8f5b496538f94c089f5b597cba9f7cc4', '0', null, '32', '2015-12-21 18:26:04', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('8f8c5057ee46468faabab8514b75ec3f', '利息支出', 'lxzc', '71921791759b480aac406bc07358fa31', '71921791759b480aac406bc07358fa31', '1', 'gdzc-cwfy-lxzc', '2', '2015-12-10 10:02:59', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('90098d9eb3204da694dea70bf25453f5', '工程建设规划许可证', null, '90ff4888133f4513aacdc9f841562924', '90ff4888133f4513aacdc9f841562924', '1', 'ghxkz', '12', '2015-12-15 18:17:13', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('90b63a2aa1814b1eaf3a6c0b7c371b19', '小学', '', '26e817a7ebbe48cb9e59f61ccf61649a', '26e817a7ebbe48cb9e59f61ccf61649a', '1', 'dg-xx', '0', '2016-06-16 11:59:07', '', '0000-00-00 00:00:00', '', '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('90ff4888133f4513aacdc9f841562924', '设计依据', 'designBasic', null, '90ff4888133f4513aacdc9f841562924', '0', null, '3', null, null, '2015-12-05 12:06:15', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('91753557528a42cdace01d790337af63', '车辆费用', 'clfy', '300a460121874c06bec1db56ba771425', '300a460121874c06bec1db56ba771425', '1', 'bx-xzfy-clfy', '5', '2015-12-05 12:36:36', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('923237e81ebb462fb1c1b9059f4f7fc0', '初步设计', null, '8f555ab3ea6c4f73b5e38513eaadfad0', '8f555ab3ea6c4f73b5e38513eaadfad0', '1', '3', '301', '2016-06-22 11:07:01', null, '2016-06-22 11:05:18', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('9509e94dff4741d68b58ebce2cb8ce22', '博士', '', '26e817a7ebbe48cb9e59f61ccf61649a', '26e817a7ebbe48cb9e59f61ccf61649a', '1', 'dg-bs', '7', '2016-06-16 12:02:36', '', '2016-06-14 14:28:43', '', '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('951738175b284237b434bf6e384ecb78', '初级', 'primary', null, '951738175b284237b434bf6e384ecb78', '0', null, '1', '2015-12-14 10:21:47', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('95bae89ef19a4195a1d751812eebbf6b', '提交及归档', null, '8f555ab3ea6c4f73b5e38513eaadfad0', '8f555ab3ea6c4f73b5e38513eaadfad0', '1', '1', '105', '2016-06-22 11:07:01', null, '2016-06-22 11:12:49', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('95c7d395c19748a3a8cce090e0a120f1', 'D', null, '53a05167102e466ebdefcd53d09d15e6', '53a05167102e466ebdefcd53d09d15e6', '1', '4', '4', '2016-06-13 15:02:46', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('96b311a6d1b846929c74ad58b256b658', '资产购置', 'bx_zcgz', null, '96b311a6d1b846929c74ad58b256b658', '0', null, '15', '2015-12-05 12:31:45', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('96f274f5cc774205b39d250561e79710', '概念方案2', null, '8f555ab3ea6c4f73b5e38513eaadfad0', '8f555ab3ea6c4f73b5e38513eaadfad0', '1', '1', '102', '2016-06-22 11:07:01', null, '2016-06-22 11:12:33', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('9a39e55456694631a18acc04acd31b88', '方案确认函', null, '90ff4888133f4513aacdc9f841562924', '90ff4888133f4513aacdc9f841562924', '1', 'faqrh', '7', '2015-12-15 18:12:59', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('9aabf7a31d5c49228b5ee34210f13847', 'B', null, '53a05167102e466ebdefcd53d09d15e6', '53a05167102e466ebdefcd53d09d15e6', '1', '2', '2', '2016-06-13 15:02:28', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('9ac279802a6f43cda4f22e393b858695', '给排水', null, '2685353ae3c942be82325c57c3e9c94a', '2685353ae3c942be82325c57c3e9c94a', '1', 'jps', '4', '2015-12-15 17:58:40', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('9c0939e5dca040d2a713ff8e4ba34a3e', '静态文本类型', 'JTWB2365', null, '9c0939e5dca040d2a713ff8e4ba34a3e', '0', null, '4', null, null, '2015-12-05 12:06:25', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('9c10c1354dba401b9d1812e893b8a3e8', '预算编制', null, '8f555ab3ea6c4f73b5e38513eaadfad0', '8f555ab3ea6c4f73b5e38513eaadfad0', '1', '4', '404', '2016-06-22 11:07:48', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('9cf50ec031974f4898238b4501d2441d', '深化方案', null, '8f555ab3ea6c4f73b5e38513eaadfad0', '8f555ab3ea6c4f73b5e38513eaadfad0', '1', '2', '203', '2016-06-22 11:07:01', null, '2016-06-22 11:06:18', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('9da0388a7edb45c8b30014c857b2aba0', '审批中途', null, 'cda20b3b8306485f8aba3e5cc4e75575', 'cda20b3b8306485f8aba3e5cc4e75575', '1', 'G', null, null, null, '2015-11-05 13:52:57', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('9f40997a4f3c4e5f92ea8653c840a0a7', '装修设计', '', 'a172ddf45101492987dda58e2a557dd7', 'a172ddf45101492987dda58e2a557dd7', '1', 'sight', '4', '2016-06-02 15:23:43', '', '2016-06-16 14:18:48', '', '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('a0023bfc56374b16bb442f28638c8c53', '室内设计', null, 'ce84085db558488facc391b107ec0ea0', 'ce84085db558488facc391b107ec0ea0', '1', '1', '1', null, null, '2015-12-21 16:09:53', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('a0fdbe1d1b074a8aa1790bb407f3e1a9', '其它', null, 'cd9789bb82ca4af283291e9e34e2b364', 'cd9789bb82ca4af283291e9e34e2b364', '1', 'server-type-other', '5', null, null, '2016-04-26 15:34:43', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('a172ddf45101492987dda58e2a557dd7', '项目类别', 'project-type', '', 'a172ddf45101492987dda58e2a557dd7', '0', '', '1', '2016-06-02 15:21:06', '', '2016-06-15 14:28:47', '', '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('a19105bbf1c74a6aa42d394180d91f51', '博士后', '', '028c0186fe434504b20ce07ac4099284', '028c0186fe434504b20ce07ac4099284', '1', 'dt-bsh', '3', '2016-06-16 12:05:46', '', '2016-06-14 14:28:50', '', '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('a1d06432c7454bfb9e67bae863f45ca6', '制图费用', 'ztfy', '5b2daf74a90b46449edcea08aef03983', '5b2daf74a90b46449edcea08aef03983', '1', 'bx-ywfy-ztfy', '2', '2015-12-05 13:07:43', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('a331067831864726b28531f521bb0ba2', '用户协议', null, '9c0939e5dca040d2a713ff8e4ba34a3e', '9c0939e5dca040d2a713ff8e4ba34a3e', '1', '1', '1', null, null, '2016-01-20 10:54:36', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('a3a655a2ad8349fa9b045ba23862c91e', '已婚', null, 'fa072191b5984b609e47838786fb921b', 'fa072191b5984b609e47838786fb921b', '1', 'yh', '2', null, null, '2016-01-20 10:44:42', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('a509097cd574431e9d576b8b9b668806', 'C', null, '53a05167102e466ebdefcd53d09d15e6', '53a05167102e466ebdefcd53d09d15e6', '1', '3', '3', '2016-06-13 15:02:36', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('a50bdfa3010d445f862ab94ab8c8a06f', '暖通空调', null, '2685353ae3c942be82325c57c3e9c94a', '2685353ae3c942be82325c57c3e9c94a', '1', 'ltkt', '7', '2015-12-15 18:00:27', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('a56af78708d54668a7b8cf3426e2a392', '规划设计', null, 'cd9789bb82ca4af283291e9e34e2b364', 'cd9789bb82ca4af283291e9e34e2b364', '1', 'server-type-plan', '3', null, null, '2016-04-26 15:30:39', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('a82ed1c297ed475c9e2aa8ba7eedd706', '规划要点', null, '90ff4888133f4513aacdc9f841562924', '90ff4888133f4513aacdc9f841562924', '1', 'ghyd', '2', null, null, '2015-12-15 18:09:30', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('a8f0f1d57bcd4471b433ebac84a30092', '竣工验收报告', null, '90ff4888133f4513aacdc9f841562924', '90ff4888133f4513aacdc9f841562924', '1', 'jgysbg', '15', '2015-12-15 18:20:29', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('adad4bc42ba444b3b85150ac66cbc5a4', '报建方案', null, '8f555ab3ea6c4f73b5e38513eaadfad0', '8f555ab3ea6c4f73b5e38513eaadfad0', '1', '2', '204', '2016-06-22 11:07:01', null, '2016-06-22 11:06:24', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('ae40cabd039c41d5a147a6a7b4116306', '结构专业', null, '8a46f8d2459545a2b2c482848ebd12d8', '8a46f8d2459545a2b2c482848ebd12d8', '1', 'jg-zy', '3', '2015-12-14 11:11:26', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('ae74d6969180426f8074fff429ce7c6d', '其他', null, '8a46f8d2459545a2b2c482848ebd12d8', '8a46f8d2459545a2b2c482848ebd12d8', '1', 'qt-zy', '6', '2016-02-25 20:12:26', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('aece7f56348f46d687204862d0cedfe5', '建筑甲级', null, '8f5b496538f94c089f5b597cba9f7cc4', '8f5b496538f94c089f5b597cba9f7cc4', '1', '3', '3', '2016-03-01 12:20:19', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('b0d02dd7fb3d4a3d969b9bb0660bc29d', '文化体育建筑', null, '5895ea885293417190a7560e52756f83', '5895ea885293417190a7560e52756f83', '1', 'zp-jzgn-whtyjz', '3', '2015-12-23 15:21:28', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('b5be090bf6554a268285602d608e0558', '审批驳回', null, 'cda20b3b8306485f8aba3e5cc4e75575', 'cda20b3b8306485f8aba3e5cc4e75575', '1', 'N', null, '2015-11-04 15:31:58', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('b5d6d4a557674d168439269399b61e40', '员工类别', 'yg-lb', null, 'b5d6d4a557674d168439269399b61e40', '0', null, '1', null, null, '2015-12-14 17:45:36', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('b91939726e8e485eb5d96f3d7b99f940', '其他税费', 'qtsf', '1d9d1549ce51450b8f5a3160353a16e6', '1d9d1549ce51450b8f5a3160353a16e6', '1', 'gdzc-swfy-qtsf', '3', null, null, '2015-12-10 10:23:15', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('ba912211ef8747bdbc62e96800faff20', '酒店建筑', null, '5895ea885293417190a7560e52756f83', '5895ea885293417190a7560e52756f83', '1', 'zp-jzgn-jdjz', '4', '2015-12-23 15:22:27', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('ba917dc2db5848b5839606e47cb1b013', '燃气', null, '2685353ae3c942be82325c57c3e9c94a', '2685353ae3c942be82325c57c3e9c94a', '1', 'rq', '8', '2015-12-15 18:02:59', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('bcfffb387b574201ab042750f3a16ead', '前端通知消息', null, '29b0dc81accb4320adf0c7d662539e82', '29b0dc81accb4320adf0c7d662539e82', '1', '0', '0', '2015-12-15 11:13:16', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('bd21c3d76ed84dcfbb798a08d95af074', '兼职', null, 'b5d6d4a557674d168439269399b61e40', 'b5d6d4a557674d168439269399b61e40', '1', 'jz', '2', '2015-12-14 17:46:14', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('bd788e38b5f746a4bab751fe9d2bf558', '幕墙', null, '2685353ae3c942be82325c57c3e9c94a', '2685353ae3c942be82325c57c3e9c94a', '1', 'mq', '15', '2015-12-15 18:01:47', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('bfcb849f88c64ec2ba5531ce61b3fda3', '结构超限设计', null, '2685353ae3c942be82325c57c3e9c94a', '2685353ae3c942be82325c57c3e9c94a', '1', 'jgcxsj', '12', '2015-12-15 18:01:17', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('c1f7038c61164e2090471874f8a434c8', '总图', null, '2685353ae3c942be82325c57c3e9c94a', '2685353ae3c942be82325c57c3e9c94a', '1', 'zt', '1', null, null, '2015-12-15 17:58:15', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('c5063e43d5e340bc9f809de1d78c84a9', '规划专业', 'gh-zy', null, 'c5063e43d5e340bc9f809de1d78c84a9', '0', null, '1', null, null, '2015-12-14 11:13:00', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('c5db32fd002a42b6a4a239e88223e139', '弱电', null, '2685353ae3c942be82325c57c3e9c94a', '2685353ae3c942be82325c57c3e9c94a', '1', 'rd', '5', '2015-12-15 17:59:18', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('c6145ca4676a4bc39633f14d4ef9890a', '返聘', null, 'b5d6d4a557674d168439269399b61e40', 'b5d6d4a557674d168439269399b61e40', '1', 'fp', '3', '2016-01-20 10:43:49', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('c7d09162a04343bba1faf9dc5cb239de', '员工薪酬', 'gdzc_ygxc', null, 'c7d09162a04343bba1faf9dc5cb239de', '0', null, '18', '2015-12-05 13:18:53', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('c8990f3273714986afa72d26f1c586eb', '初设批文', null, '90ff4888133f4513aacdc9f841562924', '90ff4888133f4513aacdc9f841562924', '1', 'cspw', '10', '2015-12-15 18:16:02', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('c906f5d30eae47508f0e849a8f10e133', '行业费用', 'hyfy', '300a460121874c06bec1db56ba771425', '300a460121874c06bec1db56ba771425', '1', 'bx-xzfy-hyfy', '2', '2015-12-05 12:35:02', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('c974e448bf9244a0b11cb690b9773f02', '智能化', null, '2685353ae3c942be82325c57c3e9c94a', '2685353ae3c942be82325c57c3e9c94a', '1', 'znh', '18', '2015-12-15 18:02:32', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('caf53482e13646e793c95fa549ca02b0', '园区建筑', null, '5895ea885293417190a7560e52756f83', '5895ea885293417190a7560e52756f83', '1', 'zp-jzgn-yqjz', '11', '2015-12-23 15:26:37', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('cd9789bb82ca4af283291e9e34e2b364', '服务类型', 'server-type', null, 'cd9789bb82ca4af283291e9e34e2b364', '0', null, '99', '2016-04-13 14:24:10', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('cda20b3b8306485f8aba3e5cc4e75575', '财务审批', 'SP00546', null, 'cda20b3b8306485f8aba3e5cc4e75575', '0', null, '9', null, null, '2015-12-05 12:07:16', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('ce558b531ebf4977ad216ddb23adbee8', '维护费用', 'whfy', '300a460121874c06bec1db56ba771425', '300a460121874c06bec1db56ba771425', '1', 'bx-xzfy-whfy', '6', '2015-12-05 13:02:25', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('ce84085db558488facc391b107ec0ea0', '公司专业类别', 'companyMajorType', null, 'ce84085db558488facc391b107ec0ea0', '0', null, '30', '2015-12-21 16:07:33', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('ced37bd9a9af4ed483b4b928a624ad82', '收入(其他收入)', 'gdzc_sr', null, 'ced37bd9a9af4ed483b4b928a624ad82', '0', null, '17', '2015-12-05 13:17:05', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('cff5d34a33be441d91394508ca460348', '硕士', '', '26e817a7ebbe48cb9e59f61ccf61649a', '26e817a7ebbe48cb9e59f61ccf61649a', '1', 'dg-ss', '6', '2016-06-16 12:02:21', '', '2016-06-15 14:28:56', '', '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('d31de826d3734d0ab0956afde7b8f1ea', '系统消息', null, '29b0dc81accb4320adf0c7d662539e82', '29b0dc81accb4320adf0c7d662539e82', '1', '1', '1', '2015-12-15 11:12:59', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('d383d21526b14d63af1915b2d7240aee', '职称等级', 'zc-dj', null, 'd383d21526b14d63af1915b2d7240aee', '0', null, '1', '2015-12-14 11:16:15', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('d598e23f216a44c6b7c34f16a39a91a4', '流程类型', 'process', null, 'd598e23f216a44c6b7c34f16a39a91a4', '0', null, '34', '2015-12-23 15:38:23', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('d5a7311064684362abbc7425c5e254cb', '专项设计', null, '8f555ab3ea6c4f73b5e38513eaadfad0', '8f555ab3ea6c4f73b5e38513eaadfad0', '1', '3', '303', '2016-06-22 11:07:01', null, '2016-06-22 11:05:31', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('d5d6065c23374816b6733adaf932ebda', '合作设计费审批流程', 'process_design_fee', 'd598e23f216a44c6b7c34f16a39a91a4', 'd598e23f216a44c6b7c34f16a39a91a4', '1', '1', '1', '2015-12-23 15:38:51', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('d6160d0b0aa34a2fa98e46d66e154238', 'C_XM_DMS', null, '40d3664df1b24bdcb7a7c19f2b465947', '40d3664df1b24bdcb7a7c19f2b465947', '1', '公司新立项”?“项目', '3', null, null, '2016-03-03 14:56:11', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('d661fa3bbb1b4f4cbd6438279479c36d', '概算', null, '2685353ae3c942be82325c57c3e9c94a', '2685353ae3c942be82325c57c3e9c94a', '1', 'gs', '13', null, null, '2015-12-15 18:08:13', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('d80e2c9ff25a452cb003a9e36e4be74a', '其他报销', 'qtbx', '56f6db669013486b8b9be7718bb35e23', '56f6db669013486b8b9be7718bb35e23', '1', 'bx-qtfy-qtbx', '1', '2015-12-05 13:13:34', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('d828409ce31343f5ad7c07d4104c1ffe', '竣工验收', null, '8f555ab3ea6c4f73b5e38513eaadfad0', '8f555ab3ea6c4f73b5e38513eaadfad0', '1', '5', '504', '2016-06-22 11:09:00', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('d8e1108e318f4f01b24f7a9c8ee2b982', '概念方案4', null, '8f555ab3ea6c4f73b5e38513eaadfad0', '8f555ab3ea6c4f73b5e38513eaadfad0', '1', '1', '104', '2016-06-22 11:07:01', null, '2016-06-22 11:12:39', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('d9e9274c8b4d4202bace49f9d3a30c84', '审批通过', null, 'cda20b3b8306485f8aba3e5cc4e75575', 'cda20b3b8306485f8aba3e5cc4e75575', '1', 'Y', null, '2015-11-04 15:31:58', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('db8cb029a945499998f402ee91f4b3ad', '合同签订', 'HT00886', null, 'db8cb029a945499998f402ee91f4b3ad', '0', null, '10', null, null, '2015-12-05 12:07:25', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('df1aaabe209845a7946335fba8fb9435', '有资质', null, '4fb565d74771450a9346715cdc534796', '4fb565d74771450a9346715cdc534796', '1', 'Y', '0', null, null, '2016-03-01 12:19:38', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('df48a52790f0403f9d3aa70a25ba7e7b', '报审及归档', null, '8f555ab3ea6c4f73b5e38513eaadfad0', '8f555ab3ea6c4f73b5e38513eaadfad0', '1', '3', '305', '2016-06-22 11:07:01', null, '2016-06-22 11:05:46', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('dfe6d6ba155e40ec874fe85f7f325f09', '在职', null, '067f463ed36a4bdc8ba6334604ca773a', '067f463ed36a4bdc8ba6334604ca773a', '1', 'zz', '1', '2015-12-14 17:47:07', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('e0c94e02549f462a9a85c68351a79571', '软件图文', 'rjtw', '96b311a6d1b846929c74ad58b256b658', '96b311a6d1b846929c74ad58b256b658', '1', 'bx-zcgz-rjtw', '2', '2015-12-05 13:12:31', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('e25effca7eb24773b17796c2db11d9ce', '男', null, '2c0b7e6fe951449b9b66ceafc96f9a05', '2c0b7e6fe951449b9b66ceafc96f9a05', '1', 'M', '1', null, null, '2015-12-14 18:41:51', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('e2a81ca0e34649a2a38534857f563ccc', '已签订', null, 'db8cb029a945499998f402ee91f4b3ad', null, '1', 'Y', null, '2015-11-05 09:22:04', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('e461ae11ead14e6b93562e78e8d3f2fa', '施工图审查合格意见书', null, '90ff4888133f4513aacdc9f841562924', '90ff4888133f4513aacdc9f841562924', '1', 'sgtschg', '11', null, null, '2016-01-14 15:52:42', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('e49f81309bfb4dd7a2dd4e6b923bf4f2', '二级注册结构工程师', null, '7a383fcfe8cf491883ea7e1a170ca347', '7a383fcfe8cf491883ea7e1a170ca347', '1', 'Level2classstructura engineer', '2', '2015-12-14 10:04:47', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('e5a76f7e11004f35bb2b97ebbe4df238', '建筑类别', 'constructionCate', null, 'e5a76f7e11004f35bb2b97ebbe4df238', '0', null, '8', null, null, '2015-12-05 12:07:01', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('e63a792aef1b41b9a177c12607f7a080', '医疗卫生建筑', null, '5895ea885293417190a7560e52756f83', '5895ea885293417190a7560e52756f83', '1', 'zp-jzgn-ylwsjz', '7', '2015-12-23 15:24:25', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('e7654a41d1324de4ac8c1c2d61e71bfc', '教育建筑', null, '5895ea885293417190a7560e52756f83', '5895ea885293417190a7560e52756f83', '1', 'zp-jzgn-jyjz', '6', '2015-12-23 15:23:37', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('e96985ea8b474687bd9826327212fff2', '方案批文', null, '90ff4888133f4513aacdc9f841562924', '90ff4888133f4513aacdc9f841562924', '1', 'fapw', '8', '2015-12-15 18:15:00', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('ea4f8e2feaef42aeb9260e6bd9273c09', '无资质', null, '4fb565d74771450a9346715cdc534796', '4fb565d74771450a9346715cdc534796', '1', 'N', '0', null, null, '2016-03-01 12:19:47', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('ebd46f0cd9af4ea2a817fb1d8ec92307', '施工图设计', null, 'fa3ec391c5294da295822e3ba8a3b61b', 'fa3ec391c5294da295822e3ba8a3b61b', '1', '4', '4', null, null, '2015-12-05 11:48:11', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('ec0c11006cb04829afe5cee2eadf3f7d', '利息收入', 'lxsr', '71921791759b480aac406bc07358fa31', '71921791759b480aac406bc07358fa31', '1', 'gdzc-cwfy-lxsr', '1', null, null, '2015-12-10 10:03:13', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('ec93cc49e4d2438293102aee5ef1444d', '请假流程', 'process_leave', 'd598e23f216a44c6b7c34f16a39a91a4', 'd598e23f216a44c6b7c34f16a39a91a4', '1', '2', '2', '2015-12-23 15:39:32', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('ecfd2d429c9541e493628759768b5b50', '概念方案', null, '8f555ab3ea6c4f73b5e38513eaadfad0', '8f555ab3ea6c4f73b5e38513eaadfad0', '1', '2', '202', '2016-06-22 11:07:01', null, '2016-06-22 11:06:13', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('ee68e18d14f04bbe865c98201cb54bb2', '基础完工', null, '8f555ab3ea6c4f73b5e38513eaadfad0', '8f555ab3ea6c4f73b5e38513eaadfad0', '1', '5', '501', '2016-06-22 11:08:13', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('efeb5209c38c44f48cf3e3c900783c5a', '规划甲级', null, '8f5b496538f94c089f5b597cba9f7cc4', '8f5b496538f94c089f5b597cba9f7cc4', '1', '1', '1', null, null, '2016-03-01 12:19:30', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('eff2c693c326498bb2ebd5a639128093', '其他收入', 'qtsr', 'ced37bd9a9af4ed483b4b928a624ad82', 'ced37bd9a9af4ed483b4b928a624ad82', '1', 'gdzc-sr-qtsr', '1', '2015-12-05 13:17:36', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('f15d1316911e4187b0cbb8c1022a6a94', '景观设计', null, 'cd9789bb82ca4af283291e9e34e2b364', 'cd9789bb82ca4af283291e9e34e2b364', '1', 'server-type-landscape', '2', null, null, '2016-04-26 15:30:34', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('f1b845afecf447a3a12e3cef4c973339', '工业建筑', null, '5895ea885293417190a7560e52756f83', '5895ea885293417190a7560e52756f83', '1', 'zp-jzgn-gyjz', '9', '2015-12-23 15:25:42', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('f2b880d0de164106b215c916d10e7f04', '初级', null, 'd383d21526b14d63af1915b2d7240aee', 'd383d21526b14d63af1915b2d7240aee', '1', 'primary', '1', '2015-12-14 11:16:58', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('f36d4f9c3e1145df845f09dac150d138', '硕士', '', '028c0186fe434504b20ce07ac4099284', '028c0186fe434504b20ce07ac4099284', '1', 'dt-ss', '1', '2016-06-16 12:05:11', '', '2016-06-16 14:27:33', '', '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('f3ee562238a64b1ab73dfcba02031cd6', '未婚', null, 'fa072191b5984b609e47838786fb921b', 'fa072191b5984b609e47838786fb921b', '1', 'wh', '1', null, null, '2016-01-20 10:44:48', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('f3f1463e3f7a4d9e8a63e4b314676eef', '五险一金', 'wxyj', 'c7d09162a04343bba1faf9dc5cb239de', 'c7d09162a04343bba1faf9dc5cb239de', '1', 'gdzc-ygxc-wxyj', '2', '2015-12-05 13:19:44', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('f7336968dafe11e782f8f8db88fcba36', '请假类型', 'leave', null, 'f7336968dafe11e782f8f8db88fcba36', '0', 'leave', '1', null, null, '2017-12-07 11:45:53', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('f7386a89dafe11e782f8f8db88fcba36', '年假', 'leave_annual', 'f7336968dafe11e782f8f8db88fcba36', 'f7336968dafe11e782f8f8db88fcba36', '0', '1', '1', null, null, '2017-12-07 11:45:53', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('f73f35d5dafe11e782f8f8db88fcba36', '事假', 'leave_casual', 'f7336968dafe11e782f8f8db88fcba36', 'f7336968dafe11e782f8f8db88fcba36', '0', '2', '2', null, null, '2017-12-07 11:45:53', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('f745c9c7dafe11e782f8f8db88fcba36', '病假', 'leave_sick', 'f7336968dafe11e782f8f8db88fcba36', 'f7336968dafe11e782f8f8db88fcba36', '0', '3', '3', null, null, '2017-12-07 11:45:53', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('f74c6fcbdafe11e782f8f8db88fcba36', '调休假', 'leave_lieu', 'f7336968dafe11e782f8f8db88fcba36', 'f7336968dafe11e782f8f8db88fcba36', '0', '4', '4', null, null, '2017-12-07 11:45:54', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('f75301b8dafe11e782f8f8db88fcba36', '婚假', 'leave_marriage', 'f7336968dafe11e782f8f8db88fcba36', 'f7336968dafe11e782f8f8db88fcba36', '0', '5', '5', null, null, '2017-12-07 11:45:54', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('f759d0fadafe11e782f8f8db88fcba36', '产假', 'leave_maternity', 'f7336968dafe11e782f8f8db88fcba36', 'f7336968dafe11e782f8f8db88fcba36', '0', '6', '6', null, null, '2017-12-07 11:45:54', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('f760a984dafe11e782f8f8db88fcba36', '陪产假', 'leave_paternity', 'f7336968dafe11e782f8f8db88fcba36', 'f7336968dafe11e782f8f8db88fcba36', '0', '7', '7', null, null, '2017-12-07 11:45:54', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('f7676efedafe11e782f8f8db88fcba36', '丧假', 'leave_funeral', 'f7336968dafe11e782f8f8db88fcba36', 'f7336968dafe11e782f8f8db88fcba36', '0', '8', '8', null, null, '2017-12-07 11:45:54', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('f76e2885dafe11e782f8f8db88fcba36', '其他', 'leave_other', 'f7336968dafe11e782f8f8db88fcba36', 'f7336968dafe11e782f8f8db88fcba36', '0', '9', '9', null, null, '2017-12-07 11:45:54', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('fa072191b5984b609e47838786fb921b', '婚姻状况', 'hy-zk', null, 'fa072191b5984b609e47838786fb921b', '0', null, '1', '2015-12-14 17:55:29', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('fa3ec391c5294da295822e3ba8a3b61b', '设计', 'designContent', null, 'fa3ec391c5294da295822e3ba8a3b61b', '0', null, '11', null, null, '2015-12-05 12:07:31', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('fb058c0b60bb4e2d871d5738682e312d', '建筑', null, '2685353ae3c942be82325c57c3e9c94a', '2685353ae3c942be82325c57c3e9c94a', '1', 'jz', '2', null, null, '2015-12-15 17:58:06', null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('fbc60442a8e944798733ff90938e2b3f', '团队建设', 'tdjs', '5a7953f5aae046b08c63f6bbc99c1c00', '5a7953f5aae046b08c63f6bbc99c1c00', '1', 'bx-jyfy-tdjs', '1', '2015-12-05 13:10:07', null, null, null, '0');
--   INSERT INTO `net_maoding_data_dictionary` VALUES ('fe83906e408a4bb890a3f66d7b7139d5', '室内装饰', null, '2685353ae3c942be82325c57c3e9c94a', '2685353ae3c942be82325c57c3e9c94a', '1', 'snzs', '19', '2015-12-15 18:03:29', null, null, null, '0');
--
--   update maoding_web_user u
--     left join maoding_data_dictionary d on (u.major = d.id)
--     left join net_maoding_data_dictionary n on (d.name = n.name)
--   set u.major=n.id,d.id=n.id
--   where u.major is not null;
--
--   DROP TABLE IF EXISTS `net_maoding_data_dictionary`;
-- END;
-- call syncMajor();


-- 建立初始化权限数据过程
DROP PROCEDURE IF EXISTS `initPermission`;
CREATE PROCEDURE `initPermission`()
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
-- call initPermission();


-- 建立初始化常量存储过程
DROP PROCEDURE IF EXISTS `initConst`;
CREATE PROCEDURE `initConst`()
  BEGIN
    -- 常量分类
    delete from md_list_const where classic_id = 0;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,0,'常量分类',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,1,'操作权限',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,2,'合作类型',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,3,'任务类型',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,4,'财务类型',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,5,'文件类型',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,6,'财务节点类型',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,7,'动态类型',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,8,'个人任务类型',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,9,'邀请目的类型',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,10,'通知类型',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,12,'用户类型',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,13,'组织类型',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,14,'存储节点类型',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,15,'锁定状态',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,16,'同步模式',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,17,'删除状态',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,19,'文件服务器类型',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,20,'文件操作类型',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,21,'sky drive文档类型定义',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,22,'专业类型',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,23,'web task任务类型定义',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,24,'资料分类',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,25,'保留',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,26,'角色类型',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,27,'通知类型',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,28,'web权限组类型',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,29,'web权限类型',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,30,'web member角色类型',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,31,'校审意见类型',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,32,'校审意见状态类型',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,33,'功能分类',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,34,'专业信息',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,35,'设计范围',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,36,'模板变量类型',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,37,'模板内容定义','md_type_template_content_detail_const');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,38,'项目模板','md_type_template_const');

    -- -- -- -- --
    delete from md_list_const where classic_id = 38;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (38,0,'项目模板','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (38,1,'规划设计','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (38,2,'建筑设计','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (38,3,'景观设计','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (38,4,'装修设计','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (38,5,'市政设计','');

    -- -- -- -- --
    delete from md_list_const where classic_id = 37;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (37,0,'1.模板内容名称;2.所属模板名称','1.所属模板编号;2.显示横轴位置;3.显示纵轴位置;4.变量大类类型;5.变量细类选择;6.取值范围;7.子项名称（仅用于数字序列)');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (37,1,'功能分类;规划设计;','1;1;5;33;1,2,3,4,5,6,7,8,9,10,11,12;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (37,2,'专业信息;规划设计;','1;0;2;34;1,2,3,4,5,6,7,8,9;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (37,3,'设计范围;规划设计;','1;0;3;35;1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (37,4,'项目名称;规划设计;','1;3;3;36;1;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (37,5,'项目编号;规划设计;','1;5;3;36;2;;');

    -- -- -- -- --
    delete from md_list_const where classic_id = 36;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (36,0,'模板变量类型','1.单位');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (36,1,'字符串','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (36,2,'整数','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (36,3,'浮点数','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (36,4,'单选','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (36,5,'多选','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (36,6,'列表','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (36,7,'日期','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (36,8,'数字序列','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (36,11,'面积','m&sup2;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (36,12,'长度','m');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (36,13,'百分比','%');

    -- -- -- -- --
    delete from md_list_const where classic_id = 35;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (35,0,'设计范围','1.基本类型;2.取值范围;3.子项名称（仅用于数字序列)');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (35,1,'总图','5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (35,2,'给排水','5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (35,3,'暖通空调','5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (35,4,'气体灭火','5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (35,5,'概算','5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (35,6,'景观','5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (35,7,'室内装饰','5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (35,8,'建筑','5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (35,9,'弱电','5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (35,10,'燃气','5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (35,11,'人防','5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (35,12,'预算','5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (35,13,'泛光照明','5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (35,14,'BIM','5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (35,15,'结构','5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (35,16,'强电','5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (35,17,'消防','5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (35,18,'结构超限设计','5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (35,19,'幕墙','5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (35,20,'智能化','5');

    -- -- -- -- --
    delete from md_list_const where classic_id = 34;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (34,0,'专业信息','1.基本类型;2.取值范围;3.子项名称（仅用于数字序列,逗号分隔的变量名-前导字符串对)');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (34,1,'基地面积','11;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (34,2,'总建筑面积','11;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (34,3,'覆盖率','13;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (34,4,'建筑高度','12;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (34,5,'容积率','3;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (34,6,'计容建筑面积','11;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (34,7,'绿化率','13;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (34,8,'建筑层数','8;;b-地下,u-地上');

    -- -- -- -- --
    delete from md_list_const where classic_id = 33;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (33,0,'功能分类','1.基本类型;2.取值范围;3.子项名称（仅用于数字序列)');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (33,1,'居住建筑','5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (33,2,'酒店建筑','5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (33,3,'医疗卫生建筑','5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (33,4,'展会建筑','5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (33,5,'办公建筑','5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (33,6,'交通建筑','5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (33,7,'商业建筑','5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (33,8,'园区建筑','5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (33,9,'文化体育建筑','5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (33,10,'教育建筑','5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (33,11,'工业建筑','5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (33,12,'其他建筑','5');

    -- -- -- -- --
    delete from md_list_const where classic_id = 32;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (32,0,'校审意见状态类型','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (32,1,'通过','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (32,2,'不通过','');

    -- -- -- -- --
    delete from md_list_const where classic_id = 31;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (31,0,'校审意见类型','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (31,1,'校验','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (31,2,'审核','');

    -- -- -- -- --
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,30,'web member角色类型','1.类型布尔属性，1-项目角色,2-任务角色,3-任务负责人,4-设计,5-校对,6-审核；2.对应的角色;3.对应的mytask内task_type;4.对应的process内的名称');
    delete from md_list_const where classic_id = 30;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (30,0,'立项人','100000;23;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (30,1,'经营负责人','100000;20;1;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (30,2,'设计负责人','100000;30;2;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (30,3,'任务负责人','111000;40;12,13;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (30,4,'设计','110100;41;3;设计');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (30,5,'校对','110010;42;3;校对');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (30,6,'审核','110001;43;3;审核');

    -- -- -- -- --
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,29,'web权限类型','1.类型布尔属性，1-转换需加偏移量;2.权限标识;3.对应的基准角色;4.设置所需权限');
    delete from md_list_const where classic_id = 29;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (29,100,'创建分支架构/事业合伙人','0;org_partner;100;21');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (29,8,'权限配置','1;sys_role_permission;60;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (29,58,'企业认证','0;sys_role_auth;101;22');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (29,103,'历史数据导入','0;data_import;102;23');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (29,12,'组织信息管理','0;com_enterprise_edit;110;24');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (29,14,'组织架构设置','0;hr_org_set,hr_employee;111;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (29,19,'通知公告发布','0;admin_notice;112;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (29,46,'查看报销、费用统计报表','0;report_exp_static;130;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (29,110,'查看请假、出差、工时统计报表','0;summary_leave;131;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (29,54,'删除项目','0;project_delete;140;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (29,51,'任务签发','0;project_manager;141;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (29,52,'生产安排','0;design_manager;142;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (29,37,'合同信息','0;project_view_amount;143;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (29,20,'项目信息/项目总览/查看项目文档','0;project_edit;144;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (29,401,'查看财务报表','0;finance_report;120;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (29,40,'费用录入','0;finance_report;121;25');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (29,10,'财务设置','0;sys_finance_type;122;26');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (29,49,'确认付款日期','0;project_charge_manage;123;27');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (29,402,'确认到账日期','0;finance_back_fee;124;28');

    -- -- -- -- --
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,28,'web权限组类型','1.包含的web权限类型');
    delete from md_list_const where classic_id = 28;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (28,0,'未定义权限组','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (28,1,'后台管理','100,8,58,103');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (28,2,'组织管理','12,14,19');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (28,3,'审批报表','46,110');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (28,4,'项目管理','54,51,52,37,20');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (28,5,'财务管理','401,40,10,49,402');

    -- -- -- -- --
    delete from md_list_const where classic_id = 27;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (27,0,'通知类型','0.主题;1.标题;2.内容');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (27,1,'用户通用消息','User{UserId};用户消息;普通用户消息');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (27,2,'任务通用消息','Task{TaskId};任务消息;普通任务消息');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (27,3,'项目通用消息','Project{ProjectId};项目消息;普通项目消息');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (27,4,'组织通用消息','Company{CompanyId};组织消息;普通组织消息');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (27,5,'公共通用消息','notify:web;公共消息;普通公共消息');

    -- -- -- -- --
    delete from md_list_const where classic_id = 26;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,0,'角色类型','1.可分配角色；2.权限');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,10,'company_manager;总公司企业负责人','11,12,13,20,30,40,41,42,43,50,60,100,101,102,110,111,112,120,121,122,123,124,130,131,140,141,142,143,144;10,11,12,13');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,11,'company_manager_1;1类公司企业负责人','20,30,40,41,42,43,51,61,100,111,112,120,121,123,124,130,131,140,141,142,143,144;11');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,12,'company_manager_2;2类公司企业负责人','20,30,40,41,42,43,52,62,111,112,120,121,123,124,130,131,140,141,142,143,144;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,13,'company_manager_3;3类公司企业负责人','20,30,40,41,42,43,53,63,111,112,120,130,131,140,141,142,143,144;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,20,'project_manager;经营负责人','21,30,31;60');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,21,'project_assistant;经营助理','30,31;60');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,23,'project_creator;立项人',';');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,30,'design_manager;设计负责人','31,40,41,42,43;70,71');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,31,'design_assistant;设计助理','40,41,42,43;70,71');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,40,'task_manager;任务负责人','41,42,43;70');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,41,'designer;设计',';');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,42,'checker;校对',';');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,43,'auditor;审核',';');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,50,'sys_manager;系统管理员','60,100,101,102,110,111,112,120,121,122,123,124,130,131,140,141,142,143,144;10,11,12,13');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,51,'sys_manager_1;1类公司系统管理员','61,100,111,112,120,121,123,124,130,131,140,141,142,143,144;11,12,13');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,52,'sys_manager_2;2类公司系统管理员','62,111,112,120,121,123,124,130,131,140,141,142,143,144;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,53,'sys_manager_3;3类公司系统管理员','63,111,112,120,130,131,140,141,142,143,144;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,60,'permission_manager;总公司权限配置管理员',';20,21,22,23,24,25,26,27,28');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,61,'permission_manager_1;1类公司权限配置管理员',';20,21,27,28');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,62,'permission_manager_2;2类公司权限配置管理员',';20,27,28');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,63,'permission_manager_3;3类公司权限配置管理员',';20');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,100,'sub_org_manager;分支架构/事业合伙人管理员',';11,12,13');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,101,'company_audit_manager;企业认证管理员',';14');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,102,'data_manager;历史数据导入管理员',';100');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,110,'org_info_manager;组织信息管理员',';30');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,111,'org_tree_manager;组织架构设置管理员',';31');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,112,'org_notify_manager;通知公告发布管理员',';90');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,120,'financial_viewer;财务报表查看者',';110');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,121,'financial_input;费用录入管理员',';121');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,122,'financial_setting_manager;财务设置管理员',';120');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,123,'pay_auditor;付款日期确认管理员',';130');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,124,'gain_auditor;到账日期确认管理员',';131');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,130,'financial_report_viewer;报销、费用统计报表查看者',';111');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,131,'work_time_report_viewer;请假、出差、工时统计报表查看者',';112');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,140,'project_delete_manager;项目删除管理员',';40,41');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,141,'issue_manager;任务签发管理员','20,21;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,142,'task_manager;生产安排管理员','30,31;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,143,'contract_manager;合同信息管理员',';51,52');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (26,144,'project_info_viewer;项目信息/项目总览/项目文档查看者',';52');

    -- -- -- -- --
    delete from md_list_const where classic_id = 25;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (25,0,'web my_task任务类型定义',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (25,1,'项目角色',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (25,2,'任务角色',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (25,3,'组织角色',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (25,4,'任务用户角色',null);

    -- -- -- -- --
    delete from md_list_const where classic_id = 24;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (24,0,'资料分类','1.分类布尔属性,1-是否根分类,2-是否项目分类,3-显示;2.目录节点类型');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (24,1,'设计','011;100');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (24,2,'校审','011;200');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (24,3,'提资','011;300');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (24,4,'发布','010;400');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (24,5,'网站','010;500');

    -- -- -- -- --
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,23,'web task任务类型定义','1.任务布尔属性,1-是经营任务,2-是生产任务;2.转换时节点类型生成偏移量,3.归属的资料分类目录');
    delete from md_list_const where classic_id = 23;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (23,0,'生产任务','01;11;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (23,1,'签发任务','10;10;1,2,3');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (23,2,'签发生产任务','11;10;1,2,3');

    -- -- -- -- --
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,22,'web 专业类型',null);
    delete from md_list_const where classic_id = 22;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (22,0,'规划',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (22,1,'建筑',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (22,2,'室内',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (22,3,'景观',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (22,4,'结构',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (22,5,'给排水',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (22,6,'暖通',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (22,7,'电气',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (22,8,'其他',null);

    -- -- -- -- --
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,21,'sky drive文档类型定义','1.转换成目录的节点类型,2.转换成文件的节点类型');
    delete from md_list_const where classic_id = 21;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (21,0,'目录','501;521');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (21,1,'文件','501;521');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (21,2,'新建文件','501;521');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (21,3,'合同附件','501;521');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (21,4,'公司logo','501;521');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (21,5,'认证授权书','501;521');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (21,6,'移动端上传轮播图片','501;521');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (21,7,'公司邀请二维码','501;521');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (21,8,'营业执照的类型','501;521');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (21,9,'法人身份证信息','501;521');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (21,10,'经办人身份证类型','501;521');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (21,20,'报销附件类型','501;521');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (21,21,'通知公告附件类型','501;521');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (21,22,'朋友圈、项目讨论组附近类型','501;521');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (21,30,'成果文件目录','501;521');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (21,40,'未发送归档通知的归档目录','501;521');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (21,50,'已发送归档通知的归档目录','520;521');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (21,60,'会议，日程文件','520;521');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (21,100,'个人文件夹','520;521');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (21,101,'个人文件','520;521');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (21,201,'临时文件','520;521');

    -- -- -- -- --
    delete from md_list_const where classic_id = 20;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (20,0,'文件操作类型',     '1.布尔属性，1-是否提交校审,2-是否提资，2.新建节点类型;3.新建节点路径;4.文件服务器类型;5.文件服务器地址;6.服务器空间;7.通知类型;8:备份目录类型');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (20,1,'备份',     '00;131;历史版本/{SrcFileNoExt}_{Time:yyyyMMddHHmmss}{Ext};1;;;;0');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (20,2,'校对',     '00;221;{SrcFileNoExt}_{Action}_{Time:yyyyMMddHHmmss}{Ext};1;;;2;230');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (20,3,'审核',     '00;221;{SrcFileNoExt}_{Action}_{Time:yyyyMMddHHmmss}{Ext};1;;;2;230');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (20,4,'提资',     '01;321;/{Project}/{Range3}/{IssuePath}/{Major}/{Version}/{DesignTaskPath}/{SrcFile};1;;;3;330');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (20,5,'上传',     '00;521;/{Project}/{Range4}/{IssuePath}/{ProjectId}-{CompanyId}-{TaskId}-{SkyPid}-{OwnerUserId}/{SrcFile};2;;;5;520');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (20,6,'提交校审', '10;221;/{Project}/{Range2}/{TaskPath}/{SrcFile};1;;;2;230');

    -- -- -- -- --
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,19,'','');
    delete from md_list_const where classic_id = 19;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (19,0,'文件服务器类型','1.默认服务器地址(分号使用|代替);2.默认文件存储空间');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (19,1,'ICE管理磁盘','127.0.0.1|127.0.0.1;c:/work/file_server');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (19,2,'直连网站空间','http://172.16.6.73:8081/filecenter;group1');

    -- -- -- -- --
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,17,'删除状态',null);
    delete from md_list_const where classic_id = 17;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (17,0,'未删除',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (17,1,'已删除',null);

    -- -- -- -- --
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,16,'同步模式',null);
    delete from md_list_const where classic_id = 16;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (16,0,'手动同步',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (16,1,'自动同步',null);

    -- -- -- -- --
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,15,'锁定状态',null);
    delete from md_list_const where classic_id = 15;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (15,0,'不锁定',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (15,1,'锁定',null);

    -- -- -- -- --
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,14,'存储节点类型','1.节点布尔属性，1-是否目录,2-是否项目,3-是否签发任务,4-是否生产任务,5-是否设计文档,6-是否校审文档,7-是否提资文档,8-是否网站文档,9-是否历史文档;2.默认子目录类型;3.默认子文件类型;4.文件默认所属角色;5.所属分类编号');
    delete from md_list_const where classic_id = 14;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,0,'存储节点类型',   '1.节点布尔属性，1-是否目录,2-是否项目,3-是否签发任务,4-是否生产任务,5-是否设计文档,6-是否校审文档,7-是否提资文档,8-是否网站文档,9-是否历史文档;2.默认子目录类型;3.默认子文件类型;4.文件默认所属角色;5.所属分类编号');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,1,'未知类型目录',   '100000000;1;0;0;0');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,10,'项目根目录',    '110000000;1;0;41;0');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,100,'设计分类目录', '100000000;120;121;0;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,110,'设计签发任务', '101010000;120;121;0;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,111,'设计生产任务', '100110000;120;121;0;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,120,'设计用户目录', '100010000;120;121;0;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,121,'设计文件',     '000010000;0;0;0;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,130,'设计历史目录', '100010001;130;131;0;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,131,'设计历史文件', '000010001;0;0;0;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,200,'校审分类目录', '100000000;220;221;0;2');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,210,'校审签发任务', '101001000;220;221;0;2');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,220,'校审用户目录', '100001000;220;221;0;2');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,221,'校审文件',     '000001000;0;0;0;2');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,230,'校审历史目录', '100001001;230;231;0;2');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,231,'校审历史文件', '000001001;0;0;0;2');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,300,'提资分类目录', '100000000;320;321;0;3');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,310,'提资签发任务', '101000100;320;321;0;3');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,320,'提资用户目录', '100000100;320;321;0;3');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,321,'提资文件',     '000000100;0;0;0;3');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,330,'提资历史目录', '100000101;330;331;0;3');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,331,'提资历史文件', '000000101;0;0;0;3');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,400,'发布分类目录', '100000000;420;421;0;4');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,410,'发布签发任务', '101000000;420;421;0;4');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,420,'发布用户目录', '100000000;420;421;0;4');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,421,'发布文件',     '000000000;0;0;0;4');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,430,'发布历史目录', '100000001;430;431;0;4');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,431,'发布历史文件', '000000001;0;0;0;4');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,500,'网站分类目录', '100000000;0;0;0;5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,501,'网站普通目录', '100000010;0;0;0;5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,520,'网站归档目录', '100000010;0;0;0;5');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (14,521,'网站文件',     '000000010;0;0;0;5');

    -- -- -- -- --
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,11,'共享类型',null);
    delete from md_list_const where classic_id = 11;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (11,0,'全部共享',null);

    -- -- -- -- --
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,5,'文件类型','1.类型布尔属性，1-是否镜像');
    delete from md_list_const where classic_id = 5;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (5,0,'未知类型','0');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (5,1,'CAD设计文档','0');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (5,3,'合同附件','0');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (5,4,'公司logo','0');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (5,5,'认证授权书','0');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (5,6,'移动端上传轮播图片','0');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (5,7,'公司邀请二维码','0');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (5,8,'营业执照','0');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (5,9,'法人身份证信息','0');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (5,20,'报销附件','0');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (5,21,'通知公告附件','0');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (5,22,'镜像文件','1');
    -- -- -- -- --
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,1,'操作权限',null);
    delete from md_list_const where classic_id = 1;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,0,'-;无权限',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,10,'sys_enterprise_logout;解散自己负责的组织',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,11,'invite_others;邀请事业合伙人/分公司',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,12,'org_partner;创建非自己负责的事业合伙人/分公司',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,13,'disband_others;解散非自己负责的事业合伙人/分公司',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,14,'sys_role_auth;申请企业认证',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,20,'sys_role_permission;权限配置',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,21,'sys_role_permission_sub_org;权限配置-配置创建分支机构/事业合伙人管理员',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,22,'sys_role_permission_company_audit;权限配置-配置企业认证管理员',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,23,'sys_role_permission_data_import;权限配置-配置历史数据导入管理员',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,24,'sys_role_permission_org_info;权限配置-配置组织信息管理员',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,25,'sys_role_permission_financial_input;权限配置-配置费用录入管理员',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,26,'sys_role_permission_financial_setting;权限配置-配置财务设置管理员',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,27,'sys_role_permission_pay_confirm;权限配置-配置付款日期管理员',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,28,'sys_role_permission_gain_confirm;权限配置-配置到账日期管理员',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,30,'com_enterprise_edit;组织信息管理',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,31,'hr_org_set,hr_employee;组织架构管理',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,40,'project_delete;删除参与项目',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,41,'delete_project_others;删除未参与项目',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,50,'project_edit;编辑参与项目基本信息',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,51,'edit_contract;查看参与项目合同信息',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,52,'view_contract_others;查看未参与项目合同信息',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,53,'list_project_others;查看未参与项目',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,60,'project_manager;编辑和发布参与项目的签发任务',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,70,'design_manager;安排自己负责的生产任务',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,71,'design_others;安排非自己负责的生产任务',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,80,'view_document;查看参与项目文档',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,81,'view_document_others;查看未参与项目文档',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,90,'admin_notice;发布通知公告',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,100,'data_import;历史数据导入',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,110,'finance_report;查看财务报表',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,111,'report_exp_static,project_view_amount;查看费用统计报表',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,112,'summary_leave;查看工时统计报表',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,120,'sys_finance_type;财务设置',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,121,'finance_fixed_edit;费用录入',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,130,'project_charge_manage;确认付款日期',null);
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (1,131,'finance_back_fee;确认到账日期',null);
  END;
call initConst();
