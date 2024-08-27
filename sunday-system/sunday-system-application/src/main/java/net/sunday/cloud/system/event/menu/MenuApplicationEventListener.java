package net.sunday.cloud.system.event.menu;

import net.sunday.cloud.system.api.menu.MenuApi;
import net.sunday.cloud.system.event.menu.source.MenuDeletedEvent;
import net.sunday.cloud.system.event.menu.source.MenuStatusChangedEvent;
import org.apache.dubbo.common.constants.ClusterRules;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 菜单相关事件监听器
 */
@Component
public class MenuApplicationEventListener {

    @DubboReference(cluster = ClusterRules.BROADCAST)
    private MenuApi menuApi;

    /**
     * 处理菜单状态变更事件
     */
    @Async
    @EventListener(MenuStatusChangedEvent.class)
    public void handleRoleStatusChangedEvent(MenuStatusChangedEvent event) {
        menuApi.menuStatusCacheEvict(event.getMenuId());
    }

    /**
     * 处理菜单删除事件
     */
    @Async
    @EventListener(MenuDeletedEvent.class)
    public void handleRoleDeletedEvent(MenuDeletedEvent event) {
        menuApi.menuStatusCacheEvict(event.getMenuId());
    }
}
