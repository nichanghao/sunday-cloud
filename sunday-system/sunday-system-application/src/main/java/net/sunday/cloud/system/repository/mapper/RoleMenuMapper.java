package net.sunday.cloud.system.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import net.sunday.cloud.base.mybatis.mapper.BaseMapperX;
import net.sunday.cloud.system.model.RoleMenuDO;

import java.util.Collection;

/**
 * 角色菜单关联 Mapper接口层
 */
public interface RoleMenuMapper extends BaseMapperX<RoleMenuDO> {

    default void deleteListByRoleIdAndMenuIds(Long roleId, Collection<Long> menuIds) {
        delete(new LambdaQueryWrapper<RoleMenuDO>()
                .eq(RoleMenuDO::getRoleId, roleId)
                .in(RoleMenuDO::getMenuId, menuIds));
    }
}
