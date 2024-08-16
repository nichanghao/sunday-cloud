package net.sunday.cloud.base.security.filter;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import net.sunday.cloud.base.common.entity.AuthUser;
import net.sunday.cloud.base.common.util.json.JsonUtils;
import net.sunday.cloud.base.security.util.SecurityFrameworkUtils;
import net.sunday.cloud.system.api.auth.AuthApi;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static net.sunday.cloud.base.common.constant.SecurityConstants.AUTH_USER_HEADER;

/**
 * spring security token 认证过滤器
 */
@AllArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final AuthApi authApi;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // 从请求头中解析出登录用户信息（在gateway中设置）
        AuthUser authUser = resolveAuthUserByHeader(request);
        if (authUser != null) {
            SecurityFrameworkUtils.setAuthUser(authUser, request);
        } else {
            // 从请求头的token中解析，主要用于不走网关时直接访问后台服务的情况
            String token = SecurityFrameworkUtils.resolveAuthorizationToken(request);
            if (StrUtil.isNotEmpty(token)) {
                authUser = authApi.checkToken(token);
                if (authUser != null) {
                    SecurityFrameworkUtils.setAuthUser(authUser, request);
                }
            }
        }

        // 继续过滤链
        filterChain.doFilter(request, response);
    }

    private AuthUser resolveAuthUserByHeader(HttpServletRequest request) {
        String authUserStr = request.getHeader(AUTH_USER_HEADER);
        if (StrUtil.isEmpty(authUserStr)) {
            return null;
        }
        return JsonUtils.parseObject(authUserStr, AuthUser.class);
    }

}