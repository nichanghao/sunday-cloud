package net.sunday.cloud.base.common.exception;

import lombok.Getter;

import java.io.Serial;

/**
 * 业务异常
 */

@Getter
public class BusinessException extends RuntimeException implements ErrorEnum {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Integer code;

    public BusinessException(Integer code, String message, Object... formats) {
        super(formatMessage(message, formats));
        this.code = code;
    }

    public BusinessException(ErrorEnum errorEnum, Object... formats) {
        super(formatMessage(errorEnum.getMessage(), formats));
        this.code = errorEnum.getCode();
    }

    public BusinessException(String message, Object... formats) {
        super(formatMessage(message, formats));
        this.code = GlobalRespCodeEnum.FAIL.getCode();
    }

    public BusinessException(Throwable cause) {
        super(cause);
        this.code = GlobalRespCodeEnum.FAIL.getCode();
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = GlobalRespCodeEnum.FAIL.getCode();
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = GlobalRespCodeEnum.FAIL.getCode();
    }

    private static String formatMessage(String message, Object... formats) {
        if (formats == null || formats.length == 0) {
            return message;
        }
        return String.format(message, formats);
    }

}
