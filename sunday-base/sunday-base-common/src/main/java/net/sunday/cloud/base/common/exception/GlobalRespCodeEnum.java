package net.sunday.cloud.base.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 全局响应码枚举类
 */

@Getter
@AllArgsConstructor
public enum GlobalRespCodeEnum implements ErrorEnum {


    SUCCESS(200, "成功"),

    FAIL(-1, "失败"),

    UNAUTHORIZED(401, "未授权"),

    SERVER_INTERNAL_ERROR(500, "服务器内部错误"),
    ;


    private final Integer code;

    private final String message;


}
