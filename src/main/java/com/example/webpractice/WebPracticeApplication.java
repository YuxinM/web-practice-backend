package com.example.webpractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebPracticeApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(WebPracticeApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
