package net.sunday.cloud.base.redis.test;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
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
    void testLua() {

        ClassPathResource resource = new ClassPathResource("lua/check_repeat.lua");
        String script = Files.readString(resource.getFile().toPath(), StandardCharsets.UTF_8);

        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(script);
        // 根据脚本返回类型设置
        redisScript.setResultType(String.class);

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
        // ensure that the parameter is of string type
        result = callScript(redisScript, key, member, score, window, ttl);
        Assertions.assertNull(result);

    }

    @Test
    void testPipeline() {
        stringRedisTemplate.opsForValue().set("key1", "value1", Duration.ofSeconds(5));
        stringRedisTemplate.opsForValue().set("key2", "value2", Duration.ofSeconds(5));

        List<Object> results = stringRedisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            StringRedisConnection stringConn = (StringRedisConnection) connection;
            stringConn.get("key1");
            stringConn.get("key2");
            // 注意：回调返回 null
            return null;
        });

        Assertions.assertEquals(2, results.size());
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
