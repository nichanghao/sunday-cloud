package net.sunday.cloud.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;


@Slf4j
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(GatewayApplication.class, args);
        log.info(context.getId() + " started...");
    }
}
