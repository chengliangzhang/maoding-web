/*
Navicat MySQL Data Transfer

Source Server         : LocalMySQL
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : maoding_test

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2017-12-08 17:00:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for maoding_corp_company_disk
-- ----------------------------
DROP TABLE IF EXISTS `maoding_corp_company_disk`;
CREATE TABLE `maoding_corp_company_disk` (
  `id` varchar(32) NOT NULL COMMENT '主键id，uuid',
  `company_id` varchar(32) NOT NULL COMMENT '团队Id',
  `total_size` bigint(20) NOT NULL DEFAULT '0' COMMENT '总容量',
  `corp_size` bigint(20) NOT NULL DEFAULT '0' COMMENT '协同占用容量',
  `docmgr_size` bigint(20) NOT NULL DEFAULT '0' COMMENT '文档库占用容量',
  `other_size` bigint(20) NOT NULL DEFAULT '0' COMMENT '其他文件占用容量',
  `free_size` bigint(20) NOT NULL DEFAULT '0' COMMENT '剩余容量',
  `corp_on_cloud` bit(1) NOT NULL DEFAULT b'0' COMMENT '协同是否云端部署',
  `up_version` bigint(20) NOT NULL DEFAULT '0' COMMENT '版本控制',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  KEY `company_id` (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AVG_ROW_LENGTH=8192 COMMENT='组织磁盘空间';

-- ----------------------------
-- Records of maoding_corp_company_disk
-- ----------------------------
INSERT INTO `maoding_corp_company_disk` VALUES ('  ', '  ', '5368709120', '0', '0', '0', '5368709120', '', '2', '2017-06-20 14:43:16', null, '2017-06-20 14:45:18', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('02077b2b62d143d7b912c6c234cc5d47', '02077b2b62d143d7b912c6c234cc5d47', '5368709120', '0', '0', '0', '5368709120', '', '2', '2017-06-29 18:10:36', null, '2017-06-29 18:10:37', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('04f01d6e16ce42fbb294b71810481b0c', '04f01d6e16ce42fbb294b71810481b0c', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-28 16:56:12', null, '2017-08-28 16:56:12', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('0655d747ddb44de793d37ec4626d1b05', '0655d747ddb44de793d37ec4626d1b05', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-27 16:12:06', null, '2017-07-27 16:12:06', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('0681fb869d9c479489e81d1a1937e394', '0681fb869d9c479489e81d1a1937e394', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-03 15:15:05', null, '2017-07-03 15:15:05', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('06973c8f8c964dde93db348e87a26155', '06973c8f8c964dde93db348e87a26155', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-21 19:24:19', null, '2017-07-21 19:24:19', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('089836c6489647c1a8dd23704e56a678', '089836c6489647c1a8dd23704e56a678', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-06-21 14:27:39', null, '2017-06-21 14:27:39', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('09a66913de6e4e94820b7855a4730e69', '09a66913de6e4e94820b7855a4730e69', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-10 16:47:20', null, '2017-08-10 16:47:20', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('0a3acae34bbf4c279a4a202a3291cd8e', '0a3acae34bbf4c279a4a202a3291cd8e', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-06-27 10:59:44', null, '2017-06-27 10:59:44', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('0b88908f89714405b095d82e5f16ecd1', '0b88908f89714405b095d82e5f16ecd1', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-06-15 11:04:36', null, '2017-06-15 11:04:36', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('0c42529c37e84c7893699592ba29206f', '0c42529c37e84c7893699592ba29206f', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-11 13:04:15', null, '2017-08-11 13:04:15', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('1', '1', '5368709120', '0', '100', '0', '5368709020', '', '200', '2017-06-23 14:39:02', null, '2017-06-23 14:39:08', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('1089eff4a43041a1816cda43a0b98dd0', '1089eff4a43041a1816cda43a0b98dd0', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-21 18:46:45', null, '2017-07-21 18:46:45', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('10e1370dd1ee4371b9fc96ad78c0b4b4', '10e1370dd1ee4371b9fc96ad78c0b4b4', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-27 16:12:15', null, '2017-07-27 16:12:15', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('1234', '1234', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-11-21 21:09:10', null, '2017-11-21 21:09:10', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('12833eea01a44ad09ef8895c14c6a8e5', '12833eea01a44ad09ef8895c14c6a8e5', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-11 17:52:46', null, '2017-08-11 17:52:46', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('13958A43827D4A3E88DFA7E66F605A4C', '13958A43827D4A3E88DFA7E66F605A4C', '5368709120', '0', '0', '392089', '5368317031', '', '3', '2017-08-28 17:13:32', null, '2017-08-28 17:14:11', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('1419726bdc42426b893589decfc9aee5', '1419726bdc42426b893589decfc9aee5', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-21 19:26:03', null, '2017-07-21 19:26:03', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('142b06c5bcf544f5b64bfdfa2cc5b5fc', '142b06c5bcf544f5b64bfdfa2cc5b5fc', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-25 10:30:07', null, '2017-07-25 10:30:07', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('144ae41ac80d4b1d9d62d58567ebe720', '144ae41ac80d4b1d9d62d58567ebe720', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-10-24 09:51:52', null, '2017-10-24 09:51:52', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('1491eb11fd2d4312b7e8507b648009e7', '1491eb11fd2d4312b7e8507b648009e7', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-14 15:12:40', null, '2017-08-14 15:12:40', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('198a4523603c41bca58e8e0d0442c694', '198a4523603c41bca58e8e0d0442c694', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-07 15:47:36', null, '2017-08-07 15:47:36', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('1a3c6c4139e84974872fe1b42abf225f', '1a3c6c4139e84974872fe1b42abf225f', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-18 14:28:37', null, '2017-08-18 14:28:37', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('1b71df1c276f4ed6a1d7d29e9af6e4e7', '1b71df1c276f4ed6a1d7d29e9af6e4e7', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-06-20 14:30:26', null, '2017-06-20 14:30:26', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('1bc5515b52ec474e9528427a6b253410', '1bc5515b52ec474e9528427a6b253410', '5368709120', '0', '0', '7547364', '5361161756', '', '28', '2017-07-31 18:08:34', null, '2017-11-03 15:59:17', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('2', '2', '5368709120', '0', '0', '100', '5368709020', '', '200', '2017-06-23 14:39:02', null, '2017-06-23 14:39:08', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('2022fc5f91a64aa6b8833b6edab78f70', '2022fc5f91a64aa6b8833b6edab78f70', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-14 10:02:32', null, '2017-07-14 10:02:32', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('2153c5271b0940569433c0c8d3764e43', '2153c5271b0940569433c0c8d3764e43', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-21 19:25:08', null, '2017-07-21 19:25:08', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('21d20bc7bb154b6d81cf3291fe1fe2ee', '21d20bc7bb154b6d81cf3291fe1fe2ee', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-06 11:40:35', null, '2017-07-06 11:40:35', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('23579892746f4a5aaf630132f6d93884', '23579892746f4a5aaf630132f6d93884', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-06-20 12:54:43', null, '2017-06-20 12:54:43', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('24bc8690a3ac437d8df890d18144e566', '24bc8690a3ac437d8df890d18144e566', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-14 15:10:48', null, '2017-07-14 15:10:48', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('24c0f4d93508465eb5b2e3f7497307e8', '24c0f4d93508465eb5b2e3f7497307e8', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-06-24 12:08:44', null, '2017-06-24 12:08:44', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('25565aced649492f92f1ec84ffce4372', '25565aced649492f92f1ec84ffce4372', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-09-28 11:42:41', null, '2017-09-28 11:42:41', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('26a6e3e582124694ae400d4685882bb1', '26a6e3e582124694ae400d4685882bb1', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-06-20 14:30:34', null, '2017-06-20 14:30:34', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('2b699d87b4fb4ff5a03a7f15ecece165', '2b699d87b4fb4ff5a03a7f15ecece165', '5368709120', '0', '0', '1277090', '5367432030', '', '1', '2017-08-01 18:42:22', null, '2017-08-01 18:42:22', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('2f2103494eb44a1fbb52ac73de556009', '2f2103494eb44a1fbb52ac73de556009', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-21 18:59:09', null, '2017-07-21 18:59:09', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('2f618ad2f2044ceabd8e5fd2a812f6f1', '2f618ad2f2044ceabd8e5fd2a812f6f1', '5368709120', '0', '2622', '0', '5368706498', '', '3', '2017-08-07 12:43:18', null, '2017-11-22 12:45:59', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('2FE5E582E94347B7967D89BB13336C4C', '2FE5E582E94347B7967D89BB13336C4C', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-02 17:29:55', null, '2017-07-02 17:29:55', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('3', '3', '5368709120', '0', '100', '100', '5368708920', '', '400', '2017-06-23 14:39:02', null, '2017-06-23 14:39:08', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('301b9cefd6544616be172c5e1ce42802', '301b9cefd6544616be172c5e1ce42802', '5368709120', '0', '0', '344339490', '5024369630', '', '36', '2017-06-15 14:55:44', null, '2017-06-16 11:08:51', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('3167002e912f444db6f6e011df38cfe6', '3167002e912f444db6f6e011df38cfe6', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-11 11:06:18', null, '2017-08-11 11:06:18', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('32c3cf43ba3e4becab484a6eb2a63018', '32c3cf43ba3e4becab484a6eb2a63018', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-10 17:36:20', null, '2017-08-10 17:36:20', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('3554ca18659e460c87130307363b08c6', '3554ca18659e460c87130307363b08c6', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-06-21 14:29:56', null, '2017-06-21 14:29:56', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('36c474cf2cfd4122abcd99a184f0aeb5', '36c474cf2cfd4122abcd99a184f0aeb5', '5368709120', '0', '0', '187504', '5368521616', '', '1', '2017-06-16 11:47:10', null, '2017-06-16 11:47:10', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('36d55f92c12f4aefaa2f27110cd14325', '36d55f92c12f4aefaa2f27110cd14325', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-03 01:13:39', null, '2017-07-03 01:13:39', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('3795abf618e04b209b8b49e551369b13', '3795abf618e04b209b8b49e551369b13', '5368709120', '0', '1820415582', '0', '3548293538', '', '133', '2017-09-07 17:24:53', null, '2017-10-26 10:27:32', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('3ed17bf238a2429c8a6f3aed0fd67fd4', '3ed17bf238a2429c8a6f3aed0fd67fd4', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-06-20 16:42:51', null, '2017-06-20 16:42:51', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('40c20cf0c6f84a88a4b83c7ea33300c9', '40c20cf0c6f84a88a4b83c7ea33300c9', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-21 18:46:21', null, '2017-07-21 18:46:21', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('41026918717a47009722f85289addf02', '41026918717a47009722f85289addf02', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-21 19:22:43', null, '2017-07-21 19:22:43', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('432e56164ac04857900df9b3a5d30353', '432e56164ac04857900df9b3a5d30353', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-07 14:26:50', null, '2017-08-07 14:26:50', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('43626bb52a364fd9b2f0f0dc1e2a030d', '43626bb52a364fd9b2f0f0dc1e2a030d', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-15 10:51:13', null, '2017-08-15 10:51:13', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('43969c859639448d9060385e825d7822', '43969c859639448d9060385e825d7822', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-06-19 15:45:29', null, '2017-06-19 15:45:29', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('48b4446a968544248ad7ef811375bf9b', '48b4446a968544248ad7ef811375bf9b', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-06-17 10:38:15', null, '2017-06-17 10:38:15', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('496cdd4f1e2b4a219d56039ee78e4785', '496cdd4f1e2b4a219d56039ee78e4785', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-06-21 14:22:07', null, '2017-06-21 14:22:07', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('498665e42f9242ff87c67c2dff8771f8', '498665e42f9242ff87c67c2dff8771f8', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-07 17:22:49', null, '2017-07-07 17:22:49', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('4d79a102996843c9823d52ac4e1ebe3b', '4d79a102996843c9823d52ac4e1ebe3b', '5368709120', '0', '169205913', '0', '5199503207', '', '26', '2017-07-21 18:49:41', null, '2017-07-21 19:08:47', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('4ea54f9af5214be7b27a09eb0dfa6cea', '4ea54f9af5214be7b27a09eb0dfa6cea', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-14 17:28:01', null, '2017-07-14 17:28:01', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('506bd497179847439ec915ad650413e2', '506bd497179847439ec915ad650413e2', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-17 11:04:40', null, '2017-08-17 11:04:40', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('515b1c2e87e943e6b07c78967d51615e', '515b1c2e87e943e6b07c78967d51615e', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-16 14:11:00', null, '2017-08-16 14:11:00', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('5339147901b34a0b99e16b6ac7054d64', '5339147901b34a0b99e16b6ac7054d64', '5368709120', '0', '53314652', '13851333', '5301543135', '', '30', '2017-06-13 15:16:15', null, '2017-06-28 15:02:53', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('5410d1719f2a4fb688b8f724160c7d28', '5410d1719f2a4fb688b8f724160c7d28', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-14 15:24:56', null, '2017-07-14 15:24:56', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('57cd5136bc254617afcab5decb9b2b57', '57cd5136bc254617afcab5decb9b2b57', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-11 17:54:15', null, '2017-08-11 17:54:15', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('59064f99e8fd4ebc8415edf6e426bbca', '59064f99e8fd4ebc8415edf6e426bbca', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-10 10:56:14', null, '2017-08-10 10:56:14', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('59b7914ed7024fd5ad68e3736b2eff3c', '59b7914ed7024fd5ad68e3736b2eff3c', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-07 14:47:59', null, '2017-08-07 14:47:59', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('5a1e0e5bca0f45adb2889ccc1e294a12', '5a1e0e5bca0f45adb2889ccc1e294a12', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-18 09:50:48', null, '2017-08-18 09:50:48', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('5a624c0b81554c2db6004e32679dc0ef', '5a624c0b81554c2db6004e32679dc0ef', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-11-07 16:25:19', null, '2017-11-07 16:25:19', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('5e0e1532f5e746ab86db8f04c656a8c7', '5e0e1532f5e746ab86db8f04c656a8c7', '5368709120', '0', '0', '0', '5368709120', '', '2', '2017-06-29 15:29:03', null, '2017-06-29 15:29:05', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('5e26e33728cf4149a480af66665ceb2f', '5e26e33728cf4149a480af66665ceb2f', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-18 14:34:40', null, '2017-08-18 14:34:40', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('5e626067af764b3ca260b829512a5e59', '5e626067af764b3ca260b829512a5e59', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-16 17:01:16', null, '2017-08-16 17:01:16', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('5e88f343b28b4b508ac181cd6c8de6ea', '5e88f343b28b4b508ac181cd6c8de6ea', '5368709120', '0', '206826', '16650665', '5351851629', '', '18', '2017-06-14 11:13:33', null, '2017-07-31 18:23:49', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('5f66214997cf42aba8d627d33ffd1c73', '5f66214997cf42aba8d627d33ffd1c73', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-02 17:55:24', null, '2017-07-02 17:55:24', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('613e40b2f46c4202a3d1f1343345d906', '613e40b2f46c4202a3d1f1343345d906', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-07 14:49:43', null, '2017-08-07 14:49:43', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('621507a5ad65455087933261bb9fb159', '621507a5ad65455087933261bb9fb159', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-15 10:15:09', null, '2017-08-15 10:15:09', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('628b5ec98e364c8280b5e61aab25a710', '628b5ec98e364c8280b5e61aab25a710', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-21 19:24:43', null, '2017-07-21 19:24:43', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('64486f128da14f35932e63c5be0fc32c', '64486f128da14f35932e63c5be0fc32c', '5368709120', '0', '3960567', '0', '5364748553', '', '5', '2017-06-29 10:34:50', null, '2017-07-05 14:13:54', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('648aadfd589b450eb23c516dd608dc72', '648aadfd589b450eb23c516dd608dc72', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-06-27 16:17:12', null, '2017-06-27 16:17:12', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('6531376e8e314989a05986839cde2d13', '6531376e8e314989a05986839cde2d13', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-13 10:22:16', null, '2017-07-13 10:22:16', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('66564602eed44ca6964aca8deabf077a', '66564602eed44ca6964aca8deabf077a', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-06 11:38:39', null, '2017-07-06 11:38:39', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('67cfd927bb394fb29d16a3ca9e25560f', '67cfd927bb394fb29d16a3ca9e25560f', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-21 19:24:03', null, '2017-07-21 19:24:03', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('682760c2411d497cb85165649e31efb2', '682760c2411d497cb85165649e31efb2', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-11 17:52:03', null, '2017-08-11 17:52:03', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('6ab776f1114842b5aa7d749e5acbe4c2', '6ab776f1114842b5aa7d749e5acbe4c2', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-11 17:56:47', null, '2017-08-11 17:56:47', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('6b3cbf9a5170413faa15d2cc7025b150', '6b3cbf9a5170413faa15d2cc7025b150', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-17 11:04:25', null, '2017-08-17 11:04:25', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('6c5a3891db644173afda79be9f76709b', '6c5a3891db644173afda79be9f76709b', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-12 15:18:07', null, '2017-07-12 15:18:07', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('6d09397a71814dea90383b8604c5c121', '6d09397a71814dea90383b8604c5c121', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-08 10:27:39', null, '2017-08-08 10:27:39', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('6d8dfe296d7b498898edd196e8bd94d9', '6d8dfe296d7b498898edd196e8bd94d9', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-09-27 10:56:56', null, '2017-09-27 10:56:56', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('6de3732ae1cf4ea3ab01e7f7939dc802', '6de3732ae1cf4ea3ab01e7f7939dc802', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-27 16:06:33', null, '2017-07-27 16:06:33', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('6f537935d7f44d57b3778540d70bf870', '6f537935d7f44d57b3778540d70bf870', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-07 15:05:14', null, '2017-08-07 15:05:14', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('73cecd35bb024713ba09ff1593ce6925', '73cecd35bb024713ba09ff1593ce6925', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-16 11:32:13', null, '2017-08-16 11:32:13', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('74308147cd954c5fbb8d488b07aee8ac', '74308147cd954c5fbb8d488b07aee8ac', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-06 11:39:05', null, '2017-07-06 11:39:05', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('75516793e8b143fcbab6dec29c9b2ad2', '75516793e8b143fcbab6dec29c9b2ad2', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-14 15:43:14', null, '2017-07-14 15:43:14', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('7661faa2fea44c2285918ae6127ac5cd', '7661faa2fea44c2285918ae6127ac5cd', '5368709120', '0', '0', '5886020', '5362823100', '', '5', '2017-07-12 10:39:56', null, '2017-09-18 18:39:37', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('7938d9cd719e4ee0900f7f76609f61c7', '7938d9cd719e4ee0900f7f76609f61c7', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-21 19:26:37', null, '2017-07-21 19:26:37', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('79e058954c4045f2925651aaca08f37c', '79e058954c4045f2925651aaca08f37c', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-06-19 14:31:19', null, '2017-06-19 14:31:19', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('7acabcf49b0d4392a86ce3ad84dda55c', '7acabcf49b0d4392a86ce3ad84dda55c', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-02 16:34:47', null, '2017-07-02 16:34:47', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('7ba47a2a65a9472c97157a543ede536a', '7ba47a2a65a9472c97157a543ede536a', '5368709120', '0', '0', '1192938', '5367516182', '', '1', '2017-08-09 15:30:34', null, '2017-08-09 19:38:36', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('7c9b917eba6548458848c2e3801dceeb', '7c9b917eba6548458848c2e3801dceeb', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-11 17:14:18', null, '2017-08-11 17:14:18', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('80189a702e4f424f9904bd48ae0986e8', '80189a702e4f424f9904bd48ae0986e8', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-10 15:26:53', null, '2017-08-10 15:26:53', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('80c89beb875d44cf8466755c1c9f382f', '80c89beb875d44cf8466755c1c9f382f', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-21 18:55:36', null, '2017-07-21 18:55:36', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('8823569d53b346e4bc7e66a02fff99c7', '8823569d53b346e4bc7e66a02fff99c7', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-05 16:40:49', null, '2017-07-05 16:40:49', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('8be6282b814d46c6a37311c1852113c8', '8be6282b814d46c6a37311c1852113c8', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-14 15:08:43', null, '2017-07-14 15:08:43', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('8dad6fda622b4c4ebb6c891a3f1d9d60', '8dad6fda622b4c4ebb6c891a3f1d9d60', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-04 17:06:22', null, '2017-07-04 17:06:22', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('8fa380531eab41d88f6725444bf92abd', '8fa380531eab41d88f6725444bf92abd', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-06-17 18:51:51', null, '2017-06-17 18:51:51', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('8fc2332074384926b9e58261887bc799', '8fc2332074384926b9e58261887bc799', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-06-17 15:06:31', null, '2017-06-17 15:06:31', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('914092d0ad8f4dc18a89711a571233be', '914092d0ad8f4dc18a89711a571233be', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-07 15:16:50', null, '2017-08-07 15:16:50', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('9201b46f4142445cb56992789a6c0530', '9201b46f4142445cb56992789a6c0530', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-07 11:10:39', null, '2017-08-07 11:10:39', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('9203865c9fd540258d1f73404491cf23', '9203865c9fd540258d1f73404491cf23', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-18 14:33:38', null, '2017-08-18 14:33:38', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('94327aa614d2451c95911b512203face', '94327aa614d2451c95911b512203face', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-14 15:05:22', null, '2017-07-14 15:05:22', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('96187fdf2bfa40c4b3154fee3c7c539c', '96187fdf2bfa40c4b3154fee3c7c539c', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-21 18:59:39', null, '2017-07-21 18:59:39', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('97373c0542f74aee9d3051dcc8eced43', '97373c0542f74aee9d3051dcc8eced43', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-08 10:15:47', null, '2017-08-08 10:15:47', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('99d1800ecab942fc8ad08f2abde25555', '99d1800ecab942fc8ad08f2abde25555', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-21 18:52:19', null, '2017-07-21 18:52:19', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('9a7a8010ff3e4b549777c8d42b02f78a', '9a7a8010ff3e4b549777c8d42b02f78a', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-16 10:07:20', null, '2017-08-16 10:07:20', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('9a8c567c222b4a8bbd37f948968b8181', '9a8c567c222b4a8bbd37f948968b8181', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-14 17:27:51', null, '2017-07-14 17:27:51', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('9bf35e2a01464158b09b6f7769405c82', '9bf35e2a01464158b09b6f7769405c82', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-08 18:26:53', null, '2017-08-08 18:26:53', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('9c84f709c4454836b70afde48e38970f', '9c84f709c4454836b70afde48e38970f', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-08 14:55:17', null, '2017-08-08 14:55:17', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('9d5fd47651664cbcbe8c37ef1a765e76', '9d5fd47651664cbcbe8c37ef1a765e76', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-21 18:54:00', null, '2017-07-21 18:54:00', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('9db0999049e9450d83bc367beb81fbb8', '9db0999049e9450d83bc367beb81fbb8', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-06 11:39:09', null, '2017-07-06 11:39:09', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('a07b656aba104f4bb97753baca1afe31', 'a07b656aba104f4bb97753baca1afe31', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-11 17:55:02', null, '2017-08-11 17:55:02', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('a11574afb53346329187279be935b995', 'a11574afb53346329187279be935b995', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-05 17:48:34', null, '2017-07-05 17:48:34', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('a4adffa9f0b4452dbf795cf174c59c80', 'a4adffa9f0b4452dbf795cf174c59c80', '5368709120', '0', '0', '12366164', '5356342956', '', '21', '2017-06-17 10:38:01', null, '2017-08-16 15:13:05', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('a5c5b1d99068453fb05fa0ce8b4fbc7f', 'a5c5b1d99068453fb05fa0ce8b4fbc7f', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-09-14 10:27:50', null, '2017-09-14 10:27:50', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('a615aa02cdec4622afe6d06634fbf279', 'a615aa02cdec4622afe6d06634fbf279', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-02 16:15:50', null, '2017-07-02 16:15:50', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('a709ce547a904375bb3ed91420585699', 'a709ce547a904375bb3ed91420585699', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-21 19:23:50', null, '2017-07-21 19:23:50', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('a9ce8ed59d4f48429c718a5d4f840340', 'a9ce8ed59d4f48429c718a5d4f840340', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-11 17:57:13', null, '2017-08-11 17:57:13', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('acaed9df18a046fe8fcb9fa417c8fead', 'acaed9df18a046fe8fcb9fa417c8fead', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-06-21 10:13:09', null, '2017-06-21 10:13:09', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('ad8924fa7fdf4239b97ccd06e82fac57', 'ad8924fa7fdf4239b97ccd06e82fac57', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-02 15:34:11', null, '2017-07-02 15:34:11', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('ad89564855ad4cc2a39bcb602b0a02a8', 'ad89564855ad4cc2a39bcb602b0a02a8', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-10-20 15:46:56', null, '2017-10-20 15:46:56', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('af39046f4394418eae344735db005e61', 'af39046f4394418eae344735db005e61', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-06-15 11:04:22', null, '2017-06-15 11:04:22', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('b06145eb3879409a8bd40722f07e0385', 'b06145eb3879409a8bd40722f07e0385', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-09-27 10:55:55', null, '2017-09-27 10:55:55', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('b1aa7e9bd14f4844b6e23691f133d92e', 'b1aa7e9bd14f4844b6e23691f133d92e', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-06-27 17:02:36', null, '2017-06-27 17:02:36', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('b3595ee4e2744a7d8f577ecbb99f5e52', 'b3595ee4e2744a7d8f577ecbb99f5e52', '5368709120', '0', '565', '0', '5368708555', '', '2', '2017-06-14 11:11:52', null, '2017-06-14 11:19:21', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('b35a6ee08e6349dcb06a81a06e572291', 'b35a6ee08e6349dcb06a81a06e572291', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-09-27 10:57:02', null, '2017-09-27 10:57:02', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('b520f4f628b24192b62139451d403bd0', 'b520f4f628b24192b62139451d403bd0', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-14 11:25:45', null, '2017-07-14 11:25:45', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('b67a90222ab64af1834464d2d496e628', 'b67a90222ab64af1834464d2d496e628', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-05 09:43:04', null, '2017-07-05 09:43:04', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('b8c98b3ef92b44e59e43827279dc05f0', 'b8c98b3ef92b44e59e43827279dc05f0', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-06-21 15:19:44', null, '2017-06-21 15:19:44', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('b9018c966791447d8a56795b24a7ce14', 'b9018c966791447d8a56795b24a7ce14', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-15 15:35:44', null, '2017-08-15 15:35:44', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('be4ef189f7494486b003add03f75b705', 'be4ef189f7494486b003add03f75b705', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-11 17:54:40', null, '2017-08-11 17:54:40', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('be6d43edf17b4b12b9d0ab315aff1404', 'be6d43edf17b4b12b9d0ab315aff1404', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-11 17:52:24', null, '2017-08-11 17:52:24', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('bea872cded6f47b18e78aec31557154c', 'bea872cded6f47b18e78aec31557154c', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-11 17:56:05', null, '2017-08-11 17:56:05', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('c1bf7892c77a4afc84969740e1310dad', 'c1bf7892c77a4afc84969740e1310dad', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-08 11:05:36', null, '2017-08-08 11:05:36', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('c2a52f9ce8aa4b8d93b8d93ad600997f', 'c2a52f9ce8aa4b8d93b8d93ad600997f', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-11 17:59:48', null, '2017-08-11 17:59:48', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('c44980e8c12a44feb50f34bf22c9827e', 'c44980e8c12a44feb50f34bf22c9827e', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-07 17:05:20', null, '2017-08-07 17:05:20', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('c681825620724230ba5a6a225f55242a', 'c681825620724230ba5a6a225f55242a', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-08 14:56:36', null, '2017-08-08 14:56:36', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('c76ad928020e4806b0f150dad3b22071', 'c76ad928020e4806b0f150dad3b22071', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-11 17:55:31', null, '2017-08-11 17:55:31', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('c82dca53194b4c1e96bf1dd028fbef0c', 'c82dca53194b4c1e96bf1dd028fbef0c', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-11-07 16:58:57', null, '2017-11-07 16:58:57', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('c8944e82e81043c5ba4e6de4dc3eae4b', 'c8944e82e81043c5ba4e6de4dc3eae4b', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-16 10:47:33', null, '2017-08-16 10:47:33', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('c92553c16c0742008c093751580020ba', 'c92553c16c0742008c093751580020ba', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-21 19:24:32', null, '2017-07-21 19:24:32', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('c936db4173404054a3e6a6965637afb3', 'c936db4173404054a3e6a6965637afb3', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-06-20 16:30:05', null, '2017-06-20 16:30:05', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('ca347dc09bec4f79ac9443c218ccea91', 'ca347dc09bec4f79ac9443c218ccea91', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-10 17:40:10', null, '2017-08-10 17:40:10', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('cc1751b060e049d8a7c599a9ad7def46', 'cc1751b060e049d8a7c599a9ad7def46', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-02 17:29:36', null, '2017-07-02 17:29:36', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('cc6b1b9889474b31bcab3634d3e090ab', 'cc6b1b9889474b31bcab3634d3e090ab', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-07 14:50:11', null, '2017-08-07 14:50:11', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('ceba3a2858214defba64d307f8a132a0', 'ceba3a2858214defba64d307f8a132a0', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-09 11:14:33', null, '2017-08-09 11:14:33', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('d18bda5241da402c965923aca6554fb0', 'd18bda5241da402c965923aca6554fb0', '5368709120', '0', '313564914', '132983136', '4922161070', '\0', '104', '2017-06-13 16:46:20', null, '2017-11-23 15:00:45', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('d37c3c80544843629b65e51999861e16', 'd37c3c80544843629b65e51999861e16', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-09-28 11:44:04', null, '2017-09-28 11:44:04', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('d3f159aad0da4f939bba6914a707a84b', 'd3f159aad0da4f939bba6914a707a84b', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-21 19:24:43', null, '2017-07-21 19:24:43', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('d4193a9f3953451e83afacfc5e77d715', 'd4193a9f3953451e83afacfc5e77d715', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-17 14:54:52', null, '2017-07-17 14:54:52', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('d57b744481e54da0abf0e1a09d31bdf6', 'd57b744481e54da0abf0e1a09d31bdf6', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-06-19 16:02:07', null, '2017-06-19 16:02:07', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('d60bcc8621e9457c81833e04e99c85b3', 'd60bcc8621e9457c81833e04e99c85b3', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-02 10:58:55', null, '2017-08-02 10:58:55', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('d7dc75e633c741bbab254f6d62551034', 'd7dc75e633c741bbab254f6d62551034', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-06-15 18:27:04', null, '2017-06-15 18:27:04', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('d8e3a5bc795e4a7d89deddba3c2bab28', 'd8e3a5bc795e4a7d89deddba3c2bab28', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-14 09:48:50', null, '2017-07-14 09:48:50', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('dd32ec2e32b34143833b56cfc6435480', 'dd32ec2e32b34143833b56cfc6435480', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-16 11:20:38', null, '2017-08-16 11:20:38', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('e2451f2efde242a3a05c729f5a15d34e', 'e2451f2efde242a3a05c729f5a15d34e', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-06-20 18:47:34', null, '2017-06-20 18:47:34', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('e25f0510d0f6406eaca5a44159ab0242', 'e25f0510d0f6406eaca5a44159ab0242', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-16 14:55:51', null, '2017-08-16 14:55:51', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('e398afbf75a241e78ea290f4f58ce075', 'e398afbf75a241e78ea290f4f58ce075', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-11 17:37:09', null, '2017-08-11 17:37:09', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('e6ce1f72d991483b969f05446e097b11', 'e6ce1f72d991483b969f05446e097b11', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-02 20:49:40', null, '2017-07-02 20:49:40', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('e720d59d3851404bbc1cc78c5fb9792f', 'e720d59d3851404bbc1cc78c5fb9792f', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-06-15 11:04:26', null, '2017-06-15 11:04:26', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('eb1f4d457c8046e0a45a12e5290b45f7', 'eb1f4d457c8046e0a45a12e5290b45f7', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-18 09:51:07', null, '2017-08-18 09:51:07', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('ee0b5b1557ee46c39cfed3c88b427021', 'ee0b5b1557ee46c39cfed3c88b427021', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-11 17:53:05', null, '2017-08-11 17:53:05', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('efb2349b89cf4f36a898705df1628cb6', 'efb2349b89cf4f36a898705df1628cb6', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-16 14:14:14', null, '2017-08-16 14:14:14', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('f0c33da653e64724ad6d5d5f912ce640', 'f0c33da653e64724ad6d5d5f912ce640', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-11 17:53:26', null, '2017-08-11 17:53:26', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('f4e9acc0e4a540229ebb9ab7e8ba2588', 'f4e9acc0e4a540229ebb9ab7e8ba2588', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-07-14 17:28:25', null, '2017-07-14 17:28:25', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('f6329687b3a1474eba31e2bd2d301db0', 'f6329687b3a1474eba31e2bd2d301db0', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-18 09:51:16', null, '2017-08-18 09:51:16', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('f87616d25f004223a1fac6bdafdac9f4', 'f87616d25f004223a1fac6bdafdac9f4', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-06-16 15:14:36', null, '2017-06-16 15:14:36', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('fcf4bd49c8e748fba9f542766de4245a', 'fcf4bd49c8e748fba9f542766de4245a', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-16 10:07:29', null, '2017-08-16 10:07:29', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('fd1915187f9a4c79a54b9eb15e2840e2', 'fd1915187f9a4c79a54b9eb15e2840e2', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-08-16 16:59:07', null, '2017-08-16 16:59:07', null);
INSERT INTO `maoding_corp_company_disk` VALUES ('null', 'null', '5368709120', '0', '0', '0', '5368709120', '', '0', '2017-06-20 18:03:12', null, '2017-06-20 18:03:12', null);
