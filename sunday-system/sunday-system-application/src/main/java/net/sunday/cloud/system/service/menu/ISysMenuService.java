package net.sunday.cloud.system.service.menu;

import net.sunday.cloud.system.controller.admin.menu.vo.MenuListReqVO;
import net.sunday.cloud.system.controller.admin.menu.vo.MenuRespVO;
import net.sunday.cloud.system.controller.admin.menu.vo.MenuSimpleRespVO;
import net.sunday.cloud.system.controller.admin.menu.vo.MenuUpsertReqVO;
import net.sunday.cloud.system.model.SysMenuDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 系统菜单 服务接口层
 */
public interface ISysMenuService extends IService<SysMenuDO> {

    /**
     * 创建菜单
     *
     * @param reqVO 菜单信息
     * @return 创建出来的菜单编号
     */
    Long createMenu(MenuUpsertReqVO reqVO);

    /**
     * 更新菜单
     *
     * @param updateReqVO 菜单信息
     */
    void updateMenu(MenuUpsertReqVO updateReqVO);

    /**
     * 删除菜单
     *
     * @param id 菜单编号
     */
    void deleteMenu(Long id);

    /**
     * 筛选菜单列表
     *
     * @param reqVO 筛选条件请求 VO
     * @return 菜单列表
     */
    List<MenuRespVO> getMenuList(MenuListReqVO reqVO);

    /**
     * 获取菜单精简信息列表
     *
     * @param reqVO 筛选条件请求 VO
     * @return 菜单精简信息列表
     */
    List<MenuSimpleRespVO> getSimpleMenuList(MenuListReqVO reqVO);
}
