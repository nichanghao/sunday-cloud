package net.sunday.cloud.system.api.user;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import net.sunday.cloud.system.api.user.dto.SysUserRespDTO;
import net.sunday.cloud.system.model.SysUserDO;
import net.sunday.cloud.system.service.user.ISysUserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.context.annotation.Primary;

/**
 * 系统用户服务接口实现类
 *
 * @see net.sunday.cloud.base.security.WebSecurityAutoConfiguration#sysUserApiReferenceBean()
 */
@Primary // 如果本地有服务实现则直接走本地，不需要dubbo远程调用
@DubboService
public class SysUserApiService implements SysUserApi {

    @Resource
    private ISysUserService iSysUserService;

    @Override
    public SysUserRespDTO loadUserByUsername(String username) {

        SysUserDO sysUser = iSysUserService.getOne(Wrappers.<SysUserDO>lambdaQuery()
                .select(SysUserDO::getId, SysUserDO::getUsername, SysUserDO::getPassword, SysUserDO::getStatus)
                .eq(SysUserDO::getUsername, username));

        return BeanUtil.copyProperties(sysUser, SysUserRespDTO.class);
    }
}
