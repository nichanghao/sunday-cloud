package net.sunday.cloud.system.repository.redis;

import jakarta.annotation.Resource;
import net.sunday.cloud.base.common.entity.auth.AuthUser;
import net.sunday.cloud.base.common.exception.BusinessException;
import net.sunday.cloud.base.common.exception.GlobalRespCodeEnum;
import net.sunday.cloud.base.common.util.json.JsonUtils;
import net.sunday.cloud.system.util.JwtUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

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

    public void setAuthUser(String key, AuthUser authUser) {
        stringRedisTemplate.opsForValue().set(
                AUTH_USER_KEY_PREFIX + key, JsonUtils.toJsonString(authUser), jwtUtils.getExpiration(), TimeUnit.HOURS);
    }

    public AuthUser getAuthUser(String key) {

        AuthUser authUser = JsonUtils.parseObject(stringRedisTemplate.opsForValue().get(AUTH_USER_KEY_PREFIX + key), AuthUser.class);
        if (authUser == null) {
            throw new BusinessException(GlobalRespCodeEnum.UNAUTHORIZED);
        }
        return authUser;
    }
}
