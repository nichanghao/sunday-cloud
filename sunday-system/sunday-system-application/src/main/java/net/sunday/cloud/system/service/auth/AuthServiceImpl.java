package net.sunday.cloud.system.service.auth;

import jakarta.annotation.Resource;
import net.sunday.cloud.system.controller.auth.vo.AuthLoginReqVO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * 用户登录认证服务实现
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Override
    public Object login(AuthLoginReqVO reqVO) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(reqVO.getUsername(), reqVO.getPassword()));

        return authentication;
    }
}
