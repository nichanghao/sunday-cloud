package net.sunday.cloud.base.dubbo.filter.exception;


import cn.hutool.core.text.StrPool;
import net.sunday.cloud.base.common.exception.BusinessException;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * business exception translator filter，用于将业务异常转换为dubbo的RpcException
 *
 * @see net.sunday.cloud.base.common.exception.BusinessException
 * @see org.apache.dubbo.spring.security.filter.AuthenticationExceptionTranslatorFilter
 * @see org.apache.dubbo.rpc.RpcException
 */

@Activate(group = CommonConstants.PROVIDER, order = Integer.MAX_VALUE)
public class BusinessExceptionTranslatorFilter implements Filter, Filter.Listener {

    static final int BUSINESS_EXCEPTION_CODE = 10000;

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        return invoker.invoke(invocation);
    }

    @Override
    public void onResponse(Result result, Invoker<?> invoker, Invocation invocation) {
        if (!result.hasException()) {
            return;
        }

        if (result.getException() instanceof BusinessException bx) {
            RpcException rpcException = new RpcException(bx.getCode() + StrPool.AT + bx.getMessage());

            rpcException.setCode(BUSINESS_EXCEPTION_CODE);

            result.setException(rpcException);
        }

    }

    @Override
    public void onError(Throwable t, Invoker<?> invoker, Invocation invocation) {
    }

}
