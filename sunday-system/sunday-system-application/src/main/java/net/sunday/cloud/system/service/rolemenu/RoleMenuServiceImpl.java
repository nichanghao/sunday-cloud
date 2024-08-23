package net.sunday.cloud.system.service.rolemenu;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.sunday.cloud.system.model.RoleMenuDO;
import net.sunday.cloud.system.repository.mapper.RoleMenuMapper;
import org.springframework.stereotype.Service;

/**
 * 角色菜单关联 服务实现类
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenuDO> implements IRoleMenuService {

    @Override
    public void removeByRoleId(Long roleId) {
        baseMapper.delete(RoleMenuDO::getRoleId, roleId);
    }

    @Override
    public void removeByMenuId(Long menuId) {
        baseMapper.delete(RoleMenuDO::getMenuId, menuId);
    }
}
