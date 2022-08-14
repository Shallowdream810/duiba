package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 * Hello world!
 */
@SpringBootApplication
public class Consumer {
    public static void main(String[] args) {
        SpringApplication.run(Consumer.class, args);
    }

    @PostConstruct
    public void init() {
        System.out.println("测试初始 =========化应用程序");
    }
}
