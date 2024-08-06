package net.sunday.cloud.base.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sunday.cloud.base.common.util.json.JsonUtils;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * 接口执行时间记录
 */
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteTimeInterceptor implements HandlerInterceptor {

    private static final String SHOW_LOG_KEY = "SLOW_KEY";

    private Long show = 1000L;

    private Boolean enable = false;


    @Override
    @SuppressWarnings("NullableProblems")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(SHOW_LOG_KEY, System.currentTimeMillis());
        return true;
    }


    @Override
    @SuppressWarnings("NullableProblems")
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Long start = (Long) request.getAttribute(SHOW_LOG_KEY);
        long responseTime = System.currentTimeMillis() - start;
        if (enable) {
            if (responseTime > show) {
                log.warn("request {} params {} cost {} ms [slow]",
                        request.getRequestURI(), JsonUtils.toJsonString(request.getParameterMap()), responseTime);
            } else {
                log.info("request {} params {} cost {} ms",
                        request.getRequestURI(), JsonUtils.toJsonString(request.getParameterMap()), responseTime);
            }
        }
    }

}
