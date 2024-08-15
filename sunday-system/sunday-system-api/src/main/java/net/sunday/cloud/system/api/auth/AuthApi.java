package net.sunday.cloud.system.api.auth;

import net.sunday.cloud.base.common.entity.AuthUser;

public interface AuthApi {

    /**
     * 检查token是否有效
     *
     * @param accessToken 访问令牌
     * @return 用户信息
     */
    AuthUser checkToken(String accessToken);
}
