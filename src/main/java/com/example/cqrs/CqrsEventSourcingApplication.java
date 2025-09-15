package com.example.cqrs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CqrsEventSourcingApplication {
    public static void main(String[] args) {
        SpringApplication.run(CqrsEventSourcingApplication.class, args);
    }
}