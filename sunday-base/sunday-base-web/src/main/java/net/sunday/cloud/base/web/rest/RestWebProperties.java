package net.sunday.cloud.base.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;

/**
 * 为 admin/app controller 提供 RESTFUL API 的统一前缀和包路径规则
 */
@Data
@Validated
@ConfigurationProperties(prefix = "rest.web")
public class RestWebProperties {

    @NotNull(message = "APP API 不能为空")
    private Controller appApi = new Controller("/app-api", "**.controller.app.**");
    @NotNull(message = "Admin API 不能为空")
    private Controller adminApi = new Controller("/admin-api", "**.controller.admin.**");

    @Valid
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Controller {

        /**
         * 现所有 Controller 提供的 RESTFul API 的统一前缀
         *
         * @see net.sunday.cloud.base.web.WebAutoConfiguration#configurePathMatch(PathMatchConfigurer)
         */
        @NotEmpty(message = "API 前缀不能为空")
        private String prefix;

        /**
         * Controller 所在包的 Ant 路径规则
         */
        @NotEmpty(message = "Controller 所在包不能为空")
        private String packagePattern;

    }
}
