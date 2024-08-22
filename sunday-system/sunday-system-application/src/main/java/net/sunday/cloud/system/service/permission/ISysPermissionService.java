package net.sunday.cloud.system.service.permission;

import net.sunday.cloud.system.controller.admin.permission.vo.PermissionRouteRespVO;

import java.util.Set;

/**
 * 权限管理 服务接口层
 */
public interface ISysPermissionService {

    /**
     * 获得当前登录用户的权限路由信息
     *
     * @return 当前登录用户的权限路由信息
     */
    PermissionRouteRespVO getPermissionRouteInfo();

    /**
     * 设置用户角色
     *
     * @param userId  角色编号
     * @param roleIds 角色编号集合
     */
    void assignUserRole(Long userId, Set<Long> roleIds);
}
