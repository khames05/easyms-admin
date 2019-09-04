package com.easyms.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAdminServer
@SpringBootApplication
public class EasymsAdminServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasymsAdminServerApplication.class, args);
    }
}
