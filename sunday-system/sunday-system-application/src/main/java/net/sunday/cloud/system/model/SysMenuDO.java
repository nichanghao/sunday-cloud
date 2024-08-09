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
 * 菜单表
 * </p>
 *
 * @author mybatis-plus-generator
 * @since 2024-08-09
 */
@Getter
@Setter
@TableName("sys_menu")
public class SysMenuDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 路由名称
     */
    private String routeName;

    /**
     * 菜单类型(1:目录,2:菜单,3:按钮)
     */
    private Integer type;

    /**
     * 权限标识
     */
    private String permission;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件
     */
    private String component;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 菜单状态(0:禁用,1:启用)
     */
    private Boolean status;

    /**
     * 路由元数据
     */
    private String meta;
}