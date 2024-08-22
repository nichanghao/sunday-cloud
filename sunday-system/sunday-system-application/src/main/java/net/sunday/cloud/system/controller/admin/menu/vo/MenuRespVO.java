package net.sunday.cloud.system.controller.admin.menu.vo;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "菜单管理 - 菜单信息")
@Data
public class MenuRespVO {

    @Schema(description = "菜单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "菜单名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "系统管理")
    private String name;

    @Schema(description = "路由名")
    private String routeName;

    @Schema(description = "权限标识", example = "sys:menu:add")
    private String permission;

    @Schema(description = "菜单类型(1:目录,2:菜单,3:按钮)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer type;

    @Schema(description = "父菜单 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long parentId;

    @Schema(description = "路由地址,仅菜单类型为菜单或者目录时才有", example = "/system/user")
    private String path;

    @Schema(description = "组件，仅菜单类型为菜单或者目录时才有")
    private String component;

    @Schema(description = "状态(1:启用 0:禁用)", example = "1")
    private Integer status;

    @Schema(description = "菜单元数据信息，json格式", example = "{\"icon\":\"el-icon-s-tools\"}")
    private JsonNode meta;

    @Schema(description = "子菜单列表")
    private List<MenuRespVO> children;

}
