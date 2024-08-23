package net.sunday.cloud.system.controller.admin.menu.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "菜单管理 - 菜单精简信息")
@Data
public class MenuSimpleRespVO {

    @Schema(description = "菜单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "菜单名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "系统管理")
    private String name;

    @Schema(description = "父菜单 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long parentId;

    @Schema(description = "菜单类型(1:目录,2:菜单,3:按钮)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer type;

    @Schema(description = "子菜单列表")
    private List<MenuSimpleRespVO> children;

}
