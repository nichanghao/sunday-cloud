package net.sunday.cloud.system.controller.admin.permission;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.sunday.cloud.base.common.entity.result.R;
import net.sunday.cloud.system.controller.admin.permission.vo.PermissionAssignRoleMenuReqVO;
import net.sunday.cloud.system.controller.admin.permission.vo.PermissionAssignUserRoleReqVO;
import net.sunday.cloud.system.controller.admin.permission.vo.PermissionRouteRespVO;
import net.sunday.cloud.system.service.permission.IPermissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * 权限管理 前端控制器
 */
@Tag(name = "权限管理")
@RestController
@RequestMapping("/system/permission")
@AllArgsConstructor
public class PermissionController {

    private final IPermissionService permissionService;

    @GetMapping("/route/info")
    @Operation(summary = "获得当前登录用户的权限路由信息")
    public R<PermissionRouteRespVO> getPermissionRouteInfo() {

        return R.ok(permissionService.getPermissionRouteInfo());
    }

    @Operation(summary = "赋予用户角色")
    @PutMapping("/assign-user-role")
    @PreAuthorize("@sf.hasPermission('sys:user:assignRoles')")
    public R<Boolean> assignUserRole(@Validated @RequestBody PermissionAssignUserRoleReqVO reqVO) {
        permissionService.assignUserRole(reqVO.getUserId(), reqVO.getRoleIds());
        return R.ok(true);
    }

    @Operation(summary = "获得角色拥有的菜单编号")
    @Parameter(name = "roleId", description = "角色编号", required = true)
    @GetMapping("/list-role-menus")
    public R<Set<Long>> listMenuIdsByRoleId(@RequestParam Long roleId) {
        return R.ok(permissionService.listMenuIdsByRoleId(roleId));
    }

    @PutMapping("/assign-role-menu")
    @Operation(summary = "赋予角色菜单")
    @PreAuthorize("@sf.hasPermission('sys:role:assignMenus')")
    public R<Boolean> assignRoleMenu(@Validated @RequestBody PermissionAssignRoleMenuReqVO reqVO) {
        permissionService.assignRoleMenu(reqVO.getRoleId(), reqVO.getMenuIds());
        return R.ok(true);
    }
}
