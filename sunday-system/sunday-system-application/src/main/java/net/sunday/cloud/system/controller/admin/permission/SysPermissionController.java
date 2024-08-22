package net.sunday.cloud.system.controller.admin.permission;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.sunday.cloud.base.common.entity.result.R;
import net.sunday.cloud.system.controller.admin.permission.vo.PermissionRouteRespVO;
import net.sunday.cloud.system.service.permission.ISysPermissionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限管理 前端控制器
 */
@Tag(name = "权限管理")
@RestController
@RequestMapping("/system/permission")
@AllArgsConstructor
public class SysPermissionController {

    private final ISysPermissionService permissionService;

    @GetMapping("/route/info")
    @Operation(summary = "获得当前登录用户的权限路由信息")
    public R<PermissionRouteRespVO> getPermissionRouteInfo() {

        return R.ok(permissionService.getPermissionRouteInfo());
    }
}
