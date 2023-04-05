package com.klbr184;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class ChatgGptProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatgGptProjectApplication.class, args);
    }

}
