package net.sunday.cloud.system.repository.cache.redis;

import cn.hutool.core.text.StrPool;
import jakarta.annotation.Resource;
import net.sunday.cloud.base.common.entity.auth.AuthUser;
import net.sunday.cloud.base.common.exception.BusinessException;
import net.sunday.cloud.base.common.exception.GlobalRespCodeEnum;
import net.sunday.cloud.base.common.util.json.JsonUtils;
import net.sunday.cloud.system.util.JwtUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 用户认证Redis DAO
 */

@Repository
public class AuthRedisDAO {

    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String AUTH_USER_KEY_PREFIX = "system:auth:user:";

    public void setAuthUser(Serializable uid, String client, AuthUser authUser) {
        stringRedisTemplate.opsForValue().set(
                formatKey(uid, client), JsonUtils.toJsonString(authUser), jwtUtils.getExpiration(), TimeUnit.HOURS);
    }

    public AuthUser getAuthUser(Serializable uid, String client) {

        AuthUser authUser = JsonUtils.parseObject(stringRedisTemplate.opsForValue().get(formatKey(uid, client)), AuthUser.class);
        if (authUser == null) {
            throw new BusinessException(GlobalRespCodeEnum.UNAUTHORIZED);
        }
        return authUser;
    }

    public void removeAuthUser(Serializable uid, String client) {
        stringRedisTemplate.delete(formatKey(uid, client));
    }

    private static String formatKey(Serializable uid, String client) {
        return AUTH_USER_KEY_PREFIX + client + StrPool.COLON + uid;
    }
}
