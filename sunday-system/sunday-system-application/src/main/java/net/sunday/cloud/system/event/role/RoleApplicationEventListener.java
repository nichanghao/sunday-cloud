package net.sunday.cloud.system.event.role;

import net.sunday.cloud.system.api.role.RoleApi;
import net.sunday.cloud.system.event.role.source.RoleDeletedEvent;
import net.sunday.cloud.system.event.role.source.RoleStatusChangedEvent;
import org.apache.dubbo.common.constants.ClusterRules;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 角色相关事件监听器
 */
@Component
public class RoleApplicationEventListener {

    @DubboReference(cluster = ClusterRules.BROADCAST)
    private RoleApi roleApi;

    /**
     * 处理角色状态变更事件
     */
    @Async
    @EventListener(RoleStatusChangedEvent.class)
    public void handleRoleStatusChangedEvent(RoleStatusChangedEvent event) {
        roleApi.roleStatusCacheEvict(event.getRoleId());
    }

    /**
     * 处理角色删除事件
     */
    @Async
    @EventListener(RoleDeletedEvent.class)
    public void handleRoleDeletedEvent(RoleDeletedEvent event) {
        roleApi.roleStatusCacheEvict(event.getRoleId());
    }
}
