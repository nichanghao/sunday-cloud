package net.sunday.cloud.system.service.auth;

import jakarta.annotation.Resource;
import net.sunday.cloud.base.common.entity.auth.AuthUser;
import net.sunday.cloud.base.common.exception.BusinessException;
import net.sunday.cloud.system.controller.auth.vo.AuthLoginReqVO;
import net.sunday.cloud.system.controller.auth.vo.AuthLoginRespVO;
import net.sunday.cloud.system.enums.auth.AuthClientEnum;
import net.sunday.cloud.system.repository.cache.redis.AuthRedisDAO;
import net.sunday.cloud.system.util.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

/**
 * 用户登录认证服务实现
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private AuthRedisDAO authRedisDAO;
    @Resource
    private AuthenticationManager authenticationManager;

    @Override
    public AuthLoginRespVO login(AuthLoginReqVO reqVO) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(reqVO.getUsername(), reqVO.getPassword()));
        } catch (AuthenticationException e) {
            throw new BusinessException(e.getMessage());
        }

        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        String accessToken = jwtUtils.generateToken(authUser.getId().toString(), null);
        // 设置token过期时间
        long expiresTime = System.currentTimeMillis() + jwtUtils.getExpiration() * 3600000;

        // 保存用户信息到redis
        authUser.setExpireTime(expiresTime);
        authRedisDAO.setAuthUser(authUser.getId(), AuthClientEnum.ADMIN.client(), authUser);

        return AuthLoginRespVO.builder()
                .userId(authUser.getId())
                .accessToken(accessToken)
                .expiresTime(expiresTime)
                .build();
    }

    @Override
    public AuthUser checkToken(String accessToken) {
        String uid = jwtUtils.extractSubject(accessToken);
        return authRedisDAO.getAuthUser(uid, AuthClientEnum.ADMIN.client());
    }
}
