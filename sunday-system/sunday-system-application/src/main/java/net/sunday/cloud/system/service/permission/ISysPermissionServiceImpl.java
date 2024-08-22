package net.sunday.cloud.system.service.permission;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.AllArgsConstructor;
import net.sunday.cloud.base.common.util.collection.CollectionUtils;
import net.sunday.cloud.base.security.util.SecurityFrameworkUtils;
import net.sunday.cloud.system.controller.admin.menu.vo.MenuRespVO;
import net.sunday.cloud.system.controller.admin.permission.vo.PermissionRouteRespVO;
import net.sunday.cloud.system.enums.menu.MenuTypeEnum;
import net.sunday.cloud.system.model.SysMenuDO;
import net.sunday.cloud.system.model.SysRoleMenuDO;
import net.sunday.cloud.system.model.SysUserRoleDO;
import net.sunday.cloud.system.repository.mapper.SysUserRoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限管理 服务实现层
 */
@Service
@AllArgsConstructor
public class ISysPermissionServiceImpl implements ISysPermissionService {

    private final SysUserRoleMapper sysUserRoleMapper;

    @Override
    public PermissionRouteRespVO getPermissionRouteInfo() {
        // 1.获取当前登录用户ID
        Long authUserId = SecurityFrameworkUtils.getAuthUserId();
        if (authUserId == null) {
            return PermissionRouteRespVO.EMPTY;
        }

        // 2.连表查询当前用户拥有的所有菜单
        MPJLambdaWrapper<SysUserRoleDO> wrapper = new MPJLambdaWrapper<SysUserRoleDO>()
                .selectAll(SysMenuDO.class)
                .leftJoin(SysRoleMenuDO.class, SysRoleMenuDO::getRoleId, SysUserRoleDO::getRoleId)
                .leftJoin(SysMenuDO.class, SysMenuDO::getId, SysRoleMenuDO::getMenuId)
                .eq(SysUserRoleDO::getUserId, authUserId);
        List<MenuRespVO> menuList = sysUserRoleMapper.selectJoinList(MenuRespVO.class, wrapper);
        if (CollectionUtils.isEmpty(menuList)) {
            return PermissionRouteRespVO.EMPTY;
        }

        // 3.构建路由信息
        List<MenuRespVO> routes = new ArrayList<>();
        Set<String> permissions = new HashSet<>();
        this.buildPermissionRouteTree(routes, permissions, menuList);

        return PermissionRouteRespVO.builder()
                .routes(routes)
                .permissions(permissions)
                .home(PermissionRouteRespVO.HOME)
                .build();
    }

    private void buildPermissionRouteTree(List<MenuRespVO> routes, Set<String> permissions, List<MenuRespVO> menuList) {
        Map<Long, MenuRespVO> menuMap = menuList.stream().collect(Collectors.toMap(MenuRespVO::getId, v -> v));

        menuList.forEach(menu -> {
            if (StringUtils.hasText(menu.getPermission())) {
                permissions.add(menu.getPermission());
            }
            // 特殊处理，前端根据routeName来缓存路由信息
            menu.setName(menu.getRouteName());

            if (Objects.equals(menu.getParentId(), SysMenuDO.ID_ROOT)) {
                routes.add(menu);
            } else if (!Objects.equals(menu.getType(), MenuTypeEnum.BUTTON.getType())) {
                MenuRespVO menuRespVO = menuMap.get(menu.getParentId());
                if (menuRespVO.getChildren() == null) {
                    menuRespVO.setChildren(new ArrayList<>());
                }
                menuRespVO.getChildren().add(menu);
            }
        });
    }
}
