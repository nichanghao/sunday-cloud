package net.sunday.cloud.system.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import net.sunday.cloud.system.model.SysUserDO;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author mybatis-plus-generator
 * @since 2024-08-09
 */
public interface SysUserMapper extends BaseMapper<SysUserDO> {

    default SysUserDO selectByUsername(String username) {
        return selectOne(Wrappers.<SysUserDO>lambdaQuery().eq(SysUserDO::getUsername, username));
    }

    default SysUserDO selectByEmail(String email) {
        return selectOne(Wrappers.<SysUserDO>lambdaQuery().eq(SysUserDO::getEmail, email));
    }

    default SysUserDO selectByPhone(String phone) {
        return selectOne(Wrappers.<SysUserDO>lambdaQuery().eq(SysUserDO::getPhone, phone));
    }
}
