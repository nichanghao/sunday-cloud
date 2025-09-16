package net.sunday.cloud.base.redis.test;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

@SpringBootTest(classes = {RedisAutoConfiguration.class})
public class RedisTemplateTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    @SneakyThrows
    void test() {

        ClassPathResource resource = new ClassPathResource("lua/check_repeat.lua");
        String script = Files.readString(resource.getFile().toPath(), StandardCharsets.UTF_8);

        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(script);
        redisScript.setResultType(String.class); // 根据你的脚本返回类型设置

        String key = "roomId:68911e30d3400ffa7f639f83";
        String member = "123456";
        String score = String.valueOf(System.currentTimeMillis());
        String window = String.valueOf(1000 * 3);
        // 10 seconds
        String ttl = "10";


        String result = callScript(redisScript, key, member, score, window, ttl);
        Assertions.assertNull(result);

        score = String.valueOf(System.currentTimeMillis());
        result = callScript(redisScript, key, member, score, window, ttl);
        Assertions.assertNotNull(result);

        Thread.sleep(Duration.ofSeconds(4));
        score = String.valueOf(System.currentTimeMillis());
        result = callScript(redisScript, key, member, score, window, ttl);
        Assertions.assertNull(result);

    }

    String callScript(DefaultRedisScript<String> redisScript,
                      String key,
                      String member,
                      String score,
                      String window,
                      String ttl) {

        List<String> keys = Collections.singletonList(key);
        // 参数顺序要和脚本里 ARGV 对应
        return stringRedisTemplate.execute(redisScript, keys, member, score, window, ttl);
    }


}
