package com.practicalTask2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@EnableWebMvc
@Configuration
@SpringBootApplication
public class PracticalTask2Application {

    public static void main(String[] args) {
        SpringApplication.run(PracticalTask2Application.class, args);
    }

}
