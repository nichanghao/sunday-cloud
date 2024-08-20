package net.sunday.cloud.system.service.userrole;

import net.sunday.cloud.system.model.SysUserRoleDO;
import com.baomidou.mybatisplus.extension.service.IService;

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

}
