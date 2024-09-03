package net.sunday.cloud.base.monitor;

import jakarta.servlet.Servlet;
import net.sunday.cloud.base.monitor.filter.TraceWebFluxFilter;
import net.sunday.cloud.base.monitor.filter.TraceWebMvcFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AutoConfiguration
public class CustomTraceAutoConfiguration {


    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnClass({Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class})
    protected static class TraceWebMvcConfiguration {

        @Bean
        public TraceWebMvcFilter traceWebmvcFilter() {
            return new TraceWebMvcFilter();
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnClass(WebFluxConfigurer.class)
    protected static class TraceWebFluxConfiguration {

        @Bean
        public TraceWebFluxFilter traceWebFluxFilter() {
            return new TraceWebFluxFilter();
        }
    }


}
