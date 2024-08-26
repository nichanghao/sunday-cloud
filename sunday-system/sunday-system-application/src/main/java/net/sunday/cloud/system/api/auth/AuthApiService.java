package net.sunday.cloud.system.api.auth;

import jakarta.annotation.Resource;
import net.sunday.cloud.base.common.entity.auth.AuthUser;
import net.sunday.cloud.system.service.auth.AuthService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * 用户认证dubbo服务接口实现
 */

@DubboService
public class AuthApiService implements AuthApi {

    @Resource
    private AuthService authService;

    @Override
    public AuthUser checkToken(String accessToken) {

        return authService.checkToken(accessToken);
    }
}
