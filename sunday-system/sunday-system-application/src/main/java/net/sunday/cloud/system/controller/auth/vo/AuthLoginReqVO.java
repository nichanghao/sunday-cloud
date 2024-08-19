package net.sunday.cloud.system.controller.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "用户登录请求信息")
public class AuthLoginReqVO {

    @NotEmpty(message = "登录账号不能为空")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "账号格式为数字以及字母")
    @Schema(description = "账号", requiredMode = Schema.RequiredMode.REQUIRED, example = "admin")
    private String username;

    @NotEmpty(message = "密码不能为空")
    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    private String password;

}
