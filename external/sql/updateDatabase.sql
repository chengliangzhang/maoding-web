-- -- -- 创建及更改表 -- 开始 -- -- --
-- 选择的标题栏
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
  BEGIN
    CREATE TABLE IF NOT EXISTS `maoding_web_project_condition` (
      `id` varchar(32) NOT NULL COMMENT '主键',
      `company_id` varchar(32) DEFAULT NULL COMMENT '企业id',
      `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
      `code` varchar(1000) DEFAULT NULL COMMENT '查询code值',
      `type` int(1) DEFAULT NULL COMMENT '类型：0：我的项目；1：项目总览',
      `status` int(1) DEFAULT NULL COMMENT '是否有效：0：有效：1：无效',
      `create_date` datetime DEFAULT NULL,
      `create_by` varchar(32) DEFAULT NULL,
      `update_date` datetime DEFAULT NULL,
      `update_by` varchar(32) DEFAULT NULL,
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_web_project_condition' and column_name='code') then
      alter table maoding_web_project_condition add column `code` varchar(1000) DEFAULT NULL COMMENT '查询code值';
    elseif exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_web_project_condition' and column_name='code' and character_maximum_length<1000) then
      alter table maoding_web_project_condition modify column `code` varchar(1000) DEFAULT NULL COMMENT '查询code值';
    end if;

  END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 交付历史表
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
    CREATE TABLE IF NOT EXISTS `md_list_deliver` (
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
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='交付历史表';
END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 项目自定义属性（模板属性）
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
    CREATE TABLE IF NOT EXISTS `maoding_web_project_property` (
      `id` char(32) NOT NULL COMMENT '项目自定义属性ID编号',
      `create_date` datetime DEFAULT NULL COMMENT '创建时间',
      `create_by` char(32) DEFAULT NULL COMMENT '创建人',
      `update_date` datetime DEFAULT NULL COMMENT '更新时间',
      `update_by` char(32) DEFAULT NULL COMMENT '更新人',

      `deleted` tinyint(1) unsigned DEFAULT '0' COMMENT '已删除',

      `project_id` char(32) NOT NULL DEFAULT '' COMMENT '项目自定义属性所属项目的ID',
      `field_name` varchar(255) NOT NULL DEFAULT '' COMMENT '自定义属性名称',
      `unit_name` varchar(20) NOT NULL DEFAULT '' COMMENT '自定义属性单位',
      `field_value` varchar(255) DEFAULT NULL COMMENT '自定义属性的值，统一格式化为字符串',
      `sequencing` int(8) unsigned DEFAULT '0' COMMENT '自定义属性在大类里的排序次序',
      `content_type_id` varchar(40) DEFAULT NULL COMMENT '内容类型编号',
      `x_pos` int(8) unsigned DEFAULT '0' COMMENT '横轴位置',
      `y_pos` int(8) unsigned DEFAULT '0' COMMENT '纵轴位置',
      `is_default` tinyint(1) unsigned DEFAULT '0' COMMENT '是否模板内容',
      `detail_var_name` varchar(255) DEFAULT NULL COMMENT '序列参数说明字符串，仅用于序列类型',
      `detail_type_id` int(8) DEFAULT '0' COMMENT '组件类型',
      PRIMARY KEY (`id`),
      KEY `project_id` (`project_id`),
      KEY `deleted` (`deleted`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_web_project_property' and column_name='content_type_id') then
      alter table maoding_web_project_property add column `content_type_id` varchar(40) DEFAULT NULL COMMENT '内容类型编号';
    end if;
    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_web_project_property' and column_name='x_pos') then
      alter table maoding_web_project_property add column `x_pos` int(8) unsigned DEFAULT '0' COMMENT '横轴位置';
    end if;
    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_web_project_property' and column_name='y_pos') then
      alter table maoding_web_project_property add column `y_pos` int(8) unsigned DEFAULT '0' COMMENT '纵轴位置';
    end if;
    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_web_project_property' and column_name='is_default') then
      alter table maoding_web_project_property add column `is_default` tinyint(1) unsigned DEFAULT '0' COMMENT '是否模板内容';
    end if;
    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_web_project_property' and column_name='detail_var_name') then
      alter table maoding_web_project_property add column `detail_var_name` varchar(255) DEFAULT NULL COMMENT '序列参数说明字符串，仅用于序列类型';
    end if;
    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_web_project_property' and column_name='detail_type_id') then
      alter table maoding_web_project_property add column `detail_type_id` int(8) DEFAULT '0' COMMENT '组件类型';
    end if;
END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 任务定义
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
    CREATE TABLE IF NOT EXISTS `maoding_web_project_task` (
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
      `priority` smallint(4) DEFAULT NULL COMMENT '优先级',
      PRIMARY KEY (`id`),
      KEY `from_company_id` (`from_company_id`),
      KEY `company_id` (`company_id`),
      KEY `project_id` (`project_id`),
      KEY `org_id` (`org_id`),
      KEY `task_pid` (`task_pid`),
      KEY `be_modify_id` (`be_modify_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务表';

    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_web_project_task' and column_name='priority') then
      alter table maoding_web_project_task add column `priority` smallint(4) DEFAULT NULL COMMENT '优先级';
    end if;

END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 常量定义
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
    CREATE TABLE IF NOT EXISTS `md_list_const` (
      `classic_id` smallint(4) unsigned NOT NULL COMMENT '常量分类编号，0：分类类别',
      `code_id` smallint(4) NOT NULL COMMENT '特定分类内的常量编号',
      `title` varchar(255) NOT NULL COMMENT '常量的可显示定义',
      `extra` varchar(255) DEFAULT NULL COMMENT '常量的控制信息定义',
      PRIMARY KEY (`classic_id`,`code_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_const' and column_name='code_id') then
      alter table md_list_const add column `code_id` smallint(4) NOT NULL COMMENT '特定分类内的常量编号';
    end if;
    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_const' and column_name='title') then
      alter table md_list_const add column `title` varchar(255) NOT NULL COMMENT '常量的可显示定义';
    end if;
    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_const' and column_name='extra') then
      alter table md_list_const add column `extra` varchar(255) DEFAULT NULL COMMENT '常量的控制信息定义';
    end if;
END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 自定义常量定义
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
    CREATE TABLE IF NOT EXISTS `md_list_const_custom` (
      `id` char(32) NOT NULL COMMENT '唯一编号',
      `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
      `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
      `last_modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
      `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
      `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',
    
      `company_id` char(32) DEFAULT NULL COMMENT '自定义常量所属组织编号',
      `project_id` char(32) DEFAULT NULL COMMENT '自定义常量所属项目编号',
      `task_id` char(32) DEFAULT NULL COMMENT '自定义常量所属任务编号',
    
      `classic_id` smallint(4) unsigned NOT NULL COMMENT '常量分类编号，0：分类类别',
      `code_id` smallint(4) NOT NULL COMMENT '初始常量编号',
      `title` varchar(255) NOT NULL COMMENT '常量的可显示定义',
      `extra` varchar(255) DEFAULT NULL COMMENT '常量的控制信息定义',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
    
    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_const_custom' and column_name='project_id') then
      alter table md_list_const_custom add column `project_id` char(32) DEFAULT NULL COMMENT '自定义常量所属项目编号';
    end if;
    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_const_custom' and column_name='code_id') then
      alter table md_list_const_custom add column `code_id` smallint(4) NOT NULL COMMENT '初始常量编号';
    end if;
    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_const_custom' and column_name='title') then
      alter table md_list_const_custom add column `title` varchar(255) NOT NULL COMMENT '常量的可显示定义';
    end if;
    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_const_custom' and column_name='extra') then
      alter table md_list_const_custom add column `extra` varchar(255) DEFAULT NULL COMMENT '常量的控制信息定义';
    end if;
END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

    
-- 软件版本描述
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
    CREATE TABLE IF NOT EXISTS `md_list_version` (
        `id` char(32) NOT NULL COMMENT '唯一编号',
        `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
        `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
        `last_modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
        `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
        `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

        `svn_repo` varchar(255) DEFAULT NULL COMMENT '在版本库内的项目名称',
        `svn_version` int(11) DEFAULT NULL COMMENT '在版本库内的唯一编号',
        `app_name` varchar(255) DEFAULT NULL COMMENT '软件名称,java软件为maven主pom内artifactId定义',
        `version_name` varchar(255) DEFAULT NULL COMMENT '版本名称,java软件为maven主pom内version定义',
        `update_url` varchar(255) DEFAULT NULL COMMENT '安装包下载地址',
        `description` varchar(512) DEFAULT NULL COMMENT '版本更改历史',
        `min_depend_svn_version` int(11) DEFAULT NULL COMMENT '依赖服务的最小有效版本',
        `max_depend_svn_version` int(11) DEFAULT NULL COMMENT '依赖服务的最大有效版本',
        `include_depend_svn_version` varchar(11) DEFAULT NULL COMMENT '依赖服务的范围外有效版本',
        `exclude_depend_svn_version` varchar(11) DEFAULT NULL COMMENT '依赖服务的范围内无效版本',
        PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 协同文件定义
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
    CREATE TABLE IF NOT EXISTS `md_list_storage_file` (
        `id` char(32) NOT NULL COMMENT '唯一编号',
        `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
        `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
        `last_modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
        `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
        `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

        `server_type_id` varchar(40) DEFAULT '0' COMMENT '文件服务器类型',
        `server_address` varchar(255) DEFAULT NULL COMMENT '文件服务器地址',
        `base_dir` varchar(255) DEFAULT NULL COMMENT '文件在文件服务器上的存储位置',
        `file_type_id` varchar(40) DEFAULT '0' COMMENT '文件类型',
        `file_version` varchar(20) DEFAULT NULL COMMENT '文件版本',
        `major_type_id` varchar(40) DEFAULT '0' NULL COMMENT '文件所属专业id',
        `main_file_id` char(32) DEFAULT NULL COMMENT '所对应的原始文件id',
        `read_only_key` varchar(255) DEFAULT NULL COMMENT '只读文件在文件服务器上的存储名称',
        `file_length` bigint(16) unsigned DEFAULT '0' COMMENT '只读文件长度',
        `file_md5` varchar(64) DEFAULT NULL COMMENT '只读文件md5校验值',
        `writable_key` varchar(255) DEFAULT NULL COMMENT '可写文件在文件服务器上的存储名称',
        `status` char(8) DEFAULT '00000000' COMMENT '文件布尔属性，1-已提交校审，2-已通过校验，3-已通过审核',
        `file_status` bit(8) DEFAULT b'0' COMMENT '准备用来代替status,文件布尔属性，1-已提交校审，2-已通过校验，3-已通过审核',
        `owner_user_id` char(32) DEFAULT NULL COMMENT '文件所属用户编号',
        `company_id` char(32) DEFAULT NULL COMMENT '文件所属组织编号',
        PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='company_id') then
        alter table md_list_storage_file add column `company_id` char(32) DEFAULT NULL COMMENT '文件所属组织编号';
    end if;
    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='owner_user_id') then
        alter table md_list_storage_file add column `owner_user_id` char(32) DEFAULT NULL COMMENT '文件所属用户编号';
    end if;
    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='file_status') then
        alter table md_list_storage_file add column `file_status` bit(8) DEFAULT b'0' COMMENT '准备用来代替status,文件布尔属性，1-已提交校审，2-已通过校验，3-已通过审核';
    end if;
    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='status') then
        alter table md_list_storage_file add column `status` char(8) DEFAULT '00000000' COMMENT '文件布尔属性，1-已提交校审，2-已通过校验，3-已通过审核';
    end if;
    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='file_md5') then
        alter table md_list_storage_file add column `file_md5` varchar(64) DEFAULT NULL COMMENT '只读文件md5校验值';
    end if;
    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='file_length') then
        alter table md_list_storage_file add column `file_length` bigint(16) unsigned DEFAULT '0' COMMENT '只读文件长度';
    end if;
    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='file_type_id') then
        alter table md_list_storage_file add column `file_type_id` varchar(40) DEFAULT '0' COMMENT '文件类型';
    elseif not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='file_type_id' and data_type='varchar') then
        alter table md_list_storage_file modify column `file_type_id` varchar(40) DEFAULT '0' COMMENT '文件类型';
    end if;
    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='server_type_id') then
        alter table md_list_storage_file add column `server_type_id` varchar(40) DEFAULT '0' COMMENT '文件服务器类型';
    elseif not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='server_type_id' and data_type='varchar') then
        alter table md_list_storage_file modify column `server_type_id` varchar(40) DEFAULT '0' COMMENT '文件服务器类型';
    end if;
    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='last_modify_role_id') then
        alter table md_list_storage_file add column `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id';
    elseif not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='last_modify_role_id' and character_maximum_length>32) then
        alter table md_list_storage_file modify column `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id';
    end if;
    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='major_type_id') then
        alter table md_list_storage_file add column `major_type_id` varchar(40) DEFAULT '0' NULL COMMENT '文件所属专业id';
    elseif not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='major_type_id' and data_type='varchar') then
        alter table md_list_storage_file modify column `major_type_id` varchar(40) DEFAULT '0' NULL COMMENT '文件所属专业id';
    end if;
    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='base_dir') then
        alter table md_list_storage_file add column `base_dir` varchar(255) DEFAULT NULL COMMENT '文件在文件服务器上的存储位置';
    end if;
    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='read_only_key') then
        alter table md_list_storage_file add column `read_only_key` varchar(255) DEFAULT NULL COMMENT '只读文件在文件服务器上的存储名称';
    end if;
    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file' and column_name='writable_key') then
        alter table md_list_storage_file add column `writable_key` varchar(255) DEFAULT NULL COMMENT '可写文件在文件服务器上的存储名称';
    end if;
