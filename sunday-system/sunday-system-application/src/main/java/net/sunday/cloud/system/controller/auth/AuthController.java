package net.sunday.cloud.system.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import net.sunday.cloud.base.common.entity.auth.AuthUser;
import net.sunday.cloud.base.common.entity.result.R;
import net.sunday.cloud.system.controller.auth.vo.AuthLoginReqVO;
import net.sunday.cloud.system.controller.auth.vo.AuthLoginRespVO;
import net.sunday.cloud.system.service.auth.AuthService;
import org.springframework.web.bind.annotation.*;

/**
 * 用户登录认证控制器
 */
@Tag(name = "用户登录认证")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private AuthService authService;

    @PermitAll
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public R<AuthLoginRespVO> login(@RequestBody @Valid AuthLoginReqVO reqVO) {

        return R.ok(authService.login(reqVO));
    }

    @PermitAll
    @GetMapping("/token/check")
    @Operation(summary = "Token校验")
    @Parameter(name = "accessToken", description = "访问令牌", required = true)
    public R<AuthUser> checkToken(@RequestParam String accessToken) {

        return R.ok(authService.checkToken(accessToken));
    }
}
