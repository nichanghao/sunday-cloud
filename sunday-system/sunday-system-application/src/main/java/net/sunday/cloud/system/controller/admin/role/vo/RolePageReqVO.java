package net.sunday.cloud.system.controller.admin.role.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.sunday.cloud.base.common.entity.page.PageParam;


@Schema(description = "角色管理 - 角色分页查询条件")
@Data
@EqualsAndHashCode(callSuper = true)
public class RolePageReqVO extends PageParam {

    @Schema(description = "角色名称，模糊匹配")
    private String name;

    @Schema(description = "角色标识，模糊匹配")
    private String code;

    @Schema(description = "状态(1:启用 0:禁用)", example = "1")
    @Min(value = 0, message = "状态值必须大于等于0")
    @Max(value = 1, message = "状态值必须小于等于1")
    private Byte status;

}
