-- -- -- 创建及更改表 -- 开始 -- -- --
-- 流程类型表
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
  BEGIN
    CREATE TABLE IF NOT EXISTS `maoding_process_type` (
      `id` varchar(32) NOT NULL COMMENT '流程ID',
      `company_id` varchar(64) DEFAULT NULL COMMENT '流程实例id',
      `target_type` varchar(32) DEFAULT NULL COMMENT '业务类型',
      `type` int(1) DEFAULT '0' COMMENT '流程类型（1：自由流程，2：固定流程，3：分条件流程）',
      `status` int(1) DEFAULT '0' COMMENT '0:未启用，1：启用',
      `finance_field_id` varchar(32) DEFAULT NULL COMMENT '动态表单中，用于作为存储财务拨款条件的字段id',
      `condition_field_id` varchar(32) DEFAULT NULL COMMENT '动态表单中，用于作为条件流程的字段id',

      `deleted` int(1) DEFAULT '0' COMMENT '删除标识',
      `create_date` datetime DEFAULT NULL,
      `create_by` varchar(50) DEFAULT NULL,
      `update_date` datetime DEFAULT NULL,
      `update_by` varchar(50) DEFAULT NULL,
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='流程与业务表的关联表';

    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_process_type' and column_name='finance_field_id') then
      alter table maoding_process_type add column `finance_field_id` varchar(32) DEFAULT NULL COMMENT '动态表单中，用于作为存储财务拨款条件的字段id';
    end if;
    if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_process_type' and column_name='target_type') then
      alter table maoding_process_type add column `target_type` varchar(32) DEFAULT NULL COMMENT '业务类型';
    elseif exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_process_type' and column_name='target_type' and character_maximum_length<32) then
      alter table maoding_process_type modify column `target_type` varchar(32) DEFAULT NULL COMMENT '业务类型';
    end if;

    call createIndex('maoding_process_type');
  END;
call update_table();

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

  call createIndex('maoding_web_exp_main');
END;
call update_table();

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

  call createIndex('maoding_web_exp_audit');
END;
call update_table();

-- 动态表单主表
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
  CREATE TABLE IF NOT EXISTS `maoding_dynamic_form` (
    `id` varchar(32) NOT NULL COMMENT '动态表单的id',
    `company_id` varchar(32) DEFAULT NULL COMMENT '组织id（如果为null，则为公共的可以供每个组织选择复制的）',
    `form_type` varchar(32) DEFAULT NULL COMMENT '表单类型',
    `form_name` varchar(32) DEFAULT NULL COMMENT '名称',
    `seq` int(4) DEFAULT NULL COMMENT '排序',
    `status` int(2) DEFAULT NULL COMMENT '1：被启用，0：未被启用',
    
    `form_code` varchar(40) DEFAULT NULL COMMENT '关键字',
    `group_type_id` char(32) DEFAULT NULL COMMENT '表单群组编号',
    `documentation` varchar(255) DEFAULT NULL COMMENT '说明',
    `var_name` varchar(40) DEFAULT NULL COMMENT '分条件流程变量名称',
    `var_unit` varchar(10) DEFAULT NULL COMMENT '分条件流程变量单位',
    `icon_key` varchar(40) DEFAULT NULL COMMENT '图标关键字',

    `deleted` int(1) DEFAULT 0 COMMENT '删除标识',
    `create_date` datetime DEFAULT NULL COMMENT '创建时间',
    `create_by` char(32) DEFAULT NULL COMMENT '创建者用户编号',
    `update_date` datetime DEFAULT NULL COMMENT '最后更新时间',
    `update_by` char(32) DEFAULT NULL COMMENT '最后更新者用户编号',
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='动态表单主表';

  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_dynamic_form' and column_name='icon_key') then
    alter table maoding_dynamic_form add column `icon_key` varchar(40) DEFAULT NULL COMMENT '图标关键字';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_dynamic_form' and column_name='form_code') then
    alter table maoding_dynamic_form add column `form_code` varchar(40) DEFAULT NULL COMMENT '关键字';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_dynamic_form' and column_name='group_type_id') then
    alter table maoding_dynamic_form add column `group_type_id` char(32) DEFAULT NULL COMMENT '表单群组编号';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_dynamic_form' and column_name='documentation') then
    alter table maoding_dynamic_form add column `documentation` varchar(255) DEFAULT NULL COMMENT '说明';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_dynamic_form' and column_name='var_name') then
    alter table maoding_dynamic_form add column `var_name` varchar(40) DEFAULT NULL COMMENT '分条件流程变量名称';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_dynamic_form' and column_name='var_unit') then
    alter table maoding_dynamic_form add column `var_unit` varchar(10) DEFAULT NULL COMMENT '分条件流程变量单位';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_dynamic_form' and column_name='is_system') then
    alter table maoding_dynamic_form add column `is_system` int(1) DEFAULT NULL COMMENT '是否系统类型';
  end if;

    call createIndex('maoding_dynamic_form');
END;
call update_table();

-- 动态表单字段表
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
  CREATE TABLE IF NOT EXISTS `maoding_dynamic_form_field` (
    `id` varchar(32) NOT NULL COMMENT '动态表单字段的id',
    `form_id` varchar(32) DEFAULT NULL COMMENT '主表id（maoding_dynamic_form的id）',
    `field_pid` varchar(32) DEFAULT NULL COMMENT '父记录id（maoding_dynamic_form_field的id）,如果pid不为null，则代表的是明细中的字段',
    `field_title` varchar(32) DEFAULT NULL COMMENT '标题',
    `field_type` int(2) DEFAULT '0' COMMENT '字段类型',
    `field_unit` varchar(10) DEFAULT '0' COMMENT '字段的单位',
    `is_statistics` int(2) DEFAULT '0' COMMENT '是否参与统计（0：不参与，1：参与）',
    `field_tooltip` varchar(32) DEFAULT NULL COMMENT '提示文本',
    `field_default_value` varchar(100) DEFAULT NULL COMMENT '默认值（暂时定100个长度）',
    `field_select_value_type` int(4) DEFAULT '0' COMMENT '默认值0(由数据字典提供，如果为0，则从maoding_dynamic_form_field_selectable_value 去获取)',
    `seq_x` int(4) DEFAULT NULL COMMENT '横坐标排序（如果x相同，则排成一行）',
    `seq_y` int(4) DEFAULT NULL COMMENT '纵坐标排序',
    `required_type` int(2) DEFAULT '0' COMMENT '必填类型（1：必填，0：非必填）',
    `deleted` int(1) DEFAULT NULL COMMENT '删除标识',
    `create_date` datetime DEFAULT NULL,
    `create_by` varchar(50) DEFAULT NULL,
    `update_date` datetime DEFAULT NULL,
    `update_by` varchar(50) DEFAULT NULL,
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='动态表单字段表';

  call createIndex('maoding_dynamic_form_field');
END;
call update_table();

-- 动态表单字段可选值表
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
  CREATE TABLE IF NOT EXISTS `maoding_dynamic_form_field_selectable_value` (
    `id` varchar(32) NOT NULL COMMENT '动态表单字段的id',
    `field_id` varchar(32) DEFAULT NULL COMMENT '动态字段表id（maoding_dynamic_form_field的id）',
    `selectable_value` varchar(100) DEFAULT NULL COMMENT '值（例如：select 下拉框对应的value，如果前端并没有设置，后台默认为vlaue = name）',
    `selectable_name` varchar(100) DEFAULT NULL COMMENT '名称（显示在外面的名称）',
    `seq` int(4) DEFAULT NULL COMMENT '排序',
    `deleted` int(1) DEFAULT NULL COMMENT '删除标识',
    `create_date` datetime DEFAULT NULL,
    `create_by` varchar(50) DEFAULT NULL,
    `update_date` datetime DEFAULT NULL,
    `update_by` varchar(50) DEFAULT NULL,
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='动态表单字段可选值表';

  call createIndex('maoding_dynamic_form_field_selectable_value');
END;
call update_table();

-- 动态表实例值
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
  CREATE TABLE IF NOT EXISTS `maoding_dynamic_form_field_value` (
    `id` varchar(32) NOT NULL COMMENT '动态表单字段的id',
    `main_id` varchar(32) DEFAULT NULL COMMENT '审批主表id（maoding_web_exp_main 的id）',
    `field_id` varchar(32) DEFAULT NULL COMMENT '动态字段表id（maoding_dynamic_form_field的id）',
    `field_value` varchar(1000) DEFAULT NULL COMMENT '值（全部用字符串类型接收）',
    `field_value_pid` varchar(32) DEFAULT NULL COMMENT '父记录id（maoding_dynamic_form_field_value）,如果pid不为null，则代表的是明细中的字段',
    `deleted` int(1) DEFAULT NULL COMMENT '删除标识',
    `create_date` datetime DEFAULT NULL,
    `create_by` varchar(50) DEFAULT NULL,
    `update_date` datetime DEFAULT NULL,
    `update_by` varchar(50) DEFAULT NULL,
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='动态表实例值';

  call createIndex('maoding_dynamic_form_field_value');
END;
call update_table();

-- 动态审批表单群组
DROP PROCEDURE IF EXISTS `update_table`;
CREATE PROCEDURE `update_table`()
BEGIN
  CREATE TABLE IF NOT EXISTS `maoding_dynamic_form_group` (
    `id` varchar(32) NOT NULL COMMENT '动态表单群组编号',
    `company_id` varchar(32) DEFAULT NULL COMMENT '所属公司编号',
    `group_name` varchar(32) DEFAULT NULL COMMENT '审批类型群组名称',
    `is_edit` int(1) DEFAULT 1 COMMENT '可编辑标识：1，可编辑，0；不可编辑',
    `seq` int(1) DEFAULT 0 COMMENT '排序',
    `type_id` int(4) DEFAULT NULL COMMENT '动态表单群组类型',

    `deleted` int(1) DEFAULT 0 COMMENT '删除标识',
    `create_date` datetime DEFAULT NULL COMMENT '创建时间',
    `create_by` char(32) DEFAULT NULL COMMENT '创建者用户编号',
    `update_date` datetime DEFAULT NULL COMMENT '最后更新时间',
    `update_by` char(32) DEFAULT NULL COMMENT '最后更新者用户编号',
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='动态表实例值';

  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_dynamic_form_group' and column_name='type_id') then
    alter table maoding_dynamic_form_group add column `type_id` int(4) DEFAULT NULL COMMENT '动态表单群组类型';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_dynamic_form_group' and column_name='is_edit') then
    alter table maoding_dynamic_form_group add column `is_edit` int(1) DEFAULT 1 COMMENT '可编辑标识：1，可编辑，0；不可编辑';
  end if;
  if not exists (select 1 from information_schema.COLUMNS where TABLE_SCHEMA=database() and table_name='maoding_dynamic_form_group' and column_name='seq') then
    alter table maoding_dynamic_form_group add column `seq` int(1) DEFAULT 0 COMMENT '排序';
  end if;

  call createIndex('maoding_dynamic_form_group');
END;
call update_table();


-- -- -- 创建及更改表 -- 结束 -- -- --

-- -- -- 创建及更改常量 -- 开始 -- -- --
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

    -- -- 视图
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

    -- -- 视图
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
        exp_type_status.classic_id = 48 and exp_type_status.code_id > -1;
  END;
call initConst();

-- 自定义表单可选控件
DROP PROCEDURE IF EXISTS `initConst`;
CREATE PROCEDURE `initConst`()
  BEGIN
    -- -- 常量
    delete from md_list_const where classic_id = 49;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,0,'可选控件',  '1.允许输入;2.允许输入字母;3.允许输入换行;4.允许选择格式;5.是时间选择框;6.允许复选;7.允许上传;8.允许嵌套;9.获取数据接口;10.可设置的属性;11.图标关键字;12.允许作为条件');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,1,'单行文本',  '1;1;0;0;0;0;0;0;;1,2,3;icon-danhangwenben;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,2,'多行文本',  '1;1;1;0;0;0;0;0;;1,2,3;icon-duohangwenben;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,3,'日期',     '0;0;0;0;1;0;0;0;;1,2,14,3;icon-riqi;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,4,'日期区间',  '0;0;0;0;1;0;0;0;;1,10,11,12,13,14,3;icon-riqiqujian;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,5,'数字',     '1;0;0;0;0;0;0;0;;1,2,3;icon-shuzi;1');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,6,'下拉列表', '0;0;0;0;0;0;0;0;;1,2,4,5,3;icon-xialaliebiao;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,7,'单选框',   '0;0;0;0;0;0;0;0;;1,2,4,5,3;icon-danxuankuang;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,8,'复选框',   '0;0;0;0;0;1;0;0;;1,2,4,5,3;icon-fuxuankuang;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,9,'明细',   '0;0;0;0;0;0;0;1;;1;icon-mingxi;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,10,'附件',   '0;0;0;0;0;0;1;0;;1,2,5,3;icon-fujian;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,11,'关联审批', '0;0;0;0;0;0;0;0;iWork/finance/getExpBaseData;1,2,15,3;icon-shenpi;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,12,'关联项目', '0;0;0;0;0;0;0;0;iWork/finance/getProjectList;1,2,15,3;icon-xiangmu;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,13,'纯文本',  '1;1;1;0;0;0;0;0;;1,2,3;icon-chunwenben;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (49,14,'分割线', '0;0;0;0;0;0;0;0;;1;icon-fengexian;');
    -- -- 类型
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,49,'可选控件','md_type_widget');

    -- -- 视图
    CREATE OR REPLACE VIEW `md_type_widget` AS
      select
        widget_type.code_id as type_id,
        widget_type.title   as type_name,
        substring(widget_type.extra,
                  1,
                  char_length(substring_index(widget_type.extra,';',1)))
          as allow_input,
        substring(widget_type.extra,
                  char_length(substring_index(widget_type.extra,';',1))+2,
                  char_length(substring_index(widget_type.extra,';',2)) - char_length(substring_index(widget_type.extra,';',1))-1)
          as allow_alpha,
        substring(widget_type.extra,
                  char_length(substring_index(widget_type.extra,';',2))+2,
                  char_length(substring_index(widget_type.extra,';',3)) - char_length(substring_index(widget_type.extra,';',2))-1)
          as allow_crlf,
        substring(widget_type.extra,
                  char_length(substring_index(widget_type.extra,';',3))+2,
                  char_length(substring_index(widget_type.extra,';',4)) - char_length(substring_index(widget_type.extra,';',3))-1)
          as allow_format,
        substring(widget_type.extra,
                  char_length(substring_index(widget_type.extra,';',4))+2,
                  char_length(substring_index(widget_type.extra,';',5)) - char_length(substring_index(widget_type.extra,';',4))-1)
          as is_time,
        substring(widget_type.extra,
                  char_length(substring_index(widget_type.extra,';',5))+2,
                  char_length(substring_index(widget_type.extra,';',6)) - char_length(substring_index(widget_type.extra,';',5))-1)
          as allow_multi,
        substring(widget_type.extra,
                  char_length(substring_index(widget_type.extra,';',6))+2,
                  char_length(substring_index(widget_type.extra,';',7)) - char_length(substring_index(widget_type.extra,';',6))-1)
          as allow_upload,
        substring(widget_type.extra,
                  char_length(substring_index(widget_type.extra,';',7))+2,
                  char_length(substring_index(widget_type.extra,';',8)) - char_length(substring_index(widget_type.extra,';',7))-1)
          as allow_nest,
        substring(widget_type.extra,
                  char_length(substring_index(widget_type.extra,';',8))+2,
                  char_length(substring_index(widget_type.extra,';',9)) - char_length(substring_index(widget_type.extra,';',8))-1)
          as optional_url,
        substring(widget_type.extra,
                  char_length(substring_index(widget_type.extra,';',9))+2,
                  char_length(substring_index(widget_type.extra,';',10)) - char_length(substring_index(widget_type.extra,';',9))-1)
          as set_type,
        substring(widget_type.extra,
                  char_length(substring_index(widget_type.extra,';',10))+2,
                  char_length(substring_index(widget_type.extra,';',11)) - char_length(substring_index(widget_type.extra,';',10))-1)
          as icon_key,
        substring(widget_type.extra,
                  char_length(substring_index(widget_type.extra,';',11))+2,
                  char_length(substring_index(widget_type.extra,';',12)) - char_length(substring_index(widget_type.extra,';',11))-1)
          as is_condition
      from
        md_list_const widget_type
      where
        widget_type.classic_id = 49 and widget_type.code_id > 0;
  END;
call initConst();

-- 自定义表单控件可设置属性
DROP PROCEDURE IF EXISTS `initConst`;
CREATE PROCEDURE `initConst`()
  BEGIN
    -- -- 常量
    delete from md_list_const where classic_id = 50;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (50,0,'可设置属性:1.属性名称;2.显示名称','1.可通过属性名称保存属性;2.控件名称作为默认值;3.类型;4.默认值;5.可选值');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (50,1,':fieldTitle;标题',  '1;1;1;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (50,2,':fieldTooltip;提示文字',  '1;1;1;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (50,3,':requiredType;是否必填',  '1;0;3;;必填');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (50,4,':可选项;选项值',   '0;0;4;选项1,选项2;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (50,5,':arrangeType;排列方式',  '1;0;2;;横向,纵向');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (50,6,':fieldUnit;单位',   '1;0;1;元;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (50,10,':开始时间标题;标题1', '0;0;1;开始时间;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (50,11,':开始时间提示;提示文字1',  '0;0;1;开始时间;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (50,12,':结束时间标题;标题2',  '0;0;1;结束时间;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (50,13,':结束时间提示;提示文字2',   '0;0;1;结束时间;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (50,14,':dateType;日期类型',   '1;0;2;;年/月/日,年/月/日 时:分,年/月/日 上午&下午');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (50,15,':项目属性;项目属性',   '0;0;2;;参与的项目,所有的项目');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (50,16,':审批属性;审批属性',   '0;0;2;;报销,费用,请假,出差');

    -- -- 类型
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,50,'可设置属性','md_type_widget_property');

    -- -- 视图
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
          as can_save_by_code,
        substring(property_type.extra,
                  char_length(substring_index(property_type.extra,';',1))+2,
                  char_length(substring_index(property_type.extra,';',2)) - char_length(substring_index(property_type.extra,';',1))-1)
          as is_name_default,
        substring(property_type.extra,
                  char_length(substring_index(property_type.extra,';',2))+2,
                  char_length(substring_index(property_type.extra,';',3)) - char_length(substring_index(property_type.extra,';',2))-1)
          as view_type,
        substring(property_type.extra,
                  char_length(substring_index(property_type.extra,';',3))+2,
                  char_length(substring_index(property_type.extra,';',4)) - char_length(substring_index(property_type.extra,';',3))-1)
          as default_value,
        substring(property_type.extra,
                  char_length(substring_index(property_type.extra,';',4))+2,
                  char_length(substring_index(property_type.extra,';',5)) - char_length(substring_index(property_type.extra,';',4))-1)
          as allow_value
      from
        md_list_const property_type
      where
        property_type.classic_id = 50 and property_type.code_id > 0;
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

    -- -- 视图
    CREATE OR REPLACE VIEW `md_type_widget_property_type` AS
      select
        input_type.code_id as type_id,
        input_type.title   as type_name
      from
        md_list_const input_type
      where
        input_type.classic_id = 51 and input_type.code_id > 0;
  END;
call initConst();

-- 表单模板
DROP PROCEDURE IF EXISTS `initConst`;
CREATE PROCEDURE `initConst`()
  BEGIN
    -- -- 常量
    delete from md_list_const where classic_id = 52;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (52,0,'表单模板:1-流程关键字;2-流程名称',  '1.所属群组;2.说明;3.分条件审批变量名;4.分条件审批变量单位');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (52,1,':leave;请假申请',                '1;适用于公司请假审批;请假时长;天');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (52,2,':onBusiness;出差申请',           '1;适用于公司出差审批;出差时长;天');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (52,3,':expense;报销申请',              '2;适用于公司报销审批;报销金额;元');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (52,4,':costApply;费用申请',            '2;适用于公司费用审批;费用申请金额;元');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (52,5,':projectPayApply;付款审批',      '3;适用于公司付款审批;付款审批金额;万元');

    -- -- 类型
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,52,'表单模板','md_type_form,maoding_dynamic_form');

    -- -- 视图
    CREATE OR REPLACE VIEW `md_type_form` AS
      select
        form_type.code_id as type_id,
        substring(form_type.title,
                  char_length(substring_index(form_type.title,':',1))+2,
                  char_length(substring_index(form_type.title,';',1)) - char_length(substring_index(form_type.title,':',1))-1)
                           as type_code,
        substring(form_type.title,
                  char_length(substring_index(form_type.title,';',1))+2,
                  char_length(substring_index(form_type.title,';',2)) - char_length(substring_index(form_type.title,';',1))-1)
                           as type_name,
        substring(form_type.extra,
                  1,
                  char_length(substring_index(form_type.extra, ';', 1)))
                           as group_type_id,
        substring(form_type.extra,
                  char_length(substring_index(form_type.extra,';',1))+2,
                  char_length(substring_index(form_type.extra,';',2)) - char_length(substring_index(form_type.extra,';',1))-1)
                           as documentation,
        substring(form_type.extra,
                  char_length(substring_index(form_type.extra,';',2))+2,
                  char_length(substring_index(form_type.extra,';',3)) - char_length(substring_index(form_type.extra,';',2))-1)
                           as var_name,
        substring(form_type.extra,
                  char_length(substring_index(form_type.extra,';',3))+2,
                  char_length(substring_index(form_type.extra,';',4)) - char_length(substring_index(form_type.extra,';',3))-1)
                           as var_unit
      from
        md_list_const form_type
      where
        form_type.classic_id = 52 and form_type.code_id > 0;

    -- -- 自定义常量
    delete from maoding_dynamic_form where company_id is null;
    REPLACE INTO maoding_dynamic_form (id,create_date,update_date,deleted,`status`,form_name,form_type,documentation,seq,var_name,var_unit,icon_key)
      SELECT type_code,now(),now(),0,1,type_name,concat('54-',group_type_id),documentation,type_id,var_name,var_unit,icon_key
      FROM md_type_form;
  END;
call initConst();

-- 表单模板字段
DROP PROCEDURE IF EXISTS `initConst`;
CREATE PROCEDURE `initConst`()
  BEGIN
    -- -- 常量
    delete from md_list_const where classic_id = 53;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,0,'表单模板字段:1-名称;2-显示标题',        '1.必填;2.参与统计;3.所属表单;4.横轴排序;5.纵轴排序;6.控件类型;7.可选项查询地址;8.可选项参数/固定可选项;9.容器编号;10.格式;11.提示文字;12.默认值;13.单位');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,1,':请假申请-请假类型;请假类型',           '1;0;1;0;1;7;iWork/leave/getLeaveType;;;;请选择请假类型;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,2,':请假申请-开始结束时间;开始时间,结束时间','1;0;1;0;2;4;;;;年/月/日 时:分;开始时间,结束时间;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,3,':请假申请-请假天数;请假天数',           '1;0;1;0;3;6;;;;;请输入请假天数;;天');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,4,':请假申请-请假事由;请假事由',           '0;0;1;0;4;2;;;;;请输入;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,5,':请假申请-附件;附件',                 '0;0;1;0;5;14;;;;;;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,6,':出差申请-出差地点;出差地点',           '1;0;2;0;1;1;;;;;请输入;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,7,':出差申请-开始结束时间;开始时间,结束时间','1;0;2;0;2;4;;;;年/月/日 时:分;开始时间,结束时间;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,8,':出差申请-出差天数;出差天数',           '1;0;2;0;3;6;;;;;请输入出差天数;;天');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,9,':出差申请-出差事由;出差事由',           '0;0;2;0;4;2;;;;;请输入;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,10,':出差申请-关联项目;关联项目',          '0;0;2;0;5;9;iWork/finance/getProjectList;1;;;请选择关联项目;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,11,':出差申请-附件;附件',                 '0;0;2;0;6;14;;;;;;;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,12,':报销申请-报销明细;报销明细',           '0;0;3;0;1;12;;;;;;;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,13,':报销申请-附件;附件',                 '0;0;3;0;2;14;;;;;;;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,14,':报销申请明细-报销金额;报销金额',       '1;1;4;0;1;6;;;12;;请输入金额;;元');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,15,':报销申请明细-报销类型;报销类型',       '1;0;4;0;2;9;iWork/finance/getExpBaseData;;12;;请选择报销类型;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,16,':报销申请明细-用途说明;用途说明',       '1;0;4;0;3;2;;;12;;请输入;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,17,':报销申请明细-关联项目;关联项目',       '0;0;4;0;4;15;iWork/finance/getExpBaseData;1;12;;请选择关联项目;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,18,':报销申请明细-关联审批;关联审批',       '0;0;4;0;5;16;iWork/finance/getExpBaseData;1;12;;请选择关联审批;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,19,':费用申请-收款方;收款方',             '0;0;5;0;1;1;;;;;请输入收款方;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,20,':费用申请-备注;备注',                '0;0;5;0;2;2;;;;;请输入;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,21,':费用申请-费用明细;费用明细',          '0;0;5;0;3;12;;;;;;;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,22,':费用申请明细-费用金额;费用金额',       '1;1;6;0;1;6;;;21;;请输入金额;;元');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,23,':费用申请明细-费用类型;费用类型',       '1;0;6;0;2;9;iWork/finance/getExpBaseData;;21;;;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,24,':费用申请明细-用途说明;用途说明',       '1;0;6;0;3;2;;;21;;请输入用途说明;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,25,':费用申请明细-关联项目;关联项目',       '0;0;6;0;4;15;iWork/finance/getExpBaseData;;21;;请选择关联项目;;');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (53,26,':费用申请明细-关联审批;关联审批',       '0;0;6;0;5;16;iWork/finance/getExpBaseData;;21;;请选择关联审批;;');

    -- -- 类型
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,53,'表单模板组件','md_type_form_field,maoding_dynamic_form_field');

    -- -- 视图
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
          as required_type,
        substring(field_type.extra,
                  char_length(substring_index(field_type.extra,';',1))+2,
                  char_length(substring_index(field_type.extra,';',2)) - char_length(substring_index(field_type.extra,';',1))-1)
          as is_statistics,
        substring(field_type.extra,
                  char_length(substring_index(field_type.extra,';',2))+2,
                  char_length(substring_index(field_type.extra,';',3)) - char_length(substring_index(field_type.extra,';',2))-1)
          as form_id,
        substring(field_type.extra,
                  char_length(substring_index(field_type.extra,';',3))+2,
                  char_length(substring_index(field_type.extra,';',4)) - char_length(substring_index(field_type.extra,';',3))-1)
          as seq_x,
        substring(field_type.extra,
                  char_length(substring_index(field_type.extra,';',4))+2,
                  char_length(substring_index(field_type.extra,';',5)) - char_length(substring_index(field_type.extra,';',4))-1)
          as seq_y,
        substring(field_type.extra,
                  char_length(substring_index(field_type.extra,';',5))+2,
                  char_length(substring_index(field_type.extra,';',6)) - char_length(substring_index(field_type.extra,';',5))-1)
          as field_type,
        substring(field_type.extra,
                  char_length(substring_index(field_type.extra,';',6))+2,
                  char_length(substring_index(field_type.extra,';',7)) - char_length(substring_index(field_type.extra,';',6))-1)
          as optional_url,
        substring(field_type.extra,
                  char_length(substring_index(field_type.extra,';',7))+2,
                  char_length(substring_index(field_type.extra,';',8)) - char_length(substring_index(field_type.extra,';',7))-1)
          as optional_param,
        substring(field_type.extra,
                  char_length(substring_index(field_type.extra,';',8))+2,
                  char_length(substring_index(field_type.extra,';',9)) - char_length(substring_index(field_type.extra,';',8))-1)
          as field_pid,
        substring(field_type.extra,
                  char_length(substring_index(field_type.extra,';',9))+2,
                  char_length(substring_index(field_type.extra,';',10)) - char_length(substring_index(field_type.extra,';',9))-1)
          as value_format,
        substring(field_type.extra,
                  char_length(substring_index(field_type.extra,';',10))+2,
                  char_length(substring_index(field_type.extra,';',11)) - char_length(substring_index(field_type.extra,';',10))-1)
          as field_tooltip,
        substring(field_type.extra,
                  char_length(substring_index(field_type.extra,';',11))+2,
                  char_length(substring_index(field_type.extra,';',12)) - char_length(substring_index(field_type.extra,';',11))-1)
          as field_default_value,
        substring(field_type.extra,
                  char_length(substring_index(field_type.extra,';',12))+2,
                  char_length(substring_index(field_type.extra,';',13)) - char_length(substring_index(field_type.extra,';',12))-1)
          as field_unit
      from
        md_list_const field_type
      where
        field_type.classic_id = 53 and field_type.code_id > 0;

    -- -- 自定义常量
    delete from maoding_dynamic_form_field where form_id in (select id from maoding_dynamic_form where company_id is null);
    REPLACE INTO maoding_dynamic_form_field (id,create_date,update_date,deleted,form_id,field_pid,field_title,field_type,field_unit,is_statistics,field_tooltip,
        field_default_value,seq_x,seq_y,required_type)
      SELECT concat('53-',type_id),now(),now(),0,concat('52-',form_id),if(field_pid = '',null,concat('53-',field_pid)),type_name,field_type,field_unit,is_statistics,field_tooltip,
        if(field_default_value='',null,field_default_value),seq_x,seq_y,required_type
      from md_type_form_field;
  END;
