package net.sunday.cloud.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.sunday.cloud.base.mybatis.entity.BaseDO;

import java.io.Serial;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author mybatis-plus-generator
 * @since 2024-08-09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
@EqualsAndHashCode(callSuper = true)
public class SysUserDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户登录名
     */
    private String username;

    /**
     * 用户登录密码
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 性别(1:男,2:女)
     *
     * @see net.sunday.cloud.system.enums.user.GenderEnum
     */
    private Integer gender;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户状态(1:启用,0:停用)
     *
     * @see net.sunday.cloud.system.enums.user.UserStatusEnum
     */
    private Integer status;
}
