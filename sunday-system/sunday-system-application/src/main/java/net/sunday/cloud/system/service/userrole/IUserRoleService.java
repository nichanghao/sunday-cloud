package net.sunday.cloud.system.service.userrole;

import com.baomidou.mybatisplus.extension.service.IService;
import net.sunday.cloud.system.model.UserRoleDO;

import java.util.Collection;
import java.util.List;

/**
 * 用户角色关联 服务接口层
 */
public interface IUserRoleService extends IService<UserRoleDO> {

    /**
     * 删除用户关联的角色关系
     *
     * @param userId 用户ID
     */
    void removeByUserId(Long userId);

    /**
     * 获取角色关联的用户关系列表
     *
     * @param roleId 角色ID
     * @return 角色关联的用户关系列表
     */
    List<UserRoleDO> listByRoleId(Long roleId);

    /**
     * 获取用户关联的角色关系列表
     *
     * @param userIds 用户ID集合
     * @return 用户关联的角色关系列表
     */
    List<UserRoleDO> listByUserIds(Collection<Long> userIds);

}
