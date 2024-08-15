package net.sunday.cloud.system.controller.auth;

import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import net.sunday.cloud.base.common.entity.AuthUser;
import net.sunday.cloud.base.common.entity.R;
import net.sunday.cloud.system.controller.auth.vo.AuthLoginReqVO;
import net.sunday.cloud.system.controller.auth.vo.AuthLoginRespVO;
import net.sunday.cloud.system.service.auth.AuthService;
import org.springframework.web.bind.annotation.*;

/**
 * 用户登录认证控制器
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private AuthService authService;

    @PermitAll
    @PostMapping("/login")
    public R<AuthLoginRespVO> login(@RequestBody @Valid AuthLoginReqVO reqVO) {

        return R.ok(authService.login(reqVO));
    }

    @PermitAll
    @GetMapping("/token/check")
    public R<AuthUser> checkToken(@RequestParam String accessToken) {

        return R.ok(authService.checkToken(accessToken));
    }
}
