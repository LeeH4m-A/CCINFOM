-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: dbvideogame
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `branches`
--
use attempt;
DROP TABLE IF EXISTS `branches`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `branches` (
  `branch_id` int NOT NULL,
  `branch_name` varchar(100) DEFAULT NULL,
  `location` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `branches`
--

LOCK TABLES `branches` WRITE;
/*!40000 ALTER TABLE `branches` DISABLE KEYS */;
INSERT INTO `branches` VALUES (3000,'DataBlitz Trinoma','Quezon City'),(3001,'DataBlitz UP Town Center','Quezon City'),(3002,'DataBlitz SM North Edsa','Quezon City'),(3003,'DataBlitz SM Mall of Asia','Pasay City'),(3004,'DataBlitz Ayala Mall Manila Bay','Pasay City'),(3005,'DataBlitz Robinsons Place Manila','Manila City'),(3006,'DataBlitz SM Makati','Makati City'),(3007,'DataBlitz Glorietta','Makati City'),(3008,'DataBlitz One Ayala Mall','Makati City');
/*!40000 ALTER TABLE `branches` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consoles`
--

DROP TABLE IF EXISTS `consoles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `consoles` (
  `console_name` varchar(25) NOT NULL,
  `price` float DEFAULT NULL,
  `release_year` int DEFAULT NULL,
  `developer` varchar(25) DEFAULT NULL,
  `generation` int DEFAULT NULL,
  PRIMARY KEY (`console_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consoles`
--

LOCK TABLES `consoles` WRITE;
/*!40000 ALTER TABLE `consoles` DISABLE KEYS */;
INSERT INTO `consoles` VALUES ('Nintendo Switch',11596,2017,'Nintendo',8),('PS4',17999.9,2013,'Sony',8),('PS5',30799.9,2020,'Sony',9),('Xbox One',16000,2013,'Microsoft',8),('Xbox Series X',25799.9,2020,'Microsoft',9);
/*!40000 ALTER TABLE `consoles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `customer_id` int NOT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `first_name` varchar(100) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `address` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (2000,'Peralta','Jacob',36,'Quezon City'),(2001,'Santiago','Amy',37,'Quezon City'),(2002,'Holt','Raymond',47,'Pasay City'),(2003,'Jeffords','Terry',45,'Pasay City'),(2004,'Diaz','Rosa',39,'Makati City'),(2005,'Boyle','Charles',42,'Makati City'),(2006,'Linetti','Gina',40,'Manila City'),(2007,'Hitchcock','Michael',66,'Manila City');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `games`
--

DROP TABLE IF EXISTS `games`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `games` (
  `game_id` int NOT NULL,
  `game_name` varchar(100) DEFAULT NULL,
  `release_year` int DEFAULT NULL,
  `genre` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `games`
--

LOCK TABLES `games` WRITE;
/*!40000 ALTER TABLE `games` DISABLE KEYS */;
INSERT INTO `games` VALUES (0,'Silent Hill 2 Remake',2024,'Horror'),(1,'Baldur\'s Gate 3',2024,'Action-Adventure'),(2,'Elden Ring',2022,'Action-Adventure'),(3,'The Legend of Zelda: Tears of the Kingdom',2023,'Action-Adventure'),(4,'Resident Evil 4 Remake',2023,'Survival-Horror'),(5,'Tekken 8',2024,'Fighting'),(6,'Black Myth Wukong',2024,'Fighting'),(7,'Super Smash Bros Ultimate',2018,'Fighting'),(8,'Dragon Ball Sparking Zero',2024,'Fighting');
/*!40000 ALTER TABLE `games` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `product_id` int NOT NULL,
  `game_id` int DEFAULT NULL,
  `console` varchar(25) DEFAULT NULL,
  `price` float DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  KEY `game_id` (`game_id`),
  KEY `console` (`console`),
  CONSTRAINT `products_ibfk_1` FOREIGN KEY (`game_id`) REFERENCES `games` (`game_id`) ON DELETE SET NULL,
  CONSTRAINT `products_ibfk_2` FOREIGN KEY (`console`) REFERENCES `consoles` (`console_name`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (10000,0,'PS5',2999.99),(10001,1,'PS5',3159.95),(10002,1,'Xbox Series X',2999.95),(10003,2,'PS4',2489.99),(10004,2,'PS5',3099.95),(10005,2,'Xbox One',2395.5),(10006,2,'Xbox One',3049.95),(10007,3,'Nintendo Switch',2649.99),(10008,4,'PS4',2594.99),(10009,4,'Xbox One',2594.99),(10010,4,'PS5',2989.95),(10011,4,'Xbox Series X',2989.95),(10012,5,'PS5',2794.95),(10013,5,'Xbox Series X',2794.95),(10014,6,'PS5',3099.95),(10015,6,'Xbox Series X',3049.95),(10016,7,'Nintendo Switch',2495.5),(10017,8,'PS5',2799.95);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `receipts`
--

DROP TABLE IF EXISTS `receipts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `receipts` (
  `receipt_id` int NOT NULL,
  `customer_id` int DEFAULT NULL,
  `product_id_purchased` int DEFAULT NULL,
  `date_of_purchase` date DEFAULT NULL,
  `branch_id` int DEFAULT NULL,
  PRIMARY KEY (`receipt_id`),
  KEY `product_id_purchased` (`product_id_purchased`),
  KEY `branch_id` (`branch_id`),
  CONSTRAINT `receipts_ibfk_1` FOREIGN KEY (`product_id_purchased`) REFERENCES `products` (`product_id`) ON DELETE SET NULL,
  CONSTRAINT `receipts_ibfk_2` FOREIGN KEY (`branch_id`) REFERENCES `branches` (`branch_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `receipts`
--

LOCK TABLES `receipts` WRITE;
/*!40000 ALTER TABLE `receipts` DISABLE KEYS */;
INSERT INTO `receipts` VALUES (100000,2000,10000,'2024-10-15',3000),(100001,2000,10004,'2024-10-20',3000),(100002,2000,10012,'2024-10-24',3001),(100003,2000,10016,'2024-10-28',3001),(100004,2004,10002,'2024-11-07',3006),(100005,2004,10009,'2024-11-16',3007),(100006,2004,10015,'2024-11-20',3007),(100007,2004,10004,'2024-11-30',3008);
/*!40000 ALTER TABLE `receipts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `suppliers`
--

DROP TABLE IF EXISTS `suppliers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `suppliers` (
  `supplier_id` int NOT NULL,
  `location` varchar(30) DEFAULT NULL,
  `product_id` int NOT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`supplier_id`,`product_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `suppliers_ibfk_1` FOREIGN KEY (`supplier_id`) REFERENCES `branches` (`branch_id`) ON DELETE CASCADE,
  CONSTRAINT `suppliers_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `suppliers`
--

LOCK TABLES `suppliers` WRITE;
/*!40000 ALTER TABLE `suppliers` DISABLE KEYS */;
INSERT INTO `suppliers` VALUES (3000,'Quezon City',10000,10),(3000,'Quezon City',10001,10),(3000,'Quezon City',10002,10),(3000,'Quezon City',10003,10),(3000,'Quezon City',10004,10),(3000,'Quezon City',10005,10),(3000,'Quezon City',10006,10),(3000,'Quezon City',10007,10),(3000,'Quezon City',10008,10),(3001,'Quezon City',10009,10),(3001,'Quezon City',10010,10),(3001,'Quezon City',10011,10),(3001,'Quezon City',10012,10),(3001,'Quezon City',10013,10),(3001,'Quezon City',10014,10),(3001,'Quezon City',10015,10),(3001,'Quezon City',10016,10),(3001,'Quezon City',10017,10),(3002,'Quezon City',10000,10),(3002,'Quezon City',10001,10),(3002,'Quezon City',10002,10),(3002,'Quezon City',10003,10),(3002,'Quezon City',10004,10),(3002,'Quezon City',10005,10),(3002,'Quezon City',10006,10),(3002,'Quezon City',10007,10),(3002,'Quezon City',10008,10),(3003,'Pasay City',10009,10),(3003,'Pasay City',10010,10),(3003,'Pasay City',10011,10),(3003,'Pasay City',10012,10),(3003,'Pasay City',10013,10),(3003,'Pasay City',10014,10),(3003,'Pasay City',10015,10),(3003,'Pasay City',10016,10),(3003,'Pasay City',10017,10),(3004,'Pasay City',10000,10),(3004,'Pasay City',10001,10),(3004,'Pasay City',10002,10),(3004,'Pasay City',10003,10),(3004,'Pasay City',10004,10),(3004,'Pasay City',10005,10),(3004,'Pasay City',10006,10),(3004,'Pasay City',10007,10),(3005,'Pasay City',10008,10),(3005,'Manila City',10009,10),(3005,'Manila City',10010,10),(3005,'Manila City',10011,10),(3005,'Manila City',10012,10),(3005,'Manila City',10013,10),(3005,'Manila City',10014,10),(3005,'Manila City',10015,10),(3005,'Manila City',10016,10),(3005,'Manila City',10017,10),(3006,'Makati City',10000,10),(3006,'Makati City',10001,10),(3006,'Makati City',10002,10),(3006,'Makati City',10003,10),(3006,'Makati City',10004,10),(3006,'Makati City',10005,10),(3006,'Makati City',10006,10),(3006,'Makati City',10007,10),(3006,'Makati City',10008,10),(3007,'Makati City',10009,10),(3007,'Makati City',10010,10),(3007,'Makati City',10011,10),(3007,'Makati City',10012,10),(3007,'Makati City',10013,10),(3007,'Makati City',10014,10),(3007,'Makati City',10015,10),(3007,'Makati City',10016,10),(3007,'Makati City',10017,10),(3008,'Makati City',10000,10),(3008,'Makati City',10001,10),(3008,'Makati City',10002,10),(3008,'Makati City',10003,10),(3008,'Makati City',10004,10),(3008,'Makati City',10005,10),(3008,'Makati City',10006,10),(3008,'Makati City',10007,10),(3008,'Makati City',10008,10),(3008,'Makati City',10009,10),(3008,'Makati City',10010,10),(3008,'Makati City',10011,10),(3008,'Makati City',10012,10),(3008,'Makati City',10013,10),(3008,'Makati City',10014,10),(3008,'Makati City',10015,10),(3008,'Makati City',10016,10),(3008,'Makati City',10017,10);
/*!40000 ALTER TABLE `suppliers` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-24 17:57:18
