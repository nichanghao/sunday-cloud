package net.sunday.cloud.system.controller.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用户登录响应信息")
public class AuthLoginRespVO {

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    /**
     * 访问令牌
     */
    @Schema(description = "访问令牌", requiredMode = Schema.RequiredMode.REQUIRED)
    private String accessToken;

    /**
     * 过期时间戳
     */
    @Schema(description = "过期时间戳", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long expiresTime;

}
