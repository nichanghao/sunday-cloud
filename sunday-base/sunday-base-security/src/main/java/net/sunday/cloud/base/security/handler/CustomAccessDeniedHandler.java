package net.sunday.cloud.base.security.handler;

import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sunday.cloud.base.common.entity.R;
import net.sunday.cloud.base.common.util.json.JsonUtils;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import static net.sunday.cloud.base.common.exception.GlobalRespCodeEnum.ACCESS_DENIED;

/**
 * 自定义权限拒绝处理器，权限不足时被调用
 */

public class CustomAccessDeniedHandler implements AccessDeniedHandler {


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {

        JakartaServletUtil.write(response, JsonUtils.toJsonString(R.failed(ACCESS_DENIED)), MediaType.APPLICATION_JSON_UTF8_VALUE);
    }
}
