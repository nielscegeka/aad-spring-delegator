package com.example.aadspringdelegator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AadSpringDelegatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(AadSpringDelegatorApplication.class, args);
    }

}
