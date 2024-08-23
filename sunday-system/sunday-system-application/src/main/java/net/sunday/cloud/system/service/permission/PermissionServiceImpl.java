package net.sunday.cloud.system.service.permission;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.AllArgsConstructor;
import net.sunday.cloud.base.common.enums.CommonStatusEnum;
import net.sunday.cloud.base.common.util.collection.CollectionUtils;
import net.sunday.cloud.base.security.util.SecurityFrameworkUtils;
import net.sunday.cloud.system.controller.admin.menu.vo.MenuRespVO;
import net.sunday.cloud.system.controller.admin.permission.vo.PermissionRouteRespVO;
import net.sunday.cloud.system.enums.menu.MenuTypeEnum;
import net.sunday.cloud.system.model.MenuDO;
import net.sunday.cloud.system.model.RoleDO;
import net.sunday.cloud.system.model.RoleMenuDO;
import net.sunday.cloud.system.model.UserRoleDO;
import net.sunday.cloud.system.repository.mapper.RoleMenuMapper;
import net.sunday.cloud.system.repository.mapper.UserRoleMapper;
import net.sunday.cloud.system.repository.redis.constant.RedisKeyConstants;
import net.sunday.cloud.system.service.menu.IMenuService;
import net.sunday.cloud.system.service.userrole.IUserRoleService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限管理 服务实现层
 */
@Service
@AllArgsConstructor
public class PermissionServiceImpl implements IPermissionService {

    private final UserRoleMapper userRoleMapper;

    private final RoleMenuMapper roleMenuMapper;

    private final IUserRoleService userRoleService;

    private final IMenuService menuService;


    @Override
    public PermissionRouteRespVO getPermissionRouteInfo() {
        // 1.获取当前登录用户ID
        Long authUserId = SecurityFrameworkUtils.getAuthUserId();
        if (authUserId == null) {
            return PermissionRouteRespVO.EMPTY;
        }

        // 2.连表查询当前用户拥有的所有菜单
        MPJLambdaWrapper<UserRoleDO> wrapper = new MPJLambdaWrapper<UserRoleDO>()
                .selectAll(MenuDO.class)
                .leftJoin(RoleMenuDO.class, RoleMenuDO::getRoleId, UserRoleDO::getRoleId)
                .leftJoin(RoleDO.class, RoleDO::getId, UserRoleDO::getRoleId)
                .leftJoin(MenuDO.class, MenuDO::getId, RoleMenuDO::getMenuId)
                .eq(UserRoleDO::getUserId, authUserId)
                .eq(RoleDO::getStatus, CommonStatusEnum.ENABLE.ordinal())
                .eq(MenuDO::getStatus, CommonStatusEnum.ENABLE.ordinal());
        List<MenuRespVO> menuList = userRoleMapper.selectJoinList(MenuRespVO.class, wrapper);
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

    @Override
    public void assignUserRole(Long userId, Set<Long> roleIds) {
        // 获得角色拥有角色编号
        List<Long> dbRoleIds = userRoleService.listEnableByUserId(userId);

        // 计算新增和删除的角色编号
        Set<Long> roleIdList = CollUtil.emptyIfNull(roleIds);
        Collection<Long> createRoleIds = CollUtil.subtract(roleIdList, dbRoleIds);
        Collection<Long> deleteMenuIds = CollUtil.subtract(dbRoleIds, roleIdList);
        // 执行新增和删除。对于已经授权的角色，不用做任何处理
        if (CollectionUtils.isNotEmpty(createRoleIds)) {
            userRoleMapper.insertBatch(CollectionUtils.convertList(createRoleIds, roleId -> {
                UserRoleDO entity = new UserRoleDO();
                entity.setUserId(userId);
                entity.setRoleId(roleId);
                return entity;
            }));
        }
        if (CollectionUtils.isNotEmpty(deleteMenuIds)) {
            userRoleMapper.deleteListByUserIdAndRoleIdIds(userId, deleteMenuIds);
        }
    }

    @Override
    public Set<Long> listMenuIdsByRoleId(Long roleId) {

        return CollectionUtils.convertSet(roleMenuMapper.selectList(RoleMenuDO::getRoleId, roleId), RoleMenuDO::getMenuId);
    }

    @Override
    public void assignRoleMenu(Long roleId, Set<Long> menuIds) {
        // 1。获得角色拥有菜单编号
        Set<Long> existMenuIds = listMenuIdsByRoleId(roleId);
        // 2。计算新增和删除的菜单编号
        Set<Long> menuIdList = CollUtil.emptyIfNull(menuIds);
        Collection<Long> createMenuIds = CollUtil.subtract(menuIdList, existMenuIds);
        Collection<Long> deleteMenuIds = CollUtil.subtract(existMenuIds, menuIdList);
        // 执行新增和删除。对于已经授权的菜单，不用做任何处理
        if (CollUtil.isNotEmpty(createMenuIds)) {
            roleMenuMapper.insertBatch(CollectionUtils.convertList(createMenuIds, menuId -> {
                RoleMenuDO entity = new RoleMenuDO();
                entity.setRoleId(roleId);
                entity.setMenuId(menuId);
                return entity;
            }));
        }
        if (CollUtil.isNotEmpty(deleteMenuIds)) {
            roleMenuMapper.deleteListByRoleIdAndMenuIds(roleId, deleteMenuIds);
        }
    }

    @Override
    public boolean hasAnyPermissions(Long userId, String... permissions) {
        if (permissions == null || permissions.length == 0) {
            return true;
        }

        // 获取用户拥有的角色编号
        List<Long> roleIds = userRoleService.listEnableByUserId(userId);
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

    @Cacheable(value = RedisKeyConstants.MENU_ROLE_ID_LIST, key = "#menuId")
    public Set<Long> listRoleIdByMenuIdWithCache(Long menuId) {

        return CollectionUtils.convertSet(roleMenuMapper.selectList(RoleMenuDO::getMenuId, menuId), RoleMenuDO::getRoleId);
    }

    private boolean hasAnyPermission(List<Long> roleIds, String permission) {
        // 获取权限拥有的菜单编号
        List<Long> menuIds = menuService.listMenuIdByPermissionWithCache(permission);
        if (CollectionUtils.isEmpty(menuIds)) {
            return false;
        }

        for (Long menuId : menuIds) {
            // 获得拥有该菜单的角色编号集合
            Set<Long> menuRoleIds = getSelf().listRoleIdByMenuIdWithCache(menuId);
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

    /**
     * 获得自身的代理对象，解决 AOP 不生效问题
     */
    private PermissionServiceImpl getSelf() {
        return SpringUtil.getBean(getClass());
    }
}
