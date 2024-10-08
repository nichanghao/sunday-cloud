package net.sunday.cloud.system.service.role;

import com.baomidou.mybatisplus.extension.service.IService;
import net.sunday.cloud.base.common.entity.page.PageResult;
import net.sunday.cloud.system.controller.admin.role.vo.RolePageReqVO;
import net.sunday.cloud.system.controller.admin.role.vo.RoleRespVO;
import net.sunday.cloud.system.controller.admin.role.vo.RoleUpsertReqVO;
import net.sunday.cloud.system.model.RoleDO;

import java.util.List;

/**
 * 系统角色 服务接口层
 */
public interface IRoleService extends IService<RoleDO> {

    /**
     * 创建角色
     *
     * @param reqVO 创建角色信息
     * @return 角色编号
     */
    Long createRole(RoleUpsertReqVO reqVO);

    /**
     * 更新角色
     *
     * @param updateReqVO 更新角色信息
     */
    void updateRole(RoleUpsertReqVO updateReqVO);

    /**
     * 更新角色状态
     *
     * @param id     角色编号
     * @param status 角色状态
     */
    void updateRoleStatus(Long id, Integer status);

    /**
     * 删除角色
     *
     * @param id 角色编号
     */
    void deleteRole(Long id);

    /**
     * 获得角色分页
     *
     * @param pageReqVO 角色分页查询
     * @return 角色分页结果
     */
    PageResult<RoleRespVO> getRolePage(RolePageReqVO pageReqVO);

    /**
     * 获得角色精简列表
     *
     * @param status 角色状态
     * @return 角色精简列表
     */
    List<RoleRespVO> listSimpleRoleByStatus(int status);
}
