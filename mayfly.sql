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
INSERT INTO `tb_admin` VALUES (1,'admin',NULL);
/*!40000 ALTER TABLE `tb_admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_menu`
--

DROP TABLE IF EXISTS `tb_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) NOT NULL,
  `code` varchar(45) DEFAULT NULL,
  `name` varchar(60) NOT NULL,
  `icon` varchar(45) DEFAULT NULL,
  `path` varchar(45) DEFAULT NULL COMMENT '路由路径',
  `weight` int(11) NOT NULL,
  `status` tinyint(2) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_menu`
--

LOCK TABLES `tb_menu` WRITE;
/*!40000 ALTER TABLE `tb_menu` DISABLE KEYS */;
INSERT INTO `tb_menu` VALUES (1,0,'permission','权限管理','fa fa-qrcode',NULL,2,1,'2018-10-12 10:09:36','2019-01-02 16:49:46'),(2,1,'permission:api','权限列表',NULL,'/permission_list',3,1,'2018-10-12 10:09:36','2018-10-12 10:09:36'),(3,1,'permission:api:role','角色管理',NULL,'/role_manage',2,1,'2018-10-12 10:09:36','2018-10-12 10:09:36'),(4,1,'permission:group','权限组管理',NULL,'/permission_group_list',1,1,'2018-10-12 10:09:36','2018-10-12 10:09:36'),(8,0,'index','首页','fa fa-tachometer','/',10,1,'2018-10-12 10:09:36','2019-01-02 16:00:53'),(9,1,'permission:menu:list','菜单管理',NULL,'/menu_manage',3,1,'2018-10-12 10:09:36','2018-10-12 10:09:36'),(16,0,'sys','Redis管理','el-icon-menu',NULL,1,1,'2019-01-02 16:18:00','2019-01-21 15:43:38'),(19,16,NULL,'redis单机',NULL,'/redis_manage',1,1,'2019-01-04 11:29:11','2019-01-04 11:29:11'),(21,16,NULL,'redis集群',NULL,NULL,10,1,'2019-01-21 15:44:07','2019-01-21 15:44:07');
/*!40000 ALTER TABLE `tb_menu` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_permission`
--

LOCK TABLES `tb_permission` WRITE;
/*!40000 ALTER TABLE `tb_permission` DISABLE KEYS */;
INSERT INTO `tb_permission` VALUES (51,'保存权限','permission:save',1,1,'2018-10-12 10:09:36','2019-03-23 14:07:25',NULL,NULL),(52,'获取权限列表','permission:list',1,1,'2018-10-12 10:09:36','2019-03-23 11:09:57','/sys/v1/permissions',1),(55,'保存菜单','menu:save',1,1,'2018-12-17 13:44:08','2019-03-26 15:37:56','/sys/v1/menus',2),(56,'获取菜单列表','menu:list',1,1,'2018-12-17 13:45:23','2019-03-26 15:37:52','/sys/v1/menus',1),(57,'更新菜单','menu:update',1,1,'2018-12-17 15:14:10','2019-03-26 15:37:47','/sys/v1/menus/{id}',3),(58,'获取角色拥有的菜单','role:roleMenus',1,1,'2018-12-20 14:22:21','2019-03-26 15:37:43','/sys/v1/roles/{id}/menus',1),(59,'获取角色拥有的权限','role:rolePermissions',1,1,'2018-12-20 14:22:24','2019-03-26 15:37:39','/sys/v1/roles/{id}/permissions',1),(60,'保存角色','role:save',1,1,'2018-12-20 14:22:27','2019-03-26 15:37:32','/sys/v1/roles',2),(61,'获取角色列表','role:list',1,1,'2018-12-20 14:22:30','2019-03-26 15:37:28','/sys/v1/roles',1),(62,'保存角色权限','role:savePermission',1,1,'2018-12-29 16:40:23','2019-03-26 15:37:23','/sys/v1/roles/{id}/permissions',2),(63,'删除菜单','menu:delete',1,1,'2019-01-02 14:37:15','2019-03-26 15:37:19','/sys/v1/menus/{id}',4),(64,'删除权限','permission:del',1,1,'2019-01-02 15:00:03','2019-03-26 15:41:17','/sys/v1/permissions/{id}',4),(65,'保存角色菜单','role:saveMenu',1,1,'2019-01-03 17:42:10','2019-03-26 15:41:07','/sys/v1/roles/{id}/menus',2),(66,'启用禁用权限','permission:changeStatus',1,1,'2019-01-04 10:47:30','2019-03-26 15:36:54','/sys/v1/permissions/{id}/{status}',3),(67,'编辑修改权限','permission:update',1,0,'2019-03-23 10:38:42','2019-03-27 14:32:07','/sys/v1/permissions/{id}',3),(68,'权限组分页列表','permission:group:list',1,1,'2019-03-26 15:09:21','2019-03-26 15:36:41','/sys/v1/permisisonGroups',1),(69,'获取所有权限组','permission:group:all',1,1,'2019-03-26 15:29:16','2019-03-26 15:36:32','/sys/v1/permissionGroups/all',1);
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
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
  `ip` varchar(45) NOT NULL,
  `port` int(11) NOT NULL,
  `pwd` varchar(45) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  `cluster_id` int(11) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_redis`
--

LOCK TABLES `tb_redis` WRITE;
/*!40000 ALTER TABLE `tb_redis` DISABLE KEYS */;
INSERT INTO `tb_redis` VALUES (1,'127.0.0.1',6379,NULL,'测试',0,'2018-12-17 15:14:10','2018-12-17 15:14:10'),(2,'94.191.96.31',6383,'',NULL,2,'2018-12-17 15:14:10','2018-12-17 15:14:10'),(3,'10.112.55.157',6379,NULL,NULL,1,'2018-12-17 15:14:10','2018-12-17 15:14:10'),(4,'10.112.55.157',6380,NULL,NULL,1,'2018-12-17 15:14:10','2018-12-17 15:14:10'),(5,'10.112.55.157',6381,NULL,NULL,1,'2018-12-17 15:14:10','2018-12-17 15:14:10'),(6,'10.112.55.157',6382,NULL,NULL,1,'2018-12-17 15:14:10','2018-12-17 15:14:10'),(7,'10.112.55.157',6383,NULL,NULL,1,'2018-12-17 15:14:10','2018-12-17 15:14:10'),(8,'10.112.55.157',6384,NULL,NULL,1,'2018-12-17 15:14:10','2018-12-17 15:14:10');
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_redis_cluster`
--

