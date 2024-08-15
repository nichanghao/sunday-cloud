package net.sunday.cloud.base.security.util;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import net.sunday.cloud.base.common.entity.AuthUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
        int index = token.indexOf(AUTHORIZATION_BEARER);
        return index >= 0 ? token.substring(index + 7).trim() : token;
    }

}
