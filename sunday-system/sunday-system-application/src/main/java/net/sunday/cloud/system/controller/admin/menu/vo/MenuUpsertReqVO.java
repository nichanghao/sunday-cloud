package net.sunday.cloud.system.controller.admin.menu.vo;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "菜单管理 - 创建/修改请求参数")
@Data
public class MenuUpsertReqVO {

    @Schema(description = "菜单编号", example = "1")
    private Long id;

    @Schema(description = "菜单名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "系统管理")
    @NotBlank(message = "菜单名称不能为空")
    @Size(max = 50, message = "菜单名称长度不能超过50个字符")
    private String name;

    @Schema(description = "路由名，仅菜单类型为菜单或者目录时才有")
    private String routeName;

    @Schema(description = "权限标识", example = "sys:menu:add")
    @Size(max = 100)
    private String permission;

    @Schema(description = "菜单类型(1:目录,2:菜单,3:按钮)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "菜单类型不能为空")
    private Integer type;

    @Schema(description = "父菜单 ID，顶级菜单为 0", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "父菜单 ID 不能为空")
    private Long parentId;

    @Schema(description = "路由地址,仅菜单类型为菜单或者目录时才有", example = "post")
    @Size(max = 200, message = "路由地址不能超过200个字符")
    private String path;

    @Schema(description = "组件，仅菜单类型为菜单或者目录时才有")
    private String component;

    @Schema(description = "状态(1:启用 0:禁用)", example = "1")
    private Integer status;

    @Schema(description = "菜单元数据信息，json格式", example = "{\"icon\":\"el-icon-s-tools\"}")
    private JsonNode meta;

}
