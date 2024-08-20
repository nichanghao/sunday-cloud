package net.sunday.cloud.system.service.auth;

import jakarta.validation.Valid;
import net.sunday.cloud.base.common.entity.auth.AuthUser;
import net.sunday.cloud.system.controller.auth.vo.AuthLoginReqVO;
import net.sunday.cloud.system.controller.auth.vo.AuthLoginRespVO;

/**
 * 用户登录认证服务
 */

public interface AuthService {

    /**
     * 用户登录
     *
     * @param reqVO 登录信息
     * @return 登录结果
     */
    AuthLoginRespVO login(@Valid AuthLoginReqVO reqVO);

    /**
     * 检查token是否有效
     *
     * @param accessToken 访问令牌
     * @return 用户信息
     */
    AuthUser checkToken(String accessToken);
}
