package net.sunday.cloud.base.security.handler;

import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sunday.cloud.base.common.entity.R;
import net.sunday.cloud.base.common.util.json.JsonUtils;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        JakartaServletUtil.write(response, JsonUtils.toJsonString(R.failed(exception.getMessage())), MediaType.APPLICATION_JSON_UTF8_VALUE);

    }
}
