package net.sunday.cloud.system.service.userrole;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import net.sunday.cloud.base.common.enums.CommonStatusEnum;
import net.sunday.cloud.base.common.util.collection.CollectionUtils;
import net.sunday.cloud.system.model.RoleDO;
import net.sunday.cloud.system.model.UserRoleDO;
import net.sunday.cloud.system.repository.mapper.UserRoleMapper;
import net.sunday.cloud.system.repository.redis.constant.RedisKeyConstants;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
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
        MPJLambdaWrapper<UserRoleDO> wrapper = new MPJLambdaWrapper<UserRoleDO>()
                .selectAll(UserRoleDO.class)
                .leftJoin(RoleDO.class, RoleDO::getId, UserRoleDO::getRoleId)
                .in(UserRoleDO::getUserId, userIds)
                .eq(RoleDO::getStatus, CommonStatusEnum.ENABLE.ordinal());

        return baseMapper.selectJoinList(UserRoleDO.class, wrapper);
    }

    @Override
    @Cacheable(value = RedisKeyConstants.USER_ROLE_ID_LIST, key = "#userId")
    public List<Long> listEnableByUserId(Long userId) {

        // 连表查询当前用户拥有的并开启状态的角色id
        MPJLambdaWrapper<UserRoleDO> wrapper = new MPJLambdaWrapper<UserRoleDO>()
                .select(UserRoleDO::getRoleId)
                .leftJoin(RoleDO.class, RoleDO::getId, UserRoleDO::getRoleId)
                .eq(UserRoleDO::getUserId, userId)
                .eq(RoleDO::getStatus, CommonStatusEnum.ENABLE.ordinal());
        List<Long> roleIds = baseMapper.selectJoinList(Long.class, wrapper);
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }

        return roleIds;
    }
}
