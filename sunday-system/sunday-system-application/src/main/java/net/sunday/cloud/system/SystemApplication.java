package net.sunday.cloud.system;


import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;


@Slf4j
@EnableDubbo
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("net.sunday.cloud.system.mapper")
public class SystemApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SystemApplication.class, args);
        log.info(context.getId() + " started...");
    }
}