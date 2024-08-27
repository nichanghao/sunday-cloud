package net.sunday.cloud.system.event.menu.source;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 菜单删除事件
 */
@Getter
public class MenuDeletedEvent extends ApplicationEvent {

    private final Long menuId;

    public MenuDeletedEvent(Object source, Long menuId) {
        super(source);
        this.menuId = menuId;
    }


}
