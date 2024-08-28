package net.sunday.cloud.system.controller.admin.permission.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sunday.cloud.system.controller.admin.menu.vo.MenuRespVO;

import java.util.List;
import java.util.Set;

@Tag(name = "权限路由信息")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionRouteRespVO {

    public static final String HOME = "home";

    @Schema(description = "权限列表")
    private Set<String> permissions;

    @Schema(description = "路由列表")
    private List<MenuRespVO> routes;

    @Schema(description = "用户登录后跳转的路由")
    private String home;

}
