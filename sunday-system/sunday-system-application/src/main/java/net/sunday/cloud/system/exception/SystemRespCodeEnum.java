package net.sunday.cloud.system.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.sunday.cloud.base.common.exception.ErrorEnum;

/**
 * system服务 响应码枚举类
 */
@Getter
@AllArgsConstructor
public enum SystemRespCodeEnum implements ErrorEnum {

    TOKEN_EXPIRED(10001, "token已过期，请重新登录"),

    ;


    private final Integer code;

    private final String message;
}
