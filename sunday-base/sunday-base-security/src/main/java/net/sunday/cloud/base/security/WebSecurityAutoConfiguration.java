package net.sunday.cloud.base.security;

import net.sunday.cloud.base.security.config.WebSecurityConfiguration;
import net.sunday.cloud.base.security.config.WebSecurityProperties;
import net.sunday.cloud.base.security.service.UserDetailsServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * web security 自动装配
 *
 * @see org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
 */

@AutoConfiguration(before = SecurityAutoConfiguration.class)
@EnableConfigurationProperties(WebSecurityProperties.class)
@Import({WebSecurityConfiguration.class})
public class WebSecurityAutoConfiguration {

    /**
     * 加载用户信息服务
     */
    @Bean
    public UserDetailsServiceImpl userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    /**
     * Spring Security 加密器
     *
     * @see <a href="http://stackabuse.com/password-encoding-with-spring-security/">Password Encoding with Spring Security</a>
     */
    @Bean
    public PasswordEncoder passwordEncoder(WebSecurityProperties securityProperties) {
        return new BCryptPasswordEncoder(securityProperties.getPasswordEncoderLength());
    }
}
