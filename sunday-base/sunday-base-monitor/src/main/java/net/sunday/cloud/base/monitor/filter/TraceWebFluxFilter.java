package net.sunday.cloud.base.monitor.filter;

import net.sunday.cloud.base.monitor.util.TraceUtils;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static net.sunday.cloud.base.common.constant.MonitorConstants.TRACE_ID;

public class TraceWebFluxFilter implements WebFilter, Ordered {
    @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {

        return chain.filter(exchange.mutate()
                .request(builder -> builder.header(TRACE_ID, TraceUtils.generateTraceId())).build());
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
