package net.sunday.cloud.base.dubbo.filter.context;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.spring.security.filter.ContextHolderAuthenticationPrepareFilter;
import org.slf4j.MDC;

import static net.sunday.cloud.base.common.constant.MonitorConstants.TRACE_ID;

/**
 * dubbo consumer filter 设置 上下文信息, security 上下文已被默认激活
 *
 * @see ContextHolderAuthenticationPrepareFilter
 */

@Activate(group = CommonConstants.CONSUMER, order = -10000)
public class ConsumerContextFilter implements Filter {


    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        // 设置 MDC 上下文
        setMDCContext(invocation);

        return invoker.invoke(invocation);
    }

    private void setMDCContext(Invocation invocation) {
        String traceId = MDC.get(TRACE_ID);
        if (StringUtils.isBlank(traceId)) {
            return;
        }

        invocation.setAttachment(TRACE_ID, traceId);
    }
}
