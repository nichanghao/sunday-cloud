package net.sunday.cloud.system.service.user;

import net.sunday.cloud.system.controller.admin.user.vo.UserUpsertReqVO;
import net.sunday.cloud.system.model.SysUserDO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author mybatis-plus-generator
 * @since 2024-08-09
 */
public interface ISysUserService extends IService<SysUserDO> {

    /**
     * 创建用户
     *
     * @param upsertVO 用户信息
     * @return 用户ID
     */
    Long addUser(UserUpsertReqVO upsertVO);

    /**
     * 修改用户
     *
     * @param upsertVO 用户信息
     */
    void updateUser(UserUpsertReqVO upsertVO);

    /**
     * 删除用户
     *
     * @param id 用户ID
     */
    void deleteUser(Long id);

    /**
     * 重置密码
     *
     * @param id       用户ID
     * @param password 密码
     */
    void resetUserPassword(Long id, String password);

    /**
     * 修改状态
     *
     * @param id     用户编号
     * @param status 状态
     */
    void updateUserStatus(Long id, Integer status);
}
