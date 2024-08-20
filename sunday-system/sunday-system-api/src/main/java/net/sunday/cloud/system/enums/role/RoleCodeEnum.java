package net.sunday.cloud.system.enums.role;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 角色标识枚举
 */
@Getter
@AllArgsConstructor
public enum RoleCodeEnum {

    /**
     * 超级管理员
     */
    SUPER_ADMIN("super_admin"),
    ;

    /**
     * 角色编码
     */
    private final String code;

    public static boolean isSuperAdmin(String code) {
        return Objects.equals(code, SUPER_ADMIN.getCode());
    }

}
