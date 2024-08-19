package net.sunday.cloud.system.enums.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 性别的枚举值
 */
@Getter
@AllArgsConstructor
public enum GenderEnum {

    /* 保密 */
    UNKNOWN(0),
    /** 男 */
    MALE(1),
    /** 女 */
    FEMALE(2),
    ;

    private final Integer sex;

}
