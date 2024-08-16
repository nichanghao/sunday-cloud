package net.sunday.cloud.base.web;


import jakarta.annotation.Resource;
import net.sunday.cloud.base.web.advice.GlobalExceptionAdvice;
import net.sunday.cloud.base.web.advice.RequestBodyLogAdvice;
import net.sunday.cloud.base.web.advice.ResponseLogAdvice;
import net.sunday.cloud.base.web.interceptor.ExecuteTimeInterceptor;
import net.sunday.cloud.base.web.jackson.JacksonBeanPostProcessor;
import net.sunday.cloud.base.web.rest.RestWebProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.lang.NonNull;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET;


@AutoConfiguration
@ConditionalOnWebApplication(type = SERVLET)
@EnableConfigurationProperties(RestWebProperties.class)
@Import({RequestBodyLogAdvice.class, ResponseLogAdvice.class, GlobalExceptionAdvice.class})
public class WebAutoConfiguration implements WebMvcConfigurer {

    @Resource
    private RestWebProperties restWebProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ExecuteTimeInterceptor(2000L, true));
    }

    @Override
    public void configurePathMatch(@NonNull PathMatchConfigurer configurer) {
        configurePathMatch(configurer, restWebProperties.getAdminApi());
        configurePathMatch(configurer, restWebProperties.getAppApi());
    }

    /**
     * 设置 REST API 前缀，仅仅匹配 controller 包路径
     */
    private void configurePathMatch(PathMatchConfigurer configurer, RestWebProperties.Controller controller) {
        AntPathMatcher antPathMatcher = new AntPathMatcher(".");
        configurer.addPathPrefix(controller.getPrefix(), clazz -> clazz.isAnnotationPresent(RestController.class)
                && antPathMatcher.match(controller.getPackagePattern(), clazz.getPackage().getName()));
    }

    /**
     * jackson 序列化配置
     */
    @Bean
    public JacksonBeanPostProcessor jacksonBeanPostProcessor() {
        return new JacksonBeanPostProcessor();
    }

}