END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 任务关系
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
  BEGIN
  CREATE TABLE IF NOT EXISTS `maoding_web_project_task_relation` (
    `id` varchar(32) NOT NULL,
    `from_company_id` varchar(32) DEFAULT NULL COMMENT '任务发包方id',
    `to_company_id` varchar(32) DEFAULT NULL COMMENT '任务接收包',
    `task_id` varchar(32) DEFAULT NULL COMMENT '任务id',
    `relation_status` int(2) DEFAULT NULL COMMENT '状态(0:有效，1无效)',
    `relation_type` int(10) DEFAULT NULL COMMENT '暂时未用',
    `create_date` datetime DEFAULT NULL COMMENT '创建时间',
    `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
    `update_date` datetime DEFAULT NULL COMMENT '更新时间',
    `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
    `project_id` varchar(32) DEFAULT NULL COMMENT '项目id（冗余字段）',
    PRIMARY KEY (`id`),
    KEY `from_company_id` (`from_company_id`),
    KEY `to_company_id` (`to_company_id`),
    KEY `task_id` (`task_id`),
    KEY `project_id` (`project_id`) USING BTREE
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_web_project_task_relation' and column_name='project_id') then
    alter table maoding_web_project_task_relation add column `project_id` varchar(32) DEFAULT NULL COMMENT '项目id（冗余字段）';
  end if;
END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 报销主表
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
  BEGIN
  CREATE TABLE IF NOT EXISTS `maoding_web_exp_main` (
    `id` varchar(32) NOT NULL COMMENT '主键id，uuid',
    `company_user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
    `exp_date` date DEFAULT NULL COMMENT '报销日期',
    `approve_status` varchar(1) DEFAULT NULL COMMENT '审批状态(0:待审核，1:同意，2，退回,3:撤回,4:删除,5.审批中）,6:财务已拨款',
    `company_id` varchar(32) DEFAULT NULL COMMENT '企业id',
    `depart_id` varchar(32) DEFAULT NULL COMMENT '部门id',
    `remark` varchar(255) DEFAULT NULL COMMENT '备注',
    `create_date` datetime DEFAULT NULL COMMENT '创建时间',
    `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
    `update_date` datetime DEFAULT NULL COMMENT '更新时间',
    `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
    `version_num` int(11) DEFAULT '0',
    `exp_no` varchar(32) DEFAULT NULL COMMENT '报销单号',
    `exp_flag` int(11) DEFAULT '0' COMMENT '0:没有任何操作，1:退回记录重新提交,2:新生成记录',
    `type` int(1) DEFAULT '0' COMMENT '报销类别：1=报销申请，2=费用申请,3请假，4出差',
    `allocation_date` date DEFAULT NULL COMMENT '拨款日期',
    `allocation_user_id` varchar(32) DEFAULT NULL COMMENT '拨款人id',
    `enterprise_id` varchar(36) DEFAULT NULL COMMENT '收款方公司id',
    PRIMARY KEY (`id`),
    KEY `company_user_id` (`company_user_id`),
    KEY `company_id` (`company_id`),
    KEY `depart_id` (`depart_id`),
    KEY `allocation_user_id` (`allocation_user_id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='报销主表';

  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_web_exp_main' and column_name='enterprise_id') then
    alter table maoding_web_exp_main add column `enterprise_id` varchar(36) DEFAULT NULL COMMENT '收款方公司id';
  end if;

  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_web_exp_main' and column_name='company_user_id') then
    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_web_exp_main' and column_name='user_id') then
      ALTER TABLE maoding_web_exp_main add company_user_id varchar(32);
    else
     ALTER TABLE maoding_web_exp_main change user_id company_user_id varchar(32);
    end if;
  end if;
  END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 报销审核表
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
  CREATE TABLE IF NOT EXISTS `maoding_web_exp_audit` (
    `id` varchar(32) NOT NULL COMMENT '主键id，uuid',
    `parent_id` varchar(32) DEFAULT NULL COMMENT '原主键id，uuid',
    `is_new` varchar(1) NOT NULL DEFAULT 'Y' COMMENT '是否最新审核 Y是 N否',
    `main_id` varchar(32) DEFAULT NULL COMMENT '报销主单id',
    `approve_status` varchar(1) DEFAULT NULL COMMENT '审批状态(0:待审核，1:同意，2，退回）',
    `approve_date` date DEFAULT NULL COMMENT '审批日期',
    `audit_person` varchar(32) DEFAULT NULL COMMENT '审核人id',
    `submit_audit_id` VARCHAR(32) DEFAULT NULL COMMENT '提交审核人的id',
    `audit_message` varchar(500) DEFAULT NULL COMMENT '审批意见',
    `create_date` datetime DEFAULT NULL COMMENT '创建时间',
    `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
    `update_date` datetime DEFAULT NULL COMMENT '更新时间',
    `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`),
    KEY `parent_id` (`parent_id`),
    KEY `main_id` (`main_id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='报销审核表';

  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_web_exp_audit' and column_name='submit_audit_id') then
     ALTER table maoding_web_exp_audit add  `submit_audit_id` VARCHAR(32) DEFAULT NULL COMMENT '提交审核人的id' ;
  end if;
END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 外部公司表
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
  CREATE TABLE IF NOT EXISTS  `maoding_web_enterprise` (
    `id` varchar(36) NOT NULL COMMENT '在工商局方的id(此处的id为36位）',
    `corpname` varchar(100) NOT NULL,
    `address` varchar(300) DEFAULT NULL,
    `creditcode` varchar(50) DEFAULT NULL,
    `gongsh` varchar(30) DEFAULT NULL,
    `orgcode` varchar(50) DEFAULT NULL,
    `state` varchar(100) DEFAULT NULL,
    `type` varchar(50) DEFAULT NULL,
    `regtime` date DEFAULT NULL,
    `proxyer` varchar(20) DEFAULT NULL,
    `money` varchar(500) DEFAULT NULL,
    `regpart` varchar(100) DEFAULT NULL,
    `fieldrange` text,
    `shortcut` varchar(50) DEFAULT NULL,
    `industry` varchar(200) DEFAULT NULL,
    `tax_number` varchar(100) DEFAULT NULL,
    `has_unify` varchar(10) DEFAULT NULL,
    `create_date` datetime DEFAULT NULL COMMENT '创建时间',
    `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
    `update_date` datetime DEFAULT NULL COMMENT '更新时间',
    `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
    `enterprise_type` int(1) DEFAULT NULL COMMENT '1=工商数据,2=手工输入',
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 外部公司关联表
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
  CREATE TABLE IF NOT EXISTS  `maoding_web_enterprise_org` (
    `id` varchar(32) NOT NULL,
    `company_id` varchar(32) NOT NULL,
    `enterprise_id` varchar(36) NOT NULL,
    `deleted` int(1) DEFAULT NULL,
    `create_date` datetime DEFAULT NULL COMMENT '创建时间',
    `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
    `update_date` datetime DEFAULT NULL COMMENT '更新时间',
    `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`),
    KEY `company_id` (`company_id`),
    KEY `enterprise_id` (`enterprise_id`),
    KEY `deleted` (`deleted`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 权限组表
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
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
END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 权限树表
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
  CREATE TABLE IF NOT EXISTS `maoding_web_role_permission` (
    `id` varchar(32) NOT NULL COMMENT 'ID',
    `role_id` varchar(32) DEFAULT NULL COMMENT '角色ID',
    `permission_id` varchar(32) DEFAULT NULL COMMENT '权限ID',
    `company_id` varchar(32) DEFAULT NULL COMMENT '公司ID',
    `create_date` datetime DEFAULT NULL COMMENT '创建时间',
    `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
    `update_date` datetime DEFAULT NULL COMMENT '更新时间',
    `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`),
    KEY `role_id` (`role_id`),
    KEY `permission_id` (`permission_id`),
    KEY `company_id` (`company_id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色视图表';
END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 权限表
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
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
END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 组织角色表
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
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
END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 文件注解附件
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
  CREATE TABLE IF NOT EXISTS `md_list_attachment` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `annotate_id` char(32) DEFAULT NULL COMMENT '文件注解编号',
    `attachment_file_id` char(32) DEFAULT NULL COMMENT '文件类附件编号',
    `attachment_element_id` char(32) DEFAULT NULL COMMENT '嵌入元素类附件编号',
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 文件注解
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
  CREATE TABLE IF NOT EXISTS `md_tree_annotate` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `pid` char(32) DEFAULT NULL COMMENT '父注解编号',
    `node_name` varchar(255) DEFAULT NULL COMMENT '文件注解标题',
    `path` varchar(255) DEFAULT NULL COMMENT '文件注解路径，以"/"作为分隔符',
    `type_id` varchar(40) DEFAULT '0' COMMENT '文件注解类型',

    `content` text(2048) DEFAULT NULL COMMENT '文件注解正文',
    `file_id` char(32) DEFAULT NULL COMMENT '被注解的文件的编号',
    `main_file_id` char(32) DEFAULT NULL COMMENT '原始文件的编号',
    `status_id` varchar(40) DEFAULT '0' COMMENT '文件注解状态',
    `creator_user_id` char(32) DEFAULT NULL COMMENT '注解创建者用户编号',
    `creator_role_id` varchar(40) DEFAULT NULL COMMENT '注解创建者职责编号',
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_annotate' and column_name='status_id') then
    alter table md_tree_annotate add column `status_id` varchar(40) DEFAULT '0' COMMENT '文件注解状态';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_annotate' and column_name='creator_role_id') then
    alter table md_tree_annotate add column `creator_role_id` varchar(40) DEFAULT NULL COMMENT '注解创建者职责编号';
  elseif not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_annotate' and column_name='creator_role_id' and data_type='varchar' and character_maximum_length>=40) then
    alter table md_tree_annotate modify column `creator_role_id` varchar(40) DEFAULT NULL COMMENT '注解创建者职责编号';
  end if;
END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 内嵌HTML元素
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
  CREATE TABLE IF NOT EXISTS `md_list_element` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `title` varchar(255) DEFAULT NULL COMMENT '占位符',
    `data_array` longblob DEFAULT NULL COMMENT '元素内容',
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_element' and column_name='title') then
    alter table md_list_element add column `title` varchar(255) DEFAULT NULL COMMENT '占位符';
  end if;
END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 协同节点定义
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
	CREATE TABLE IF NOT EXISTS `md_tree_storage` (
		`id` char(32) NOT NULL COMMENT '唯一编号',
		`deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
		`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
		`last_modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
		`last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
		`last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

		`pid` char(32) DEFAULT NULL COMMENT '父节点在此表中的id',
    `node_name` varchar(255) DEFAULT NULL COMMENT '节点名称',
		`path` varchar(255) DEFAULT NULL COMMENT '文件或目录带路径全名，以"/"作为分隔符',
		`type_id` varchar(40) DEFAULT '0' COMMENT '节点类型',

		`task_id` char(32) DEFAULT NULL COMMENT '节点所属生产任务id',
		`project_id` char(32) DEFAULT NULL COMMENT '节点所属项目id',
    `owner_user_id` char(32) DEFAULT NULL COMMENT '节点所属用户id',

    `file_length` bigint(16) unsigned DEFAULT '0' COMMENT '节点文件长度，与只读文件的长度相同',
    `file_md5` varchar(64) DEFAULT NULL COMMENT '节点文件md5校验值，与只读文件md5校验值相同',
		PRIMARY KEY (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage' and column_name='file_md5') then
    alter table md_tree_storage add column `file_md5` varchar(64) DEFAULT NULL COMMENT '节点文件md5校验值，与只读文件md5校验值相同';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage' and column_name='project_id') then
    alter table md_tree_storage add column `project_id` char(32) DEFAULT NULL COMMENT '节点所属项目id';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage' and column_name='file_length') then
    alter table md_tree_storage add column `file_length` bigint(16) unsigned DEFAULT '0' COMMENT '节点文件长度';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage' and column_name='node_name') then
    alter table md_tree_storage add column `node_name` varchar(255) DEFAULT NULL COMMENT '节点名称';
  end if;
	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage' and column_name='task_id') then
		alter table md_tree_storage add column `task_id` char(32) DEFAULT NULL COMMENT '节点所属生产任务id';
	end if;
	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage' and column_name='last_modify_role_id') then
		alter table md_tree_storage add column `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id';
	end if;
	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_tree_storage' and column_name='owner_user_id') then
		alter table md_tree_storage add column `owner_user_id` char(32) DEFAULT NULL COMMENT '节点所属用户id';
	end if;
END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 协同文件校审提资历史记录定义
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
	CREATE TABLE IF NOT EXISTS `md_list_storage_file_his` (
		`id` char(32) NOT NULL COMMENT '唯一编号',
		`deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
		`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
		`last_modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
		`last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `main_file_id` char(32) DEFAULT NULL COMMENT '协同文件编号',
    `action_type_id` varchar(40) DEFAULT '0' COMMENT '文件操作动作类型',
    `file_length` bigint(16) unsigned DEFAULT '0' COMMENT '文件操作时的只读文件长度',
    `file_md5` varchar(64) DEFAULT NULL COMMENT '文件操作时的只读文件md5校验值',
    `remark` text(2048) DEFAULT NULL COMMENT '文件注释',
		PRIMARY KEY (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;

	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file_his' and column_name='file_length') then
		alter table md_list_storage_file_his add column `file_length` bigint(16) unsigned DEFAULT '0' COMMENT '文件操作时的只读文件长度';
	end if;
	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file_his' and column_name='file_md5') then
		alter table md_list_storage_file_his add column `file_md5` varchar(64) DEFAULT NULL COMMENT '文件操作时的只读文件md5校验值';
	end if;
	if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file_his' and column_name='remark') then
		alter table md_list_storage_file_his add column `remark` text(2048) DEFAULT NULL COMMENT '文件注释';
	end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file_his' and column_name='last_modify_role_id') then
    alter table md_list_storage_file_his add column `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='md_list_storage_file_his' and column_name='main_file_id') then
    alter table md_list_storage_file_his add column `main_file_id` char(32) DEFAULT NULL COMMENT '协同文件编号id';
  end if;
END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 组织定义
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
  CREATE TABLE IF NOT EXISTS `md_tree_org` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `pid` char(32) DEFAULT NULL COMMENT '父节点在此表中的id',
    `node_name` varchar(255) DEFAULT NULL COMMENT '节点名称',
    `path` varchar(255) DEFAULT NULL COMMENT '文件或目录带路径全名，以"/"作为分隔符',
    `type_id` varchar(40) DEFAULT '0' COMMENT '节点类型',

    `company_id` char(32) DEFAULT NULL COMMENT '组织详细内容编号',
    `response_user_id` char(32) DEFAULT NULL COMMENT '负责人用户编号',
    `response_role_id` varchar(40) DEFAULT NULL COMMENT '负责人角色编号',
    `role_type_id` varchar(40) DEFAULT NULL COMMENT '默认角色编号',
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 公司定义
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
  CREATE TABLE IF NOT EXISTS `md_list_org_company` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `company_name` varchar(255) DEFAULT NULL COMMENT '公司名称',

    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 成员定义
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
  CREATE TABLE IF NOT EXISTS `md_list_role` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `org_id` char(32) DEFAULT NULL COMMENT '组织id',
    `work_id` char(32) DEFAULT NULL COMMENT '工作id',

    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 工作定义
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
  CREATE TABLE IF NOT EXISTS `md_tree_work` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `pid` char(32) DEFAULT NULL COMMENT '父节点在此表中的id',
    `node_name` varchar(255) DEFAULT NULL COMMENT '节点名称',
    `path` varchar(255) DEFAULT NULL COMMENT '文件或目录带路径全名，以"/"作为分隔符',
    `type_id` varchar(40) DEFAULT '0' COMMENT '节点类型',

    `response_user_id` char(32) DEFAULT NULL COMMENT '负责人用户编号',
    `response_role_id` varchar(40) DEFAULT NULL COMMENT '负责人角色编号',
    `start_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '启动时间',
    `end_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '预期结束时间',
    `company_id` char(32) DEFAULT NULL COMMENT '所属公司编号',

    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 项目定义
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
  CREATE TABLE IF NOT EXISTS `md_list_work_project` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `from_company_id` char(32) DEFAULT NULL COMMENT '甲方编号',
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 签发任务定义
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
  CREATE TABLE IF NOT EXISTS `md_list_work_issue` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `from_company_id` char(32) DEFAULT NULL COMMENT '来源组织编号',
    `project_id` char(32) DEFAULT NULL COMMENT '项目编号',

    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 生产任务定义
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
  CREATE TABLE IF NOT EXISTS `md_list_work_task` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `issue_id` char(32) DEFAULT NULL COMMENT '来源签发任务编号',
    `project_id` char(32) DEFAULT NULL COMMENT '项目编号',
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 项目和任务更改历史
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
  CREATE TABLE IF NOT EXISTS `md_list_work_his` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `work_id` char(32) DEFAULT NULL COMMENT '工作编号',
    `start_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '启动时间',
    `end_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '预期结束时间',
    `remark` text(2048) DEFAULT NULL COMMENT '更改注释',
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 用户界面
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
  CREATE TABLE IF NOT EXISTS `md_list_gui` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `gui_type_id` varchar(40) DEFAULT NULL COMMENT '界面参数类型',
    `user_id` char(32) DEFAULT NULL COMMENT '相关用户',
    `record_id` varchar(40) DEFAULT NULL COMMENT '相关记录',
    `order_num` int(8) unsigned DEFAULT '0' COMMENT '记录排序序号',

    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 财务记录
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
  CREATE TABLE IF NOT EXISTS `md_tree_finance` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `pid` char(32) DEFAULT NULL COMMENT '父节点在此表中的id',
    `node_name` varchar(255) DEFAULT NULL COMMENT '节点名称',
    `path` varchar(255) DEFAULT NULL COMMENT '文件或目录带路径全名，以"/"作为分隔符',
    `type_id` varchar(40) DEFAULT '0' COMMENT '节点类型',

    `project_id` char(32) DEFAULT NULL COMMENT '相关项目编号',
    `fee` bigint(20) DEFAULT '0' COMMENT '费用金额',

    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 收支记录
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
  CREATE TABLE IF NOT EXISTS `md_list_finance_bill` (
    `id` char(32) NOT NULL COMMENT '唯一编号',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `last_modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
    `last_modify_user_id` char(32) DEFAULT NULL COMMENT '记录最后修改者用户id',
    `last_modify_role_id` varchar(40) DEFAULT NULL COMMENT '记录最后修改者职责id',

    `bill_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '收发日期',

    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
END;
call update_table();
DROP PROCEDURE IF EXISTS `update_table`;

-- 建立重置索引存储过程
DROP PROCEDURE IF EXISTS `createIndex`;
CREATE PROCEDURE `createIndex`()
BEGIN
	-- 重新创建索引
	declare sqlString VARCHAR(255);
	declare tableName VARCHAR(255);
	declare doneTable tinyint default false;
	declare curTable cursor for select table_name from information_schema.tables where (table_schema=database()) && (table_type='BASE TABLE');
	declare continue HANDLER for not found set doneTable = true;

	open curTable;
	fetch curTable into tableName;
	while (not doneTable) do
		BEGIN
				-- 创建针对名称为*_id的字段的索引
				declare fieldName VARCHAR(255);
				declare doneField tinyint default false;
				declare curField cursor for select column_name from information_schema.columns
																		where (table_schema=database()) and (table_name=tableName)
																				and ((column_name='deleted') or (column_name='pid') or (column_name='path') or (column_name like '%\_id') or (column_name like '%\_pid'));
				declare continue HANDLER for not found set doneField = true;
				open curField;
				fetch curField into fieldName;
				while (not doneField) do
					-- 删除原有字段同名索引
					BEGIN
						declare indexName VARCHAR(255);
						declare doneDrop tinyint default false;
						declare curIndex cursor for select index_name from information_schema.statistics
																				where (table_schema=database()) and (table_name=tableName) and (index_name=fieldName);
						declare continue HANDLER for not found set doneDrop = true;
						open curIndex;
						fetch curIndex into indexName;
						while (not doneDrop) do
							set @sqlString = concat("drop index ",fieldName," on ",tableName);
							prepare sqlCommand from @sqlString;
							execute sqlCommand;
							drop PREPARE sqlCommand;
							fetch curIndex into indexName;
						end while;
						close curIndex;
					END;

					-- 创建与字段同名索引
					BEGIN
						set @sqlString = concat("create index ",fieldName," on ",tableName," (",fieldName,")");
						prepare sqlCommand from @sqlString;
						execute sqlCommand;
						drop PREPARE sqlCommand;
					END;
					fetch curField into fieldName;
				end while;
				close curField;
		END;
		fetch curTable into tableName;
	end while;
	close curTable;
END;
-- call createIndex();

-- 建立备份数据存储过程
DROP PROCEDURE IF EXISTS `backupData`;
CREATE PROCEDURE `backupData`()
BEGIN
	declare tableName VARCHAR(255);
	declare doneTable tinyint default false;
	declare curTable cursor for select table_name from information_schema.tables where (table_schema=database()) and (TABLE_NAME not like 'backup_%');
	declare continue HANDLER for not found set doneTable = true;

	open curTable;
	fetch curTable into tableName;
	while (not doneTable) do
		BEGIN
			-- 清理原有备份表
			set @sqlString = concat("drop table if exists `backup_",tableName,'`');
			prepare sqlCommand from @sqlString;
			execute sqlCommand;
			drop PREPARE sqlCommand;
			-- 创建备份表
			set @sqlString = concat("create table `backup_",tableName,'` like ',tableName);
			prepare sqlCommand from @sqlString;
			execute sqlCommand;
			drop PREPARE sqlCommand;
			-- 备份数据
			set @sqlString = concat("insert `backup_",tableName,'` select * from ',tableName);
			prepare sqlCommand from @sqlString;
			execute sqlCommand;
			drop PREPARE sqlCommand;
		END;
		fetch curTable into tableName;
	end while;
	close curTable;
END;
-- call backupData();

-- 建立还原备份数据存储过程
DROP PROCEDURE IF EXISTS `restoreData`;
CREATE PROCEDURE `restoreData`()
BEGIN
	declare tableName VARCHAR(255);
	declare doneTable tinyint default false;
	declare curTable cursor for select a.table_name from information_schema.tables a inner join information_schema.tables b on (concat('backup_',a.TABLE_NAME) = b.TABLE_NAME)
	where (a.table_schema=database()) and (a.TABLE_NAME not like 'backup_%') and (b.table_schema=database()) and (b.TABLE_NAME like 'backup_%');
	declare continue HANDLER for not found set doneTable = true;

	open curTable;
	fetch curTable into tableName;
	while (not doneTable) do
		BEGIN
			declare fieldName VARCHAR(255);
			declare fieldLength LONG;
			declare fieldType VARCHAR(255);
			declare doneField tinyint default false;
			declare curField cursor for select a.column_name,a.CHARACTER_MAXIMUM_LENGTH,a.DATA_TYPE from information_schema.columns a inner join information_schema.columns b on (a.column_name=b.column_name)
			where (a.table_schema=database()) and (a.table_name=tableName) and (b.table_schema=database()) and (b.table_name=concat('backup_',tableName));
			declare continue HANDLER for not found set doneField = true;

			open curField;
			fetch curField into fieldName,fieldLength,fieldType;
			set @cs = "";
			set @vs = "";
			while (not doneField) do
				if (@cs != "") then
					set @cs = concat(@cs,",");
					set @vs = concat(@vs,",");
				end if;
				set @cs = concat(@cs,"`",fieldName,"`");
				if ((fieldType='varchar') or (fieldType='char') or (fieldType='text') or (fieldType='longtext')) then
					set @vs = concat(@vs,"left(`",fieldName,"`,",fieldLength,")");
				else
					set @vs = concat(@vs,"`",fieldName,"`");
				end if;
				fetch curField into fieldName,fieldLength,fieldType;
			end while;
			close curField;

			set @sqlString = concat("replace into ",tableName," (",@cs,") select ",@vs," from backup_",tableName);
			prepare sqlCommand from @sqlString;
			execute sqlCommand;
			drop PREPARE sqlCommand;
		END;
		fetch curTable into tableName;
	end while;
	close curTable;
END;
-- call restoreData();
-- -- -- 创建及更改表 -- 结束 -- -- --

-- -- -- 创建及更改常量 -- 开始 -- -- --
-- 建立初始化常量存储过程
DROP PROCEDURE IF EXISTS `initConst`;
CREATE PROCEDURE `initConst`()
  BEGIN
    -- 分类
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

-- web member角色类型
DROP PROCEDURE IF EXISTS `initConst`;
CREATE PROCEDURE `initConst`()
  BEGIN
    -- -- 常量
    delete from md_list_const where classic_id = 30;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (30,-1,'web member角色类型','000000:1-任务角色,2-负责人,3-设计,4-校对,5-审核,6-助理;2.对应的角色;3.对应的mytask内task_type;4.对应的process内的名称');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (30,0,'立项人','000000;23;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (30,1,'经营负责人','010000;20;1;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (30,2,'设计负责人','011000;30;2;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (30,3,'任务负责人','110000;40;12,13;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (30,4,'设计','101000;41;3;设计');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (30,5,'校对','100100;42;3;校对');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (30,6,'审核','100010;43;3;审核');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (30,7,'经营助理','000001;20;1;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (30,8,'设计助理','001001;30;2;');

    -- -- 类型
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,30,'web member角色类型','md_type_web_member');

    -- 视图
    CREATE OR REPLACE VIEW `md_type_web_member` AS
      select
        member_type.code_id                as id,
        member_type.code_id                as type_id,
        member_type.title                  as type_title,
        member_type.title                  as type_name,
        member_type.extra                  as type_extra,
        substring(member_type.extra,
                  1,
                  char_length(substring_index(member_type.extra, ';', 1)))
                                           as type_attr,
        substring(member_type.extra,1,1)   as is_task_role,
        substring(member_type.extra,2,1)   as is_leader,
        substring(member_type.extra,3,1)   as is_designer,
        substring(member_type.extra,4,1)   as is_checker,
        substring(member_type.extra,5,1)   as is_auditor,
        substring(member_type.extra,6,1)   as is_assistant,

        (substring(member_type.extra, 1, 1) != 1) as is_project_role,
        (substring(member_type.extra, 1, 1) and substring(member_type.extra, 2, 1)) as is_task_leader,
        (substring(member_type.extra, 1, 1) and substring(member_type.extra, 3, 1)) as is_task_designer,
        (substring(member_type.extra, 1, 1) and substring(member_type.extra, 4, 1)) as is_task_checker,
        (substring(member_type.extra, 1, 1) and substring(member_type.extra, 5, 1)) as is_task_auditor,
        substring(member_type.extra,
                  char_length(substring_index(member_type.extra, ';', 1)) + 2,
                  char_length(substring_index(member_type.extra, ';', 2)) - char_length(substring_index(member_type.extra, ';', 1)) - 1)
                                           as role_type,
        substring(member_type.extra,
                  char_length(substring_index(member_type.extra, ';', 2)) + 2,
                  char_length(substring_index(member_type.extra, ';', 3)) - char_length(substring_index(member_type.extra, ';', 2)) - 1)
                                           as mytask_task_type,
        substring(member_type.extra,
                  char_length(substring_index(member_type.extra, ';', 3)) + 2,
                  char_length(substring_index(member_type.extra, ';', 4)) - char_length(substring_index(member_type.extra, ';', 3)) - 1)
                                           as process_name
      from
        md_list_const member_type
      where
        member_type.classic_id = 30;
  END;
call initConst();

-- 可选标题栏
DROP PROCEDURE IF EXISTS `initConst`;
CREATE PROCEDURE `initConst`()
  BEGIN
    -- -- 常量
    delete from md_list_const where classic_id = 39;
    -- 项目列表标题栏
    -- 项目基本信息
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,0,'可选标题栏:1.关键字;2.名称','0000000000000:1-可隐藏,2-可排序,3-合作组织信息,4-任务负责人,5-设计人员,6-校对人员,7-审核人员,8-合同款,9-技术审查费,10-合作设计费收,11-其他费用收,12-合作设计费付,13-其他费用付;2.web可选标题栏类型;3.可选标题栏群组;4.过滤器类型;5.字段类型');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,4,':projectNo;项目编号','0000000000000;0,1;1;0;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,2,':projectName;项目名称','0000000000000;0,1;1;0;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,9,':createCompany;立项组织','0000000000000;0,1;1;2;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,1,':projectCreateDate;立项时间','1100000000000;0,1;1;4;2');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,7,':signDate;合同签订','1100000000000;0,1;1;4;2');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,3,':status;项目状态','1000000000000;0,1;1;2;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,10,':buildName;功能分类','1000000000000;0,1;1;3;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,11,':address;项目地点','1000000000000;0,1;1;5;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,5,':partA;甲方','1000000000000;0,1;1;2;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,6,':partB;乙方','1000000000000;0,1;1;2;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,12,':relationCompany;合作组织','1010000000000;0,1;1;2;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,8,':projectType;项目类型','1000000000000;0,1;1;2;1');
    -- 项目成员信息
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,13,':busPersonInCharge;经营负责人','1000000000000;0,1;2;2;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,14,':busPersonInChargeAssistant;经营助理','1000000000000;0,1;2;2;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,15,':designPersonInCharge;设计负责人','1000000000000;0,1;2;2;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,16,':designPersonInChargeAssistant;设计助理','1000000000000;0,1;2;2;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,17,':taskLeader;任务负责人','1001000000000;0,1;2;2;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,18,':designer;设计人员','1000100000000;0,1;2;2;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,19,':checker;校对人员','1000010000000;0,1;2;2;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,20,':auditor;审核人员','1000001000000;0,1;2;2;1');
    -- 项目收入情况
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,21,':contract;合同计划收款','1000000100000;0,1;3;0;3');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,22,':contractReal;合同到账信息','1000000100000;0,1;3;0;3');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,23,':technicalGain;技术审查费计划收款','1000000010000;0,1;3;0;3');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,24,':technicalGainReal;技术审查费到账金额','1000000010000;0,1;3;0;3');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,25,':cooperateGain;合作设计费计划收款','1000000001000;0,1;3;0;3');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,26,':cooperateGainReal;合作设计费到账金额','1000000001000;0,1;3;0;3');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,27,':otherGain;其他收入计划收款','1000000000100;0,1;3;0;4');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,28,':otherGainReal;其他收入到账金额','1000000000100;0,1;3;0;4');
    -- 项目支出情况
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,29,':technicalPay;技术审查费计划付款','1000000010000;0,1;4;0;3');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,30,':technicalPayReal;技术审查费付款金额','1000000010000;0,1;4;0;3');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,31,':cooperatePay;合作设计费计划付款','1000000000010;0,1;4;0;3');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,32,':cooperatePayReal;合作设计费付款金额','1000000000010;0,1;4;0;3');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,33,':otherPay;其他支出计划付款','1000000000001;0,1;4;0;4');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,34,':otherPayReal;其他支出付款金额','1000000000001;0,1;4;0;4');

    -- 发票汇总标题栏
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,35,':applyDate;申请日期','0000000000001;2;5;4;2');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,36,':companyUserName;申请人','0000000000001;2;5;1;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,37,':fee;金额','0000000000001;2;5;0;3');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,38,':invoiceTypeName;发票类型','1000000000001;2;5;2;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,39,':costTypeName;分类子项','1000000000001;2;5;3;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,40,':relationCompanyName;收票方名称','1000000000001;2;5;1;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,41,':taxIdNumber;纳税识别号','1000000000001;2;5;0;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,42,':feeDescription;开票内容','1000000000001;2;5;0;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,43,':projectName;关联项目','1000000000001;2;5;1;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (39,44,':invoiceNo;发票号码','0000000000001;2;5;1;1');

    -- -- 类型
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,39,'可选标题栏','md_type_optional_title');

    -- 视图
    CREATE OR REPLACE VIEW `md_type_optional_title` AS
      select
        option_title.code_id as id,
        option_title.code_id as type_id,
        option_title.title as type_title,
        substring(option_title.title,
                  char_length(substring_index(option_title.title,':',1))+2,
                  char_length(substring_index(option_title.title,';',1)) - char_length(substring_index(option_title.title,':',1))-1)
                             as type_code,
        substring(option_title.title,
                  char_length(substring_index(option_title.title,';',1))+2,
                  char_length(substring_index(option_title.title,';',2)) - char_length(substring_index(option_title.title,';',1))-1)
                             as type_name,
        option_title.extra as type_extra,
        substring(option_title.extra,
                  1,
                  char_length(substring_index(option_title.extra,';',1)))
                             as type_attr,
        substring(option_title.extra,1,1) as can_be_hide,
        substring(option_title.extra,2,1) as can_be_order,
        substring(option_title.extra,3,1) as is_relation_company,
        substring(option_title.extra,4,1) as is_task_leader,
        substring(option_title.extra,5,1) as is_task_designer,
        substring(option_title.extra,6,1) as is_task_checker,
        substring(option_title.extra,7,1) as is_task_auditor,
        substring(option_title.extra,8,1) as is_contract_fee,
        substring(option_title.extra,9,1) as is_technical_fee,
        substring(option_title.extra,10,1) as is_cooperate_gain_fee,
        substring(option_title.extra,11,1) as is_other_gain_fee,
        substring(option_title.extra,12,1) as is_cooperate_pay_fee,
        substring(option_title.extra,13,1) as is_other_pay_fee,
        substring(option_title.extra,
                  char_length(substring_index(option_title.extra,';',1))+2,
                  char_length(substring_index(option_title.extra,';',2)) - char_length(substring_index(option_title.extra,';',1))-1)
          as web_classic,
        substring(option_title.extra,
                  char_length(substring_index(option_title.extra,';',2))+2,
                  char_length(substring_index(option_title.extra,';',3)) - char_length(substring_index(option_title.extra,';',2))-1)
          as group_type,
        substring(option_title.extra,
                  char_length(substring_index(option_title.extra,';',3))+2,
                  char_length(substring_index(option_title.extra,';',4)) - char_length(substring_index(option_title.extra,';',3))-1)
          as filter_type,
        substring(option_title.extra,
                  char_length(substring_index(option_title.extra,';',4))+2,
                  char_length(substring_index(option_title.extra,';',5)) - char_length(substring_index(option_title.extra,';',4))-1)
          as field_type
      from
        md_list_const option_title
      where
        option_title.classic_id = 39;
  END;
