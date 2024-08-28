package net.sunday.cloud.base.dubbo.filter.exception;

import cn.hutool.core.text.StrPool;
import net.sunday.cloud.base.common.exception.BusinessException;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import static net.sunday.cloud.base.dubbo.filter.exception.BusinessExceptionTranslatorFilter.BUSINESS_EXCEPTION_CODE;

/**
 * Rpc异常过滤器
 *
 * @see org.apache.dubbo.rpc.filter.RpcExceptionFilter
 */
@Activate(group = CommonConstants.CONSUMER)
public class BusinessExceptionResolverFilter implements Filter, Filter.Listener {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        return invoker.invoke(invocation);
    }

    @Override
    public void onResponse(Result result, Invoker<?> invoker, Invocation invocation) {
        if (!result.hasException()) {
            return;
        }

        if (result.getException() instanceof RpcException ex) {
            if (ex.getCode() == BUSINESS_EXCEPTION_CODE) {
                String message = ex.getMessage();
                String[] split = message.split(StrPool.AT);
                throw new BusinessException(Integer.parseInt(split[0]), split[1]);
            }
            throw ex;
        }

    }

    @Override
    public void onError(Throwable t, Invoker<?> invoker, Invocation invocation) {

    }
}
