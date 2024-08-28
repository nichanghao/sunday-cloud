package net.sunday.cloud.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.sunday.cloud.base.common.exception.ErrorEnum;

/**
 * System响应码枚举类
 * system 系统，使用 100_000_000 段
 */

@Getter
@AllArgsConstructor
public enum SystemRespCodeEnum implements ErrorEnum {

    /* 用户相关 100_001_000 */
    USER_NOT_EXISTS(100_001_001, "用户不存在"),

    USER_USERNAME_EXISTS(100_001_002, "用户账号已经存在"),

    USER_MOBILE_EXISTS(100_001_003, "手机号已经存在"),

    USER_EMAIL_EXISTS(100_001_004, "邮箱已经存在"),

    USER_BUILT_IN_NOT_OPERATE(100_001_005, "内置用户不允许此操作"),


    /* 角色相关 100_002_000 */
    ROLE_CODE_EXISTS(100_002_001, "角色编码已存在"),

    ROLE_NAME_EXISTS(100_002_002, "角色名称已存在"),

    ROLE_NOT_EXISTS(100_002_003, "角色不存在"),

    ROLE_BE_USED_BY_USER(100_002_004, "角色已被用户使用"),

    ROLE_BUILT_IN_NOT_OPERATE(100_002_005, "内置角色不允许此操作"),


    /* 菜单相关 100_003_000 */
    MENU_PARENT_NOT_SELF(100_003_001, "不能设置自己为父菜单"),

    MENU_PARENT_NOT_EXISTS(100_003_002, "父菜单不存在"),

    MENU_PARENT_NOT_DIR_OR_MENU(100_003_003, "父菜单的类型必须是目录或者菜单"),

    MENU_NAME_EXISTS(100_003_004, "已经存在该名字的菜单"),

    MENU_NOT_EXISTS(100_003_005, "菜单不存在"),

    MENU_EXISTS_CHILDREN(100_003_006, "存在子菜单，无法操作"),


    /* 权限相关 100_004_000 */
    PERMISSION_UNASSIGNED_MENU(100_004_001, "请联系管理员分配权限"),
    ;


    private final Integer code;

    private final String message;


}
