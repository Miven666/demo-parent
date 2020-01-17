package com.miven.demo.spring.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author miven
 * @date 2020/1/17
 * @see 1.0
 */
@RestController
@SpringBootApplication
public class HelloWorldLauncher {

    public static void main(String[] args) {
        SpringApplication.run(HelloWorldLauncher.class, args);
    }

    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello World";
    }
}
