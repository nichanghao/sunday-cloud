package net.sunday.cloud.system.api.user;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import net.sunday.cloud.system.api.user.dto.UserRespDTO;
import net.sunday.cloud.system.model.UserDO;
import net.sunday.cloud.system.service.user.IUserService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * 系统用户服务接口实现类
 *
 * @see net.sunday.cloud.base.security.WebSecurityAutoConfiguration#userApiReferenceBean()
 */
@DubboService
public class UserApiService implements UserApi {

    @Resource
    private IUserService iUserService;

    @Override
    public UserRespDTO loadUserByUsername(String username) {

        UserDO sysUser = iUserService.getOne(Wrappers.<UserDO>lambdaQuery()
                .select(UserDO::getId, UserDO::getUsername, UserDO::getPassword, UserDO::getStatus)
                .eq(UserDO::getUsername, username));

        return BeanUtil.copyProperties(sysUser, UserRespDTO.class);
    }
}
