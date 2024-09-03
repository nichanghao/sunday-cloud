package net.sunday.cloud.base.monitor.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sunday.cloud.base.common.constant.MonitorConstants;
import net.sunday.cloud.base.monitor.util.TraceUtils;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class TraceWebMvcFilter extends OncePerRequestFilter implements Ordered {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String traceId = request.getHeader(MonitorConstants.TRACE_ID);
        if (traceId == null) {
            traceId = TraceUtils.generateTraceId();
        }
        MDC.put(MonitorConstants.TRACE_ID, traceId);

        // 继续过滤链
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
