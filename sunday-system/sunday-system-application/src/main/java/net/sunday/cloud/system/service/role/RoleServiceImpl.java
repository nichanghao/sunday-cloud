package net.sunday.cloud.system.service.role;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import net.sunday.cloud.base.common.entity.page.PageResult;
import net.sunday.cloud.base.common.enums.CommonStatusEnum;
import net.sunday.cloud.base.common.exception.BusinessException;
import net.sunday.cloud.base.common.util.collection.CollectionUtils;
import net.sunday.cloud.base.common.util.object.BeanUtils;
import net.sunday.cloud.system.controller.admin.role.vo.RolePageReqVO;
import net.sunday.cloud.system.controller.admin.role.vo.RoleRespVO;
import net.sunday.cloud.system.controller.admin.role.vo.RoleUpsertReqVO;
import net.sunday.cloud.system.model.RoleDO;
import net.sunday.cloud.system.repository.mapper.RoleMapper;
import net.sunday.cloud.system.service.rolemenu.IRoleMenuService;
import net.sunday.cloud.system.service.userrole.IUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static net.sunday.cloud.system.enums.SystemRespCodeEnum.*;

/**
 * 系统角色 服务实现层
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleDO> implements IRoleService {

    @Resource
    private IUserRoleService userRoleService;
    @Resource
    private IRoleMenuService roleMenuService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRole(RoleUpsertReqVO reqVO) {

        // 1. 校验角色
        validateRoleForUpsert(reqVO.getName(), reqVO.getCode(), null);

        // 2. 插入到数据库
        RoleDO role = BeanUtils.toBean(reqVO, RoleDO.class);
        role.setStatus(CommonStatusEnum.ENABLE.ordinal());
        baseMapper.insert(role);
        return role.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(RoleUpsertReqVO reqVO) {
        // 1. 校验是否可以更新
        validateRoleForUpsert(reqVO.getName(), reqVO.getCode(), reqVO.getId());

        // 2. 更新到数据库
        RoleDO updateObj = BeanUtils.toBean(reqVO, RoleDO.class);
        baseMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long id) {
        // 1. 校验角色是否存在
        validateRoleExists(id);
        // 2. 校验角色是否被用户使用
        validateRoleBeUsedByUser(id);
        // 3. 删除角色
        baseMapper.deleteById(new RoleDO(id));
        // 4. 删除角色关联的菜单数据
        roleMenuService.removeByRoleId(id);
    }

    @Override
    public PageResult<RoleRespVO> getRolePage(RolePageReqVO reqVO) {

        PageResult<RoleDO> pageResult = baseMapper.selectPage(reqVO, Wrappers.<RoleDO>lambdaQuery()
                .like(reqVO.getName() != null, RoleDO::getName, reqVO.getName())
                .like(reqVO.getCode() != null, RoleDO::getCode, reqVO.getCode())
                .eq(reqVO.getStatus() != null, RoleDO::getStatus, reqVO.getStatus())
        );

        if (CollectionUtils.isEmpty(pageResult.getRecords())) {
            return PageResult.empty();
        }

        return new PageResult<>(CollectionUtils
                .convertList(pageResult.getRecords(), role -> BeanUtils.toBean(role, RoleRespVO.class)), pageResult.getTotal());
    }

    @Override
    public List<RoleRespVO> listSimpleRoleByStatus(int status) {
        List<RoleDO> roleList = baseMapper.selectList(Wrappers.<RoleDO>lambdaQuery()
                .eq(RoleDO::getStatus, status)
                .select(RoleDO::getId, RoleDO::getName, RoleDO::getCode));

        return CollectionUtils.convertList(roleList, role -> BeanUtils.toBean(role, RoleRespVO.class));
    }

    private void validateRoleBeUsedByUser(Long roleId) {
        if (CollectionUtils.isEmpty(userRoleService.listByRoleId(roleId))) {
            return;
        }

        throw new BusinessException(ROLE_BE_USED_BY_USER);
    }

    private void validateRoleForUpsert(String name, String code, Long id) {
        // 校验角色是否存在
        validateRoleExists(id);
        // 校验角色名称是否唯一
        validateRoleNameUnique(id, name);
        // 校验角色编码是否唯一
        validateRoleCodeUnique(id, code);
    }

    private void validateRoleExists(Long id) {
        if (id == null) {
            return;
        }
        if (baseMapper.selectCount(Wrappers.<RoleDO>lambdaQuery().eq(RoleDO::getId, id)) != 1) {
            throw new BusinessException(ROLE_NOT_EXISTS);
        }
    }

    @SuppressWarnings("Duplicates")
    private void validateRoleNameUnique(Long id, String name) {
        if (!StringUtils.hasText(name)) {
            return;
        }

        RoleDO role = baseMapper.selectOne(RoleDO::getName, name);
        if (role == null) {
            return;
        }

        if (!Objects.equals(role.getId(), id)) {
            throw new BusinessException(ROLE_NAME_EXISTS);
        }
    }

    @SuppressWarnings("Duplicates")
    private void validateRoleCodeUnique(Long id, String code) {
        if (!StringUtils.hasText(code)) {
            return;
        }

        RoleDO role = baseMapper.selectOne(RoleDO::getCode, code);
        if (role == null) {
            return;
        }

        if (!Objects.equals(role.getId(), id)) {
            throw new BusinessException(ROLE_CODE_EXISTS);
        }
    }
}
