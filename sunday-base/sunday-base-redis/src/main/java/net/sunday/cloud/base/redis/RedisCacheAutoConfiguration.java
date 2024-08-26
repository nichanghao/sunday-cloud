package net.sunday.cloud.base.redis;

import cn.hutool.core.text.StrPool;
import net.sunday.cloud.base.redis.manager.ExpirationTimeRedisCacheManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

/**
 * 基于 redis 实现的 Spring Cache 自动配置类
 * org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration
 */

@AutoConfiguration
@EnableCaching
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(CacheProperties.class)
public class RedisCacheAutoConfiguration {

    @Bean
    public RedisCacheManager redisCacheManager(CacheProperties cacheProperties, RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                // 设置key的层次结构分割符，默认是"::"，设置为":"
                .computePrefixWith(cacheKeyPrefix -> cacheKeyPrefix + StrPool.COLON)
                // 设置value的序列化方式为json，默认为JavaSerializer
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair.fromSerializer(RedisAutoConfiguration.buildRedisJsonSerializer()));

        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        } else {
            // 默认过期时间为2天
            config = config.entryTtl(Duration.ofHours(48));
        }
        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixCacheNameWith(redisProperties.getKeyPrefix());
        }
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }

        return new ExpirationTimeRedisCacheManager(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory), config);
    }
}
