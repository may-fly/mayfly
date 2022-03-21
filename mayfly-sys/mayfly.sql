/*
 Navicat Premium Data Transfer

 Source Server         : JD云
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : 114.67.67.10:3306
 Source Schema         : mayfly

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 13/09/2021 09:40:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_account
-- ----------------------------
DROP TABLE IF EXISTS `t_account`;
CREATE TABLE `t_account` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `status` tinyint(2) NOT NULL,
  `creator_id` bigint(20) NOT NULL DEFAULT '0',
  `creator` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0',
  `modifier_id` bigint(20) NOT NULL DEFAULT '0',
  `modifier` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `last_login_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_account
-- ----------------------------
BEGIN;
INSERT INTO `t_account` VALUES (1, 'admin', '7b116d117002fad3f6dde6c718b885b9', 1, 1, 'admin', 1, 'admin', '2020-03-05 07:41:18', '2021-09-03 15:02:25', '2021-09-03 15:02:25');
INSERT INTO `t_account` VALUES (9, 'test', 'e10adc3949ba59abbe56e057f20f883e', 1, 1, 'admin', 1, 'admin', '2019-08-21 11:30:33', '2021-09-11 16:18:34', '2021-09-11 16:18:34');
INSERT INTO `t_account` VALUES (10, 'test2', 'ad0234829205b9033196ba818f7a872b', 0, 1, 'admin', 1, 'admin', '2019-12-12 09:22:37', '2021-09-03 15:04:02', NULL);
INSERT INTO `t_account` VALUES (11, 'meilin.huang', 'e10adc3949ba59abbe56e057f20f883e', 1, 1, 'admin', 1, 'admin', '2019-12-12 09:31:36', '2021-07-12 23:10:50', '2021-07-12 23:10:50');
INSERT INTO `t_account` VALUES (12, 'test3', '859e58a36cfe12a7f97b3b02f7b13380', 1, 1, 'admin', 1, 'admin', '2020-03-30 13:20:51', '2021-05-31 17:14:06', NULL);
COMMIT;

-- ----------------------------
-- Table structure for t_account_role
-- ----------------------------
DROP TABLE IF EXISTS `t_account_role`;
CREATE TABLE `t_account_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `account_id` bigint(20) NOT NULL COMMENT '账号id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `creator` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `creator_id` bigint(20) unsigned DEFAULT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_account_role
-- ----------------------------
BEGIN;
INSERT INTO `t_account_role` VALUES (23, 3, 6, 'admin', 1, '2021-05-28 16:19:21');
INSERT INTO `t_account_role` VALUES (24, 2, 1, 'admin', 1, '2021-05-28 16:21:38');
INSERT INTO `t_account_role` VALUES (25, 1, 1, 'admin', 1, '2021-05-28 16:21:45');
INSERT INTO `t_account_role` VALUES (26, 4, 6, 'admin', 1, '2021-05-28 17:04:48');
INSERT INTO `t_account_role` VALUES (29, 9, 6, 'admin', 1, '2021-05-31 14:11:43');
INSERT INTO `t_account_role` VALUES (31, 10, 2, 'admin', 1, '2021-06-10 10:11:33');
INSERT INTO `t_account_role` VALUES (32, 11, 2, 'admin', 1, '2021-06-10 10:11:40');
INSERT INTO `t_account_role` VALUES (33, 11, 6, 'admin', 1, '2021-06-21 16:40:13');
COMMIT;

-- ----------------------------
-- Table structure for t_db
-- ----------------------------
DROP TABLE IF EXISTS `t_db`;
CREATE TABLE `t_db` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `host` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `port` int(16) DEFAULT NULL,
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `type` tinyint(8) NOT NULL COMMENT '类型',
  `database` varchar(32) DEFAULT NULL,
  `jdbc_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `creator_id` bigint(20) DEFAULT NULL,
  `creator` varchar(32) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `modifier_id` bigint(20) DEFAULT NULL,
  `modifier` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='数据库信息表';

-- ----------------------------
-- Records of t_db
-- ----------------------------
BEGIN;
INSERT INTO `t_db` VALUES (1, 'maylfy', 'localhost', 3306, 'root', '111049', 1, 'mayfly', NULL, '2021-01-07 16:57:39', 1, '1', '2021-01-07 16:57:44', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for t_db_sql
-- ----------------------------
DROP TABLE IF EXISTS `t_db_sql`;
CREATE TABLE `t_db_sql` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `db_id` bigint(20) NOT NULL COMMENT '数据库id',
  `sql` text COMMENT 'sql',
  `create_time` datetime DEFAULT NULL,
  `creator_id` bigint(20) NOT NULL,
  `creator` varchar(32) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `modifier_id` bigint(20) DEFAULT NULL,
  `modifier` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='个人保存的sql记录';

-- ----------------------------
-- Records of t_db_sql
-- ----------------------------
BEGIN;
INSERT INTO `t_db_sql` VALUES (1, 1, 'SELECT * FROM t_resource\n\nSELECT\n  t.id, t.name, t.code /*标识符（资源为菜单时表示path，其他为自定义标识符）*/\nFROM\n  t_resource t WHERE t.type /* 1：菜单；2：资源（按钮等）*/ = 2', '2021-01-08 16:28:42', 1, 'admin', '2021-01-08 17:14:25', 1, 'admin');
INSERT INTO `t_db_sql` VALUES (2, 3, 'SELECT\n  *\nFROM\n  t_sys_operation_log\nORDER BY\n  id DESC\nLIMIT\n  20\n  \nSELECT\n  *\nFROM\n  t_machine_script', '2021-01-12 14:55:29', 1, 'admin', '2021-06-01 16:42:28', 1, 'admin');
COMMIT;

