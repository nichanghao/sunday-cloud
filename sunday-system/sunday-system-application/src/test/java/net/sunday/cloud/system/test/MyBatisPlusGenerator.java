package net.sunday.cloud.system.test;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import net.sunday.cloud.base.mybatis.entity.BaseDO;
import org.apache.ibatis.type.JdbcType;

public class MyBatisPlusGenerator {

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/sunday?useUnicode=true&characterEncoding=utf8",
                        "root", "root")
                .globalConfig(builder -> {
                    builder.author("mybatis-plus-generator")
//                            .dateType(DateType.ONLY_DATE)
                            .disableOpenDir()
                            // 指定输出目录
                            .outputDir(System.getProperty("user.dir") + "/sunday-system/sunday-system-application/src/main/java/")

                    ;
                })
                .dataSourceConfig(builder -> {
                    builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                        // tinyint转换为integer
                        if (JdbcType.TINYINT == metaInfo.getJdbcType()) {
                            return DbColumnType.INTEGER;
                        }
                        return typeRegistry.getColumnType(metaInfo);
                    });
                })

                .packageConfig(builder -> builder.parent("net.sunday.cloud")
                        .moduleName("system")
                        .entity("model")
                        .service("service")
                        .serviceImpl("service.impl")
                        .build())
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                .strategyConfig(builder ->
                        builder
                                .addInclude("sys_role_menu")
                                .entityBuilder()
                                .enableFileOverride()
                                .formatFileName("%sDO")
                                .superClass(BaseDO.class)
                                .addIgnoreColumns("deleted", "update_time", "updater")
                                .enableLombok()
                                // 不生成controller
                                .controllerBuilder().disable()
                                // 不生成mapper.xml
                                .mapperBuilder().disableMapperXml()
                                .build())
                .execute();
    }

}
