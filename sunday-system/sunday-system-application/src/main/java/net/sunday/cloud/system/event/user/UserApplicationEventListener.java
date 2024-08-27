package net.sunday.cloud.system.event.user;

import jakarta.annotation.Resource;
import net.sunday.cloud.base.common.enums.CommonStatusEnum;
import net.sunday.cloud.system.enums.auth.AuthClientEnum;
import net.sunday.cloud.system.event.user.source.UserDeletedEvent;
import net.sunday.cloud.system.event.user.source.UserStatusChangedEvent;
import net.sunday.cloud.system.repository.cache.redis.AuthRedisDAO;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 用户相关事件监听器
 */
@Component
public class UserApplicationEventListener {

    @Resource
    private AuthRedisDAO authRedisDAO;

    /**
     * 处理用户状态变更事件
     */
    @Async
    @EventListener(UserStatusChangedEvent.class)
    public void handleUserStatusChangedEvent(UserStatusChangedEvent event) {
        // 禁用用户时，清除认证token
        if (Objects.equals(event.getStatus(), CommonStatusEnum.DISABLE.ordinal())) {
            authRedisDAO.removeAuthUser(event.getUserId(), AuthClientEnum.ADMIN.client());
        }
    }

    /**
     * 处理用户删除事件
     */
    @Async
    @EventListener(UserDeletedEvent.class)
    public void handleRoleDeletedEvent(UserDeletedEvent event) {
        authRedisDAO.removeAuthUser(event.getUserId(), AuthClientEnum.ADMIN.client());
    }
}
