package net.sunday.cloud.system.api.role;

import jakarta.annotation.Resource;
import net.sunday.cloud.system.repository.cache.caffeine.RoleStatusCaffeineDAO;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class RoleApiService implements RoleApi {

    @Resource
    private RoleStatusCaffeineDAO roleStatusCaffeineDAO;

    @Override
    public void roleStatusCacheEvict(Long roleId) {
        roleStatusCaffeineDAO.invalidate(roleId);
    }
}
