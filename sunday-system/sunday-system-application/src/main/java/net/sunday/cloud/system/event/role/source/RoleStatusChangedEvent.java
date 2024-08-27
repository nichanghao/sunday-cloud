package net.sunday.cloud.system.event.role.source;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 角色状态改变事件
 */
@Getter
public class RoleStatusChangedEvent extends ApplicationEvent {

    private final Long roleId;

    private final Integer status;


    public RoleStatusChangedEvent(Object source, Long roleId, Integer status) {
        super(source);
        this.roleId = roleId;
        this.status = status;
    }


}
