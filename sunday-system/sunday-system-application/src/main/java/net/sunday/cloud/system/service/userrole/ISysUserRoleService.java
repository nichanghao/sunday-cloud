package net.sunday.cloud.system.service.userrole;

import com.baomidou.mybatisplus.extension.service.IService;
import net.sunday.cloud.system.model.SysUserRoleDO;

import java.util.List;

/**
 * 用户角色关联 服务接口层
 */
public interface ISysUserRoleService extends IService<SysUserRoleDO> {

    /**
     * 删除用户关联的角色关系
     *
     * @param userId 用户ID
     */
    void removeByUserId(Long userId);

    /**
     * 获取角色关联的用户关系列表
     *
     * @param roleId 用户ID
     * @return 角色关联的用户关系列表
     */
    List<SysUserRoleDO> listByRoleId(Long roleId);

}
