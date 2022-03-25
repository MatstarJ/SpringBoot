package com.matstar.ex7;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Ex7Application {

    public static void main(String[] args) {
        SpringApplication.run(Ex7Application.class, args);
    }

}
