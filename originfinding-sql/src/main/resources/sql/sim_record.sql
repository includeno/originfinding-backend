-- MySQL dump 10.13  Distrib 8.0.24, for macos11 (x86_64)
--
-- Host: localhost    Database: demo
-- ------------------------------------------------------
-- Server version	8.0.26

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
-- Table structure for table `sim_record`
--
USE demo;
DROP TABLE IF EXISTS `sim_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sim_record` (
  `id` int NOT NULL AUTO_INCREMENT,
  `url` varchar(300) NOT NULL DEFAULT '',
  `title` varchar(300) NOT NULL DEFAULT '',
  `tag` varchar(1000) NOT NULL DEFAULT '',
  `time` datetime NOT NULL,
  `view` int NOT NULL DEFAULT '-1',
  `simhash` varchar(300) NOT NULL DEFAULT '',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `valid` int NOT NULL DEFAULT '1',
  `simlevelfirst` varchar(300) DEFAULT NULL,
  `simlevelsecond` varchar(300) DEFAULT NULL,
  `earlyparent_id` int NOT NULL DEFAULT '-1',
  `simparent_id` int NOT NULL DEFAULT '-1',
  `manulparent_id` int NOT NULL DEFAULT '-1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

