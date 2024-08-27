/*
 Navicat Premium Data Transfer

 Source Server         : mysql@localhost
 Source Server Type    : MySQL
 Source Server Version : 80200 (8.2.0)
 Source Host           : localhost:3306
 Source Schema         : sunday

 Target Server Type    : MySQL
 Target Server Version : 80200 (8.2.0)
 File Encoding         : 65001

 Date: 23/08/2024 13:14:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `route_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '路由名称',
  `type` tinyint NULL DEFAULT NULL COMMENT '菜单类型(1:目录,2:菜单,3:按钮)',
  `permission` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '权限标识',
  `path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '路由地址',
  `component` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '组件',
  `parent_id` bigint UNSIGNED NULL DEFAULT 0 COMMENT '父菜单ID',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '菜单状态(0:禁用,1:启用)',
  `meta` json NULL COMMENT '路由元数据',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  `updater` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` bit(1) NULL DEFAULT b'0' COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '首页', 'home', 1, '', '/home', 'layout.base$view.home', 0, b'1', '{\"icon\": \"mdi:monitor-dashboard\", \"order\": 1, \"i18nKey\": \"route.home\"}', '2024-07-25 10:47:55.157', '1', b'0');
INSERT INTO `sys_menu` VALUES (2, '系统管理', 'system', 1, '', '/system', 'layout.base', 0, b'1', '{\"icon\": \"carbon:cloud-service-management\", \"order\": 2, \"i18nKey\": \"route.system\"}', '2024-07-25 10:50:04.754', '1', b'0');
INSERT INTO `sys_menu` VALUES (3, '用户管理', 'system_user', 2, 'sys:user', '/system/user', 'view.system_user', 2, b'1', '{\"icon\": \"ic:round-manage-accounts\", \"order\": 1, \"i18nKey\": \"route.system_user\"}', '2024-07-31 13:06:24.526', '1', b'0');
INSERT INTO `sys_menu` VALUES (4, '新增用户', '', 3, 'sys:user:add', '', '', 3, b'1', '{\"order\": 0, \"i18nKey\": null}', '2024-07-30 16:31:15.887', '1', b'0');
INSERT INTO `sys_menu` VALUES (5, '编辑用户', '', 3, 'sys:user:edit', '', '', 3, b'1', '{\"order\": 1, \"i18nKey\": null}', '2024-07-30 16:58:40.209', '1', b'0');
INSERT INTO `sys_menu` VALUES (6, '分配角色', '', 3, 'sys:user:assignRoles', '', '', 3, b'1', '{\"order\": 2, \"i18nKey\": null}', '2024-07-30 17:02:26.964', '1', b'0');
INSERT INTO `sys_menu` VALUES (7, '重置密码', '', 3, 'sys:user:resetPwd', '', '', 3, b'1', '{\"order\": 4, \"i18nKey\": null}', '2024-07-30 17:04:57.943', '1', b'0');
INSERT INTO `sys_menu` VALUES (8, '删除用户', '', 3, 'sys:user:delete', '', '', 3, b'1', '{\"order\": 5, \"i18nKey\": null}', '2024-07-30 17:05:44.823', '1', b'0');
INSERT INTO `sys_menu` VALUES (9, '角色管理', 'system_role', 2, 'sys:role', '/system/role', 'view.system_role', 2, b'1', '{\"icon\": \"carbon:user-role\", \"order\": 2, \"i18nKey\": \"route.system_role\"}', '2024-07-31 13:12:02.585', '1', b'0');
INSERT INTO `sys_menu` VALUES (10, '新增角色', '', 3, 'sys:role:add', '', '', 9, b'1', '{\"order\": 0, \"i18nKey\": null}', '2024-07-30 17:14:30.884', '1', b'0');
INSERT INTO `sys_menu` VALUES (11, '编辑角色', '', 3, 'sys:role:edit', '', '', 9, b'1', '{\"order\": 1, \"i18nKey\": null}', '2024-07-30 17:15:13.891', '1', b'0');
INSERT INTO `sys_menu` VALUES (12, '分配权限', '', 3, 'sys:role:assignMenus', '', '', 9, b'1', '{\"order\": 0, \"i18nKey\": null}', '2024-07-30 17:17:27.685', '1', b'0');
INSERT INTO `sys_menu` VALUES (13, '删除角色', '', 3, 'sys:role:delete', '', '', 9, b'1', '{\"order\": 0, \"i18nKey\": null}', '2024-07-30 17:18:07.425', '1', b'0');
INSERT INTO `sys_menu` VALUES (14, '菜单管理', 'system_menu', 2, 'sys:menu', '/system/menu', 'view.system_menu', 2, b'1', '{\"icon\": \"material-symbols:route\", \"order\": 3, \"i18nKey\": \"route.system_menu\"}', '2024-07-31 13:04:19.701', '1', b'0');
INSERT INTO `sys_menu` VALUES (15, '新增菜单', '', 3, 'sys:menu:add', '', '', 14, b'1', '{\"order\": 0, \"i18nKey\": null}', '2024-07-30 17:19:10.776', '1', b'0');
INSERT INTO `sys_menu` VALUES (16, '编辑菜单', '', 3, 'sys:menu:edit', '', '', 14, b'1', '{\"order\": 0, \"i18nKey\": null}', '2024-07-30 17:19:35.597', '1', b'0');
INSERT INTO `sys_menu` VALUES (17, '删除菜单', '', 3, 'sys:menu:delete', '', '', 14, b'1', '{\"order\": 0, \"i18nKey\": null}', '2024-07-30 17:19:58.259', '1', b'0');
INSERT INTO `sys_menu` VALUES (18, '关于', 'about', 1, '', '/about', 'layout.base$view.about', 0, b'1', '{\"icon\": \"fluent:book-information-24-regular\", \"order\": 10, \"i18nKey\": \"route.about\"}', '2024-07-31 11:18:37.165', '1', b'0');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色名',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色标识',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态(1:启用 0:禁用)',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  `updater` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` bit(1) NULL DEFAULT b'0' COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'super_admin', 1, '超级管理员', '2024-07-29 15:56:36.859', NULL, b'0');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint UNSIGNED NOT NULL COMMENT '角色ID',
  `menu_id` bigint UNSIGNED NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE,
  INDEX `idx_role_id`(`role_id` ASC) USING BTREE,
  INDEX `fk_sys_role_menu_sys_menu`(`menu_id` ASC) USING BTREE,
  CONSTRAINT `fk_sys_role_menu_sys_menu` FOREIGN KEY (`menu_id`) REFERENCES `sys_menu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_sys_role_menu_sys_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色菜单关联' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1);
INSERT INTO `sys_role_menu` VALUES (1, 2);
INSERT INTO `sys_role_menu` VALUES (1, 3);
INSERT INTO `sys_role_menu` VALUES (1, 4);
INSERT INTO `sys_role_menu` VALUES (1, 5);
INSERT INTO `sys_role_menu` VALUES (1, 6);
INSERT INTO `sys_role_menu` VALUES (1, 7);
INSERT INTO `sys_role_menu` VALUES (1, 8);
INSERT INTO `sys_role_menu` VALUES (1, 9);
INSERT INTO `sys_role_menu` VALUES (1, 10);
INSERT INTO `sys_role_menu` VALUES (1, 11);
INSERT INTO `sys_role_menu` VALUES (1, 12);
INSERT INTO `sys_role_menu` VALUES (1, 13);
INSERT INTO `sys_role_menu` VALUES (1, 14);
INSERT INTO `sys_role_menu` VALUES (1, 15);
INSERT INTO `sys_role_menu` VALUES (1, 16);
INSERT INTO `sys_role_menu` VALUES (1, 17);
INSERT INTO `sys_role_menu` VALUES (1, 18);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户登录名',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户登录密码',
  `nickname` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `gender` tinyint NULL DEFAULT NULL COMMENT '性别(1:男,2:女)',
  `phone` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` tinyint NULL DEFAULT 1 COMMENT '用户状态(1:正常,0:停用)',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  `updater` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` bit(1) NULL DEFAULT b'0' COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sys_user_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'sunday', '$2a$08$tp4yMH0uP5t58x6hajRewu1GOvEZkfideFy7N6rBeQ8KPiwy33dJW', '周末', 1, '13800138000', 'sunday@sunday.net', 1, '2024-08-21 09:59:31.220', '1', b'0');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint UNSIGNED NOT NULL COMMENT '用户ID',
  `role_id` bigint UNSIGNED NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`role_id`, `user_id`) USING BTREE,
  INDEX `fk_sys_user_role_sys_user`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fk_sys_user_role_sys_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_sys_user_role_sys_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户角色关联' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);

SET FOREIGN_KEY_CHECKS = 1;
