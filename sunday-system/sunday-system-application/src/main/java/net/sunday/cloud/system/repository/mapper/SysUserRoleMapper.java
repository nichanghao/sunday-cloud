package net.sunday.cloud.system.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import net.sunday.cloud.base.mybatis.mapper.BaseMapperX;
import net.sunday.cloud.system.model.SysUserRoleDO;

import java.util.Collection;

/**
 *  用户角色关联 Mapper接口层
 */
public interface SysUserRoleMapper extends BaseMapperX<SysUserRoleDO> {

    default void deleteListByUserIdAndRoleIdIds(Long userId, Collection<Long> roleIds) {
        delete(new LambdaQueryWrapper<SysUserRoleDO>()
                .eq(SysUserRoleDO::getUserId, userId)
                .in(SysUserRoleDO::getRoleId, roleIds));
    }
}
