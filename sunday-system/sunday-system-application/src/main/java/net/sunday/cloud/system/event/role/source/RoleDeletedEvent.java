package net.sunday.cloud.system.event.role.source;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 角色删除事件
 */
@Getter
public class RoleDeletedEvent extends ApplicationEvent {

    private final Long roleId;

    public RoleDeletedEvent(Object source, Long roleId) {
        super(source);
        this.roleId = roleId;
    }


}
