package net.sunday.cloud.system.service.auth;

import jakarta.validation.Valid;
import net.sunday.cloud.system.controller.auth.vo.AuthLoginReqVO;

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
    Object login(@Valid AuthLoginReqVO reqVO);
}
