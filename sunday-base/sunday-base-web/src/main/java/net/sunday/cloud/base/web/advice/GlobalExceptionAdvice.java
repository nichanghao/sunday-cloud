package net.sunday.cloud.base.web.advice;

import lombok.extern.slf4j.Slf4j;
import net.sunday.cloud.base.common.entity.R;
import net.sunday.cloud.base.common.exception.BusinessException;
import net.sunday.cloud.base.common.exception.GlobalRespCodeEnum;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {


    /**
     * 处理业务异常 BusinessException
     */
    @ExceptionHandler(value = BusinessException.class)
    public R<?> businessExceptionHandler(BusinessException ex) {

        log.info("BusinessException: ", ex);

        return R.failed(ex);
    }

    /**
     * 兜底处理系统异常
     */
    @ExceptionHandler(value = Exception.class)
    public R<?> globalExceptionHandler(Throwable ex) {

        log.error("GlobalException: {}", ex.getMessage(), ex);

        return R.failed(GlobalRespCodeEnum.SERVER_INTERNAL_ERROR);
    }


}
