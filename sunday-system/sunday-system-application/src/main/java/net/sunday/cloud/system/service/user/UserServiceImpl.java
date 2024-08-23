package net.sunday.cloud.system.service.user;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import net.sunday.cloud.base.common.entity.page.PageResult;
import net.sunday.cloud.base.common.enums.CommonStatusEnum;
import net.sunday.cloud.base.common.exception.BusinessException;
import net.sunday.cloud.base.common.util.collection.CollectionUtils;
import net.sunday.cloud.base.common.util.object.BeanUtils;
import net.sunday.cloud.base.security.util.SecurityFrameworkUtils;
import net.sunday.cloud.system.controller.admin.role.vo.RoleRespVO;
import net.sunday.cloud.system.controller.admin.user.vo.UserPageReqVO;
import net.sunday.cloud.system.controller.admin.user.vo.UserRespVO;
import net.sunday.cloud.system.controller.admin.user.vo.UserUpsertReqVO;
import net.sunday.cloud.system.model.RoleDO;
import net.sunday.cloud.system.model.UserDO;
import net.sunday.cloud.system.model.UserRoleDO;
import net.sunday.cloud.system.repository.mapper.UserMapper;
import net.sunday.cloud.system.service.role.IRoleService;
import net.sunday.cloud.system.service.userrole.IUserRoleService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static net.sunday.cloud.system.enums.SystemRespCodeEnum.*;

/**
 * 系统用户 服务实现层
 */
