package net.sunday.cloud.system.controller.admin.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "用户管理 - 用户信息")
@Data
public class UserRespVO {

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "用户账号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Schema(description = "用户昵称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nickname;

    @Schema(description = "用户邮箱")
    private String email;

    @Schema(description = "手机号码")
    private String phone;

    @Schema(description = "用户性别(0:保密,1:男,2:女)", example = "1")
    private Integer gender;

    @Schema(description = "用户状态(1:启用,0:停用)", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "毫秒时间戳格式")
    private LocalDateTime updateTime;

}
