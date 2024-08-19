package net.sunday.cloud.system.service.userrole;

import net.sunday.cloud.system.model.SysUserRoleDO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mybatis-plus-generator
 * @since 2024-08-09
 */
public interface ISysUserRoleService extends IService<SysUserRoleDO> {

    /**
     * 删除用户关联的角色关系
     *
     * @param userId 用户ID
     */
    void removeByUserId(Long userId);

}