@Service
@AllArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements IUserService {

    private final PasswordEncoder passwordEncoder;

    private final IUserRoleService userRoleService;

    private final IRoleService roleService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addUser(UserUpsertReqVO upsertVO) {
        // 校验用户信息是否存在
        validateUserForUpsert(null, upsertVO.getUsername(), upsertVO.getPhone(), upsertVO.getEmail());

        // 插入用户数据
        UserDO user = BeanUtils.toBean(upsertVO, UserDO.class);
        user.setPassword(passwordEncoder.encode(upsertVO.getPassword()));
        baseMapper.insert(user);

        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserUpsertReqVO upsertVO) {
        upsertVO.setPassword(null);
        // 校验用户信息是否存在
        validateUserForUpsert(upsertVO.getId(), upsertVO.getUsername(), upsertVO.getPhone(), upsertVO.getEmail());

        // 更新用户
        UserDO updateObj = BeanUtils.toBean(upsertVO, UserDO.class);
        baseMapper.updateById(updateObj);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        // 1. 校验用户存在
        validateUserExists(id);

        // 2. 删除用户，注意逻辑删除需要传 entity 才会自动填充 ：net.sunday.cloud.base.mybatis.handler.DefaultFillFieldHandler
        baseMapper.deleteById(new UserDO(id));

        // 3. 删除用户角色关系
        userRoleService.removeByUserId(id);
    }

    @Override
    public void resetUserPassword(Long id, String password) {
        // 1. 校验用户存在
        validateUserExists(id);

        // 2. 更新密码
        baseMapper.updateById(UserDO.builder()
                .id(id)
                .password(passwordEncoder.encode(password))
                .build());
    }

    @Override
    public void updateUserStatus(Long id, Integer status) {
        // 1. 校验用户存在
        validateUserExists(id);

        // 2. 更新用户状态
        baseMapper.updateById(UserDO.builder()
                .id(id)
                .status(status)
                .build());
    }

    @Override
    public PageResult<UserRespVO> getUserPage(UserPageReqVO reqVO) {

        // 1.查询用户数据
        PageResult<UserDO> pageResult = baseMapper.selectPage(reqVO, Wrappers.<UserDO>lambdaQuery()
                .like(reqVO.getUsername() != null, UserDO::getUsername, reqVO.getUsername())
                .like(reqVO.getPhone() != null, UserDO::getPhone, reqVO.getPhone())
                .eq(reqVO.getStatus() != null, UserDO::getStatus, reqVO.getStatus())
        );

        if (CollectionUtils.isEmpty(pageResult.getRecords())) {
            return PageResult.empty();
        }

        // 2.查询用户关联的角色信息
        List<UserRespVO> records = new ArrayList<>(pageResult.getRecords().size());
        List<Long> userIds = new ArrayList<>(pageResult.getRecords().size());
        for (UserDO record : pageResult.getRecords()) {
            userIds.add(record.getId());
            records.add(BeanUtils.toBean(record, UserRespVO.class));
        }
        List<UserRoleDO> userRoleList = userRoleService.listByUserIds(userIds);
        if (CollectionUtils.isEmpty(userRoleList)) {
            return PageResult.<UserRespVO>builder()
                    .records(records)
                    .total(pageResult.getTotal())
                    .build();
        }

        // 3.用户填充角色信息
        Map<Long, List<UserRoleDO>> userRoleMap = userRoleList.stream().collect(Collectors.groupingBy(UserRoleDO::getUserId));
        return PageResult.<UserRespVO>builder()
                .records(records.stream().peek(record -> {
                    if (userRoleMap.containsKey(record.getId())) {
                        record.setRoles(userRoleMap.get(record.getId()).stream().map(v -> {
                            RoleRespVO roleRespVO = new RoleRespVO();
                            roleRespVO.setId(v.getRoleId());
                            return roleRespVO;
                        }).collect(Collectors.toList()));
                    } else {
                        record.setRoles(Collections.emptyList());
                    }
                }).toList())
                .total(pageResult.getTotal())
                .build();
    }

    @Override
    public UserRespVO getUserSelfInfo() {
        // 获取当前用户ID
        Long userId = SecurityFrameworkUtils.getAuthUserId();
        if (userId == null) {
            return null;
        }

        return this.getUserDetails(userId);
    }

    @Override
    public UserRespVO getUserDetails(Long userId) {
        // 1.查询用户信息
        UserDO user = baseMapper.selectById(userId);
        if (user == null) {
            return null;
        }
        UserRespVO userResp = BeanUtils.toBean(user, UserRespVO.class);

        // 2.查询用户角色信息
        List<Long> roleIds = userRoleService.listEnableByUserId(userId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return userResp;
        }
        List<RoleDO> roleDOS = roleService.listByIds(roleIds);
        List<RoleDO> list = roleDOS.stream().filter(v -> v.getStatus() == CommonStatusEnum.ENABLE.ordinal()).toList();
        userResp.setRoles(CollectionUtils.convertList(list, role -> BeanUtils.toBean(role, RoleRespVO.class)));

        return userResp;
    }

    private void validateUserForUpsert(Long id, String username, String phone, String email) {

        // 校验用户存在
        validateUserExists(id);
        // 校验用户名唯一
        validateUsernameUnique(id, username);
        // 校验手机号唯一
        validatePhoneUnique(id, phone);
        // 校验邮箱唯一
        validateEmailUnique(id, email);

    }

    private void validateUserExists(Long id) {
        if (id == null) {
            return;
        }
        UserDO user = baseMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(USER_NOT_EXISTS);
        }
    }

    @SuppressWarnings("Duplicates")
    private void validateUsernameUnique(Long id, String username) {
        if (StrUtil.isBlank(username)) {
            return;
        }

        UserDO user = baseMapper.selectOne(UserDO::getUsername, username);
        if (user == null) {
            return;
        }

        if (!Objects.equals(user.getId(), id)) {
            throw new BusinessException(USER_USERNAME_EXISTS);
        }
    }

    @SuppressWarnings("Duplicates")
    private void validatePhoneUnique(Long id, String mobile) {
        if (StrUtil.isBlank(mobile)) {
            return;
        }
        UserDO user = baseMapper.selectOne(UserDO::getPhone, mobile);
        if (user == null) {
            return;
        }
        if (!Objects.equals(user.getId(), id)) {
            throw new BusinessException(USER_MOBILE_EXISTS);
        }
    }


    @SuppressWarnings("Duplicates")
    private void validateEmailUnique(Long id, String email) {
        if (StrUtil.isBlank(email)) {
            return;
        }
        UserDO user = baseMapper.selectOne(UserDO::getEmail, email);
        if (user == null) {
            return;
        }
        if (!Objects.equals(user.getId(), id)) {
            throw new BusinessException(USER_EMAIL_EXISTS);
        }

    }

}
