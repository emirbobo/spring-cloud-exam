CREATE DATABASE IF NOT EXISTS spcl CHARACTER SET utf8 COLLATE utf8_general_ci;
GRANT ALL ON spcl.* TO 'spcl'@'localhost' identified by 'rL_giTjZmsrC7kHpXoMz';
flush privileges;
CREATE TABLE `record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`,`name`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
