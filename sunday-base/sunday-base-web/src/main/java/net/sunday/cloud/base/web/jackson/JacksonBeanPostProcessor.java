package net.sunday.cloud.base.web.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import net.sunday.cloud.base.common.util.json.databind.NumberSerializer;
import net.sunday.cloud.base.common.util.json.databind.TimestampLocalDateTimeDeserializer;
import net.sunday.cloud.base.common.util.json.databind.TimestampLocalDateTimeSerializer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class JacksonBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        if (bean instanceof ObjectMapper objectMapper) {
            // 1.1 创建 SimpleModule 对象
            SimpleModule simpleModule = new SimpleModule();
            simpleModule
                    // 新增 Long 类型序列化规则，数值超过 2^53-1，在 JS 会出现精度丢失问题，因此 Long 自动序列化为字符串类型
                    .addSerializer(Long.class, NumberSerializer.INSTANCE)
                    .addSerializer(Long.TYPE, NumberSerializer.INSTANCE)
                    .addSerializer(LocalDate.class, LocalDateSerializer.INSTANCE)
                    .addDeserializer(LocalDate.class, LocalDateDeserializer.INSTANCE)
                    .addSerializer(LocalTime.class, LocalTimeSerializer.INSTANCE)
                    .addDeserializer(LocalTime.class, LocalTimeDeserializer.INSTANCE)
                    // 新增 LocalDateTime 时间戳序列化、反序列化
                    .addSerializer(LocalDateTime.class, TimestampLocalDateTimeSerializer.INSTANCE)
                    .addDeserializer(LocalDateTime.class, TimestampLocalDateTimeDeserializer.INSTANCE);
            // 1.2 注册到 objectMapper
            objectMapper.registerModule(simpleModule);
        }

        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }
}
