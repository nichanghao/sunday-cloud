package net.sunday.cloud.system.repository.cache.redis.constant;


/**
 * System Redis Key 常量类
 */
public interface RedisKeyConstants {

    /**
     * 用户拥有的角色编号的缓存
     * KEY 格式：user_role_ids:{userId}
     * VALUE 格式：角色编号数组
     */
    String USER_ROLE_ID_LIST = "user_role_ids";

    /**
     * 拥有权限对应的菜单编号数组的缓存
     * KEY 格式：permission_menu_ids:{permission}
     * VALUE 格式：菜单编号数组
     */
    String PERMISSION_MENU_ID_LIST = "permission_menu_ids";

    /**
     * 拥有指定菜单的角色编号的缓存
     * KEY 格式：user_role_ids:{menuId}
     * VALUE 格式：角色编号数组
     */
    String MENU_ROLE_ID_LIST = "menu_role_ids";

}
