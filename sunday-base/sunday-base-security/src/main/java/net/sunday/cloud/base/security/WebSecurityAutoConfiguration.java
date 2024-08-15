package net.sunday.cloud.base.security;

import net.sunday.cloud.base.security.config.WebSecurityConfiguration;
import net.sunday.cloud.base.security.config.WebSecurityProperties;
import net.sunday.cloud.base.security.filter.TokenAuthenticationFilter;
import net.sunday.cloud.base.security.handler.CustomAccessDeniedHandler;
import net.sunday.cloud.base.security.handler.CustomAuthenticationEntryPoint;
import net.sunday.cloud.base.security.service.UserDetailsServiceImpl;
import net.sunday.cloud.system.api.auth.AuthApi;
import net.sunday.cloud.system.api.user.SysUserApi;
import org.apache.dubbo.config.spring.ReferenceBean;
import org.apache.dubbo.config.spring.reference.ReferenceBeanBuilder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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

@AutoConfiguration()
@AutoConfigureOrder(-1)
@EnableConfigurationProperties(WebSecurityProperties.class)
@Import({WebSecurityConfiguration.class})
public class WebSecurityAutoConfiguration {

    /**
     * 注入 dubbo sysUser 远程服务
     */
    @Bean
    @ConditionalOnClass(ReferenceBean.class)
    @ConditionalOnMissingBean(SysUserApi.class)
    public ReferenceBean<SysUserApi> sysUserApiReferenceBean() {
        return new ReferenceBeanBuilder().setInterface(SysUserApi.class).build();
    }

    /**
     * 加载用户信息服务
     */
    @Bean
    public UserDetailsServiceImpl userDetailsService(SysUserApi sysUserApi) {
        return new UserDetailsServiceImpl(sysUserApi);
    }

    /**
     * 注入 dubbo auth 远程服务
     */
    @Bean
    @ConditionalOnClass(ReferenceBean.class)
    @ConditionalOnMissingBean(AuthApi.class)
    public ReferenceBean<AuthApi> authApiReferenceBean() {
        return new ReferenceBeanBuilder().setInterface(AuthApi.class).build();
    }

    /**
     * Token 认证过滤器 Bean
     */
    @Bean
    public TokenAuthenticationFilter authenticationTokenFilter(AuthApi authApi) {
        return new TokenAuthenticationFilter(authApi);
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
