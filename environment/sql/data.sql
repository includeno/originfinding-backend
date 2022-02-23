Use demo;
DROP TABLE IF EXISTS `data`;
CREATE TABLE `data` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `content` mediumtext,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;