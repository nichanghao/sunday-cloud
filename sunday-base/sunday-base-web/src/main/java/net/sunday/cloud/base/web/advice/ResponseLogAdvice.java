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
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import java.util.Objects;

import static net.sunday.cloud.base.web.advice.RequestBodyLogAdvice.SUPPORT_CLASS_SUFFIX_NAME;


/**
 * 接口响应日志打印拦截器
 */
@ControllerAdvice
@AllArgsConstructor
public class ResponseLogAdvice implements ResponseBodyAdvice<Object> {

    private static final Logger logger = LoggerFactory.getLogger(ResponseLogAdvice.class);

    @Override
    public boolean supports(@NonNull MethodParameter returnType,
                            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getDeclaringClass().getName().endsWith(SUPPORT_CLASS_SUFFIX_NAME);
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  @NonNull MethodParameter returnType,
                                  @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request,
                                  @NonNull ServerHttpResponse response) {

        try {
            String requestUrl = request.getURI().getRawPath();
            Class<?> clazz = returnType.getDeclaringClass();

            String requestMethod = "";
            Method method = returnType.getMethod();
            if (Objects.nonNull(method)) {
                requestMethod = clazz.getSimpleName() + "." + method.getName();
            }
            logger.info("响应请求 {} ({}) end, response: {}", requestUrl, requestMethod, JsonUtils.toJsonString(body));
        } catch (Exception e) {
            logger.debug("[响应日志打印失败] [{}]", e.getMessage());
        }

        return body;
    }

}
