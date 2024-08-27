package net.sunday.cloud.system.repository.cache.caffeine;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.Resource;
import net.sunday.cloud.base.common.enums.CommonStatusEnum;
import net.sunday.cloud.base.common.util.collection.CollectionUtils;
import net.sunday.cloud.system.model.MenuDO;
import net.sunday.cloud.system.repository.mapper.MenuMapper;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 菜单状态缓存 DAO
 */
@Repository
public class MenuStatusCaffeineDAO {

    @Resource
    private MenuMapper menuMapper;

    /**
     * KEY: 角色id
     * VALUE: 角色状态
     */
    private static final Cache<Long, Boolean> CACHE = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .initialCapacity(100)
            .build();


    public Boolean get(Long key) {
        return CACHE.get(key, v -> {

            MenuDO menu = menuMapper.selectOne(Wrappers.
                    <MenuDO>lambdaQuery().eq(MenuDO::getId, key).select(MenuDO::getStatus));

            return Objects.equals(menu.getStatus(), CommonStatusEnum.ENABLE.ordinal());

        });
    }

    public Map<Long, Boolean> getAll(Collection<Long> keys) {
        return CACHE.getAll(keys, values -> {
            List<MenuDO> menuList = menuMapper.selectList(Wrappers
                    .<MenuDO>lambdaQuery().in(MenuDO::getId, keys).select(MenuDO::getStatus, MenuDO::getId));
            if (CollectionUtils.isEmpty(menuList)) {
                return Collections.emptyMap();
            }

            return menuList.stream().collect(Collectors.toMap(MenuDO::getId,
                    roleDO -> Objects.equals(roleDO.getStatus(), CommonStatusEnum.ENABLE.ordinal())));
        });

    }

    public void invalidate(Long key) {
        if (key == null) {
            return;
        }

        CACHE.invalidate(key);
    }

}
