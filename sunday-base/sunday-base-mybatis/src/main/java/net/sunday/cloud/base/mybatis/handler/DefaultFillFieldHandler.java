package net.sunday.cloud.base.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import net.sunday.cloud.base.mybatis.entity.BaseDO;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 默认填充字段处理器
 */
public class DefaultFillFieldHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        if (Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof BaseDO baseDO) {

            LocalDateTime current = LocalDateTime.now();

            // 更新时间为空，则以当前时间为更新时间
            if (Objects.isNull(baseDO.getUpdateTime())) {
                baseDO.setUpdateTime(current);
            }

            if (Objects.isNull(baseDO.getUpdater())) {
                // todo 从上下文中获取当前登录用户
                Long userId = 1L;
                baseDO.setUpdater(userId.toString());
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.insertFill(metaObject);
    }
}
