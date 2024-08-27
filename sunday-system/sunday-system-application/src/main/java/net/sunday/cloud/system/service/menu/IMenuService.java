package net.sunday.cloud.system.service.menu;

import com.baomidou.mybatisplus.extension.service.IService;
import net.sunday.cloud.base.common.entity.page.PageResult;
import net.sunday.cloud.system.controller.admin.menu.vo.MenuListReqVO;
import net.sunday.cloud.system.controller.admin.menu.vo.MenuRespVO;
import net.sunday.cloud.system.controller.admin.menu.vo.MenuSimpleRespVO;
import net.sunday.cloud.system.controller.admin.menu.vo.MenuUpsertReqVO;
import net.sunday.cloud.system.model.MenuDO;

import java.util.List;

/**
 * 系统菜单 服务接口层
 */
public interface IMenuService extends IService<MenuDO> {

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
     * 更新菜单状态
     *
     * @param id     菜单编号
     * @param status 状态
     */
    void updateMenuStatus(Long id, Integer status);

    /**
     * 删除菜单
     *
     * @param id 菜单编号
     */
    void deleteMenu(Long id);

    /**
     * 获取菜单分页列表
     *
     * @param reqVO 筛选条件请求 VO
     * @return 菜单分页列表
     */
    PageResult<MenuRespVO> listMenuPage(MenuListReqVO reqVO);

    /**
     * 获取精简信息菜单树
     *
     * @param reqVO 筛选条件请求 VO
     * @return 精简信息菜单树
     */
    List<MenuSimpleRespVO> listSimpleMenuTree(MenuListReqVO reqVO);

    /**
     * 根据权限标识获取菜单编号列表
     *
     * @param permission 权限标识
     * @return 菜单编号列表
     */
    List<Long> listMenuIdByPermission(String permission);
}
