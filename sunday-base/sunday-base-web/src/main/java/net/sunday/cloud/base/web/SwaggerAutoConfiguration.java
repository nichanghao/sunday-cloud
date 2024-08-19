package net.sunday.cloud.base.web;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import net.sunday.cloud.base.web.swagger.SwaggerInfoProperties;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

import static net.sunday.cloud.base.common.constant.SecurityConstants.AUTHORIZATION_BEARER;

/**
 * Swagger 自动配置类，基于 OpenAPI + Springdoc 实现。
 */
@AutoConfiguration
@ConditionalOnClass({OpenAPI.class})
@EnableConfigurationProperties(SwaggerInfoProperties.class)
@ConditionalOnProperty(prefix = "springdoc.api-docs", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SwaggerAutoConfiguration {


    /**
     * 自定义 OpenAPI 配置
     *
     * @link <a href="https://doc.xiaominfo.com/docs/blog/add-authorization-header">...</a>
     */
    @Bean
    public OpenAPI customOpenAPI(SwaggerInfoProperties properties) {

        return new OpenAPI()
                // 接口信息
                .info(buildInfo(properties))
                // 接口安全配置
                .components(new Components().addSecuritySchemes(HttpHeaders.AUTHORIZATION,
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme(AUTHORIZATION_BEARER)))
                .addSecurityItem(new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION));
    }

    /**
     * admin-api 接口分组
     */
    @Bean
    public GroupedOpenApi adminGroupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("admin")
                .pathsToMatch("/admin-api/**")
                .addOperationCustomizer((operation, handlerMethod) -> operation
                        .addParametersItem(buildSecurityHeaderParameter()))
                .build();
    }

    /**
     * app-api 接口分组
     */
    @Bean
    public GroupedOpenApi appGroupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("app")
                .pathsToMatch("/app-api/**")
                .addOperationCustomizer((operation, handlerMethod) -> operation
                        .addParametersItem(buildSecurityHeaderParameter()))
                .build();
    }

    /**
     * auth 接口分组
     */
    @Bean
    public GroupedOpenApi authGroupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("auth")
                .pathsToMatch("/auth/**")
                .build();
    }


    /**
     * API 摘要信息
     */
    private Info buildInfo(SwaggerInfoProperties properties) {
        return new Info()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .version(properties.getVersion())
                .contact(new Contact().name(properties.getContact().getName())
                        .url(properties.getContact().getUrl()).email(properties.getContact().getEmail()))
                .license(new License().name(properties.getLicense().getName()).url(properties.getLicense().getUrl()));
    }

    /**
     * 构建 Authorization 认证请求头参数
     */
    private static Parameter buildSecurityHeaderParameter() {
        return new Parameter()
                .name(HttpHeaders.AUTHORIZATION)
                .description("认证 Token")
                .in(String.valueOf(SecurityScheme.In.HEADER))
                .schema(new StringSchema()._default("Bearer xxx").description("认证 Token"));
    }


}
