package net.sunday.cloud.system.controller.admin.role;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import net.sunday.cloud.base.common.entity.page.PageResult;
import net.sunday.cloud.base.common.entity.result.R;
import net.sunday.cloud.base.common.enums.CommonStatusEnum;
import net.sunday.cloud.system.controller.admin.role.vo.RolePageReqVO;
import net.sunday.cloud.system.controller.admin.role.vo.RoleRespVO;
import net.sunday.cloud.system.controller.admin.role.vo.RoleUpsertReqVO;
import net.sunday.cloud.system.service.role.IRoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统角色 前端控制器
 */
@Tag(name = "角色管理")
@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Resource
    private IRoleService roleService;

    @PostMapping("/add")
    @Operation(summary = "创建角色")
    @PreAuthorize("@sf.hasPermission('sys:role:add')")
    public R<Long> createRole(@Valid @RequestBody RoleUpsertReqVO reqVO) {
        return R.ok(roleService.createRole(reqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "修改角色")
    @PreAuthorize("@sf.hasPermission('sys:role:edit')")
    public R<Boolean> updateRole(@Valid @RequestBody RoleUpsertReqVO updateReqVO) {
        Assert.notNull(updateReqVO.getId(), () -> "ID不能为空");
        roleService.updateRole(updateReqVO);
        return R.ok(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除角色")
    @PreAuthorize("@sf.hasPermission('sys:role:delete')")
    @Parameter(name = "id", description = "角色编号", required = true, example = "1")
    public R<Boolean> deleteRole(@RequestParam("id") Long id) {
        roleService.deleteRole(id);
        return R.ok(true);
    }

    @PostMapping("/page")
    @Operation(summary = "获得角色分页列表")
    @PreAuthorize("@sf.hasPermission('sys:role')")
    public R<PageResult<RoleRespVO>> getRolePage(@Valid @RequestBody RolePageReqVO pageReqVO) {
        return R.ok(roleService.getRolePage(pageReqVO));
    }

    @GetMapping("/list-all-simple")
    @Operation(summary = "获取角色精简信息列表", description = "只包含被开启的角色，主要用于分配角色时的下拉选项")
    public R<List<RoleRespVO>> getSimpleRoleList() {
        return R.ok(roleService.listSimpleRoleByStatus(CommonStatusEnum.ENABLE.ordinal()));
    }

}
