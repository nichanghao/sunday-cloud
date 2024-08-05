package net.sunday.cloud.base.web.advice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import net.sunday.cloud.base.common.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * 接口请求日志打印拦截器
 */
@ControllerAdvice
@AllArgsConstructor
public class RequestBodyLogAdvice extends RequestBodyAdviceAdapter {

    private final HttpServletRequest httpServletRequest;

    private static final Logger logger = LoggerFactory.getLogger(RequestBodyLogAdvice.class);


    @Override
    public boolean supports(MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {

        return parameter.getDeclaringClass().getName().endsWith("Controller");
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
                                Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        try {
            String requestUrl = httpServletRequest.getRequestURI();
            Class<?> clazz = parameter.getDeclaringClass();

            String requestMethod = "";
            Method method = parameter.getMethod();
            if (Objects.nonNull(method)) {
                requestMethod = clazz.getSimpleName() + "." + method.getName();
            }
            logger.info("收到请求 {} ({}), params: {}", requestUrl, requestMethod, JsonUtils.toJsonString(body));
        } catch (Exception ignored) {

        }

        return body;
    }
}
