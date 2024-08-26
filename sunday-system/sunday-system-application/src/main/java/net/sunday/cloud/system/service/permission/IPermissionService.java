package net.sunday.cloud.system.service.permission;

import net.sunday.cloud.system.controller.admin.permission.vo.PermissionRouteRespVO;

import java.util.Set;

/**
 * 权限管理 服务接口层
 */
public interface IPermissionService {

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


    /**
     * 赋予角色菜单
     *
     * @param roleId  角色编号
     * @param menuIds 菜单编号集合
     */
    void assignRoleMenu(Long roleId, Set<Long> menuIds);

    /**
     * 判断用户是否有仁义一个权限
     *
     * @param userId      用户ID
     * @param permissions 权限列表
     * @return 是否有权限
     */
    boolean hasAnyPermissions(Long userId, String... permissions);
}
