package net.sunday.cloud.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import net.sunday.cloud.base.mybatis.entity.BaseDO;
import net.sunday.cloud.system.enums.menu.MenuTypeEnum;

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
@TableName(value = "sys_menu", autoResultMap = true)
public class SysMenuDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    public SysMenuDO(Long id) {
        this.id = id;
    }
    /**
     * 菜单编号 - 根节点
     */
    public static final Long ID_ROOT = 0L;

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
     *
     * @see MenuTypeEnum
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
    @TableField(typeHandler = JacksonTypeHandler.class)
    private JsonNode meta;
}
