package com.liftoff.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Lopi2Application {

    public static void main(String[] args) {
        SpringApplication.run(Lopi2Application.class, args);
    }

}