call initConst();

-- 可选标题栏群组
DROP PROCEDURE IF EXISTS `initConst`;
CREATE PROCEDURE `initConst`()
  BEGIN
    -- -- 常量
    delete from md_list_const where classic_id = 40;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (40,0,'可选标题栏群组','1.保留;2.web可选标题栏类型');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (40,1,'项目基本信息',';0,1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (40,2,'项目成员信息',';0,1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (40,3,'项目收入情况',';0,1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (40,4,'项目支出情况',';0,1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (40,5,'发票信息',';2');

    -- -- 类型
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,40,'可选标题栏群组','md_type_optional_group');

    -- 视图
    CREATE OR REPLACE VIEW `md_type_optional_group` AS
      select
        optional_group.code_id as id,
        optional_group.code_id as type_id,
        optional_group.title as type_title,
        optional_group.extra as type_extra,
        substring(optional_group.extra,
                  char_length(substring_index(optional_group.extra,';',1))+2,
                  char_length(substring_index(optional_group.extra,';',2)) - char_length(substring_index(optional_group.extra,';',1))-1)
                               as web_classic
      from
        md_list_const optional_group
      where
        optional_group.classic_id = 40;
  END;
call initConst();

-- web可选标题栏类型
DROP PROCEDURE IF EXISTS `initConst`;
CREATE PROCEDURE `initConst`()
  BEGIN
    -- -- 常量
    delete from md_list_const where classic_id = 41;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (41,-1,'web可选标题栏类型','0:1-分组');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (41,0,'我的项目列表','1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (41,1,'项目总览列表','1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (41,2,'发票汇总列表','1');

    -- -- 类型
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,41,'web可选标题栏类型','md_type_web_title_classic');

    -- 视图
    CREATE OR REPLACE VIEW `md_type_web_title_classic` AS
      select
        web_title_classic.code_id as id,
        web_title_classic.code_id as type_id,
        web_title_classic.title as type_title,
        web_title_classic.extra as type_extra,
        substring(web_title_classic.extra,
                  1,
                  char_length(substring_index(web_title_classic.extra,';',1)))
          as type_attr,
        substring(web_title_classic.extra,1,1) as is_group
      from
        md_list_const web_title_classic
      where
        web_title_classic.classic_id = 41;
  END;
call initConst();

-- 标题过滤器类型
DROP PROCEDURE IF EXISTS `initConst`;
CREATE PROCEDURE `initConst`()
  BEGIN
    -- -- 常量
    delete from md_list_const where classic_id = 42;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (42,0,'标题过滤器类型','0:1-有列表过滤器');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (42,1,'字符串','0');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (42,2,'单选列表','1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (42,3,'多选列表','1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (42,4,'日期','0');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (42,5,'地点','0');

    -- -- 类型
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,42,'标题过滤器类型','md_type_filter');

    -- 视图
    CREATE OR REPLACE VIEW `md_type_filter` AS
      select
        filter_type.code_id as id,
        filter_type.code_id as type_id,
        filter_type.title as type_title,
        filter_type.extra as type_extra,
        substring(filter_type.extra,
                  1,
                  char_length(substring_index(filter_type.extra,';',1)))
                                               as type_attr,
        substring(filter_type.extra,1,1) as has_list
      from
        md_list_const filter_type
      where
        filter_type.classic_id = 42;
  END;
call initConst();

-- 列表字段类型
DROP PROCEDURE IF EXISTS `initConst`;
CREATE PROCEDURE `initConst`()
  BEGIN
    -- -- 常量
    delete from md_list_const where classic_id = 43;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (43,0,'列表字段类型','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (43,1,'字符串','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (43,2,'日期','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (43,3,'金额-万元','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (43,4,'金额-元','');

    -- -- 类型
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,43,'列表字段类型','md_type_field');

    -- 视图
    CREATE OR REPLACE VIEW `md_type_field` AS
      select
        filter_type.code_id as id,
        filter_type.code_id as type_id,
        filter_type.title as type_title,
        filter_type.extra as type_extra
      from
        md_list_const filter_type
      where
        filter_type.classic_id = 43;
  END;
call initConst();

-- 项目状态
DROP PROCEDURE IF EXISTS `initConst`;
CREATE PROCEDURE `initConst`()
  BEGIN
    -- -- 常量
    delete from md_list_const where classic_id = 44;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (44,-1,'项目状态','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (44,0,'进行中','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (44,1,'已暂停-未结清','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (44,2,'已完成-未结清','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (44,3,'已终止-未结清','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (44,4,'已完成-已结清','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (44,5,'已暂停-已结清','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (44,6,'已终止-已结清','');

    -- -- 类型
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,44,'项目状态','md_type_project_status');

    -- 视图
    CREATE OR REPLACE VIEW `md_type_project_status` AS
      select
        project_status.code_id as id,
        project_status.code_id as type_id,
        project_status.title   as type_title,
        project_status.extra   as type_extra
      from
        md_list_const project_status
      where
        project_status.classic_id = 44;
  END;
call initConst();

-- 发票类型
DROP PROCEDURE IF EXISTS `initConst`;
CREATE PROCEDURE `initConst`()
  BEGIN
    -- -- 常量
    delete from md_list_const where classic_id = 45;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (45,0,'发票类型','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (45,1,'普票','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (45,2,'专票','');

    -- -- 类型
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,45,'发票类型','md_type_invoice');

    -- 视图
    CREATE OR REPLACE VIEW `md_type_invoice` AS
      select
        invoice_type.code_id as id,
        invoice_type.code_id as type_id,
        invoice_type.title   as type_title,
        invoice_type.title   as type_name,
        invoice_type.extra   as type_extra
      from
        md_list_const invoice_type
      where
        invoice_type.classic_id = 45;
  END;
call initConst();

-- 款项分类子项
DROP PROCEDURE IF EXISTS `initConst`;
CREATE PROCEDURE `initConst`()
  BEGIN
    -- -- 常量
    delete from md_list_const where classic_id = 46;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (46,0,'款项分类子项','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (46,1,'合同回款','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (46,2,'技术审查费','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (46,3,'合作设计费','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (46,4,'其他付款','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (46,5,'其他收款','');

    -- -- 类型
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,46,'款项分类子项','md_type_fee');

    -- 视图
    CREATE OR REPLACE VIEW `md_type_fee` AS
      select
        invoice_type.code_id as id,
        invoice_type.code_id as type_id,
        invoice_type.title   as type_title,
        invoice_type.title   as type_name,
        invoice_type.extra   as type_extra
      from
        md_list_const invoice_type
      where
        invoice_type.classic_id = 46;
  END;
call initConst();

-- 审批类别
DROP PROCEDURE IF EXISTS `initConst`;
CREATE PROCEDURE `initConst`()
  BEGIN
    -- -- 常量
    delete from md_list_const where classic_id = 47;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (47,0,'审批类别','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (47,1,'报销申请','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (47,2,'费用申请','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (47,3,'请假','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (47,4,'出差','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (47,5,'项目费用申请','');

    -- -- 类型
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,47,'审批类别','md_type_exp');

    -- 视图
    CREATE OR REPLACE VIEW `md_type_exp` AS
      select
        exp_type.code_id as id,
        exp_type.code_id as type_id,
        exp_type.title   as type_title,
        exp_type.title   as type_name,
        exp_type.extra   as type_extra
      from
        md_list_const exp_type
      where
        exp_type.classic_id = 47;
  END;
call initConst();

-- 审批状态
DROP PROCEDURE IF EXISTS `initConst`;
CREATE PROCEDURE `initConst`()
  BEGIN
    -- -- 常量
    delete from md_list_const where classic_id = 48;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (48,-1,'审批状态','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (48,0,'待审核','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (48,1,'已完成','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (48,2,'已退回','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (48,3,'已撤回','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (48,4,'已删除','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (48,5,'审批中','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (48,6,'财务已拨款','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (48,7,'财务拒绝拨款','');

    -- -- 类型
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,48,'审批状态','md_type_exp_status');

    -- 视图
    CREATE OR REPLACE VIEW `md_type_exp_status` AS
      select
        exp_type_status.code_id as id,
        exp_type_status.code_id as type_id,
        exp_type_status.title   as type_title,
        exp_type_status.title   as type_name,
        exp_type_status.extra   as type_extra
      from
        md_list_const exp_type_status
      where
        exp_type_status.classic_id = 48;
  END;
call initConst();

-- 自定义表单可选控件
DROP PROCEDURE IF EXISTS `initConst`;
CREATE PROCEDURE `initConst`()
  BEGIN
    -- -- 常量
    delete from md_list_const where classic_id = 49;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,0,'可选控件',  '000000000:1-允许输入,2-允许字母,3-允许多行,4-允许格式,5-是日期,6-可多选,7-可上传,8-可链接,9-可嵌套;2:可设置属性;3:图标');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,1,'单行文本',  '110000000;1,2,3;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,2,'多行文本',  '111000000;1,2,3;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,3,'日期',     '000010000;1,2,3;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,4,'日期区间',  '000010000;10,11,12,13,14,3;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,5,'数字',     '100000000;1,2,3;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,6,'金额',     '100000000;1,2,6,3;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,7,'单选框',   '000000000;1,2,4,5,3;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,8,'复选框',   '000001000;1,2,4,5,3;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,9,'下拉列表', '000000000;1,2,4,5,3;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,10,'富文本',  '111100000;1,2,3;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,11,'纯文本',  '111000000;1,2,3;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,12,'明细',   '000000001;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,13,'图片',   '000000100;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,14,'附件',   '000000100;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,15,'分割线', '000000000;;');

    -- -- 类型
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,49,'可选控件','md_type_widget');

    -- 视图
    CREATE OR REPLACE VIEW `md_type_widget` AS
      select
        widget_type.code_id as type_id,
        widget_type.title   as type_name,
        substring(widget_type.extra,
                  1,
                  char_length(substring_index(widget_type.extra,';',1)))
                                           as type_attr,
        substring(widget_type.extra,1,1) as allow_input,
        substring(widget_type.extra,2,1) as allow_alpha,
        substring(widget_type.extra,3,1) as allow_crlf,
        substring(widget_type.extra,4,1) as allow_format,
        substring(widget_type.extra,5,1) as is_time,
        substring(widget_type.extra,6,1) as allow_multi,
        substring(widget_type.extra,7,1) as allow_upload,
        substring(widget_type.extra,8,1) as allow_link,
        substring(widget_type.extra,9,1) as allow_nest,
        substring(widget_type.extra,
                  char_length(substring_index(widget_type.extra,';',1))+2,
                  char_length(substring_index(widget_type.extra,';',2)) - char_length(substring_index(widget_type.extra,';',1))-1)
                                           as set_type,
        substring(widget_type.extra,
                  char_length(substring_index(widget_type.extra,';',2))+2,
                  char_length(substring_index(widget_type.extra,';',3)) - char_length(substring_index(widget_type.extra,';',2))-1)
                                           as icon
      from
        md_list_const widget_type
      where
        widget_type.classic_id = 49;
  END;
call initConst();

-- 自定义表单控件可设置属性
DROP PROCEDURE IF EXISTS `initConst`;
CREATE PROCEDURE `initConst`()
  BEGIN
    -- -- 常量
    delete from md_list_const where classic_id = 50;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (50,0,'可设置属性:1.名称;2.显示名称','00:1-允许为空,2-控件名称作为默认值;2.类型;3.默认值;4.可选值');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (50,1,':标题;标题',  '11;1;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (50,2,':提示文字;提示文字',  '10;1;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (50,3,':是否必填;是否必填',  '10;3;;必填');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (50,4,':可选项;提示文字',   '10;4;;选项1,选项2');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (50,5,':排列方式;排列方式',  '10;2;;横向,纵向');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (50,6,':单位;单位',   '10;1;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (50,10,':开始时间标题;标题1', '10;1;开始时间;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (50,11,':开始时间提示;提示文字1',  '10;1;开始时间;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (50,12,':结束时间标题;标题2',  '10;1;结束时间;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (50,13,':结束时间提示;提示文字2',   '10;1;结束时间;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (50,14,':时间格式;日期类型',   '10;2;;年/月/日,年/月/日 时:分,年/月/日 上午&下午');

    -- -- 类型
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,50,'可设置属性','md_type_widget_property,md_widget_property');

    -- 视图
    CREATE OR REPLACE VIEW `md_type_widget_property` AS
      select
        property_type.code_id as type_id,
        substring(property_type.title,
                  char_length(substring_index(property_type.title,':',1))+2,
                  char_length(substring_index(property_type.title,';',1)) - char_length(substring_index(property_type.title,':',1))-1)
          as type_code,
        substring(property_type.title,
                  char_length(substring_index(property_type.title,';',1))+2,
                  char_length(substring_index(property_type.title,';',2)) - char_length(substring_index(property_type.title,';',1))-1)
          as type_name,
        substring(property_type.extra,
                  1,
                  char_length(substring_index(property_type.extra,';',1)))
                                           as type_attr,
        substring(property_type.extra,1,1) as allow_null,
        substring(property_type.extra,2,1) as is_name_default,
        substring(property_type.extra,
                  char_length(substring_index(property_type.extra,';',1))+2,
                  char_length(substring_index(property_type.extra,';',2)) - char_length(substring_index(property_type.extra,';',1))-1)
                                           as input_type,
        substring(property_type.extra,
                  char_length(substring_index(property_type.extra,';',2))+2,
                  char_length(substring_index(property_type.extra,';',3)) - char_length(substring_index(property_type.extra,';',2))-1)
                                           as default_value,
        substring(property_type.extra,
                  char_length(substring_index(property_type.extra,';',3))+2,
                  char_length(substring_index(property_type.extra,';',4)) - char_length(substring_index(property_type.extra,';',3))-1)
                                           as allow_value
      from
        md_list_const property_type
      where
        property_type.classic_id = 50;
  END;
call initConst();

-- 控件可设置属性类型
DROP PROCEDURE IF EXISTS `initConst`;
CREATE PROCEDURE `initConst`()
  BEGIN
    -- -- 常量
    delete from md_list_const where classic_id = 51;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (51,0,'控件可设置属性类型','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (51,1,'文本框',  '');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (51,2,'单选框',  '');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (51,3,'多选框',  '');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (51,4,'选项列表',  '');

    -- -- 类型
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,51,'控件可设置属性类型','md_type_widget_property_type');

    -- 视图
    CREATE OR REPLACE VIEW `md_type_widget_property_type` AS
      select
        input_type.code_id as type_id,
        input_type.title   as type_name
      from
        md_list_const input_type
      where
        input_type.classic_id = 51;
  END;
call initConst();

-- 表单模板
DROP PROCEDURE IF EXISTS `initConst`;
CREATE PROCEDURE `initConst`()
  BEGIN
    -- -- 常量
    delete from md_list_const where classic_id = 52;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (52,0,'表单模板','0:1-明细表单');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (52,1,'请假申请',  '0');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (52,2,'出差申请',  '0');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (52,3,'报销申请',  '0');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (52,4,'报销申请-明细',  '1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (52,5,'费用申请',  '0');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (52,6,'费用申请-明细',  '1');

    -- -- 类型
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,52,'表单模板','md_type_form');

    -- 视图
    CREATE OR REPLACE VIEW `md_type_form` AS
      select
        form_type.code_id as type_id,
        form_type.title   as type_name,
        substring(form_type.extra,
                  1,
                  char_length(substring_index(form_type.extra,';',1)))
                                           as type_attr,
        substring(form_type.extra,1,1) as is_detail
      from
        md_list_const form_type
      where
        form_type.classic_id = 52;
  END;
call initConst();

-- 表单模板字段
DROP PROCEDURE IF EXISTS `initConst`;
CREATE PROCEDURE `initConst`()
  BEGIN
    -- -- 常量
    delete from md_list_const where classic_id = 53;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,0,'表单模板字段:1-名称;2-显示标题',        '0:1-必填;2.所属表单;3.横轴排序;4.纵轴排序;5.控件类型;6.默认值;7.格式;8.提示文字;9.固定可选项;10.查询请求;11.嵌套表单');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,1,':请假申请-请假类型;请假类型',           '1;1;0;1;7;;;;;iWork/leave/getLeaveType;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,2,':请假申请-开始结束时间;开始时间,结束时间','1;1;0;2;4;;年/月/日 时:分;;;;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,3,':请假申请-请假天数;请假天数',           '1;1;0;3;5;;;;;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,4,':请假申请-请假事由;请假事由',           '0;1;0;4;2;;;;;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,5,':请假申请-附件;附件',                 '0;1;0;5;14;;;;;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,6,':出差申请-出差地点;出差地点',           '1;2;0;1;1;;;;;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,7,':出差申请-开始结束时间;开始时间,结束时间','1;2;0;2;4;;年/月/日 时:分;;;;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,8,':出差申请-出差天数;出差天数',           '1;2;0;3;5;;;;;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,9,':出差申请-出差事由;出差事由',           '0;2;0;4;2;;;;;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,10,':出差申请-关联项目;关联项目',          '0;2;0;5;9;;;;;iWork/finance/getProjectList;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,11,':出差申请-附件;附件',                 '0;2;0;6;14;;;;;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,12,':报销申请-报销明细;报销明细',           '0;3;0;1;12;;;;;;4');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,13,':报销申请-附件;附件',                 '0;3;0;2;14;;;;;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,14,':报销申请明细-报销金额;报销金额',       '1;4;0;1;6;;;;;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,15,':报销申请明细-报销类型;报销类型',       '1;4;0;2;9;;;;;iWork/finance/getExpBaseData;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,16,':报销申请明细-用途说明;用途说明',       '1;4;0;3;2;;;;;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,17,':报销申请明细-关联项目;关联项目',       '0;4;0;4;9;;;;;iWork/finance/getExpBaseData;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,18,':报销申请明细-关联审批;关联审批',       '0;4;0;5;9;;;;;iWork/finance/getExpBaseData;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,19,':费用申请-收款方;收款方',             '0;5;0;1;1;;;;;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,20,':费用申请-备注;备注',                '0;5;0;2;2;;;;;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,21,':费用申请-费用明细;费用明细',          '0;5;0;3;12;;;;;;6');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,22,':费用申请明细-费用金额;费用金额',       '1;6;0;1;6;;;;;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,23,':费用申请明细-费用类型;费用类型',       '1;6;0;2;9;;;;;iWork/finance/getExpBaseData;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,24,':费用申请明细-用途说明;用途说明',       '1;6;0;3;2;;;;;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,25,':费用申请明细-关联项目;关联项目',       '0;6;0;4;9;;;;;iWork/finance/getExpBaseData;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,26,':费用申请明细-关联审批;关联审批',       '0;6;0;5;9;;;;;iWork/finance/getExpBaseData;');

    -- -- 类型
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,53,'表单模板组件','md_type_form_field,md_form_field');

    -- 视图
    CREATE OR REPLACE VIEW `md_type_form_field` AS
      select
        field_type.code_id as type_id,
        substring(field_type.title,
                  char_length(substring_index(field_type.title,':',1))+2,
                  char_length(substring_index(field_type.title,';',1)) - char_length(substring_index(field_type.title,':',1))-1)
          as type_code,
        substring(field_type.title,
                  char_length(substring_index(field_type.title,';',1))+2,
                  char_length(substring_index(field_type.title,';',2)) - char_length(substring_index(field_type.title,';',1))-1)
          as type_name,
        substring(field_type.extra,
                  1,
                  char_length(substring_index(field_type.extra,';',1)))
                          as type_attr,
        substring(field_type.extra,1,1) as required_type,
        substring(field_type.extra,
                  char_length(substring_index(field_type.extra,';',1))+2,
                  char_length(substring_index(field_type.extra,';',2)) - char_length(substring_index(field_type.extra,';',1))-1)
          as direct_form_id,
        substring(field_type.extra,
                  char_length(substring_index(field_type.extra,';',2))+2,
                  char_length(substring_index(field_type.extra,';',3)) - char_length(substring_index(field_type.extra,';',2))-1)
          as xpos,
        substring(field_type.extra,
                  char_length(substring_index(field_type.extra,';',3))+2,
                  char_length(substring_index(field_type.extra,';',4)) - char_length(substring_index(field_type.extra,';',3))-1)
          as ypos,
        substring(field_type.extra,
                  char_length(substring_index(field_type.extra,';',4))+2,
                  char_length(substring_index(field_type.extra,';',5)) - char_length(substring_index(field_type.extra,';',4))-1)
          as field_type,
        substring(field_type.extra,
                  char_length(substring_index(field_type.extra,';',5))+2,
                  char_length(substring_index(field_type.extra,';',6)) - char_length(substring_index(field_type.extra,';',5))-1)
          as field_default_value,
        substring(field_type.extra,
                  char_length(substring_index(field_type.extra,';',6))+2,
                  char_length(substring_index(field_type.extra,';',7)) - char_length(substring_index(field_type.extra,';',6))-1)
          as value_format,
        substring(field_type.extra,
                  char_length(substring_index(field_type.extra,';',7))+2,
                  char_length(substring_index(field_type.extra,';',8)) - char_length(substring_index(field_type.extra,';',7))-1)
          as field_tooltip,
        substring(field_type.extra,
                  char_length(substring_index(field_type.extra,';',8))+2,
                  char_length(substring_index(field_type.extra,';',9)) - char_length(substring_index(field_type.extra,';',8))-1)
          as optional_text,
        substring(field_type.extra,
                  char_length(substring_index(field_type.extra,';',9))+2,
                  char_length(substring_index(field_type.extra,';',10)) - char_length(substring_index(field_type.extra,';',9))-1)
          as request_url,
        substring(field_type.extra,
                  char_length(substring_index(field_type.extra,';',10))+2,
                  char_length(substring_index(field_type.extra,';',11)) - char_length(substring_index(field_type.extra,';',10))-1)
          as detail_id
      from
        md_list_const field_type
      where
        field_type.classic_id = 53;
  END;
call initConst();

-- -- -- 创建及更改常量 -- 结束 -- -- --

-- -- -- 创建及更改视图 -- 开始 -- -- --
-- 表单模板字段定义
CREATE OR REPLACE VIEW `md_form_field` AS
  select
    concat(form_type.type_id,'-',field_type.type_id) as id,
    form_type.type_id as form_id,
    0 as is_detail,
    field_type.*
  from
    md_type_form_field field_type
    inner join md_type_form form_type on (concat(form_type.type_id,'') = field_type.direct_form_id)
  
  union all

  select
    concat(form_type.type_id,'-',field_type.type_id) as id,
    form_type.type_id as form_id,
    1 as is_detail,
    field_type.*
  from
    md_type_form_field top_field_type
    inner join md_type_form_field field_type on (field_type.direct_form_id = top_field_type.detail_id)
    inner join md_type_form form_type on (form_type.type_id = top_field_type.direct_form_id);

-- 控件可设置属性
CREATE OR REPLACE VIEW `md_widget_property` AS
  select
    concat(widget_list.type_id,'-',property_type.type_id) as id,
    if(property_type.is_name_default = 1,widget_list.type_name,property_type.default_value) as property_default_value,
    widget_list.type_id as widget_id,
    widget_list.type_name as widget_name,
    input_type.type_name as input_type_name,
    property_type.*
  from
    md_type_widget_property property_type
    inner join md_type_widget widget_list on (find_in_set(property_type.type_id,widget_list.set_type))
    inner join md_type_widget_property_type input_type on (input_type.type_id = property_type.input_type);

-- 默认项目模板名称定义视图
CREATE OR REPLACE VIEW `md_type_template_const` AS
  select
    concat(template_type.classic_id,'-',template_type.code_id) as id,
    null as company_id,
    null as project_id,
    null as task_id,
    template_type.code_id as type_id,
    template_type.title as type_name
  from
    md_list_const template_type
  where template_type.classic_id = 38;

-- 默认项目模板内容定义视图
CREATE OR REPLACE VIEW `md_type_template_content_const` AS
  select
    concat(template_type.classic_id,'-',template_type.code_id) as id,
    null as company_id,
    null as project_id,
    null as task_id,
    concat(substring(template_type.extra,
                     1,char_length(substring_index(template_type.extra,';',1))),
           '-',template_type.code_id)
      as type_id,
    template_type.title as type_name,
    substring(template_type.title,
              1,char_length(substring_index(template_type.title,';',1)))
      as content_name,
    substring(template_type.title,
              char_length(substring_index(template_type.title,';',1))+2,
              char_length(substring_index(template_type.title,';',2)) - char_length(substring_index(template_type.title,';',1))-1)
      as template_name,
    substring(template_type.extra,
              1,char_length(substring_index(template_type.extra,';',1)))
      as template_id,
    substring(template_type.extra,
              char_length(substring_index(template_type.extra,';',1))+2,
              char_length(substring_index(template_type.extra,';',2)) - char_length(substring_index(template_type.extra,';',1))-1)
      as x_pos,
    substring(template_type.extra,
              char_length(substring_index(template_type.extra,';',2))+2,
              char_length(substring_index(template_type.extra,';',3)) - char_length(substring_index(template_type.extra,';',2))-1)
      as y_pos,
    substring(template_type.extra,
              char_length(substring_index(template_type.extra,';',3))+2,
              char_length(substring_index(template_type.extra,';',4)) - char_length(substring_index(template_type.extra,';',3))-1)
      as content_type,
    substring(template_type.extra,
              char_length(substring_index(template_type.extra,';',4))+2,
              char_length(substring_index(template_type.extra,';',5)) - char_length(substring_index(template_type.extra,';',4))-1)
      as detail_type,
    substring(template_type.extra,
              char_length(substring_index(template_type.extra,';',5))+2,
              char_length(substring_index(template_type.extra,';',6)) - char_length(substring_index(template_type.extra,';',5))-1)
      as detail_range,
    substring(template_type.extra,
              char_length(substring_index(template_type.extra,';',6))+2,
              char_length(substring_index(template_type.extra,';',7)) - char_length(substring_index(template_type.extra,';',6))-1)
      as detail_var_name
  from
    md_list_const template_type
  where template_type.classic_id = 37;

-- 默认项目模板基本类型定义视图
CREATE OR REPLACE VIEW `md_template_content_const` AS
  select
    concat(template_type.id,'-',content_type.code_id) as id,
    template_type.company_id,
    template_type.project_id,
    template_type.task_id,
    template_type.type_id,
    template_type.type_name,
    template_type.template_id,
    template_type.template_name,
    template_type.content_type,
    template_type.content_name,
    template_type.x_pos,
    template_type.y_pos,
    if(template_type.content_type=33,1,0) as is_function,
    if(template_type.content_type=34,1,0) as is_measure,
    if(template_type.content_type=35,1,0) as is_range,
    if(template_type.content_type=36,template_type.type_id,content_type.code_id) as detail_id,
    if(template_type.content_type=36,template_type.content_name,content_type.title) as detail_name,
    if(template_type.content_type=36,template_type.detail_range,
      substring(content_type.extra,char_length(substring_index(content_type.extra,';',1))+2,
        char_length(substring_index(content_type.extra,';',2)) - char_length(substring_index(content_type.extra,';',1))-1))
      as detail_range,
    if(template_type.content_type=36,template_type.detail_var_name,
      substring(content_type.extra,char_length(substring_index(content_type.extra,';',2))+2,
        char_length(substring_index(content_type.extra,';',3)) - char_length(substring_index(content_type.extra,';',2))-1))
      as detail_var_name,
    detail_type.code_id as detail_type_id,
    detail_type.title as detail_type_name,
    detail_type.extra as detail_unit
  from
    md_type_template_content_const template_type
    inner join md_list_const content_type on (content_type.classic_id = template_type.content_type
      and find_in_set(content_type.code_id,template_type.detail_type))
    inner join md_list_const detail_type on (detail_type.classic_id = 36
      and detail_type.code_id = if(template_type.content_type=36,
        template_type.detail_type,
        substring(content_type.extra,1,char_length(substring_index(content_type.extra,';',1)))));

-- 项目模板默认功能分类视图
CREATE OR REPLACE VIEW `md_type_template_built_type` AS
  select
    built_type.id,
    built_type.company_id,
    built_type.project_id,
    built_type.task_id,
    built_type.code_id as type_id,
    built_type.title as type_name
  from
    md_list_const_custom template_type
    inner join md_list_const_custom built_type on (built_type.classic_id = 33
        and find_in_set(built_type.code_id,template_type.extra))
  where template_type.classic_id = 33;

-- 默认功能分类视图
CREATE OR REPLACE VIEW `md_type_built_const` AS
  select
    built_type.id,
    null as company_id,
    null as project_id,
    null as task_id,
    built_type.id as type_id,
    built_type.name as type_name
  from
    maoding_data_dictionary built_type
  where
    deleted = 0
    and pid = (
      select id from maoding_data_dictionary
      where code='zp-jzgn')
  order by built_type.seq;

-- 自定义功能分类视图
CREATE OR REPLACE VIEW `md_type_built_custom` AS
  select
    built_type.id,
    built_type.company_id,
    built_type.project_id,
    built_type.task_id,
    built_type.id as type_id,
    built_type.title as type_name
  from
    md_list_const_custom built_type
  where deleted = 0 and built_type.classic_id = 33;

-- 功能分类
CREATE OR REPLACE VIEW `md_build_type` AS
  select
    build_type.id,
    null            as company_id,
    null            as project_id,
    null            as task_id,
    build_type.id   as type_id,
    build_type.name as type_name
  from
    maoding_data_dictionary build_type
  where
    build_type.deleted = 0 and pid = (
      select id from maoding_data_dictionary
      where code='zp-jzgn')

  union all

  select
    build_type.id,
    build_type.company_id,
    build_type.project_id,
    build_type.task_id,
    build_type.id as type_id,
    build_type.title as type_name
  from
    md_list_const_custom build_type
  where build_type.deleted = 0 and build_type.classic_id = 33;


-- 交付视图
CREATE OR REPLACE VIEW `md_deliver` AS
  select
    deliver_task.id, -- 交付任务编号
    deliver_task.task_title as name, -- 交付任务名称
    deliver_task.task_content as description, -- 说明
    deliver_task.deadline as end_time, -- 截止时间
    deliver_task.create_by, -- 发起人
    deliver_task.create_date, -- 发起时间
    if(deliver_task.complete_date is null,'0','1') as is_finished, -- 是否已经结束
    account.id as response_id, -- 负责人编号
    account.user_name as response_name, -- 负责人姓名
    if(response_task.complete_date is null,'0','1') as response_is_finised -- 负责人是否已结束任务
  from maoding_web_my_task deliver_task -- 交付任务
    inner join maoding_web_my_task response_task on (response_task.target_id = deliver_task.id and response_task.task_type = 26) -- 负责人任务
    inner join maoding_web_company_user company_user on (company_user.id = response_task.handler_id) -- 负责人角色
    inner join maoding_web_account account on (account.id = company_user.user_id) -- 用户名
  where deliver_task.task_type = 28;

-- 文件类型定义视图
CREATE OR REPLACE VIEW `md_type_file` AS
  select
    file_type.code_id as type_id,
    file_type.title as type_name,
    substring(file_type.extra,
              1,char_length(substring_index(file_type.extra,';',1)))
        as attr_str,
    substring(file_type.extra,1,1) as is_mirror
  from
    md_list_const file_type
  where (file_type.classic_id = 5);

-- 专业类型定义视图
CREATE OR REPLACE VIEW `md_type_web_major` AS
  select
    major_type.code_id as type_id,
    major_type.title as type_name
  from
    md_list_const major_type
  where (major_type.classic_id = 22);

-- 服务器类型定义视图
CREATE OR REPLACE VIEW `md_type_server` AS
  select
    server_type.code_id as type_id,
    server_type.title as type_name,
    replace(substring(server_type.extra,
              1,char_length(substring_index(server_type.extra,';',1))),'|',';')
      as default_server_address,
    substring(server_type.extra,
              char_length(substring_index(server_type.extra,';',1))+2,
              char_length(substring_index(server_type.extra,';',2)) - char_length(substring_index(server_type.extra,';',1))-1)
      as default_base_dir
  from
    md_list_const server_type
  where (server_type.classic_id = 19 and server_type.code_id != 0);

-- 文件操作定义视图
CREATE OR REPLACE VIEW `md_type_action` AS
  select
    action_type.code_id as type_id,
    action_type.title as type_name,
    substring(action_type.extra,
              1,char_length(substring_index(action_type.extra,';',1)))
        as attr_str,
    substring(action_type.extra,1,1) as is_ca,
    substring(action_type.extra,2,1) as is_commit,
    substring(action_type.extra,
              char_length(substring_index(action_type.extra,';',1))+2,
              char_length(substring_index(action_type.extra,';',2)) - char_length(substring_index(action_type.extra,';',1))-1)
        as node_type,
    substring(action_type.extra,
              char_length(substring_index(action_type.extra,';',2))+2,
              char_length(substring_index(action_type.extra,';',3)) - char_length(substring_index(action_type.extra,';',2))-1)
        as node_path,
    substring(action_type.extra,
              char_length(substring_index(action_type.extra,';',3))+2,
              char_length(substring_index(action_type.extra,';',4)) - char_length(substring_index(action_type.extra,';',3))-1)
        as server_type,
    substring(action_type.extra,
              char_length(substring_index(action_type.extra,';',4))+2,
              char_length(substring_index(action_type.extra,';',5)) - char_length(substring_index(action_type.extra,';',4))-1)
        as server_address,
    substring(action_type.extra,
              char_length(substring_index(action_type.extra,';',5))+2,
              char_length(substring_index(action_type.extra,';',6)) - char_length(substring_index(action_type.extra,';',5))-1)
        as base_dir,
    substring(action_type.extra,
              char_length(substring_index(action_type.extra,';',6))+2,
              char_length(substring_index(action_type.extra,';',7)) - char_length(substring_index(action_type.extra,';',6))-1)
        as notice_type
  from
    md_list_const action_type
  where
    (action_type.classic_id = 20) and (action_type.code_id != 0);

-- web公司角色类型定义视图
CREATE OR REPLACE VIEW `md_type_web_role_company` AS
  select
    web_role_type.code_id as type_id,
    web_role_type.title as type_name,
    substring(web_role_type.extra,
              1,
              char_length(substring_index(web_role_type.extra,';',1)))
      as attr_str,
    substring(web_role_type.extra,1,1) as is_plus_offset,
    substring(web_role_type.extra,
              char_length(substring_index(web_role_type.extra,';',1))+2,
              char_length(substring_index(web_role_type.extra,';',2)) - char_length(substring_index(web_role_type.extra,';',1))-1)
      as code_id,
    substring(web_role_type.extra,
              char_length(substring_index(web_role_type.extra,';',2))+2,
              char_length(substring_index(web_role_type.extra,';',3)) - char_length(substring_index(web_role_type.extra,';',2))-1)
      as role_type,
    substring(web_role_type.extra,
              char_length(substring_index(web_role_type.extra,';',3))+2,
              char_length(substring_index(web_role_type.extra,';',4)) - char_length(substring_index(web_role_type.extra,';',3))-1)
      as need_permission
  from
    md_list_const web_role_type
  where
    web_role_type.classic_id = 29;

-- web项目角色类型定义视图
CREATE OR REPLACE VIEW `md_type_web_role_project` AS
  select
    web_role_type.code_id as type_id,
    web_role_type.title as type_name,
    substring(web_role_type.extra,
              1,
              char_length(substring_index(web_role_type.extra,';',1)))
      as attr_str,
    substring(web_role_type.extra,1,1) as is_project_role,
    substring(web_role_type.extra,2,1) as is_task_role,
    substring(web_role_type.extra,3,1) as is_task_leader,
    substring(web_role_type.extra,4,1) as is_task_designer,
    substring(web_role_type.extra,5,1) as is_task_checker,
    substring(web_role_type.extra,6,1) as is_task_auditor,
    substring(web_role_type.extra,
              char_length(substring_index(web_role_type.extra,';',1))+2,
              char_length(substring_index(web_role_type.extra,';',2)) - char_length(substring_index(web_role_type.extra,';',1))-1)
      as role_type,
    substring(web_role_type.extra,
              char_length(substring_index(web_role_type.extra,';',2))+2,
              char_length(substring_index(web_role_type.extra,';',3)) - char_length(substring_index(web_role_type.extra,';',2))-1)
      as mytask_task_type,
    substring(web_role_type.extra,
              char_length(substring_index(web_role_type.extra,';',3))+2,
              char_length(substring_index(web_role_type.extra,';',4)) - char_length(substring_index(web_role_type.extra,';',3))-1)
      as process_name
  from
    md_list_const web_role_type
  where
    web_role_type.classic_id = 30;

-- skydrive上文档分类定义视图
CREATE OR REPLACE VIEW `md_type_sky_drive` AS
  select
    sky_drive_type.code_id as type_id,
    sky_drive_type.title as type_name,
    substring(sky_drive_type.extra,
              1,
              char_length(substring_index(sky_drive_type.extra,';',1)))
      as dir_node_type,
    substring(sky_drive_type.extra,
              char_length(substring_index(sky_drive_type.extra,';',1))+2,
              char_length(substring_index(sky_drive_type.extra,';',2)) - char_length(substring_index(sky_drive_type.extra,';',1))-1)
      as file_node_type
  from
    md_list_const sky_drive_type
  where
    sky_drive_type.classic_id = 21;

-- 文档分类定义视图
CREATE OR REPLACE VIEW `md_type_range` AS
  select
    range_type.code_id as type_id,
    range_type.title as type_name,
    substring(range_type.extra,
              1,
              char_length(substring_index(range_type.extra,';',1)))
                       as attr_str,
    substring(range_type.extra,1,1) as is_root_range,
    substring(range_type.extra,2,1) as is_project_range,
    substring(range_type.extra,3,1) as is_display,
    substring(range_type.extra,
              char_length(substring_index(range_type.extra,';',1))+2,
              char_length(substring_index(range_type.extra,';',2)) - char_length(substring_index(range_type.extra,';',1))-1)
                       as node_type
  from
    md_list_const range_type
  where
    range_type.classic_id = 24;

-- 节点类型定义视图
CREATE OR REPLACE VIEW `md_type_node` AS
  select
    node_type.code_id as type_id,
    node_type.title as type_name,
    substring(node_type.extra,
              1,
              char_length(substring_index(node_type.extra,';',1)))
                       as attr_str,
    substring(node_type.extra,1,1) as is_directory,
    substring(node_type.extra,2,1) as is_project,
    substring(node_type.extra,3,1) as is_issue,
    substring(node_type.extra,4,1) as is_task,
    substring(node_type.extra,5,1) as is_design,
    substring(node_type.extra,6,1) as is_ca,
    substring(node_type.extra,7,1) as is_commit,
    substring(node_type.extra,8,1) as is_web,
    substring(node_type.extra,9,1) as is_history,
    substring(node_type.extra,
              char_length(substring_index(node_type.extra,';',1))+2,
              char_length(substring_index(node_type.extra,';',2)) - char_length(substring_index(node_type.extra,';',1))-1)
                       as sub_dir_type,
    substring(node_type.extra,
              char_length(substring_index(node_type.extra,';',2))+2,
              char_length(substring_index(node_type.extra,';',3)) - char_length(substring_index(node_type.extra,';',2))-1)
                       as sub_file_type,
    substring(node_type.extra,
              char_length(substring_index(node_type.extra,';',3))+2,
              char_length(substring_index(node_type.extra,';',4)) - char_length(substring_index(node_type.extra,';',3))-1)
                       as owner_role_type,
    substring(node_type.extra,
              char_length(substring_index(node_type.extra,';',4))+2,
              char_length(substring_index(node_type.extra,';',5)) - char_length(substring_index(node_type.extra,';',4))-1)
                       as range_id
  from
    md_list_const node_type
  where
    node_type.classic_id = 14;

-- task任务类型定义视图
CREATE OR REPLACE VIEW `md_type_web_task` AS
  select
    web_task_type.code_id as type_id,
    web_task_type.title as type_name,
    substring(web_task_type.extra,
              1,
              char_length(substring_index(web_task_type.extra,';',1)))
                       as attr_str,
    substring(web_task_type.extra,1,1) as is_issue,
    substring(web_task_type.extra,2,1) as is_task,
    substring(web_task_type.extra,
              char_length(substring_index(web_task_type.extra,';',1))+2,
              char_length(substring_index(web_task_type.extra,';',2)) - char_length(substring_index(web_task_type.extra,';',1))-1)
                       as node_type_offset,
    substring(web_task_type.extra,
              char_length(substring_index(web_task_type.extra,';',2))+2,
              char_length(substring_index(web_task_type.extra,';',3)) - char_length(substring_index(web_task_type.extra,';',2))-1)
                       as in_range
  from
    md_list_const web_task_type
  where
    web_task_type.classic_id = 23;

-- 权限定义视图
CREATE OR REPLACE VIEW `md_type_permission` AS
  select
    permission_type.code_id as type_id,
    substring(permission_type.title,locate(';',permission_type.title) + 1) AS type_name,
    substring(permission_type.title,1,locate(';',permission_type.title) - 1) AS code_id
  from
    md_list_const permission_type
  where
    permission_type.classic_id = 1;

-- 角色定义视图
CREATE OR REPLACE VIEW `md_type_role` AS
  select
    role_type.code_id as type_id,
    substring(role_type.title,locate(';',role_type.title) + 1) as type_name,
    substring(role_type.title,1,locate(';',role_type.title) - 1) as code_id,
    substring(role_type.extra,
              1,
              char_length(substring_index(role_type.extra,';',1)))
      as manage_role,
    substring(role_type.extra,
              char_length(substring_index(role_type.extra,';',1))+2,
              char_length(substring_index(role_type.extra,';',2)) - char_length(substring_index(role_type.extra,';',1))-1)
      as have_permission
  from
    md_list_const role_type
  where
    role_type.classic_id = 26;

-- 角色及可管理角色
CREATE OR REPLACE VIEW `md_role_and_sub_role` AS
  select
    role_type.*,
    sub_role_type.type_id as sub_role_type_id,
    sub_role_type.type_name as sub_role_type_name,
    sub_role_type.code_id as sub_role_code_id,
    sub_role_type.have_permission as sub_role_have_permission
  from
    md_type_role role_type
    left join md_type_role sub_role_type on (find_in_set(sub_role_type.type_id,role_type.manage_role));

-- 角色及权限
CREATE OR REPLACE VIEW `md_role_permission` AS
  select
    concat(role_type.type_id,'-',permission_type.type_id) as type_id,
    role_type.type_id as role_type_id,
    role_type.type_name as role_type_name,
    role_type.code_id as role_code_id,
    permission_type.type_id as permission_type_id,
    permission_type.type_name as permission_type_name,
    permission_type.code_id as permission_code_id,
    find_in_set(permission_type.type_id,role_type.have_permission)>0 as is_direct_permission,
    group_concat(if(find_in_set(permission_type.type_id,sub_role_type.have_permission),concat(sub_role_type.type_id,','),'') separator '') as have_permission_sub_role_type,
    group_concat(if(find_in_set(permission_type.type_id,sub_role_type.have_permission),concat(sub_role_type.code_id,','),'') separator '') as have_permission_sub_role_code,
    group_concat(if(find_in_set(permission_type.type_id,sub_role_type.have_permission),concat(sub_role_type.type_name,','),'') separator '') as have_permission_sub_role_title
  from
    md_type_role role_type
    left join md_type_role sub_role_type on (find_in_set(sub_role_type.type_id,role_type.manage_role))
    inner join md_type_permission permission_type on (find_in_set(permission_type.type_id,role_type.have_permission)
                                                      or find_in_set(permission_type.type_id,sub_role_type.have_permission))
  group by role_type.type_id,permission_type.type_id;

-- 任务表
CREATE OR REPLACE VIEW `md_web_task` AS
    select
        task.id,
        task.task_name,
        task.task_type as type_id,
        task.task_pid as pid,
        concat(if(task_parent6.task_name is null,'',concat(task_parent6.task_name,'/')),
            if(task_parent5.task_name is null,'',concat(task_parent5.task_name,'/')),
            if(task_parent4.task_name is null,'',concat(task_parent4.task_name,'/')),
            if(task_parent3.task_name is null,'',concat(task_parent3.task_name,'/')),
            if(task_parent2.task_name is null,'',concat(task_parent2.task_name,'/')),
            if(task_parent1.task_name is null,'',concat(task_parent1.task_name,'/')),
            task.task_name) as path,
        if(task.task_type in (1,2),task.id,
            if(task_parent1.task_type in (1,2),task_parent1.id,
            if(task_parent2.task_type in (1,2),task_parent2.id,
            if(task_parent3.task_type in (1,2),task_parent3.id,
            if(task_parent4.task_type in (1,2), task_parent4.id,
            if(task_parent5.task_type in (1,2), task_parent5.id,
            if(task_parent6.task_type in (1,2), task_parent6.id,
            null))))))) as issue_id,
        if(task.task_type in (1,2),task.task_name,
            if(task_parent1.task_type in (1,2),task_parent1.task_name,
            if(task_parent2.task_type in (1,2),task_parent2.task_name,
            if(task_parent3.task_type in (1,2),task_parent3.task_name,
            if(task_parent4.task_type in (1,2), task_parent4.task_name,
            if(task_parent5.task_type in (1,2), task_parent5.task_name,
            if(task_parent6.task_type in (1,2), task_parent6.task_name,
            null))))))) as issue_name,
      concat(ifnull(concat(if(task_parent6.task_type in (1,2),task_parent6.task_name,null),'/'),''),
             ifnull(concat(if(task_parent5.task_type in (1,2),task_parent5.task_name,null),'/'),''),
             ifnull(concat(if(task_parent4.task_type in (1,2),task_parent4.task_name,null),'/'),''),
             ifnull(concat(if(task_parent3.task_type in (1,2),task_parent3.task_name,null),'/'),''),
             ifnull(concat(if(task_parent2.task_type in (1,2),task_parent2.task_name,null),'/'),''),
             ifnull(concat(if(task_parent1.task_type in (1,2),task_parent1.task_name,null),'/'),''),
             ifnull(concat(if(task.task_type in (1,2),task.task_name,null),''),'')) as issue_path,
      concat(ifnull(concat(if(task_parent6.task_type in (0),task_parent6.task_name,null),'/'),''),
             ifnull(concat(if(task_parent5.task_type in (0),task_parent5.task_name,null),'/'),''),
             ifnull(concat(if(task_parent4.task_type in (0),task_parent4.task_name,null),'/'),''),
             ifnull(concat(if(task_parent3.task_type in (0),task_parent3.task_name,null),'/'),''),
             ifnull(concat(if(task_parent2.task_type in (0),task_parent2.task_name,null),'/'),''),
             ifnull(concat(if(task_parent1.task_type in (0),task_parent1.task_name,null),'/'),''),
             ifnull(concat(if(task.task_type in (0),task.task_name,null),''),'')) as task_path,
        task.project_id,
        task.company_id,
        task.create_date,
        task.create_by,
        task.update_date,
        task.update_by
    from
        maoding_web_project_task task
        left join maoding_web_project_task task_parent1 ON (task_parent1.id = task.task_pid and task_parent1.task_type in (0,1,2) and task_parent1.task_status = '0')
        left join maoding_web_project_task task_parent2 ON (task_parent2.id = task_parent1.task_pid and task_parent2.task_type in (0,1,2) and task_parent2.task_status = '0')
        left join maoding_web_project_task task_parent3 ON (task_parent3.id = task_parent2.task_pid and task_parent3.task_type in (0,1,2) and task_parent3.task_status = '0')
        left join maoding_web_project_task task_parent4 ON (task_parent4.id = task_parent3.task_pid and task_parent4.task_type in (0,1,2) and task_parent4.task_status = '0')
        left join maoding_web_project_task task_parent5 ON (task_parent5.id = task_parent4.task_pid and task_parent5.task_type in (0,1,2) and task_parent5.task_status = '0')
        left join maoding_web_project_task task_parent6 ON (task_parent6.id = task_parent5.task_pid and task_parent6.task_type in (0,1,2) and task_parent6.task_status = '0')
    where
        (task.task_status = '0')
        and (task.task_type in (0,1,2));

-- 文件节点视图与项目相关的部分
CREATE OR REPLACE VIEW `md_node_project` AS
  select
    project.id,
    project.project_name as name,
    null AS pid,
    unix_timestamp(ifnull(project.create_date,0)) as create_time_stamp,
    date_format(project.create_date,'%Y/%m/%d %T') as create_time_text,
    unix_timestamp(ifnull(project.update_date,ifnull(project.create_date,0))) as last_modify_time_stamp,
    date_format(ifnull(project.update_date,project.create_date),'%Y/%m/%d %T') as last_modify_time_text,
    ifnull(project.update_by,project.create_by) as owner_user_id,
    project.update_by as last_modify_user_id,
    null as last_modify_role_id,
    project.id as project_id,
    project.project_name as prepare_project_name,
    null as range_id,
    null as range_name,
    null as task_id,
    null as prepare_task_name,
    null as storage_id,
    null as storage_path,
    project.company_id as prepare_company_id,
    null as owner_role_id,
    if(project.id is null,null,0) as fileLength,
    null as fileMd5,
    node_type.type_id,
    node_type.type_name,
    node_type.attr_str as node_type_attr,
    node_type.is_directory,
    node_type.is_project,
    node_type.is_issue,
    node_type.is_task,
    node_type.is_design,
    node_type.is_ca,
    node_type.is_commit,
    node_type.is_web,
    node_type.is_history
  from
    maoding_web_project project
    inner join md_type_node node_type on (node_type.type_id = 10)
  where
    (project.pstatus = '0');

-- 文件节点视图与分类相关的部分
CREATE OR REPLACE VIEW `md_node_range` AS
  select
    concat(ifnull(project.id,''),'-',range_type.type_id) as id,
    range_type.type_name as name,
    project.id AS pid,
    unix_timestamp(ifnull(project.create_date,0)) as create_time_stamp,
    date_format(project.create_date,'%Y/%m/%d %T') as create_time_text,
    unix_timestamp(ifnull(project.update_date,ifnull(project.create_date,0))) as last_modify_time_stamp,
    date_format(ifnull(project.update_date,project.create_date),'%Y/%m/%d %T') as last_modify_time_text,
    ifnull(project.update_by,project.create_by) as owner_user_id,
    project.update_by as last_modify_user_id,
    null as last_modify_role_id,
    project.id as project_id,
    project.project_name as prepare_project_name,
    range_type.type_id as range_id,
    range_type.type_name as range_name,
    null as task_id,
    null as prepare_task_name,
    null as storage_id,
    null as storage_path,
    project.company_id as prepare_company_id,
    null as owner_role_id,
    if(range_type.type_id is null,null,0) as fileLength,
    null as fileMd5,
    node_type.type_id,
    node_type.type_name,
    node_type.attr_str as node_type_attr,
    node_type.is_directory,
    node_type.is_project,
    node_type.is_issue,
    node_type.is_task,
    node_type.is_design,
    node_type.is_ca,
    node_type.is_commit,
    node_type.is_web,
    node_type.is_history
  from
    md_type_range range_type
    inner join md_type_node node_type on (range_type.node_type = node_type.type_id)
    left join maoding_web_project project on (range_type.is_project_range = 1 and project.pstatus = '0')
  where
    (range_type.is_display != 0);

-- 文件节点视图与任务相关的部分
CREATE OR REPLACE VIEW `md_node_task` AS
  select
    concat(task.id,'-',range_type.type_id) as id,
    task.task_name as name,
    concat(ifnull(task.task_pid,task.project_id),'-',range_type.type_id) AS pid,
    unix_timestamp(ifnull(task.create_date,0)) as create_time_stamp,
    date_format(task.create_date,'%Y/%m/%d %T') as create_time_text,
    unix_timestamp(ifnull(task.update_date,ifnull(task.create_date,0))) as last_modify_time_stamp,
    date_format(ifnull(task.update_date,task.create_date),'%Y/%m/%d %T') as last_modify_time_text,
    ifnull(task.update_by,task.create_by) as owner_user_id,
    task.update_by as last_modify_user_id,
    null as last_modify_role_id,
    task.project_id,
    null as prepare_project_name,
    range_type.type_id as range_id,
    range_type.type_name as range_name,
    task.id as task_id,
    task.task_name as prepare_task_name,
    null as storage_id,
    null as storage_path,
    task.company_id as prepare_company_id,
    null as owner_role_id,
    if(task.id is null,null,0) as fileLength,
    null as fileMd5,
    node_type.type_id,
    node_type.type_name,
    node_type.attr_str as node_type_attr,
    node_type.is_directory,
    node_type.is_project,
    node_type.is_issue,
    node_type.is_task,
    node_type.is_design,
    node_type.is_ca,
    node_type.is_commit,
    node_type.is_web,
    node_type.is_history
  from
    maoding_web_project_task task
    inner join md_type_web_task web_task_type on (task.task_type = web_task_type.type_id)
    inner join md_type_range range_type on (range_type.is_display != 0 and find_in_set(range_type.type_id,web_task_type.in_range))
    inner join md_type_node node_type on (node_type.type_id = (range_type.node_type + web_task_type.node_type_offset))
  where
    (task.task_status = '0') and (task.task_type in (0,1,2));

-- 文件节点视图与文件树相关部分
CREATE OR REPLACE VIEW `md_node_storage` AS
  select
    concat(storage_tree.id,'-',range_type.type_id) as id,
    storage_tree.node_name as name,
    ifnull(concat(if('-' = storage_tree.pid,null,storage_tree.pid),'-',range_type.type_id),
              concat(ifnull(storage_tree.task_id,ifnull(storage_tree.project_id,'')),'-',range_type.type_id)) as pid,
    unix_timestamp(ifnull(storage_tree.create_time,0)) as create_time_stamp,
    date_format(storage_tree.create_time,'%Y/%m/%d %T') as create_time_text,
    unix_timestamp(ifnull(storage_tree.last_modify_time,0)) as last_modify_time_stamp,
    date_format(storage_tree.last_modify_time,'%Y/%m/%d %T') as last_modify_time_text,
    storage_tree.owner_user_id,
    storage_tree.last_modify_user_id,
    storage_tree.last_modify_role_id,
    storage_tree.project_id,
    null as prepare_project_name,
    range_type.type_id as range_id,
    range_type.type_name as range_name,
    storage_tree.task_id,
    null as prepare_task_name,
    storage_tree.id as storage_id,
    storage_tree.path as storage_path,
    null as prepare_company_id,
    node_type.owner_role_type as owner_role_id,
    storage_tree.file_length,
    storage_tree.file_md5,
    node_type.type_id,
    node_type.type_name,
    node_type.attr_str as node_type_attr,
    node_type.is_directory,
    node_type.is_project,
    node_type.is_issue,
    node_type.is_task,
    node_type.is_design,
    node_type.is_ca,
    node_type.is_commit,
    node_type.is_web,
    node_type.is_history
  from
    md_tree_storage storage_tree
    inner join md_type_node node_type on (storage_tree.type_id = node_type.type_id)
    inner join md_type_range range_type on (range_type.is_display != 0 and node_type.range_id = range_type.type_id)
  where
    (storage_tree.deleted = 0);

-- 网站节点视图
CREATE OR REPLACE VIEW `md_node_sky_drive` AS
  select
    old_node.id,
    ifnull(old_node.pid,old_node.project_id) AS pid,
    if(old_node.file_size > 0,old_node_type.file_node_type,
       if(position('归档' in old_node.file_name)>0,42,old_node_type.dir_node_type)) as type_id,
    old_node.file_name as name,
    concat('/',project.project_name,'/',
      if(old_node_parent6.file_name is null,'',concat(old_node_parent6.file_name,'/')),
      if(old_node_parent5.file_name is null,'',concat(old_node_parent5.file_name,'/')),
      if(old_node_parent4.file_name is null,'',concat(old_node_parent4.file_name,'/')),
      if(old_node_parent3.file_name is null,'',concat(old_node_parent3.file_name,'/')),
      if(old_node_parent2.file_name is null,'',concat(old_node_parent2.file_name,'/')),
      if(old_node_parent1.file_name is null,'',concat(old_node_parent1.file_name,'/')),
      old_node.file_name) as path,
    unix_timestamp(ifnull(old_node.create_date,0)) as create_time_stamp,
    date_format(old_node.create_date,'%Y/%m/%d %T') as create_time_text,
    unix_timestamp(ifnull(old_node.update_date,ifnull(old_node.create_date,0))) as last_modify_time_stamp,
    date_format(ifnull(old_node.update_date,old_node.create_date),'%Y/%m/%d %T') as last_modify_time_text,
    ifnull(old_node.update_by,old_node.create_by) as owner_user_id,
    old_node.update_by as last_modify_user_id,
    null as last_modify_role_id,
    old_node.file_size as file_length,
    node_type.range_id as range_id,
    node_type.attr_str as node_type_attr,
    node_type.is_directory,
    node_type.is_project,
    node_type.is_issue,
    node_type.is_task,
    node_type.is_design,
    node_type.is_ca,
    node_type.is_commit,
    node_type.is_web,
    node_type.is_history,
    task.issue_id,
    old_node.task_id,
    old_node.project_id,
    old_node.company_id,
    old_node.project_id as root_id,
    node_type.owner_role_type as owner_role_id,
    account.user_name as owner_name,
    project.project_name as project_name,
    task.task_name as task_name
  from
    maoding_web_project_sky_drive old_node
    inner join md_type_sky_drive old_node_type on (old_node.type = old_node_type.type_id)
    inner join md_type_node node_type on (node_type.type_id = if(old_node.file_size > 0,old_node_type.file_node_type,
                                                                 if(position('归档' in old_node.file_name) > 0,42,
                                                                    old_node_type.dir_node_type)))
    left join md_web_task task on (old_node.task_id = task.id)
    left join maoding_web_project project on (old_node.project_id = project.id)
    left join maoding_web_project_sky_drive old_node_parent1 ON (old_node_parent1.id = old_node.pid)
    left join maoding_web_project_sky_drive old_node_parent2 ON (old_node_parent2.id = old_node_parent1.pid)
    left join maoding_web_project_sky_drive old_node_parent3 ON (old_node_parent3.id = old_node_parent2.pid)
    left join maoding_web_project_sky_drive old_node_parent4 ON (old_node_parent4.id = old_node_parent3.pid)
    left join maoding_web_project_sky_drive old_node_parent5 ON (old_node_parent5.id = old_node_parent4.pid)
    left join maoding_web_project_sky_drive old_node_parent6 ON (old_node_parent6.id = old_node_parent5.pid)
    left join maoding_web_account account on (old_node.create_by = account.id)
  where
    (old_node.status in (0,1)) and char_length(old_node.file_name) > 0;

-- 新的文件树节点通用视图
CREATE OR REPLACE VIEW `md_node` AS
  select * from md_node_project
  union all
  select * from md_node_range
  union all
  select * from md_node_task
  union all
  select * from md_node_storage;

-- 带操作属性历史视图
CREATE OR REPLACE VIEW `md_history` AS
  select
    md_history.*,
    action_type.type_name,
    action_type.attr_str as action_attr,
    action_type.is_ca,
    action_type.is_commit
  from
    md_list_storage_file_his md_history
    inner join md_type_action action_type on (action_type.type_id = md_history.action_type_id)
  where (md_history.deleted = 0);

-- 文件及附件视图
CREATE OR REPLACE VIEW `md_file` AS
  select
    file_list.*,
    substring(file_list.status,1,1) as pass_design,
    substring(file_list.status,2,1) as pass_check,
    substring(file_list.status,3,1) as pass_audit,
    file_type.type_name as file_type_name,
    file_type.attr_str as file_type_attr,
    file_type.is_mirror
  from
    md_list_storage_file file_list
    inner join md_type_file file_type on (file_type.type_id = file_list.file_type_id)
  where (file_list.deleted = 0);

-- 带状态文件视图
CREATE OR REPLACE VIEW `md_file_node` AS
    select
        concat(storage_tree.id,'-',range_type.type_id) as id,
        concat(if(storage_tree.pid is null,
                  if(ifnull(storage_tree.task_id,storage_tree.project_id) is null,null,
                     concat(ifnull(storage_tree.task_id,storage_tree.project_id),'-',range_type.type_id)),
                  concat(storage_tree.pid,'-',range_type.type_id))) as pid,
        storage_tree.type_id,
        storage_tree.node_name as name,
        concat(ifnull(concat('/',project.project_name),''),
               ifnull(concat('/',range_type.type_name),''),
               ifnull(concat('/',task.path),''),
               if(project.project_name is null
                  and range_type.type_name is null
                  and task.path is null,'','/'),
               storage_tree.path) as path,
        unix_timestamp(ifnull(storage_tree.create_time,0)) as create_time_stamp,
        date_format(storage_tree.create_time,'%Y/%m/%d %T') as create_time_text,
        unix_timestamp(ifnull(storage_tree.last_modify_time,0)) as last_modify_time_stamp,
        date_format(storage_tree.last_modify_time,'%Y/%m/%d %T') as last_modify_time_text,
        storage_tree.owner_user_id,
        storage_tree.last_modify_user_id,
        storage_tree.last_modify_role_id,
        storage_tree.file_length,
        storage_tree.file_md5,
        file_list.main_file_id,
        range_type.type_id as range_id,
        node_type.attr_str as node_type_attr,
        node_type.is_directory,
        node_type.is_project,
        node_type.is_issue,
        node_type.is_task,
        node_type.is_design,
        node_type.is_ca,
        node_type.is_commit,
        node_type.is_web,
        node_type.is_history,
        task.issue_id,
        task.id as task_id,
        project.id as project_id,
        task.company_id,
        project.id as root_id,
        node_type.owner_role_type as owner_role_id,
        owner_account.user_name as owner_name,
        project.project_name as project_name,
        task.task_name as task_name,
      substring(file_list.status,1,1) as is_pass_design,
      substring(file_list.status,2,1) as is_pass_check,
      substring(file_list.status,3,1) as is_pass_audit,
        task_role.account_id as role_user_id,
        web_role_type.attr_str as role_attr_str,
        web_role_type.is_task_leader,
        web_role_type.is_task_designer,
        web_role_type.is_task_checker,
        web_role_type.is_task_auditor,
        web_role_type.type_id as single_role_type_id,
        web_role_type.type_name as single_role_name
    from
        md_list_storage_file file_list
        inner join md_tree_storage storage_tree on (storage_tree.id = file_list.id)
        inner join md_type_node node_type on (storage_tree.type_id = node_type.type_id)
        inner join md_type_range range_type on (range_type.is_display != 0 and node_type.range_id = range_type.type_id)
        inner join md_web_task task on (storage_tree.task_id = task.id)
        inner join maoding_web_project project on (storage_tree.project_id = project.id)
        left join maoding_web_account owner_account on (storage_tree.owner_user_id = owner_account.id)

        inner join maoding_web_project_member task_role on ((task_role.node_id = storage_tree.task_id or task_role.target_id = storage_tree.task_id)
            and task_role.project_id = storage_tree.project_id)
        inner join md_type_web_role_project web_role_type on (task_role.member_type = web_role_type.type_id and web_role_type.is_task_role = 1)
        left join maoding_web_project_process_node process on (process.status = '0'
            and process.process_id = task.id
            and process.company_user_id = task_role.company_user_id
            and process.node_name = web_role_type.process_name)
    where
        (file_list.deleted = 0);

-- web项目角色视图
CREATE OR REPLACE VIEW `md_web_role_project` AS
    select
        concat(web_role_type.type_id,'-',if(web_role_type.is_project_role,task_role.project_id,
                                            ifnull(task_role.node_id,task_role.target_id)),'-',task_role.account_id) as id,
        task_role.id as web_role_id,
        web_role_type.type_id,
        web_role_type.type_name,
        web_role_type.role_type,
        web_role_type.attr_str,
        web_role_type.is_project_role,
        web_role_type.is_task_role,
        web_role_type.is_task_leader,
        web_role_type.is_task_designer,
        web_role_type.is_task_checker,
        web_role_type.is_task_auditor,
        if(web_role_type.type_id is null,null,0) as is_company_role,
        if(project.status = '0',0,1) as is_complete,
        task_role.account_id as user_id,
        task_role.project_id as project_id,
        task.id as task_id,
        task_role.company_id as company_id,
        unix_timestamp(ifnull(task_role.create_date,0)) as create_time_stamp,
        date_format(task_role.create_date,'%Y/%m/%d %T') as create_time_text,
        unix_timestamp(if(task_role.update_date is null,ifnull(task_role.create_date,0),task_role.update_date)) as last_modify_time_stamp,
        date_format(ifnull(task_role.update_date,task_role.create_date),'%Y/%m/%d %T') as last_modify_time_text,
        account.user_name,
        project.project_name,
        task.task_name as task_name,
        company.company_name
    from
        maoding_web_project_member task_role
        inner join md_type_web_role_project web_role_type on (task_role.member_type = web_role_type.type_id and web_role_type.is_project_role = 1)
        inner join maoding_web_account account on (task_role.account_id = account.id)
        inner join maoding_web_project project on (project.pstatus = '0' and task_role.project_id = project.id)
        inner join maoding_web_company company on (company.status = '0' and task_role.company_id = company.id)
        left join maoding_web_project_task task on (task.task_status = '0' and (task_role.node_id = task.id or task_role.target_id = task.id))
    where
        task_role.deleted = 0;

-- web任务角色视图
CREATE OR REPLACE VIEW `md_web_role_task` AS
    select
        concat(web_role_type.type_id,'-',if(web_role_type.is_project_role,task_role.project_id,
                                            ifnull(task_role.node_id,task_role.target_id)),'-',task_role.account_id) as id,
        task_role.id as web_role_id,
        web_role_type.type_id,
        web_role_type.type_name,
        web_role_type.role_type,
        web_role_type.attr_str,
        web_role_type.is_project_role,
        web_role_type.is_task_role,
        web_role_type.is_task_leader,
        web_role_type.is_task_designer,
        web_role_type.is_task_checker,
        web_role_type.is_task_auditor,
        if(web_role_type.type_id is null,null,0) as is_company_role,
        if(web_role_type.is_task_leader,
           task.complete_date is not null or mytask.complete_date is not null,
           mytask.complete_date is not null) as is_complete,
        task_role.account_id as user_id,
        task_role.project_id as project_id,
        ifnull(task_role.node_id,task_role.target_id) as task_id,
        task_role.company_id as company_id,
        unix_timestamp(ifnull(task_role.create_date,0)) as create_time_stamp,
        date_format(task_role.create_date,'%Y/%m/%d %T') as create_time_text,
        unix_timestamp(if(task_role.update_date is null,ifnull(task_role.create_date,0),task_role.update_date)) as last_modify_time_stamp,
        date_format(ifnull(task_role.update_date,task_role.create_date),'%Y/%m/%d %T') as last_modify_time_text,
        account.user_name,
        project.project_name,
        task.task_name,
        company.company_name
    from
        maoding_web_project_member task_role
        inner join md_type_web_role_project web_role_type on (task_role.member_type = web_role_type.type_id and web_role_type.is_task_role = 1)
        inner join maoding_web_account account on (task_role.account_id = account.id)
        inner join maoding_web_project project on (project.pstatus = '0' and task_role.project_id = project.id)
        inner join maoding_web_project_task task on (task.task_status = '0' and (task_role.node_id = task.id or task_role.target_id = task.id))
        inner join maoding_web_company company on (company.status = '0' and task_role.company_id = company.id)
        left join maoding_web_project_process_node process on (process.status = '0'
                                                    and process.process_id = task.id
                                                    and process.company_user_id = task_role.company_user_id
                                                    and process.node_name = web_role_type.process_name)
        left join maoding_web_my_task mytask on (param4 = '0'
                                                    and mytask.target_id = process.id)
    where
        task_role.deleted = 0;


-- web公司权限设置视图
CREATE OR REPLACE VIEW `md_web_permission_setting` AS
  select
    web_permission_group.code_id as group_id,
    web_permission_group.title as group_name,
    web_permission.type_id as permission_id,
    web_permission.type_name as permission_name,
    web_permission.is_plus_offset,
    web_permission.role_type,
    web_permission.need_permission
  from
    md_list_const web_permission_group
    inner join md_type_web_role_company web_permission on (find_in_set(web_permission.type_id,web_permission_group.extra))
  where web_permission_group.classic_id = 28
  order by web_permission_group.code_id,find_in_set(web_permission.type_id,web_permission_group.extra);

-- 公司岗位定义，即可分配的角色定义
CREATE OR REPLACE VIEW `md_web_job_company` AS
  select
    concat(company_job_list.company_id,'-',web_job_type.id) as id,
    company_job_list.id as company_job_id,
    concat(company_list.company_name,'-',web_job_type.name) as job_name,
    company_job_list.company_id as company_id,
    web_job_type.id as job_type_id,
    web_job_type.name as job_type_name
  from
    maoding_web_role_permission company_job_list
    inner join maoding_web_company company_list on (
             company_list.status = '0'
             and company_list.id = company_job_list.company_id
          )
    inner join maoding_web_permission web_job_type on (
             web_job_type.status = '0'
             and web_job_type.id = company_job_list.permission_id
           )
    ;

-- web公司角色视图
-- 依赖视图：md_web_job_company
CREATE OR REPLACE VIEW `md_web_role_company` AS
  select
    concat(company_role_list.company_id,'-',company_role_list.user_id,'-',company_role_list.permission_id) as id,
    company_role_list.id as role_id,
    company_role_list.company_id as company_id,
    company_role_list.user_id as user_id,
    company_role_list.permission_id as job_type_id,
    company_job_list.id as job_id,
    company_job_list.company_job_id as company_job_id,
    company_job_list.job_name as company_job_name,
    company_job_list.job_type_name as job_type_name,
    company_user_list.id as company_user_id,
    company_user_list.user_name as company_user_name
  from
    maoding_web_user_permission company_role_list
    inner join md_web_job_company company_job_list on (
      company_role_list.company_id = company_job_list.company_id
      and company_role_list.permission_id = company_job_list.job_type_id
    )
    inner join maoding_web_company_user company_user_list on (
      company_user_list.company_id = company_role_list.company_id
      and company_user_list.user_id = company_role_list.user_id
    )
  ;

-- 工作流用户群组关联，在创建act_id_user，act_id_group视图前必须先删除act_id_membership表，以消除外链影响
-- 依赖视图：md_web_role_company
DROP TABLE IF EXISTS act_id_membership;
CREATE OR REPLACE VIEW `act_id_membership` AS
  select
    company_role_list.company_user_id as USER_ID_,
    company_role_list.company_job_id as GROUP_ID_
  from
    md_web_role_company company_role_list;

-- 工作流用户
DROP TABLE IF EXISTS act_id_user;
CREATE OR REPLACE VIEW `act_id_user` AS
  select
    company_user_list.id as ID_,
    if(account_list.id is null,null,1) as REV_,
    company_user_list.user_name as FIRST_,
    account_list.user_name as LAST_,
    account_list.email as EMAIL_,
    account_list.password as PWD_,
    null as PICTURE_ID_
  from
    maoding_web_company_user company_user_list
    inner join maoding_web_account account_list on (
      company_user_list.user_id = account_list.id
    )
    left join maoding_web_user_attach account_attach on (
      account_attach.attach_type = 5
      and account_attach.user_id = account_list.id
    )
  ;

-- 工作流群组
-- 依赖视图：md_web_job_company
DROP TABLE IF EXISTS act_id_group;
CREATE OR REPLACE VIEW `act_id_group` AS
  select
    company_job_list.company_job_id as ID_,
    if(company_job_list.id is null,null,1) as REV_,
    company_job_list.job_name as NAME_,
    company_job_list.job_type_name as TYPE_
  from
    md_web_job_company company_job_list;

-- 可选择标题
CREATE OR REPLACE VIEW `md_optional_title` AS
  select
    concat(title_classic.id,'-',optional_group.id,'-',optional_title.id) as optional_title_id,
    title_classic.type_id as classic_type,
    title_classic.type_title as classic,
    optional_group.type_title as group_name,
    optional_title.type_code as code,
    optional_title.type_name as name,
    optional_title.*
  from
    md_type_web_title_classic title_classic
    inner join md_type_optional_group optional_group on (find_in_set(title_classic.id,optional_group.web_classic) and optional_group.id != '0')
    inner join md_type_optional_title optional_title on (optional_title.group_type = optional_group.id and optional_title.id != '0')
  where title_classic.is_group = 1;

-- 固定标题栏
CREATE OR REPLACE VIEW `md_title_default` AS
  select
    optional_title.optional_title_id as title_id,
    optional_title.*,
    optional_title.type_id as seq,
    filter_type.has_list
  from
    md_optional_title optional_title
    inner join md_type_filter filter_type on (filter_type.id = optional_title.filter_type)
  where optional_title.can_be_hide = 0;

-- 用户已选择标题栏
CREATE OR REPLACE VIEW `md_title` AS
  select
    concat(user_title.id,'-',optional_title.optional_title_id) as title_id,
    user_title.company_id,
    user_title.user_id,
    optional_title.*,
    find_in_set(optional_title.code,user_title.code) as seq,
    filter_type.has_list
  from
    maoding_web_project_condition user_title
    inner join md_optional_title optional_title on (
        optional_title.classic_type = user_title.type
        and find_in_set(optional_title.code,user_title.code)
    )
    inner join md_type_filter filter_type on (filter_type.id = optional_title.filter_type);

-- 项目参与人
CREATE OR REPLACE VIEW `md_project_member` AS
  select
    concat(role_type.type_id,'-',task_role.project_id,'-',task_role.company_user_id) as id,
    task_role.id as member_id,
    task_role.company_id as company_id,
    task_role.account_id as user_id,
    task_role.company_user_id as company_user_id,
    task_role.project_id as project_id,
    role_type.type_id as member_type,
    role_type.type_name as member_type_name,
    role_type.is_task_leader,
    role_type.is_task_designer,
    role_type.is_task_checker,
    role_type.is_task_auditor,
    project.project_name,
    company_user.user_name
  from
    maoding_web_project_member task_role
    inner join md_type_web_role_project role_type on (
        role_type.is_project_role = 1
        and task_role.member_type = role_type.type_id
    )
    inner join maoding_web_project project on (project.pstatus = '0' and task_role.project_id = project.id)
    inner join maoding_web_company_user company_user on (company_user.id = task_role.company_user_id)
  where
    task_role.deleted = 0;

-- -- -- 创建及更改视图 -- 结束 -- -- --