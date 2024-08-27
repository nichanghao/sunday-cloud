package net.sunday.cloud.system.event.menu.source;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 菜单状态改变事件
 */
@Getter
public class MenuStatusChangedEvent extends ApplicationEvent {

    private final Long menuId;

    private final Integer status;


    public MenuStatusChangedEvent(Object source, Long menuId, Integer status) {
        super(source);
        this.menuId = menuId;
        this.status = status;
    }


}
