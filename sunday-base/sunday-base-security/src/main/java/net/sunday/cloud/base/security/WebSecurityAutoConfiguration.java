package net.sunday.cloud.base.security;

import net.sunday.cloud.base.common.properties.RestWebProperties;
import net.sunday.cloud.base.security.config.WebSecurityConfiguration;
import net.sunday.cloud.base.security.config.WebSecurityProperties;
import net.sunday.cloud.base.security.filter.TokenAuthenticationFilter;
import net.sunday.cloud.base.security.handler.CustomAccessDeniedHandler;
import net.sunday.cloud.base.security.handler.CustomAuthenticationEntryPoint;
import net.sunday.cloud.base.security.permission.SecurityFrameworkPermissionEvaluator;
import net.sunday.cloud.base.security.service.UserDetailsServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * web security 自动装配
 *
 * @see org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
 */

@AutoConfiguration(before = SecurityAutoConfiguration.class)
@EnableConfigurationProperties({WebSecurityProperties.class, RestWebProperties.class})
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
     * Token 认证过滤器 Bean
     */
    @Bean
    public TokenAuthenticationFilter authenticationTokenFilter() {
        return new TokenAuthenticationFilter();
    }

    /**
     * security框架权限验证器 Bean
     */
    @Bean("sf")
    public SecurityFrameworkPermissionEvaluator securityFrameworkPermissionEvaluator() {
        return new SecurityFrameworkPermissionEvaluator();
    }


    /**
     * 自定义权限拒绝处理器
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    /**
     * 自定义认证 端点
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
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

    /**
     * 注入 认证管理器
     */
    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
