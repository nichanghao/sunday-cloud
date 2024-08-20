package net.sunday.cloud.system.enums.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 菜单类型枚举类
 */
@Getter
@AllArgsConstructor
public enum MenuTypeEnum {

    /**
     * 目录
     */
    DIR(1),

    /**
     * 菜单
     */
    MENU(2),

    /**
     * 按钮
     */
    BUTTON(3);

    /**
     * 类型
     */
    private final Integer type;

}
