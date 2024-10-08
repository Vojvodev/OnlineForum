-- MySQL dump 10.13  Distrib 8.0.35, for Win64 (x86_64)
--
-- Host: localhost    Database: forum
-- ------------------------------------------------------
-- Server version	8.0.35

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Science'),(2,'Culture'),(3,'Sports'),(4,'Music');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `text` varchar(255) NOT NULL,
  `status` enum('ACTIVE','BLOCKED') NOT NULL DEFAULT 'ACTIVE',
  `categories_id` int NOT NULL,
  `users_id` int NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_comments_categories_idx` (`categories_id`),
  KEY `fk_comments_users1_idx` (`users_id`),
  CONSTRAINT `fk_comments_categories` FOREIGN KEY (`categories_id`) REFERENCES `categories` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_comments_users1` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
INSERT INTO `comments` VALUES (2,'Ovo je drugi komentar TEST TEST','ACTIVE',2,2,'2024-08-09 19:38:28'),(4,'Ovaj komentar je napisan u 22.22','ACTIVE',1,1,'2024-08-13 20:23:57'),(5,'Jos neki komentar','ACTIVE',3,2,'2024-08-13 20:23:57'),(6,'Dugaaaacak komentar jedandva 123a daw1 ae12e das c1e wad 123 sad','ACTIVE',2,1,'2024-08-13 20:23:57'),(7,'Komentar u naukama 123','ACTIVE',1,1,'2024-08-16 18:41:32'),(8,'Ovaj komentar pise dpdpdp iz aplikacije za sportsku rubriku','ACTIVE',3,6,'2024-08-16 21:15:00'),(11,'Muzikaaaa ooo oo oo o o','ACTIVE',4,6,'2024-08-16 21:29:03'),(12,'Komentar koji dpdpdp dostavlja na kvarno, tako sto je izbrisao disabled atribut koristeci inspect element','ACTIVE',4,6,'2024-08-17 13:05:16'),(13,'Prethodni komentar je naravno nastao prije ugradjene ispravne autorizacije','ACTIVE',4,6,'2024-08-17 13:06:22'),(18,'Nauka je dobra, nauka je super dobra.','ACTIVE',1,6,'2024-08-17 15:56:58'),(19,'╨ƒ╨╛╤ê╤é╨╛ ╨║╨╕╨╗╨░ ╨╜╨░╤â╨║╨╡ PROMJENA SA SAJTA HIHIHI - izmjena -- ','ACTIVE',1,7,'2024-08-17 16:02:35'),(21,'volim nekulturu','ACTIVE',2,9,'2024-08-19 09:54:12'),(22,'Mnogo sporta i povreda','ACTIVE',3,9,'2024-08-19 10:16:13'),(25,'Jedan komentar u sportove\nIZMJENA!!!!','ACTIVE',3,8,'2024-08-22 16:22:06');
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permissions`
--

DROP TABLE IF EXISTS `permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permissions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `categories_id` int NOT NULL,
  `users_id` int NOT NULL,
  `add_permission` tinyint NOT NULL DEFAULT '0',
  `edit_permission` tinyint NOT NULL DEFAULT '0',
  `delete_permission` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`,`categories_id`,`users_id`),
  KEY `fk_permissions_categories1_idx` (`categories_id`),
  KEY `fk_permissions_users1_idx` (`users_id`),
  CONSTRAINT `fk_permissions_categories1` FOREIGN KEY (`categories_id`) REFERENCES `categories` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_permissions_users1` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=78 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permissions`
--

LOCK TABLES `permissions` WRITE;
/*!40000 ALTER TABLE `permissions` DISABLE KEYS */;
INSERT INTO `permissions` VALUES (1,1,1,0,1,0),(2,2,1,0,1,0),(3,3,1,0,1,0),(4,4,1,0,1,0),(5,1,2,0,1,1),(6,2,2,1,0,1),(7,3,2,1,1,0),(9,4,2,0,0,0),(14,1,5,0,1,0),(15,2,5,1,0,1),(16,3,5,0,0,0),(17,4,5,0,0,1),(18,1,6,1,0,0),(19,2,6,1,1,1),(20,3,6,1,0,0),(21,4,6,0,0,1),(22,1,7,1,0,1),(23,2,7,0,0,1),(24,3,7,1,0,1),(25,4,7,1,1,0),(26,1,8,1,1,1),(27,2,8,1,1,1),(28,3,8,1,1,1),(29,4,8,1,1,1),(30,1,9,0,1,0),(31,2,9,0,0,1),(32,3,9,1,0,0),(33,4,9,0,0,0),(34,1,10,0,0,0),(35,2,10,0,0,0),(36,3,10,0,0,0),(37,4,10,0,0,0);
/*!40000 ALTER TABLE `permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `role` enum('ADMIN','MODERATOR','USER') NOT NULL DEFAULT 'USER',
  `status` enum('ACTIVE','BLOCKED') NOT NULL DEFAULT 'BLOCKED',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'DARIJOTEST','$2a$12$JO4f5nU7OkWayFeXH1OGEO2kLAnAnfLqobnrhShZcurwt9ZA1uU92','asd@asd','ADMIN','BLOCKED','2024-08-09 19:35:13'),(2,'zoran','zorantestpass','qwe@asd','MODERATOR','BLOCKED','2024-08-09 19:35:13'),(5,'Treci Korisnikk','passss','wewewe@asdasd','MODERATOR','BLOCKED','2024-08-15 15:22:25'),(6,'dpdpdp','$2a$10$W1iTj32Yr5kn3jCpgLjM7uiDNidFbRQRqw2rwoYaJfj547vrQ.3Ge','dp@dp','USER','BLOCKED','2024-08-16 19:29:36'),(7,'plpl','$2a$10$RZNEbx03gLmmzwZ7iOoPLuie.mWH.LnWe0kv1WTXGhWTcwmWVHQp2','pl@pl','USER','BLOCKED','2024-08-16 19:31:11'),(8,'fg','$2a$10$Y27/ZP3s1QDrX07ZLctWz.PY4BL5F0Dh3rh.RcOWtKynJs90jlIsm','darijo.prerad@student.etf.unibl.org','ADMIN','ACTIVE','2024-08-16 19:35:54'),(9,'darijo','$2a$10$o3wRL/jgQyzBxYhBQa.dY.SlIWcxV8teBeEMPe1tMUmeVsCiP.jZe','darijonainternetu@gmail.com','USER','ACTIVE','2024-08-18 14:39:10'),(10,'ime','$2a$10$9CaAJBFrmuSIKFmr0iSXteLMb2b2fpThBQyWJkIl6gcaD4Qrggplq','darijonanetu@gmail.com','USER','ACTIVE','2024-08-20 09:52:48');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `users_AFTER_INSERT` AFTER INSERT ON `users` FOR EACH ROW BEGIN
	INSERT INTO permissions (categories_id, users_id) VALUES (1, new.id);
    INSERT INTO permissions (categories_id, users_id) VALUES (2, new.id);
    INSERT INTO permissions (categories_id, users_id) VALUES (3, new.id);
    INSERT INTO permissions (categories_id, users_id) VALUES (4, new.id);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-08-23 22:51:40
