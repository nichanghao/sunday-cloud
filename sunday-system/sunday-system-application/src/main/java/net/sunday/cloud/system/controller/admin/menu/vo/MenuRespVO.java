package net.sunday.cloud.system.controller.admin.menu.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "菜单管理 - 菜单信息")
@Data
public class MenuRespVO {

    @Schema(description = "菜单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "菜单名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "系统管理")
    private String name;

    @Schema(description = "权限标识", example = "sys:menu:add")
    private String permission;

    @Schema(description = "菜单类型(1:目录,2:菜单,3:按钮)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer type;

    @Schema(description = "显示顺序", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer sort;

    @Schema(description = "父菜单 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long parentId;

    @Schema(description = "路由地址,仅菜单类型为菜单或者目录时才有", example = "post")
    private String path;

    @Schema(description = "组件路径", example = "system/post/index")
    private String component;

    @Schema(description = "组件名", example = "SystemUser")
    private String componentName;

    @Schema(description = "状态(1:启用 0:禁用)", example = "1")
    private Integer status;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "时间戳格式")
    private LocalDateTime updateTime;

}
