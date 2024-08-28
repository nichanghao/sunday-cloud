package net.sunday.cloud.base.dubbo.filter.context;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.spring.security.filter.ContextHolderAuthenticationResolverFilter;
import org.slf4j.MDC;

import static net.sunday.cloud.base.common.constant.MonitorConstants.TRACE_ID;

/**
 * dubbo provider filter 解析 上下文信息
 *
 * @see ContextHolderAuthenticationResolverFilter
 */

@Activate(group = CommonConstants.PROVIDER, order = -10000)
public class ProviderContextFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        // 解析 mdc context
        resolveMDCContext(invocation);

        return invoker.invoke(invocation);
    }

    private void resolveMDCContext(Invocation invocation) {
        String traceId = invocation.getAttachment(TRACE_ID);
        if (StringUtils.isBlank(traceId)) {
            return;
        }

        MDC.put(TRACE_ID, traceId);
    }
}
