package net.sunday.cloud.base.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

/**
 * 自定义 security 用户实体类
 */
@Data
@NoArgsConstructor
public class AuthUser implements UserDetails, CredentialsContainer {

    private Long id;

    @JsonIgnore
    private String password;

    private String username;

    private Set<GrantedAuthority> authorities;

    private boolean enabled;

    /**
     * 过期时间戳
     */
    private Long expireTime;


    public AuthUser(Long id, String username, String password, boolean enabled, Set<GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.authorities = authorities;
    }

    @Override
    public void eraseCredentials() {
        // 密码擦除
        this.password = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
