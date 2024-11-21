-- MySQL dump 10.13  Distrib 8.0.39, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: test
-- ------------------------------------------------------
-- Server version	8.0.39

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
-- Table structure for table `participate`
--

DROP TABLE IF EXISTS `participate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `participate` (
  `session_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  KEY `FK7hjfuksklx7u6skattt3ykwkb` (`user_id`),
  KEY `FKhf15dnx2t64ujblwvig4553ek` (`session_id`),
  CONSTRAINT `FK7hjfuksklx7u6skattt3ykwkb` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `participate`
--

LOCK TABLES `participate` WRITE;
/*!40000 ALTER TABLE `participate` DISABLE KEYS */;
/*!40000 ALTER TABLE `participate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sessions`
--

DROP TABLE IF EXISTS `sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sessions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `date` datetime NOT NULL,
  `description` varchar(2500) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `teacher_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKko4pgjn6gk1r8tvomb12lk5fq` (`teacher_id`),
  CONSTRAINT `FKko4pgjn6gk1r8tvomb12lk5fq` FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sessions`
--

LOCK TABLES `sessions` WRITE;
/*!40000 ALTER TABLE `sessions` DISABLE KEYS */;
/*!40000 ALTER TABLE `sessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teachers`
--

DROP TABLE IF EXISTS `teachers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teachers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `first_name` varchar(20) DEFAULT NULL,
  `last_name` varchar(20) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teachers`
--

LOCK TABLES `teachers` WRITE;
/*!40000 ALTER TABLE `teachers` DISABLE KEYS */;
INSERT INTO `teachers` VALUES (1,'2024-10-15 05:27:53','John','Doe','2024-10-15 05:27:53'),(2,NULL,'Margot','DELAHAYE',NULL),(3,NULL,'Hélène','THIERCELIN',NULL),(4,'2024-10-31 20:02:50','Karim','ABDEL','2024-10-31 20:02:50'),(5,'2024-11-01 09:21:26','Karim','ABDEL','2024-11-01 09:21:26'),(6,'2024-11-01 09:22:06','Karim','ABDEL','2024-11-01 09:22:06'),(7,'2024-11-01 09:22:11','Karim','ABDEL','2024-11-01 09:22:11'),(8,'2024-11-01 09:22:27','Karim','ABDEL','2024-11-01 09:22:27'),(9,'2024-11-01 09:27:07','Karim','ABDEL','2024-11-01 09:27:07'),(10,'2024-11-01 09:27:12','Karim','ABDEL','2024-11-01 09:27:12'),(11,'2024-11-01 09:28:18','Karim','ABDEL','2024-11-01 09:28:18'),(12,'2024-11-01 09:28:33','Karim','ABDEL','2024-11-01 09:28:33'),(13,'2024-11-01 09:30:08','Karim','ABDEL','2024-11-01 09:30:08'),(14,'2024-11-01 09:30:30','Karim','ABDEL','2024-11-01 09:30:30'),(15,'2024-11-01 09:31:55','Karim','ABDEL','2024-11-01 09:31:55'),(16,'2024-11-01 09:33:26','Karim','ABDEL','2024-11-01 09:33:26'),(17,'2024-11-01 09:34:27','Karim','ABDEL','2024-11-01 09:34:27'),(18,'2024-11-01 09:36:36','Karim','ABDEL','2024-11-01 09:36:36'),(19,'2024-11-01 09:37:41','Karim','ABDEL','2024-11-01 09:37:41'),(20,'2024-11-01 09:40:17','Karim','ABDEL','2024-11-01 09:40:17'),(21,'2024-11-01 09:41:45','Karim','ABDEL','2024-11-01 09:41:45'),(22,'2024-11-01 09:42:08','Karim','ABDEL','2024-11-01 09:42:08'),(23,'2024-11-01 09:43:55','Karim','ABDEL','2024-11-01 09:43:55'),(24,'2024-11-01 09:44:59','Karim','ABDEL','2024-11-01 09:44:59'),(25,'2024-11-01 09:46:29','Karim','ABDEL','2024-11-01 09:46:29'),(26,'2024-11-01 09:47:51','Karim','ABDEL','2024-11-01 09:47:51'),(27,'2024-11-01 09:48:49','Karim','ABDEL','2024-11-01 09:48:49'),(28,'2024-11-01 09:49:06','Karim','ABDEL','2024-11-01 09:49:06'),(29,'2024-11-01 09:50:00','Karim','ABDEL','2024-11-01 09:50:00'),(30,'2024-11-01 09:52:16','Karim','ABDEL','2024-11-01 09:52:16'),(31,'2024-11-01 09:55:03','Karim','ABDEL','2024-11-01 09:55:03'),(32,'2024-11-01 09:55:20','Karim','ABDEL','2024-11-01 09:55:20'),(33,'2024-11-01 09:57:49','Karim','ABDEL','2024-11-01 09:57:49'),(34,'2024-11-01 09:58:27','Karim','ABDEL','2024-11-01 09:58:27'),(35,'2024-11-01 09:59:20','Karim','ABDEL','2024-11-01 09:59:20'),(36,'2024-11-01 10:01:37','Karim','ABDEL','2024-11-01 10:01:37'),(37,'2024-11-01 10:04:17','Karim','ABDEL','2024-11-01 10:04:17'),(38,'2024-11-01 10:04:55','Karim','ABDEL','2024-11-01 10:04:55'),(39,'2024-11-01 10:09:21','Karim','ABDEL','2024-11-01 10:09:21'),(40,'2024-11-01 10:09:46','Karim','ABDEL','2024-11-01 10:09:46'),(41,'2024-11-01 10:10:06','Karim','ABDEL','2024-11-01 10:10:06'),(42,'2024-11-01 10:15:45','Karim','ABDEL','2024-11-01 10:15:45'),(43,'2024-11-01 10:16:03','Karim','ABDEL','2024-11-01 10:16:03'),(44,'2024-11-01 10:16:18','Karim','ABDEL','2024-11-01 10:16:18'),(45,'2024-11-01 10:20:09','Karim','ABDEL','2024-11-01 10:20:09'),(46,'2024-11-01 10:20:49','Karim','ABDEL','2024-11-01 10:20:49'),(47,'2024-11-01 10:24:24','Karim','ABDEL','2024-11-01 10:24:24'),(48,'2024-11-01 10:24:39','Karim','ABDEL','2024-11-01 10:24:39'),(49,'2024-11-01 10:24:42','Karim','ABDEL','2024-11-01 10:24:42'),(50,'2024-11-01 10:25:43','Karim','ABDEL','2024-11-01 10:25:43'),(51,'2024-11-01 11:33:47','Karim','ABDEL','2024-11-01 11:33:47'),(52,'2024-11-01 11:37:58','Karim','ABDEL','2024-11-01 11:37:58'),(53,'2024-11-01 11:38:53','Karim','ABDEL','2024-11-01 11:38:53'),(54,'2024-11-01 11:46:20','Karim','ABDEL','2024-11-01 11:46:20'),(55,'2024-11-01 11:46:41','Karim','ABDEL','2024-11-01 11:46:41'),(56,'2024-11-01 11:46:58','Karim','ABDEL','2024-11-01 11:46:58'),(57,'2024-11-01 11:51:24','Karim','ABDEL','2024-11-01 11:51:24'),(58,'2024-11-01 11:51:59','Karim','ABDEL','2024-11-01 11:51:59'),(59,'2024-11-01 11:52:17','Karim','ABDEL','2024-11-01 11:52:17'),(60,'2024-11-01 16:09:42','Karim','ABDEL','2024-11-01 16:09:42'),(61,'2024-11-01 16:15:44','Karim','ABDEL','2024-11-01 16:15:44'),(62,'2024-11-01 16:19:08','Karim','ABDEL','2024-11-01 16:19:08'),(63,'2024-11-01 16:19:16','Karim','ABDEL','2024-11-01 16:19:16'),(64,'2024-11-01 16:20:53','Karim','ABDEL','2024-11-01 16:20:53'),(65,'2024-11-01 16:21:48','Karim','ABDEL','2024-11-01 16:21:48'),(66,'2024-11-01 16:46:53','Karim','ABDEL','2024-11-01 16:46:53'),(67,'2024-11-01 17:14:47','Karim','ABDEL','2024-11-01 17:14:47'),(68,'2024-11-01 17:16:44','Karim','ABDEL','2024-11-01 17:16:44'),(69,'2024-11-01 17:20:10','Karim','ABDEL','2024-11-01 17:20:10'),(70,'2024-11-01 17:20:17','Karim','ABDEL','2024-11-01 17:20:17'),(71,'2024-11-01 17:21:41','Karim','ABDEL','2024-11-01 17:21:41'),(72,'2024-11-01 17:22:57','Karim','ABDEL','2024-11-01 17:22:57'),(73,'2024-11-01 19:35:04','Karim','ABDEL','2024-11-01 19:35:04');
/*!40000 ALTER TABLE `teachers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `admin` bit(1) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `first_name` varchar(20) DEFAULT NULL,
  `last_name` varchar(20) DEFAULT NULL,
  `password` varchar(120) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKavh1b2ec82audum2lyjx2p1ws` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,_binary '\0','2024-10-14 22:40:36','aflouat@gmail.com','AFLOUAT','ABDEL WEDOUD','$2a$10$UreN.a73ZCPWpQ8qR5ICwO6zl/9hoOQjomLug2wPp1nJ.bPqrb05m','2024-10-14 22:40:36'),(2,_binary '\0','2024-10-15 05:29:35','minetou1987@gmail.com','Esma','ABDEL WEDOUD','$2a$10$NSuGcMD30HSxFSrj.ppJW..F6NLRRM1D3M42ztJ/bXHbNsFDYNFp2','2024-10-15 05:29:35'),(3,_binary '\0','2024-10-23 23:03:06','aflouat1@gmail.com','AFLOUAT','ABDEL WEDOUD','$2a$10$IJcWt60lFaGABlvrrzW7oefsHYt9dAOHzT9FNKVqJ05H7gRcF2r6m','2024-10-23 23:03:06'),(4,_binary '',NULL,'yoga@studio.com','Admin','Admin','$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq',NULL),(5,_binary '\0','2024-10-31 22:13:17','aflouat@google.com','Aflouat','ABDEL','$2a$10$9Kvn1ilIFhKTIFR1GjLyveqBf05rC6uyvvXRvWmvxz7lvYoNLrnwq','2024-10-31 22:13:17'),(6,_binary '\0','2024-10-31 22:20:49','aflouat1@google.com','Aflouat','ABDEL','$2a$10$TFNJ5Y2/rx58Z264fdQWMuTRqHBRTqqI31GNHo//crQiC6FVmOpJK','2024-10-31 22:20:49'),(7,_binary '\0','2024-10-31 22:21:06','aflouat2@google.com','Aflouat','ABDEL','$2a$10$kjCxSKezuXGGoK34WDoT7OGPQzJpPxGDE6OKgaloGOYwlM8WFo8cm','2024-10-31 22:21:06'),(8,_binary '\0','2024-11-01 22:11:49','toto3@toto.com','toto','toto','$2a$10$p3X3Hd3ayjbNPBj161VLlOy70zQWk7Fj5qKE90keMHVEl7VxL3dvq','2024-11-01 22:11:50');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-02 23:13:40