-- ----------------------------
-- Table structure for t_machine
-- ----------------------------
DROP TABLE IF EXISTS `t_machine`;
CREATE TABLE `t_machine` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `ip` varchar(45) NOT NULL,
  `port` int(11) NOT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `need_monitor` tinyint(255) DEFAULT NULL COMMENT '是否需要监控',
  `creator_id` bigint(20) unsigned DEFAULT NULL,
  `creator` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `modifier_id` bigint(20) unsigned DEFAULT NULL,
  `modifier` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='机器信息';

-- ----------------------------
-- Records of t_machine
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_machine_file
-- ----------------------------
DROP TABLE IF EXISTS `t_machine_file`;
CREATE TABLE `t_machine_file` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '机器文件配置（linux一切皆文件，故也可以表示目录）',
  `machine_id` bigint(20) NOT NULL,
  `name` varchar(45) NOT NULL,
  `path` varchar(45) NOT NULL,
  `type` varchar(45) NOT NULL COMMENT '1：目录；2：文件',
  `creator_id` bigint(20) unsigned DEFAULT NULL,
  `creator` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `modifier_id` bigint(20) unsigned DEFAULT NULL,
  `modifier` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COMMENT='机器文件';

-- ----------------------------
-- Records of t_machine_file
-- ----------------------------
BEGIN;
INSERT INTO `t_machine_file` VALUES (1, 1, 'redis配置文件', '/etc/my.cnf', '2', 1, 'admin', NULL, NULL, '2019-08-21 11:30:33', NULL);
INSERT INTO `t_machine_file` VALUES (2, 1, '测试文件', '/usr/local/test/file', '2', 1, 'admin', NULL, NULL, '2019-08-21 11:30:33', NULL);
INSERT INTO `t_machine_file` VALUES (7, 1, 'test', '/usr/local/test/file1', '2', 1, 'admin', NULL, NULL, '2019-11-05 16:12:58', NULL);
INSERT INTO `t_machine_file` VALUES (8, 2, 'redis', '/root/restart-redis.sh', '2', 1, 'admin', NULL, NULL, '2019-11-05 20:08:34', NULL);
INSERT INTO `t_machine_file` VALUES (10, 2, 'java-dockerfile', '/usr/local/java/Dockerfile', '2', 1, 'admin', NULL, NULL, '2019-11-06 15:16:12', NULL);
INSERT INTO `t_machine_file` VALUES (11, 1, 'usr', '/usr', '1', 1, 'admin', NULL, NULL, '2019-11-06 15:16:12', NULL);
INSERT INTO `t_machine_file` VALUES (12, 1, 'opt', '/opt', '1', 1, 'admin', NULL, NULL, '2019-11-06 15:16:12', NULL);
INSERT INTO `t_machine_file` VALUES (13, 3, '根目录', '/', '1', 1, 'admin', NULL, NULL, '2019-11-18 05:19:28', NULL);
INSERT INTO `t_machine_file` VALUES (14, 2, 'usr', '/usr/local', '1', 1, 'admin', NULL, NULL, '2019-11-19 05:52:28', NULL);
INSERT INTO `t_machine_file` VALUES (15, 3, 'usr', '/usr/', '1', 1, 'admin', NULL, NULL, '2019-11-19 08:35:19', NULL);
INSERT INTO `t_machine_file` VALUES (16, 3, '软件安装目录', '/usr/local', '1', 1, 'admin', NULL, NULL, '2019-11-19 08:40:55', NULL);
INSERT INTO `t_machine_file` VALUES (23, 4, '根目录', '/', '1', 1, 'admin', NULL, NULL, '2019-12-11 07:54:04', NULL);
INSERT INTO `t_machine_file` VALUES (24, 4, '项目目录', '/usr/local/java', '1', 1, 'admin', NULL, NULL, '2020-03-07 07:12:20', NULL);
INSERT INTO `t_machine_file` VALUES (25, 8, '软件安装位置', '/usr/local', '1', 1, 'admin', NULL, NULL, '2020-08-28 09:41:56', NULL);
INSERT INTO `t_machine_file` VALUES (26, 8, '54', '/root', '1', NULL, NULL, NULL, NULL, '2021-06-23 11:21:15', NULL);
INSERT INTO `t_machine_file` VALUES (27, 8, '54', '/root', '1', NULL, NULL, NULL, NULL, '2021-06-23 11:21:15', NULL);
COMMIT;

