package net.sunday.cloud.system.service.userrole;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import jakarta.annotation.Resource;
import net.sunday.cloud.base.common.enums.CommonStatusEnum;
import net.sunday.cloud.base.common.util.collection.CollectionUtils;
import net.sunday.cloud.system.controller.admin.menu.vo.MenuRespVO;
import net.sunday.cloud.system.model.MenuDO;
import net.sunday.cloud.system.model.RoleDO;
import net.sunday.cloud.system.model.RoleMenuDO;
import net.sunday.cloud.system.model.UserRoleDO;
import net.sunday.cloud.system.repository.cache.caffeine.RoleStatusCaffeineDAO;
import net.sunday.cloud.system.repository.cache.redis.constant.RedisKeyConstants;
import net.sunday.cloud.system.repository.mapper.UserRoleMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 用户角色关联 服务实现类
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleDO> implements IUserRoleService {

    @Resource
    private RoleStatusCaffeineDAO roleStatusCaffeineDAO;

    @Override
    @CacheEvict(value = RedisKeyConstants.USER_ROLE_ID_LIST, key = "#userId")
    public void removeByUserId(Long userId) {
        baseMapper.delete(Wrappers.<UserRoleDO>lambdaQuery().eq(UserRoleDO::getUserId, userId));
    }

    @Override
    public List<UserRoleDO> listByRoleId(Long roleId) {
        return baseMapper.selectList(UserRoleDO::getRoleId, roleId);
    }

    @Override
    public List<UserRoleDO> listEnableByUserIds(Collection<Long> userIds) {
        MPJLambdaWrapper<UserRoleDO> wrapper = new MPJLambdaWrapper<UserRoleDO>()
                .selectAll(UserRoleDO.class)
                .leftJoin(RoleDO.class, RoleDO::getId, UserRoleDO::getRoleId)
                .in(UserRoleDO::getUserId, userIds)
                .eq(RoleDO::getStatus, CommonStatusEnum.ENABLE.ordinal());

        return baseMapper.selectJoinList(UserRoleDO.class, wrapper);
    }

    @Override
    public List<Long> listEnableByUserId(Long userId) {
        List<Long> roleIds = getSelf().listByUserId(userId);
        // 移除不可用的角色
        roleIds.removeIf(roleId -> !roleStatusCaffeineDAO.get(roleId));
        return roleIds;
    }

    @Override
    public List<MenuRespVO> joinMenuByUserId(Long userId) {
        MPJLambdaWrapper<UserRoleDO> wrapper = new MPJLambdaWrapper<UserRoleDO>()
                .selectAll(MenuDO.class)
                .leftJoin(RoleMenuDO.class, RoleMenuDO::getRoleId, UserRoleDO::getRoleId)
                .leftJoin(RoleDO.class, RoleDO::getId, UserRoleDO::getRoleId)
                .leftJoin(MenuDO.class, MenuDO::getId, RoleMenuDO::getMenuId)
                .eq(UserRoleDO::getUserId, userId)
                .eq(RoleDO::getStatus, CommonStatusEnum.ENABLE.ordinal())
                .eq(MenuDO::getStatus, CommonStatusEnum.ENABLE.ordinal());
        return baseMapper.selectJoinList(MenuRespVO.class, wrapper);
    }

    @Override
    @CacheEvict(value = RedisKeyConstants.USER_ROLE_ID_LIST, key = "#userId")
    public void deleteByUserIdAndRoleIdIds(Long userId, Collection<Long> roleIds) {
        remove(new LambdaQueryWrapper<UserRoleDO>()
                .eq(UserRoleDO::getUserId, userId)
                .in(UserRoleDO::getRoleId, roleIds));
    }

    @Cacheable(value = RedisKeyConstants.USER_ROLE_ID_LIST, key = "#userId")
    public List<Long> listByUserId(Long userId) {

        return CollectionUtils.convertList(baseMapper
                .selectList(Wrappers.<UserRoleDO>lambdaQuery().eq(UserRoleDO::getUserId, userId)), UserRoleDO::getRoleId);
    }

    private UserRoleServiceImpl getSelf() {
        return SpringUtil.getBean(this.getClass());
    }

}
