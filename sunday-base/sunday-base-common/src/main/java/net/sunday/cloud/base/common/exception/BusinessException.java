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

	public BusinessException(Integer code, String message) {
		super(message);
		this.code = code;
	}

	public BusinessException(String message) {
		super(message);
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

}
