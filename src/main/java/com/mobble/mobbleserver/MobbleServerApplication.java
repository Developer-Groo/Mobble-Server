package com.mobble.mobbleserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MobbleServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MobbleServerApplication.class, args);
    }

}
