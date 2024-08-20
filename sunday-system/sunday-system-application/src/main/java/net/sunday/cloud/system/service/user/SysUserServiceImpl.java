package net.sunday.cloud.system.service.user;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import net.sunday.cloud.base.common.entity.page.PageResult;
import net.sunday.cloud.base.common.exception.BusinessException;
import net.sunday.cloud.base.common.util.collection.ArrayUtils;
import net.sunday.cloud.base.common.util.collection.CollectionUtils;
import net.sunday.cloud.base.common.util.object.BeanUtils;
import net.sunday.cloud.system.controller.admin.user.vo.UserPageReqVO;
import net.sunday.cloud.system.controller.admin.user.vo.UserRespVO;
import net.sunday.cloud.system.controller.admin.user.vo.UserUpsertReqVO;
import net.sunday.cloud.system.model.SysUserDO;
import net.sunday.cloud.system.repository.mapper.SysUserMapper;
import net.sunday.cloud.system.service.userrole.ISysUserRoleService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

import static net.sunday.cloud.system.enums.SystemRespCodeEnum.*;

/**
 * 系统用户 服务实现层
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserDO> implements ISysUserService {

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private ISysUserRoleService sysUserRoleService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addUser(UserUpsertReqVO upsertVO) {
        // 校验用户信息是否存在
        validateUserForUpsert(null, upsertVO.getUsername(), upsertVO.getPhone(), upsertVO.getEmail());

        // 插入用户数据
        SysUserDO user = BeanUtils.toBean(upsertVO, SysUserDO.class);
        user.setPassword(passwordEncoder.encode(upsertVO.getPassword()));
        baseMapper.insert(user);

        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserUpsertReqVO upsertVO) {
        // 校验用户信息是否存在
        validateUserForUpsert(upsertVO.getId(), upsertVO.getUsername(), upsertVO.getPhone(), upsertVO.getEmail());

        // 更新用户
        SysUserDO updateObj = BeanUtils.toBean(upsertVO, SysUserDO.class);
        baseMapper.updateById(updateObj);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        // 1. 校验用户存在
        validateUserExists(id);

        // 2. 删除用户
        baseMapper.deleteById(id);

        // 3. 删除用户角色关系
        sysUserRoleService.removeByUserId(id);
    }

    @Override
    public void resetUserPassword(Long id, String password) {
        // 1. 校验用户存在
        validateUserExists(id);

        // 2. 更新密码
        baseMapper.updateById(SysUserDO.builder()
                .id(id)
                .password(passwordEncoder.encode(password))
                .build());
    }

    @Override
    public void updateUserStatus(Long id, Integer status) {
        // 1. 校验用户存在
        validateUserExists(id);

        // 2. 更新用户状态
        baseMapper.updateById(SysUserDO.builder()
                .id(id)
                .status(status)
                .build());
    }

    @Override
    public PageResult<UserRespVO> getUserPage(UserPageReqVO reqVO) {
        LocalDateTime[] updateTimes = reqVO.getUpdateTimes();

        PageResult<SysUserDO> pageResult = baseMapper.selectPage(reqVO, Wrappers.<SysUserDO>lambdaQuery()
                .like(reqVO.getUsername() != null, SysUserDO::getUsername, reqVO.getUsername())
                .like(reqVO.getPhone() != null, SysUserDO::getPhone, reqVO.getPhone())
                .eq(reqVO.getStatus() != null, SysUserDO::getStatus, reqVO.getStatus())
                .ge(ArrayUtils.get(updateTimes, 0) != null, SysUserDO::getUpdateTime, updateTimes[0])
                .le(ArrayUtils.get(updateTimes, 1) != null, SysUserDO::getUpdateTime, updateTimes[1])
        );

        if (CollectionUtils.isEmpty(pageResult.getList())) {
            return PageResult.empty();
        }
        return PageResult.<UserRespVO>builder()
                .list(CollectionUtils.convertList(pageResult.getList(), user -> BeanUtils.toBean(user, UserRespVO.class)))
                .total(pageResult.getTotal())
                .build();
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
        SysUserDO user = baseMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(USER_NOT_EXISTS);
        }
    }

    @SuppressWarnings("Duplicates")
    private void validateUsernameUnique(Long id, String username) {
        if (StrUtil.isBlank(username)) {
            return;
        }

        SysUserDO user = baseMapper.selectOne(SysUserDO::getUsername, username);
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
        SysUserDO user = baseMapper.selectOne(SysUserDO::getPhone, mobile);
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
        SysUserDO user = baseMapper.selectOne(SysUserDO::getEmail, email);
        if (user == null) {
            return;
        }
        if (!Objects.equals(user.getId(), id)) {
            throw new BusinessException(USER_EMAIL_EXISTS);
        }

    }

}
