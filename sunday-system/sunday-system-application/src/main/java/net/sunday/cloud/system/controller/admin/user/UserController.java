package net.sunday.cloud.system.controller.admin.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import net.sunday.cloud.base.common.entity.page.PageResult;
import net.sunday.cloud.base.common.entity.result.R;
import net.sunday.cloud.base.common.entity.vo.UpdateStatusReqVO;
import net.sunday.cloud.system.controller.admin.user.vo.UserPageReqVO;
import net.sunday.cloud.system.controller.admin.user.vo.UserResetPasswordReqVO;
import net.sunday.cloud.system.controller.admin.user.vo.UserRespVO;
import net.sunday.cloud.system.controller.admin.user.vo.UserUpsertReqVO;
import net.sunday.cloud.system.service.user.IUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;


/**
 * 系统用户 前端控制器
 */
@RestController
@RequestMapping("/system/user")
@Tag(name = "用户管理")
public class UserController {

    @Resource
    private IUserService userService;

    @PostMapping("/add")
    @Operation(summary = "新增用户")
    @PreAuthorize("@sf.hasPermission('sys:user:add')")
    public R<Long> addUser(@Valid @RequestBody UserUpsertReqVO upsertVO) {
        Assert.notNull(upsertVO.getPassword(), () -> "密码不能为空");
        Long id = userService.addUser(upsertVO);
        return R.ok(id);
    }

    @PutMapping("update")
    @Operation(summary = "修改用户")
    @PreAuthorize("@sf.hasPermission('sys:user:edit')")
    public R<Boolean> updateUser(@Valid @RequestBody UserUpsertReqVO upsertVO) {
        Assert.notNull(upsertVO.getId(), () -> "ID不能为空");
        userService.updateUser(upsertVO);
        return R.ok(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除用户")
    @PreAuthorize("@sf.hasPermission('sys:user:delete')")
    @Parameter(name = "id", description = "编号", required = true, example = "1")
    public R<Boolean> deleteUser(@RequestParam Long id) {
        userService.deleteUser(id);
        return R.ok(true);
    }

    @PutMapping("/reset-password")
    @Operation(summary = "重置用户密码")
    @PreAuthorize("@sf.hasPermission('sys:user:resetPwd')")
    public R<Boolean> resetUserPassword(@Valid @RequestBody UserResetPasswordReqVO reqVO) {
        userService.resetUserPassword(reqVO.getId(), reqVO.getPassword());
        return R.ok(true);
    }

    @PutMapping("/update-status")
    @Operation(summary = "修改用户状态")
    public R<Boolean> updateUserStatus(@Valid @RequestBody UpdateStatusReqVO reqVO) {
        userService.updateUserStatus(reqVO.getId(), reqVO.getStatus());
        return R.ok(true);
    }

    @PostMapping("/page")
    @Operation(summary = "获得用户分页列表")
    @PreAuthorize("@sf.hasPermission('sys:user')")
    public R<PageResult<UserRespVO>> getUserPage(@Valid @RequestBody UserPageReqVO pageReqVO) {

        return R.ok(userService.getUserPage(pageReqVO));
    }

    @GetMapping("/self/info")
    @Operation(summary = "获得当前登录的用户信息")
    public R<UserRespVO> getUserSelfInfo() {

        return R.ok(userService.getUserSelfInfo());
    }

}
