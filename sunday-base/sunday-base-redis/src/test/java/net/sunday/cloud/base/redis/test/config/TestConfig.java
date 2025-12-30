package net.sunday.cloud.base.redis.test.config;

import net.sunday.cloud.base.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = RedisAutoConfiguration.class)
public class TestConfig {
}
