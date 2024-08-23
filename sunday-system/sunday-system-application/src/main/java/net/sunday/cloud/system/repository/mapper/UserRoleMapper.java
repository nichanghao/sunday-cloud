package net.sunday.cloud.system.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import net.sunday.cloud.base.mybatis.mapper.BaseMapperX;
import net.sunday.cloud.system.model.UserRoleDO;

import java.util.Collection;

/**
 *  用户角色关联 Mapper接口层
 */
public interface UserRoleMapper extends BaseMapperX<UserRoleDO> {

    default void deleteListByUserIdAndRoleIdIds(Long userId, Collection<Long> roleIds) {
        delete(new LambdaQueryWrapper<UserRoleDO>()
                .eq(UserRoleDO::getUserId, userId)
                .in(UserRoleDO::getRoleId, roleIds));
    }
}
