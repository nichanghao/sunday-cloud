package net.sunday.cloud.base.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import net.sunday.cloud.base.mybatis.handler.DefaultFillFieldHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(before = com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration.class)
public class MybatisPlusAutoConfiguration {

    /**
     * mybatis-plus插件配置
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor()); // 如果配置多个插件, 切记分页最后添加
        return mybatisPlusInterceptor;
    }

    /**
     * 默认填充字段
     */
    @Bean
    public MetaObjectHandler defaultMetaObjectHandler(){
        return new DefaultFillFieldHandler();
    }
}
