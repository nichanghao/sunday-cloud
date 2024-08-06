package net.sunday.cloud.base.web.advice;

import lombok.AllArgsConstructor;
import net.sunday.cloud.base.common.util.json.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import java.util.Objects;


/**
 * 接口响应日志打印拦截器
 */
@ControllerAdvice
@AllArgsConstructor
public class ResponseLogAdvice implements ResponseBodyAdvice<Object> {

    private static final Logger logger = LoggerFactory.getLogger(ResponseLogAdvice.class);

    @Override
    @SuppressWarnings("NullableProblems")
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        try {
            String requestUrl = request.getURI().getRawPath();
            Class<?> clazz = returnType.getDeclaringClass();

            String requestMethod = "";
            Method method = returnType.getMethod();
            if (Objects.nonNull(method)) {
                requestMethod = clazz.getSimpleName() + "." + method.getName();
            }
            logger.info("响应请求 {} ({}) end, response: {}", requestUrl, requestMethod, JsonUtils.toJsonString(body));
        } catch (Exception ignored) {

        }

        return body;
    }

}
