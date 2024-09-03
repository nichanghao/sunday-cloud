package net.sunday.cloud.base.monitor.util;

import cn.hutool.core.lang.UUID;

public class TraceUtils {

    public static String generateTraceId() {
        return UUID.fastUUID().toString();
    }
}
