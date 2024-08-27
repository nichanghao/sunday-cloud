package net.sunday.cloud.system.enums.auth;

import lombok.AllArgsConstructor;

/**
 * 认证客户端枚举
 */
@AllArgsConstructor
public enum AuthClientEnum {

    /**
     * 管理后台
     */
    ADMIN("admin"),

    /**
     * APP
     */
    APP("app"),
    ;

    /**
     * 客户端标识
     */
    private final String client;

    public String client() {
        return client;
    }

}
