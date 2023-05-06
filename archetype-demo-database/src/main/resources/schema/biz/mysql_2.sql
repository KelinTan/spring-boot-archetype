DROP TABLE IF EXISTS `year_sharding_2020`;

CREATE TABLE `year_sharding_2020` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sharding_time` varchar(20) COLLATE utf8mb4_bin NOT NULL,
  `data` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

DROP TABLE IF EXISTS `year_sharding_2021`;

CREATE TABLE `year_sharding_2021` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sharding_time` varchar(20) COLLATE utf8mb4_bin NOT NULL,
  `data` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;