USE manage;
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(100) NOT NULL DEFAULT '' COMMENT '用户名称',
  `password` varchar(100) NOT NULL DEFAULT '' COMMENT '用户密码',
  `salt` varchar(64) DEFAULT NULL COMMENT '密码加盐',
  `email` varchar(100) NOT NULL DEFAULT '' COMMENT '用户邮箱',

  `create_time` datetime NOT NULL COMMENT '用户创建时间',
  `update_time` datetime NOT NULL COMMENT '用户修改时间',
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '逻辑删除:0=未删除,1=已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
-- https://zhuanlan.zhihu.com/p/97035035

CREATE TABLE `role`  (
 `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
 `code` varchar(100)  DEFAULT NULL COMMENT '角色唯一CODE代码',
 `rolename` varchar(100)  DEFAULT NULL COMMENT '角色名称',

 `create_time` datetime NOT NULL COMMENT '角色创建时间',
 `update_time` datetime NOT NULL COMMENT '角色修改时间',
 `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '逻辑删除:0=未删除,1=已删除',
 PRIMARY KEY (`role_id`) USING BTREE,
 INDEX `code_index`(`code`) USING BTREE COMMENT '权限CODE代码 index'
) ENGINE = InnoDB COMMENT = '角色';

DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
   `permission_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '权限ID',
   `operation` varchar(100)  COMMENT '操作代码 存放数组[add,delete,list]',
   `permissionname` varchar(100)  COMMENT '权限英文名称 user:form,user:list,menu',

   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
   `update_time` datetime DEFAULT NULL COMMENT '修改时间',
   `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '逻辑删除:0=未删除,1=已删除',
   PRIMARY KEY (`permission_id`) USING BTREE,
   INDEX `code`(`code`) USING BTREE COMMENT '权限CODE代码 Index'
) ENGINE = InnoDB CHARSET=utf8mb4 COMMENT = '权限';

DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',

  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '逻辑删除:0=未删除,1=已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_index`(`user_id`) USING BTREE COMMENT '用户ID Index',
  INDEX `role_index`(`role_id`) USING BTREE COMMENT '角色ID Index',
  CONSTRAINT `user_role_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `user_role_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARSET=utf8mb4 COMMENT = '用户角色';

CREATE TABLE `role_permission`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
    `permission_id` bigint(20) NULL DEFAULT NULL COMMENT '权限ID',

    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '修改时间',
    `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '逻辑删除:0=未删除,1=已删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `role_index`(`role_id`) USING BTREE COMMENT '角色ID Index',
    INDEX `permission_index`(`permission_id`) USING BTREE COMMENT '权限ID Index',
    CONSTRAINT `role_permission_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `role_permission_ibfk_2` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`permission_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARSET=utf8mb4 COMMENT = '角色权限';