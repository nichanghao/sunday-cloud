package net.sunday.cloud.base.security.permission;

import net.sunday.cloud.base.security.util.SecurityFrameworkUtils;

/**
 * security框架权限验证器
 */

public class SecurityFrameworkPermissionEvaluator {


    /**
     * 判断是否有权限，任一一个即可
     *
     * @param permissions 权限
     * @return 是否
     */
    public boolean hasAnyPermissions(String... permissions) {
        // 1. 获取当前登录的用户
        Long authUserId = SecurityFrameworkUtils.getAuthUserId();
        if (authUserId == null) {
            return false;
        }

        // 2. 判断是否有权限
        return false;

    }
}
