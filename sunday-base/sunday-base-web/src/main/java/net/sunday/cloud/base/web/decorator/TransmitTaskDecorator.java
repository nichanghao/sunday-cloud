package net.sunday.cloud.base.web.decorator;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static net.sunday.cloud.base.common.constant.MonitorConstants.TRACE_ID;

/**
 * 自定义 TaskDecorator，用于在线程切换时传递上下文
 *
 * @see org.springframework.security.concurrent.DelegatingSecurityContextRunnable
 */

public class TransmitTaskDecorator implements TaskDecorator {
    @Override
    public @NonNull Runnable decorate(@NonNull Runnable runnable) {

        // 获取当前线程的上下文
        SecurityContext securityContext = SecurityContextHolder.getContext();

        // 获取MDC 上下文
        String traceId = MDC.get(TRACE_ID);

        return () -> {
            try {
                // 设置新线程的请求上下文
                SecurityContextHolder.setContext(securityContext);
                // 设置MDC 上下文
                MDC.put(TRACE_ID, traceId);

                runnable.run();
            } finally {
                // 清除新线程的请求上下文
                SecurityContextHolder.clearContext();
                // 清除MDC 上下文
                MDC.clear();
            }
        };
    }
}
