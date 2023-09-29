package com.practicalTask1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@EnableWebMvc
@Configuration
@SpringBootApplication
public class PracticalTask3Application {

    public static void main(String[] args) {
        SpringApplication.run(PracticalTask3Application.class, args);
    }

}
