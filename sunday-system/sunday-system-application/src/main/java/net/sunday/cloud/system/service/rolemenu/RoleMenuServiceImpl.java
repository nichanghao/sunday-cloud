package net.sunday.cloud.system.service.rolemenu;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.sunday.cloud.base.common.util.collection.CollectionUtils;
import net.sunday.cloud.system.model.RoleMenuDO;
import net.sunday.cloud.system.repository.cache.redis.constant.RedisKeyConstants;
import net.sunday.cloud.system.repository.mapper.RoleMenuMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

/**
 * 角色菜单关联 服务实现类
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenuDO> implements IRoleMenuService {

    @Override
    @CacheEvict(value = RedisKeyConstants.MENU_ROLE_ID_LIST, allEntries = true)
    public void removeByRoleId(Long roleId) {
        baseMapper.delete(RoleMenuDO::getRoleId, roleId);
    }

    @Override
    @CacheEvict(value = RedisKeyConstants.MENU_ROLE_ID_LIST, key = "#menuId")
    public void removeByMenuId(Long menuId) {
        baseMapper.delete(RoleMenuDO::getMenuId, menuId);
    }

    @Override
    public Set<Long> listMenuIdsByRoleId(Long roleId) {
        return CollectionUtils.convertSet(baseMapper.selectList(RoleMenuDO::getRoleId, roleId), RoleMenuDO::getMenuId);
    }

    @Override
    @Cacheable(value = RedisKeyConstants.MENU_ROLE_ID_LIST, key = "#menuId")
    public Set<Long> listRoleIdByMenuId(Long menuId) {
        return CollectionUtils.convertSet(baseMapper.selectList(RoleMenuDO::getMenuId, menuId), RoleMenuDO::getRoleId);
    }

    @Override
    public void deleteByRoleIdAndMenuIds(Long roleId, Collection<Long> menuIds) {
        baseMapper.delete(new LambdaQueryWrapper<RoleMenuDO>()
                .eq(RoleMenuDO::getRoleId, roleId)
                .in(RoleMenuDO::getMenuId, menuIds));
    }

}
