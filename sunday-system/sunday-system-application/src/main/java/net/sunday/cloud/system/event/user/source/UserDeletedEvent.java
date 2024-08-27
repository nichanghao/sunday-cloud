package net.sunday.cloud.system.event.user.source;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 用户删除事件
 */
@Getter
public class UserDeletedEvent extends ApplicationEvent {

    private final Long userId;

    public UserDeletedEvent(Object source, Long userId) {
        super(source);
        this.userId = userId;
    }


}
