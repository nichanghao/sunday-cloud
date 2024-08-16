package net.sunday.cloud.base.web.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "swagger.info")
public class SwaggerInfoProperties {

    /**
     * 标题
     */
    private String title;
    /**
     * 描述
     */
    private String description;

    /**
     * 版本
     */
    private String version;

    /**
     * 联系人信息
     */
    private Contact contact = new Contact();


    @Data
    public static class Contact {
        /**
         * 作者
         */
        private String name;
        /**
         * url
         */
        private String url;
        /**
         * email
         */
        private String email;
    }

}
