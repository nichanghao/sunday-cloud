package net.sunday.cloud.base.security.util;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import net.sunday.cloud.base.common.entity.AuthUser;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;

import java.util.Collections;

import static net.sunday.cloud.base.common.constant.SecurityConstants.AUTHORIZATION_BEARER;
import static net.sunday.cloud.base.common.constant.SecurityConstants.AUTHORIZATION_HEADER;

/**
 * security框架工具类
 */
public class SecurityFrameworkUtils {

    /**
     * 设置当前用户
     *
     * @param authUser 登录用户
     * @param request  请求
     */
    public static void setAuthUser(AuthUser authUser, HttpServletRequest request) {
        // 创建 Authentication，并设置到上下文
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(authUser, null, Collections.emptyList());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 获取当前用户Id
     *
     * @return 当前用户Id
     */
    @Nullable
    public static Long getAuthUserId() {
        AuthUser authUser = getAuthUser();
        if (authUser == null) {
            return null;
        }
        return authUser.getId();
    }

    /**
     * 获取当前用户
     *
     * @return 当前用户
     */
    @Nullable
    public static AuthUser getAuthUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getPrincipal() instanceof AuthUser ? (AuthUser) authentication.getPrincipal() : null;
    }

    /**
     * 获得当前认证信息
     *
     * @return 认证信息
     */
    public static Authentication getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return null;
        }
        return context.getAuthentication();
    }

    /**
     * 从请求中，获得认证 Token
     *
     * @param request 请求
     * @return 认证 Token
     */
    public static String resolveAuthorizationToken(HttpServletRequest request) {
        // 1. 获得 Token。优先级：Header > Parameter
        String token = request.getHeader(AUTHORIZATION_HEADER);
        if (StrUtil.isEmpty(token)) {
            token = request.getParameter(AUTHORIZATION_HEADER);
        }
        if (!StringUtils.hasText(token)) {
            return null;
        }
        // 2. 去除 Token 中带的 Bearer
        int index = token.indexOf(AUTHORIZATION_BEARER + " ");
        return index >= 0 ? token.substring(index + 7).trim() : token;
    }

}
