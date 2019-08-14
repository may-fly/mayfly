-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: 127.0.0.1    Database: sys
-- ------------------------------------------------------
-- Server version	5.7.21

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tb_admin`
--

DROP TABLE IF EXISTS `tb_admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_admin`
--

LOCK TABLES `tb_admin` WRITE;
/*!40000 ALTER TABLE `tb_admin` DISABLE KEYS */;
INSERT INTO `tb_admin` VALUES (1,'admin','e10adc3949ba59abbe56e057f20f883e');
/*!40000 ALTER TABLE `tb_admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_permission`
--

DROP TABLE IF EXISTS `tb_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(45) NOT NULL COMMENT 'api描述',
  `code` varchar(45) DEFAULT NULL,
  `group_id` int(11) DEFAULT NULL,
  `status` tinyint(2) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `uri_pattern` varchar(45) DEFAULT NULL,
  `method` tinyint(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_permission`
--

LOCK TABLES `tb_permission` WRITE;
/*!40000 ALTER TABLE `tb_permission` DISABLE KEYS */;
INSERT INTO `tb_permission` VALUES (52,'获取权限列表','permission:list',1,1,'2018-10-12 10:09:36','2019-03-23 11:09:57','/sys/v1/permissions',1),(55,'保存菜单','menu:save',1,1,'2018-12-17 13:44:08','2019-03-26 15:37:56','/sys/v1/menus',2),(56,'获取菜单列表','menu:list',1,1,'2018-12-17 13:45:23','2019-03-26 15:37:52','/sys/v1/menus',1),(57,'更新菜单','menu:update',1,1,'2018-12-17 15:14:10','2019-03-26 15:37:47','/sys/v1/menus/{id}',3),(58,'获取角色拥有的菜单','role:roleMenus',1,1,'2018-12-20 14:22:21','2019-03-26 15:37:43','/sys/v1/roles/{id}/menus',1),(59,'获取角色拥有的权限','role:rolePermissions',1,1,'2018-12-20 14:22:24','2019-03-26 15:37:39','/sys/v1/roles/{id}/permissions',1),(60,'保存角色','role:save',1,1,'2018-12-20 14:22:27','2019-03-29 12:53:04','/sys/v1/roles',2),(61,'获取角色列表','role:list',1,1,'2018-12-20 14:22:30','2019-03-29 12:52:52','/sys/v1/roles',1),(62,'保存角色权限','role:savePermission',1,1,'2018-12-29 16:40:23','2019-03-26 15:37:23','/sys/v1/roles/{id}/permissions',2),(63,'删除菜单','menu:delete',1,1,'2019-01-02 14:37:15','2019-03-26 15:37:19','/sys/v1/menus/{id}',4),(64,'删除权限','permission:del',1,1,'2019-01-02 15:00:03','2019-06-07 14:11:17','/sys/v1/permissions/{id}',4),(65,'保存角色菜单','role:saveMenu',1,1,'2019-01-03 17:42:10','2019-06-29 14:59:57','/sys/v1/roles/{id}/menus',2),(66,'启用禁用权限','permission:changeStatus',1,1,'2019-01-04 10:47:30','2019-06-29 14:57:54','/sys/v1/permissions/{id}/{status}',3),(67,'编辑修改权限','permission:update',1,1,'2019-03-23 10:38:42','2019-07-02 15:42:12','/sys/v1/permissions/{id}',3),(68,'权限组分页列表','permission:group:list',1,1,'2019-03-26 15:09:21','2019-06-29 14:51:25','/sys/v1/permisisonGroups',1),(69,'获取所有权限组','permission:group:all',1,1,'2019-03-26 15:29:16','2019-06-29 14:57:48','/sys/v1/permissionGroups/all',1),(70,'账号列表','admin:list',1,1,'2019-07-06 16:48:43','2019-07-27 10:25:27','/sys/v1/admins',1);
/*!40000 ALTER TABLE `tb_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_permission_group`
--

DROP TABLE IF EXISTS `tb_permission_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_permission_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(45) DEFAULT NULL,
  `status` tinyint(2) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL COMMENT '操作组',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_permission_group`
--

LOCK TABLES `tb_permission_group` WRITE;
/*!40000 ALTER TABLE `tb_permission_group` DISABLE KEYS */;
INSERT INTO `tb_permission_group` VALUES (1,'权限管理模块','权限管理中菜单，角色以及权限组管理相关权限',1,'2018-12-14 13:44:05','2018-12-14 13:44:05'),(2,'Redis管理模块','redis模块相关权限',1,'2018-12-14 13:50:56','2018-12-14 13:50:56');
/*!40000 ALTER TABLE `tb_permission_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_redis`
--

DROP TABLE IF EXISTS `tb_redis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_redis` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `host` varchar(45) NOT NULL,
  `port` int(11) NOT NULL,
  `pwd` varchar(45) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  `cluster_id` int(11) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_redis`
--

LOCK TABLES `tb_redis` WRITE;
/*!40000 ALTER TABLE `tb_redis` DISABLE KEYS */;
INSERT INTO `tb_redis` VALUES (1,'127.0.0.1',6379,NULL,'测试',0,'2018-12-17 15:14:10','2018-12-17 15:14:10'),(2,'94.191.96.31',6383,'',NULL,2,'2018-12-17 15:14:10','2018-12-17 15:14:10'),(3,'10.112.55.98',6379,NULL,NULL,1,'2018-12-17 15:14:10','2018-12-17 15:14:10'),(4,'10.112.55.98',6380,NULL,NULL,1,'2018-12-17 15:14:10','2018-12-17 15:14:10'),(5,'10.112.55.98',6381,NULL,NULL,1,'2018-12-17 15:14:10','2018-12-17 15:14:10'),(6,'10.112.55.98',6382,NULL,NULL,1,'2018-12-17 15:14:10','2018-12-17 15:14:10'),(7,'10.112.55.98',6383,NULL,NULL,1,'2018-12-17 15:14:10','2018-12-17 15:14:10'),(8,'10.112.55.98',6384,NULL,NULL,1,'2018-12-17 15:14:10','2018-12-17 15:14:10'),(9,'94.191.96.31',6381,NULL,NULL,2,'2018-12-17 15:14:10','2018-12-17 15:14:10'),(10,'94.191.96.31',6382,NULL,NULL,2,'2018-12-17 15:14:10','2018-12-17 15:14:10'),(11,'94.191.96.31',6384,NULL,NULL,2,'2018-12-17 15:14:10','2018-12-17 15:14:10'),(12,'94.191.96.31',6379,NULL,NULL,2,'2018-12-17 15:14:10','2018-12-17 15:14:10'),(14,'94.191.96.31',6380,NULL,NULL,2,'2018-12-17 15:14:10','2018-12-17 15:14:10');
/*!40000 ALTER TABLE `tb_redis` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_redis_cluster`
--

DROP TABLE IF EXISTS `tb_redis_cluster`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) NOT NULL,
  `type` tinyint(2) NOT NULL DEFAULT '0' COMMENT '0：菜单；1：资源（按钮等）',
  `code` varchar(45) DEFAULT NULL,
  `name` varchar(60) NOT NULL,
  `icon` varchar(45) DEFAULT NULL,
  `path` varchar(45) DEFAULT NULL COMMENT '路由路径',
  `weight` int(11) NOT NULL,
  `status` tinyint(2) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`,`update_time`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_resource`
--

LOCK TABLES `tb_resource` WRITE;
/*!40000 ALTER TABLE `tb_resource` DISABLE KEYS */;
INSERT INTO `tb_resource` VALUES (1,0,1,'permission','权限管理','fa fa-qrcode',NULL,3,1,'2018-10-12 10:09:36','2019-07-03 09:27:08'),(8,0,1,'index','首页','fa fa-tachometer','/',10,1,'2018-10-12 10:09:36','2019-07-26 15:06:23'),(9,1,1,'permission:menu:list','菜单&权限',NULL,'/menu_manage',3,1,'2018-10-12 10:09:36','2019-07-27 10:58:38'),(16,0,1,'sys','Redis管理','el-icon-menu',NULL,1,1,'2019-01-02 16:18:00','2019-01-21 15:43:38'),(19,16,1,NULL,'redis机器管理',NULL,'/redis_manage',1,1,'2019-01-04 11:29:11','2019-01-04 11:29:11'),(21,16,1,NULL,'redis集群管理',NULL,'redis_clusters',10,1,'2019-01-21 15:44:07','2019-06-30 17:31:37'),(26,0,1,NULL,'账号管理','el-icon-menu','/account_manage',2,1,'2019-07-02 17:53:45','2019-07-02 18:03:34'),(27,26,1,NULL,'角色管理',NULL,'/role_manage',2,1,'2019-07-02 17:56:24','2019-07-26 15:24:41'),(28,26,1,NULL,'账号管理',NULL,'/account_list',1,1,'2019-07-02 17:57:15','2019-07-06 16:44:10'),(29,2,2,'permission:save','新增按钮权限',NULL,NULL,1,0,'2019-07-26 16:41:54','2019-07-27 15:20:58'),(30,2,2,'permission:update','编辑按钮权限',NULL,NULL,2,0,'2019-07-26 16:43:48','2019-07-27 15:20:57'),(31,2,2,'permission:delete','删除按钮权限',NULL,NULL,3,1,'2019-07-26 16:44:33','2019-07-27 15:25:16'),(33,9,2,'menu:save','新增菜单&权限按钮',NULL,NULL,1,1,'2019-07-27 10:22:16','2019-07-27 17:26:06'),(34,9,2,'menu:update','编辑菜单权限',NULL,NULL,2,1,'2019-07-27 10:22:50','2019-07-27 21:58:07'),(35,9,2,'menu:delete','删除菜单按钮权限',NULL,NULL,3,0,'2019-07-27 10:27:04','2019-07-27 21:58:36'),(36,27,2,'role:saveMenu','分配菜单按钮权限',NULL,NULL,1,1,'2019-07-27 10:27:48','2019-07-27 15:39:42'),(37,9,2,'menu:list','菜单列表权限',NULL,NULL,4,1,'2019-07-27 10:37:03','2019-07-27 10:37:03'),(38,27,2,'role:list','角色列表权限',NULL,NULL,4,1,'2019-07-27 10:49:33','2019-07-27 10:49:33'),(39,27,2,'role:roleMenus','获取角色菜单权限',NULL,NULL,3,1,'2019-07-27 11:21:45','2019-07-27 11:21:45'),(40,9,2,'menu:changeStatus','启用禁用按钮权限',NULL,NULL,3,1,'2019-07-27 15:08:54','2019-07-27 16:09:46'),(44,28,2,'admin:list','账号列表权限',NULL,NULL,10,0,'2019-07-27 17:26:58','2019-07-27 21:50:30');
/*!40000 ALTER TABLE `tb_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_role`
--

DROP TABLE IF EXISTS `tb_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `status` tinyint(2) NOT NULL,
  `remark` varchar(45) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_role`
--

LOCK TABLES `tb_role` WRITE;
/*!40000 ALTER TABLE `tb_role` DISABLE KEYS */;
INSERT INTO `tb_role` VALUES (1,'超级管理员',1,'具有全站所有权限','2018-12-17 15:14:10','2018-12-17 15:14:10');
/*!40000 ALTER TABLE `tb_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_role_resource`
--

DROP TABLE IF EXISTS `tb_role_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_role_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `resource_id` int(11) NOT NULL,
  `type` tinyint(2) NOT NULL COMMENT '1:菜单；2:按钮',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=128 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_role_resource`
--

LOCK TABLES `tb_role_resource` WRITE;
/*!40000 ALTER TABLE `tb_role_resource` DISABLE KEYS */;
INSERT INTO `tb_role_resource` VALUES (9,1,9,1,NULL),(17,1,61,2,'2018-12-21 11:24:28'),(19,1,59,2,'2018-12-21 11:24:28'),(20,1,52,2,NULL),(21,1,56,2,'2018-12-21 11:31:14'),(23,1,58,2,'2018-12-30 10:54:10'),(24,1,62,2,'2018-12-30 10:54:10'),(43,1,8,1,'2019-01-03 17:51:54'),(55,1,60,2,'2019-03-21 11:13:14'),(56,1,55,2,'2019-03-21 11:17:04'),(57,1,66,2,'2019-03-22 18:54:12'),(68,1,65,2,'2019-03-22 19:12:26'),(69,1,63,2,'2019-03-22 19:12:26'),(73,1,68,2,'2019-03-26 15:14:33'),(74,1,69,2,'2019-03-26 15:29:26'),(75,1,64,2,'2019-04-19 10:49:37'),(84,1,57,2,'2019-06-29 16:25:56'),(89,1,1,1,'2019-06-29 16:38:23'),(92,1,19,1,'2019-06-29 16:42:24'),(93,1,16,1,'2019-06-29 16:42:24'),(108,1,26,1,'2019-07-02 17:57:35'),(109,1,28,1,'2019-07-02 17:57:35'),(110,1,27,1,'2019-07-02 17:57:35'),(113,1,70,2,'2019-07-24 11:33:47'),(114,1,67,2,'2019-07-24 11:34:24'),(118,1,34,1,'2019-07-27 10:23:09'),(119,1,33,1,'2019-07-27 10:23:09'),(120,1,35,1,'2019-07-27 10:28:54'),(121,1,36,1,'2019-07-27 10:28:54'),(123,1,37,1,'2019-07-27 10:37:25'),(124,1,38,1,'2019-07-27 10:51:05'),(125,1,39,1,'2019-07-27 11:22:07'),(126,1,40,1,'2019-07-27 15:13:15'),(127,1,44,1,'2019-07-27 17:27:11');
/*!40000 ALTER TABLE `tb_role_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_role_user`
--

DROP TABLE IF EXISTS `tb_role_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_role_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_role_user`
--

LOCK TABLES `tb_role_user` WRITE;
/*!40000 ALTER TABLE `tb_role_user` DISABLE KEYS */;
INSERT INTO `tb_role_user` VALUES (1,1,1,'2018-10-12 10:09:36');
/*!40000 ALTER TABLE `tb_role_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-08-07 14:00:23
