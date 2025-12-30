package net.sunday.cloud.base.redis.test;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sunday.cloud.base.redis.distribute.SimpleRedisDistributedLock;
import net.sunday.cloud.base.redis.test.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

@Slf4j
@SpringBootTest(classes = {TestConfig.class})
public class SimpleRedisDistributedLockTest {

    @Autowired
    private SimpleRedisDistributedLock simpleRedisDistributedLock;

    private static final String LOCK_KEY = "lock_key";


    @Test
    void testLock() {
        this.execute(() -> simpleRedisDistributedLock.lock(LOCK_KEY, Duration.ofSeconds(10), Duration.ofSeconds(10)));
    }

    @Test
    void testTryLock() {
        this.execute(() -> simpleRedisDistributedLock.tryLock(LOCK_KEY, Duration.ofSeconds(10)));
    }

    @SneakyThrows
    private void execute(BooleanSupplier supplier) {
        try (ExecutorService executorService = Executors.newFixedThreadPool(10)) {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(Duration.ofSeconds(1));
                executorService.execute(() -> {
                    boolean lock = supplier.getAsBoolean();
                    if (!lock) {
                        log.info("Thread {} failed to lock.", Thread.currentThread().getName());
                        return;
                    } else {
                        log.info("Thread {} successfully locked.", Thread.currentThread().getName());
                    }

                    // mock business logic
                    try {
                        Thread.sleep(Duration.ofSeconds(5));
                    } catch (InterruptedException ignored) {

                    }

                    if (simpleRedisDistributedLock.unlock(LOCK_KEY)) {
                        log.info("Thread {} unlocked.", Thread.currentThread().getName());
                    }
                });
            }

            // 停止接受新任务
            executorService.shutdown();
            if (executorService.awaitTermination(5, TimeUnit.MINUTES)) {
                log.info("all tasks was finished.");
            }

        }
    }

}
