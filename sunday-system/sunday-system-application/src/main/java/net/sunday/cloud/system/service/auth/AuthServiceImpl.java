package net.sunday.cloud.system.service.auth;

import jakarta.annotation.Resource;
import net.sunday.cloud.base.common.exception.BusinessException;
import net.sunday.cloud.system.controller.auth.vo.AuthLoginReqVO;
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
    private AuthenticationManager authenticationManager;

    @Override
    public Object login(AuthLoginReqVO reqVO) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(reqVO.getUsername(), reqVO.getPassword()));
        } catch (AuthenticationException e) {
            throw new BusinessException(e.getMessage());
        }

        return authentication;
    }
}
