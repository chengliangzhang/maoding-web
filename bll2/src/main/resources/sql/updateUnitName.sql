-- 建立更新单位存储过程
DROP PROCEDURE IF EXISTS `updateUnitName`;
CREATE PROCEDURE `updateUnitName`()
BEGIN
  -- maoding_web_company_property -- 公司自定义属性表
  CREATE TABLE IF NOT EXISTS `maoding_web_company_property` (
      `id` char(32) NOT NULL COMMENT '项目自定义属性ID编号',
      `field_name` varchar(255) NOT NULL DEFAULT '' COMMENT '字段名称',
      `unit_name` varchar(20) NOT NULL DEFAULT '' COMMENT '字段单位编号，单位名称在const表内记录',
      `company_id` char(32) DEFAULT NULL COMMENT '项目自定义属性所属组织的ID',
      `be_selected` tinyint(1) unsigned DEFAULT '1' COMMENT '在最近一次项目定义中是否有被选中，0：否，1：是',
      `be_default` tinyint(1) unsigned DEFAULT '0' COMMENT '是否默认属性（不可修改删除），0：否，1：是',
      `sequencing` int(8) unsigned DEFAULT '0' COMMENT '排序次序',
      `deleted` tinyint(1) unsigned DEFAULT '0' COMMENT '已删除',
      `create_date` datetime DEFAULT NULL COMMENT '创建时间',
      `create_by` char(32) DEFAULT NULL COMMENT '创建人',
      `update_date` datetime DEFAULT NULL COMMENT '更新时间',
      `update_by` char(32) DEFAULT NULL COMMENT '更新人',
      `unit_id` smallint(2) DEFAULT NULL,
      PRIMARY KEY (`id`),
      KEY `company_id` (`company_id`),
      KEY `deleted` (`deleted`),
      KEY `unit_id` (`unit_id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  -- maoding_web_project_property -- 项目自定义属性表
  CREATE TABLE IF NOT EXISTS `maoding_web_project_property` (
      `id` char(32) NOT NULL COMMENT '项目自定义属性ID编号',
      `project_id` char(32) NOT NULL DEFAULT '' COMMENT '项目自定义属性所属项目的ID',
      `field_name` varchar(255) NOT NULL DEFAULT '' COMMENT '字段名称',
      `unit_name` varchar(20) NOT NULL DEFAULT '' COMMENT '字段单位编号，单位名称在const表内记录',
      `field_value` varchar(255) DEFAULT NULL COMMENT '自定义属性的值，统一格式化为字符串',
      `sequencing` int(8) unsigned DEFAULT '0' COMMENT '排序次序',
      `deleted` tinyint(1) unsigned DEFAULT '0' COMMENT '已删除',
      `create_date` datetime DEFAULT NULL COMMENT '创建时间',
      `create_by` char(32) DEFAULT NULL COMMENT '创建人',
      `update_date` datetime DEFAULT NULL COMMENT '更新时间',
      `update_by` char(32) DEFAULT NULL COMMENT '更新人',
      `unit_id` smallint(2) DEFAULT NULL,
      PRIMARY KEY (`id`),
      KEY `project_id` (`project_id`),
      KEY `deleted` (`deleted`),
      KEY `unit_id` (`unit_id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  update maoding_web_company_property set unit_name='㎡' where unit_name = 'm2';
  update maoding_web_project_property set unit_name='㎡' where unit_name = 'm2';
END;

call updateUnitName();