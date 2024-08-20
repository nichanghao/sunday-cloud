package net.sunday.cloud.gateway.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.sunday.cloud.base.common.entity.auth.AuthUser;

import java.time.Duration;

/**
 * token 认证本地缓存
 */

public class TokenAuthenticationCache {

    private static final Cache<String, AuthUser> tokenCache = Caffeine.newBuilder()
            .maximumSize(100_000)
            // 缓存有效期为1分钟
            .expireAfterWrite(Duration.ofMinutes(1))
            .initialCapacity(100)
            .build();


    public static void put(String token, AuthUser value) {
        tokenCache.put(token, value);
    }

    public static AuthUser get(String token) {
        return tokenCache.getIfPresent(token);
    }

}
