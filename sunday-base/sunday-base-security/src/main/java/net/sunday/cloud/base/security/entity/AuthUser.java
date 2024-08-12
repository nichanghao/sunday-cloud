package net.sunday.cloud.base.security.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * 自定义 security 用户实体类
 */

public class AuthUser extends User {

    /**
     * 用户ID
     */
    private Long id;


    public AuthUser(Long id, String username, String password, boolean enabled, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, true, true, true, authorities);
        this.id = id;
    }
}
