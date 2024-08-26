package net.sunday.cloud.system.service.rolemenu;

import net.sunday.cloud.system.model.RoleMenuDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.Set;

/**
 * 角色菜单关联 服务接口层
 */
public interface IRoleMenuService extends IService<RoleMenuDO> {

    /**
     * 删除角色关联的菜单关系
     *
     * @param roleId 角色ID
     */
    void removeByRoleId(Long roleId);

    /**
     * 删除菜单关联的角色关系
     *
     * @param menuId 菜单ID
     */
    void removeByMenuId(Long menuId);

    /**
     * 获得角色拥有的菜单编号集合
     *
     * @param roleId 角色编号
     * @return 菜单编号集合
     */
    Set<Long> listMenuIdsByRoleId(Long roleId);

    /**
     * 根据菜单关联的角色ID集合
     *
     * @param menuId 菜单ID
     * @return 角色ID集合
     */
    Set<Long> listRoleIdByMenuId(Long menuId);

    /**
     * 根据角色ID和菜单ID集合删除角色菜单关联关系
     *
     * @param roleId  角色ID
     * @param menuIds 菜单ID集合
     */
    void deleteByRoleIdAndMenuIds(Long roleId, Collection<Long> menuIds);
}
