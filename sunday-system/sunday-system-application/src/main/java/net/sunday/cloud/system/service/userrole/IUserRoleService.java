package net.sunday.cloud.system.service.userrole;

import com.baomidou.mybatisplus.extension.service.IService;
import net.sunday.cloud.system.controller.admin.menu.vo.MenuRespVO;
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
     * 获取当前用户拥有的并开启状态的角色id
     *
     * @param userIds 用户ID集合
     * @return 用户关联的角色关系列表
     */
    List<UserRoleDO> listEnableByUserIds(Collection<Long> userIds);

    /**
     * 获取当前用户拥有的并开启状态的角色id
     *
     * @param userId 用户ID
     * @return 当前用户拥有的并开启状态的角色id
     */
    List<Long> listEnableByUserId(Long userId);

    /**
     * 连表查询当前用户拥有的所有菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<MenuRespVO> joinMenuByUserId(Long userId);

    /**
     * 根据用户ID和角色ID集合删除用户角色关联关系
     *
     * @param userId  用户ID
     * @param roleIds 角色ID集合
     */
    void deleteByUserIdAndRoleIdIds(Long userId, Collection<Long> roleIds);


}
