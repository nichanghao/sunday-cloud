
package net.sunday.cloud.gateway.filter;

import jakarta.annotation.Resource;
import net.sunday.cloud.base.common.entity.auth.AuthUser;
import net.sunday.cloud.base.common.entity.result.R;
import net.sunday.cloud.base.common.util.json.JsonUtils;
import net.sunday.cloud.gateway.caffeine.TokenAuthenticationCache;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static net.sunday.cloud.base.common.constant.SecurityConstants.*;

/**
 * token 认证 Filter
 */

@Order(1)
@Component
public class TokenAuthenticationFilter implements WebFilter {

    private static final AuthUser EMPTY_AUTH_USER = new AuthUser();

    /**
     * 调用认证服务URL
     * see: net.sunday.cloud.system.controller.auth.AuthController#checkToken(String)
     */
    private static final String CHECK_TOKEN_URL = "http://sunday-cloud-system/auth/token/check";

    @Resource
    private WebClient webClient;


    @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        // 移除登录用户请求头，防止伪造用户
        removeAuthUser(exchange);

        String token = resolveAuthorization(exchange);
        // 没有token直接放行，由下游服务处理
        if (token == null) {
            return chain.filter(exchange);
        }

        return checkAccessToken(token).defaultIfEmpty(EMPTY_AUTH_USER).flatMap(user -> {
            // 无用户信息，直接放行让下游服务处理
            if (user == EMPTY_AUTH_USER || user.getExpireTime() == null || user.getExpireTime() <= System.currentTimeMillis()) {
                return chain.filter(exchange);
            }

            // 设置登录用户请求头
            return chain.filter(exchange.mutate()
                    .request(builder -> builder.header(AUTH_USER_HEADER, JsonUtils.toJsonString(user))).build());
        });
    }

    private Mono<AuthUser> checkAccessToken(String token) {

        // 从缓存中获取
        AuthUser authUser = TokenAuthenticationCache.get(token);
        if (authUser != null) {
            return Mono.just(authUser);
        }

        // 缓存未命中时，调用认证服务
        return webClient.get()
                .uri(CHECK_TOKEN_URL, uriBuilder -> uriBuilder.queryParam("accessToken", token).build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<R<AuthUser>>() {
                }).flatMap(res -> {
                    if (res.isSuccess()) {
                        TokenAuthenticationCache.put(token, res.getData());
                        return Mono.just(res.getData());
                    }
                    return Mono.empty();
                });

    }

    /**
     * 从请求中，获得认证 Token
     *
     * @param exchange 请求
     * @return 认证 Token
     */
    public static String resolveAuthorization(ServerWebExchange exchange) {
        String authorization = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION_HEADER);
        if (!StringUtils.hasText(authorization)) {
            return null;
        }
        int index = authorization.indexOf(AUTHORIZATION_BEARER + " ");
        if (index == -1) { // 未找到
            return null;
        }
        return authorization.substring(index + 7).trim();
    }


    /**
     * 移除请求头的用户
     *
     * @param exchange 请求
     */
    public static void removeAuthUser(ServerWebExchange exchange) {
        // 如果不包含，直接返回
        if (!exchange.getRequest().getHeaders().containsKey(AUTH_USER_HEADER)) {
            return;
        }
        // 如果包含，则移除。参考 RemoveRequestHeaderGatewayFilterFactory 实现
        ServerHttpRequest request = exchange.getRequest().mutate()
                .headers(httpHeaders -> httpHeaders.remove(AUTH_USER_HEADER)).build();
        exchange.mutate().request(request).build();
    }
}
