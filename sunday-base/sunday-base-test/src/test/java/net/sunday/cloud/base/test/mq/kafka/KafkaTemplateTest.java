package net.sunday.cloud.base.test.mq.kafka;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * test kafka template send and receive message
 */

@Slf4j
@SpringBootTest(classes = {KafkaAutoConfiguration.class})
@EnabledIf(expression = "#{environment['spring.profiles.active'] == 'local'}",
        // 控制执行 表达式前预加载上下文
        loadContext = true)
@Import(KafkaTemplateTest.KafkaMessageListener.class)
public class KafkaTemplateTest {

    @Autowired
    private KafkaTemplate<Object, Object> kafkaTemplate;

    private static final String topic = "test_topic";

    private static final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();

    @Test
    public void testSend() throws InterruptedException {


        kafkaTemplate.send(topic, "test_message")
                .whenCompleteAsync((result, ex) -> {
                    if (ex == null) {
                        // 消息发送成功的逻辑
                        log.info("发送 topic: {} success, record: {}", topic, result.getProducerRecord());
                    } else {
                        // 消息发送失败的逻辑
                        log.error("发送 topic: {} error: {}", topic, ex.getMessage(), ex);
                    }
                });

        log.info("消费消息数据：{}", queue.poll(10, TimeUnit.SECONDS));
    }

    /**
     * kafka 消息监听器
     */
    static class KafkaMessageListener {

        @KafkaListener(topics = topic)
        public void listener(String message) {
            queue.add(message);
        }
    }
}
