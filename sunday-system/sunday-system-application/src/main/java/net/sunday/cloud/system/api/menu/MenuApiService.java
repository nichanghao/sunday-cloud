package net.sunday.cloud.system.api.menu;

import jakarta.annotation.Resource;
import net.sunday.cloud.system.repository.cache.caffeine.MenuStatusCaffeineDAO;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class MenuApiService implements MenuApi {

    @Resource
    private MenuStatusCaffeineDAO menuStatusCaffeineDAO;

    @Override
    public void menuStatusCacheEvict(Long menuId) {
        menuStatusCaffeineDAO.invalidate(menuId);
    }
}
