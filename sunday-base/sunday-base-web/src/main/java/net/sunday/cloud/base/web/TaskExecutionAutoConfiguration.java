package net.sunday.cloud.base.web;

import net.sunday.cloud.base.web.decorator.TransmitTaskDecorator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnThreading;
import org.springframework.boot.autoconfigure.thread.Threading;
import org.springframework.boot.task.SimpleAsyncTaskExecutorCustomizer;
import org.springframework.boot.task.ThreadPoolTaskExecutorCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * spring 内置 task execute 自动配置类
 *
 * @see org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration
 */

@AutoConfiguration(before = org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration.class)
public class TaskExecutionAutoConfiguration {

    private static final String THREAD_NAME_PREFIX = "async-task-";

    @Bean
    @ConditionalOnThreading(Threading.PLATFORM)
    public ThreadPoolTaskExecutorCustomizer threadPoolTaskExecutorCustomizer() {
        return taskExecutor -> {
            // 设置自定义 TaskDecorator
            taskExecutor.setTaskDecorator(new TransmitTaskDecorator());
            taskExecutor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        };
    }

    @Bean
    @ConditionalOnThreading(Threading.VIRTUAL)
    public SimpleAsyncTaskExecutorCustomizer simpleAsyncTaskExecutorCustomizer() {
        return taskExecutor -> {
            // 设置自定义 TaskDecorator
            taskExecutor.setTaskDecorator(new TransmitTaskDecorator());
            taskExecutor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        };
    }
}
