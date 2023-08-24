package com.example.bai3.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {
    @Bean
    public void hello() {
        System.out.println("hello");
    }
}
