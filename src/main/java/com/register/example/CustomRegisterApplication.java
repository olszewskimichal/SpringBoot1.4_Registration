package com.register.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class CustomRegisterApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomRegisterApplication.class, args);
    }
}
