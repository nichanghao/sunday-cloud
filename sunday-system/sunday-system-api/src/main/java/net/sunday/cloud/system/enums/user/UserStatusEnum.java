package net.sunday.cloud.system.enums.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态的枚举值
 */
@Getter
@AllArgsConstructor
public enum UserStatusEnum {

    /* 禁用 */
    DISABLED(0),
    /** 启动 */
    ENABLED(1),
    ;

    private final Integer status;

}
