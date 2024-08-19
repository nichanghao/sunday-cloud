package net.sunday.cloud.system.controller.admin.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "用户管理 - 创建/修改请求参数")
public class UserUpsertReqVO {

    @Schema(description = "用户ID", example = "1")
    private Long id;

    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(description = "用户昵称", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(max = 32, message = "用户昵称长度不能超过32个字符")
    private String nickname;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    private String password;

    /**
     * @see net.sunday.cloud.system.enums.user.GenderEnum
     */
    @Schema(description = "用户性别(0:保密,1:男,2:女)", example = "1")
    @NotBlank(message = "用户性别不能为空")
    @Min(value = 0, message = "用户性别参数不能小于0")
    @Max(value = 2, message = "用户性别参数不能大于2")
    private Integer gender;

    @Schema(description = "手机号码")
    private String phone;

    @Schema(description = "用户邮箱")
    @Email(message = "邮箱格式不正确")
    @Size(max = 64, message = "邮箱长度不能超过 64 个字符")
    private String email;

}
