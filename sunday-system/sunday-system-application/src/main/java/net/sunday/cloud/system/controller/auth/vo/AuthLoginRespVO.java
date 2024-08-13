package net.sunday.cloud.system.controller.auth.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthLoginRespVO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 过期时间戳
     */
    private Long expiresTime;

}
