/*
Navicat MySQL Data Transfer

Source Server         : RemotMySQL
Source Server Version : 50625
Source Host           : 172.16.6.71:3306
Source Database       : maoding_qa

Target Server Type    : MYSQL
Target Server Version : 50625
File Encoding         : 65001

Date: 2018-06-07 15:28:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for maoding_app_version
-- ----------------------------
DROP TABLE IF EXISTS `maoding_app_version`;
CREATE TABLE `maoding_app_version` (
  `id` varchar(32) NOT NULL,
  `platform` varchar(32) DEFAULT NULL,
  `versionCode` int(11) DEFAULT NULL,
  `versionName` varchar(32) DEFAULT NULL,
  `updateUrl` varchar(512) DEFAULT NULL,
  `minSdkVersion` varchar(32) DEFAULT NULL,
  `versionDesc` varchar(512) DEFAULT NULL,
  `create_by` varchar(32) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_by` varchar(32) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `mandatory_update` varchar(1) DEFAULT '0' COMMENT '是否强制更新(0,不强制,1，强制)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of maoding_app_version
-- ----------------------------
INSERT INTO `maoding_app_version` VALUES ('002933', 'android', '51', '1.0.0.070401', 'https://www.imaoding.com/download/MaoDing070401.apk', '14', '修改Bug', null, null, null, null, '1');
INSERT INTO `maoding_app_version` VALUES ('00293312', 'android', '52', '1.0.0.070701', 'http://imaoding.oss-cn-shenzhen.aliyuncs.com/MaoDing070701.apk', '14', '1.修复Bug', null, null, null, null, '1');
INSERT INTO `maoding_app_version` VALUES ('0029ca6f89374cc197334b82c5e12345', 'android', '2', '1.0.1', 'idcc_app/DesignPlus.apk', '14', '1.更改手机号码多出“保存”字段\r\n2.更改手机号码缺失一个页面\r\n3.绑定的邮箱为空，保存时系统返回提示“验证码错误或失效”\r\n4.绑定的邮箱没有对邮箱格式进行判断，当邮箱格式不正确进行频繁点击获取验证码时出现卡机\r\n5.性别选择完成后不能自动返回主界面\r\n6.修改姓名输入框缺少一个叉叉\r\n7.团队名称为长数据时，“我创建的”没有显示出来\r\n8.一键离开团队，去掉“暂时”二字\r\n9.解散团队后数据没有实时刷新\r\n10.图队简介页面显示团队全称\r\n11.团队logo显示不全', '', '0000-00-00 00:00:00', '', '0000-00-00 00:00:00', '0');
INSERT INTO `maoding_app_version` VALUES ('0029ca6f89374cc197334b82c5e12346', 'ios', '2', '1.01', 'idcc_app/DesignPlus.dmg', '14', 'sdff', null, '2016-11-09 10:36:34', null, null, '0');
INSERT INTO `maoding_app_version` VALUES ('0029ca6f89374cc197334b82c5e12347', 'ios', '3', '2.0.4', 'idcc_app/DesignPlus.dmg', '14', 'hhhh', null, '2016-11-09 10:39:24', null, null, '0');
INSERT INTO `maoding_app_version` VALUES ('0029ca6f89374cc197334b82c5e12348', 'android', '25', '2.0.5', 'http://172.16.6.73/nav/MaoDing-debug.apk', '14', '测试', null, '2017-03-29 17:16:05', null, null, '1');
INSERT INTO `maoding_app_version` VALUES ('0029ca6f89374cc197334b82c5e12349', 'android', '26', '1.1.2.033101', 'http://172.16.6.73/nav/MaoDing033101.apk', '14', '修复bug', null, null, null, null, '1');
INSERT INTO `maoding_app_version` VALUES ('0029ca6f89374cc197334b82c5e1sdf7', 'android', '63', '1.0.0.030601', 'http://maoding-net.oss-cn-shenzhen.aliyuncs.com/apk/app-release2018030602.apk', '14', '修改Bug', null, '2018-03-06 10:50:05', null, '2018-03-06 10:49:58', '1');
INSERT INTO `maoding_app_version` VALUES ('0029ca6f8c5e12349', 'android', '27', '1.1.2.040101', 'https://www.imaoding.com/download/MaoDing070401.apk', '14', '修复bug', null, null, null, null, '1');
INSERT INTO `maoding_app_version` VALUES ('23281', 'android', '53', '1.1.0.080401', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.idccapp.aty \r\n', '14', '1.修复bug\r\n2.增加自定义群组功能\r\n3.取消项目群组功能', null, null, null, null, '1');
INSERT INTO `maoding_app_version` VALUES ('4ebd3135371411e88c6bf8db88fcba36', 'android', '70', '1.0.0.040302', 'https://maoding-net.oss-cn-shenzhen.aliyuncs.com/apk/app-release2018-04-03-02.apk', '16', '修复BUG', null, '2018-04-03 15:56:05', null, null, '1');
INSERT INTO `maoding_app_version` VALUES ('4fd2addf364f11e88c6bf8db88fcba36', 'android', '68', '1.0.0.040203', 'https://maoding-net.oss-cn-shenzhen.aliyuncs.com/apk/app-release2018-04-02-03.apk', '16', '修复bug', null, '2018-04-02 16:24:54', null, null, '1');
INSERT INTO `maoding_app_version` VALUES ('5839463d361a11e88c6bf8db88fcba36', 'android', '67', '1.0.0.040201', 'https://maoding-net.oss-cn-shenzhen.aliyuncs.com/apk/app-release2018-04-02-02.apk', '16', '修复bug', null, '2018-04-02 10:05:44', null, null, '1');
INSERT INTO `maoding_app_version` VALUES ('c23b486036e011e88c6bf8db88fcba36', 'android', '69', '1.0.0.040301', 'https://maoding-net.oss-cn-shenzhen.aliyuncs.com/apk/app-release2018-04-03-01.apk', '16', '修复BUG', null, '2018-04-03 09:47:04', null, null, '1');
INSERT INTO `maoding_app_version` VALUES ('e0354c7d220a11e887548038bc0f0103', 'android', '64', '1.0.0.030701', 'http://maoding-net.oss-cn-shenzhen.aliyuncs.com/apk/app-release2018030701.apk', '14', '修改BUG', '', '2018-03-07 21:24:34', '', '0000-00-00 00:00:00', '1');
INSERT INTO `maoding_app_version` VALUES ('e0354c7d220a11e887548038bc0f234d', 'android', '63', '1.0.0.031401', 'http://maoding-net.oss-cn-shenzhen.aliyuncs.com/apk/app-release2018031401.apk', '16', '版本更新', '', '2018-03-14 09:52:34', '', '0000-00-00 00:00:00', '1');
