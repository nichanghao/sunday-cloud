package net.sunday.cloud.system.repository.cache.caffeine;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.AllArgsConstructor;
import net.sunday.cloud.base.common.enums.CommonStatusEnum;
import net.sunday.cloud.base.common.util.collection.CollectionUtils;
import net.sunday.cloud.system.model.RoleDO;
import net.sunday.cloud.system.repository.mapper.RoleMapper;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色状态缓存 DAO
 */
@Repository
@AllArgsConstructor
public class RoleStatusCaffeineDAO {

    private final RoleMapper roleMapper;


    /**
     * KEY: 角色id
     * VALUE: 角色状态
     */
    private static final Cache<Long, Boolean> CACHE = Caffeine.newBuilder()
            .maximumSize(10_0000)
            .initialCapacity(1_0000)
            .build();


    public Boolean get(Long key) {
        return CACHE.get(key, v -> {

            RoleDO roleDO = roleMapper.selectOne(Wrappers.
                    <RoleDO>lambdaQuery().eq(RoleDO::getId, key).select(RoleDO::getStatus));

            return Objects.equals(roleDO.getStatus(), CommonStatusEnum.ENABLE.ordinal());

        });
    }

    public Map<Long, Boolean> getAll(Collection<Long> keys) {
        return CACHE.getAll(keys, values -> {
            List<RoleDO> roleList = roleMapper.selectList(Wrappers
                    .<RoleDO>lambdaQuery().in(RoleDO::getId, keys).select(RoleDO::getStatus, RoleDO::getId));
            if (CollectionUtils.isEmpty(roleList)) {
                return Collections.emptyMap();
            }

            return roleList.stream().collect(Collectors.toMap(RoleDO::getId,
                    roleDO -> Objects.equals(roleDO.getStatus(), CommonStatusEnum.ENABLE.ordinal())));
        });

    }

    public void update(Long key, Boolean value) {
        if (key == null || value == null) {
            return;
        }

        CACHE.put(key, value);
    }


}
