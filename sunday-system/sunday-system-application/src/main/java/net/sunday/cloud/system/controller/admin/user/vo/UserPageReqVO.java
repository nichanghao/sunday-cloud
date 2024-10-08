package net.sunday.cloud.system.controller.admin.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.sunday.cloud.base.common.entity.page.PageParam;

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

    @Schema(description = "用户状态(1:启用,0:停用)")
    @Min(value = 0, message = "状态值必须大于等于0")
    @Max(value = 1, message = "状态值必须小于等于1")
    private Integer status;

}
