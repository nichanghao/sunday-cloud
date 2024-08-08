package net.sunday.cloud.base.security.config;

import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class WebSecurityConfiguration {

    @Resource
    private WebSecurityProperties webSecurityProperties;

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                // 开启跨域支持
                .cors(Customizer.withDefaults())
                // 不使用 Session，不需要csrf
                .csrf(AbstractHttpConfigurer::disable)
                // 使用无状态的 token 认证
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 配置不需要登录的请求
                .authorizeHttpRequests(c -> c.requestMatchers(webSecurityProperties.getPermitUrls().toArray(new String[0])).permitAll())
                // 剩余所有请求都需要认证
                .authorizeHttpRequests(c -> c.anyRequest().authenticated())
        ;


        return httpSecurity.build();
    }
}