-- ----------------------------
-- Table structure for t_machine_monitor
-- ----------------------------
DROP TABLE IF EXISTS `t_machine_monitor`;
CREATE TABLE `t_machine_monitor` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `machine_id` bigint(20) NOT NULL,
  `mem_rate` float(255,2) DEFAULT NULL,
  `cpu_rate` float(255,2) DEFAULT NULL,
  `one_min_loadavg` float(255,2) DEFAULT NULL,
  `five_min_loadavg` float(255,2) DEFAULT NULL,
  `fif_min_loadavg` float(255,2) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11489 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of t_machine_monitor
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_machine_script
-- ----------------------------
DROP TABLE IF EXISTS `t_machine_script`;
CREATE TABLE `t_machine_script` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `machine_id` int(11) NOT NULL COMMENT '机器id',
  `desc` varchar(45) DEFAULT NULL COMMENT '服务描述',
  `script` varchar(128) NOT NULL COMMENT '服务脚本',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_machine_script
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_redis
-- ----------------------------
DROP TABLE IF EXISTS `t_redis`;
CREATE TABLE `t_redis` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `host` varchar(45) NOT NULL,
  `port` int(11) NOT NULL,
  `pwd` varchar(45) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  `cluster_id` bigint(20) DEFAULT NULL,
  `creator_id` bigint(20) unsigned DEFAULT NULL,
  `creator` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `modifier_id` bigint(20) unsigned DEFAULT NULL,
  `modifier` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_redis
-- ----------------------------
BEGIN;
INSERT INTO `t_redis` VALUES (1, '127.0.0.1', 6379, NULL, '测试使用jjjj', 0, NULL, NULL, 1, 'admin', '2020-02-27 09:55:08', '2020-07-02 14:07:53.092924');
COMMIT;

-- ----------------------------
-- Table structure for t_redis_cluster
-- ----------------------------
DROP TABLE IF EXISTS `t_redis_cluster`;
CREATE TABLE `t_redis_cluster` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_redis_cluster
-- ----------------------------
BEGIN;
INSERT INTO `t_redis_cluster` VALUES (1, '测试1', '2018-10-12 10:09:36', '2018-10-12 10:09:36');
INSERT INTO `t_redis_cluster` VALUES (2, '测试2', '2018-10-12 10:09:36', '2018-10-12 10:09:36');
COMMIT;

