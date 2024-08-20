package net.sunday.cloud.base.common.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * 业务异常
 */

@NoArgsConstructor
public class BusinessException extends RuntimeException implements ErrorEnum {

    @Serial
    private static final long serialVersionUID = 1L;

    @Getter
    private Integer code;

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
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    private static String formatMessage(String message, Object... formats) {
        if (formats == null || formats.length == 0) {
            return message;
        }
        return String.format(message, formats);
    }

}
