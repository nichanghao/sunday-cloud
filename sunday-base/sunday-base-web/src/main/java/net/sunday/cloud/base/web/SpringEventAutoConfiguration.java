package net.sunday.cloud.base.web;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.TaskExecutor;

import static org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME;
import static org.springframework.context.support.AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME;

/**
 * spring event auto configuration
 */
@AutoConfiguration(after = TaskExecutionAutoConfiguration.class)
public class SpringEventAutoConfiguration {

    @Bean(name = APPLICATION_EVENT_MULTICASTER_BEAN_NAME)
    @ConditionalOnBean(value = TaskExecutor.class, name = APPLICATION_TASK_EXECUTOR_BEAN_NAME)
    public SimpleApplicationEventMulticaster simpleApplicationEventMulticaster(
            @Qualifier(APPLICATION_TASK_EXECUTOR_BEAN_NAME) TaskExecutor taskExecutor) {
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
        // 配置线程池, 用于异步处理事件
        eventMulticaster.setTaskExecutor(taskExecutor);
        return eventMulticaster;
    }
}
