package net.sunday.cloud.base.web;


import net.sunday.cloud.base.web.advice.GlobalExceptionAdvice;
import net.sunday.cloud.base.web.advice.RequestBodyLogAdvice;
import net.sunday.cloud.base.web.advice.ResponseLogAdvice;
import net.sunday.cloud.base.web.interceptor.ExecuteTimeInterceptor;
import net.sunday.cloud.base.web.jackson.JacksonBeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET;


@AutoConfiguration
@ConditionalOnWebApplication(type = SERVLET)
@Import({RequestBodyLogAdvice.class, ResponseLogAdvice.class, GlobalExceptionAdvice.class})
public class WebAutoConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ExecuteTimeInterceptor(2000L, true));
    }

    @Bean
    public JacksonBeanPostProcessor jacksonBeanPostProcessor() {
        return new JacksonBeanPostProcessor();
    }

}
