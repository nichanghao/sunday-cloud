package net.sunday.cloud.system.service.menu;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import net.sunday.cloud.base.common.entity.page.PageResult;
import net.sunday.cloud.base.common.exception.BusinessException;
import net.sunday.cloud.base.common.util.collection.CollectionUtils;
import net.sunday.cloud.base.common.util.object.BeanUtils;
import net.sunday.cloud.system.controller.admin.menu.vo.MenuListReqVO;
import net.sunday.cloud.system.controller.admin.menu.vo.MenuRespVO;
import net.sunday.cloud.system.controller.admin.menu.vo.MenuSimpleRespVO;
import net.sunday.cloud.system.controller.admin.menu.vo.MenuUpsertReqVO;
import net.sunday.cloud.system.enums.menu.MenuTypeEnum;
import net.sunday.cloud.system.model.MenuDO;
import net.sunday.cloud.system.repository.mapper.MenuMapper;
import net.sunday.cloud.system.repository.cache.redis.constant.RedisKeyConstants;
import net.sunday.cloud.system.service.rolemenu.IRoleMenuService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static net.sunday.cloud.system.enums.SystemRespCodeEnum.*;
import static net.sunday.cloud.system.model.MenuDO.ID_ROOT;

/**
 * 系统菜单 服务实现类
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuDO> implements IMenuService {

    @Resource
    private IRoleMenuService roleMenuService;

    @Override
    @CacheEvict(value = RedisKeyConstants.PERMISSION_MENU_ID_LIST,
            key = "#reqVO.permission", condition = "#reqVO.permission != null")
    public Long createMenu(MenuUpsertReqVO reqVO) {
        // 1.校验父菜单存在
        validateParentMenu(reqVO.getParentId(), null);
        // 2.校验菜单自身
        validateMenu(reqVO.getParentId(), reqVO.getName(), null);
        // 3.插入数据库
        MenuDO menu = BeanUtils.toBean(reqVO, MenuDO.class);
        baseMapper.insert(menu);
        return menu.getId();
    }

    @Override
    @CacheEvict(value = RedisKeyConstants.PERMISSION_MENU_ID_LIST, allEntries = true)
    public void updateMenu(MenuUpsertReqVO updateReqVO) {
        // 1.校验菜单存在
        validateMenuExists(updateReqVO.getId());
        // 2.校验父菜单存在
        validateParentMenu(updateReqVO.getParentId(), updateReqVO.getId());
        // 3.校验菜单自身
        validateMenu(updateReqVO.getParentId(), updateReqVO.getName(), updateReqVO.getId());

        // 4.更新到数据库
        baseMapper.updateById(BeanUtils.toBean(updateReqVO, MenuDO.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = RedisKeyConstants.PERMISSION_MENU_ID_LIST, allEntries = true)
    public void deleteMenu(Long id) {
        // 1.校验是否还有子菜单
        validateMenuExistsChildren(id);
        // 2.校验删除的菜单是否存在
        validateMenuExists(id);
        // 3.标记删除
        baseMapper.deleteById(new MenuDO(id));
        // 4.级联删除角色菜单关系
        roleMenuService.removeByMenuId(id);
    }

    @Override
    public PageResult<MenuRespVO> listMenuPage(MenuListReqVO reqVO) {
        // 1.查询菜单列表
        List<MenuDO> menuList = baseMapper.selectList(Wrappers.<MenuDO>lambdaQuery()
                .like(reqVO.getName() != null, MenuDO::getName, reqVO.getName())
                .eq(reqVO.getStatus() != null, MenuDO::getStatus, reqVO.getStatus())
        );

        // 2.构建菜单树
        List<MenuRespVO> menuRespVOS = this.buildMenuTree(menuList, MenuRespVO.class);
        // 3.暂时不进行分页，返回所有数据
        return new PageResult<>(menuRespVOS, (long) menuRespVOS.size());
    }

    @Override
    public List<MenuSimpleRespVO> listSimpleMenuTree(MenuListReqVO reqVO) {
        List<MenuDO> menuList = baseMapper.selectList(Wrappers.<MenuDO>lambdaQuery()
                .like(reqVO.getName() != null, MenuDO::getName, reqVO.getName())
                .eq(reqVO.getStatus() != null, MenuDO::getStatus, reqVO.getStatus())
                .select(MenuDO::getId, MenuDO::getName, MenuDO::getType, MenuDO::getParentId)
        );

        return this.buildMenuTree(menuList, MenuSimpleRespVO.class);
    }

    @Override
    @Cacheable(value = RedisKeyConstants.PERMISSION_MENU_ID_LIST, key = "#permission")
    public List<Long> listMenuIdByPermission(String permission) {
        List<MenuDO> menuList = baseMapper.selectList(Wrappers.<MenuDO>lambdaQuery()
                .select(MenuDO::getId)
                .eq(MenuDO::getPermission, permission));
        if (CollectionUtils.isEmpty(menuList)) {
            return Collections.emptyList();
        }

        return menuList.stream().map(MenuDO::getId).collect(Collectors.toList());
    }

    private <T extends MenuSimpleRespVO> List<T> buildMenuTree(List<MenuDO> menuList, Class<T> clazz) {
        if (CollectionUtils.isEmpty(menuList))
            return Collections.emptyList();

        Map<Long, T> menuMap = new LinkedHashMap<>(menuList.size());
        menuList.forEach(menu -> {
            menuMap.put(menu.getId(), BeanUtils.toBean(menu, clazz));
        });

        List<T> menuTree = new ArrayList<>();
        menuMap.values().forEach(menu -> {
            if (Objects.equals(menu.getParentId(), ID_ROOT)) {
                menuTree.add(menu);
            } else {
                MenuSimpleRespVO parent = menuMap.get(menu.getParentId());
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
        if (baseMapper.selectCount(Wrappers.<MenuDO>lambdaQuery().eq(MenuDO::getParentId, id)) > 0) {
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
        if (baseMapper.selectCount(Wrappers.<MenuDO>lambdaQuery().eq(MenuDO::getId, id)) != 1) {
            throw new BusinessException(MENU_NOT_EXISTS);
        }
    }

    /**
     * 校验菜单是否合法
     */
    private void validateMenu(Long parentId, String name, Long id) {
        // 1.校验相同父菜单编号下，是否存在相同的菜单名
        MenuDO menu = baseMapper.selectOne(Wrappers.<MenuDO>lambdaQuery()
                .eq(MenuDO::getParentId, parentId).eq(MenuDO::getName, name).select(MenuDO::getId));
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

        MenuDO menu = baseMapper.selectOne(Wrappers
                .<MenuDO>lambdaQuery().eq(MenuDO::getId, parentId).select(MenuDO::getId, MenuDO::getType));
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
