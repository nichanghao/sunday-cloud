package net.sunday.cloud.system;


import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;


@Slf4j
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("net.sunday.cloud.system.mapper")
@EnableDubbo(scanBasePackages = "net.sunday.cloud.system.api")
public class SystemApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SystemApplication.class, args);
        System.out.println("ddd: " + context.getBean(PasswordEncoder.class).encode("123456"));
        log.info(context.getId() + " started...");
    }
}