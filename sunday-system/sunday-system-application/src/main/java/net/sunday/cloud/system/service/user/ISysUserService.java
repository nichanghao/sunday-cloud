package net.sunday.cloud.system.service.user;

import net.sunday.cloud.base.common.entity.page.PageResult;
import net.sunday.cloud.system.controller.admin.user.vo.UserPageReqVO;
import net.sunday.cloud.system.controller.admin.user.vo.UserRespVO;
import net.sunday.cloud.system.controller.admin.user.vo.UserUpsertReqVO;
import net.sunday.cloud.system.model.SysUserDO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 系统用户 服务接口层
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

    /**
     * 获得用户分页列表
     *
     * @param reqVO 分页条件
     * @return 分页列表
     */
    PageResult<UserRespVO> getUserPage(UserPageReqVO reqVO);

    /**
     * 获得当前登录的用户信息
     *
     * @return 当前登录的用户信息
     */
    UserRespVO getUserSelfInfo();

    /**
     * 获取用户的详细信息
     *
     * @param id 用户ID
     * @return 用户的详细信息
     */
    UserRespVO getUserDetails(Long id);
}
