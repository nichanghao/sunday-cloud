package net.sunday.cloud.base.redis.distribute;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;

/**
 * 使用`redis`实现简单的分布式锁，注意：没有实现`watchDog`续锁机制，需要自行评估加锁时长
 */

@Slf4j
@Component
public class SimpleRedisDistributedLock {


    private final StringRedisTemplate redis;
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;

    static {
        UNLOCK_SCRIPT = new DefaultRedisScript<>();
        UNLOCK_SCRIPT.setScriptText(
                "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                        "    return redis.call('del', KEYS[1]) " +
                        "else " +
                        "    return 0 " +
                        "end"
        );
        UNLOCK_SCRIPT.setResultType(Long.class);
    }

    public SimpleRedisDistributedLock(StringRedisTemplate redis) {
        this.redis = redis;
    }

    /**
     * 非阻塞尝试获取锁（立即返回）
     *
     * @param key    锁 key
     * @param expire 锁过期时间
     * @return 获取成功返回 true
     */
    public boolean tryLock(String key, Duration expire) {
        String lockValue = UUID.randomUUID().toString();
        Boolean success = redis.opsForValue().setIfAbsent(key, lockValue, expire);
        if (Boolean.TRUE.equals(success)) {
            LockHolder.set(lockValue);
            return true;
        }
        return false;
    }

    /**
     * 带等待的 tryLock（循环尝试直到超时）
     *
     * @param lockKey 锁 key
     * @param wait    最长等待时长
     * @param expire  锁的过期时间
     * @return 获取成功返回 true
     */
    public boolean lock(String lockKey, Duration wait, Duration expire) {
        String lockValue = UUID.randomUUID().toString();
        boolean locked = false;
        int count = 0;
        long end = System.currentTimeMillis() + wait.toMillis();
        try {
            // 最多尝试1000次，避免长轮训
            while (System.currentTimeMillis() < end && count++ < 1000) {
                Boolean ok = redis.opsForValue().setIfAbsent(lockKey, lockValue, expire);
                if (Boolean.TRUE.equals(ok)) {
                    locked = true;
                    // 保存 lockValue 到线程上下文，以便最终释放时校验
                    LockHolder.set(lockValue);
                    return true;
                }
                // 简单退避，处理中断
                try {
                    Thread.sleep(Math.min(10, count * 5L));
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            log.error("[SimpleRedisDistributedLock.tryLock({})] error", lockKey, e);
        }

        return locked;
    }

    /**
     * 释放锁（安全释放，只有 value 匹配才删除）
     * 如果当前线程没有记录该 key 的 value（即没持有锁），不会尝试删除
     */
    public boolean unlock(String lockKey) {
        String lockValue = LockHolder.get();
        if (lockValue == null) {
            // 没有本地持有记录，不尝试删除
            return false;
        }
        try {
            Long res = redis.execute(UNLOCK_SCRIPT, Collections.singletonList(lockKey), lockValue);
            return Long.valueOf(1L).equals(res);
        } finally {
            LockHolder.remove();
        }
    }


    static class LockHolder {
        private static final ThreadLocal<String> TL = new ThreadLocal<>();

        static void set(String v) {
            TL.set(v);
        }

        static String get() {
            return TL.get();
        }

        static void remove() {
            TL.remove();
        }
    }


}


