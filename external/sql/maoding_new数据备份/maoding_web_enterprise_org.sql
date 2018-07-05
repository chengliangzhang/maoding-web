/*
Navicat MySQL Data Transfer

Source Server         : RemotMySQL
Source Server Version : 50625
Source Host           : 172.16.6.71:3306
Source Database       : maoding_new

Target Server Type    : MYSQL
Target Server Version : 50625
File Encoding         : 65001

Date: 2018-04-24 18:02:05
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for maoding_web_enterprise_org
-- ----------------------------
DROP TABLE IF EXISTS `maoding_web_enterprise_org`;
CREATE TABLE `maoding_web_enterprise_org` (
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

-- ----------------------------
-- Records of maoding_web_enterprise_org
-- ----------------------------
INSERT INTO `maoding_web_enterprise_org` VALUES ('02bcbd7fd51d436d9c9a0365fb581217', 'd18bda5241da402c965923aca6554fb0', '3f78e2dc-8740-11e7-8a67-70106fac13fa', '0', '2018-02-07 17:59:14', null, '2018-02-07 17:59:14', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('02fbab8046ff486a91895e53c37dd33b', 'd18bda5241da402c965923aca6554fb0', '6d161c04d8de4155a5fe769c4dd81b79', '0', '2018-02-07 14:48:46', null, '2018-02-07 14:48:46', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('046232a3dd1e408db092db941c11a322', '681913fe71034fc98d6b973e0d09cb41', '82311949-873f-11e7-8a67-70106fac13fa', '0', '2018-03-12 10:57:12', null, '2018-03-12 10:57:12', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('05406ea8959842b79e6d32b6da799ae4', '8ca7bcbc77c746e7a0f44bcbf9ee26bc', 'c6507bed70584f97a6632366ea087c2b', '0', '2018-03-07 10:52:41', null, '2018-03-07 10:52:41', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('07d88e1253f94cf6a77cfaac1a59a657', '1bc5515b52ec474e9528427a6b253410', '7f6b61ee-873f-11e7-8a67-70106fac13fa', '0', '2017-12-13 17:39:01', null, '2017-12-13 17:39:01', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('09885266be8841b7ac274c94eb8addc2', '2FE5E582E94347B7967D89BB13336C4C', 'f36f6367814a4e1ba36ddd89f8345a43', '0', '2018-03-13 15:26:32', null, '2018-03-13 15:26:32', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('09fab4d3045e4b21b1fbeb3b2cd84f3d', 'bbdcfb2bfa9649b2baa72e6f57e1e9b0', '04c712b98ad346bd88b316a4588cac97', '0', '2018-02-08 16:58:48', null, '2018-02-08 16:58:48', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('0a6fddcdd0154153ae061a2f7fb01fa2', '4d0431400f3e41668ececd918ffe2cd9', '9f623834-8740-11e7-8a67-70106fac13fa', '0', '2018-01-08 10:39:09', null, '2018-01-08 10:39:09', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('0aecdf791a174fc3aa8e59117d478251', '4d1b9dcf0720451ea63ea614911247d0', 'a12b6bab-8740-11e7-8a67-70106fac13fa', '0', '2017-12-21 15:13:04', null, '2017-12-21 15:13:04', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('0ecd4b8fa33240fbb7851c421a7b1090', '614f33fe309a4d9a9ce6e624010b85a1', 'f5aa57262d6c499cad86981d250b6c77', '0', '2018-01-12 14:17:09', null, '2018-01-12 14:17:09', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('12a2784913c04b60817bb0d68177ff0b', 'd0790cc8cc6445d390ee56e346c6a6a4', '9d4f114b-8740-11e7-8a67-70106fac13fa', '0', '2018-01-16 16:04:35', null, '2018-01-16 16:04:35', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('1479d56660d94b99a1e6868e49be5fac', '8ca7bcbc77c746e7a0f44bcbf9ee26bc', 'b6db56b1629f414ba7367c9ccd19467c', '0', '2018-03-06 19:05:08', null, '2018-03-06 19:05:08', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('1b92bc1c0f314a9aa93123da484ee6d5', '8ca7bcbc77c746e7a0f44bcbf9ee26bc', '312c7f49913d44cf8ce3cb094be6ef6e', '0', '2018-03-12 11:34:14', null, '2018-03-12 11:34:14', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('1d0a6dd565ab4fce9a267025068d199e', '3d086851ae494c6aaf5bbaccb0e6c0f7', '0dd0cee9-8740-11e7-8a67-70106fac13fa', '0', '2018-03-07 16:40:55', null, '2018-03-07 16:40:55', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('21e9a12da9b446d8bdf49add4f6a81c4', '36c474cf2cfd4122abcd99a184f0aeb5', 'cbef0829-873e-11e7-8a67-70106fac13fa', '0', '2017-12-21 16:58:10', null, '2017-12-21 16:58:10', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('227b3466c8004277aa56ae5f344ac93c', '18eacb2e01b34133853b09dff4f6cb8b', '05dd378a-7f67-4656-abfb-a948f5fb5a62', '0', '2018-03-09 11:36:04', null, '2018-03-09 11:36:04', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('29005b37a471494bb2ca40141cfc99c4', 'eb3b2c593b0641c89795c152bcdce620', '3fa8de43-8740-11e7-8a67-70106fac13fa', '0', '2018-01-18 16:43:36', null, '2018-01-18 16:43:36', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('2a330ffe32ba4a2a971b5b3bbe3124d9', '1bc5515b52ec474e9528427a6b253410', 'c132fb4d72a04992bffe9d2e36ecce30', '0', '2018-01-10 17:37:58', null, '2018-01-10 17:37:58', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('3115c0790420415dbe665415ce0eadf3', '8ca7bcbc77c746e7a0f44bcbf9ee26bc', '8b558c8e-873f-11e7-8a67-70106fac13fa', '0', '2018-01-22 16:48:00', null, '2018-01-22 16:48:00', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('369a961f37b54544b233956a8c8267ab', 'c12c37085b1140eb84c0dcd41760726d', 'bd1ca9c2-873e-11e7-8a67-70106fac13fa', '0', '2017-12-14 18:33:52', null, '2017-12-14 18:33:52', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('37e7ed827ab741d6988d0ea6ece2c132', 'd18bda5241da402c965923aca6554fb0', '4f148297c2af40e3ac81fbdf545641e8', '0', '2018-03-07 09:40:29', null, '2018-03-07 09:40:29', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('38b4ae01f3d84f9ab3843dee672b4c22', 'd7969004608243619910593fb7d7800c', '2a5a2145-8740-11e7-8a67-70106fac13fa', '0', '2018-03-07 16:33:53', null, '2018-03-07 16:33:53', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('3a305e1a4e4f4941b1d1c879be44d244', 'd0790cc8cc6445d390ee56e346c6a6a4', '94535e5b-8740-11e7-8a67-70106fac13fa', '0', '2018-01-22 15:41:12', null, '2018-01-22 15:41:12', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('4220d9e3dd1540399a525842ee2e2a23', '36c474cf2cfd4122abcd99a184f0aeb5', 'e4542509-873f-11e7-8a67-70106fac13fa', '0', '2017-12-21 18:36:39', null, '2017-12-21 18:36:39', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('43723ba93d7140a5b6bdaa804bcb49da', '8ca7bcbc77c746e7a0f44bcbf9ee26bc', '1c5e78fdeb13478ebbfb8d9a53055116', '0', '2018-01-22 16:27:16', null, '2018-01-22 16:27:16', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('48a1952b45934138936b576b3367da8a', '4d1b9dcf0720451ea63ea614911247d0', 'd6cc0a66-8740-11e7-8a67-70106fac13fa', '0', '2017-12-21 15:04:43', null, '2017-12-21 15:04:43', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('4a59857e0898480697888ed06feefb37', '8ca7bcbc77c746e7a0f44bcbf9ee26bc', '5e6ce069-873e-11e7-8a67-70106fac13fa', '0', '2018-01-17 10:38:29', null, '2018-01-17 10:38:29', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('4a8120eef93642f4beaccb08ba243c9e', '4d0431400f3e41668ececd918ffe2cd9', '0dd0cee9-8740-11e7-8a67-70106fac13fa', '0', '2018-01-08 11:24:21', null, '2018-01-08 11:24:21', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('4ca72274dab34d78a7d14f7c18644e82', '614f33fe309a4d9a9ce6e624010b85a1', '384cd32d-8740-11e7-8a67-70106fac13fa', '0', '2017-12-14 17:45:51', null, '2017-12-14 17:45:51', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('4f767374707d431a9f0562f88b2f7056', '8ca7bcbc77c746e7a0f44bcbf9ee26bc', 'db6143bdd97d4d5c8cbc8c113e98f1eb', '0', '2018-03-07 10:53:06', null, '2018-03-07 10:53:06', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('516f4b62718d45c19fd381f9b250f4f4', 'd18bda5241da402c965923aca6554fb0', '2b7d086b4e504afaa7669fd9a2b4894d', '0', '2018-03-07 10:55:05', null, '2018-03-07 10:55:05', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('51f3040bc74640ec9d28be73f95623fe', '36c474cf2cfd4122abcd99a184f0aeb5', '43358517-873e-11e7-8a67-70106fac13fa', '0', '2018-01-25 16:45:32', null, '2018-01-25 16:45:32', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('56e0c3a6b51a4c6d84a19fd6405ffc79', '8ca7bcbc77c746e7a0f44bcbf9ee26bc', '94535e5b-8740-11e7-8a67-70106fac13fa', '0', '2018-01-25 18:39:40', null, '2018-01-25 18:39:40', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('5753ac4a9b6d49a397327dfc07de2949', '4d0431400f3e41668ececd918ffe2cd9', 'a12b6bab-8740-11e7-8a67-70106fac13fa', '0', '2018-01-08 10:36:40', null, '2018-01-08 10:36:40', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('5ac8479e99ae4948bb23c84cf5c6124d', 'd18bda5241da402c965923aca6554fb0', 'aaa44445baf3454baa06af026d643fde', '0', '2018-03-07 10:54:57', null, '2018-03-07 10:54:57', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('6286389aa477465fa2c261e366093349', '18eacb2e01b34133853b09dff4f6cb8b', '94535e5b-8740-11e7-8a67-70106fac13fa', '0', '2018-03-07 16:27:38', null, '2018-03-07 16:27:38', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('642e94e3afe34dc981459784bdd41e7a', '89cbb34cbe984589a6418da3402b10e1', '4495a0c4-873e-11e7-8a67-70106fac13fa', '0', '2017-12-18 09:42:07', null, '2017-12-18 09:42:07', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('655cb80484b24aafa10fd591b617f016', '1bc5515b52ec474e9528427a6b253410', '33894572-873e-11e7-8a67-70106fac13fa', '0', '2018-01-23 14:48:00', null, '2018-01-23 14:48:00', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('67a28efe1dd44d69ae09f7366fc76d95', '4d0431400f3e41668ececd918ffe2cd9', 'a3c7687ac74d4dbc990d6558ecfea276', '0', '2018-02-05 17:19:07', null, '2018-02-05 17:19:07', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('6a7cf889b83548c4a818a300aca3fc7e', '1bc5515b52ec474e9528427a6b253410', 'ef6d25e8-873f-11e7-8a67-70106fac13fa', '0', '2017-12-13 17:48:31', null, '2017-12-13 17:48:31', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('6ed323e73580438bbcf5a1c689a51fba', '8ca7bcbc77c746e7a0f44bcbf9ee26bc', '943118f29cf2421bb3177b0607e27bbc', '0', '2018-03-12 11:38:28', null, '2018-03-12 11:38:28', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('6f603baba12e41eb9c04283cf5a753fc', '36c474cf2cfd4122abcd99a184f0aeb5', 'f0b0a007-873e-11e7-8a67-70106fac13fa', '0', '2017-12-21 18:34:26', null, '2017-12-21 18:34:26', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('714c1cc0026544ec8161e69ee458d746', '8ca7bcbc77c746e7a0f44bcbf9ee26bc', '18fbbf0879574fbf9b37a5a609bbbc2f', '0', '2018-02-09 11:33:06', null, '2018-02-09 11:33:06', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('71cc4be56beb4a35b86b2ef5695999a8', '36c474cf2cfd4122abcd99a184f0aeb5', '65e9fc10-8740-11e7-8a67-70106fac13fa', '0', '2018-01-10 15:40:23', null, '2018-01-10 15:40:23', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('75a8e1a854fa407f90b023baa39c950d', '8ca7bcbc77c746e7a0f44bcbf9ee26bc', '6ad78bf5-8740-11e7-8a67-70106fac13fa', '0', '2017-12-22 11:29:04', null, '2017-12-22 11:29:04', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('75e91917b1844d6ea91cfbe6ed9e4a97', 'd18bda5241da402c965923aca6554fb0', '1ba473f59c2c4173888b0f80f47638a1', '0', '2018-02-07 15:58:59', null, '2018-02-07 15:58:59', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('7cdd80880ed241cf913da9333c78b997', '59908e39ea624df7b8aaf40a883f0e23', '9f623834-8740-11e7-8a67-70106fac13fa', '0', '2018-01-22 15:04:56', null, '2018-01-22 15:04:56', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('7de24eb91cda4133adc4a8c0646afe4d', 'bbdcfb2bfa9649b2baa72e6f57e1e9b0', '86768a496fdd46cfbf158b7c0bbbd570', '0', '2018-02-09 11:30:25', null, '2018-02-09 11:30:25', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('80314b0f32354694a256d52ae2ae475b', '9909c364c6b2498092444b9650e159d1', '943118f29cf2421bb3177b0607e27bbc', '0', '2018-03-12 11:43:47', null, '2018-03-12 11:43:47', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('8ba90f9f5e174ac9899ba16a868b4c22', 'd18bda5241da402c965923aca6554fb0', 'a0ef2040-8740-11e7-8a67-70106fac13fa', '0', '2018-01-03 18:46:11', null, '2018-01-03 18:46:11', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('90f7174871a142bca04be27b907ef00f', 'd18bda5241da402c965923aca6554fb0', 'a646d97592d34ee086a667104e722e86', '0', '2018-03-07 11:00:57', null, '2018-03-07 11:00:57', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('91784b131124413ebf6d9e112d927ea6', 'a5c5b1d99068453fb05fa0ce8b4fbc7f', 'b7dd66c6dc394c88a17e36d2ec833cb0', '0', '2018-02-08 16:54:59', null, '2018-02-08 16:54:59', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('924355f0258b444b8c63220e859b53e9', '8ca7bcbc77c746e7a0f44bcbf9ee26bc', '9d6b3796-8740-11e7-8a67-70106fac13fa', '0', '2017-12-22 10:43:21', null, '2017-12-22 10:43:21', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('93ee766bcded467fb8fd8fd75101b045', '36c474cf2cfd4122abcd99a184f0aeb5', '176facb0-8740-11e7-8a67-70106fac13fa', '0', '2017-12-21 17:45:41', null, '2017-12-21 17:45:41', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('958eaa71f76846b6baa6c5ba25c5254e', '8ca7bcbc77c746e7a0f44bcbf9ee26bc', '9e1faa54-8740-11e7-8a67-70106fac13fa', '0', '2017-12-22 10:46:31', null, '2017-12-22 10:46:31', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('97cfe2aa840142df9cbefe4d789eca56', '1bc5515b52ec474e9528427a6b253410', '3adf3cc1-873e-11e7-8a67-70106fac13fa', '0', '2017-12-13 17:54:08', null, '2017-12-13 17:54:08', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('982cca1aa9084c729c959acfed151698', '36c474cf2cfd4122abcd99a184f0aeb5', 'c53ace2dbfc646599d321853e8ceeeda', '0', '2018-03-13 15:32:16', null, '2018-03-13 15:32:16', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('9c49346960834c478395759cdf361a58', '4d1b9dcf0720451ea63ea614911247d0', '384cd32d-8740-11e7-8a67-70106fac13fa', '0', '2017-12-21 14:14:09', null, '2017-12-21 14:14:09', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('9c65bb9fc7c34b9ba5ac0c8990f547ad', '2b68da9856ad489e84598be15f34b678', '28f2bd0af9e3490da26631930c0fbdb1', '0', '2018-02-09 14:25:22', null, '2018-02-09 14:25:22', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('a0b5304ac9b14a399d7c1b63c4f3646b', '2b699d87b4fb4ff5a03a7f15ecece165', '6f9b95e1-8740-11e7-8a67-70106fac13fa', '0', '2018-01-26 14:21:20', null, '2018-01-26 14:21:20', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('a2ea002b29f348fda722e9a4d17e8384', 'd0790cc8cc6445d390ee56e346c6a6a4', '72d0bcf4-8740-11e7-8a67-70106fac13fa', '0', '2018-01-24 18:19:29', null, '2018-01-24 18:19:29', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('a4dceef135e448e3b6f0dbd41d17fa83', '48b4446a968544248ad7ef811375bf9b', 'f07e6c12-8740-11e7-8a67-70106fac13fa', '0', '2017-12-25 14:50:57', null, '2017-12-25 14:50:57', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('a86fe3c8cc364779852dfa9946e2d02b', '48b4446a968544248ad7ef811375bf9b', '8b558c8e-873f-11e7-8a67-70106fac13fa', '0', '2018-01-03 10:06:25', null, '2018-01-03 10:06:25', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('a9580e9b9bc6462e943266a9b8b11281', '8ca7bcbc77c746e7a0f44bcbf9ee26bc', 'fcee329d-873f-11e7-8a67-70106fac13fa', '0', '2017-12-22 11:21:24', null, '2017-12-22 11:21:24', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('a9953291d50448f291175fdef62f5b9e', '9909c364c6b2498092444b9650e159d1', '3adf3cc1-873e-11e7-8a67-70106fac13fa', '0', '2018-03-12 11:43:47', null, '2018-03-12 11:43:47', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('b1669688d179432898cb634fdfa5f11b', '8ca7bcbc77c746e7a0f44bcbf9ee26bc', '3adf3cc1-873e-11e7-8a67-70106fac13fa', '0', '2018-03-12 11:34:14', null, '2018-03-12 11:34:14', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('b28baf3fa7f741e48d1279e52974ed91', '36c474cf2cfd4122abcd99a184f0aeb5', '110de7870e2e45afb7aaf99393c4518f', '0', '2018-01-19 17:44:53', null, '2018-01-19 17:44:53', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('b31e272ec56e42fa9ca0993e6f24ee57', 'd40debdbdc9d4f65a7f01443966579f2', '1a03ed46-8740-11e7-8a67-70106fac13fa', '0', '2018-03-07 16:42:38', null, '2018-03-07 16:42:38', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('b7964bfd273543419b7ce93119abc257', 'e348058e55b14d61aee575ff05213416', '534fd0dc-8740-11e7-8a67-70106fac13fa', '0', '2018-01-16 15:24:54', null, '2018-01-16 15:24:54', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('b8f81eaf98864520b8a9f4bdccce07da', '8ca7bcbc77c746e7a0f44bcbf9ee26bc', 'f998ab7c292444d8bc0b98e153446fcb', '0', '2018-03-13 18:12:58', null, '2018-03-13 18:12:58', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('bb240bfbe9e84a418f571e23302fd599', '4d0431400f3e41668ececd918ffe2cd9', '94535e5b-8740-11e7-8a67-70106fac13fa', '0', '2018-01-26 15:49:06', null, '2018-01-26 15:49:06', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('c3389faac62246d7aa0a717b8742aed4', '8ca7bcbc77c746e7a0f44bcbf9ee26bc', 'e73d33061d7a46f9820c9c84ebe5ac75', '0', '2018-01-22 16:32:06', null, '2018-01-22 16:32:06', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('cc728e15103444e8a55cce6e93ff106a', '36c474cf2cfd4122abcd99a184f0aeb5', 'eebab85e-873e-11e7-8a67-70106fac13fa', '0', '2017-12-21 18:18:42', null, '2017-12-21 18:18:42', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('cee9b9d854584763808d31953ad694c5', '4d0431400f3e41668ececd918ffe2cd9', 'f1e4c757-8740-11e7-8a67-70106fac13fa', '0', '2018-01-31 18:45:30', null, '2018-01-31 18:45:30', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('d1be31d1d4a040a1b42d612dc28fa2ac', '1bc5515b52ec474e9528427a6b253410', '8b558c8e-873f-11e7-8a67-70106fac13fa', '0', '2018-01-17 14:54:37', null, '2018-01-17 14:54:37', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('d2431e45664c4e32bbb4a2b542671628', 'd18bda5241da402c965923aca6554fb0', '3fa8de43-8740-11e7-8a67-70106fac13fa', '0', '2018-01-03 18:45:05', null, '2018-01-03 18:45:05', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('d316dd420133430a98a645e2b2c8f3ce', '8ca7bcbc77c746e7a0f44bcbf9ee26bc', '82200bfa48f44e34ab09542b84e81b09', '0', '2018-03-07 10:54:16', null, '2018-03-07 10:54:16', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('dc2aa51c910f44989be7da8099a5071f', 'dfadb25faf9f40f8a641a27309b66d6b', '94535e5b-8740-11e7-8a67-70106fac13fa', '0', '2018-01-18 16:45:32', null, '2018-01-18 16:45:32', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('dc5916e14aa64f81b803b6ca93f0de82', '4d1b9dcf0720451ea63ea614911247d0', '9d4f114b-8740-11e7-8a67-70106fac13fa', '0', '2017-12-21 15:08:00', null, '2017-12-21 15:08:00', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('dd24972f410b498a87dd00870f24cbdc', '4d0431400f3e41668ececd918ffe2cd9', '10d2de3d-8740-11e7-8a67-70106fac13fa', '0', '2018-01-08 18:23:45', null, '2018-01-08 18:23:45', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('e53c4c6a743e482ba94a608b361e85a5', 'c12c37085b1140eb84c0dcd41760726d', 'eebab85e-873e-11e7-8a67-70106fac13fa', '0', '2017-12-14 17:45:57', null, '2017-12-14 17:45:57', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('e57dd15d86104f369e71e6c7f2b20938', '39bc796bb7a34459bac4900161aac0ea', '5fc4ecaa-8740-11e7-8a67-70106fac13fa', '0', '2018-01-26 16:14:08', null, '2018-01-26 16:14:08', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('e6b778e2d51949d1a0034e529d3581f2', '8ca7bcbc77c746e7a0f44bcbf9ee26bc', '511fa337ea934328a37f503a50903eb6', '0', '2018-03-07 10:51:28', null, '2018-03-07 10:51:28', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('ecff755abedc4efdbe81df80865f7971', '210b23a226984f8d8b00e176a24f2dd5', '8b558c8e-873f-11e7-8a67-70106fac13fa', '0', '2017-12-13 17:31:29', null, '2017-12-13 17:31:29', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('ed941d1db47a46419c99d01b3e57a03f', '8ca7bcbc77c746e7a0f44bcbf9ee26bc', 'a2b00b716de849a792fe5ba46d6b66d3', '0', '2018-01-22 16:26:53', null, '2018-01-22 16:26:53', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('f18556e554184802afea8e8ef7aa63e2', '36c474cf2cfd4122abcd99a184f0aeb5', '62c8cb12be3d40ab97251493f7544547', '0', '2018-03-08 16:54:28', null, '2018-03-08 16:54:28', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('f1a223aad649436a8e9052b143bae591', '2b699d87b4fb4ff5a03a7f15ecece165', '2e992fc7e9134131839a0d6ba7250795', '0', '2018-01-30 11:45:27', null, '2018-01-30 11:45:27', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('f552161928d74c0994acdac68428da03', '4d1b9dcf0720451ea63ea614911247d0', 'c0bc11ef-873e-11e7-8a67-70106fac13fa', '0', '2017-12-19 10:41:17', null, '2017-12-19 10:41:17', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('fd3dfa845be542cc96a1b1b8665cc612', '36c474cf2cfd4122abcd99a184f0aeb5', '8c3ecc573ad14a299b295d536d528339', '0', '2018-01-10 15:41:06', null, '2018-01-10 15:41:06', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('fdb91a83cc354471890f0fa2ba19b0ee', '2FE5E582E94347B7967D89BB13336C4C', '24c379fd7e2647e9b2e374363b48256c', '0', '2018-03-13 15:04:53', null, '2018-03-13 15:04:53', null);
INSERT INTO `maoding_web_enterprise_org` VALUES ('ffa2d97ef3e34bcab44bd880779d173b', 'acaed9df18a046fe8fcb9fa417c8fead', '3fa8de43-8740-11e7-8a67-70106fac13fa', '0', '2018-01-19 15:56:41', null, '2018-01-19 15:56:41', null);
