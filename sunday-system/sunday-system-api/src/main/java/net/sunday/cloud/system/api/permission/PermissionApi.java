package net.sunday.cloud.system.api.permission;

public interface PermissionApi {

    /**
     * 判断用户是否有仁义一个权限
     *
     * @param userId      用户ID
     * @param permissions 权限列表
     * @return 是否有权限
     */
    boolean hasAnyPermissions(Long userId, String... permissions);
}
