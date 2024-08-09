package net.sunday.cloud.system.controller.auth;

import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import net.sunday.cloud.base.common.entity.R;
import net.sunday.cloud.system.controller.auth.vo.AuthLoginReqVO;
import net.sunday.cloud.system.service.auth.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户登录认证控制器
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private AuthService authService;

    @PostMapping("/login")
    @PermitAll
    public R<Object> login(@RequestBody @Valid AuthLoginReqVO reqVO) {

        return R.ok(authService.login(reqVO));
    }
}
