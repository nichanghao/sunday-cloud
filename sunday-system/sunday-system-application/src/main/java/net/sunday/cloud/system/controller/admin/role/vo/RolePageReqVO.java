package net.sunday.cloud.system.controller.admin.role.vo;

import cn.hutool.core.date.DatePattern;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.sunday.cloud.base.common.entity.page.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Schema(description = "角色管理 - 角色分页查询条件")
@Data
@EqualsAndHashCode(callSuper = true)
public class RolePageReqVO extends PageParam {

    @Schema(description = "角色名称，模糊匹配")
    private String name;

    @Schema(description = "角色标识，模糊匹配")
    private String code;

    @Schema(description = "状态(1:启用 0:禁用)", example = "1")
    @Min(value = 0, message = "状态值必须大于等于0")
    @Max(value = 1, message = "状态值必须小于等于1")
    private Integer status;

    @Schema(description = "修改时间", example = "[2024-07-01 00:00:00, 2024-08-01 23:59:59]")
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime[] updateTimes;

}
