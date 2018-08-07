-- -- 交付历史表
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

-- -- 项目自定义属性（模板属性）
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

-- -- 任务定义
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

-- -- 常量定义
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

-- -- 自定义常量定义
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

DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
    -- -- 协同文件定义
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

-- maoding_web_role -- 权限组表
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

-- maoding_web_role_permission -- 权限树表
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

-- maoding_web_permission -- 权限表
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

-- maoding_web_user_permission -- 组织角色表
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

-- -- 文件注解附件
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

-- -- 文件注解
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

-- -- 内嵌HTML元素
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

-- -- 协同节点定义
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

-- -- 协同文件校审提资历史记录定义
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

-- -- 组织定义
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

-- -- 公司定义
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

-- -- 成员定义
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

-- -- 工作定义
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

-- -- 项目定义
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

-- -- 签发任务定义
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

-- -- 生产任务定义
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

-- -- 项目和任务更改历史
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

-- -- 用户界面
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

-- -- 财务记录
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

-- -- 收支记录
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




-- -- 常量视图
DROP TABLE IF EXISTS `md_const`;
CREATE OR REPLACE VIEW `md_const` AS
  select
    concat(const_list.classic_id,'-',const_list.code_id) as id,
    null as company_id,
    null as project_id,
    null as task_id,
    const_list.classic_id,
    const_list.code_id,
    const_list.title,
    const_list.extra
  from
    md_list_const const_list

  union all

  select
    custom_const_list.id,
    custom_const_list.company_id,
    custom_const_list.project_id,
    custom_const_list.task_id,
    custom_const_list.classic_id,
    custom_const_list.code_id,
    custom_const_list.title,
    custom_const_list.extra
  from
    md_list_const_custom custom_const_list
  where deleted = 0;

-- -- 默认项目模板名称定义视图
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

-- -- 默认项目模板内容定义视图
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

-- -- 默认项目模板基本类型定义视图
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

-- -- 项目模板默认功能分类视图
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

-- -- 默认功能分类视图
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

-- -- 自定义功能分类视图
CREATE OR REPLACE VIEW `md_type_built_custom` AS
  select
    built_type.id,
    built_type.company_id,
    built_type.project_id,
    built_type.task_id,
    built_type.code_id as type_id,
    built_type.title as type_name
  from
    md_list_const_custom built_type
  where deleted = 0 and built_type.classic_id = 33;

-- -- 交付视图
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

-- -- 文件类型定义视图
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

-- -- 专业类型定义视图
CREATE OR REPLACE VIEW `md_type_web_major` AS
  select
    major_type.code_id as type_id,
    major_type.title as type_name
  from
    md_list_const major_type
  where (major_type.classic_id = 22);

-- -- 服务器类型定义视图
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

-- -- 文件操作定义视图
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

-- -- web项目角色类型定义视图
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

-- -- skydrive上文档分类定义视图
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

-- -- 文档分类定义视图
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

-- -- 节点类型定义视图
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

-- -- task任务类型定义视图
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

-- -- 权限定义视图
CREATE OR REPLACE VIEW `md_type_permission` AS
  select
    permission_type.code_id as type_id,
    substring(permission_type.title,locate(';',permission_type.title) + 1) AS type_name,
    substring(permission_type.title,1,locate(';',permission_type.title) - 1) AS code_id
  from
    md_list_const permission_type
  where
    permission_type.classic_id = 1;

-- -- 角色定义视图
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

-- -- 角色及可管理角色
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

-- -- 角色及权限
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

-- -- 任务表
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

-- -- 文件节点视图与项目相关的部分
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

-- -- 文件节点视图与分类相关的部分
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

-- -- 文件节点视图与任务相关的部分
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

-- -- 文件节点视图与文件树相关部分
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

-- -- 网站节点视图
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

-- -- 新的文件树节点通用视图
CREATE OR REPLACE VIEW `md_node` AS
  select * from md_node_project
  union all
  select * from md_node_range
  union all
  select * from md_node_task
  union all
  select * from md_node_storage;

-- -- 带操作属性历史视图
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

-- -- 文件及附件视图
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

-- -- 带状态文件视图
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

-- -- web项目角色视图
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

-- -- web任务角色视图
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


-- -- web公司权限设置视图
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
