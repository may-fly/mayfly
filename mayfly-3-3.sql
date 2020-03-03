-- MySQL dump 10.13  Distrib 8.0.17, for macos10.14 (x86_64)
--
-- Host: 127.0.0.1    Database: mayfly
-- ------------------------------------------------------
-- Server version	8.0.18

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tb_account`
--

DROP TABLE IF EXISTS `tb_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_account` (
  `id` int(11) unsigned NOT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) DEFAULT NULL,
  `status` tinyint(2) DEFAULT NULL,
  `create_account_id` int(11) NOT NULL,
  `create_account` varchar(45) DEFAULT NULL,
  `update_account_id` int(11) NOT NULL,
  `update_account` varchar(45) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_account`
--

LOCK TABLES `tb_account` WRITE;
/*!40000 ALTER TABLE `tb_account` DISABLE KEYS */;
INSERT INTO `tb_account` VALUES (1,'admin','e10adc3949ba59abbe56e057f20f883e',1,0,NULL,0,NULL,NULL,NULL),(9,'test','e10adc3949ba59abbe56e057f20f883e',1,0,NULL,0,NULL,'2019-08-21 11:30:33','2019-08-21 11:30:33'),(10,'test2','ad0234829205b9033196ba818f7a872b',0,0,NULL,0,NULL,'2019-12-12 09:22:37','2020-02-21 11:57:16'),(11,'meilin.huang','e10adc3949ba59abbe56e057f20f883e',1,0,NULL,0,NULL,'2019-12-12 09:31:36','2019-12-12 09:31:36');
/*!40000 ALTER TABLE `tb_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_account_role`
--

DROP TABLE IF EXISTS `tb_account_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_account_role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `account_id` int(11) NOT NULL COMMENT '账号id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `create_account` varchar(45) DEFAULT NULL,
  `create_account_id` int(10) unsigned DEFAULT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_account_role`
--

LOCK TABLES `tb_account_role` WRITE;
/*!40000 ALTER TABLE `tb_account_role` DISABLE KEYS */;
INSERT INTO `tb_account_role` VALUES (3,1,1,NULL,NULL,'2019-08-20 15:53:32'),(4,9,2,NULL,NULL,'2019-08-21 11:32:01'),(7,10,2,NULL,NULL,'2019-12-12 09:23:11'),(8,11,2,NULL,NULL,'2019-12-15 07:55:22');
/*!40000 ALTER TABLE `tb_account_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_machine`
--

DROP TABLE IF EXISTS `tb_machine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_machine` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `ip` varchar(45) NOT NULL,
  `port` int(11) NOT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `create_account_id` int(10) unsigned DEFAULT NULL,
  `create_account` varchar(45) DEFAULT NULL,
  `update_account_id` int(10) unsigned DEFAULT NULL,
  `update_account` varchar(45) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_machine`
--

LOCK TABLES `tb_machine` WRITE;
/*!40000 ALTER TABLE `tb_machine` DISABLE KEYS */;
INSERT INTO `tb_machine` VALUES (1,'腾讯云','118.24.26.101',22,'root','',NULL,NULL,NULL,NULL,'2019-11-04 14:23:04','2019-11-04 14:23:04'),(2,'test','94.191.96.31',22,'root','',NULL,NULL,NULL,NULL,'2019-11-05 20:05:22','2019-11-05 20:05:22'),(3,'ucar','10.130.20.148',22,'root','123456',NULL,NULL,NULL,NULL,'2019-11-18 05:16:48','2019-11-18 05:16:48'),(4,'京东云','114.67.67.10',22,'root','',NULL,NULL,NULL,NULL,'2019-12-11 07:52:51','2019-12-11 07:52:51');
/*!40000 ALTER TABLE `tb_machine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_machine_file`
--

DROP TABLE IF EXISTS `tb_machine_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_machine_file` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '机器文件配置（linux一切皆文件，故也可以表示目录）',
  `machine_id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `path` varchar(45) NOT NULL,
  `type` varchar(45) NOT NULL COMMENT '1：目录；2：文件',
  `create_account_id` int(10) unsigned DEFAULT NULL,
  `create_account` varchar(45) DEFAULT NULL,
  `update_account_id` int(10) unsigned DEFAULT NULL,
  `update_account` varchar(45) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_machine_file`
--

LOCK TABLES `tb_machine_file` WRITE;
/*!40000 ALTER TABLE `tb_machine_file` DISABLE KEYS */;
INSERT INTO `tb_machine_file` VALUES (1,1,'redis配置文件','/etc/my.cnf','2',NULL,NULL,NULL,NULL,'2019-08-21 11:30:33',NULL),(2,1,'测试文件','/usr/local/test/file','2',NULL,NULL,NULL,NULL,'2019-08-21 11:30:33',NULL),(7,1,'test','/usr/local/test/file1','2',NULL,NULL,NULL,NULL,'2019-11-05 16:12:58',NULL),(8,2,'redis','/root/restart-redis.sh','2',NULL,NULL,NULL,NULL,'2019-11-05 20:08:34',NULL),(10,2,'java-dockerfile','/usr/local/java/Dockerfile','2',NULL,NULL,NULL,NULL,'2019-11-06 15:16:12',NULL),(11,1,'usr','/usr','1',NULL,NULL,NULL,NULL,'2019-11-06 15:16:12',NULL),(12,1,'opt','/opt','1',NULL,NULL,NULL,NULL,'2019-11-06 15:16:12',NULL),(13,3,'根目录','/','1',NULL,NULL,NULL,NULL,'2019-11-18 05:19:28',NULL),(14,2,'usr','/usr/local','1',NULL,NULL,NULL,NULL,'2019-11-19 05:52:28',NULL),(15,3,'usr','/usr/','1',NULL,NULL,NULL,NULL,'2019-11-19 08:35:19',NULL),(16,3,'软件安装目录','/usr/local','1',NULL,NULL,NULL,NULL,'2019-11-19 08:40:55',NULL),(23,4,'根目录','/','1',NULL,NULL,NULL,NULL,'2019-12-11 07:54:04',NULL);
/*!40000 ALTER TABLE `tb_machine_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_machine_script`
--

DROP TABLE IF EXISTS `tb_machine_script`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_machine_script` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `machine_id` int(11) NOT NULL COMMENT '机器id',
  `desc` varchar(45) DEFAULT NULL COMMENT '服务描述',
  `script` varchar(128) NOT NULL COMMENT '服务脚本',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_machine_script`
--

LOCK TABLES `tb_machine_script` WRITE;
/*!40000 ALTER TABLE `tb_machine_script` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_machine_script` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_redis`
--

DROP TABLE IF EXISTS `tb_redis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_redis` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `host` varchar(45) NOT NULL,
  `port` int(11) NOT NULL,
  `pwd` varchar(45) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  `cluster_id` int(11) DEFAULT NULL,
  `create_account_id` int(10) unsigned DEFAULT NULL,
  `create_account` varchar(45) DEFAULT NULL,
  `update_account_id` int(10) unsigned DEFAULT NULL,
  `update_account` varchar(45) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_redis`
--

LOCK TABLES `tb_redis` WRITE;
/*!40000 ALTER TABLE `tb_redis` DISABLE KEYS */;
INSERT INTO `tb_redis` VALUES (1,'127.0.0.1',6379,NULL,'测试使用jjjj',0,NULL,NULL,NULL,NULL,'2020-02-27 09:55:08','2020-02-27 09:55:08.237432'),(15,'mayfly.1yue.net',6379,'','京东云Redis',0,NULL,NULL,NULL,NULL,'2020-02-28 01:16:26','2020-02-28 01:16:26.255366');
/*!40000 ALTER TABLE `tb_redis` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_redis_cluster`
--

DROP TABLE IF EXISTS `tb_redis_cluster`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_redis_cluster` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_redis_cluster`
--

LOCK TABLES `tb_redis_cluster` WRITE;
/*!40000 ALTER TABLE `tb_redis_cluster` DISABLE KEYS */;
INSERT INTO `tb_redis_cluster` VALUES (1,'测试1','2018-10-12 10:09:36','2018-10-12 10:09:36'),(2,'测试2','2018-10-12 10:09:36','2018-10-12 10:09:36');
/*!40000 ALTER TABLE `tb_redis_cluster` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_resource`
--

DROP TABLE IF EXISTS `tb_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) NOT NULL,
  `type` tinyint(2) NOT NULL DEFAULT '1' COMMENT '1：菜单；2：资源（按钮等）',
  `code` varchar(45) DEFAULT NULL COMMENT '标识符（资源为菜单时表示path，其他为自定义标识符）',
  `name` varchar(60) NOT NULL,
  `icon` varchar(45) DEFAULT NULL,
  `weight` int(11) NOT NULL,
  `status` tinyint(2) NOT NULL,
  `create_account_id` int(10) unsigned DEFAULT NULL,
  `create_account` varchar(45) DEFAULT NULL,
  `update_account_id` int(10) unsigned DEFAULT NULL,
  `update_account` varchar(45) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=113 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_resource`
--

LOCK TABLES `tb_resource` WRITE;
/*!40000 ALTER TABLE `tb_resource` DISABLE KEYS */;
INSERT INTO `tb_resource` VALUES (1,0,1,'','系统管理','el-icon-menu',2,1,NULL,NULL,1,'admin','2018-10-12 10:09:36','2020-03-03 05:40:31'),(8,0,1,'/','首页','fa fa-tachometer',1,1,NULL,NULL,NULL,NULL,'2018-10-12 10:09:36','2019-11-06 11:19:00'),(9,1,1,'/menu_manage','菜单&权限',NULL,1,1,NULL,NULL,NULL,NULL,'2018-10-12 10:09:36','2020-03-03 03:14:45'),(16,0,1,'','运维','el-icon-menu',3,1,NULL,NULL,NULL,NULL,'2019-01-02 16:18:00','2019-12-23 05:14:46'),(19,16,1,'/redis_manage','redis节点',NULL,2,1,NULL,NULL,NULL,NULL,'2019-01-04 11:29:11','2019-11-15 01:27:53'),(21,16,1,'http://localhost:8016/#/role_manage','redis集群',NULL,3,1,NULL,NULL,NULL,NULL,'2019-01-21 15:44:07','2019-12-16 03:35:04'),(27,1,1,'/role_manage','角色管理',NULL,2,1,NULL,NULL,NULL,NULL,'2019-07-02 17:56:24','2019-07-26 15:24:41'),(28,1,1,'/account_list','账号管理',NULL,3,1,NULL,NULL,NULL,NULL,'2019-07-02 17:57:15','2019-11-06 11:20:59'),(33,9,2,'resource:save','新增按钮',NULL,4,1,NULL,NULL,NULL,NULL,'2019-07-27 10:22:16','2019-10-30 13:55:49'),(34,9,2,'resource:update','编辑按钮',NULL,3,1,NULL,NULL,NULL,NULL,'2019-07-27 10:22:50','2020-03-03 03:15:57'),(35,9,2,'resource:delete','删除按钮',NULL,1,1,NULL,NULL,NULL,NULL,'2019-07-27 10:27:04','2020-03-01 07:23:49'),(36,27,2,'role:saveResources','分配菜单&权限按钮',NULL,3,1,NULL,NULL,1,'admin','2019-07-27 10:27:48','2020-03-03 05:42:20'),(37,9,2,'resource:list','菜单列表权限',NULL,4,1,NULL,NULL,NULL,NULL,'2019-07-27 10:37:03','2019-12-16 05:26:34'),(38,27,2,'role:list','角色列表权限',NULL,6,1,NULL,NULL,NULL,NULL,'2019-07-27 10:49:33','2019-08-24 15:00:02'),(39,27,2,'role:roleResources','获取角色菜单&权限列表',NULL,5,1,NULL,NULL,NULL,NULL,'2019-07-27 11:21:45','2019-08-24 15:00:04'),(40,9,2,'resource:changeStatus','启用禁用按钮',NULL,2,1,NULL,NULL,NULL,NULL,'2019-07-27 15:08:54','2019-12-23 02:21:54'),(44,28,2,'account:list','账号列表权限',NULL,10,1,NULL,NULL,NULL,NULL,'2019-07-27 17:26:58','2019-12-12 06:17:12'),(52,27,2,'role:save','新增按钮',NULL,4,1,NULL,NULL,NULL,NULL,'2019-08-19 14:25:55','2019-08-24 15:10:39'),(53,27,2,'role:update','修改编辑按钮',NULL,2,1,NULL,NULL,NULL,NULL,'2019-08-19 19:49:48','2019-08-24 15:10:43'),(54,27,2,'role:delete','删除按钮',NULL,1,1,NULL,NULL,1,'admin','2019-08-19 19:53:57','2020-03-03 05:42:16'),(55,28,2,'account:roles','获取账号的角色权限',NULL,1,1,NULL,NULL,NULL,NULL,'2019-08-20 14:46:22','2019-12-15 07:42:36'),(56,28,2,'account:saveRoles','分配角色按钮',NULL,1,1,NULL,NULL,NULL,NULL,'2019-08-20 15:49:35','2019-12-12 06:16:59'),(57,28,2,'account:save','新增按钮',NULL,1,1,NULL,NULL,NULL,NULL,'2019-08-20 17:38:24','2019-12-12 06:16:52'),(58,28,2,'account:delete','删除按钮',NULL,1,1,NULL,NULL,NULL,NULL,'2019-08-21 10:58:14','2019-12-12 06:16:45'),(64,21,2,'redis:add','新增按钮',NULL,1,1,NULL,NULL,NULL,NULL,'2019-08-21 14:27:18','2019-08-21 14:27:18'),(67,28,2,'account:changeStatus','启用禁用按钮',NULL,1,1,NULL,NULL,NULL,NULL,'2019-08-21 15:38:51','2019-12-12 06:16:38'),(68,19,2,'redis:list','redis列表',NULL,9,1,NULL,NULL,NULL,NULL,'2019-09-08 14:42:30','2019-09-08 14:42:30'),(69,19,2,'redis:key:scan','redis键scan列表',NULL,8,1,NULL,NULL,NULL,NULL,'2019-09-08 14:43:09','2019-09-08 14:44:20'),(70,19,2,'redis:info','info按钮',NULL,7,1,NULL,NULL,NULL,NULL,'2019-09-08 14:44:11','2019-09-08 14:44:11'),(71,19,2,'redis:key:delete','key删除按钮',NULL,6,1,NULL,NULL,NULL,NULL,'2019-09-08 15:56:21','2019-09-08 15:56:42'),(72,19,2,'redis:key:value','查看value按钮',NULL,5,1,NULL,NULL,NULL,NULL,'2019-09-08 16:15:28','2019-09-08 16:15:49'),(73,19,2,'redis:key:add','新增key-value按钮',NULL,4,1,NULL,NULL,NULL,NULL,'2019-09-08 16:16:10','2019-09-08 16:16:10'),(74,19,2,'redis:key:update','修改value按钮',NULL,4,1,NULL,NULL,NULL,NULL,'2019-09-08 16:21:23','2019-09-08 16:21:23'),(84,16,1,'/machines','机器','',1,1,NULL,NULL,NULL,NULL,'2019-11-04 14:39:49','2020-01-17 13:23:20'),(85,84,2,'machine:save','添加按钮',NULL,2,1,NULL,NULL,NULL,NULL,'2019-11-06 11:08:57','2020-01-20 03:05:36'),(86,84,2,'machine:serviceManage','服务管理按钮',NULL,3,1,NULL,NULL,NULL,NULL,'2019-11-06 11:09:21','2019-12-15 02:33:59'),(87,84,2,'machine:delete','删除按钮',NULL,2,1,NULL,NULL,NULL,NULL,'2019-11-06 11:09:44','2019-12-15 02:33:57'),(88,84,2,'machineFile:addConf','添加配置文件按钮',NULL,4,1,NULL,NULL,NULL,NULL,'2019-11-06 11:17:16','2019-12-23 06:22:48'),(89,84,2,'machineFile:updateFileContent','修改文件内容权限（确定按钮）',NULL,5,1,NULL,NULL,NULL,NULL,'2019-11-06 11:22:31','2020-01-21 01:21:26'),(90,84,2,'machineFile:delConf','删除配置文件按钮',NULL,6,1,NULL,NULL,NULL,NULL,'2019-11-06 11:23:16','2019-12-23 06:23:23'),(93,84,2,'machineFile:ls','获取目录树',NULL,7,1,NULL,NULL,NULL,NULL,'2019-12-23 05:56:49','2019-12-23 06:20:55'),(94,84,2,'machineFile:rm','删除文件',NULL,8,1,NULL,NULL,NULL,NULL,'2019-12-23 05:57:12','2019-12-23 06:21:03'),(95,84,2,'machineFile:cat','查看文件内容',NULL,9,1,NULL,NULL,NULL,NULL,'2019-12-23 05:57:44','2019-12-23 06:21:09'),(96,84,2,'machineFile:upload','上传文件',NULL,10,1,NULL,NULL,NULL,NULL,'2019-12-23 05:58:19','2019-12-23 06:21:21'),(101,9,2,'resource:detail','查看资源详情按钮',NULL,1,1,NULL,NULL,NULL,NULL,'2019-12-25 02:18:42','2020-02-21 11:57:26'),(102,16,1,'http://118.24.26.101:8888','jenkins',NULL,4,1,NULL,NULL,NULL,NULL,'2020-01-04 12:38:36','2020-01-04 15:30:19'),(107,84,2,'machineFile:files','查看配置文件列表',NULL,1,1,NULL,NULL,NULL,NULL,'2020-01-20 03:05:28','2020-01-20 03:05:28'),(110,19,2,'redis:save','新增redis节点',NULL,1,1,NULL,NULL,NULL,NULL,'2020-01-21 02:47:07','2020-01-21 02:47:07'),(111,19,2,'redis:update','修改redis节点',NULL,2,1,NULL,NULL,NULL,NULL,'2020-01-21 02:47:23','2020-01-21 02:47:23');
/*!40000 ALTER TABLE `tb_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_role`
--

DROP TABLE IF EXISTS `tb_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `status` tinyint(2) NOT NULL,
  `remark` varchar(45) DEFAULT NULL,
  `create_account_id` int(10) unsigned DEFAULT NULL,
  `create_account` varchar(45) DEFAULT NULL,
  `update_account_id` int(10) unsigned NOT NULL,
  `update_account` varchar(45) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_role`
--

LOCK TABLES `tb_role` WRITE;
/*!40000 ALTER TABLE `tb_role` DISABLE KEYS */;
INSERT INTO `tb_role` VALUES (1,'超级管理员',1,'具有全站所有权限',NULL,NULL,0,NULL,'2018-12-17 15:14:10','2018-12-17 15:14:10'),(2,'无名游客',1,'一些重要操作没有权限，如菜单等的增删改',NULL,NULL,0,NULL,'2019-08-19 15:11:09','2019-08-22 09:00:48');
/*!40000 ALTER TABLE `tb_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_role_resource`
--

DROP TABLE IF EXISTS `tb_role_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_role_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `resource_id` int(11) NOT NULL,
  `create_account_id` int(10) unsigned DEFAULT NULL,
  `create_account` varchar(45) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=254 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_role_resource`
--

LOCK TABLES `tb_role_resource` WRITE;
/*!40000 ALTER TABLE `tb_role_resource` DISABLE KEYS */;
INSERT INTO `tb_role_resource` VALUES (150,1,8,NULL,NULL,'2019-08-19 19:38:50'),(151,1,1,NULL,NULL,'2019-08-19 19:38:50'),(152,1,9,NULL,NULL,'2019-08-19 19:38:50'),(153,1,37,NULL,NULL,'2019-08-19 19:38:50'),(154,1,40,NULL,NULL,'2019-08-19 19:38:50'),(155,1,35,NULL,NULL,'2019-08-19 19:38:50'),(156,1,34,NULL,NULL,'2019-08-19 19:38:50'),(157,1,33,NULL,NULL,'2019-08-19 19:38:50'),(158,1,27,NULL,NULL,'2019-08-19 19:38:50'),(159,1,38,NULL,NULL,'2019-08-19 19:38:50'),(160,1,39,NULL,NULL,'2019-08-19 19:38:50'),(161,1,36,NULL,NULL,'2019-08-19 19:38:50'),(162,1,52,NULL,NULL,'2019-08-19 19:38:50'),(163,1,28,NULL,NULL,'2019-08-19 19:38:50'),(164,1,44,NULL,NULL,'2019-08-19 19:38:50'),(165,1,16,NULL,NULL,'2019-08-19 19:38:50'),(167,1,19,NULL,NULL,'2019-08-19 19:38:50'),(169,2,38,NULL,NULL,'2019-08-19 19:41:52'),(170,2,28,NULL,NULL,'2019-08-19 19:41:52'),(171,2,44,NULL,NULL,'2019-08-19 19:41:52'),(172,2,1,NULL,NULL,'2019-08-19 19:41:52'),(174,2,27,NULL,NULL,'2019-08-19 19:41:52'),(175,1,53,NULL,NULL,'2019-08-19 19:50:42'),(176,1,54,NULL,NULL,'2019-08-19 19:54:15'),(177,2,8,NULL,NULL,'2019-08-19 20:39:26'),(180,1,55,NULL,NULL,'2019-08-20 14:46:32'),(181,1,56,NULL,NULL,'2019-08-20 15:49:42'),(182,1,57,NULL,NULL,'2019-08-20 17:38:34'),(184,1,58,NULL,NULL,'2019-08-21 11:30:17'),(185,1,67,NULL,NULL,'2019-08-21 15:38:58'),(186,1,21,NULL,NULL,'2019-08-21 15:56:03'),(187,1,64,NULL,NULL,'2019-08-21 15:56:03'),(189,2,39,NULL,NULL,'2019-08-22 09:01:57'),(190,2,36,NULL,NULL,'2019-08-22 09:01:57'),(191,2,55,NULL,NULL,'2019-08-22 09:01:57'),(193,2,67,NULL,NULL,'2019-08-22 09:01:57'),(194,1,68,NULL,NULL,'2019-09-08 14:50:06'),(195,1,69,NULL,NULL,'2019-09-08 14:50:06'),(196,1,70,NULL,NULL,'2019-09-08 14:50:06'),(197,1,71,NULL,NULL,'2019-09-08 15:56:52'),(198,1,72,NULL,NULL,'2019-09-08 16:20:08'),(199,1,73,NULL,NULL,'2019-09-08 16:20:08'),(200,1,74,NULL,NULL,'2019-09-08 16:21:42'),(201,1,84,NULL,NULL,'2019-11-04 14:40:47'),(202,2,16,NULL,NULL,'2019-11-05 15:11:12'),(204,2,21,NULL,NULL,'2019-11-05 15:11:12'),(205,2,64,NULL,NULL,'2019-11-05 15:11:12'),(206,2,19,NULL,NULL,'2019-11-05 15:11:12'),(207,2,68,NULL,NULL,'2019-11-05 15:11:12'),(208,2,69,NULL,NULL,'2019-11-05 15:11:12'),(209,2,70,NULL,NULL,'2019-11-05 15:11:12'),(210,2,71,NULL,NULL,'2019-11-05 15:11:12'),(211,2,72,NULL,NULL,'2019-11-05 15:11:12'),(212,2,74,NULL,NULL,'2019-11-05 15:11:12'),(213,2,73,NULL,NULL,'2019-11-05 15:11:12'),(214,1,87,NULL,NULL,'2019-11-06 11:13:25'),(215,1,86,NULL,NULL,'2019-11-06 11:13:25'),(216,1,85,NULL,NULL,'2019-11-06 11:13:25'),(217,1,88,NULL,NULL,'2019-11-06 11:24:16'),(218,1,89,NULL,NULL,'2019-11-06 11:24:16'),(219,1,90,NULL,NULL,'2019-11-06 11:24:16'),(222,1,93,NULL,NULL,'2019-12-23 06:16:23'),(223,1,94,NULL,NULL,'2019-12-23 06:16:23'),(224,1,95,NULL,NULL,'2019-12-23 06:16:23'),(225,1,96,NULL,NULL,'2019-12-23 06:16:23'),(226,1,101,NULL,NULL,'2019-12-25 02:18:52'),(227,1,102,NULL,NULL,'2020-01-04 12:38:43'),(228,1,107,NULL,NULL,'2020-01-20 03:06:06'),(229,1,110,NULL,NULL,'2020-01-21 02:47:39'),(230,1,111,NULL,NULL,'2020-01-21 02:47:39'),(241,2,9,NULL,NULL,'2020-03-01 08:14:31'),(242,2,101,NULL,NULL,'2020-03-01 08:14:31'),(247,2,37,NULL,NULL,'2020-03-01 08:14:31');
/*!40000 ALTER TABLE `tb_role_resource` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-03-03 13:55:06
