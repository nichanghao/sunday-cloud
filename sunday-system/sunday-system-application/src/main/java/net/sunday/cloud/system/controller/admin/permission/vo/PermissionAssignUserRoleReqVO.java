package net.sunday.cloud.system.controller.admin.permission.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Collections;
import java.util.Set;

@Schema(description = "赋予用户角色请求参数")
@Data
public class PermissionAssignUserRoleReqVO {

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "用户编号不能为空")
    private Long userId;

    @Schema(description = "角色编号列表", example = "1,3,5")
    private Set<Long> roleIds = Collections.emptySet();

}
