package net.sunday.cloud.base.security.handler;

import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sunday.cloud.base.common.entity.result.R;
import net.sunday.cloud.base.common.util.json.JsonUtils;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

import static net.sunday.cloud.base.common.exception.GlobalRespCodeEnum.UNAUTHORIZED;

/**
 * 自定义认证端点, 当用户尝试访问一个需要认证的资源但未通过认证时，返回给前端错误码重定向至登录页认证
 */

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        JakartaServletUtil.write(response, JsonUtils.toJsonString(R.failed(UNAUTHORIZED)), MediaType.APPLICATION_JSON_UTF8_VALUE);

    }
}
