package net.sunday.cloud.base.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

@Data
@Validated
@ConfigurationProperties(prefix = "web.security")
public class WebSecurityProperties {

    /**
     * PasswordEncoder 加密复杂度，越高开销越大
     */
    private Integer passwordEncoderLength = 8;

    /**
     * 免登录的 URL 列表
     */
    private List<String> permitUrls = Collections.emptyList();
}
