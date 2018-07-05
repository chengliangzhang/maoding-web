-- -- 添加带类型的组织关系的测试数据
DROP PROCEDURE IF EXISTS `addTestRelation`;
CREATE PROCEDURE `addTestRelation`()
BEGIN
	update
END;

-- createDefaultUser -- 创建默认用户
DROP PROCEDURE IF EXISTS `createDefaultUser`;
CREATE PROCEDURE `createDefaultUser`()
BEGIN
	if not exists (select 1 from maoding_web_account where cellphone='13680809727') then
		REPLACE INTO maoding_web_account (id,user_name,password,cellphone,status,create_date,active_time)
		VALUES ('5ffee496fa814ea4b6d26a9208b00a0b','sun','E10ADC3949BA59ABBE56E057F20F883E','13680809727','0','2017-07-04 11:59:37','2017-07-03 18:57:36');
	end if;
END;

call createDefaultUser();
