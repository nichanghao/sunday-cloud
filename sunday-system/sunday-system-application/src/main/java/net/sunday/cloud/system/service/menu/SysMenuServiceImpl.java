package net.sunday.cloud.system.service.menu;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import net.sunday.cloud.base.common.exception.BusinessException;
import net.sunday.cloud.base.common.util.collection.CollectionUtils;
import net.sunday.cloud.base.common.util.object.BeanUtils;
import net.sunday.cloud.system.controller.admin.menu.vo.MenuListReqVO;
import net.sunday.cloud.system.controller.admin.menu.vo.MenuRespVO;
import net.sunday.cloud.system.controller.admin.menu.vo.MenuSimpleRespVO;
import net.sunday.cloud.system.controller.admin.menu.vo.MenuUpsertReqVO;
import net.sunday.cloud.system.enums.menu.MenuTypeEnum;
import net.sunday.cloud.system.model.SysMenuDO;
import net.sunday.cloud.system.repository.mapper.SysMenuMapper;
import net.sunday.cloud.system.service.rolemenu.ISysRoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static net.sunday.cloud.system.enums.SystemRespCodeEnum.*;
import static net.sunday.cloud.system.model.SysMenuDO.ID_ROOT;

/**
 * 系统菜单 服务实现类
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenuDO> implements ISysMenuService {

    @Resource
    private ISysRoleMenuService roleMenuService;

    @Override
    public Long createMenu(MenuUpsertReqVO reqVO) {
        // 1.校验父菜单存在
        validateParentMenu(reqVO.getParentId(), null);
        // 2.校验菜单自身
        validateMenu(reqVO.getParentId(), reqVO.getName(), null);
        // 3.插入数据库
        SysMenuDO menu = BeanUtils.toBean(reqVO, SysMenuDO.class);
        baseMapper.insert(menu);
        return menu.getId();
    }

    @Override
    public void updateMenu(MenuUpsertReqVO updateReqVO) {
        // 1.校验菜单存在
        validateMenuExists(updateReqVO.getId());
        // 2.校验父菜单存在
        validateParentMenu(updateReqVO.getParentId(), updateReqVO.getId());
        // 3.校验菜单自身
        validateMenu(updateReqVO.getParentId(), updateReqVO.getName(), updateReqVO.getId());

        // 4.更新到数据库
        baseMapper.updateById(BeanUtils.toBean(updateReqVO, SysMenuDO.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(Long id) {
        // 1.校验是否还有子菜单
        validateMenuExistsChildren(id);
        // 2.校验删除的菜单是否存在
        validateMenuExists(id);
        // 3.标记删除
        baseMapper.deleteById(id);
        // 4.级联删除角色菜单关系
        roleMenuService.removeByMenuId(id);
    }

    @Override
    public List<MenuRespVO> getMenuList(MenuListReqVO reqVO) {
        // 1.查询菜单列表
        List<SysMenuDO> menuList = baseMapper.selectList(Wrappers.<SysMenuDO>lambdaQuery()
                .like(reqVO.getName() != null, SysMenuDO::getName, reqVO.getName())
                .eq(reqVO.getStatus() != null, SysMenuDO::getStatus, reqVO.getStatus())
        );

        // 2.构建菜单树
        return this.buildMenuTree(menuList);
    }

    @Override
    public List<MenuSimpleRespVO> getSimpleMenuList(MenuListReqVO reqVO) {
        List<SysMenuDO> menuList = baseMapper.selectList(Wrappers.<SysMenuDO>lambdaQuery()
                .like(reqVO.getName() != null, SysMenuDO::getName, reqVO.getName())
                .eq(reqVO.getStatus() != null, SysMenuDO::getStatus, reqVO.getStatus())
                .select(SysMenuDO::getId, SysMenuDO::getName, SysMenuDO::getType, SysMenuDO::getParentId)
        );

        return CollectionUtils.convertList(menuList, menu -> BeanUtils.toBean(menu, MenuSimpleRespVO.class));
    }

    private List<MenuRespVO> buildMenuTree(List<SysMenuDO> menuList) {
        if (CollectionUtils.isEmpty(menuList))
            return Collections.emptyList();

        Map<Long, MenuRespVO> menuMap = new LinkedHashMap<>(menuList.size());
        menuList.forEach(menu -> {
            menuMap.put(menu.getId(), BeanUtils.toBean(menu, MenuRespVO.class));
        });

        List<MenuRespVO> menuTree = new ArrayList<>();
        menuMap.values().forEach(menu -> {
            if (Objects.equals(menu.getParentId(), ID_ROOT)) {
                menuTree.add(menu);
            } else {
                MenuRespVO parent = menuMap.get(menu.getParentId());
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                parent.getChildren().add(menu);
            }
        });

        return menuTree;
    }

    /**
     * 校验是否存在子菜单
     */
    private void validateMenuExistsChildren(Long id) {
        if (id == null) {
            return;
        }
        if (baseMapper.selectCount(Wrappers.<SysMenuDO>lambdaQuery().eq(SysMenuDO::getParentId, id)) > 0) {
            throw new BusinessException(MENU_EXISTS_CHILDREN);
        }
    }

    /**
     * 校验菜单是否存在
     */
    private void validateMenuExists(Long id) {
        if (id == null) {
            return;
        }
        if (baseMapper.selectCount(Wrappers.<SysMenuDO>lambdaQuery().eq(SysMenuDO::getId, id)) != 1) {
            throw new BusinessException(MENU_NOT_EXISTS);
        }
    }

    /**
     * 校验菜单是否合法
     */
    private void validateMenu(Long parentId, String name, Long id) {
        // 1.校验相同父菜单编号下，是否存在相同的菜单名
        SysMenuDO menu = baseMapper.selectOne(Wrappers.<SysMenuDO>lambdaQuery()
                .eq(SysMenuDO::getParentId, parentId).eq(SysMenuDO::getName, name).select(SysMenuDO::getId));
        if (menu == null) {
            return;
        }
        if (!menu.getId().equals(id)) {
            throw new BusinessException(MENU_NAME_EXISTS);
        }
    }

    private void validateParentMenu(Long parentId, Long childId) {
        if (parentId == null || ID_ROOT.equals(parentId)) {
            return;
        }
        // 不能设置自己为父菜单
        if (parentId.equals(childId)) {
            throw new BusinessException(MENU_PARENT_NOT_SELF);
        }

        SysMenuDO menu = baseMapper.selectOne(Wrappers
                .<SysMenuDO>lambdaQuery().eq(SysMenuDO::getId, parentId).select(SysMenuDO::getId, SysMenuDO::getType));
        // 父菜单不存在
        if (menu == null) {
            throw new BusinessException(MENU_PARENT_NOT_EXISTS);
        }
        // 父菜单必须是目录或者菜单类型
        if (!MenuTypeEnum.DIR.getType().equals(menu.getType())
                && !MenuTypeEnum.MENU.getType().equals(menu.getType())) {
            throw new BusinessException(MENU_PARENT_NOT_DIR_OR_MENU);
        }
    }
}
