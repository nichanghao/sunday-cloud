package net.sunday.cloud.base.security.service;

import lombok.AllArgsConstructor;
import net.sunday.cloud.base.security.entity.AuthUser;
import net.sunday.cloud.system.api.user.SysUserApi;
import net.sunday.cloud.system.api.user.dto.SysUserRespDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysUserApi sysUserApi;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SysUserRespDTO user = sysUserApi.loadUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User Not Found");
        }

        return new AuthUser(user.getId(), user.getUsername(), user.getPassword(), user.getStatus(), Collections.emptyList());

    }
}
