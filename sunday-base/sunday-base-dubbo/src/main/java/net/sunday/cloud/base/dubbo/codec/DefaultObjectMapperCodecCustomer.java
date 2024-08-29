package net.sunday.cloud.base.dubbo.codec;

import net.sunday.cloud.base.common.entity.auth.AuthUser;
import net.sunday.cloud.base.dubbo.codec.mixin.AuthUserMixin;
import org.apache.dubbo.spring.security.jackson.ObjectMapperCodec;
import org.apache.dubbo.spring.security.jackson.ObjectMapperCodecCustomer;

/**
 * 自定义 codec 实现
 *
 * @link <a href="https://github.com/apache/dubbo/issues/12167">...</a>
 */

public class DefaultObjectMapperCodecCustomer implements ObjectMapperCodecCustomer {
    @Override
    public void customize(ObjectMapperCodec objectMapperCodec) {
        objectMapperCodec.configureMapper(objectMapper -> {
                    objectMapper.addMixIn(AuthUser.class, AuthUserMixin.class);
                }
        );
    }

}
