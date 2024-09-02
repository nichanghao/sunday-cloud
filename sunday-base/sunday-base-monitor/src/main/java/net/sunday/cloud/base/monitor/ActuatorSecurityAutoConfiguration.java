package net.sunday.cloud.base.monitor;

import net.sunday.cloud.base.common.entity.security.AuthorizeRequestsCustomizer;
import net.sunday.cloud.base.security.WebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@ConditionalOnClass(SecurityFilterChain.class)
@AutoConfiguration(before = WebSecurityAutoConfiguration.class)
public class ActuatorSecurityAutoConfiguration {

    @Bean("actuatorAuthorizeRequestsCustomizer")
    public AuthorizeRequestsCustomizer authorizeRequestsCustomizer() {
        return new AuthorizeRequestsCustomizer() {

            @Override
            public void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
                registry.requestMatchers("/actuator/health").permitAll()
                ;
            }

        };
    }
}
