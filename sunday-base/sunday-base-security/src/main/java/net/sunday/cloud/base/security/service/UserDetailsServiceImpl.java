package net.sunday.cloud.base.security.service;

import net.sunday.cloud.base.common.entity.auth.AuthUser;
import net.sunday.cloud.system.api.user.UserApi;
import net.sunday.cloud.system.api.user.dto.UserRespDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {

    @DubboReference(check = true, providedBy = "sunday-cloud-system")
    private UserApi userApi;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserRespDTO user = userApi.loadUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User Not Found");
        }

        return new AuthUser(user.getId(), user.getUsername(), user.getPassword(), user.getStatus());

    }
}
