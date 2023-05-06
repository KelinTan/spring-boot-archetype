DROP TABLE IF EXISTS `biz_account`;
CREATE TABLE `biz_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `password` varchar(30) COLLATE utf8mb4_bin DEFAULT NULL,
  `token` varchar(2505) COLLATE utf8mb4_bin DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `height` int(11) DEFAULT NULL,
  `money` decimal(10,2) DEFAULT NULL,
  `birth_date` timestamp NULL DEFAULT NULL,
  `verify` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

DROP TABLE IF EXISTS `id_sharding_0`;
CREATE TABLE `id_sharding_0` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

DROP TABLE IF EXISTS `id_sharding_1`;
CREATE TABLE `id_sharding_1` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

