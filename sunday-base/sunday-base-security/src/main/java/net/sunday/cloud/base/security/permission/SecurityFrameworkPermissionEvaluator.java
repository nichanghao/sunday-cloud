package net.sunday.cloud.base.security.permission;

import cn.hutool.core.util.ArrayUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.sunday.cloud.base.security.util.SecurityFrameworkUtils;
import net.sunday.cloud.system.api.permission.PermissionApi;
import org.apache.dubbo.config.annotation.DubboReference;

import java.time.Duration;

/**
 * security框架权限验证器
 */
public class SecurityFrameworkPermissionEvaluator {

    @DubboReference(check = true, providedBy = "sunday-cloud-system")
    private PermissionApi permissionApi;

    private static final Cache<String, Boolean> permissionCache = Caffeine.newBuilder()
            .maximumSize(100_000)
            // 缓存有效期为1分钟
            .expireAfterWrite(Duration.ofMinutes(1))
            .initialCapacity(100)
            .build();

    /**
     * 判断是否有权限
     *
     * @param permission 权限
     * @return 是否
     */
    public boolean hasPermission(String permission) {
        return this.hasAnyPermissions(permission);
    }

    /**
     * 判断是否有权限，任一一个即可
     *
     * @param permissions 权限
     * @return 是否
     */
    public boolean hasAnyPermissions(String... permissions) {
        // 1. 获取当前登录的用户
        Long authUserId = SecurityFrameworkUtils.getAuthUserId();
        if (authUserId == null) {
            return false;
        }

        // 2. 从本地缓存中判断是否有权限
        String key = authUserId + ArrayUtil.join(permissions, "#");
        if (Boolean.TRUE.equals(permissionCache.getIfPresent(key))) {
            return true;
        }

        // 3. 从服务端判断是否有权限
        boolean hasPermission = permissionApi.hasAnyPermissions(authUserId, permissions);
        if (hasPermission) {
            permissionCache.put(key, Boolean.TRUE);
        }
        return hasPermission;
    }
}
