package net.sunday.cloud.system.api.permission;

import jakarta.annotation.Resource;
import net.sunday.cloud.system.service.permission.IPermissionService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.context.annotation.Primary;

/**
 * 权限相关 dubbo 服务接口实现
 */
@Primary
@DubboService
public class PermissionApiService implements PermissionApi {

    @Resource
    private IPermissionService permissionService;

    @Override
    public boolean hasAnyPermissions(Long userId, String... permissions) {
        return permissionService.hasAnyPermissions(userId, permissions);
    }
}
