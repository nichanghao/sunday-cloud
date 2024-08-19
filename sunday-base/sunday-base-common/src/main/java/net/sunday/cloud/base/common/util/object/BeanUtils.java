package net.sunday.cloud.base.common.util.object;

import cn.hutool.core.bean.BeanUtil;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Bean 工具类
 */
public class BeanUtils {

    public static <T> T toBean(Object source, Class<T> targetClass) {
        return BeanUtil.toBean(source, targetClass);
    }

    public static <T> T toBean(Object source, Class<T> targetClass, Consumer<T> peek) {
        T target = toBean(source, targetClass);
        if (target != null) {
            peek.accept(target);
        }
        return target;
    }

    public static <S, T> List<T> toBean(List<S> source, Class<T> targetType) {
        if (source == null) {
            return null;
        }

        return source.stream().map(s -> toBean(s, targetType)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static <S, T> List<T> toBean(List<S> source, Class<T> targetType, Consumer<T> peek) {
        List<T> list = toBean(source, targetType);
        if (list != null) {
            list.forEach(peek);
        }
        return list;
    }

}