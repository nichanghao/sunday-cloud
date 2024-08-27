package net.sunday.cloud.system.event.user.source;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 用户状态改变事件
 */

@Getter
public class UserStatusChangedEvent extends ApplicationEvent {

    private final Long userId;

    private final Integer status;

    public UserStatusChangedEvent(Object source, Long userId, Integer status) {
        super(source);
        this.userId = userId;
        this.status = status;
    }
}
