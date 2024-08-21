package net.sunday.cloud.base.web.advice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import net.sunday.cloud.base.common.entity.result.R;
import net.sunday.cloud.base.common.exception.BusinessException;
import net.sunday.cloud.base.common.exception.GlobalRespCodeEnum;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static org.springframework.http.HttpStatus.*;


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

        // 只打印第一层 StackTraceElement 信息
        try {
            StackTraceElement[] stackTraces = ex.getStackTrace();
            for (StackTraceElement stackTrace : stackTraces) {
                log.info("[businessExceptionHandler] [{}]", stackTrace);
                break;
            }
        } catch (Exception ignored) {
        }

        return R.failed(ex);
    }

    /**
     * 处理 Spring Security AccessDeniedException 的异常
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public R<?> accessDeniedExceptionHandler(HttpServletRequest req, AccessDeniedException ex) {
        log.warn("[accessDeniedExceptionHandler] [无法访问 url({})]", req.getRequestURL(), ex);
        return R.failed(GlobalRespCodeEnum.ACCESS_DENIED);
    }

    /**
     * 处理 Spring Security AuthenticationException 的异常
     */
    @ExceptionHandler(value = AuthenticationException.class)
    public R<?> authenticationExceptionHandler(HttpServletRequest req, AuthenticationException ex) {
        log.warn("[authenticationExceptionHandler] [未认证 url({})]", req.getRequestURL(), ex);
        return R.failed(GlobalRespCodeEnum.UNAUTHORIZED);
    }

    /**
     * 处理 IllegalArgumentException 的异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public R<?> illegalArgumentExceptionExceptionHandler(IllegalArgumentException ex) {
        log.info("[illegalArgumentExceptionExceptionHandler] [{}]", ex.getMessage());
        return R.failed(String.format("请求参数不正确: %s", ex.getMessage()));
    }

    /**
     * 处理 SpringMVC 请求方法不正确
     * eg: A 接口的方法为 GET 方式，结果请求方法为 POST 方式，导致不匹配
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R<?> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException ex) {
        log.info("[httpRequestMethodNotSupportedExceptionHandler] [{}]", ex.getMessage());
        return R.failed(METHOD_NOT_ALLOWED.value(), String.format("请求方法不正确: %s", ex.getMessage()));
    }

    /**
     * 处理 NoResourceFoundException
     * eg: 请求的静态资源不存在
     */
    @ExceptionHandler(NoResourceFoundException.class)
    private R<?> noResourceFoundExceptionHandler(NoResourceFoundException ex) {
        log.warn("[noResourceFoundExceptionHandler] [{}]", ex.getMessage());
        return R.failed(NOT_FOUND.value(), String.format("请求地址不存在: %s", ex.getResourcePath()));
    }

    /**
     * 处理 SpringMVC 请求参数缺失异常
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public R<?> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException ex) {
        log.warn("[missingServletRequestParameterExceptionHandler]", ex);
        return R.failed(BAD_REQUEST.value(), String.format("请求参数缺失: %s", ex.getParameterName()));
    }

    /**
     * 处理 SpringMVC 请求参数类型匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public R<?> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException ex) {
        log.warn("[missingServletRequestParameterExceptionHandler]", ex);
        return R.failed(BAD_REQUEST.value(), String.format("请求参数类型错误: %s", ex.getMessage()));
    }

    /**
     * 处理 SpringMVC 参数校验不正确异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<?> methodArgumentNotValidExceptionExceptionHandler(MethodArgumentNotValidException ex) {
        log.warn("[methodArgumentNotValidExceptionExceptionHandler] [{}]", ex.getMessage());
        FieldError fieldError = ex.getBindingResult().getFieldError();
        assert fieldError != null;
        return R.failed(BAD_REQUEST.value(), String.format("请求参数不正确: %s", fieldError.getDefaultMessage()));
    }

    /**
     * 处理 SpringMVC 参数绑定不正确异常
     */
    @ExceptionHandler(BindException.class)
    public R<?> bindExceptionHandler(BindException ex) {
        log.warn("[handleBindException]", ex);
        FieldError fieldError = ex.getFieldError();
        assert fieldError != null;
        return R.failed(BAD_REQUEST.value(), String.format("请求参数不正确: %s", fieldError.getDefaultMessage()));
    }

    /**
     * 处理 Validator 校验不通过产生的异常
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public R<?> constraintViolationExceptionHandler(ConstraintViolationException ex) {
        log.warn("[constraintViolationExceptionHandler]", ex);
        ConstraintViolation<?> constraintViolation = ex.getConstraintViolations().iterator().next();
        return R.failed(BAD_REQUEST.value(), String.format("请求参数不正确: %s", constraintViolation.getMessage()));
    }

    /**
     * 兜底处理系统异常
     */
    @ExceptionHandler(value = Exception.class)
    public R<?> globalExceptionHandler(Throwable ex) {
        log.error("[globalExceptionHandler]", ex);
        return R.failed(GlobalRespCodeEnum.SERVER_INTERNAL_ERROR);
    }

    /**
     * 处理所有异常，主要是提供给 Filter 使用
     * 因为 Filter 中抛出的异常无法被 ExceptionHandler 捕获
     *
     * @param request 请求
     * @param e       异常
     * @return 通用返回
     */
    public R<?> filterExceptionHandler(HttpServletRequest request, Throwable e) {

        if (e instanceof BusinessException ex) {
            return businessExceptionHandler(ex);
        }
        if (e instanceof AccessDeniedException ex) {
            return accessDeniedExceptionHandler(request, ex);
        }
        if (e instanceof AuthenticationException ex) {
            return authenticationExceptionHandler(request, ex);
        }
        if (e instanceof IllegalArgumentException ex) {
            return illegalArgumentExceptionExceptionHandler(ex);
        }
        return globalExceptionHandler(e);
    }


}
