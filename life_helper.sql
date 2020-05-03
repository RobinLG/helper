DROP TABLE IF EXISTS `t_picture`;
CREATE TABLE `t_picture` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id of picture',
    `thumbnail` blob NOT NULL COMMENT 'for preview pictures',
    `picture` mediumblob NOT NULL COMMENT 'for download',
    `time` datetime NOT NULL COMMENT 'time to save the picture',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB CHARSET=utf8
