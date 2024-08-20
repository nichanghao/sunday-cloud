package net.sunday.cloud.system.service.rolemenu;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.sunday.cloud.system.model.SysRoleMenuDO;
import net.sunday.cloud.system.repository.mapper.SysRoleMenuMapper;
import org.springframework.stereotype.Service;

/**
 * 角色菜单关联 服务实现类
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenuDO> implements ISysRoleMenuService {

    @Override
    public void removeByRoleId(Long roleId) {
        baseMapper.delete(SysRoleMenuDO::getRoleId, roleId);
    }
}
