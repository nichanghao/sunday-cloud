package net.sunday.cloud.base.dubbo.filter;


import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.service.GenericService;

/**
 * dubbo 接口调用日志拦截器
 */
@Slf4j
@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER})
public class StatisticalLogFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String invokeMethod = invoker.getInterface().getName() + "." + invocation.getMethodName();
        Object[] arguments = invocation.getArguments();
        Object invokeRes = null;

        long begin = System.currentTimeMillis();
        Throwable throwable = null;
        try {
            Result result = invoker.invoke(invocation);
            if (result.hasException() && GenericService.class != invoker.getInterface()) {
                throwable = result.getException();
            }

            invokeRes = result.getValue();
            return result;
        } catch (Exception e) {
            throwable = e;
            throw e;
        } finally {
            float cost = (System.currentTimeMillis() - begin) / (float) 1000;
            if (throwable != null) {
                log.error("dubbo 调用异常, invokeMethod: {}, arguments: {}, ex: {}", invokeMethod, arguments, throwable.getMessage(), throwable);
            } else {
                log.info("dubbo 调用成功, invokeMethod: {}, arguments: {}, result: {}, cost: {}s", invokeMethod, arguments, invokeRes, cost);
            }
        }
    }
}
