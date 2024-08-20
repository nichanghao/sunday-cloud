package net.sunday.cloud.system.controller.admin.menu.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "菜单管理 - 菜单列表请求参数")
@Data
@Builder
public class MenuListReqVO {

    @Schema(description = "菜单名称，模糊匹配", example = "系统管理")
    private String name;

    @Schema(description = "状态(1:启用 0:禁用)", example = "1")
    private Integer status;

}
