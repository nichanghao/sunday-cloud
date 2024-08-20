package net.sunday.cloud.system.service.rolemenu;

import net.sunday.cloud.system.model.SysRoleMenuDO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 角色菜单关联 服务接口层
 */
public interface ISysRoleMenuService extends IService<SysRoleMenuDO> {

    /**
     * 删除角色关联的菜单关系
     *
     * @param roleId 角色ID
     */
    void removeByRoleId(Long roleId);

}
