package net.sunday.cloud.system.service.permission;

import cn.hutool.core.collection.CollUtil;
import jakarta.annotation.Resource;
import net.sunday.cloud.base.common.exception.BusinessException;
import net.sunday.cloud.base.common.exception.GlobalRespCodeEnum;
import net.sunday.cloud.base.common.util.collection.CollectionUtils;
import net.sunday.cloud.base.security.util.SecurityFrameworkUtils;
import net.sunday.cloud.system.controller.admin.menu.vo.MenuRespVO;
import net.sunday.cloud.system.controller.admin.permission.vo.PermissionRouteRespVO;
import net.sunday.cloud.system.enums.menu.MenuTypeEnum;
import net.sunday.cloud.system.model.MenuDO;
import net.sunday.cloud.system.model.RoleMenuDO;
import net.sunday.cloud.system.model.UserRoleDO;
import net.sunday.cloud.system.repository.cache.caffeine.MenuStatusCaffeineDAO;
import net.sunday.cloud.system.repository.cache.caffeine.RoleStatusCaffeineDAO;
import net.sunday.cloud.system.service.menu.IMenuService;
import net.sunday.cloud.system.service.rolemenu.IRoleMenuService;
import net.sunday.cloud.system.service.userrole.IUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static net.sunday.cloud.system.enums.SystemRespCodeEnum.PERMISSION_UNASSIGNED_MENU;

/**
 * 权限管理 服务实现层
 */
@Service
public class PermissionServiceImpl implements IPermissionService {

    @Resource
    private IUserRoleService userRoleService;
    @Resource
    private IRoleMenuService roleMenuService;
    @Resource
    private IMenuService menuService;
    @Resource
    private RoleStatusCaffeineDAO roleStatusCaffeineDAO;

    @Resource
    private MenuStatusCaffeineDAO menuStatusCaffeineDAO;


    @Override
    public PermissionRouteRespVO getPermissionRouteInfo() {
        // 1.获取当前登录用户ID
        Long authUserId = SecurityFrameworkUtils.getAuthUserId();
        if (authUserId == null) {
            throw new BusinessException(GlobalRespCodeEnum.UNAUTHORIZED);
        }

        // 2.连表查询当前用户拥有的所有菜单
        List<MenuRespVO> menuList = userRoleService.joinMenuByUserId(authUserId);
        if (CollectionUtils.isEmpty(menuList)) {
            throw new BusinessException(PERMISSION_UNASSIGNED_MENU);
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

    @Override
    public void assignUserRole(Long userId, Set<Long> roleIds) {
        // 获得角色拥有角色编号
        List<Long> dbRoleIds = userRoleService.listByUserId(userId);

        // 计算新增和删除的角色编号
        Set<Long> roleIdList = CollUtil.emptyIfNull(roleIds);
        Collection<Long> createRoleIds = CollUtil.subtract(roleIdList, dbRoleIds);
        Collection<Long> deleteMenuIds = CollUtil.subtract(dbRoleIds, roleIdList);
        // 执行新增和删除。对于已经授权的角色，不用做任何处理
        if (CollectionUtils.isNotEmpty(createRoleIds)) {
            userRoleService.saveBatch(CollectionUtils.convertList(createRoleIds, roleId -> {
                UserRoleDO entity = new UserRoleDO();
                entity.setUserId(userId);
                entity.setRoleId(roleId);
                return entity;
            }));
        }
        if (CollectionUtils.isNotEmpty(deleteMenuIds)) {
            userRoleService.deleteByUserIdAndRoleIdIds(userId, deleteMenuIds);
        }
    }

    @Override
    public void assignRoleMenu(Long roleId, Set<Long> menuIds) {
        // 1。获得角色拥有菜单编号
        Set<Long> existMenuIds = roleMenuService.listMenuIdsByRoleId(roleId);
        // 2。计算新增和删除的菜单编号
        Set<Long> menuIdList = CollUtil.emptyIfNull(menuIds);
        Collection<Long> createMenuIds = CollUtil.subtract(menuIdList, existMenuIds);
        Collection<Long> deleteMenuIds = CollUtil.subtract(existMenuIds, menuIdList);
        // 执行新增和删除。对于已经授权的菜单，不用做任何处理
        if (CollUtil.isNotEmpty(createMenuIds)) {
            roleMenuService.saveBatch(CollectionUtils.convertList(createMenuIds, menuId -> {
                RoleMenuDO entity = new RoleMenuDO();
                entity.setRoleId(roleId);
                entity.setMenuId(menuId);
                return entity;
            }));
        }
        if (CollUtil.isNotEmpty(deleteMenuIds)) {
            roleMenuService.deleteByRoleIdAndMenuIds(roleId, deleteMenuIds);
        }
    }

    @Override
    public boolean hasAnyPermissions(Long userId, String... permissions) {
        if (permissions == null || permissions.length == 0) {
            return true;
        }

        // 获取用户拥有的角色编号
        List<Long> roleIds = userRoleService.listByUserId(userId);
        // 移除不可用的角色
        roleIds.removeIf(roleId -> !roleStatusCaffeineDAO.get(roleId));
        if (CollectionUtils.isEmpty(roleIds)) {
            return false;
        }

        // 权限匹配
        for (String permission : permissions) {
            if (hasAnyPermission(roleIds, permission)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasAnyPermission(List<Long> roleIds, String permission) {
        // 获取权限拥有的菜单编号
        List<Long> menuIds = menuService.listMenuIdByPermission(permission);
        menuIds.removeIf(menuId -> !menuStatusCaffeineDAO.get(menuId));
        if (CollectionUtils.isEmpty(menuIds)) {
            return false;
        }

        for (Long menuId : menuIds) {
            // 获得拥有该菜单的角色编号集合
            Set<Long> menuRoleIds = roleMenuService.listRoleIdByMenuId(menuId);
            // 如果有交集，说明有权限
            if (CollUtil.containsAny(menuRoleIds, roleIds)) {
                return true;
            }
        }
        return false;

    }

    private void buildPermissionRouteTree(List<MenuRespVO> routes, Set<String> permissions, List<MenuRespVO> menuList) {
        Map<Long, MenuRespVO> menuMap = menuList.stream().collect(Collectors.toMap(MenuRespVO::getId, v -> v));

        menuList.forEach(menu -> {
            if (StringUtils.hasText(menu.getPermission())) {
                permissions.add(menu.getPermission());
            }
            // 特殊处理，前端根据routeName来缓存路由信息
            menu.setName(menu.getRouteName());

            if (Objects.equals(menu.getParentId(), MenuDO.ID_ROOT)) {
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
