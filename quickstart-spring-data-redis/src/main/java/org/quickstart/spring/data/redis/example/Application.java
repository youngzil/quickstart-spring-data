package org.quickstart.spring.data.redis.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.quickstart.spring.boot.redis.example")
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication application = new SpringApplication(Application.class);
        application.run(args);
    }
}
