package net.sunday.cloud.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.sunday.cloud.base.mybatis.entity.BaseDO;

import java.io.Serial;

/**
 * 角色表
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_role")
public class RoleDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 系统内置角色ID
     */
    public static final Long BUILT_IN_ROLE_ID = 1L;

    public RoleDO(Long id) {
        this.id = id;
    }

    /**
     * 角色ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色名
     */
    private String name;

    /**
     * 角色标识
     */
    private String code;

    /**
     * 状态(1:启用 0:禁用)
     *
     * @see net.sunday.cloud.base.common.enums.CommonStatusEnum
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}