LOCK TABLES `tb_redis_cluster` WRITE;
/*!40000 ALTER TABLE `tb_redis_cluster` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_redis_cluster` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_role_resource`
--

LOCK TABLES `tb_role_resource` WRITE;
/*!40000 ALTER TABLE `tb_role_resource` DISABLE KEYS */;
INSERT INTO `tb_role_resource` VALUES (1,1,1,1,NULL),(2,1,2,1,NULL),(3,1,3,1,NULL),(9,1,9,1,NULL),(17,1,61,2,'2018-12-21 11:24:28'),(19,1,59,2,'2018-12-21 11:24:28'),(20,1,52,2,NULL),(21,1,56,2,'2018-12-21 11:31:14'),(23,1,58,2,'2018-12-30 10:54:10'),(24,1,62,2,'2018-12-30 10:54:10'),(43,1,8,1,'2019-01-03 17:51:54'),(48,1,16,1,'2019-01-04 11:33:18'),(49,1,19,1,'2019-01-04 11:33:18'),(50,1,4,1,'2019-01-21 15:49:48'),(51,1,21,1,'2019-01-21 15:49:48'),(55,1,60,2,'2019-03-21 11:13:14'),(56,1,55,2,'2019-03-21 11:17:04'),(57,1,66,2,'2019-03-22 18:54:12'),(60,1,51,2,'2019-03-22 18:54:12'),(65,1,64,2,'2019-03-22 19:00:43'),(68,1,65,2,'2019-03-22 19:12:26'),(69,1,63,2,'2019-03-22 19:12:26'),(70,1,57,2,'2019-03-22 19:12:26'),(72,1,67,2,'2019-03-23 15:31:02'),(73,1,68,2,'2019-03-26 15:14:33'),(74,1,69,2,'2019-03-26 15:29:26');
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

-- Dump completed on 2019-03-27 16:20:10
