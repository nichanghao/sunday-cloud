package net.sunday.cloud.system;


import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;


@Slf4j
@EnableAsync
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("net.sunday.cloud.system.repository.mapper")
@EnableDubbo(scanBasePackages = "net.sunday.cloud.system.api")
public class SystemApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SystemApplication.class, args);
        log.info(context.getId() + " started...");
    }
}