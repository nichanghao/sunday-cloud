package net.sunday.cloud.system.api.user;

import net.sunday.cloud.system.api.user.dto.SysUserRespDTO;

/**
 * 系统用户API接口
 */
public interface SysUserApi {

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    SysUserRespDTO loadUserByUsername(String username);
}
