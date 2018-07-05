/*
Navicat MySQL Data Transfer

Source Server         : LocalMySQL
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : maoding_test

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2017-12-08 17:00:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for maoding_web_company_relation
-- ----------------------------
DROP TABLE IF EXISTS `maoding_web_company_relation`;
CREATE TABLE `maoding_web_company_relation` (
  `id` varchar(32) NOT NULL COMMENT '主键id，uuid',
  `org_id` varchar(32) DEFAULT NULL COMMENT '企业id',
  `org_pid` varchar(32) DEFAULT NULL COMMENT '父id',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  KEY `org_id` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织加盟申请表';

-- ----------------------------
-- Records of maoding_web_company_relation
-- ----------------------------
INSERT INTO `maoding_web_company_relation` VALUES ('03a3c72b5ddb439fb24dd5f24b537879', 'c13fa26118f544f2bf6778dd5e1b6365', '0a3acae34bbf4c279a4a202a3291cd8e', '2017-06-29 09:45:06', '351a42402d8a44dba6437c7d5c7bc2c5', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('081dd9fc3bde4fe7bf1ac31835e77f13', 'f93e9550613b44ed83af03aaae9f83d0', '48b4446a968544248ad7ef811375bf9b', '2017-07-31 17:22:57', 'd5edc119ae3247bd8c9fe8bcbe57f700', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('08a559310fe84a249f1c0b52722e9f5d', '205ef305dc59498bb57aa417af19b5a8', '3795abf618e04b209b8b49e551369b13', '2017-09-18 10:58:34', '189f436bb05b4090b512e279d4a1a0a1', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('0eb9761fd9a544d392ebceb9ff0d11e6', '328f6383e019445bbe5e523124bf0511', '8fa380531eab41d88f6725444bf92abd', '2017-06-01 09:41:15', '6399603b3c2f4130b38cbddb02e33e5c', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('1129758e2167403887a85fb05ea091c2', '75516793e8b143fcbab6dec29c9b2ad2', '48b4446a968544248ad7ef811375bf9b', '2017-07-31 17:25:40', 'd5edc119ae3247bd8c9fe8bcbe57f700', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('1587436d8bb849baa3416c35a4d83981', '92bf82b606f64834829ad7b13823686e', '301b9cefd6544616be172c5e1ce42802', '2017-05-13 17:38:41', 'f5d38c3417c248a29410146d71e13dd1', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('18812507b6f049899f5af7c5ac3b135e', '8823569d53b346e4bc7e66a02fff99c7', 'acaed9df18a046fe8fcb9fa417c8fead', '2017-07-04 15:50:36', null, null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('1889d0ea18cf4e149405612b199bee18', 'a5c5b1d99068453fb05fa0ce8b4fbc7f', 'bbdcfb2bfa9649b2baa72e6f57e1e9b0', '2017-08-22 11:12:29', '8b11983cd972427fb3ef5640bd3cde81', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('1931248477c14cc6a7024294b0cd5d86', '5f66214997cf42aba8d627d33ffd1c73', 'a4adffa9f0b4452dbf795cf174c59c80', '2017-07-02 17:16:10', null, null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('1ccdd93b13c84c748cf0277e5e57d18f', '36badcaa34694cee9bcda96d7e2f6558', 'd7dc75e633c741bbab254f6d62551034', '2017-05-02 11:37:57', '6399603b3c2f4130b38cbddb02e33e5c', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('246a249bd49a4bb7827f67fa4ea91a97', '7b58ed31bd444b3da7dacc80b3eac694', '7acabcf49b0d4392a86ce3ad84dda55c', '2017-04-28 11:38:42', '9b6971e5414948b3aff09226d44e1ff9', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('2516ac4b5ed346ea934ca0db244dd3d0', '2b699d87b4fb4ff5a03a7f15ecece165', '36c474cf2cfd4122abcd99a184f0aeb5', '2017-05-06 14:31:53', 'db1e0945aa454651a052dd6a9708ba94', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('324d3295d5cd440d985d10da3689efd1', '3cc0ddd5b9b749a8bc33c07186714310', '8fa380531eab41d88f6725444bf92abd', '2017-05-15 18:29:47', '6399603b3c2f4130b38cbddb02e33e5c', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('32b32d1d80a84a549e1d27b6424b157a', '3193aa82dc7e4aadb71c0580c5b37922', 'b78b4f5a29ac4aa8a9150bfd462b0687', '2017-05-27 18:09:19', '9b6971e5414948b3aff09226d44e1ff9', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('3589806c437a47f189ed25cda6959f91', 'b353fbd5cd664cb291b0fbf6c18c1a98', '0a3acae34bbf4c279a4a202a3291cd8e', '2017-07-02 16:43:11', '351a42402d8a44dba6437c7d5c7bc2c5', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('35aa57452ed74347862febd401da47e5', '81890a8ce2b4425f91f98535bf98e3ba', '8fa380531eab41d88f6725444bf92abd', '2017-05-15 18:09:38', '6399603b3c2f4130b38cbddb02e33e5c', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('374cd4c01a9a487eabedce45efa7f5d3', '64486f128da14f35932e63c5be0fc32c', '5e0e1532f5e746ab86db8f04c656a8c7', '2017-06-29 15:35:17', '8394a028dcfc453e80054973c3cb8c1b', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('440464d872ff42bca42a5d264b77dca2', 'acaed9df18a046fe8fcb9fa417c8fead', 'd18bda5241da402c965923aca6554fb0', '2017-04-24 09:56:05', 'd3c73abb16704355a56b38aade9cf812', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('455f15e1c92a4ab5b3a91645b9487ebc', '36d55f92c12f4aefaa2f27110cd14325', '0a3acae34bbf4c279a4a202a3291cd8e', '2017-06-23 11:00:48', '351a42402d8a44dba6437c7d5c7bc2c5', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('468ca576778e4ff18f49d1e562f3871a', '1bc5515b52ec474e9528427a6b253410', 'b78b4f5a29ac4aa8a9150bfd462b0687', '2017-05-27 18:07:31', '9b6971e5414948b3aff09226d44e1ff9', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('49b7e4b4624442e282daa07d506ca631', '8ca7bcbc77c746e7a0f44bcbf9ee26bc', 'bbdcfb2bfa9649b2baa72e6f57e1e9b0', '2017-08-22 10:38:41', '8b11983cd972427fb3ef5640bd3cde81', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('4f5e259490ae453a8656d4554084d5db', '8566d8f1beef4d2b90906b959a10bbbf', '24c0f4d93508465eb5b2e3f7497307e8', '2017-05-02 11:06:56', '49a49c3aeee44dd49c4d3d0412f434b6', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('532bc9a9f57f456094a488ccbd66f0ba', 'f97068cb0b9c4d8087e7d9d2ea1cbdcf', '8fa380531eab41d88f6725444bf92abd', '2017-05-15 18:09:06', '6399603b3c2f4130b38cbddb02e33e5c', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('533a6e6a68044710b1eac7d47cb253ec', '144ae41ac80d4b1d9d62d58567ebe720', '3795abf618e04b209b8b49e551369b13', '2017-09-20 16:42:07', null, null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('54b6416340ad4f0e9a47a565b29942e3', '36c474cf2cfd4122abcd99a184f0aeb5', 'c12c37085b1140eb84c0dcd41760726d', '2017-05-06 14:30:49', 'db1e0945aa454651a052dd6a9708ba94', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('54bfe6ac758c48c58fd5e7d8461275f1', '81073f35e21f43f19c4a33b555d08839', '6c5a3891db644173afda79be9f76709b', '2017-09-05 19:04:43', '5ffee496fa814ea4b6d26a9208b00a0b', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('5574f38f11a74ff79127a0c4e2cfb05b', 'c936db4173404054a3e6a6965637afb3', '48b4446a968544248ad7ef811375bf9b', '2017-06-20 16:37:05', 'd5edc119ae3247bd8c9fe8bcbe57f700', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('5af0d7dd01d54196911b4a1b5fee774b', 'bd0e499fbbc841bfb9dd254db3971f07', '0a3acae34bbf4c279a4a202a3291cd8e', '2017-07-02 17:09:13', null, null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('5bbb22ebea3d47d48f4db8b58f554afc', 'ddad446d54474c39a0d8aa0fdf89ca29', '24c0f4d93508465eb5b2e3f7497307e8', '2017-05-17 11:50:42', '49a49c3aeee44dd49c4d3d0412f434b6', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('5d998fba6a664ba18fef7a35c42baf8b', '0017d83c28fe4bc9a9090e0481a9aa5c', '3795abf618e04b209b8b49e551369b13', '2017-09-04 16:57:33', '189f436bb05b4090b512e279d4a1a0a1', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('6001a23443ea4a798f307fe8a638c3ff', '2022fc5f91a64aa6b8833b6edab78f70', '4d79a102996843c9823d52ac4e1ebe3b', '2017-07-21 19:29:09', '52f0be3452a6491eae0ba038e3773244', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('60d34df0c8be4d5997318930893e8d68', '301b9cefd6544616be172c5e1ce42802', '5e88f343b28b4b508ac181cd6c8de6ea', '2017-05-08 18:33:30', 'f5d38c3417c248a29410146d71e13dd1', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('612ba28b0b284397a185a4704b1b853d', 'b1be4fbe359a4f97b8030a88c5ed0294', '5e88f343b28b4b508ac181cd6c8de6ea', '2017-05-16 16:44:02', 'f5d38c3417c248a29410146d71e13dd1', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('614d4d650251406f84d215ebe7bec0ce', 'a688e4475d334d98844044862069c81d', '844bd9e549dd478eaab7d44bef48fcb5', '2017-04-24 15:31:38', '518206ee704f4779b3de79df3885c2e1', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('70aa04c939b74ee0add0bb1dbd8b7165', '79e058954c4045f2925651aaca08f37c', 'd7dc75e633c741bbab254f6d62551034', '2017-05-25 10:40:20', '6399603b3c2f4130b38cbddb02e33e5c', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('7905ccda78df4c8792d0ea93e40eac1d', 'd57b744481e54da0abf0e1a09d31bdf6', 'f87616d25f004223a1fac6bdafdac9f4', '2017-06-01 09:47:53', '6399603b3c2f4130b38cbddb02e33e5c', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('7a6332d316e24525bfade6a0b29724e6', '858414a8919943418f13f5fdca49a83e', 'd18bda5241da402c965923aca6554fb0', '2017-06-28 17:28:59', 'd3c73abb16704355a56b38aade9cf812', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('7cbd754e44ba40a5b6295ae796e07228', '089836c6489647c1a8dd23704e56a678', 'd18bda5241da402c965923aca6554fb0', '2017-04-24 09:57:02', 'd3c73abb16704355a56b38aade9cf812', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('839b13056c554e73974a4810a248939b', '496cdd4f1e2b4a219d56039ee78e4785', '7acabcf49b0d4392a86ce3ad84dda55c', '2017-04-25 10:00:45', '9b6971e5414948b3aff09226d44e1ff9', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('852c8fd2788e485dbc957e6928340260', '2fb5a1b46d22467d8e3adf568c606d87', '40d38712118740a4af4170c4ec546ff4', '2017-04-28 18:45:31', '6399603b3c2f4130b38cbddb02e33e5c', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('8641ee5abaa94cbf93d9e9a677a8f9d0', '4be40adbc52f4cf59da085eb505ac716', '48b4446a968544248ad7ef811375bf9b', '2017-07-31 17:23:25', 'd5edc119ae3247bd8c9fe8bcbe57f700', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('89fff707713749a0b8ae288e59a67392', '6c5a3891db644173afda79be9f76709b', '7661faa2fea44c2285918ae6127ac5cd', '2017-07-06 14:26:47', '5ffee496fa814ea4b6d26a9208b00a0b', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('8c10777c5c6e497a8750498ce0a12b2f', 'ad89564855ad4cc2a39bcb602b0a02a8', 'bbdcfb2bfa9649b2baa72e6f57e1e9b0', '2017-08-22 10:37:55', '8b11983cd972427fb3ef5640bd3cde81', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('94d69429ff75464da74de6ae7c55e892', '430519a61423467ab5bdeeaae13eb901', '25f58ca1705d417ab9f2383210977c8d', '2017-05-17 16:16:01', '91b7cedf778040a8ad1efa679c8f27f5', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('9701220e68134addbd42b1cb7fd850a3', '96187fdf2bfa40c4b3154fee3c7c539c', '2f2103494eb44a1fbb52ac73de556009', '2017-07-21 19:21:33', '1296b85b4ce943dab32959d375bc1ce8', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('a0226346aefd4030b4a20eca900093c2', '2FE5E582E94347B7967D89BB13336C4C', 'a4adffa9f0b4452dbf795cf174c59c80', '2017-06-16 14:31:00', 'db1e0945aa454651a052dd6a9708ba94', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('a16667b4fd26412ba690aed4fbfa3e83', 'b5ac585672b54f3eae6864c4e5025d20', 'd7dc75e633c741bbab254f6d62551034', '2017-05-25 10:39:07', '6399603b3c2f4130b38cbddb02e33e5c', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('aa6b3df180154c87a05c4c6c6b6bfa38', '0bc556aa74574617b118fd2df1d9ee51', 'd7dc75e633c741bbab254f6d62551034', '2017-05-25 10:37:49', '6399603b3c2f4130b38cbddb02e33e5c', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('ae013b2c71274d718df681e5191c6d26', 'a6aa138bbb6345268efdc3ea3bc84455', '36c474cf2cfd4122abcd99a184f0aeb5', '2017-05-09 11:00:12', 'db1e0945aa454651a052dd6a9708ba94', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('b12ef148b5994ac29b46308eb86fd9cb', '844bd9e549dd478eaab7d44bef48fcb5', 'c0aa0bd5fbed4bf08191771753388a93', '2017-04-24 15:29:50', 'c43184563300429f8ad058a7171852f9', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('b8b677ee102b4fb38623825444eef111', '68b5590cc3f44361a59c54abc59ce465', 'b11d6e08d62348fe9f1c76eddce8415a', '2017-09-21 17:35:59', 'df2e12d6ea7e429280fff5b657875ac1', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('bb3aa8d5525c4fc9a5fe5df4ea1031dd', '6acc0ac39a6a439f8cfa9e15fd8e8c74', '24c0f4d93508465eb5b2e3f7497307e8', '2017-05-05 09:40:04', '49a49c3aeee44dd49c4d3d0412f434b6', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('bba3497e7ea84e57942add2245f9419e', '5339147901b34a0b99e16b6ac7054d64', 'd18bda5241da402c965923aca6554fb0', '2017-06-27 17:15:38', '8394a028dcfc453e80054973c3cb8c1b', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('bdf3425684634cf3b73cbfbdfcfe6663', '3554ca18659e460c87130307363b08c6', '48b4446a968544248ad7ef811375bf9b', '2017-06-20 16:37:24', 'd5edc119ae3247bd8c9fe8bcbe57f700', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('c2cb7bdd544b4d5099fcfc1810549a35', '7812d6f9d4e148768ae1a9fd7bbbaf2d', 'd7dc75e633c741bbab254f6d62551034', '2017-06-01 09:42:07', '6399603b3c2f4130b38cbddb02e33e5c', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('cc3155ff4bd6437f8eb576400c27d522', '70b6668b9d6c47cca2c115ff991014b6', '0a3acae34bbf4c279a4a202a3291cd8e', '2017-07-03 14:19:49', '454be6f2bbd946d598367777705e0cba', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('d053cb7cdad0443890eb8fe633aa9360', '2c843b70558747fb8aec7658fe99395c', 'f6270182efef47a7964f46846c05e334', '2017-05-25 10:32:03', '6399603b3c2f4130b38cbddb02e33e5c', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('db2da2a224fe44a1aa471088e66b2cb9', '03cbdeff5cca4fda826471b5697262ee', '24c0f4d93508465eb5b2e3f7497307e8', '2017-05-02 10:59:48', '49a49c3aeee44dd49c4d3d0412f434b6', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('e061d1da2530470a8b02f06928b81829', '681913fe71034fc98d6b973e0d09cb41', '496cdd4f1e2b4a219d56039ee78e4785', '2017-05-13 17:35:21', '9b6971e5414948b3aff09226d44e1ff9', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('e1c8597017a54e1ca11df248f7fa191f', 'f2fd98b9ed0345f6a465c7e3fe053d34', 'cc1751b060e049d8a7c599a9ad7def46', '2017-04-24 14:52:46', 'db1e0945aa454651a052dd6a9708ba94', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('e27eca794ec34d4cb44d2306ff2ae8e4', '0b88908f89714405b095d82e5f16ecd1', '5339147901b34a0b99e16b6ac7054d64', '2017-05-26 17:46:07', '8394a028dcfc453e80054973c3cb8c1b', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('f1a57c615cec4f579737ac75926f1696', '614f33fe309a4d9a9ce6e624010b85a1', 'bbdcfb2bfa9649b2baa72e6f57e1e9b0', '2017-08-22 11:13:00', '8b11983cd972427fb3ef5640bd3cde81', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('f565ffdd14474913ba2fa6d76c4be36c', '2fe7dc3dab4f422994a44acae4f01c8c', 'b11d6e08d62348fe9f1c76eddce8415a', '2017-09-13 15:55:37', 'df2e12d6ea7e429280fff5b657875ac1', null, null);
INSERT INTO `maoding_web_company_relation` VALUES ('fced4438541a4d4e9f0b8af252ba70d1', '29c6f761c22240bca5cdf826039fccc4', 'b11d6e08d62348fe9f1c76eddce8415a', '2017-09-13 16:04:02', 'df2e12d6ea7e429280fff5b657875ac1', null, null);