call initConst();

-- 动态审批表单群组
DROP PROCEDURE IF EXISTS `initConst`;
CREATE PROCEDURE `initConst`()
  BEGIN
    -- -- 常量
    delete from md_list_const where classic_id = 54;
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (54,0,'动态审批表单群组','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (54,1,'行政审批','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (54,2,'财务审批','');
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (54,3,'项目审批','');

    -- -- 类型
    REPLACE INTO md_list_const (classic_id,code_id,title,extra) VALUES (0,54,'动态审批表单群组','md_type_form_group,maoding_audit_type_group');

    -- -- 视图
    CREATE OR REPLACE VIEW `md_type_form_group` AS
      select
        group_type.code_id as type_id,
        group_type.title   as type_name
      from
        md_list_const group_type
      where
        group_type.classic_id = 54 and group_type.code_id > 0;

    -- -- 自定义常量
    delete from maoding_dynamic_form_group where company_id is null;
    REPLACE INTO maoding_dynamic_form_group (id,create_date,update_date,deleted,group_name,type_id)
      SELECT concat('54-',type_id),now(),now(),0,type_name,type_id
      FROM md_type_form_group;
  END;
call initConst();

-- -- -- 创建及更改常量 -- 结束 -- -- --

-- -- -- 创建及更改视图 -- 开始 -- -- --
-- -- -- 创建及更改视图 -- 结束 -- -- --

-- 固定表单转换成动态表单的初始化数据
  REPLACE INTO   `maoding_dynamic_form` (`id`, `form_name`, `company_id`, `form_type`, `seq`, `status`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`, `form_code`, `group_type_id`, `documentation`, `var_name`, `var_unit`, `is_system`) VALUES ('leave', '请假申请', NULL, '54-1', '1', '1', '0', '2018-09-14 23:43:23', NULL, '2018-09-14 23:43:23', NULL, NULL, NULL, '适用于公司请假审批', '请假时长', '天', NULL);
  REPLACE INTO   `maoding_dynamic_form` (`id`, `form_name`, `company_id`, `form_type`, `seq`, `status`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`, `form_code`, `group_type_id`, `documentation`, `var_name`, `var_unit`, `is_system`) VALUES ('expense', '报销申请', NULL, '54-2', '3', '1', '0', '2018-09-14 23:43:23', NULL, '2018-09-14 23:43:23', NULL, NULL, NULL, '适用于公司报销审批', '报销金额', '元', NULL);
  REPLACE INTO   `maoding_dynamic_form` (`id`, `form_name`, `company_id`, `form_type`, `seq`, `status`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`, `form_code`, `group_type_id`, `documentation`, `var_name`, `var_unit`, `is_system`) VALUES ('costApply', '费用申请', NULL, '54-2', '4', '1', '0', '2018-09-14 23:43:23', NULL, '2018-09-14 23:43:23', NULL, NULL, NULL, '适用于公司费用审批', '费用申请金额', '元', NULL);
  REPLACE INTO   `maoding_dynamic_form` (`id`, `form_name`, `company_id`, `form_type`, `seq`, `status`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`, `form_code`, `group_type_id`, `documentation`, `var_name`, `var_unit`, `is_system`) VALUES ('onBusiness', '出差申请', NULL, '54-1', '2', '1', '0', '2018-09-14 23:43:23', NULL, '2018-09-14 23:43:23', NULL, NULL, NULL, '适用于公司出差审批', '出差时长', '天', NULL);
  REPLACE INTO   `maoding_dynamic_form` (`id`, `form_name`, `company_id`, `form_type`, `seq`, `status`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`, `form_code`, `group_type_id`, `documentation`, `var_name`, `var_unit`, `is_system`) VALUES ('projectPayApply', '付款审批', NULL, '54-3', '5', '1', '0', '2018-09-14 23:43:23', NULL, '2018-09-14 23:43:23', NULL, NULL, NULL, '适用于公司付款审批', '付款审批金额', '万元', NULL);


  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-1', 'leave', NULL, '请假类型', '7', '', '0', '请选择请假类型', NULL, '0', '0', '1', '1', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-2', 'leave', NULL, '开始时间,结束时间', '4', '', '0', '开始时间,结束时间', NULL, '0', '0', '2', '1', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-3', 'leave', NULL, '请假天数', '6', '天', '0', '请输入请假天数', NULL, '0', '0', '3', '1', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-4', 'leave', NULL, '请假事由', '2', '', '0', '请输入', NULL, '0', '0', '4', '0', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-5', 'leave', NULL, '附件', '14', '', '0', '', NULL, '0', '0', '5', '0', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-27', 'leave', NULL, '开始时间,结束时间', '4', '', '0', '开始时间,结束时间', NULL, '0', '1', '2', '1', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-6', 'onBusiness', NULL, '出差地点', '1', '', '0', '请输入', NULL, '0', '0', '1', '1', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-7', 'onBusiness', NULL, '开始时间,结束时间', '4', '', '0', '开始时间,结束时间', NULL, '0', '0', '2', '1', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-8', 'onBusiness', NULL, '出差天数', '6', '天', '0', '请输入出差天数', NULL, '0', '0', '3', '1', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-9', 'onBusiness', NULL, '出差事由', '2', '', '0', '请输入', NULL, '0', '0', '4', '0', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-10', 'onBusiness', NULL, '关联项目', '9', '', '0', '请选择关联项目', NULL, '0', '0', '5', '0', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-11', 'onBusiness', NULL, '附件', '14', '', '0', '', NULL, '0', '0', '6', '0', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-28', 'onBusiness', NULL, '开始时间,结束时间', '4', '', '0', '开始时间,结束时间', NULL, '0', '1', '2', '1', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-12', 'expense', NULL, '报销明细', '12', '', '0', '', NULL, '0', '0', '1', '0', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-13', 'expense', NULL, '附件', '14', '', '0', '', NULL, '0', '0', '2', '0', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-14', 'expense', '53-12', '报销金额', '6', '元', '1', '请输入金额', NULL, '0', '0', '1', '1', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-15', 'expense', '53-12', '报销类型', '9', '', '0', '请选择报销类型', NULL, '0', '0', '2', '1', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-16', 'expense', '53-12', '用途说明', '2', '', '0', '请输入', NULL, '0', '0', '3', '1', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-17', 'expense', '53-12', '关联项目', '15', '', '0', '请选择关联项目', NULL, '0', '0', '4', '0', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-18', 'expense', '53-12', '关联审批', '16', '', '0', '请选择关联审批', NULL, '0', '0', '5', '0', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-19', '52-5', NULL, '收款方', '1', '', '0', '请输入收款方', NULL, '0', '0', '1', '0', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-20', '52-5', NULL, '备注', '2', '', '0', '请输入', NULL, '0', '0', '2', '0', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-21', '52-5', NULL, '费用明细', '12', '', '0', '', NULL, '0', '0', '3', '0', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-22', '52-6', '53-21', '费用金额', '6', '元', '1', '请输入金额', NULL, '0', '0', '1', '1', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-23', '52-6', '53-21', '费用类型', '9', '', '0', '', NULL, '0', '0', '2', '1', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-24', '52-6', '53-21', '用途说明', '2', '', '0', '请输入用途说明', NULL, '0', '0', '3', '1', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-25', '52-6', '53-21', '关联项目', '15', '', '0', '请选择关联项目', NULL, '0', '0', '4', '0', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-26', '52-6', '53-21', '关联审批', '16', '', '0', '请选择关联审批', NULL, '0', '0', '5', '0', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-29', 'costApply', '53-34', '申请金额', '6', '元', '1', '请输入金额', NULL, '0', '0', '1', '1', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-30', 'costApply', '53-34', '费用类型', '9', '', '0', '请选择报销类型', NULL, '0', '0', '2', '1', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-31', 'costApply', '53-34', '用途说明', '2', '', '0', '请输入', NULL, '0', '0', '3', '1', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-32', 'costApply', '53-34', '关联项目', '15', '', '0', '请选择关联项目', NULL, '0', '0', '4', '0', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-33', 'costApply', '53-34', '关联审批', '16', '', '0', '请选择关联审批', NULL, '0', '0', '5', '0', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-34', 'costApply', NULL, '费用明细', '12', '', '0', '', NULL, '0', '0', '1', '0', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
  REPLACE INTO  `maoding_dynamic_form_field` (`id`, `form_id`, `field_pid`, `field_title`, `field_type`, `field_unit`, `is_statistics`, `field_tooltip`, `field_default_value`, `field_select_value_type`, `seq_x`, `seq_y`, `required_type`, `deleted`, `create_date`, `create_by`, `update_date`, `update_by`) VALUES ('53-35', 'costApply', NULL, '附件', '14', '', '0', '', NULL, '0', '0', '2', '0', '0', '2018-09-14 15:55:59', NULL, '2018-09-14 15:55:59', NULL);
