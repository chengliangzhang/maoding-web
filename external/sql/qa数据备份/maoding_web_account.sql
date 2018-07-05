/*
Navicat MySQL Data Transfer

Source Server         : RemotMySQL
Source Server Version : 50625
Source Host           : 172.16.6.71:3306
Source Database       : maoding_qa

Target Server Type    : MYSQL
Target Server Version : 50625
File Encoding         : 65001

Date: 2018-04-14 16:44:57
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for maoding_web_account
-- ----------------------------
DROP TABLE IF EXISTS `maoding_web_account`;
CREATE TABLE `maoding_web_account` (
  `id` varchar(32) NOT NULL COMMENT '主键id，uuid',
  `user_name` varchar(30) DEFAULT NULL COMMENT '用户名（冗余）',
  `nick_name` varchar(30) DEFAULT NULL COMMENT '昵称',
  `password` varchar(32) DEFAULT NULL COMMENT '密码',
  `cellphone` varchar(11) NOT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL,
  `default_company_id` varchar(32) DEFAULT NULL COMMENT '默认企业id',
  `signature` varchar(100) DEFAULT NULL COMMENT '个性签名',
  `status` varchar(1) DEFAULT '0' COMMENT '账号状态(1:未激活，0：激活）',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `emial_code` varchar(100) DEFAULT NULL COMMENT '邮箱绑定(验证）格式：邮箱-验证码（email-code）',
  `active_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cellphone` (`cellphone`) USING BTREE,
  KEY `default_company_id` (`default_company_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='登录用户表';

-- ----------------------------
-- Records of maoding_web_account
-- ----------------------------
INSERT INTO `maoding_web_account` VALUES ('0076919a2aae48a781e77a63ef6e5f50', '小迪', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13221450717', null, null, null, '0', '2018-02-07 09:57:10', null, '2018-03-04 09:42:14', null, null, '2018-03-04 09:42:14');
INSERT INTO `maoding_web_account` VALUES ('07649b3d23094f28bfce78930bf4d4ac', '卢沂', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13926516981', null, null, null, '0', '2018-02-02 11:41:57', null, '2018-02-09 13:17:08', null, null, '2018-02-09 13:17:08');
INSERT INTO `maoding_web_account` VALUES ('09ab6aacd4274f29aa91c9adcb84fdd2', '贾筠', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13924669522', null, null, null, '1', '2018-02-09 11:08:24', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('0d486c8f682d4c4985252973927ab6ff', '刘扬', null, '3C8AABCE8906310CAA443C8F1DA756A1', '13902957370', null, null, null, '0', '2018-02-09 11:09:42', null, '2018-02-09 11:19:41', null, null, '2018-02-09 11:19:41');
INSERT INTO `maoding_web_account` VALUES ('0d9b3c72dfdc40809cb1dabe9dcc9af9', '温盛健', null, 'E10ADC3949BA59ABBE56E057F20F883E', '18620251394', null, null, null, '0', '2018-02-02 11:41:59', null, '2018-02-27 17:24:34', null, null, '2018-02-06 11:20:42');
INSERT INTO `maoding_web_account` VALUES ('0f995754f39e4385a404c0efd932bfef', '李长明', null, 'B24B60F6F663BD413E063EC482326914', '13509672920', null, null, null, '0', '2018-02-06 10:47:29', null, '2018-02-07 15:24:02', null, null, '2018-02-07 15:24:02');
INSERT INTO `maoding_web_account` VALUES ('0fac3579212a4d598c162122770257f3', '10086', null, 'E10ADC3949BA59ABBE56E057F20F883E', '15606767157', null, '9f44f6699319439dae5289d852f7195f', null, '0', '2018-02-02 11:41:58', null, '2018-04-12 09:21:20', null, null, '2018-02-06 14:55:13');
INSERT INTO `maoding_web_account` VALUES ('137afc596d8b40a4a53b3e3e07d8be8d', '刘思斯', null, 'E10ADC3949BA59ABBE56E057F20F883E', '18711178513', null, null, null, '1', '2018-03-05 10:19:27', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('161b978f3936460d9ea03276e4f3977f', '许佳迪', null, 'EB2E42BB67BBFEDC04A6ECD930B71C25', '15353032981', null, null, null, '0', '2018-02-02 11:41:58', null, '2018-02-06 12:42:11', null, null, '2018-02-06 12:42:11');
INSERT INTO `maoding_web_account` VALUES ('17e2fd4f0afc4b84a643eb54a8affff0', '老冒', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13602572998', null, null, null, '1', '2018-03-23 15:39:26', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('1aea476bc66a4e7695b8af340b23d4e2', '李超红', null, '1DAF647954D2CB58DB38C0A973901554', '13603018023', null, null, null, '0', '2018-02-06 10:47:31', null, '2018-02-06 11:03:07', null, null, '2018-02-06 11:03:07');
INSERT INTO `maoding_web_account` VALUES ('1caef412f9ee46d1b29cbfd216b25620', '孙  冰', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13600180660', null, null, null, '0', '2018-02-06 10:47:30', null, '2018-02-09 11:04:40', null, null, '2018-02-09 11:04:40');
INSERT INTO `maoding_web_account` VALUES ('1ece4940686244eca4b7ca6f1c9c5bce', '李广华', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13728782265', null, null, null, '0', '2018-02-09 11:13:47', null, '2018-02-09 11:19:32', null, null, '2018-02-09 11:19:32');
INSERT INTO `maoding_web_account` VALUES ('24524d8fbf6b44c7a3550a90b64d16fc', 'Dorde', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13924600474', null, null, null, '1', '2018-02-06 10:47:33', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('26e88aa6e86f408ea8a10dc0d57d29bb', '王汝彬', null, 'E10ADC3949BA59ABBE56E057F20F883E', '18620154157', null, null, null, '0', '2018-02-02 11:41:58', null, '2018-02-27 09:29:44', null, null, '2018-02-27 09:29:44');
INSERT INTO `maoding_web_account` VALUES ('27f17d213aa949da832762718a08dbe2', '李  凌', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13922888108', null, null, null, '1', '2018-02-06 10:47:30', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('2df40feb481f4337aa19d0e53065b775', '钱老五', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13903022225', null, null, null, '1', '2018-03-23 19:44:44', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('35ca98b779274f7385f6a5c3e77bbb65', '王老七', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13903022237', null, null, null, '1', '2018-03-23 19:44:46', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('3936f3e7dde74f3590384dce96c5b16c', '钱老三', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13903022223', null, null, null, '1', '2018-03-23 19:44:43', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('3945628bc265408482d1051ed8f363af', '刘昌萍', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13530050560', null, null, null, '0', '2018-02-09 11:10:35', null, '2018-02-09 11:17:41', null, null, '2018-02-09 11:17:41');
INSERT INTO `maoding_web_account` VALUES ('3c7d56eac57a4447b0f7f078ac4b72a3', '钱老八', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13903022228', null, null, null, '1', '2018-03-23 19:44:45', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('4bb63f087d1a4fee909e97efc8cda010', '111', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13111111111', null, null, null, '1', '2018-03-28 16:08:51', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('4be9684e83ad49b4bcc51ab8c62b6a6f', '郭青燕', null, '186A6785333315317E867DC1CB26BE27', '13554897196', null, null, null, '0', '2018-02-07 15:21:11', null, '2018-02-07 15:28:00', null, null, '2018-02-07 15:28:00');
INSERT INTO `maoding_web_account` VALUES ('4cf7cd5790be4ad4bf5c39ea74b6dcd0', '小迪', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13145555770', null, null, null, '0', '2018-03-05 09:10:54', null, '2018-03-07 09:00:36', null, null, '2018-03-07 09:00:36');
INSERT INTO `maoding_web_account` VALUES ('4e4ee80cc1364ad697e702d5cc97e59a', 'Justing', null, '25F9E794323B453885F5181F1B624D0B', '13798291624', null, null, null, '0', '2018-02-02 11:41:57', null, '2018-04-03 10:26:25', null, null, '2018-02-27 09:42:59');
INSERT INTO `maoding_web_account` VALUES ('4ed1986b56f94627b63b3972a7659c0a', '钱老七', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13903022227', null, null, null, '1', '2018-03-23 19:44:44', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('59c49d5da9484457b6bd25edbb1f68ce', '孙勇', null, 'FED31FB124D94E121E5F2C372FDDA877', '13602630000', null, null, null, '0', '2018-02-06 10:39:09', null, null, null, null, '2018-02-06 10:39:09');
INSERT INTO `maoding_web_account` VALUES ('5a5893338a4449f185d5016a855624f6', '黄铎', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13510106915', null, null, null, '0', '2018-02-02 11:35:28', null, null, null, null, '2018-02-02 11:35:28');
INSERT INTO `maoding_web_account` VALUES ('5dc8b7b2503e41a69074a8925cec7b45', '钱老九', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13903022229', null, null, null, '1', '2018-03-23 19:44:45', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('61b6ce39974f4fe3b55e20b257726200', '王迪钦', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13006654553', null, null, null, '0', '2018-02-06 10:47:30', null, '2018-02-09 11:03:43', null, null, '2018-02-09 11:03:43');
INSERT INTO `maoding_web_account` VALUES ('62568b90140f4c00be08e262aa96f2d3', 'liu_QA', null, '21218CCA77804D2BA1922C33E0151105', '15712008191', null, null, null, '0', '2018-02-01 21:04:09', null, '2018-03-15 09:46:27', null, null, '2018-02-01 21:04:09');
INSERT INTO `maoding_web_account` VALUES ('67e9dd9cfd7a4079a4add338218b02a9', '芦文清', null, 'F9EC54D8E65BC9C38CF5E3F1A886D77C', '18098958090', null, null, null, '0', '2018-02-06 10:47:32', null, '2018-02-07 15:24:45', null, null, '2018-02-07 15:24:45');
INSERT INTO `maoding_web_account` VALUES ('6abdc0a47b3541fb9d43b70dc316d45f', 'adion', null, 'E10ADC3949BA59ABBE56E057F20F883E', '18589035085', null, null, null, '0', '2018-02-02 11:42:00', null, '2018-02-26 11:24:11', null, null, '2018-02-26 11:24:11');
INSERT INTO `maoding_web_account` VALUES ('6d421fe5a63043cabbe0fb7e3fff03c6', '毛双凤', null, 'FCEA920F7412B5DA7BE0CF42B8C93759', '18665965065', null, null, null, '0', '2018-02-02 11:41:59', null, '2018-04-03 10:34:03', null, null, '2018-02-27 09:30:11');
INSERT INTO `maoding_web_account` VALUES ('6e1902bb136149caaa4ca91ba251a0b1', '20180324-01', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13631285200', null, null, null, '1', '2018-03-24 15:08:13', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('81251978e8954f87ac26c0fbdbd45713', '陈旭生', null, 'E10ADC3949BA59ABBE56E057F20F883E', '15816851148', null, null, null, '1', '2018-02-09 11:12:58', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('81318a1e092046c182fd6ac49174bb29', '王大勇', null, 'F050474FE8AE13C57BDABBB7444ED5DA', '13823153335', null, null, null, '0', '2018-02-09 11:05:53', null, '2018-02-09 11:15:05', null, null, '2018-02-09 11:15:05');
INSERT INTO `maoding_web_account` VALUES ('88ac7b96ffb740448900f3b0e53255e7', '老冒', null, 'FED31FB124D94E121E5F2C372FDDA877', '13602672998', null, null, null, '0', '2018-02-06 12:32:45', null, null, null, null, '2018-02-06 12:32:45');
INSERT INTO `maoding_web_account` VALUES ('9157f56307a748cf8395ef64fd22f57d', '张小芬', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13538295829', null, null, null, '1', '2018-02-06 10:47:32', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('93534df3bae7433fabf8cd2e2c26dfcb', '吕全新', null, '6EEBD813AF9C82FB368A1E54086FF62B', '13560755621', null, null, null, '0', '2018-02-07 15:20:05', null, '2018-02-07 15:45:31', null, null, '2018-02-07 15:45:31');
INSERT INTO `maoding_web_account` VALUES ('963c76b0ead14536aa60d19fcd0e2398', '格老', null, 'FED31FB124D94E121E5F2C372FDDA877', '18098958096', null, null, null, '0', '2018-02-06 12:30:16', null, null, null, null, '2018-02-06 12:30:16');
INSERT INTO `maoding_web_account` VALUES ('997b83d5618c4d479dd3ed114ecbcdf1', '赵老大', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13902911111', null, null, null, '1', '2018-03-23 19:44:41', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('9f04cf670e724748aa0e0a442277162b', '杨深', null, 'B7DCAC15D2DFC827BB9881933448684A', '13714847045', null, null, null, '0', '2018-02-06 10:47:31', null, '2018-02-07 16:32:38', null, null, '2018-02-06 10:52:26');
INSERT INTO `maoding_web_account` VALUES ('a2edd046709a492e944a1fc1b0cbeb0b', '钱老二', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13903022221', null, null, null, '1', '2018-03-23 19:44:42', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('ad335a67676d43eb8805db237813fb5c', '钱老十', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13903022210', null, null, null, '1', '2018-03-23 19:44:45', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('ade02680a6eb4f499439a364204aa591', '李老三', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13903233331', null, null, null, '1', '2018-03-23 19:44:43', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('b00b52e2828549e0b987ae6add350378', 'test11', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13631280341', null, null, null, '1', '2018-04-04 11:48:58', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('b6181dc6f552461a8fe00c6b02d28678', '张1', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13000000001', null, null, null, '0', '2018-03-06 10:55:13', null, '2018-03-06 10:55:41', null, null, '2018-03-06 10:55:41');
INSERT INTO `maoding_web_account` VALUES ('b94eb259c2f745129a456c05871f8771', '王进', null, 'C94E3180FCBF9A4DEB865FC66B0C2FBB', '13723470581', null, null, null, '0', '2018-02-06 10:47:31', null, '2018-02-07 16:26:31', null, null, '2018-02-07 16:26:31');
INSERT INTO `maoding_web_account` VALUES ('b970ccad837145a099a43203c32ed268', '王老五', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13903022235', null, null, null, '1', '2018-03-23 19:44:46', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('c1c4b24aed2042789ce9398dfc55a571', '钱老六', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13903022226', null, null, null, '1', '2018-03-23 19:44:44', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('c2465ca724714948ac7df180b1a19d93', '张某某', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13012345678', null, null, null, '1', '2018-03-07 09:03:13', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('c2c3a01170fc4cef977c9ad601e3407d', '小练', null, 'E10ADC3949BA59ABBE56E057F20F883E', '15606767715', null, null, null, '1', '2018-03-28 15:30:40', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('c6879b0bd859429cb5a198b2143241e5', '李青', null, 'E10ADC3949BA59ABBE56E057F20F883E', '15899870305', null, null, null, '1', '2018-03-05 10:14:00', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('c99c0e0ee75548a786749c483a79a28d', '王老六', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13903022236', null, null, null, '1', '2018-03-23 19:44:46', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('cadaf40ae9db4124b6847b52ac862e53', '许峻', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13510213251', null, null, null, '1', '2018-02-09 11:12:14', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('d0542bc11035473383f09c7de4a9ee8a', '曹大大', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13632353729', null, null, null, '0', '2018-02-02 11:36:34', null, null, null, null, '2018-02-02 11:36:34');
INSERT INTO `maoding_web_account` VALUES ('d2f69870ccbd4580b61bcec161b6231a', '程芳', null, 'E10ADC3949BA59ABBE56E057F20F883E', '15220069967', null, null, null, '1', '2018-03-05 10:12:37', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('d42902deba504b2e9787bd11777c2c34', 'aa', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13631285022', null, null, null, '1', '2018-03-23 19:46:42', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('d437448683314cad91dc30b68879901d', '张成亮', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13680809727', null, null, null, '0', '2018-02-02 11:41:59', null, '2018-02-27 15:52:53', null, null, '2018-02-27 09:30:38');
INSERT INTO `maoding_web_account` VALUES ('d8213e169e9d4c82b7de34ff6b9ac4d8', '许佳迪', null, 'E10ADC3949BA59ABBE56E057F20F883E', '15353503298', null, null, null, '1', '2018-03-21 20:15:42', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('e1b8b954c5524746ad739c49431ad9d4', '魏昕', null, '8B7AD7BEAFA86F2BAC2EDD31666E1DB4', '18688715679', null, null, null, '0', '2018-02-06 10:47:31', null, '2018-02-07 15:23:48', null, null, '2018-02-07 15:23:48');
INSERT INTO `maoding_web_account` VALUES ('e32bcadccc59422dbb8b62bd14cebf6d', '梁沛江', null, 'E10ADC3949BA59ABBE56E057F20F883E', '18665996791', null, null, null, '0', '2018-02-09 11:11:21', null, '2018-02-09 11:18:41', null, null, '2018-02-09 11:18:41');
INSERT INTO `maoding_web_account` VALUES ('e3bb00a8ec4d45adbc9f39b855bd41a2', '李英', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13620949309', null, null, null, '1', '2018-02-06 10:47:32', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('eee31691b50c443cba976d40d31f771c', '伍娜', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13410177865', null, null, null, '1', '2018-02-06 10:47:33', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('efc1d88ef24f45d4a464f2165502bdd6', '钱老四', null, 'E10ADC3949BA59ABBE56E057F20F883E', '13903022224', null, null, null, '1', '2018-03-23 19:44:43', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('f1aaebf23090496790625fb7c493864e', 'Edison', null, 'E10ADC3949BA59ABBE56E057F20F883E', '15013551861', null, null, null, '0', '2018-02-02 11:41:58', null, '2018-04-03 11:14:58', null, null, '2018-02-06 14:09:26');
INSERT INTO `maoding_web_account` VALUES ('f22effa5f40d48789c8ce36ba2816616', '丁喜彤', null, 'E10ADC3949BA59ABBE56E057F20F883E', '15818663612', null, null, null, '1', '2018-02-06 10:47:32', null, null, null, null, null);
INSERT INTO `maoding_web_account` VALUES ('fd18b2ee8aae484297bb3b5a09a76909', '张骐', null, 'ABA67BE3A4A94383C3932B195BCBC974', '13923426730', null, null, null, '0', '2018-02-06 10:47:30', null, '2018-03-06 12:46:54', null, null, '2018-02-28 16:14:29');
