package net.sunday.cloud.system.service.userrole;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.sunday.cloud.system.model.UserRoleDO;
import net.sunday.cloud.system.repository.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 用户角色关联 服务实现类
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleDO> implements IUserRoleService {


    @Override
    public void removeByUserId(Long userId) {
        baseMapper.delete(Wrappers.<UserRoleDO>lambdaQuery().eq(UserRoleDO::getUserId, userId));
    }

    @Override
    public List<UserRoleDO> listByRoleId(Long roleId) {
        return baseMapper.selectList(UserRoleDO::getRoleId, roleId);
    }

    @Override
    public List<UserRoleDO> listByUserIds(Collection<Long> userIds) {
        return baseMapper.selectList(Wrappers.<UserRoleDO>lambdaQuery().in(UserRoleDO::getUserId, userIds));
    }
}
