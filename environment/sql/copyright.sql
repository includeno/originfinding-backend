USE manage;
DROP TABLE IF EXISTS `copyright`;
CREATE TABLE `copyright`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',

  `url` varchar(200) NULL DEFAULT NULL COMMENT '唯一网页地址',
  `request_id` bigint(20) NULL DEFAULT NULL COMMENT '关联请求ID -1为取消绑定',
  `audit_id` bigint(20) NULL DEFAULT NULL COMMENT '审核用户ID',

  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `version` int NOT NULL DEFAULT '1' COMMENT '乐观锁',

  PRIMARY KEY (`id`) USING BTREE,
  INDEX `url_index`(`url`) USING BTREE COMMENT 'url Index'
) ENGINE = InnoDB CHARSET=utf8mb4 COMMENT = '版权记录表';

DROP TABLE IF EXISTS `copyright_commit`;
CREATE TABLE `copyright_commit`  (
     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
     `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
     `url` varchar(200) NULL DEFAULT NULL COMMENT '网页地址',
     `platform` varchar(1000) DEFAULT NULL COMMENT '平台',
     `platform_hash` varchar(1000) DEFAULT NULL COMMENT '平台hash',
     `comment` varchar(1000) DEFAULT NULL COMMENT '用户留言',
     `status` int NOT NULL DEFAULT '0' COMMENT '版权记录 状态 0待审核 1已通过审核 -1未通过审核 非0状态下普票用户无法修改',
     `audit_id` bigint(20) NULL DEFAULT NULL COMMENT '审核员用户ID',

     `create_time` datetime DEFAULT NULL COMMENT '创建时间',
     `update_time` datetime DEFAULT NULL COMMENT '修改时间',
     `version` int NOT NULL DEFAULT '1' COMMENT '乐观锁',
     `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '逻辑删除:0=未删除,1=已删除',

     PRIMARY KEY (`id`) USING BTREE,
     INDEX `url_index`(`url`) USING BTREE COMMENT 'url Index'
) ENGINE = InnoDB CHARSET=utf8mb4 COMMENT = '版权信息提交记录表';

DROP TABLE IF EXISTS `copyright_commit_log`;
CREATE TABLE `copyright_commit_log`  (
 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
 `copyright_commit_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'copyright_commit表ID',
 `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
 `url` varchar(200) NULL DEFAULT NULL COMMENT '网页地址',
 `platform` varchar(1000) DEFAULT NULL COMMENT '平台',
 `platform_hash` varchar(1000) DEFAULT NULL COMMENT '平台hash',
 `comment` varchar(1000) DEFAULT NULL COMMENT '用户留言',
 `status` int NOT NULL DEFAULT '0' COMMENT '版权记录 状态 0待审核 1已通过审核 -1未通过审核 非0状态下普票用户无法修改',
 `audit_id` bigint(20) NULL DEFAULT NULL COMMENT '审核员用户ID',

 `create_time` datetime DEFAULT NULL COMMENT '创建时间',
 `update_time` datetime DEFAULT NULL COMMENT '修改时间',
 `version` int NOT NULL DEFAULT '1' COMMENT '乐观锁',
 `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '逻辑删除:0=未删除,1=已删除',

 PRIMARY KEY (`id`) USING BTREE,
 INDEX `url_index`(`url`) USING BTREE COMMENT 'url Index'
) ENGINE = InnoDB CHARSET=utf8mb4 COMMENT = '版权信息提交日志表';