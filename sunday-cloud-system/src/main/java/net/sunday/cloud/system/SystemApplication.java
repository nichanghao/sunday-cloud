package net.sunday.cloud.system;


import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;


@Slf4j
@EnableDubbo
@EnableDiscoveryClient
@SpringBootApplication
public class SystemApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SystemApplication.class, args);
        log.info(context.getId() + " started...");
    }
}