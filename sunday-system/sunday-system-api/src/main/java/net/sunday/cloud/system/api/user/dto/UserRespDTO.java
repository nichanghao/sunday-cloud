package net.sunday.cloud.system.api.user.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 系统用户响应 DTO
 */
@Data
public class UserRespDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1L;

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户登录名
     */
    private String username;

    /**
     * 用户登录密码
     */
    private String password;

    /**
     * 用户状态(1:正常,0:停用)
     */
    private Boolean status;

}
