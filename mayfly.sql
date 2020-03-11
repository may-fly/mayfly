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
INSERT INTO `tb_account` VALUES (1,'admin','e10adc3949ba59abbe56e057f20f883e',1,1,'admin',1,'admin',NULL,'2020-03-05 07:41:18'),(9,'test','e10adc3949ba59abbe56e057f20f883e',1,1,'admin',1,'admin','2019-08-21 11:30:33','2020-03-05 05:21:05'),(10,'test2','ad0234829205b9033196ba818f7a872b',0,1,'admin',1,'admin','2019-12-12 09:22:37','2020-03-05 05:21:58'),(11,'meilin.huang','e10adc3949ba59abbe56e057f20f883e',1,1,'admin',1,'admin','2019-12-12 09:31:36','2020-03-05 09:41:50');
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
INSERT INTO `tb_machine` VALUES (1,'腾讯云','118.24.26.101',22,'root','',1,'admin',NULL,NULL,'2019-11-04 14:23:04','2019-11-04 14:23:04'),(2,'test','94.191.96.31',22,'root','',1,'admin',NULL,NULL,'2019-11-05 20:05:22','2019-11-05 20:05:22'),(3,'ucar','10.130.20.148',22,'root','123456',1,'admin',NULL,NULL,'2019-11-18 05:16:48','2019-11-18 05:16:48'),(4,'京东云','114.67.67.10',22,'root','',1,'admin',NULL,NULL,'2019-12-11 07:52:51','2019-12-11 07:52:51');
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
INSERT INTO `tb_machine_file` VALUES (1,1,'redis配置文件','/etc/my.cnf','2',1,'admin',NULL,NULL,'2019-08-21 11:30:33',NULL),(2,1,'测试文件','/usr/local/test/file','2',1,'admin',NULL,NULL,'2019-08-21 11:30:33',NULL),(7,1,'test','/usr/local/test/file1','2',1,'admin',NULL,NULL,'2019-11-05 16:12:58',NULL),(8,2,'redis','/root/restart-redis.sh','2',1,'admin',NULL,NULL,'2019-11-05 20:08:34',NULL),(10,2,'java-dockerfile','/usr/local/java/Dockerfile','2',1,'admin',NULL,NULL,'2019-11-06 15:16:12',NULL),(11,1,'usr','/usr','1',1,'admin',NULL,NULL,'2019-11-06 15:16:12',NULL),(12,1,'opt','/opt','1',1,'admin',NULL,NULL,'2019-11-06 15:16:12',NULL),(13,3,'根目录','/','1',1,'admin',NULL,NULL,'2019-11-18 05:19:28',NULL),(14,2,'usr','/usr/local','1',1,'admin',NULL,NULL,'2019-11-19 05:52:28',NULL),(15,3,'usr','/usr/','1',1,'admin',NULL,NULL,'2019-11-19 08:35:19',NULL),(16,3,'软件安装目录','/usr/local','1',1,'admin',NULL,NULL,'2019-11-19 08:40:55',NULL),(23,4,'根目录','/','1',1,'admin',NULL,NULL,'2019-12-11 07:54:04',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=125 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_resource`
--

LOCK TABLES `tb_resource` WRITE;
/*!40000 ALTER TABLE `tb_resource` DISABLE KEYS */;
INSERT INTO `tb_resource` VALUES (1,0,1,'','系统管理','el-icon-menu',2,1,1,'admin',1,'admin','2018-10-12 10:09:36','2020-03-03 05:40:31'),(8,0,1,'/','首页','fa fa-tachometer',1,1,1,'admin',1,'admin','2018-10-12 10:09:36','2020-03-06 09:08:29'),(9,1,1,'/resources','菜单&权限',NULL,1,1,1,'admin',NULL,NULL,'2018-10-12 10:09:36','2020-03-03 03:14:45'),(16,0,1,'','运维','el-icon-menu',3,1,1,'admin',1,'admin','2019-01-02 16:18:00','2020-03-05 05:13:16'),(19,16,1,'/redis','redis节点',NULL,2,1,1,'admin',NULL,NULL,'2019-01-04 11:29:11','2019-11-15 01:27:53'),(21,16,1,'http://localhost:8016/#/role_manage','redis集群',NULL,3,1,1,'admin',NULL,NULL,'2019-01-21 15:44:07','2019-12-16 03:35:04'),(27,1,1,'/roles','角色管理',NULL,2,1,1,'admin',NULL,NULL,'2019-07-02 17:56:24','2019-07-26 15:24:41'),(28,1,1,'/accounts','账号管理',NULL,3,1,1,'admin',NULL,NULL,'2019-07-02 17:57:15','2019-11-06 11:20:59'),(33,9,2,'resource:save','新增按钮',NULL,4,1,1,'admin',1,'admin','2019-07-27 10:22:16','2020-03-06 06:57:30'),(34,9,2,'resource:update','编辑按钮',NULL,3,1,1,'admin',NULL,NULL,'2019-07-27 10:22:50','2020-03-03 03:15:57'),(35,9,2,'resource:delete','删除按钮',NULL,1,1,1,'admin',1,'admin','2019-07-27 10:27:04','2020-03-05 07:09:26'),(54,27,2,'role:delete','删除按钮',NULL,1,1,1,'admin',1,'admin','2019-08-19 19:53:57','2020-03-03 05:42:16'),(58,28,2,'account:delete','删除按钮',NULL,3,1,1,'admin',1,'admin','2019-08-21 10:58:14','2020-03-06 08:08:28'),(64,21,2,'redis:add','新增按钮',NULL,1,1,1,'admin',NULL,NULL,'2019-08-21 14:27:18','2019-08-21 14:27:18'),(67,28,2,'account','基本权限',NULL,1,1,1,'admin',1,'admin','2019-08-21 15:38:51','2020-03-07 02:38:21'),(71,19,2,'redis:key:delete','key删除按钮',NULL,6,1,1,'admin',NULL,NULL,'2019-09-08 15:56:21','2019-09-08 15:56:42'),(73,19,2,'redis:key:add','新增key-value按钮',NULL,4,1,1,'admin',NULL,NULL,'2019-09-08 16:16:10','2019-09-08 16:16:10'),(74,19,2,'redis:key','key操作基本权限',NULL,2,1,1,'admin',1,'admin','2019-09-08 16:21:23','2020-03-07 02:39:17'),(84,16,1,'/machines','机器','',1,1,1,'admin',NULL,NULL,'2019-11-04 14:39:49','2020-01-17 13:23:20'),(88,84,2,'machineFile:addConf','添加配置文件按钮',NULL,4,1,1,'admin',9,'test','2019-11-06 11:17:16','2020-03-07 02:24:02'),(89,84,2,'machineFile:updateFileContent','修改文件内容权限（确定按钮）',NULL,5,1,1,'admin',NULL,NULL,'2019-11-06 11:22:31','2020-01-21 01:21:26'),(94,84,2,'machineFile:rm','删除文件',NULL,8,1,1,'admin',9,'test','2019-12-23 05:57:12','2020-03-07 02:23:47'),(96,84,2,'machineFile:upload','上传文件',NULL,10,1,1,'admin',9,'test','2019-12-23 05:58:19','2020-03-07 02:23:55'),(102,16,1,'http://118.24.26.101:8888','jenkins',NULL,4,1,1,'admin',NULL,NULL,'2020-01-04 12:38:36','2020-01-04 15:30:19'),(115,1,1,'/logs','操作日志',NULL,4,1,1,'admin',1,'admin','2020-03-05 07:41:55','2020-03-07 02:20:06'),(117,9,2,'resource','基本权限',NULL,1,1,1,'admin',1,'admin','2020-03-06 06:23:00','2020-03-07 02:38:01'),(118,27,2,'role','基本权限',NULL,1,1,1,'admin',1,'admin','2020-03-06 07:21:44','2020-03-07 02:38:11'),(119,19,2,'redis','redis基本操作',NULL,1,1,1,'admin',1,'admin','2020-03-06 07:25:51','2020-03-07 02:39:09'),(120,84,2,'machine','机器基本操作权限',NULL,1,1,1,'admin',1,'admin','2020-03-06 07:27:25','2020-03-07 02:38:47'),(121,84,2,'machineFile','机器文件配置基本权限',NULL,2,1,1,'admin',1,'admin','2020-03-06 07:29:46','2020-03-07 02:38:56'),(122,27,2,'role:saveResources','分配菜单与权限',NULL,2,1,1,'admin',1,'admin','2020-03-06 08:05:59','2020-03-06 08:05:59'),(123,28,2,'account:saveRoles','角色分配',NULL,2,1,1,'admin',1,'admin','2020-03-06 08:08:23','2020-03-06 08:08:23'),(124,115,2,'log','日志基本权限',NULL,1,1,1,'admin',1,'admin','2020-03-07 02:37:32','2020-03-07 02:37:32');
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
INSERT INTO `tb_role` VALUES (1,'超级管理员',1,'具有全站所有权限',1,'admin',0,NULL,'2018-12-17 15:14:10','2018-12-17 15:14:10'),(2,'无名游客',1,'一些重要操作没有权限，如重要资源的增删改',1,'admin',1,'admin','2019-08-19 15:11:09','2020-03-06 08:42:59');
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
) ENGINE=InnoDB AUTO_INCREMENT=273 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_role_resource`
--

LOCK TABLES `tb_role_resource` WRITE;
/*!40000 ALTER TABLE `tb_role_resource` DISABLE KEYS */;
INSERT INTO `tb_role_resource` VALUES (150,1,8,NULL,NULL,'2019-08-19 19:38:50'),(151,1,1,NULL,NULL,'2019-08-19 19:38:50'),(152,1,9,NULL,NULL,'2019-08-19 19:38:50'),(155,1,35,NULL,NULL,'2019-08-19 19:38:50'),(156,1,34,NULL,NULL,'2019-08-19 19:38:50'),(157,1,33,NULL,NULL,'2019-08-19 19:38:50'),(158,1,27,NULL,NULL,'2019-08-19 19:38:50'),(163,1,28,NULL,NULL,'2019-08-19 19:38:50'),(165,1,16,NULL,NULL,'2019-08-19 19:38:50'),(167,1,19,NULL,NULL,'2019-08-19 19:38:50'),(170,2,28,NULL,NULL,'2019-08-19 19:41:52'),(172,2,1,NULL,NULL,'2019-08-19 19:41:52'),(174,2,27,NULL,NULL,'2019-08-19 19:41:52'),(176,1,54,NULL,NULL,'2019-08-19 19:54:15'),(177,2,8,NULL,NULL,'2019-08-19 20:39:26'),(184,1,58,NULL,NULL,'2019-08-21 11:30:17'),(185,1,67,NULL,NULL,'2019-08-21 15:38:58'),(186,1,21,NULL,NULL,'2019-08-21 15:56:03'),(187,1,64,NULL,NULL,'2019-08-21 15:56:03'),(193,2,67,NULL,NULL,'2019-08-22 09:01:57'),(197,1,71,NULL,NULL,'2019-09-08 15:56:52'),(199,1,73,NULL,NULL,'2019-09-08 16:20:08'),(200,1,74,NULL,NULL,'2019-09-08 16:21:42'),(201,1,84,NULL,NULL,'2019-11-04 14:40:47'),(202,2,16,NULL,NULL,'2019-11-05 15:11:12'),(204,2,21,NULL,NULL,'2019-11-05 15:11:12'),(205,2,64,NULL,NULL,'2019-11-05 15:11:12'),(206,2,19,NULL,NULL,'2019-11-05 15:11:12'),(210,2,71,NULL,NULL,'2019-11-05 15:11:12'),(212,2,74,NULL,NULL,'2019-11-05 15:11:12'),(213,2,73,NULL,NULL,'2019-11-05 15:11:12'),(217,1,88,NULL,NULL,'2019-11-06 11:24:16'),(218,1,89,NULL,NULL,'2019-11-06 11:24:16'),(223,1,94,NULL,NULL,'2019-12-23 06:16:23'),(225,1,96,NULL,NULL,'2019-12-23 06:16:23'),(227,1,102,NULL,NULL,'2020-01-04 12:38:43'),(241,2,9,NULL,NULL,'2020-03-01 08:14:31'),(255,1,115,1,'admin','2020-03-05 08:10:21'),(257,1,117,1,'admin','2020-03-06 06:26:18'),(258,1,118,1,'admin','2020-03-06 06:26:18'),(259,1,120,1,'admin','2020-03-06 07:44:49'),(260,1,121,1,'admin','2020-03-06 07:44:49'),(261,1,119,1,'admin','2020-03-06 07:44:49'),(262,2,117,1,'admin','2020-03-06 08:01:17'),(263,2,118,1,'admin','2020-03-06 08:01:17'),(264,2,120,1,'admin','2020-03-06 08:01:17'),(265,2,119,1,'admin','2020-03-06 08:01:17'),(266,2,84,1,'admin','2020-03-06 08:01:17'),(267,1,122,1,'admin','2020-03-06 08:06:41'),(268,1,123,1,'admin','2020-03-06 08:08:50'),(269,2,121,1,'admin','2020-03-07 02:27:12'),(270,2,115,1,'admin','2020-03-07 02:31:29'),(271,1,124,1,'admin','2020-03-07 02:37:39'),(272,2,124,1,'admin','2020-03-07 02:40:35');
/*!40000 ALTER TABLE `tb_role_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_operation_log`
--

DROP TABLE IF EXISTS `tb_sys_operation_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_sys_operation_log` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `type` tinyint(2) DEFAULT NULL COMMENT '1：正常操作；2：异常信息',
  `operation` varchar(1024) NOT NULL,
  `create_account_id` int(10) unsigned NOT NULL,
  `create_account` varchar(45) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=204 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_operation_log`
--

LOCK TABLES `tb_sys_operation_log` WRITE;
/*!40000 ALTER TABLE `tb_sys_operation_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_operation_log` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-03-07 11:07:03
