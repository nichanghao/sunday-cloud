package net.sunday.cloud.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import net.sunday.cloud.base.mybatis.entity.BaseDO;

import java.io.Serial;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author mybatis-plus-generator
 * @since 2024-08-09
 */
@Getter
@Setter
@TableName("sys_role")
public class SysRoleDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

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
     */
    private Boolean status;

    /**
     * 备注
     */
    private String desc;
}