-- ----------------------------
-- Table structure for t_resource
-- ----------------------------
DROP TABLE IF EXISTS `t_resource`;
CREATE TABLE `t_resource` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `pid` int(11) NOT NULL,
  `type` tinyint(255) NOT NULL COMMENT '1：菜单路由；2：资源（按钮等）',
  `status` int(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '主要用于按钮等资源',
  `weight` int(11) DEFAULT NULL,
  `meta` varchar(255) DEFAULT NULL COMMENT '原数据',
  `creator_id` bigint(20) NOT NULL,
  `creator` varchar(255) NOT NULL,
  `modifier_id` bigint(20) NOT NULL,
  `modifier` varchar(255) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of t_resource
-- ----------------------------
BEGIN;
INSERT INTO `t_resource` VALUES (1, 0, 1, 1, '首页', '/home', 1, '{\"component\":\"Home\",\"icon\":\"HomeFilled\",\"isAffix\":true,\"isKeepAlive\":true,\"routeName\":\"Home\"}', 1, 'admin', 1, 'admin', '2021-05-25 16:44:41', '2021-05-27 09:12:56');
INSERT INTO `t_resource` VALUES (2, 0, 1, 1, '运维', '/ops', 3, '{\"routeName\":\"Ops\",\"isKeepAlive\":true,\"redirect\":\"machine/list\",\"icon\":\"Monitor\"}', 1, 'admin', 1, 'admin', '2021-05-25 16:48:16', '2022-01-10 14:19:08');
INSERT INTO `t_resource` VALUES (3, 2, 1, 1, '机器列表', 'machines', 1, '{\"component\":\"MachineList\",\"icon\":\"Menu\",\"isKeepAlive\":true,\"routeName\":\"MachineList\"}', 2, 'admin', 1, 'admin', '2021-05-25 16:50:04', '2021-06-03 10:03:29');
INSERT INTO `t_resource` VALUES (4, 0, 1, 1, '系统管理', '/sys', 4, '{\"icon\":\"Setting\",\"isKeepAlive\":true,\"redirect\":\"/sys/resources\",\"routeName\":\"sys\"}', 1, 'admin', 1, 'admin', '2021-05-26 15:20:20', '2021-06-08 14:20:34');
INSERT INTO `t_resource` VALUES (5, 4, 1, 1, '资源管理', 'resources', 3, '{\"component\":\"ResourceList\",\"icon\":\"Menu\",\"isKeepAlive\":true,\"routeName\":\"ResourceList\"}', 1, 'admin', 1, 'admin', '2021-05-26 15:23:07', '2021-06-08 11:27:55');
INSERT INTO `t_resource` VALUES (9, 0, 1, 1, 'iframes', '/iframes', 5, '{\"routeName\":\"Iframe\",\"isKeepAlive\":true,\"isIframe\":true,\"link\":\"https://www.baidu.com\",\"component\":\"RouterParent\",\"icon\":\"el-icon-pear\"}', 1, 'admin', 1, 'admin', '2021-05-27 09:58:37', '2021-09-23 16:32:45');
INSERT INTO `t_resource` VALUES (11, 4, 1, 1, '角色管理', 'roles', 2, '{\"component\":\"RoleList\",\"icon\":\"Menu\",\"isKeepAlive\":true,\"routeName\":\"RoleList\"}', 1, 'admin', 1, 'admin', '2021-05-27 11:15:35', '2021-06-03 09:59:41');
INSERT INTO `t_resource` VALUES (12, 3, 2, 1, '机器终端按钮', 'machine:terminal', 4, '', 1, 'admin', 1, 'admin', '2021-05-28 14:06:02', '2021-05-31 17:47:59');
INSERT INTO `t_resource` VALUES (14, 4, 1, 1, '账号管理', 'accounts', 1, '{\"component\":\"AccountList\",\"icon\":\"Menu\",\"isKeepAlive\":true,\"routeName\":\"AccountList\"}', 1, 'admin', 1, 'admin', '2021-05-28 14:56:25', '2021-06-03 09:39:22');
INSERT INTO `t_resource` VALUES (15, 3, 2, 1, '文件管理按钮', 'machine:file', 5, NULL, 1, 'admin', 1, 'admin', '2021-05-31 17:44:37', '2021-05-31 17:48:07');
INSERT INTO `t_resource` VALUES (16, 3, 2, 1, '机器添加按钮', 'machine:add', 1, NULL, 1, 'admin', 1, 'admin', '2021-05-31 17:46:11', '2022-01-10 16:13:24');
INSERT INTO `t_resource` VALUES (17, 3, 2, 1, '机器编辑按钮', 'machine:update', 2, NULL, 1, 'admin', 1, 'admin', '2021-05-31 17:46:23', '2021-05-31 19:34:18');
INSERT INTO `t_resource` VALUES (18, 3, 2, 1, '机器删除按钮', 'machine:del', 3, NULL, 1, 'admin', 1, 'admin', '2021-05-31 17:46:36', '2021-12-31 14:23:02');
INSERT INTO `t_resource` VALUES (19, 14, 2, 1, '角色分配按钮', 'account:saveRoles', 1, NULL, 1, 'admin', 1, 'admin', '2021-05-31 17:50:51', '2022-01-07 11:23:22');
INSERT INTO `t_resource` VALUES (20, 11, 2, 1, '分配菜单&权限按钮', 'role:saveResources', 1, NULL, 1, 'admin', 1, 'admin', '2021-05-31 17:51:41', '2021-05-31 19:33:37');
INSERT INTO `t_resource` VALUES (21, 14, 2, 1, '账号删除按钮', 'account:del', 2, 'null', 1, 'admin', 1, 'admin', '2021-05-31 18:02:01', '2021-06-10 17:12:17');
INSERT INTO `t_resource` VALUES (22, 11, 2, 1, '角色删除按钮', 'role:del', 2, NULL, 1, 'admin', 1, 'admin', '2021-05-31 18:02:29', '2021-05-31 19:33:38');
INSERT INTO `t_resource` VALUES (23, 11, 2, 1, '角色新增按钮', 'role:add', 3, NULL, 1, 'admin', 1, 'admin', '2021-05-31 18:02:44', '2021-05-31 19:33:39');
INSERT INTO `t_resource` VALUES (24, 11, 2, 1, '角色编辑按钮', 'role:update', 4, NULL, 1, 'admin', 1, 'admin', '2021-05-31 18:02:57', '2021-05-31 19:33:40');
INSERT INTO `t_resource` VALUES (25, 5, 2, 1, '资源新增按钮', 'resource:add', 1, NULL, 1, 'admin', 1, 'admin', '2021-05-31 18:03:33', '2021-05-31 19:31:47');
INSERT INTO `t_resource` VALUES (26, 5, 2, 1, '资源删除按钮', 'resource:del', 2, NULL, 1, 'admin', 1, 'admin', '2021-05-31 18:03:47', '2021-05-31 19:29:40');
INSERT INTO `t_resource` VALUES (27, 5, 2, 1, '资源编辑按钮', 'resource:update', 3, NULL, 1, 'admin', 1, 'admin', '2021-05-31 18:04:03', '2021-05-31 19:29:40');
INSERT INTO `t_resource` VALUES (28, 5, 2, 1, '资源禁用启用按钮', 'resource:changeStatus', 4, NULL, 1, 'admin', 1, 'admin', '2021-05-31 18:04:33', '2021-05-31 18:04:33');
INSERT INTO `t_resource` VALUES (29, 14, 2, 1, '账号添加按钮', 'account:add', 3, NULL, 1, 'admin', 1, 'admin', '2021-05-31 19:23:42', '2021-05-31 19:23:42');
INSERT INTO `t_resource` VALUES (30, 14, 2, 1, '账号编辑修改按钮', 'account:update', 4, NULL, 1, 'admin', 1, 'admin', '2021-05-31 19:23:58', '2021-05-31 19:23:58');
INSERT INTO `t_resource` VALUES (31, 14, 2, 1, '账号管理基本权限', 'account', 0, NULL, 1, 'admin', 1, 'admin', '2021-05-31 21:25:06', '2022-01-10 16:15:20');
INSERT INTO `t_resource` VALUES (32, 5, 2, 1, '资源管理基本权限', 'resource', 0, NULL, 1, 'admin', 1, 'admin', '2021-05-31 21:25:25', '2021-05-31 21:25:25');
INSERT INTO `t_resource` VALUES (33, 11, 2, 1, '角色管理基本权限', 'role', 0, NULL, 1, 'admin', 1, 'admin', '2021-05-31 21:25:40', '2021-05-31 21:25:40');
INSERT INTO `t_resource` VALUES (34, 14, 2, 1, '账号启用禁用按钮', 'account:changeStatus', 5, NULL, 1, 'admin', 1, 'admin', '2021-05-31 21:29:48', '2021-05-31 21:29:48');
INSERT INTO `t_resource` VALUES (36, 2, 1, 1, 'DBMS', 'dbms', 2, '{\"routeName\":\"DBMS\",\"isKeepAlive\":true,\"icon\":\"Grid\"}', 1, 'admin', 1, 'admin', '2021-06-01 14:01:33', '2022-01-10 14:42:28');
INSERT INTO `t_resource` VALUES (37, 3, 2, 1, '添加文件配置', 'machine:addFile', 6, 'null', 1, 'admin', 1, 'admin', '2021-06-01 19:54:23', '2021-06-01 19:54:23');
INSERT INTO `t_resource` VALUES (38, 36, 1, 1, '数据查询', 'select-data', 1, '{\"component\":\"SelectData\",\"icon\":\"Search\",\"isKeepAlive\":true,\"routeName\":\"SelectData\"}', 1, 'admin', 1, 'admin', '2021-06-03 09:09:29', '2021-06-03 14:34:01');
INSERT INTO `t_resource` VALUES (39, 0, 1, 1, '个人中心', '/personal', 2, '{\"component\":\"Personal\",\"icon\":\"User\",\"isKeepAlive\":true,\"routeName\":\"Personal\"}', 1, 'admin', 1, 'admin', '2021-06-03 14:25:35', '2021-06-11 10:50:45');
INSERT INTO `t_resource` VALUES (40, 3, 2, 1, '文件管理-新增按钮', 'machine:file:add', 7, 'null', 1, 'admin', 1, 'admin', '2021-06-08 11:06:26', '2021-06-08 11:12:28');
INSERT INTO `t_resource` VALUES (41, 3, 2, 1, '文件管理-删除按钮', 'machine:file:del', 8, 'null', 1, 'admin', 1, 'admin', '2021-06-08 11:06:49', '2021-06-08 11:06:49');
INSERT INTO `t_resource` VALUES (42, 3, 2, 1, '文件管理-写入or下载文件权限', 'machine:file:write', 9, 'null', 1, 'admin', 1, 'admin', '2021-06-08 11:07:27', '2021-06-08 11:07:27');
INSERT INTO `t_resource` VALUES (43, 3, 2, 1, '文件管理-文件上传按钮', 'machine:file:upload', 10, 'null', 1, 'admin', 1, 'admin', '2021-06-08 11:07:42', '2021-06-08 11:07:42');
INSERT INTO `t_resource` VALUES (44, 3, 2, 1, '文件管理-删除文件按钮', 'machine:file:rm', 11, 'null', 1, 'admin', 1, 'admin', '2021-06-08 11:08:12', '2021-06-08 11:08:12');
INSERT INTO `t_resource` VALUES (45, 3, 2, 1, '脚本管理-保存脚本按钮', 'machine:script:save', 12, 'null', 1, 'admin', 1, 'admin', '2021-06-08 11:09:01', '2021-06-08 11:09:01');
INSERT INTO `t_resource` VALUES (46, 3, 2, 1, '脚本管理-删除按钮', 'machine:script:del', 13, 'null', 1, 'admin', 1, 'admin', '2021-06-08 11:09:27', '2021-06-08 11:09:27');
INSERT INTO `t_resource` VALUES (47, 3, 2, 1, '脚本管理-执行按钮', 'machine:script:run', 14, 'null', 1, 'admin', 1, 'admin', '2021-06-08 11:09:50', '2021-06-08 11:09:50');
INSERT INTO `t_resource` VALUES (48, 4, 1, 1, '操作日志', 'logs', 4, '{\"routeName\":\"LogList\",\"isKeepAlive\":true,\"component\":\"LogList\",\"icon\":\"Tickets\"}', 1, 'admin', 1, 'admin', '2021-09-29 15:44:04', '2021-09-29 15:46:38');
INSERT INTO `t_resource` VALUES (49, 48, 2, 1, '日志查看', 'log:list', 1, NULL, 1, 'admin', 1, 'admin', '2021-09-29 15:44:39', '2021-09-29 15:47:55');
COMMIT;

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `status` tinyint(2) NOT NULL,
  `remark` varchar(45) DEFAULT NULL,
  `creator_id` bigint(20) unsigned DEFAULT NULL,
  `creator` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `modifier_id` bigint(20) unsigned NOT NULL,
  `modifier` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role
-- ----------------------------
BEGIN;
INSERT INTO `t_role` VALUES (1, '超级管理员', 1, '具有全站所有权限', 1, 'admin', 1, 'admin', '2018-12-17 15:14:10', '2020-07-01 15:23:08');
INSERT INTO `t_role` VALUES (2, '游客', 1, '一些重要操作没有权限，如重要资源的增删改', 1, 'admin', 1, 'admin', '2019-08-19 15:11:09', '2020-07-01 15:20:07');
INSERT INTO `t_role` VALUES (5, '运维管理员', 1, '拥有运维相关权限', 1, 'admin', 1, 'admin', '2020-03-30 00:56:28', '2020-07-01 15:34:21');
INSERT INTO `t_role` VALUES (6, '系统管理员', 1, '拥有系统管理等权限...11', 1, 'admin', 1, 'admin', '2020-03-30 00:55:18', '2021-06-01 09:36:20');
COMMIT;

-- ----------------------------
-- Table structure for t_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `t_role_resource`;
CREATE TABLE `t_role_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL,
  `resource_id` bigint(20) NOT NULL,
  `creator_id` bigint(20) unsigned DEFAULT NULL,
  `creator` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=423 DEFAULT CHARSET=utf8 COMMENT='角色资源表';

-- ----------------------------
-- Records of t_role_resource
-- ----------------------------
BEGIN;
INSERT INTO `t_role_resource` VALUES (1, 1, 1, 1, 'admin', '2021-05-27 15:07:39');
INSERT INTO `t_role_resource` VALUES (323, 1, 2, 1, 'admin', '2021-05-28 09:04:50');
INSERT INTO `t_role_resource` VALUES (326, 1, 4, 1, 'admin', '2021-05-28 09:04:50');
INSERT INTO `t_role_resource` VALUES (327, 1, 5, 1, 'admin', '2021-05-28 09:04:50');
INSERT INTO `t_role_resource` VALUES (328, 1, 11, 1, 'admin', '2021-05-28 09:04:50');
INSERT INTO `t_role_resource` VALUES (335, 1, 14, 1, 'admin', '2021-05-28 17:42:21');
INSERT INTO `t_role_resource` VALUES (336, 1, 3, 1, 'admin', '2021-05-28 17:42:43');
INSERT INTO `t_role_resource` VALUES (337, 1, 12, 1, 'admin', '2021-05-28 17:42:43');
INSERT INTO `t_role_resource` VALUES (338, 6, 2, 1, 'admin', '2021-05-28 19:19:38');
INSERT INTO `t_role_resource` VALUES (339, 6, 3, 1, 'admin', '2021-05-28 19:19:38');
INSERT INTO `t_role_resource` VALUES (342, 6, 1, 1, 'admin', '2021-05-29 01:31:22');
INSERT INTO `t_role_resource` VALUES (343, 5, 1, 1, 'admin', '2021-05-31 14:05:23');
INSERT INTO `t_role_resource` VALUES (344, 5, 4, 1, 'admin', '2021-05-31 14:05:23');
INSERT INTO `t_role_resource` VALUES (345, 5, 14, 1, 'admin', '2021-05-31 14:05:23');
INSERT INTO `t_role_resource` VALUES (346, 5, 5, 1, 'admin', '2021-05-31 14:05:23');
INSERT INTO `t_role_resource` VALUES (347, 5, 11, 1, 'admin', '2021-05-31 14:05:23');
INSERT INTO `t_role_resource` VALUES (348, 5, 3, 1, 'admin', '2021-05-31 16:33:14');
INSERT INTO `t_role_resource` VALUES (349, 5, 12, 1, 'admin', '2021-05-31 16:33:14');
INSERT INTO `t_role_resource` VALUES (350, 5, 2, 1, 'admin', '2021-05-31 16:33:14');
INSERT INTO `t_role_resource` VALUES (353, 1, 15, 1, 'admin', '2021-05-31 17:48:33');
INSERT INTO `t_role_resource` VALUES (354, 1, 16, 1, 'admin', '2021-05-31 17:48:33');
INSERT INTO `t_role_resource` VALUES (355, 1, 17, 1, 'admin', '2021-05-31 17:48:33');
INSERT INTO `t_role_resource` VALUES (356, 1, 18, 1, 'admin', '2021-05-31 17:48:33');
INSERT INTO `t_role_resource` VALUES (357, 1, 19, 1, 'admin', '2021-05-31 17:52:08');
INSERT INTO `t_role_resource` VALUES (358, 1, 20, 1, 'admin', '2021-05-31 17:52:08');
INSERT INTO `t_role_resource` VALUES (359, 1, 21, 1, 'admin', '2021-05-31 18:05:04');
INSERT INTO `t_role_resource` VALUES (360, 1, 22, 1, 'admin', '2021-05-31 18:05:04');
INSERT INTO `t_role_resource` VALUES (361, 1, 23, 1, 'admin', '2021-05-31 18:05:04');
INSERT INTO `t_role_resource` VALUES (362, 1, 24, 1, 'admin', '2021-05-31 18:05:04');
INSERT INTO `t_role_resource` VALUES (363, 1, 25, 1, 'admin', '2021-05-31 18:05:04');
INSERT INTO `t_role_resource` VALUES (364, 1, 26, 1, 'admin', '2021-05-31 18:05:04');
INSERT INTO `t_role_resource` VALUES (365, 1, 27, 1, 'admin', '2021-05-31 18:05:04');
INSERT INTO `t_role_resource` VALUES (366, 1, 28, 1, 'admin', '2021-05-31 18:05:04');
INSERT INTO `t_role_resource` VALUES (367, 1, 29, 1, 'admin', '2021-05-31 19:24:15');
INSERT INTO `t_role_resource` VALUES (368, 1, 30, 1, 'admin', '2021-05-31 19:24:15');
INSERT INTO `t_role_resource` VALUES (369, 1, 31, 1, 'admin', '2021-05-31 21:25:56');
INSERT INTO `t_role_resource` VALUES (370, 1, 32, 1, 'admin', '2021-05-31 21:25:56');
INSERT INTO `t_role_resource` VALUES (371, 1, 33, 1, 'admin', '2021-05-31 21:25:56');
INSERT INTO `t_role_resource` VALUES (372, 1, 34, 1, 'admin', '2021-05-31 21:30:06');
INSERT INTO `t_role_resource` VALUES (374, 1, 36, 1, 'admin', '2021-06-01 14:01:57');
INSERT INTO `t_role_resource` VALUES (375, 1, 37, 1, 'admin', '2021-06-03 11:27:10');
INSERT INTO `t_role_resource` VALUES (376, 1, 38, 1, 'admin', '2021-06-11 15:24:30');
INSERT INTO `t_role_resource` VALUES (377, 1, 39, 1, 'admin', '2021-06-11 15:24:30');
INSERT INTO `t_role_resource` VALUES (378, 1, 40, 1, 'admin', '2021-06-11 15:24:30');
INSERT INTO `t_role_resource` VALUES (379, 1, 41, 1, 'admin', '2021-06-11 15:24:30');
INSERT INTO `t_role_resource` VALUES (380, 1, 42, 1, 'admin', '2021-06-11 15:24:30');
INSERT INTO `t_role_resource` VALUES (381, 1, 43, 1, 'admin', '2021-06-11 15:24:30');
INSERT INTO `t_role_resource` VALUES (382, 1, 44, 1, 'admin', '2021-06-11 15:24:30');
INSERT INTO `t_role_resource` VALUES (383, 1, 45, 1, 'admin', '2021-06-11 15:24:30');
INSERT INTO `t_role_resource` VALUES (384, 1, 46, 1, 'admin', '2021-06-11 15:24:30');
INSERT INTO `t_role_resource` VALUES (385, 1, 47, 1, 'admin', '2021-06-11 15:24:30');
INSERT INTO `t_role_resource` VALUES (386, 6, 39, 1, 'admin', '2021-06-11 15:25:36');
INSERT INTO `t_role_resource` VALUES (387, 6, 15, 1, 'admin', '2021-06-11 15:25:36');
INSERT INTO `t_role_resource` VALUES (388, 6, 32, 1, 'admin', '2021-06-11 15:25:55');
INSERT INTO `t_role_resource` VALUES (389, 6, 33, 1, 'admin', '2021-06-11 15:25:55');
INSERT INTO `t_role_resource` VALUES (390, 6, 4, 1, 'admin', '2021-06-11 15:25:55');
INSERT INTO `t_role_resource` VALUES (391, 6, 5, 1, 'admin', '2021-06-11 15:25:55');
INSERT INTO `t_role_resource` VALUES (392, 6, 11, 1, 'admin', '2021-06-11 15:25:55');
INSERT INTO `t_role_resource` VALUES (393, 6, 14, 1, 'admin', '2021-06-11 15:25:55');
INSERT INTO `t_role_resource` VALUES (394, 6, 31, 1, 'admin', '2021-06-11 15:25:55');
INSERT INTO `t_role_resource` VALUES (395, 6, 12, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (396, 6, 16, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (397, 6, 17, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (398, 6, 18, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (399, 6, 19, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (400, 6, 20, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (401, 6, 21, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (402, 6, 22, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (403, 6, 23, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (404, 6, 24, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (405, 6, 25, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (406, 6, 26, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (407, 6, 27, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (408, 6, 28, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (409, 6, 29, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (410, 6, 30, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (411, 6, 34, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (412, 6, 36, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (413, 6, 37, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (414, 6, 38, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (415, 6, 40, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (416, 6, 41, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (417, 6, 42, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (418, 6, 43, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (419, 6, 44, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (420, 6, 45, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (421, 6, 46, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (422, 6, 47, 1, 'admin', '2021-06-11 15:27:13');
COMMIT;

-- ----------------------------
-- Table structure for t_sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_operation_log`;
CREATE TABLE `t_sys_operation_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `type` tinyint(2) DEFAULT NULL COMMENT '1：正常操作；2：异常信息',
  `operation` varchar(1024) NOT NULL,
  `creator_id` bigint(20) unsigned DEFAULT NULL,
  `creator` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=477 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of t_sys_operation_log
-- ----------------------------
SET FOREIGN_KEY_CHECKS = 1;
