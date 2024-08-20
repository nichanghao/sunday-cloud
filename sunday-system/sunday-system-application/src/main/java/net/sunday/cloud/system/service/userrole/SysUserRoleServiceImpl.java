package net.sunday.cloud.system.service.userrole;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.sunday.cloud.system.model.SysUserRoleDO;
import net.sunday.cloud.system.repository.mapper.SysUserRoleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户角色关联 服务实现类
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRoleDO> implements ISysUserRoleService {


    @Override
    public void removeByUserId(Long userId) {
        baseMapper.delete(Wrappers.<SysUserRoleDO>lambdaQuery().eq(SysUserRoleDO::getUserId, userId));
    }

    @Override
    public List<SysUserRoleDO> listByRoleId(Long roleId) {
        return baseMapper.selectList(SysUserRoleDO::getRoleId, roleId);
    }
}
