package net.sunday.cloud.system.service.permission;

import net.sunday.cloud.system.controller.admin.permission.vo.PermissionRouteRespVO;

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
}
