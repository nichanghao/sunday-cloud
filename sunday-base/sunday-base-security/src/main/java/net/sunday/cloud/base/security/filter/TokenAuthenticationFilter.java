package net.sunday.cloud.base.security.filter;

import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sunday.cloud.base.common.entity.auth.AuthUser;
import net.sunday.cloud.base.common.util.json.JsonUtils;
import net.sunday.cloud.base.security.util.SecurityFrameworkUtils;
import net.sunday.cloud.system.api.auth.AuthApi;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static net.sunday.cloud.base.common.constant.SecurityConstants.AUTH_USER_HEADER;

/**
 * spring security token 认证过滤器
 * 过滤链结束后，spring security 会自动清理上下文信息，防止内存泄漏
 *
 * @see org.springframework.security.web.context.SecurityContextHolderFilter
 */
@Slf4j
@AllArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final AuthApi authApi;

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher("/");

    private static final List<String> IGNORE_PATHS = List.of("/auth/**", "/actuator/**");

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();
        for (String ignorePath : IGNORE_PATHS) {
            if (PATH_MATCHER.match(ignorePath, uri)) {
                return true;
            }
        }
        return super.shouldNotFilter(request);
    }

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
                try {
                    authUser = authApi.checkToken(token);
                    if (authUser != null) {
                        SecurityFrameworkUtils.setAuthUser(authUser, request);
                    }
                } catch (Exception ex) {
                    log.warn("[check token]: {}", ex.getMessage());
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
