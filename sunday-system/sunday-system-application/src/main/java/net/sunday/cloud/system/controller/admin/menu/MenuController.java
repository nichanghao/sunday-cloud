package net.sunday.cloud.system.controller.admin.menu;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import net.sunday.cloud.base.common.entity.page.PageResult;
import net.sunday.cloud.base.common.entity.result.R;
import net.sunday.cloud.base.common.enums.CommonStatusEnum;
import net.sunday.cloud.system.controller.admin.menu.vo.MenuListReqVO;
import net.sunday.cloud.system.controller.admin.menu.vo.MenuRespVO;
import net.sunday.cloud.system.controller.admin.menu.vo.MenuSimpleRespVO;
import net.sunday.cloud.system.controller.admin.menu.vo.MenuUpsertReqVO;
import net.sunday.cloud.system.service.menu.IMenuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统菜单 前端控制器
 */
@Tag(name = "菜单管理")
@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Resource
    private IMenuService menuService;

    @PostMapping("/add")
    @Operation(summary = "创建菜单")
    @PreAuthorize("")
    public R<Long> createMenu(@Valid @RequestBody MenuUpsertReqVO reqVO) {
        Long menuId = menuService.createMenu(reqVO);
        return R.ok(menuId);
    }

    @PutMapping("/update")
    @Operation(summary = "修改菜单")
    public R<Boolean> updateMenu(@Valid @RequestBody MenuUpsertReqVO updateReqVO) {
        Assert.notNull(updateReqVO.getId(), () -> "ID不能为空");
        menuService.updateMenu(updateReqVO);
        return R.ok(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除菜单")
    @Parameter(name = "id", description = "菜单编号", required = true, example = "1")
    public R<Boolean> deleteMenu(@RequestParam Long id) {
        menuService.deleteMenu(id);
        return R.ok(true);
    }

    @GetMapping("/page")
    @Operation(summary = "获取菜单分页列表", description = "用于【菜单管理】界面")
    public R<PageResult<MenuRespVO>> listMenuPage(MenuListReqVO reqVO) {
        return R.ok(menuService.listMenuPage(reqVO));
    }

    @GetMapping("/list-simple-tree")
    @Operation(summary = "获取精简信息菜单树", description = "只包含被开启的菜单，用于【角色分配菜单】功能的选项")
    public R<List<MenuSimpleRespVO>> listSimpleMenuTree() {

        return R.ok(menuService.listSimpleMenuTree(MenuListReqVO.builder()
                .status(CommonStatusEnum.ENABLE.ordinal())
                .build()));
    }
}
