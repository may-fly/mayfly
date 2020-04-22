/*
 Navicat Premium Data Transfer

 Source Server         : self
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : localhost:3306
 Source Schema         : mayfly

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 22/04/2020 11:21:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_account
-- ----------------------------
DROP TABLE IF EXISTS `tb_account`;
CREATE TABLE `tb_account` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `status` tinyint(2) NOT NULL,
  `create_account_id` bigint(20) NOT NULL DEFAULT '0',
  `create_account` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0',
  `update_account_id` bigint(20) NOT NULL DEFAULT '0',
  `update_account` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `last_login_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_account
-- ----------------------------
BEGIN;
INSERT INTO `tb_account` VALUES (1, 'admin', '859e58a36cfe12a7f97b3b02f7b13380', 1, 1, 'admin', 1, 'admin', '2020-03-05 07:41:18', '2020-03-05 07:41:18', NULL);
INSERT INTO `tb_account` VALUES (9, 'test', 'e10adc3949ba59abbe56e057f20f883e', 1, 1, 'admin', 1, 'admin', '2019-08-21 11:30:33', '2020-03-30 12:40:33', NULL);
INSERT INTO `tb_account` VALUES (10, 'test2', 'ad0234829205b9033196ba818f7a872b', 0, 1, 'admin', 1, 'admin', '2019-12-12 09:22:37', '2020-03-27 13:38:36', NULL);
INSERT INTO `tb_account` VALUES (11, 'meilin.huang', 'e10adc3949ba59abbe56e057f20f883e', 1, 1, 'admin', 1, 'admin', '2019-12-12 09:31:36', '2020-03-05 09:41:50', NULL);
INSERT INTO `tb_account` VALUES (12, 'test3', '859e58a36cfe12a7f97b3b02f7b13380', 1, 1, 'admin', 1, 'admin', '2020-03-30 13:20:51', '2020-03-30 13:20:51', NULL);
COMMIT;

-- ----------------------------
-- Table structure for tb_account_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_account_role`;
CREATE TABLE `tb_account_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `account_id` bigint(20) NOT NULL COMMENT '账号id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `create_account` varchar(45) DEFAULT NULL,
  `create_account_id` bigint(20) unsigned DEFAULT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_account_role
-- ----------------------------
BEGIN;
INSERT INTO `tb_account_role` VALUES (11, 11, 2, 'admin', 1, '2020-03-29 08:34:19');
INSERT INTO `tb_account_role` VALUES (13, 10, 2, 'admin', 1, '2020-03-29 08:34:34');
INSERT INTO `tb_account_role` VALUES (15, 9, 2, 'admin', 1, '2020-03-29 08:34:52');
INSERT INTO `tb_account_role` VALUES (17, 1, 1, 'admin', 1, '2020-03-29 08:35:12');
INSERT INTO `tb_account_role` VALUES (18, 9, 4, 'admin', 1, '2020-03-30 00:55:46');
INSERT INTO `tb_account_role` VALUES (19, 11, 5, 'admin', 1, '2020-03-30 00:57:08');
INSERT INTO `tb_account_role` VALUES (20, 9, 5, 'admin', 1, '2020-03-30 12:46:14');
INSERT INTO `tb_account_role` VALUES (21, 12, 2, 'admin', 1, '2020-04-20 04:50:31');
COMMIT;

-- ----------------------------
-- Table structure for tb_machine
-- ----------------------------
DROP TABLE IF EXISTS `tb_machine`;
CREATE TABLE `tb_machine` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `ip` varchar(45) NOT NULL,
  `port` int(11) NOT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `create_account_id` bigint(20) unsigned DEFAULT NULL,
  `create_account` varchar(45) DEFAULT NULL,
  `update_account_id` bigint(20) unsigned DEFAULT NULL,
  `update_account` varchar(45) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_machine
-- ----------------------------
BEGIN;
INSERT INTO `tb_machine` VALUES (1, '腾讯云', '118.24.26.101', 22, 'root', '', 1, 'admin', NULL, NULL, '2019-11-04 14:23:04', '2019-11-04 14:23:04');
INSERT INTO `tb_machine` VALUES (2, 'test', '94.191.96.31', 22, 'root', '', 1, 'admin', NULL, NULL, '2019-11-05 20:05:22', '2019-11-05 20:05:22');
INSERT INTO `tb_machine` VALUES (3, 'ucar', '10.130.20.148', 22, 'root', '123456', 1, 'admin', NULL, NULL, '2019-11-18 05:16:48', '2019-11-18 05:16:48');
INSERT INTO `tb_machine` VALUES (4, '京东云', '114.67.67.10', 22, 'root', '', 1, 'admin', NULL, NULL, '2019-12-11 07:52:51', '2019-12-11 07:52:51');
COMMIT;

-- ----------------------------
-- Table structure for tb_machine_file
-- ----------------------------
DROP TABLE IF EXISTS `tb_machine_file`;
CREATE TABLE `tb_machine_file` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '机器文件配置（linux一切皆文件，故也可以表示目录）',
  `machine_id` bigint(20) NOT NULL,
  `name` varchar(45) NOT NULL,
  `path` varchar(45) NOT NULL,
  `type` varchar(45) NOT NULL COMMENT '1：目录；2：文件',
  `create_account_id` bigint(20) unsigned DEFAULT NULL,
  `create_account` varchar(45) DEFAULT NULL,
  `update_account_id` bigint(20) unsigned DEFAULT NULL,
  `update_account` varchar(45) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_machine_file
-- ----------------------------
BEGIN;
INSERT INTO `tb_machine_file` VALUES (1, 1, 'redis配置文件', '/etc/my.cnf', '2', 1, 'admin', NULL, NULL, '2019-08-21 11:30:33', NULL);
INSERT INTO `tb_machine_file` VALUES (2, 1, '测试文件', '/usr/local/test/file', '2', 1, 'admin', NULL, NULL, '2019-08-21 11:30:33', NULL);
INSERT INTO `tb_machine_file` VALUES (7, 1, 'test', '/usr/local/test/file1', '2', 1, 'admin', NULL, NULL, '2019-11-05 16:12:58', NULL);
INSERT INTO `tb_machine_file` VALUES (8, 2, 'redis', '/root/restart-redis.sh', '2', 1, 'admin', NULL, NULL, '2019-11-05 20:08:34', NULL);
INSERT INTO `tb_machine_file` VALUES (10, 2, 'java-dockerfile', '/usr/local/java/Dockerfile', '2', 1, 'admin', NULL, NULL, '2019-11-06 15:16:12', NULL);
INSERT INTO `tb_machine_file` VALUES (11, 1, 'usr', '/usr', '1', 1, 'admin', NULL, NULL, '2019-11-06 15:16:12', NULL);
INSERT INTO `tb_machine_file` VALUES (12, 1, 'opt', '/opt', '1', 1, 'admin', NULL, NULL, '2019-11-06 15:16:12', NULL);
INSERT INTO `tb_machine_file` VALUES (13, 3, '根目录', '/', '1', 1, 'admin', NULL, NULL, '2019-11-18 05:19:28', NULL);
INSERT INTO `tb_machine_file` VALUES (14, 2, 'usr', '/usr/local', '1', 1, 'admin', NULL, NULL, '2019-11-19 05:52:28', NULL);
INSERT INTO `tb_machine_file` VALUES (15, 3, 'usr', '/usr/', '1', 1, 'admin', NULL, NULL, '2019-11-19 08:35:19', NULL);
INSERT INTO `tb_machine_file` VALUES (16, 3, '软件安装目录', '/usr/local', '1', 1, 'admin', NULL, NULL, '2019-11-19 08:40:55', NULL);
INSERT INTO `tb_machine_file` VALUES (23, 4, '根目录', '/', '1', 1, 'admin', NULL, NULL, '2019-12-11 07:54:04', NULL);
INSERT INTO `tb_machine_file` VALUES (24, 4, '项目目录', '/usr/local/java', '1', 1, 'admin', NULL, NULL, '2020-03-07 07:12:20', NULL);
COMMIT;

-- ----------------------------
-- Table structure for tb_machine_script
-- ----------------------------
DROP TABLE IF EXISTS `tb_machine_script`;
CREATE TABLE `tb_machine_script` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `machine_id` int(11) NOT NULL COMMENT '机器id',
  `desc` varchar(45) DEFAULT NULL COMMENT '服务描述',
  `script` varchar(128) NOT NULL COMMENT '服务脚本',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_machine_script
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for tb_redis
-- ----------------------------
DROP TABLE IF EXISTS `tb_redis`;
CREATE TABLE `tb_redis` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `host` varchar(45) NOT NULL,
  `port` int(11) NOT NULL,
  `pwd` varchar(45) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  `cluster_id` bigint(20) DEFAULT NULL,
  `create_account_id` bigint(20) unsigned DEFAULT NULL,
  `create_account` varchar(45) DEFAULT NULL,
  `update_account_id` bigint(20) unsigned DEFAULT NULL,
  `update_account` varchar(45) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_redis
-- ----------------------------
BEGIN;
INSERT INTO `tb_redis` VALUES (1, '127.0.0.1', 6379, NULL, '测试使用jjjj', 0, NULL, NULL, NULL, NULL, '2020-02-27 09:55:08', '2020-02-27 09:55:08.237432');
INSERT INTO `tb_redis` VALUES (15, 'mayfly.1yue.net', 6379, '', '京东云Redis', 0, NULL, NULL, NULL, NULL, '2020-02-28 01:16:26', '2020-02-28 01:16:26.255366');
COMMIT;

-- ----------------------------
-- Table structure for tb_redis_cluster
-- ----------------------------
DROP TABLE IF EXISTS `tb_redis_cluster`;
CREATE TABLE `tb_redis_cluster` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_redis_cluster
-- ----------------------------
BEGIN;
INSERT INTO `tb_redis_cluster` VALUES (1, '测试1', '2018-10-12 10:09:36', '2018-10-12 10:09:36');
INSERT INTO `tb_redis_cluster` VALUES (2, '测试2', '2018-10-12 10:09:36', '2018-10-12 10:09:36');
COMMIT;

-- ----------------------------
-- Table structure for tb_resource
-- ----------------------------
DROP TABLE IF EXISTS `tb_resource`;
CREATE TABLE `tb_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pid` bigint(20) NOT NULL,
  `type` tinyint(2) NOT NULL DEFAULT '1' COMMENT '1：菜单；2：资源（按钮等）',
  `code` varchar(45) DEFAULT NULL COMMENT '标识符（资源为菜单时表示path，其他为自定义标识符）',
  `name` varchar(60) NOT NULL,
  `url` varchar(64) DEFAULT NULL,
  `icon` varchar(45) DEFAULT NULL,
  `weight` int(11) NOT NULL,
  `status` tinyint(2) NOT NULL,
  `create_account_id` bigint(20) unsigned DEFAULT NULL,
  `create_account` varchar(45) DEFAULT NULL,
  `update_account_id` bigint(20) unsigned DEFAULT NULL,
  `update_account` varchar(45) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=128 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_resource
-- ----------------------------
BEGIN;
INSERT INTO `tb_resource` VALUES (1, 0, 1, '', '系统管理', NULL, 'el-icon-menu', 2, 1, 1, 'admin', 1, 'admin', '2018-10-12 10:09:36', '2020-03-03 05:40:31');
INSERT INTO `tb_resource` VALUES (8, 0, 1, 'index', '首页', '/', 'fa fa-tachometer', 1, 1, 1, 'admin', 1, 'admin', '2018-10-12 10:09:36', '2020-04-02 05:44:10');
INSERT INTO `tb_resource` VALUES (9, 1, 1, 'resource', '菜单&权限', '/resources', NULL, 1, 1, 1, 'admin', NULL, NULL, '2018-10-12 10:09:36', '2020-03-03 03:14:45');
INSERT INTO `tb_resource` VALUES (16, 0, 1, '', '运维', NULL, 'el-icon-menu', 3, 1, 1, 'admin', 1, 'admin', '2019-01-02 16:18:00', '2020-03-05 05:13:16');
INSERT INTO `tb_resource` VALUES (19, 16, 1, 'redis', 'redis节点', '/redis', NULL, 2, 1, 1, 'admin', NULL, NULL, '2019-01-04 11:29:11', '2019-11-15 01:27:53');
INSERT INTO `tb_resource` VALUES (21, 16, 1, 'redis:cluster', 'redis集群', 'http://localhost:8016/#/role_manage', NULL, 3, 1, 1, 'admin', NULL, NULL, '2019-01-21 15:44:07', '2019-12-16 03:35:04');
INSERT INTO `tb_resource` VALUES (27, 1, 1, 'role', '角色管理', '/roles', NULL, 2, 1, 1, 'admin', 1, 'admin', '2019-07-02 17:56:24', '2020-03-29 04:18:24');
INSERT INTO `tb_resource` VALUES (28, 1, 1, 'account', '账号管理', '/accounts', NULL, 3, 1, 1, 'admin', 1, 'admin', '2019-07-02 17:57:15', '2020-03-29 04:18:20');
INSERT INTO `tb_resource` VALUES (33, 9, 2, 'resource:save', '新增按钮', NULL, NULL, 4, 1, 1, 'admin', 1, 'admin', '2019-07-26 02:22:16', '2020-04-20 05:35:36');
INSERT INTO `tb_resource` VALUES (34, 9, 2, 'resource:update', '编辑按钮', NULL, NULL, 3, 1, 1, 'admin', 1, 'admin', '2019-07-26 18:22:50', '2020-04-20 05:35:37');
INSERT INTO `tb_resource` VALUES (35, 9, 2, 'resource:delete', '删除按钮', NULL, NULL, 2, 1, 1, 'admin', 1, 'admin', '2019-07-26 02:27:04', '2020-04-20 04:52:16');
INSERT INTO `tb_resource` VALUES (54, 27, 2, 'role:delete', '删除按钮', NULL, NULL, 1, 1, 1, 'admin', 1, 'admin', '2019-08-19 19:53:57', '2020-03-03 05:42:16');
INSERT INTO `tb_resource` VALUES (58, 28, 2, 'account:delete', '删除按钮', NULL, NULL, 3, 1, 1, 'admin', 1, 'admin', '2019-08-21 10:58:14', '2020-03-06 08:08:28');
INSERT INTO `tb_resource` VALUES (64, 21, 2, 'redis:add', '新增按钮', NULL, NULL, 1, 1, 1, 'admin', NULL, NULL, '2019-08-21 14:27:18', '2019-08-21 14:27:18');
INSERT INTO `tb_resource` VALUES (71, 19, 2, 'redis:key:delete', 'key删除按钮', NULL, NULL, 6, 1, 1, 'admin', NULL, NULL, '2019-09-08 15:56:21', '2019-09-08 15:56:42');
INSERT INTO `tb_resource` VALUES (73, 19, 2, 'redis:key:add', '新增key-value按钮', NULL, NULL, 4, 1, 1, 'admin', NULL, NULL, '2019-09-08 16:16:10', '2019-09-08 16:16:10');
INSERT INTO `tb_resource` VALUES (74, 19, 2, 'redis:key', 'key操作基本权限', NULL, NULL, 2, 1, 1, 'admin', 1, 'admin', '2019-09-08 16:21:23', '2020-03-07 02:39:17');
INSERT INTO `tb_resource` VALUES (84, 16, 1, 'machine', '机器', '/machines', '', 1, 1, 1, 'admin', NULL, NULL, '2019-11-04 14:39:49', '2020-01-17 13:23:20');
INSERT INTO `tb_resource` VALUES (88, 84, 2, 'machineFile:addConf', '添加配置文件按钮', NULL, NULL, 4, 1, 1, 'admin', 9, 'test', '2019-11-06 11:17:16', '2020-03-07 02:24:02');
INSERT INTO `tb_resource` VALUES (89, 84, 2, 'machineFile:updateFileContent', '修改文件内容权限（确定按钮）', NULL, NULL, 5, 1, 1, 'admin', NULL, NULL, '2019-11-06 11:22:31', '2020-01-21 01:21:26');
INSERT INTO `tb_resource` VALUES (94, 84, 2, 'machineFile:rm', '删除文件', NULL, NULL, 8, 1, 1, 'admin', 9, 'test', '2019-12-23 05:57:12', '2020-03-07 02:23:47');
INSERT INTO `tb_resource` VALUES (96, 84, 2, 'machineFile:upload', '上传文件', NULL, NULL, 10, 1, 1, 'admin', 9, 'test', '2019-12-23 05:58:19', '2020-03-07 02:23:55');
INSERT INTO `tb_resource` VALUES (102, 16, 1, 'jenkins', 'jenkins', 'http://ampvpstest.ucarinc.com/#/contract/list', NULL, 4, 1, 1, 'admin', 1, 'admin', '2020-01-04 12:38:36', '2020-03-08 03:13:56');
INSERT INTO `tb_resource` VALUES (115, 1, 1, 'log', '操作日志', '/logs', NULL, 4, 1, 1, 'admin', 1, 'admin', '2020-03-05 07:41:55', '2020-03-07 02:20:06');
INSERT INTO `tb_resource` VALUES (121, 84, 2, 'machineFile', '机器文件配置基本权限', NULL, NULL, 2, 1, 1, 'admin', 1, 'admin', '2020-03-06 07:29:46', '2020-03-07 02:38:56');
INSERT INTO `tb_resource` VALUES (122, 27, 2, 'role:saveResources', '分配菜单与权限', NULL, NULL, 2, 1, 1, 'admin', 1, 'admin', '2020-03-06 08:05:59', '2020-03-06 08:05:59');
INSERT INTO `tb_resource` VALUES (123, 28, 2, 'account:saveRoles', '角色分配', NULL, NULL, 2, 1, 1, 'admin', 1, 'admin', '2020-03-06 08:08:23', '2020-03-06 08:08:23');
COMMIT;

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `status` tinyint(2) NOT NULL,
  `remark` varchar(45) DEFAULT NULL,
  `create_account_id` bigint(20) unsigned DEFAULT NULL,
  `create_account` varchar(45) DEFAULT NULL,
  `update_account_id` bigint(20) unsigned NOT NULL,
  `update_account` varchar(45) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_role
-- ----------------------------
BEGIN;
INSERT INTO `tb_role` VALUES (1, '超级管理员', 1, '具有全站所有权限', 1, 'admin', 0, NULL, '2018-12-17 15:14:10', '2018-12-17 15:14:10');
INSERT INTO `tb_role` VALUES (2, '游客', 1, '一些重要操作没有权限，如重要资源的增删改', 1, 'admin', 1, 'admin', '2019-08-19 15:11:09', '2020-03-29 04:04:22');
INSERT INTO `tb_role` VALUES (4, '系统管理员', 1, '拥有系统管理等权限', 1, 'admin', 1, 'admin', '2020-03-30 00:55:18', '2020-03-30 00:55:18');
INSERT INTO `tb_role` VALUES (5, '运维管理员', 1, '拥有运维相关权限', 1, 'admin', 1, 'admin', '2020-03-30 00:56:28', '2020-03-30 00:56:28');
COMMIT;

-- ----------------------------
-- Table structure for tb_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_resource`;
CREATE TABLE `tb_role_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL,
  `resource_id` bigint(20) NOT NULL,
  `create_account_id` bigint(20) unsigned DEFAULT NULL,
  `create_account` varchar(45) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=307 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_role_resource
-- ----------------------------
BEGIN;
INSERT INTO `tb_role_resource` VALUES (150, 1, 8, 1, 'admin', '2019-08-19 19:38:50');
INSERT INTO `tb_role_resource` VALUES (151, 1, 1, 1, 'admin', '2019-08-19 19:38:50');
INSERT INTO `tb_role_resource` VALUES (152, 1, 9, 1, 'admin', '2019-08-19 19:38:50');
INSERT INTO `tb_role_resource` VALUES (155, 1, 35, 1, 'admin', '2019-08-19 19:38:50');
INSERT INTO `tb_role_resource` VALUES (156, 1, 34, 1, 'admin', '2019-08-19 19:38:50');
INSERT INTO `tb_role_resource` VALUES (157, 1, 33, 1, 'admin', '2019-08-19 19:38:50');
INSERT INTO `tb_role_resource` VALUES (158, 1, 27, 1, 'admin', '2019-08-19 19:38:50');
INSERT INTO `tb_role_resource` VALUES (163, 1, 28, 1, 'admin', '2019-08-19 19:38:50');
INSERT INTO `tb_role_resource` VALUES (165, 1, 16, 1, 'admin', '2019-08-19 19:38:50');
INSERT INTO `tb_role_resource` VALUES (167, 1, 19, 1, 'admin', '2019-08-19 19:38:50');
INSERT INTO `tb_role_resource` VALUES (170, 2, 28, 1, 'admin', '2019-08-19 19:41:52');
INSERT INTO `tb_role_resource` VALUES (172, 2, 1, 1, 'admin', '2019-08-19 19:41:52');
INSERT INTO `tb_role_resource` VALUES (174, 2, 27, 1, 'admin', '2019-08-19 19:41:52');
INSERT INTO `tb_role_resource` VALUES (176, 1, 54, 1, 'admin', '2019-08-19 19:54:15');
INSERT INTO `tb_role_resource` VALUES (177, 2, 8, 1, 'admin', '2019-08-19 20:39:26');
INSERT INTO `tb_role_resource` VALUES (184, 1, 58, 1, 'admin', '2019-08-21 11:30:17');
INSERT INTO `tb_role_resource` VALUES (185, 1, 67, 1, 'admin', '2019-08-21 15:38:58');
INSERT INTO `tb_role_resource` VALUES (186, 1, 21, 1, 'admin', '2019-08-21 15:56:03');
INSERT INTO `tb_role_resource` VALUES (187, 1, 64, 1, 'admin', '2019-08-21 15:56:03');
INSERT INTO `tb_role_resource` VALUES (193, 2, 67, 1, 'admin', '2019-08-22 09:01:57');
INSERT INTO `tb_role_resource` VALUES (197, 1, 71, 1, 'admin', '2019-09-08 15:56:52');
INSERT INTO `tb_role_resource` VALUES (199, 1, 73, 1, 'admin', '2019-09-08 16:20:08');
INSERT INTO `tb_role_resource` VALUES (200, 1, 74, 1, 'admin', '2019-09-08 16:21:42');
INSERT INTO `tb_role_resource` VALUES (201, 1, 84, 1, 'admin', '2019-11-04 14:40:47');
INSERT INTO `tb_role_resource` VALUES (202, 2, 16, 1, 'admin', '2019-11-05 15:11:12');
INSERT INTO `tb_role_resource` VALUES (204, 2, 21, 1, 'admin', '2019-11-05 15:11:12');
INSERT INTO `tb_role_resource` VALUES (205, 2, 64, 1, 'admin', '2019-11-05 15:11:12');
INSERT INTO `tb_role_resource` VALUES (206, 2, 19, 1, 'admin', '2019-11-05 15:11:12');
INSERT INTO `tb_role_resource` VALUES (217, 1, 88, 1, 'admin', '2019-11-06 11:24:16');
INSERT INTO `tb_role_resource` VALUES (218, 1, 89, 1, 'admin', '2019-11-06 11:24:16');
INSERT INTO `tb_role_resource` VALUES (223, 1, 94, 1, 'admin', '2019-12-23 06:16:23');
INSERT INTO `tb_role_resource` VALUES (225, 1, 96, 1, 'admin', '2019-12-23 06:16:23');
INSERT INTO `tb_role_resource` VALUES (227, 1, 102, 1, 'admin', '2020-01-04 12:38:43');
INSERT INTO `tb_role_resource` VALUES (241, 2, 9, 1, 'admin', '2020-03-01 08:14:31');
INSERT INTO `tb_role_resource` VALUES (255, 1, 115, 1, 'admin', '2020-03-05 08:10:21');
INSERT INTO `tb_role_resource` VALUES (257, 1, 117, 1, 'admin', '2020-03-06 06:26:18');
INSERT INTO `tb_role_resource` VALUES (258, 1, 118, 1, 'admin', '2020-03-06 06:26:18');
INSERT INTO `tb_role_resource` VALUES (259, 1, 120, 1, 'admin', '2020-03-06 07:44:49');
INSERT INTO `tb_role_resource` VALUES (260, 1, 121, 1, 'admin', '2020-03-06 07:44:49');
INSERT INTO `tb_role_resource` VALUES (261, 1, 119, 1, 'admin', '2020-03-06 07:44:49');
INSERT INTO `tb_role_resource` VALUES (262, 2, 117, 1, 'admin', '2020-03-06 08:01:17');
INSERT INTO `tb_role_resource` VALUES (263, 2, 118, 1, 'admin', '2020-03-06 08:01:17');
INSERT INTO `tb_role_resource` VALUES (264, 2, 120, 1, 'admin', '2020-03-06 08:01:17');
INSERT INTO `tb_role_resource` VALUES (265, 2, 119, 1, 'admin', '2020-03-06 08:01:17');
INSERT INTO `tb_role_resource` VALUES (266, 2, 84, 1, 'admin', '2020-03-06 08:01:17');
INSERT INTO `tb_role_resource` VALUES (267, 1, 122, 1, 'admin', '2020-03-06 08:06:41');
INSERT INTO `tb_role_resource` VALUES (268, 1, 123, 1, 'admin', '2020-03-06 08:08:50');
INSERT INTO `tb_role_resource` VALUES (270, 2, 115, 1, 'admin', '2020-03-07 02:31:29');
INSERT INTO `tb_role_resource` VALUES (271, 1, 124, 1, 'admin', '2020-03-07 02:37:39');
INSERT INTO `tb_role_resource` VALUES (272, 2, 124, 1, 'admin', '2020-03-07 02:40:35');
INSERT INTO `tb_role_resource` VALUES (273, 2, 102, 1, 'admin', '2020-03-28 09:49:45');
INSERT INTO `tb_role_resource` VALUES (275, 4, 1, 1, 'admin', '2020-03-30 00:55:27');
INSERT INTO `tb_role_resource` VALUES (276, 4, 9, 1, 'admin', '2020-03-30 00:55:27');
INSERT INTO `tb_role_resource` VALUES (277, 4, 117, 1, 'admin', '2020-03-30 00:55:27');
INSERT INTO `tb_role_resource` VALUES (278, 4, 35, 1, 'admin', '2020-03-30 00:55:27');
INSERT INTO `tb_role_resource` VALUES (279, 4, 34, 1, 'admin', '2020-03-30 00:55:27');
INSERT INTO `tb_role_resource` VALUES (280, 4, 33, 1, 'admin', '2020-03-30 00:55:27');
INSERT INTO `tb_role_resource` VALUES (281, 4, 27, 1, 'admin', '2020-03-30 00:55:27');
INSERT INTO `tb_role_resource` VALUES (282, 4, 118, 1, 'admin', '2020-03-30 00:55:27');
INSERT INTO `tb_role_resource` VALUES (283, 4, 54, 1, 'admin', '2020-03-30 00:55:27');
INSERT INTO `tb_role_resource` VALUES (284, 4, 122, 1, 'admin', '2020-03-30 00:55:27');
INSERT INTO `tb_role_resource` VALUES (285, 4, 28, 1, 'admin', '2020-03-30 00:55:27');
INSERT INTO `tb_role_resource` VALUES (286, 4, 67, 1, 'admin', '2020-03-30 00:55:27');
INSERT INTO `tb_role_resource` VALUES (287, 4, 123, 1, 'admin', '2020-03-30 00:55:27');
INSERT INTO `tb_role_resource` VALUES (288, 4, 58, 1, 'admin', '2020-03-30 00:55:27');
INSERT INTO `tb_role_resource` VALUES (289, 4, 115, 1, 'admin', '2020-03-30 00:55:27');
INSERT INTO `tb_role_resource` VALUES (290, 4, 124, 1, 'admin', '2020-03-30 00:55:27');
INSERT INTO `tb_role_resource` VALUES (291, 5, 16, 1, 'admin', '2020-03-30 00:56:33');
INSERT INTO `tb_role_resource` VALUES (292, 5, 84, 1, 'admin', '2020-03-30 00:56:33');
INSERT INTO `tb_role_resource` VALUES (293, 5, 120, 1, 'admin', '2020-03-30 00:56:33');
INSERT INTO `tb_role_resource` VALUES (294, 5, 121, 1, 'admin', '2020-03-30 00:56:33');
INSERT INTO `tb_role_resource` VALUES (295, 5, 88, 1, 'admin', '2020-03-30 00:56:33');
INSERT INTO `tb_role_resource` VALUES (296, 5, 89, 1, 'admin', '2020-03-30 00:56:33');
INSERT INTO `tb_role_resource` VALUES (297, 5, 94, 1, 'admin', '2020-03-30 00:56:33');
INSERT INTO `tb_role_resource` VALUES (298, 5, 96, 1, 'admin', '2020-03-30 00:56:33');
INSERT INTO `tb_role_resource` VALUES (299, 5, 19, 1, 'admin', '2020-03-30 00:56:33');
INSERT INTO `tb_role_resource` VALUES (300, 5, 119, 1, 'admin', '2020-03-30 00:56:33');
INSERT INTO `tb_role_resource` VALUES (301, 5, 74, 1, 'admin', '2020-03-30 00:56:33');
INSERT INTO `tb_role_resource` VALUES (302, 5, 73, 1, 'admin', '2020-03-30 00:56:33');
INSERT INTO `tb_role_resource` VALUES (303, 5, 71, 1, 'admin', '2020-03-30 00:56:33');
INSERT INTO `tb_role_resource` VALUES (304, 5, 21, 1, 'admin', '2020-03-30 00:56:33');
INSERT INTO `tb_role_resource` VALUES (305, 5, 64, 1, 'admin', '2020-03-30 00:56:33');
INSERT INTO `tb_role_resource` VALUES (306, 5, 102, 1, 'admin', '2020-03-30 00:56:33');
COMMIT;

-- ----------------------------
-- Table structure for tb_sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `tb_sys_operation_log`;
CREATE TABLE `tb_sys_operation_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `type` tinyint(2) DEFAULT NULL COMMENT '1：正常操作；2：异常信息',
  `operation` varchar(1024) NOT NULL,
  `create_account_id` bigint(20) unsigned NOT NULL,
  `create_account` varchar(45) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=247 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of tb_sys_operation_log
-- ----------------------------
BEGIN;
INSERT INTO `tb_sys_operation_log` VALUES (204, 4, '查询redis value\n==> Invoke: mayfly.sys.module.redis.controller.RedisController#value(cluster:false, id:15, key:\"account:token:ed73ec12-fafb-458d-bfa6-aeffd6f76220\") -> (ExeTime: 86)', 1, 'admin', '2020-03-07 03:26:00');
INSERT INTO `tb_sys_operation_log` VALUES (205, 4, '查询redis value\n==> Invoke: mayfly.sys.module.redis.controller.RedisController#value(cluster:false, id:15, key:\"account:token:ed73ec12-fafb-458d-bfa6-aeffd6f76220\") -> (ExeTime: 80)', 1, 'admin', '2020-03-07 03:26:03');
INSERT INTO `tb_sys_operation_log` VALUES (206, 4, '新增文件配置\n==> Invoke: mayfly.sys.module.machine.controller.MachineFileController#addConf(machineId:4, form:{\"name\":\"项目目录\",\"path\":\"/usr/local/java\",\"type\":1}) -> (ExeTime: 158)\n<== Result: {\"code\":200,\"data\":{\"createAccount\":\"admin\",\"createAccountId\":1,\"createTime\":\"2020-03-07T15:12:19.555415000\",\"id\":24,\"machineId\":4,\"name\":\"项目目录\",\"path\":\"/usr/local/java\",\"type\":1,\"updateAccount\":\"admin\",\"updateAccountId\":1,\"updateTime\":\"2020-03-07T15:12:19.555415000\"},\"msg\":\"操作成功\",\"success\":true}', 1, 'admin', '2020-03-07 07:12:20');
INSERT INTO `tb_sys_operation_log` VALUES (207, 4, '资源管理:update\n==> Invoke: mayfly.sys.module.sys.service.impl.ResourceServiceImpl#update(resource:{\"code\":\"http://ampvpstest.ucarinc.com/#/contract/list\",\"id\":102,\"name\":\"jenkins\",\"type\":1,\"updateAccount\":\"admin\",\"updateAccountId\":1,\"updateTime\":\"2020-03-08T10:15:17.909674000\",\"weight\":4}) -> (ExeTime: 71)', 1, 'admin', '2020-03-08 02:15:18');
INSERT INTO `tb_sys_operation_log` VALUES (208, 2, '更新权限&菜单(id = 102) => (code: http://118.24.26.101:8888 -> http://ampvpstest.ucarinc.com/#/contract/list)', 1, 'admin', '2020-03-08 02:15:18');
INSERT INTO `tb_sys_operation_log` VALUES (209, 2, '更新权限&菜单(id = 102) => (code: http://ampvpstest.ucarinc.com/#/contract/list -> http://www.baidu.com)', 1, 'admin', '2020-03-08 02:48:11');
INSERT INTO `tb_sys_operation_log` VALUES (210, 4, '资源管理:update\n==> Invoke: mayfly.sys.module.sys.service.impl.ResourceServiceImpl#update(resource:{\"code\":\"http://www.baidu.com\",\"id\":102,\"name\":\"jenkins\",\"type\":1,\"updateAccount\":\"admin\",\"updateAccountId\":1,\"updateTime\":\"2020-03-08T10:48:11.470139000\",\"weight\":4}) -> (ExeTime: 24)', 1, 'admin', '2020-03-08 02:48:11');
INSERT INTO `tb_sys_operation_log` VALUES (211, 2, '更新权限&菜单(id = 102) => (code: http://www.baidu.com -> http://ampvpstest.ucarinc.com/#/contract/list)', 1, 'admin', '2020-03-08 02:51:41');
INSERT INTO `tb_sys_operation_log` VALUES (212, 4, '资源管理:update\n==> Invoke: mayfly.sys.module.sys.service.impl.ResourceServiceImpl#update(resource:{\"code\":\"http://ampvpstest.ucarinc.com/#/contract/list\",\"id\":102,\"name\":\"jenkins\",\"type\":1,\"updateAccount\":\"admin\",\"updateAccountId\":1,\"updateTime\":\"2020-03-08T10:51:40.711348000\",\"weight\":4}) -> (ExeTime: 19)', 1, 'admin', '2020-03-08 02:51:41');
INSERT INTO `tb_sys_operation_log` VALUES (213, 2, '更新权限&菜单(id = 102) => (code: http://ampvpstest.ucarinc.com/#/contract/list -> http://www.baidu.com)', 1, 'admin', '2020-03-08 03:02:22');
INSERT INTO `tb_sys_operation_log` VALUES (214, 4, '资源管理:update\n==> Invoke: mayfly.sys.module.sys.service.impl.ResourceServiceImpl#update(resource:{\"code\":\"http://www.baidu.com\",\"id\":102,\"name\":\"jenkins\",\"type\":1,\"updateAccount\":\"admin\",\"updateAccountId\":1,\"updateTime\":\"2020-03-08T11:02:21.786739000\",\"weight\":4}) -> (ExeTime: 19)', 1, 'admin', '2020-03-08 03:02:22');
INSERT INTO `tb_sys_operation_log` VALUES (215, 4, '资源管理:update\n==> Invoke: mayfly.sys.module.sys.service.impl.ResourceServiceImpl#update(resource:{\"code\":\"http://ampvpstest.ucarinc.com/#/contract/list\",\"id\":102,\"name\":\"jenkins\",\"type\":1,\"updateAccount\":\"admin\",\"updateAccountId\":1,\"updateTime\":\"2020-03-08T11:13:55.734949000\",\"weight\":4}) -> (ExeTime: 23)', 1, 'admin', '2020-03-08 03:13:56');
INSERT INTO `tb_sys_operation_log` VALUES (216, 2, '更新权限&菜单(id = 102) => (code: http://www.baidu.com -> http://ampvpstest.ucarinc.com/#/contract/list)', 1, 'admin', '2020-03-08 03:13:56');
INSERT INTO `tb_sys_operation_log` VALUES (217, 4, '查询redis value\n==> Invoke: mayfly.sys.module.redis.controller.RedisController#value(cluster:false, id:1, key:\"account:token:b3213302-0fa9-478b-9496-4e3a8e654e67\") -> (ExeTime: 5)', 1, 'admin', '2020-03-08 08:27:41');
INSERT INTO `tb_sys_operation_log` VALUES (218, 4, '查询redis value\n==> Invoke: mayfly.sys.module.redis.controller.RedisController#value(cluster:false, id:1, key:\"account:token:1cd342fb-68a7-4372-80b1-0eaa18b243ab\") -> (ExeTime: 12)', 1, 'admin', '2020-03-09 02:43:19');
INSERT INTO `tb_sys_operation_log` VALUES (219, 4, '查询redis value\n==> Invoke: mayfly.sys.module.redis.controller.RedisController#value(cluster:false, id:1, key:\"account:token:b0de2c63-5440-49ff-b2ac-94d206922e98\") -> (ExeTime: 6)', 1, 'admin', '2020-03-09 06:53:44');
INSERT INTO `tb_sys_operation_log` VALUES (220, 4, '资源管理:update\n==> Invoke: mayfly.sys.module.sys.service.impl.ResourceServiceImpl#update(resource:{\"code\":\"/\",\"icon\":\"fa fa-tachometer\",\"id\":8,\"name\":\"首页\",\"type\":1,\"updateAccount\":\"admin\",\"updateAccountId\":1,\"updateTime\":\"2020-03-11T09:06:08.010134000\",\"weight\":1}) -> (ExeTime: 26)', 1, 'admin', '2020-03-11 01:06:08');
INSERT INTO `tb_sys_operation_log` VALUES (221, 4, '资源管理:update\n==> Invoke: mayfly.sys.module.sys.service.impl.ResourceServiceImpl#update(resource:{\"code\":\"/\",\"icon\":\"fa fa-tachometer\",\"id\":8,\"name\":\"首页\",\"type\":1,\"updateAccount\":\"admin\",\"updateAccountId\":1,\"updateTime\":\"2020-03-11T09:07:05.502043000\",\"weight\":2}) -> (ExeTime: 15)', 1, 'admin', '2020-03-11 01:07:06');
INSERT INTO `tb_sys_operation_log` VALUES (222, 2, '更新权限&菜单(id = 8) => (weight: 1 -> 2)', 1, 'admin', '2020-03-11 01:07:06');
INSERT INTO `tb_sys_operation_log` VALUES (223, 2, '更新权限&菜单(id = 8) => (weight: 2 -> 1)', 1, 'admin', '2020-03-11 01:07:19');
INSERT INTO `tb_sys_operation_log` VALUES (224, 4, '资源管理:update\n==> Invoke: mayfly.sys.module.sys.service.impl.ResourceServiceImpl#update(resource:{\"code\":\"/\",\"icon\":\"fa fa-tachometer\",\"id\":8,\"name\":\"首页\",\"type\":1,\"updateAccount\":\"admin\",\"updateAccountId\":1,\"updateTime\":\"2020-03-11T09:07:19.222162000\",\"weight\":1}) -> (ExeTime: 16)', 1, 'admin', '2020-03-11 01:07:19');
INSERT INTO `tb_sys_operation_log` VALUES (225, 2, '更新权限&菜单(id = 8) => (name: 首页 -> 0首页)', 1, 'admin', '2020-03-27 13:38:13');
INSERT INTO `tb_sys_operation_log` VALUES (226, 2, '更新权限&菜单(id = 8) => (name: 0首页 -> 首页)', 1, 'admin', '2020-03-27 13:38:20');
INSERT INTO `tb_sys_operation_log` VALUES (227, 2, '更新权限&菜单(id = 35) => (weight: 1 -> 2)', 1, 'admin', '2020-03-28 09:51:47');
INSERT INTO `tb_sys_operation_log` VALUES (228, 2, '修改角色(id = 2) => (name: 无名游客 -> 游客)', 1, 'admin', '2020-03-29 04:04:22');
INSERT INTO `tb_sys_operation_log` VALUES (229, 2, '更新权限&菜单(id = 8) => (name: 首页 -> 首页111)', 1, 'admin', '2020-03-30 06:18:01');
INSERT INTO `tb_sys_operation_log` VALUES (230, 2, '更新权限&菜单(id = 8) => (name: 首页111 -> 首页)', 1, 'admin', '2020-03-30 06:18:06');
INSERT INTO `tb_sys_operation_log` VALUES (231, 2, '/usr/local/java/mayfly-sys-1.0-SNAPSHOT-exec.jar文件上传成功', 1, 'admin', '2020-03-30 13:06:44');
INSERT INTO `tb_sys_operation_log` VALUES (232, 2, '/usr/local/java/TreeUtils.png文件上传成功', 1, 'admin', '2020-04-08 08:06:01');
INSERT INTO `tb_sys_operation_log` VALUES (233, 2, '/usr/local/java/VUE权限控制.png文件上传成功', 1, 'admin', '2020-04-08 08:07:52');
INSERT INTO `tb_sys_operation_log` VALUES (234, 2, '/usr/local/java/Mybatis通用Mapper.png文件上传成功', 1, 'admin', '2020-04-08 08:07:55');
INSERT INTO `tb_sys_operation_log` VALUES (235, 2, '/usr/local/java/VUE等项目 枚举值统一 管理维护.png文件上传成功', 1, 'admin', '2020-04-08 08:11:28');
INSERT INTO `tb_sys_operation_log` VALUES (236, 2, '/usr/local/java/spring组合注解.png文件上传成功', 1, 'admin', '2020-04-08 09:04:25');
INSERT INTO `tb_sys_operation_log` VALUES (237, 2, '/usr/local/java/TreeUtils.png文件上传成功', 1, 'admin', '2020-04-08 09:06:31');
INSERT INTO `tb_sys_operation_log` VALUES (238, 2, '/usr/local/java/code.jpg文件上传成功', 1, 'admin', '2020-04-08 09:23:38');
INSERT INTO `tb_sys_operation_log` VALUES (239, 5, '文件上传失败：session不存在', 1, 'admin', '2020-04-08 09:23:38');
INSERT INTO `tb_sys_operation_log` VALUES (240, 5, '文件上传失败：session不存在', 1, 'admin', '2020-04-08 09:24:34');
INSERT INTO `tb_sys_operation_log` VALUES (241, 2, '/usr/local/java/TreeUtils.png文件上传成功', 1, 'admin', '2020-04-08 09:24:34');
INSERT INTO `tb_sys_operation_log` VALUES (242, 2, '/usr/local/java/spring组合注解.png文件上传成功', 1, 'admin', '2020-04-08 09:32:30');
INSERT INTO `tb_sys_operation_log` VALUES (243, 5, '文件上传失败：session不存在', 1, 'admin', '2020-04-08 09:32:30');
INSERT INTO `tb_sys_operation_log` VALUES (244, 2, '/usr/local/java/TreeUtils.png文件上传成功', 1, 'admin', '2020-04-08 09:35:52');
INSERT INTO `tb_sys_operation_log` VALUES (245, 2, '/usr/local/java/deploy.sh文件上传成功', 1, 'admin', '2020-04-20 08:01:47');
INSERT INTO `tb_sys_operation_log` VALUES (246, 2, '/usr/local/java/deploy.sh文件上传成功', 1, 'admin', '2020-04-21 08:04:50');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
