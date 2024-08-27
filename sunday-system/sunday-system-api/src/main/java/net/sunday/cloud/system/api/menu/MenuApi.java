package net.sunday.cloud.system.api.menu;

/**
 * 系统菜单API接口
 */

public interface MenuApi {

    /**
     * 菜单状态缓存失效
     *
     * @param menuId 菜单ID
     */
    void menuStatusCacheEvict(Long menuId);
}
