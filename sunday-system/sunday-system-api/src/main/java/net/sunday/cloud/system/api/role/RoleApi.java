package net.sunday.cloud.system.api.role;

/**
 * 系统角色API接口
 */

public interface RoleApi {

    /**
     * 角色状态缓存失效
     *
     * @param roleId 角色ID
     */
    void roleStatusCacheEvict(Long roleId);
}
