package net.sunday.cloud.base.common.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用状态枚举
 */
@Getter
@AllArgsConstructor
public enum CommonStatusEnum {
    /**
     * 0: 禁用
     */
    DISABLE,

    /**
     * 1: 启用
     */
    ENABLE,
    ;


    public static boolean isEnable(Integer status) {
        return ObjUtil.equal(ENABLE.ordinal(), status);
    }

    public static boolean isDisable(Integer status) {
        return ObjUtil.equal(DISABLE.ordinal(), status);
    }

}
