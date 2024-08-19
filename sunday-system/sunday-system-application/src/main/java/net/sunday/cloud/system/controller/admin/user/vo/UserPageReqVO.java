package net.sunday.cloud.system.controller.admin.user.vo;

import cn.hutool.core.date.DatePattern;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.sunday.cloud.base.common.entity.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Schema(description = "用户管理 - 用户分页查询条件")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserPageReqVO extends PageParam {

    @Schema(description = "用户账号，模糊匹配")
    private String username;

    @Schema(description = "手机号码，模糊匹配")
    private String phone;

    @Schema(description = "用户状态(true:启动,false:停用)")
    private Integer status;

    @Schema(description = "创建时间", example = "[2024-07-01 00:00:00, 2024-08-01 23:59:59]")
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime[] createTime;

}
