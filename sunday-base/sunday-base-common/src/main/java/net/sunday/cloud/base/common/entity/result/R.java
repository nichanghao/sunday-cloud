package net.sunday.cloud.base.common.entity.result;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sunday.cloud.base.common.exception.ErrorEnum;
import net.sunday.cloud.base.common.exception.GlobalRespCodeEnum;

import java.io.Serial;
import java.io.Serializable;

/**
 * 响应信息主体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int code;

    private String msg;

    private T data;

    public static <T> R<T> ok() {
        return restResult(null, GlobalRespCodeEnum.SUCCESS.getCode(), null);
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, GlobalRespCodeEnum.SUCCESS.getCode(), null);
    }

    public static <T> R<T> ok(T data, String msg) {
        return restResult(data, GlobalRespCodeEnum.SUCCESS.getCode(), msg);
    }

    public static <T> R<T> failed() {
        return restResult(null, GlobalRespCodeEnum.FAIL.getCode(), null);
    }

    public static <T> R<T> failed(String msg) {
        return restResult(null, GlobalRespCodeEnum.FAIL.getCode(), msg);
    }

    public static <T> R<T> failed(int code, String msg) {
        return restResult(null, code, msg);
    }

    public static <T> R<T> failed(ErrorEnum errorEnum) {
        return restResult(null,
                errorEnum.getCode() == null ? GlobalRespCodeEnum.FAIL.getCode() : errorEnum.getCode(), errorEnum.getMessage());
    }

    public static <T> R<T> restResult(T data, int code, String msg) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

    public boolean isSuccess() {
        return GlobalRespCodeEnum.SUCCESS.getCode() == this.code;
    }

}
