package net.sunday.cloud.system.controller.admin.menu.vo;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Schema(description = "菜单管理 - 菜单信息")
@Data
public class MenuRespVO extends MenuSimpleRespVO {

    @Schema(description = "路由名")
    private String routeName;

    @Schema(description = "权限标识", example = "sys:menu:add")
    private String permission;

    @Schema(description = "路由地址,仅菜单类型为菜单或者目录时才有", example = "/system/user")
    private String path;

    @Schema(description = "组件，仅菜单类型为菜单或者目录时才有")
    private String component;

    @Schema(description = "状态(1:启用 0:禁用)", example = "1")
    private Integer status;

    @Schema(description = "菜单元数据信息，json格式", example = "{\"icon\":\"el-icon-s-tools\"}")
    private JsonNode meta;


}
