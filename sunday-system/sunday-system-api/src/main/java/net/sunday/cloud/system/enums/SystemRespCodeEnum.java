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


    /* 角色相关 100_002_000 */
    ROLE_CODE_EXISTS(100_002_001, "角色编码已存在"),

    ROLE_NAME_EXISTS(100_002_002, "角色名称已存在"),

    ROLE_NOT_EXISTS(100_002_003, "角色不存在"),

    ROLE_BE_USED_BY_USER(100_002_004, "角色已被用户使用")
    ;


    private final Integer code;

    private final String message;


}
